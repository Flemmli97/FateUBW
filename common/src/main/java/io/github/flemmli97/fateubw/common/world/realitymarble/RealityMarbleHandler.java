package io.github.flemmli97.fateubw.common.world.realitymarble;

import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.registry.FateAttachments;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class RealityMarbleHandler extends SavedData {

    private static final String IDENTIFIER = "RealityMarbleHandler";
    private static final Factory<RealityMarbleHandler> FACTORY = new Factory<>(RealityMarbleHandler::new, RealityMarbleHandler::new, DataFixTypes.LEVEL);

    private final Map<UUID, RealityMarbleGroup> realityMarbleGroups = new HashMap<>();
    private final Map<UUID, EntityMarbleData> entityGroupLookup = new HashMap<>();

    private final Map<ResourceKey<Level>, Int2ObjectMap<UUID>> spacingLookup = new HashMap<>();

    private RealityMarbleHandler() {
    }

    private RealityMarbleHandler(CompoundTag tag, HolderLookup.Provider provider) {
        this.load(tag);
    }

    public static RealityMarbleHandler get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(FACTORY, IDENTIFIER);
    }

    /**
     * Preloads chunks around the teleport target in a given radius.
     * Makes sense if the teleport doesn't happen immediately
     */
    public static void prepareChunks(Entity entity, ResourceKey<Level> target, double radius) {
        ServerLevel targetLevel = entity.getServer().getLevel(target);
        if (targetLevel == null)
            return;
        double scale = DimensionType.getTeleportationScale(entity.level().dimensionType(), targetLevel.dimensionType());
        Vec3 pos = entity.position().multiply(scale, 1, scale);
        ChunkPos chunk = new ChunkPos(SectionPos.blockToSectionCoord(Mth.floor(pos.x())), SectionPos.blockToSectionCoord(Mth.floor(pos.z())));
        int chunkRadius = SectionPos.blockToSectionCoord(Mth.ceil(radius));
        // We simply preload the chunks earlier to ensure faster teleportation.
        // Its fine... probably... to do this offthread since no modifications
        Thread.startVirtualThread(() -> {
            for (int x = -chunkRadius; x < chunkRadius; x++) {
                for (int z = -chunkRadius; z < chunkRadius; z++) {
                    targetLevel.getChunkSource().getChunkFuture(x + chunk.x, z + chunk.z, ChunkStatus.FULL, true);
                }
            }
        });
    }

    @Nullable
    public RealityMarbleGroup getGroupOf(Entity entity) {
        EntityMarbleData current = this.entityGroupLookup.get(entity.getUUID());
        return current == null ? null : this.getGroup(current.group());
    }

    @Nullable
    public RealityMarbleGroup getGroup(UUID id) {
        return this.realityMarbleGroups.get(id);
    }

    /**
     * Creates a reality marble group with the given entity and teleports them to the marble
     */
    public void createAndTransportTo(Entity creator, List<Entity> entities, ResourceKey<Level> targetLevel) {
        ServerLevel target = creator.getServer().getLevel(targetLevel);
        if (target == null || this.isManagingRealityMarble(creator))
            return;
        List<Entity> vehicles = new ArrayList<>();
        entities.forEach(entity -> this.addVehicles(vehicles, entity, entities));
        entities.addAll(vehicles);
        entities.remove(creator);
        RealityMarbleGroup current = this.getGroupOf(creator);
        RealityMarbleGroup group;
        if (current != null) {
            this.removeGroup(current.id());
            Pair<Integer, BlockPos> free = this.findFreePosition(targetLevel);
            // Add all entities from existing group since we need to teleport them too
            ServerLevel currentLevel = (ServerLevel) creator.level();
            current.loadChunks(currentLevel);
            current.entities().forEach(uuid -> {
                Entity entity = currentLevel.getEntity(uuid);
                if (entity != null && !entities.contains(entity)) {
                    entities.add(entity);
                }
            });
            group = new RealityMarbleGroup(UUID.randomUUID(), creator.getUUID(),
                    current.sourceLevel(),
                    current.sourcePosition(),
                    targetLevel, free.right(), free.first(), entities.stream().map(Entity::getUUID).toList());
        } else {
            Pair<Integer, BlockPos> free = this.findFreePosition(targetLevel);
            group = new RealityMarbleGroup(UUID.randomUUID(), creator.getUUID(),
                    creator.level().dimension(),
                    creator.blockPosition(),
                    targetLevel, free.right(), free.first(), entities.stream().map(Entity::getUUID).toList());
        }
        entities.forEach(entity -> this.overrideAndTransportEntity(entity, target, group));
        this.overrideAndTransportEntity(creator, target, group);
        this.addGroup(group);
        this.setDirty();
    }

    private Pair<Integer, BlockPos> findFreePosition(ResourceKey<Level> targetLevel) {
        int idx = 0;
        Int2ObjectMap<UUID> lookup = this.spacingLookup.get(targetLevel);
        while (lookup != null && lookup.containsKey(idx)) {
            idx++;
        }
        int[] coords = PositionUtil.spiralCoord(idx);
        return Pair.of(idx, new BlockPos(coords[0] * PositionUtil.SPACING, 0, coords[1] * PositionUtil.SPACING));
    }

    private void addVehicles(List<Entity> vehicles, Entity current, List<Entity> entities) {
        Entity vehicle = current.getVehicle();
        if (vehicle != null && !entities.contains(vehicle)) {
            vehicles.add(vehicle);
            this.addVehicles(vehicles, vehicle, entities);
        }
    }

    public void onEntityLoad(Entity entity) {
        RealityMarbleGroup group = this.getGroupOf(entity);
        if (group == null) {
            RealityMarbleGroup nearest = this.getNearest(entity);
            if (nearest != null) {
                nearest.entities().add(entity.getUUID());
                this.entityGroupLookup.put(entity.getUUID(), new EntityMarbleData(nearest));
                this.setDirty();
            } else {
                this.clearEntityData(entity, true);
            }
        }
    }

    private RealityMarbleGroup getNearest(Entity entity) {
        Int2ObjectMap<UUID> lookup = this.spacingLookup.get(entity.level().dimension());
        if (lookup == null)
            return null;
        for (UUID id : lookup.values()) {
            RealityMarbleGroup group = this.getGroup(id);
            if (group == null) continue;
            double distSqr = entity.position().distanceToSqr(group.targetPosition().getX() + 0.5, entity.position().y(), group.targetPosition().getZ() + 0.5);
            double size = CommonConfig.realityMarbleSize + 64;
            if (distSqr < size * size) {
                return group;
            }
        }
        return null;
    }

    private void overrideAndTransportEntity(Entity entity, ServerLevel targetLevel, RealityMarbleGroup group) {
        this.teleportEntityTo(entity, targetLevel, group.sourcePosition(), group.targetPosition());
        entity.getServer().tell(new TickTask(1, () -> this.entityGroupLookup.put(entity.getUUID(), new EntityMarbleData(group))));
    }

    /**
     * Teleport the entity to the target level.
     * The location depends on the current position of the entity in relation to the reality marbles root position
     *
     * @param center         Current center of where this entity resides.
     *                       When entering this is the location where the reality marble was used.
     *                       When exiting this is the location where the reality marbles center is.
     * @param targetPosition Target position in the target level
     */
    private void teleportEntityTo(Entity entity, ServerLevel targetLevel,
                                  BlockPos center, BlockPos targetPosition) {
        entity = entity.getRootVehicle();
        if (entity.level().dimension().equals(targetLevel.dimension())) {
            return;
        }
        Vec3 offset = entity.position().subtract(center.getX() + 0.5, 0, center.getZ() + 0.5);
        Vec3 pos = offset.add(targetPosition.getX() + 0.5, 0, targetPosition.getZ() + 0.5);
        BlockPos blockPos = BlockPos.containing(pos);
        int height = targetLevel.getChunkAt(blockPos).getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, blockPos.getX(), blockPos.getZ()) + 1;
        AABB aabb = this.collectiveBB(entity, null).toAABB()
                .move(-entity.getX(), -entity.getY(), -entity.getZ())
                .move(pos.x(), height, pos.z());
        while (!targetLevel.noCollision(entity, aabb)) {
            height++;
            aabb = aabb.move(0, 1, 0);
        }
        int finalHeight = height;
        Entity toTeleport = entity;
        entity.getServer().tell(new TickTask(1, () -> toTeleport.changeDimension(new DimensionTransition(targetLevel, new Vec3(pos.x(), finalHeight, pos.z()), Vec3.ZERO, toTeleport.getYRot(), toTeleport.getXRot(), DimensionTransition.PLACE_PORTAL_TICKET))));
    }

    private MutableAABB collectiveBB(Entity entity, MutableAABB bb) {
        bb = bb == null ? new MutableAABB(entity.getBoundingBox()) : bb.merge(entity.getBoundingBox());
        for (Entity passenger : entity.getPassengers()) {
            this.collectiveBB(passenger, bb);
        }
        return bb;
    }

    /**
     * Remove from the group but without teleporting the entity back
     */
    public void removeFromGroup(Entity entity) {
        RealityMarbleGroup current = this.getGroupOf(entity);
        if (current != null && current.creator().equals(entity.getUUID())) {
            this.deleteGroupOf(entity);
            return;
        }
        this.clearEntityData(entity, false);
    }

    public void deleteGroupOf(Entity creator) {
        RealityMarbleGroup current = this.getGroupOf(creator);
        if (current == null || !current.creator().equals(creator.getUUID()))
            return;
        ServerLevel serverLevel = (ServerLevel) creator.level();
        // Load all chunks for this group first so unloaded entities also get processed.
        current.loadChunks(serverLevel);
        Collection<UUID> entities = Set.copyOf(current.entities());
        entities.forEach(uuid -> {
            Entity entity = serverLevel.getEntity(uuid);
            if (entity != null) {
                this.clearEntityData(entity, true);
            }
        });
        this.clearEntityData(creator, true);
        this.removeGroup(current.id());
    }

    private void clearEntityData(Entity entity, boolean teleport) {
        RealityMarbleGroup current = this.getGroupOf(entity);
        if (current != null) {
            current.entities().remove(entity.getUUID());
        }
        EntityMarbleData data = this.entityGroupLookup.remove(entity.getUUID());
        FateAttachments.REALITY_MARBLE_CONSTRAINT.get().get(entity).clearConstraints();
        if (data == null) {
            return;
        }
        this.setDirty();
        ServerLevel targetLevel = entity.getServer().getLevel(data.sourceLevel());
        if (targetLevel == null || !teleport) {
            return;
        }
        this.teleportEntityTo(entity, targetLevel, data.targetPosition(), data.sourcePosition());
    }

    public boolean isManagingRealityMarble(Entity entity) {
        RealityMarbleGroup group = this.getGroupOf(entity);
        return group != null && group.creator().equals(entity.getUUID());
    }

    public boolean allowChangingDimensionsTo(Entity entity, ResourceKey<Level> target) {
        RealityMarbleGroup group = this.getGroupOf(entity);
        if (group == null)
            return true;
        return group.targetLevel().equals(target);
    }

    private void addGroup(RealityMarbleGroup group) {
        this.realityMarbleGroups.put(group.id(), group);
        this.spacingLookup.computeIfAbsent(group.targetLevel(), k -> new Int2ObjectArrayMap<>())
                .put(group.positionIndex(), group.id());
        this.setDirty();
    }

    private void removeGroup(UUID id) {
        RealityMarbleGroup current = this.realityMarbleGroups.remove(id);
        if (current != null) {
            Int2ObjectMap<UUID> lookup = this.spacingLookup.get(current.targetLevel());
            if (lookup != null) {
                lookup.remove(current.positionIndex());
            }
            this.setDirty();
        }
    }

    private void load(CompoundTag tag) {
        CompoundTag groups = tag.getCompound("groups");
        groups.getAllKeys().forEach(id -> this.addGroup(RealityMarbleGroup.CODEC.parse(NbtOps.INSTANCE, groups.get(id)).getOrThrow()));
        CompoundTag lookup = tag.getCompound("lookup");
        lookup.getAllKeys().forEach(id -> this.entityGroupLookup.put(UUID.fromString(id), EntityMarbleData.CODEC.parse(NbtOps.INSTANCE, lookup.get(id)).getOrThrow()));
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        CompoundTag groups = new CompoundTag();
        this.realityMarbleGroups.forEach((id, group) ->
                groups.put(id.toString(), RealityMarbleGroup.CODEC.encodeStart(NbtOps.INSTANCE, group).getOrThrow()));
        tag.put("groups", groups);
        CompoundTag lookup = new CompoundTag();
        this.entityGroupLookup.forEach((id, data) ->
                lookup.put(id.toString(), EntityMarbleData.CODEC.encodeStart(NbtOps.INSTANCE, data).getOrThrow()));
        tag.put("lookup", lookup);
        return tag;
    }

    public String debug() {
        return String.format("%s %s ", this.realityMarbleGroups, this.entityGroupLookup);
    }

    private static class MutableAABB {

        public double minX;
        public double minY;
        public double minZ;
        public double maxX;
        public double maxY;
        public double maxZ;

        public MutableAABB(AABB aabb) {
            this.minX = aabb.minX;
            this.minY = aabb.minY;
            this.minZ = aabb.minZ;
            this.maxX = aabb.maxX;
            this.maxY = aabb.maxY;
            this.maxZ = aabb.maxZ;
        }

        public MutableAABB merge(AABB aabb) {
            this.minX = Math.min(this.minX, aabb.minX);
            this.minY = Math.min(this.minY, aabb.minY);
            this.minZ = Math.min(this.minZ, aabb.minZ);
            this.maxX = Math.max(this.maxX, aabb.maxX);
            this.maxY = Math.max(this.maxY, aabb.maxY);
            this.maxZ = Math.max(this.maxZ, aabb.maxZ);
            return this;
        }

        public AABB toAABB() {
            return new AABB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
        }
    }
}
