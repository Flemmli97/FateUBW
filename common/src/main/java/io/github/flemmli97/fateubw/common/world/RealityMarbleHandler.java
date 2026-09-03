package io.github.flemmli97.fateubw.common.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.SectionPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.OwnableEntity;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class RealityMarbleHandler extends SavedData {

    private static final String IDENTIFIER = "RealityMarbleHandler";
    private static final Factory<RealityMarbleHandler> FACTORY = new Factory<>(RealityMarbleHandler::new, RealityMarbleHandler::new, DataFixTypes.LEVEL);

    private final Map<UUID, RealityMarbleGroup> entityGroups = new HashMap<>();
    private final Map<UUID, EntityMarbleData> entityGroupLookup = new HashMap<>();

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
        return this.entityGroups.get(id);
    }

    /**
     * Creates a reality marble group with the given entity and teleports them to the marble
     */
    public void createAndTransportTo(Entity creator, List<Entity> entities, ResourceKey<Level> targetLevel) {
        ServerLevel target = creator.getServer().getLevel(targetLevel);
        if (target == null)
            return;
        List<Entity> vehicles = new ArrayList<>();
        entities.forEach(entity -> this.addVehicles(vehicles, entity, entities));
        entities.addAll(vehicles);
        RealityMarbleGroup current = this.getGroupOf(creator);
        RealityMarbleGroup group = new RealityMarbleGroup(UUID.randomUUID(), creator.getUUID(),
                current != null ? current.sourceLevel() : creator.level().dimension(), targetLevel, entities
                .stream().map(Entity::getUUID).toList());
        entities.forEach(entity -> this.overrideAndTransportEntity(entity, target, group));
        this.overrideAndTransportEntity(creator, target, group);
        this.entityGroups.put(group.id(), group);
        this.setDirty();
    }

    private void addVehicles(List<Entity> vehicles, Entity current, List<Entity> entities) {
        Entity vehicle = current.getVehicle();
        if (vehicle != null && !entities.contains(vehicle)) {
            vehicles.add(vehicle);
            this.addVehicles(vehicles, vehicle, entities);
        }
    }

    public void onEntityLoad(Entity entity) {
        if (entity instanceof OwnableEntity ownable) {
            EntityMarbleData current = this.entityGroupLookup.get(ownable.getOwnerUUID());
            if (current != null) {
                RealityMarbleGroup group = this.getGroup(current.group());
                if (group != null) {
                    group.entities().add(entity.getUUID());
                    this.entityGroupLookup.put(entity.getUUID(), new EntityMarbleData(group.id(), group.sourceLevel()));
                    this.setDirty();
                }
            }
        }
        RealityMarbleHandler.RealityMarbleGroup group = this.getGroupOf(entity);
        if (group == null) {
            this.clearAndTeleportBack(entity);
        }
    }

    private void overrideAndTransportEntity(Entity entity, ServerLevel targetLevel, RealityMarbleGroup group) {
        RealityMarbleGroup current = this.getGroupOf(entity);
        if (current != null) {
            if (current.creator().equals(entity.getUUID())) {
                // Incase it's the creator merge the old group with the new one and warp the entities too
                this.entityGroups.remove(current.id(), group);
                current.entities().forEach(uuid -> {
                    if (!current.entities().contains(uuid)) {
                        Entity other = ((ServerLevel) entity.level()).getEntity(uuid);
                        if (other != null) {
                            this.teleportEntityTo(entity, targetLevel);
                        }
                        this.entityGroupLookup.put(uuid, new EntityMarbleData(group.id(), group.sourceLevel()));
                    }
                });
            } else {
                current.entities().remove(entity.getUUID());
            }
        }
        this.entityGroupLookup.put(entity.getUUID(), new EntityMarbleData(group.id(), group.sourceLevel()));
        this.teleportEntityTo(entity, targetLevel);
    }

    private void teleportEntityTo(Entity entity, ServerLevel targetLevel) {
        entity = entity.getRootVehicle();
        if (entity.level().dimension().equals(targetLevel.dimension()))
            return;
        double scale = DimensionType.getTeleportationScale(entity.level().dimensionType(), targetLevel.dimensionType());
        Vec3 pos = entity.position().multiply(scale, 1, scale);
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
        if (current == null)
            return;
        if (current.creator().equals(entity.getUUID())) {
            this.deleteGroupOf(entity);
        } else {
            current.entities().remove(entity.getUUID());
        }
        this.setDirty();
    }

    public void deleteGroupOf(Entity creator) {
        RealityMarbleGroup current = this.getGroupOf(creator);
        if (current == null || !current.creator().equals(creator.getUUID()))
            return;
        ServerLevel serverLevel = (ServerLevel) creator.level();
        Collection<UUID> entities = Set.copyOf(current.entities());
        entities.forEach(uuid -> {
            Entity entity = serverLevel.getEntity(uuid);
            if (entity != null) {
                this.clearAndTeleportBack(entity);
            }
        });
        this.clearAndTeleportBack(creator);
        this.entityGroups.remove(current.id());
        this.setDirty();
    }

    public void clearAndTeleportBack(Entity entity) {
        RealityMarbleGroup current = this.getGroupOf(entity);
        if (current != null) {
            current.entities().remove(entity.getUUID());
        }
        EntityMarbleData data = this.entityGroupLookup.remove(entity.getUUID());
        this.setDirty();
        if (data == null) {
            return;
        }
        ServerLevel targetLevel = entity.getServer().getLevel(data.sourceLevel());
        if (targetLevel == null) {
            return;
        }
        this.teleportEntityTo(entity, targetLevel);
    }

    public boolean isInRealityMarble(Entity entity) {
        return this.getGroupOf(entity) != null;
    }

    public boolean allowChangingDimensionsTo(Entity entity, ResourceKey<Level> target) {
        RealityMarbleGroup group = this.getGroupOf(entity);
        if (group == null)
            return true;
        return group.targetLevel().equals(target);
    }

    private void load(CompoundTag tag) {
        CompoundTag groups = tag.getCompound("groups");
        groups.getAllKeys().forEach(id -> {
            this.entityGroups.put(UUID.fromString(id), RealityMarbleGroup.CODEC.parse(NbtOps.INSTANCE, groups.get(id)).getOrThrow());
        });
        CompoundTag lookup = tag.getCompound("lookup");
        lookup.getAllKeys().forEach(id -> {
            this.entityGroupLookup.put(UUID.fromString(id), EntityMarbleData.CODEC.parse(NbtOps.INSTANCE, lookup.get(id)).getOrThrow());
        });
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        CompoundTag groups = new CompoundTag();
        this.entityGroups.forEach((id, group) ->
                groups.put(id.toString(), RealityMarbleGroup.CODEC.encodeStart(NbtOps.INSTANCE, group).getOrThrow()));
        tag.put("groups", groups);
        CompoundTag lookup = new CompoundTag();
        this.entityGroupLookup.forEach((id, data) ->
                lookup.put(id.toString(), EntityMarbleData.CODEC.encodeStart(NbtOps.INSTANCE, data).getOrThrow()));
        tag.put("lookup", lookup);
        return tag;
    }

    public record RealityMarbleGroup(UUID id, UUID creator, ResourceKey<Level> sourceLevel,
                                     ResourceKey<Level> targetLevel,
                                     Set<UUID> entities) {

        public static final Codec<RealityMarbleGroup> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(UUIDUtil.CODEC.fieldOf("id").forGetter(RealityMarbleGroup::id),
                                UUIDUtil.CODEC.fieldOf("creator").forGetter(RealityMarbleGroup::creator),
                                ResourceKey.codec(Registries.DIMENSION).fieldOf("source_level").forGetter(RealityMarbleGroup::sourceLevel),
                                ResourceKey.codec(Registries.DIMENSION).fieldOf("target_level").forGetter(RealityMarbleGroup::targetLevel),
                                UUIDUtil.CODEC.listOf().fieldOf("entities").forGetter(d -> List.copyOf(d.entities())))
                        .apply(instance, RealityMarbleGroup::new));

        public RealityMarbleGroup(UUID id, UUID creator, ResourceKey<Level> sourceLevel, ResourceKey<Level> targetLevel, Collection<UUID> entities) {
            this(id, creator, sourceLevel, targetLevel, new HashSet<>(entities));
        }
    }

    public record EntityMarbleData(UUID group, ResourceKey<Level> sourceLevel) {

        public static final Codec<EntityMarbleData> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(UUIDUtil.CODEC.fieldOf("group").forGetter(EntityMarbleData::group),
                                ResourceKey.codec(Registries.DIMENSION).fieldOf("source_level").forGetter(EntityMarbleData::sourceLevel))
                        .apply(instance, EntityMarbleData::new));
    }

    private static class MutableAABB {

        public double minX;
        public double minY;
        public double minZ;
        public double maxX;
        public double maxY;
        public double maxZ;

        public MutableAABB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
            this.minX = minX;
            this.minY = minY;
            this.minZ = minZ;
            this.maxX = maxX;
            this.maxY = maxY;
            this.maxZ = maxZ;
        }

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

        public MutableAABB merge(MutableAABB aabb) {
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
