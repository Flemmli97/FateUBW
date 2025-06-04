package io.github.flemmli97.fateubw.common.world;

import com.google.common.collect.ImmutableSet;
import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.datapack.ServantPropManager;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.registry.AdvancementRegister;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.SummonUtils;
import io.github.flemmli97.fateubw.platform.Platform;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class GrailWarHandler extends SavedData {

    private static final String IDENTIFIER = "GrailWarTracker";

    private final Map<UUID, Participant> participants = new HashMap<>();

    /**
     * Tracking what servants and classes spawned in the grailwar
     */
    private final Set<ResourceLocation> servantsTypes = new HashSet<>();
    private final Set<ResourceLocation> servantClasses = new HashSet<>();
    private int spawnedServants;

    private State state = State.NOTHING;

    private int joinTime, rewardDelay;
    private int timeToNextServant;

    private final List<UUID> sheduledPlayerRemoval = new ArrayList<>();

    private final ServantTracker chunkReload = new ServantTracker();

    public GrailWarHandler() {
    }

    private GrailWarHandler(CompoundTag tag) {
        this.load(tag);
    }

    public static GrailWarHandler get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(GrailWarHandler::new, GrailWarHandler::new, IDENTIFIER);
    }

    /**
     * Joins the grailwar as a player with the given servant
     */
    public JoinResult join(ServerPlayer player, BaseServant servant) {
        if (this.canJoin(player)) {
            if (this.joinFinal(player, servant)) {
                if (this.state == State.NOTHING) {
                    this.joinTime = Config.Common.joinTime;
                    player.getLevel().getServer().getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.grailwar.init", this.joinTime / 20).withStyle(ChatFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
                    this.state = State.JOIN;
                }
                this.setDirty();
                return JoinResult.SUCCESS;
            }
            return JoinResult.WRONG_SERVANT;
        }
        return JoinResult.WRONG_STATE;
    }

    /**
     * Joins the grailwar as a servant without master
     */
    public boolean joinAsServant(BaseServant servant) {
        return this.joinFinal(null, servant);
    }

    private boolean joinFinal(@Nullable ServerPlayer player, BaseServant servant) {
        Participant participant = new Participant(servant, player);
        if (!this.participants.containsValue(participant) && this.canSpawnServant(servant)) {
            this.participants.put(participant.getUuid(), participant);
            this.servantClasses.add(servant.props().getServantClass());
            this.servantsTypes.add(PlatformUtils.INSTANCE.entities().getIDFrom(servant.getType()));
            this.spawnedServants++;
            return true;
        }
        return false;
    }

    public boolean canJoin(ServerPlayer player) {
        return !this.participants.containsKey(player.getUUID()) && this.state != State.RUN && this.state != State.FINISH && this.spawnedServants < Config.Common.maxPlayer;
    }

    /**
     * Removes the player only
     */
    public boolean removePlayer(ServerPlayer player, boolean clear) {
        if (this.isParticipant(player)) {
            this.participants.remove(player.getUUID());
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.setCommandSeals(player, 0));
            player.getLevel().getServer().getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.grailwar.playerout", player.getName()).withStyle(ChatFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
            this.setDirty();
            return true;
        } else if (clear) {
            this.setDirty();
        }
        return false;
    }

    public boolean removeServant(BaseServant servant) {
        if (!servant.level.isClientSide) {
            Player player = servant.getOwner();
            boolean success = false;
            if (player != null) {
                servant.setOwner(null);
                success = this.removePlayer((ServerPlayer) player, false);
            } else if (servant.hasOwner()) {
                this.sheduledPlayerRemoval.add(servant.getOwnerUUID());
                //TODO if player not online
            } else {
                Participant participant = this.participants.remove(servant.getUUID());
                success = participant != null;
            }
            this.checkWinCondition(servant.getServer());
            this.setDirty();
            return success;
        }
        return false;
    }

    protected void checkWinCondition(MinecraftServer server) {
        if (this.state == State.RUN) {
            Set<UUID> players = this.players();
            if (this.participants.size() == 1 && this.spawnedServants >= Config.Common.maxPlayer && players.size() == 1) {
                UUID playerUuid = players.iterator().next();
                this.rewardDelay = Config.Common.rewardDelay;
                this.state = State.FINISH;
                ServerPlayer player = server.getPlayerList().getPlayer(playerUuid);
                if (player != null) {
                    String name = player.getGameProfile().getName();
                    AdvancementRegister.GRAIL_WAR_TRIGGER.trigger(player, false);
                    server.getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.grailwar.win", name).withStyle(ChatFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
                } else {
                    server.getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.grailwar.win.none").withStyle(ChatFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
                    this.reset(server);
                }
                this.setDirty();
            } else if (players.isEmpty())
                this.reset(server);
        }
    }

    public boolean isParticipant(Entity entity) {
        return this.participants.containsKey(entity.getUUID());
    }

    public BaseServant getServant(ServerPlayer player) {
        Participant participant = this.participants.get(player.getUUID());
        if (participant != null)
            return participant.getServant(player.level.getServer());
        return null;
    }

    public boolean removeConnection(ServerPlayer player) {
        return this.sheduledPlayerRemoval.remove(player.getUUID());
    }

    /**
     * The participating players
     */
    public Set<UUID> players() {
        return ImmutableSet.copyOf(this.participants.entrySet().stream().filter(p -> p.getValue().isPlayerParticipant())
                .map(Map.Entry::getKey)
                .toList());
    }

    /**
     * The winning player if present
     */
    public ServerPlayer winner(MinecraftServer server) {
        if (this.state != State.FINISH || this.players().size() != 1)
            return null;
        return server.getPlayerList().getPlayer(this.players().iterator().next());
    }

    private void start(MinecraftServer server) {
        this.state = State.RUN;
        Set<UUID> players = this.players();
        if (players.size() >= Config.Common.minPlayer) {
            server.getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.grailwar.start").withStyle(ChatFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
        } else if (players.isEmpty())
            this.reset(server);
        else {
            this.joinTime = Config.Common.joinTime;
            this.state = State.JOIN;
            server.getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.grailwar.missingplayer").withStyle(ChatFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
        }
        this.setDirty();
    }

    public void tick(ServerLevel level) {
        this.chunkReload.onLoad(level);
        if (this.state == State.JOIN) {
            if (--this.joinTime <= 0)
                this.start(level.getServer());
        } else if (this.state == State.RUN) {
            if (Config.Common.fillMissingSlots && this.spawnedServants < Config.Common.maxPlayer && --this.timeToNextServant <= 0) {
                this.trySpawnNPCServant(level);
            }
        } else if (this.state == State.FINISH) {
            if (--this.rewardDelay <= 0) {
                ServerPlayer player = this.winner(level.getServer());
                if (player != null) {
                    ItemEntity holyGrail = new ItemEntity(player.level, player.getX() + level.random.nextInt(9) - 4, player.getY(), player.getZ() + level.random.nextInt(9) - 4, new ItemStack(ModItems.GRAIL.get()));
                    holyGrail.setExtendedLifetime();
                    holyGrail.setOwner(player.getUUID());
                    holyGrail.setInvulnerable(true);
                    holyGrail.setGlowingTag(true);
                    player.level.addFreshEntity(holyGrail);
                    Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.saveServant(player));
                }
                this.reset(level.getServer());
            }
        }
        this.setDirty();
    }

    public void reset(MinecraftServer server) {
        this.joinTime = 0;
        this.rewardDelay = 0;
        this.timeToNextServant = 0;
        this.state = State.NOTHING;
        this.participants.values().forEach(p -> {
            BaseServant servant = p.getServant(server);
            if (servant != null)
                servant.hurt(DamageSource.OUT_OF_WORLD, Integer.MAX_VALUE);
        });
        this.participants.clear();
        this.servantsTypes.clear();
        this.servantClasses.clear();
        this.spawnedServants = 0;
        server.getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.grailwar.end").withStyle(ChatFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
        this.setDirty();
    }

    public boolean canSpawnMoreServants(ServerLevel level) {
        for (ServantPropManager.EntityTypeAndID entry : DatapackHandler.SERVANT_PROPS.getServants(level)) {
            if (!Config.Common.allowDuplicateServant && this.servantsTypes.contains(entry.id()))
                return false;
            if (!Config.Common.allowDuplicateClass && this.servantClasses.contains(DatapackHandler.SERVANT_PROPS.get(entry.id()).getServantClass()))
                return false;
        }
        return true;
    }

    public boolean canSpawnServant(BaseServant servant) {
        if (!this.canSpawnServantType(PlatformUtils.INSTANCE.entities().getIDFrom(servant.getType())))
            return false;
        return this.canSpawnServantClass(servant.props().getServantClass());
    }

    public boolean canSpawnServantType(ResourceLocation entityType) {
        return Config.Common.allowDuplicateServant || !this.servantsTypes.contains(entityType);
    }

    public boolean canSpawnServantClass(ResourceLocation servantClass) {
        return Config.Common.allowDuplicateClass || !this.servantClasses.contains(servantClass);
    }

    private void trySpawnNPCServant(ServerLevel level) {
        List<ServerPlayer> players = new ArrayList<>();
        Set<UUID> playerParticipant = this.players();
        level.getServer().getPlayerList().getPlayers().forEach(player -> {
            if (playerParticipant.contains(player.getUUID()))
                players.add(player);
        });
        int spawns = level.random.nextInt(Config.Common.maxServantCircle) + 1;
        for (int i = 0; i < spawns; i++) {
            if (players.isEmpty())
                return;
            ServerPlayer player = players.remove(level.random.nextInt(players.size()));
            int x = player.blockPosition().getX() + player.getLevel().random.nextInt(64) + 48;
            int z = player.blockPosition().getZ() + player.getLevel().random.nextInt(64) + 48;
            LevelChunk chunk = player.getLevel().getChunk(x >> 4, z >> 4);
            int y = chunk.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z) + 1;
            BaseServant servant = SummonUtils.randomServant(player.getLevel(), new Vec3(x, y, z), null, null);
            if (servant != null) {
                SpawnPlacements.Type place = SpawnPlacements.getPlacementType(servant.getType());
                if (Platform.INSTANCE.canSpawnEvent(servant, player.getLevel(), x, y, z, null, MobSpawnType.TRIGGERED, place)) {
                    servant.finalizeSpawn(player.getLevel(), player.getLevel().getCurrentDifficultyAt(servant.blockPosition()), MobSpawnType.NATURAL, null, null);
                    ChunkPos cpos = new ChunkPos(x >> 4, z >> 4);
                    player.getLevel().getChunkSource().addRegionTicket(TicketType.UNKNOWN, cpos, 9, cpos);
                    player.getLevel().addFreshEntity(servant);
                    this.joinFinal(null, servant);
                    this.timeToNextServant = Mth.nextInt(player.getLevel().random, Config.Common.servantMinSpawnDelay, Config.Common.servantMaxSpawnDelay);
                    if (!this.notify(PlatformUtils.INSTANCE.entities().getIDFrom(servant.getType())))
                        if (Config.Common.notifyAll)
                            player.getLevel().getServer().getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.grailwar.spawn", player.getName()).withStyle(ChatFormatting.GOLD), ChatType.SYSTEM, Util.NIL_UUID);
                        else
                            player.sendMessage(new TranslatableComponent("fateubw.chat.grailwar.spawn", player.getName()).withStyle(ChatFormatting.GOLD), Util.NIL_UUID);
                }
            }
        }
    }

    private boolean notify(ResourceLocation loc) {
        return Config.Common.notificationWhitelist == Config.Common.notifyBlackList.contains(loc.toString());
    }

    public void untrack(BaseServant servant) {
        this.chunkReload.remove(servant);
    }

    public void track(BaseServant servant) {
        this.chunkReload.add(servant);
    }

    public void load(CompoundTag compound) {
        ListTag tag = compound.getList("Participants", Tag.TAG_COMPOUND);
        tag.forEach(cT -> {
            Participant participant = new Participant((CompoundTag) cT);
            this.participants.put(participant.getUuid(), participant);
        });
        ListTag list = compound.getList("Servants", Tag.TAG_STRING);
        list.forEach(s -> this.servantsTypes.add(new ResourceLocation(s.getAsString())));
        ListTag list2 = compound.getList("ServantClasses", Tag.TAG_STRING);
        list2.forEach(s -> this.servantClasses.add(new ResourceLocation(s.getAsString())));
        ListTag list3 = compound.getList("ToRemove", Tag.TAG_INT_ARRAY);
        list3.forEach(s -> this.sheduledPlayerRemoval.add(NbtUtils.loadUUID(s)));
        this.joinTime = compound.getInt("Ticker");
        this.rewardDelay = compound.getInt("WinDelay");
        this.timeToNextServant = compound.getInt("SpawnTick");
        this.state = State.valueOf(compound.getString("State"));
        this.spawnedServants = compound.getInt("SpawnedServants");
        this.chunkReload.load(compound.getCompound("ChunkLoading"));
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        ListTag tag = new ListTag();
        this.participants.values().forEach(Participant::save);
        compound.put("Participants", tag);
        ListTag list = new ListTag();
        this.servantsTypes.forEach(res -> list.add(StringTag.valueOf(res.toString())));
        compound.put("Servants", list);
        ListTag list2 = new ListTag();
        this.servantClasses.forEach(e -> list2.add(StringTag.valueOf(e.toString())));
        compound.put("ServantClasses", list2);
        ListTag list3 = new ListTag();
        this.sheduledPlayerRemoval.forEach(uuid -> list3.add(NbtUtils.createUUID(uuid)));
        compound.put("ToRemove", list3);
        compound.putInt("Ticker", this.joinTime);
        compound.putInt("WinDelay", this.rewardDelay);
        compound.putInt("SpawnTick", this.timeToNextServant);
        compound.putString("State", this.state.toString());
        compound.putInt("SpawnedServants", this.spawnedServants);
        compound.put("ChunkLoading", this.chunkReload.onSave(new CompoundTag()));
        return compound;
    }

    enum State {
        JOIN,
        RUN,
        FINISH,
        NOTHING
    }

    public enum JoinResult {

        WRONG_STATE("fate.war.join.state.fail"),
        WRONG_SERVANT("fate.war.join.servant.fail"),
        OTHER("fate.war.join.misc.fail"),
        SUCCESS("fate.war.join.success");

        public final String translationKey;

        JoinResult(String translationKey) {
            this.translationKey = translationKey;
        }
    }
}