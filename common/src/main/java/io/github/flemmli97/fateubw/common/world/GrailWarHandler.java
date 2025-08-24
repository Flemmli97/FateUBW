package io.github.flemmli97.fateubw.common.world;

import com.google.common.collect.ImmutableSet;
import io.github.flemmli97.fateubw.api.entity.ServantLike;
import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.datapack.EntityPropsManager;
import io.github.flemmli97.fateubw.common.registry.FateCriterionTriggers;
import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class GrailWarHandler extends SavedData {

    private static final String IDENTIFIER = "FateGrailWar";
    private static final Function<MinecraftServer, SavedData.Factory<GrailWarHandler>> FACTORY =
            server -> new Factory<>(() -> new GrailWarHandler(server), (tag, provider) -> new GrailWarHandler(server, tag, provider), DataFixTypes.LEVEL);

    private final MinecraftServer server;
    /**
     * Active participants in the current grailwar
     */
    private final Map<UUID, Participant<?>> participants = new HashMap<>();
    /**
     * The participants that have at one point joined this grailwar. Includes participants that are not eliminated
     */
    private Set<UUID> joinedParticipants = new HashSet<>();

    /**
     * Tracking what servants and classes spawned in the grailwar
     */
    private final Set<ResourceLocation> servantsTypes = new HashSet<>();
    private final Set<ResourceLocation> servantClasses = new HashSet<>();

    private Phase phase = Phase.NONE;

    private int lastGrailEndDay, joinTime;
    private int timeToNextServant;

    private Map<ChunkPos, ResourceKey<Level>> servantTickets;

    private GrailWarHandler(MinecraftServer server) {
        this.server = server;
    }

    private GrailWarHandler(MinecraftServer server, CompoundTag tag, HolderLookup.Provider provider) {
        this.server = server;
        this.load(tag);
    }

    public static GrailWarHandler get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(FACTORY.apply(server), IDENTIFIER);
    }

    private static int day(Level level) {
        return (int) ((level.getDayTime()) / 24000 % Integer.MAX_VALUE);
    }

    /**
     * Joins the grailwar as a player with the given servant
     */
    public boolean join(ServantLike<?> servant) {
        Player player = servant.getOwner();
        if (player != null) {
            JoinResult res = this.checkJoining(player);
            if (res != JoinResult.SUCCESS) {
                player.sendSystemMessage(Component.translatable(res.translationKey));
                return false;
            }
        }
        if (!this.joinInternal(player, servant)) {
            if (player != null)
                player.sendSystemMessage(Component.translatable(JoinResult.WRONG_SERVANT.translationKey));
            return false;
        }
        this.setDirty();
        return true;
    }

    public boolean forceStartGrailWar() {
        if (this.phase == Phase.NONE) {
            this.setupStart();
            return true;
        }
        return false;
    }

    private boolean joinInternal(@Nullable Player player, ServantLike<?> servant) {
        Participant<?> participant = new Participant<>(servant, player);
        if (!this.participants.containsKey(participant.getId()) && this.canSpawnServant(servant)) {
            this.participants.put(participant.getId(), participant);
            this.servantClasses.add(servant.props().servantClass());
            this.servantsTypes.add(BuiltInRegistries.ENTITY_TYPE.getKey(servant.get().getType()));
            this.joinedParticipants.add(participant.getId());
            return true;
        }
        return false;
    }

    public JoinResult checkJoining(Player player) {
        if (this.phase != Phase.JOIN) {
            return JoinResult.WRONG_STATE;
        }
        if (this.isFull()) {
            return JoinResult.FULL;
        }
        if (this.joinedParticipants.contains(player.getUUID())) {
            return JoinResult.JOINED;
        }
        if (!this.canSpawnMoreServants(player.level())) {
            return JoinResult.NO_MORE_SERVANTS;
        }
        return JoinResult.SUCCESS;
    }

    public boolean isFull() {
        if (this.phase == Phase.JOIN)
            return this.participants.size() >= CommonConfig.maxPlayer;
        return this.joinedParticipants.size() >= CommonConfig.maxPlayer;
    }

    public boolean isParticipant(Entity entity) {
        if (entity instanceof ServantLike<?> servant && servant.getOwnerUUID() != null)
            return this.participants.containsKey(servant.getOwnerUUID());
        return this.participants.containsKey(entity.getUUID());
    }

    public Optional<ServantLike<?>> getServant(ServerPlayer player) {
        Participant<?> participant = this.participants.get(player.getUUID());
        if (participant != null)
            return Optional.ofNullable(participant.getServant(this.server));
        return Optional.empty();
    }

    /**
     * The participating players
     */
    public Set<UUID> players(boolean valid) {
        return ImmutableSet.copyOf(this.participants.entrySet().stream().filter(p ->
                        p.getValue().isPlayerParticipant() && (!valid || p.getValue().valid(this.server)))
                .map(Map.Entry::getKey)
                .toList());
    }

    public void tick(ServerLevel level) {
        this.loadTickets(level);
        switch (this.phase) {
            case NONE -> {
                if (Math.abs(day(level) - this.lastGrailEndDay) > CommonConfig.grailWarCooldown
                        && level.getDayTime() % 24000 == 1) {
                    this.setupStart();
                }
            }
            case JOIN -> {
                if (--this.joinTime <= 0)
                    this.start();
                // Remove invalid servants during join times too. joinedParticipants is not updated to prevent players killing their servants and try to rejoin
                Set<UUID> invalid = new HashSet<>();
                this.participants.forEach((id, participant) -> {
                    if (!participant.valid(this.server)) {
                        invalid.add(id);
                        Entity servant = participant.getServant(this.server);
                        if (servant != null)
                            servant.hurt(FateDamageTypes.grail(servant.registryAccess()), Integer.MAX_VALUE);
                    }
                });
                invalid.forEach(this.participants::remove);
            }
            case ACTIVE -> {
                if (CommonConfig.fillMissingSlots && --this.timeToNextServant <= 0) {
                    this.trySpawnNPCServant(level);
                }
                this.runGrailWar();
            }
        }
        this.setDirty();
    }

    private void loadTickets(ServerLevel level) {
        if (this.servantTickets != null) {
            this.servantTickets.forEach((c, r) -> {
                if (level.dimension().equals(r))
                    level.getChunkSource().addRegionTicket(ServantLike.TRACKINGTICKET, c, 2, c);
                else {
                    ServerLevel w = this.server.getLevel(r);
                    w.getChunkSource().addRegionTicket(ServantLike.TRACKINGTICKET, c, 2, c);
                }
            });
            this.servantTickets = null;
        }
    }

    private void setupStart() {
        this.joinTime = CommonConfig.joinTime;
        this.phase = Phase.JOIN;
        this.server.getPlayerList().broadcastSystemMessage(Component.translatable("fateubw.chat.grailwar.init", this.joinTime / 20)
                .withStyle(ChatFormatting.LIGHT_PURPLE), true);
    }

    private void start() {
        this.phase = Phase.ACTIVE;
        Set<UUID> players = this.players(true);
        if (players.size() >= CommonConfig.minPlayer) {
            this.server.getPlayerList().broadcastSystemMessage(Component.translatable("fateubw.chat.grailwar.start").withStyle(ChatFormatting.GOLD), true);
            this.joinedParticipants = new HashSet<>(this.participants.keySet());
        } else if (players.isEmpty()) {
            this.server.getPlayerList().broadcastSystemMessage(Component.translatable("fateubw.chat.grailwar.players.none").withStyle(ChatFormatting.RED), true);
            this.reset(false);
        } else {
            this.joinTime = CommonConfig.joinTime;
            this.phase = Phase.JOIN;
            this.server.getPlayerList().broadcastSystemMessage(Component.translatable("fateubw.chat.grailwar.players.missing").withStyle(ChatFormatting.RED), true);
        }
        this.setDirty();
    }

    private void runGrailWar() {
        Set<UUID> invalid = new HashSet<>();
        this.participants.forEach((id, participant) -> {
            if (!participant.valid(this.server) && invalid.size() + 1 < this.participants.size()) {
                invalid.add(id);
                if (participant.isPlayerParticipant()) {
                    this.server.getProfileCache()
                            .get(participant.getId()).ifPresent(prof ->
                                    this.broadcastParticipants(Component.translatable("fateubw.chat.grailwar.player.out", prof.getName()).withStyle(ChatFormatting.RED)));
                }
                Entity servant = participant.getServant(this.server);
                if (servant != null)
                    servant.hurt(FateDamageTypes.grail(servant.registryAccess()), Integer.MAX_VALUE);
            }
        });
        invalid.forEach(this.participants::remove);
        if (this.players(false).isEmpty()) {
            this.reset(false);
            this.server.getPlayerList().broadcastSystemMessage(Component.translatable("fateubw.chat.grailwar.players.dead").withStyle(ChatFormatting.RED), true);
        } else if (this.isFull()) {
            if (this.participants.isEmpty()) {
                // Abort grail as no winner
                this.reset(true);
            } else if (this.participants.size() == 1) {
                Participant<?> participant = this.participants.values().iterator().next();
                ServerPlayer player = participant.getAsPlayer(this.server);
                if (player != null) {
                    String name = player.getGameProfile().getName();
                    FateCriterionTriggers.WIN_GRAIL_WAR.get().trigger(player);
                    this.server.getPlayerList().broadcastSystemMessage(Component.translatable("fateubw.chat.grailwar.win", name).withStyle(ChatFormatting.GOLD), true);

                    ItemEntity holyGrail = new ItemEntity(player.level(),
                            player.getX() + player.getRandom().nextInt(7) - 3, player.getY() + 3, player.getZ() + player.getRandom().nextInt(7) - 3,
                            new ItemStack(FateItems.GRAIL.get()));
                    holyGrail.setExtendedLifetime();
                    holyGrail.setThrower(player);
                    holyGrail.setInvulnerable(true);
                    holyGrail.setGlowingTag(true);
                    holyGrail.setNoGravity(true);
                    player.level().addFreshEntity(holyGrail);
                    player.sendSystemMessage(Component.translatable("fateubw.chat.grailwar.win.spawn")
                            .withStyle(ChatFormatting.GRAY));

                    Platform.INSTANCE.getPlayerData(player).saveServant(participant.getServant(this.server));
                } else {
                    this.server.getPlayerList().broadcastSystemMessage(Component.translatable("fateubw.chat.grailwar.win.none").withStyle(ChatFormatting.RED), true);
                }
                this.reset(false);
            }
        }
    }

    public void reset(boolean notify) {
        this.participants.values().forEach(participant -> {
            Entity servant = participant.getServant(this.server);
            if (servant != null)
                servant.hurt(FateDamageTypes.grail(servant.registryAccess()), Integer.MAX_VALUE);
        });
        this.participants.clear();
        this.servantsTypes.clear();
        this.servantClasses.clear();
        this.joinedParticipants.clear();
        this.phase = Phase.NONE;
        this.lastGrailEndDay = day(this.server.overworld());
        this.joinTime = 0;
        this.timeToNextServant = 0;
        if (notify)
            this.server.getPlayerList().broadcastSystemMessage(Component.translatable("fateubw.chat.grailwar.end").withStyle(ChatFormatting.RED), true);
        this.setDirty();
    }

    public boolean canSpawnMoreServants(Level level) {
        for (EntityPropsManager.EntityTypeAndID entry : DatapackHandler.SERVANT_PROPS.getServants()) {
            boolean canSpawn = CommonConfig.allowDuplicateServant || !this.servantsTypes.contains(entry.id());
            if (canSpawn && (CommonConfig.allowDuplicateClass
                    || !this.servantClasses.contains(DatapackHandler.SERVANT_PROPS.get(entry.type().value()).servantClass())))
                return true;
        }
        return false;
    }

    public boolean canSpawnServant(ServantLike<?> servant) {
        if (this.isFull())
            return false;
        if (!this.canSpawnServantType(BuiltInRegistries.ENTITY_TYPE.getKey(servant.get().getType())))
            return false;
        return this.canSpawnServantClass(servant.props().servantClass());
    }

    public boolean canSpawnServantType(ResourceLocation entityType) {
        if (this.isFull())
            return false;
        return CommonConfig.allowDuplicateServant || !this.servantsTypes.contains(entityType);
    }

    public boolean canSpawnServantClass(ResourceLocation servantClass) {
        if (this.isFull())
            return false;
        return CommonConfig.allowDuplicateClass || !this.servantClasses.contains(servantClass);
    }

    private void trySpawnNPCServant(ServerLevel level) {
        if (this.isFull())
            return;
        List<ServerPlayer> players = new ArrayList<>();
        Set<UUID> playerParticipant = this.players(false);
        this.server.getPlayerList().getPlayers().forEach(player -> {
            if (playerParticipant.contains(player.getUUID()))
                players.add(player);
        });
        int spawns = Math.min(level.random.nextInt(CommonConfig.maxServantCircle) + 1, CommonConfig.maxPlayer - this.joinedParticipants.size());
        for (int i = 0; i < spawns; i++) {
            if (players.isEmpty())
                return;
            ServerPlayer player = players.remove(level.random.nextInt(players.size()));
            double xR = player.serverLevel().random.nextDouble() - 0.5;
            double zR = player.serverLevel().random.nextDouble() - 0.5;
            int x = (int) (player.getX() + xR * 128 + (xR <= 0 ? -24 : 24));
            int z = (int) (player.getZ() + zR * 128 + (zR <= 0 ? -24 : 24));
            ChunkPos cpos = new ChunkPos(x >> 4, z >> 4);
            LevelChunk chunk = player.serverLevel().getChunk(cpos.x, cpos.z);
            int y = chunk.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z) + 1;
            ServantLike<?> servant = this.summonRandomServant(player.serverLevel(), new Vec3(x, y, z), null, null, true, true);
            if (servant != null) {
                player.serverLevel().getChunkSource().addRegionTicket(ServantLike.TRACKINGTICKET, cpos, 2, cpos);
                this.timeToNextServant = Mth.nextInt(player.serverLevel().random, CommonConfig.servantMinSpawnDelay, CommonConfig.servantMaxSpawnDelay);
                if (this.notify(BuiltInRegistries.ENTITY_TYPE.getKey(servant.get().getType()))) {
                    if (CommonConfig.notifyAll)
                        this.broadcastParticipants(Component.translatable("fateubw.chat.grailwar.spawn", player.getName()).withStyle(ChatFormatting.GRAY));
                    else
                        player.sendSystemMessage(Component.translatable("fateubw.chat.grailwar.spawn", player.getName()).withStyle(ChatFormatting.GRAY));
                }
            }
        }
    }

    private boolean notify(ResourceLocation loc) {
        return CommonConfig.notificationWhitelist == CommonConfig.notifyBlacklist.contains(loc.toString());
    }

    public void broadcastParticipants(Component message) {
        this.server.getPlayerList().broadcastSystemMessage(message,
                player -> this.isParticipant(player) ? message : null, true);
    }

    public ServantLike<?> summonRandomServant(ServerLevel level, Vec3 pos, @Nullable ServerPlayer player, @Nullable ItemStack stack, boolean event, boolean addToLevel) {
        Collection<EntityPropsManager.EntityTypeAndID> servants = DatapackHandler.SERVANT_PROPS.getServants();
        List<EntityPropsManager.EntityTypeAndID> entities = servants.stream()
                .filter(entry -> this.canSpawnServantClass(entry.servantClass()) && this.canSpawnServantType(entry.id()))
                .map(entry -> entry.updatedWeight(stack)).toList();
        if (entities.isEmpty())
            return null;
        Entity entity = WeightedRandom.getRandomItem(level.random, entities)
                .map(t -> t.type().value()
                        .create(level, null, BlockPos.containing(pos),
                                MobSpawnType.MOB_SUMMONED, false, false))
                .orElse(null);
        if (!(entity instanceof ServantLike<?> servant))
            return null;
        Mob mob = servant.get();
        mob.moveTo(pos.x(), pos.y(), pos.z(), level.random.nextFloat() * 360.0F, 0);
        if (player != null)
            mob.lookAt(EntityAnchorArgument.Anchor.EYES, player.position());
        servant.setOwner(player);
        if (!mob.checkSpawnObstruction(level) || event && !Platform.INSTANCE.canSpawnEvent(mob, level, MobSpawnType.TRIGGERED))
            return null;
        this.join(servant);
        mob.finalizeSpawn(level, level.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.TRIGGERED, null);
        if (addToLevel) {
            level.addFreshEntity(mob);
        }
        return servant;
    }

    /**
     * Move masterless servant to a random player if too far away
     */
    public void moveToPlayer(ServantLike<?> servantLike) {
        if (servantLike.getOwnerUUID() != null || servantLike.get().getServer() == null)
            return;
        LivingEntity servant = servantLike.get();
        if (!this.hasPlayersNearby(servant)) {
            List<ServerPlayer> players = servant.getServer().getPlayerList().getPlayers().stream().filter(this::isParticipant).toList();
            ServerPlayer player = players.get(servant.getRandom().nextInt(players.size()));
            for (int i = 0; i < 10; i++) {
                double xR = servant.getRandom().nextDouble() - 0.5;
                double zR = servant.getRandom().nextDouble() - 0.5;
                double x = player.getX() + xR * 128 + (xR <= 0 ? -24 : 24);
                double z = player.getZ() + zR * 128 + (zR <= 0 ? -24 : 24);
                double y = player.getY() + (servant.getRandom().nextInt(64) - 32);
                if (servant.randomTeleport(x, y, z, false)) {
                    break;
                }
            }
        }
    }

    private boolean hasPlayersNearby(Entity entity) {
        for (Player player : entity.level().players()) {
            if (!this.isParticipant(player))
                continue;
            double dist = player.distanceToSqr(entity.getX(), player.getY(), entity.getZ());
            if (dist < 256 * 256)
                return true;
        }
        return false;
    }

    public void load(CompoundTag compound) {
        ListTag tag = compound.getList("Participants", Tag.TAG_COMPOUND);
        tag.forEach(cT -> {
            Participant<?> participant = new Participant<>((CompoundTag) cT);
            this.participants.put(participant.getId(), participant);
        });
        ListTag joined = compound.getList("JoinedParticipants", Tag.TAG_INT_ARRAY);
        joined.forEach(s -> this.joinedParticipants.add(NbtUtils.loadUUID(s)));
        ListTag list = compound.getList("Servants", Tag.TAG_STRING);
        list.forEach(s -> this.servantsTypes.add(ResourceLocation.parse(s.getAsString())));
        ListTag list2 = compound.getList("ServantClasses", Tag.TAG_STRING);
        list2.forEach(s -> this.servantClasses.add(ResourceLocation.parse(s.getAsString())));
        this.phase = Phase.valueOf(compound.getString("Phase"));
        this.lastGrailEndDay = compound.getInt("LastGrailWarDay");
        this.joinTime = compound.getInt("JoinTime");
        this.timeToNextServant = compound.getInt("NextServantSpawnTick");
        CompoundTag tickets = compound.getCompound("ToLoadChunks");
        tickets.getAllKeys().forEach(id -> {
            CompoundTag entryTag = tickets.getCompound(id);
            this.servantTickets.put(new ChunkPos(entryTag.getLong("Position")), ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(entryTag.getString("Level"))));
        });
    }

    @Override
    public CompoundTag save(CompoundTag compound, HolderLookup.Provider provider) {
        ListTag tag = new ListTag();
        this.participants.values().forEach(p -> tag.add(p.save()));
        compound.put("Participants", tag);
        ListTag joined = new ListTag();
        this.joinedParticipants.forEach(id -> joined.add(NbtUtils.createUUID(id)));
        compound.put("JoinedParticipants", joined);
        ListTag list = new ListTag();
        this.servantsTypes.forEach(res -> list.add(StringTag.valueOf(res.toString())));
        compound.put("Servants", list);
        ListTag list2 = new ListTag();
        this.servantClasses.forEach(e -> list2.add(StringTag.valueOf(e.toString())));
        compound.put("ServantClasses", list2);
        compound.putString("Phase", this.phase.toString());
        compound.putInt("LastGrailWarDay", this.lastGrailEndDay);
        compound.putInt("JoinTime", this.joinTime);
        compound.putInt("NextServantSpawnTick", this.timeToNextServant);
        CompoundTag tickets = new CompoundTag();
        this.participants.forEach(((uuid, participant) -> {
            Entity servant = participant.getServant(this.server);
            if (servant != null) {
                CompoundTag entryTag = new CompoundTag();
                entryTag.putLong("Position", ChunkPos.asLong(servant.blockPosition()));
                entryTag.putString("Level", servant.level().dimension().location().toString());
                tickets.put(uuid.toString(), entryTag);
            }
        }));
        compound.put("ToLoadChunks", tickets);
        return compound;
    }

    enum Phase {
        NONE,
        JOIN,
        ACTIVE
    }

    public enum JoinResult {

        WRONG_STATE("fateubw.war.join.result.fail"),
        FULL("fateubw.war.join.result.full"),
        JOINED("fateubw.war.join.result.joined"),
        WRONG_SERVANT("fateubw.war.join.servant.fail"),
        NO_MORE_SERVANTS("fateubw.war.join.servant.full"),
        SUCCESS("fateubw.war.join.success");

        public final String translationKey;

        JoinResult(String translationKey) {
            this.translationKey = translationKey;
        }
    }
}