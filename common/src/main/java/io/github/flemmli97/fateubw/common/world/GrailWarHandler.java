package io.github.flemmli97.fateubw.common.world;

import com.google.common.collect.ImmutableSet;
import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.datapack.EntityPropsManager;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.items.ItemServantCharm;
import io.github.flemmli97.fateubw.common.registry.AdvancementRegister;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.CustomDamageSource;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
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
import java.util.Set;
import java.util.UUID;

public class GrailWarHandler extends SavedData {

    private static final String IDENTIFIER = "FateGrailWar";

    private final MinecraftServer server;
    private final Map<UUID, Participant> participants = new HashMap<>();
    private int joinedParticipants;

    /**
     * Tracking what servants and classes spawned in the grailwar
     */
    private final Set<ResourceLocation> servantsTypes = new HashSet<>();
    private final Set<ResourceLocation> servantClasses = new HashSet<>();

    private Phase phase = Phase.NONE;

    private int lastGrailEndDay, joinTime;
    private int timeToNextServant;

    private Map<ChunkPos, ResourceKey<Level>> servantTickets;

    public GrailWarHandler(MinecraftServer server) {
        this.server = server;
    }

    private GrailWarHandler(MinecraftServer server, CompoundTag tag) {
        this.server = server;
        this.load(tag);
    }

    public static GrailWarHandler get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(tag -> new GrailWarHandler(server, tag), () -> new GrailWarHandler(server), IDENTIFIER);
    }

    private static int day(Level world) {
        return (int) ((world.getDayTime()) / 24000 % Integer.MAX_VALUE);
    }

    /**
     * Joins the grailwar as a player with the given servant
     */
    public boolean join(BaseServant servant) {
        Player player = servant.getOwner();
        if (player != null) {
            JoinResult res = this.checkJoining(player);
            if (res != JoinResult.SUCCESS) {
                player.sendMessage(new TranslatableComponent(res.translationKey), Util.NIL_UUID);
                return false;
            }
        }
        if (!this.joinInternal(player, servant)) {
            if (player != null)
                player.sendMessage(new TranslatableComponent(JoinResult.WRONG_SERVANT.translationKey), Util.NIL_UUID);
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

    private boolean joinInternal(@Nullable Player player, BaseServant servant) {
        Participant participant = new Participant(servant, player);
        if (!this.participants.containsKey(participant.getId()) && this.canSpawnServant(servant)) {
            this.participants.put(participant.getId(), participant);
            this.servantClasses.add(servant.props().getServantClass());
            this.servantsTypes.add(Registry.ENTITY_TYPE.getKey(servant.getType()));
            this.joinedParticipants++;
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
        if (this.participants.containsKey(player.getUUID())) {
            return JoinResult.JOINED;
        }
        if (!this.canSpawnMoreServants(player.level)) {
            return JoinResult.NO_MORE_SERVANTS;
        }
        return JoinResult.SUCCESS;
    }

    public boolean isFull() {
        return this.joinedParticipants >= CommonConfig.maxPlayer;
    }

    public boolean isParticipant(Entity entity) {
        if (entity instanceof BaseServant servant && servant.getOwnerUUID() != null)
            return this.participants.containsKey(servant.getOwnerUUID());
        return this.participants.containsKey(entity.getUUID());
    }

    public BaseServant getServant(ServerPlayer player) {
        Participant participant = this.participants.get(player.getUUID());
        if (participant != null)
            return participant.getServant(this.server);
        return null;
    }

    /**
     * The participating players
     */
    public Set<UUID> players() {
        return ImmutableSet.copyOf(this.participants.entrySet().stream().filter(p -> p.getValue().isPlayerParticipant())
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

    private void loadTickets(ServerLevel world) {
        if (this.servantTickets != null) {
            this.servantTickets.forEach((c, r) -> {
                if (world.dimension().equals(r))
                    world.getChunkSource().addRegionTicket(BaseServant.TRACKINGTICKET, c, 1, c);
                else {
                    ServerLevel w = this.server.getLevel(r);
                    w.getChunkSource().addRegionTicket(BaseServant.TRACKINGTICKET, c, 1, c);
                }
            });
            this.servantTickets = null;
        }
    }

    private void setupStart() {
        this.joinTime = CommonConfig.joinTime;
        this.phase = Phase.JOIN;
        this.server.getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.grailwar.init", this.joinTime / 20)
                .withStyle(ChatFormatting.LIGHT_PURPLE), ChatType.SYSTEM, Util.NIL_UUID);
    }

    private void start() {
        this.phase = Phase.ACTIVE;
        Set<UUID> players = this.players();
        if (players.size() >= CommonConfig.minPlayer) {
            this.server.getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.grailwar.start").withStyle(ChatFormatting.GOLD), ChatType.SYSTEM, Util.NIL_UUID);
        } else if (players.isEmpty()) {
            this.server.getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.grailwar.players.none").withStyle(ChatFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
            this.reset(false);
        } else {
            this.joinTime = CommonConfig.joinTime;
            this.phase = Phase.JOIN;
            this.server.getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.grailwar.players.missing").withStyle(ChatFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
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
                                    this.broadcastParticipants(new TranslatableComponent("fateubw.chat.grailwar.player.out", prof.getName()).withStyle(ChatFormatting.RED)));
                }
                BaseServant servant = participant.getServant(this.server);
                if (servant != null)
                    servant.hurt(CustomDamageSource.GRAIL_DAMAGE, Integer.MAX_VALUE);
            }
        });
        invalid.forEach(this.participants::remove);
        if (this.players().isEmpty()) {
            this.reset(false);
            this.server.getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.grailwar.players.dead").withStyle(ChatFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
        } else if (this.isFull()) {
            if (this.participants.isEmpty()) {
                // Abort grail as no winner
                this.reset(true);
            } else if (this.participants.size() == 1) {
                Participant participant = this.participants.values().iterator().next();
                ServerPlayer player = participant.getAsPlayer(this.server);
                if (player != null) {
                    String name = player.getGameProfile().getName();
                    AdvancementRegister.GRAIL_WAR_TRIGGER.trigger(player, false);
                    this.server.getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.grailwar.win", name).withStyle(ChatFormatting.GOLD), ChatType.SYSTEM, Util.NIL_UUID);

                    ItemEntity holyGrail = new ItemEntity(player.level, player.getX() + player.getRandom().nextInt(9) - 4, player.getY(), player.getZ() + player.getRandom().nextInt(9) - 4, new ItemStack(ModItems.GRAIL.get()));
                    holyGrail.setExtendedLifetime();
                    holyGrail.setOwner(player.getUUID());
                    holyGrail.setInvulnerable(true);
                    holyGrail.setGlowingTag(true);
                    player.level.addFreshEntity(holyGrail);
                    Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.saveServant(player));

                } else {
                    this.server.getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.grailwar.win.none").withStyle(ChatFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
                }
                this.reset(false);
            }
        }
    }

    public void reset(boolean notify) {
        this.participants.values().forEach(participant -> {
            BaseServant servant = participant.getServant(this.server);
            if (servant != null)
                servant.hurt(CustomDamageSource.GRAIL_DAMAGE, Integer.MAX_VALUE);
        });
        this.participants.clear();
        this.servantsTypes.clear();
        this.servantClasses.clear();
        this.joinedParticipants = 0;
        this.phase = Phase.NONE;
        this.lastGrailEndDay = day(this.server.overworld());
        this.joinTime = 0;
        this.timeToNextServant = 0;
        if (notify)
            this.server.getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.grailwar.end").withStyle(ChatFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
        this.setDirty();
    }

    public boolean canSpawnMoreServants(Level level) {
        for (EntityPropsManager.EntityTypeAndID entry : DatapackHandler.SERVANT_PROPS.getServants(level)) {
            if (!CommonConfig.allowDuplicateServant && this.servantsTypes.contains(entry.id()))
                return false;
            if (!CommonConfig.allowDuplicateClass && this.servantClasses.contains(DatapackHandler.SERVANT_PROPS.get(entry.id()).getServantClass()))
                return false;
        }
        return true;
    }

    public boolean canSpawnServant(BaseServant servant) {
        if (this.isFull())
            return false;
        if (!this.canSpawnServantType(Registry.ENTITY_TYPE.getKey(servant.getType())))
            return false;
        return this.canSpawnServantClass(servant.props().getServantClass());
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
        Set<UUID> playerParticipant = this.players();
        this.server.getPlayerList().getPlayers().forEach(player -> {
            if (playerParticipant.contains(player.getUUID()))
                players.add(player);
        });
        int spawns = Math.min(level.random.nextInt(CommonConfig.maxServantCircle) + 1, CommonConfig.maxPlayer - this.joinedParticipants);
        for (int i = 0; i < spawns; i++) {
            if (players.isEmpty())
                return;
            ServerPlayer player = players.remove(level.random.nextInt(players.size()));
            double xR = player.getLevel().random.nextDouble() - 0.5;
            double zR = player.getLevel().random.nextDouble() - 0.5;
            int x = (int) (player.getX() + xR * 128 + (xR <= 0 ? -24 : 24));
            int z = (int) (player.getZ() + zR * 128 + (zR <= 0 ? -24 : 24));
            ChunkPos cpos = new ChunkPos(x >> 4, z >> 4);
            LevelChunk chunk = player.getLevel().getChunk(cpos.x, cpos.z);
            int y = chunk.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z) + 1;
            BaseServant servant = this.summonRandomServant(player.getLevel(), new Vec3(x, y, z), null, null, true);
            if (servant != null) {
                player.getLevel().getChunkSource().addRegionTicket(BaseServant.TRACKINGTICKET, cpos, 1, cpos);
                this.timeToNextServant = Mth.nextInt(player.getLevel().random, CommonConfig.servantMinSpawnDelay, CommonConfig.servantMaxSpawnDelay);
                if (this.notify(Registry.ENTITY_TYPE.getKey(servant.getType()))) {
                    if (CommonConfig.notifyAll)
                        this.broadcastParticipants(new TranslatableComponent("fateubw.chat.grailwar.spawn", player.getName()).withStyle(ChatFormatting.GRAY));
                    else
                        player.sendMessage(new TranslatableComponent("fateubw.chat.grailwar.spawn", player.getName()).withStyle(ChatFormatting.GRAY), Util.NIL_UUID);
                }
            }
        }
    }

    private boolean notify(ResourceLocation loc) {
        return CommonConfig.notificationWhitelist == CommonConfig.notifyBlacklist.contains(loc.toString());
    }

    private void broadcastParticipants(Component message) {
        this.server.getPlayerList().broadcastMessage(message,
                player -> this.isParticipant(player) ? message : null, ChatType.SYSTEM, Util.NIL_UUID);
    }

    public BaseServant summonRandomServant(ServerLevel level, Vec3 pos, @Nullable ServerPlayer player, @Nullable ItemStack stack, boolean event) {
        ResourceLocation servantClass = null;
        if (stack != null && stack.getItem() instanceof ItemServantCharm charm && player.getRandom().nextFloat() <= 0.6 && this.canSpawnServantClass(charm.type))
            servantClass = ((ItemServantCharm) stack.getItem()).type;
        Collection<EntityPropsManager.EntityTypeAndID> servants;
        if (servantClass == null || !this.canSpawnServantClass(servantClass)) {
            servants = DatapackHandler.SERVANT_PROPS.getServants(level);
        } else {
            servants = DatapackHandler.SERVANT_PROPS.getServantsFromClass(level, servantClass);
        }
        List<EntityPropsManager.EntityTypeAndID> entities = servants.stream().filter(entry -> this.canSpawnServantType(entry.id())).toList();
        if (entities.isEmpty())
            return null;
        BaseServant servant = entities.get(level.random.nextInt(entities.size())).type().create(level);
        if (servant == null)
            return null;
        servant.moveTo(pos.x(), pos.y(), pos.z(), level.random.nextFloat() * 360.0F, 0);
        if (player != null)
            servant.lookAt(EntityAnchorArgument.Anchor.EYES, player.position());
        servant.setOwner(player);
        if (event && !Platform.INSTANCE.canSpawnEvent(servant, level, pos.x(), pos.y(), pos.z(), null, MobSpawnType.TRIGGERED, SpawnPlacements.getPlacementType(servant.getType())))
            return null;
        servant.finalizeSpawn(level, level.getCurrentDifficultyAt(servant.blockPosition()), MobSpawnType.TRIGGERED, null, null);
        level.addFreshEntity(servant);
        this.join(servant);
        return servant;
    }

    public void load(CompoundTag compound) {
        ListTag tag = compound.getList("Participants", Tag.TAG_COMPOUND);
        tag.forEach(cT -> {
            Participant participant = new Participant((CompoundTag) cT);
            this.participants.put(participant.getId(), participant);
        });
        this.joinedParticipants = compound.getInt("JoinedParticipants");
        ListTag list = compound.getList("Servants", Tag.TAG_STRING);
        list.forEach(s -> this.servantsTypes.add(new ResourceLocation(s.getAsString())));
        ListTag list2 = compound.getList("ServantClasses", Tag.TAG_STRING);
        list2.forEach(s -> this.servantClasses.add(new ResourceLocation(s.getAsString())));
        this.phase = Phase.valueOf(compound.getString("Phase"));
        this.lastGrailEndDay = compound.getInt("LastGrailWarDay");
        this.joinTime = compound.getInt("JoinTime");
        this.timeToNextServant = compound.getInt("NextServantSpawnTick");
        CompoundTag tickets = compound.getCompound("ToLoadChunks");
        tickets.getAllKeys().forEach(id -> {
            CompoundTag entryTag = tickets.getCompound(id);
            this.servantTickets.put(new ChunkPos(entryTag.getLong("Position")), ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(entryTag.getString("Level"))));
        });
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        ListTag tag = new ListTag();
        this.participants.values().forEach(p -> tag.add(p.save()));
        compound.put("Participants", tag);
        compound.putInt("JoinedParticipants", this.joinedParticipants);
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
            BaseServant servant = participant.getServant(this.server);
            if (servant != null) {
                CompoundTag entryTag = new CompoundTag();
                entryTag.putLong("Position", ChunkPos.asLong(servant.blockPosition()));
                entryTag.putString("Level", servant.level.dimension().location().toString());
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