package io.github.flemmli97.fateubw.common.world;

import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.network.S2CWarData;
import io.github.flemmli97.fateubw.common.registry.AdvancementRegister;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.EnumServantType;
import io.github.flemmli97.fateubw.common.utils.SummonUtils;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import io.github.flemmli97.fateubw.platform.Platform;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class GrailWarHandler extends SavedData {

    private static final String IDENTIFIER = "GrailWarTracker";

    private final Set<UUID> players = new HashSet<>();

    private final Set<UUID> activeServants = new HashSet<>();
    /**
     * Tracking what servants and classes spawned in the grailwar
     */
    private final Set<ResourceLocation> servantsTypes = new HashSet<>();
    private final Set<EnumServantType> servantClasses = new HashSet<>();
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

    public boolean join(ServerPlayer player) {
        UUID uuid = player.getUUID();
        if (this.canJoin(player)) {
            BaseServant servant = Platform.INSTANCE.getPlayerData(player).map(data -> data.getServant(player)).orElse(null);
            if (servant != null) {
                if (this.addServant(servant)) {
                    this.players.add(uuid);
                    if (this.state == State.NOTHING) {
                        this.joinTime = Config.Common.joinTime;
                        player.level.getServer().getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.grailwar.init", this.joinTime / 20).withStyle(ChatFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
                        this.state = State.JOIN;
                    }
                    this.setDirty();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canJoin(ServerPlayer player) {
        return !this.players.contains(player.getUUID()) && this.state != State.RUN && this.state != State.FINISH && this.spawnedServants < Config.Common.maxPlayer;
    }

    /**
     * Removes the player only
     */
    public boolean removePlayer(ServerPlayer player) {
        if (this.hasPlayer(player)) {
            this.players.remove(player.getUUID());
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                data.setCommandSeals(player, 0);
                data.setServant(null);
            });
            TruceHandler.get(player.getServer()).disbandAll(player);
            player.level.getServer().getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.grailwar.playerout", player.getName()).withStyle(ChatFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
            this.setDirty();
            return true;
        }
        return false;
    }

    protected void checkWinCondition(ServerLevel level) {
        if (this.state == State.RUN) {
            if (this.activeServants.size() == 1 && this.spawnedServants >= Config.Common.maxPlayer && this.players.size() == 1) {
                this.rewardDelay = Config.Common.rewardDelay;
                this.state = State.FINISH;
                for (UUID uuid : this.players) {
                    Player player = level.getPlayerByUUID(uuid);
                    String name;
                    if (player instanceof ServerPlayer) {
                        AdvancementRegister.GRAIL_WAR_TRIGGER.trigger((ServerPlayer) player, false);
                        name = player.getGameProfile().getName();
                    } else {

                        name = player.getServer().getProfileCache().get(uuid).map(GameProfile::getName).orElse("MISSINGNO");
                    }
                    level.getServer().getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.grailwar.win", name).withStyle(ChatFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
                }
                this.setDirty();
            } else if (this.players.isEmpty())
                this.reset(level);
        }
    }

    public boolean hasPlayer(ServerPlayer player) {
        return this.players.contains(player.getUUID());
    }

    public boolean removeConnection(ServerPlayer player) {
        return this.sheduledPlayerRemoval.remove(player.getUUID());
    }

    /**
     * The participating players
     */
    public Set<UUID> players() {
        return ImmutableSet.copyOf(this.players);
    }

    public Player winner(ServerLevel level) {
        if (this.state != State.FINISH || this.players.size() != 1)
            return null;
        return level.getPlayerByUUID(this.players.iterator().next());
    }

    public boolean addServant(BaseServant servant) {
        if (!this.activeServants.contains(servant.getUUID()) && this.canSpawnServant(servant)) {
            this.activeServants.add(servant.getUUID());
            this.servantClasses.add(servant.getServantType());
            this.servantsTypes.add(PlatformUtils.INSTANCE.entities().getIDFrom(servant.getType()));
            this.spawnedServants++;
            return true;
        }
        return false;
    }

    public boolean removeServant(BaseServant servant) {
        if (!servant.level.isClientSide) {
            Player player = servant.getOwner();
            if (player != null) {
                servant.setOwner(null);
                this.removePlayer((ServerPlayer) player);
            } else if (servant.hasOwner())
                this.sheduledPlayerRemoval.add(servant.getOwnerUUID());
            if (this.activeServants.remove(servant.getUUID())) {
                this.checkWinCondition((ServerLevel) servant.level);
                this.setDirty();
                return true;
            }
        }
        return false;
    }

    private void start(ServerLevel level) {
        this.state = State.RUN;
        if (this.players.size() >= Config.Common.minPlayer) {
            level.getServer().getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.grailwar.start").withStyle(ChatFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
        } else if (this.players.isEmpty())
            this.reset(level);
        else {
            this.joinTime = Config.Common.joinTime;
            this.state = State.JOIN;
            level.getServer().getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.grailwar.missingplayer").withStyle(ChatFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
        }
        this.setDirty();
    }

    public void tick(ServerLevel level) {
        this.chunkReload.onLoad(level);
        if (this.state == State.JOIN) {
            if (--this.joinTime <= 0)
                this.start(level);
        } else if (this.state == State.RUN) {
            if (Config.Common.fillMissingSlots && this.spawnedServants < Config.Common.maxPlayer && --this.timeToNextServant <= 0) {
                this.trySpawnNPCServant(level);
            }
        } else if (this.state == State.FINISH) {
            if (--this.rewardDelay <= 0) {
                Player player = this.winner(level);
                if (player != null) {
                    ItemEntity holyGrail = new ItemEntity(player.level, player.getX() + level.random.nextInt(9) - 4, player.getY(), player.getZ() + level.random.nextInt(9) - 4, new ItemStack(ModItems.GRAIL.get()));
                    holyGrail.setExtendedLifetime();
                    holyGrail.setOwner(player.getUUID());
                    holyGrail.setInvulnerable(true);
                    holyGrail.setGlowingTag(true);
                    level.addFreshEntity(holyGrail);
                    Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.saveServant(player));
                }
                this.reset(level);
            }
        }
        this.setDirty();
    }

    public void reset(ServerLevel level) {
        this.players.clear();
        this.joinTime = 0;
        this.rewardDelay = 0;
        this.timeToNextServant = 0;
        this.state = State.NOTHING;
        this.activeServants.forEach(uuid -> {
            BaseServant servant = EntityUtil.findFromUUID(BaseServant.class, level, uuid);
            if (servant != null)
                servant.hurt(DamageSource.OUT_OF_WORLD, Integer.MAX_VALUE);
        });
        this.activeServants.clear();
        this.servantsTypes.clear();
        this.servantClasses.clear();
        this.spawnedServants = 0;
        level.getServer().getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.grailwar.end").withStyle(ChatFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
        NetworkCalls.INSTANCE.sendToAll(new S2CWarData(level), level.getServer());
        this.setDirty();
    }

    public boolean canSpawnMoreServants() {
        for (ResourceLocation loc : ModEntities.registeredServants()) {
            if (!Config.Common.allowDuplicateServant && this.servantsTypes.contains(loc))
                return false;
            if (!Config.Common.allowDuplicateClass && this.servantClasses.contains(ModEntities.get(loc)))
                return false;
        }
        return true;
    }

    public boolean canSpawnServant(BaseServant servant) {
        if (!this.canSpawnServantType(PlatformUtils.INSTANCE.entities().getIDFrom(servant.getType())))
            return false;
        return this.canSpawnServantClass(servant.getServantType());
    }

    public boolean canSpawnServantType(ResourceLocation entityType) {
        return Config.Common.allowDuplicateServant || !this.servantsTypes.contains(entityType);
    }

    public boolean canSpawnServantClass(EnumServantType type) {
        return Config.Common.allowDuplicateClass || !this.servantClasses.contains(type);
    }

    private void trySpawnNPCServant(ServerLevel level) {
        List<Player> players = new ArrayList<>();
        level.players().forEach(player -> {
            if (this.players.contains(player.getUUID()))
                players.add(player);
        });
        int spawns = level.random.nextInt(Config.Common.maxServantCircle) + 1;
        for (int i = 0; i < spawns; i++) {
            if (players.isEmpty())
                return;
            Player player = players.remove(level.random.nextInt(players.size()));
            int x = player.blockPosition().getX() + level.random.nextInt(64) + 48;
            int z = player.blockPosition().getZ() + level.random.nextInt(64) + 48;
            LevelChunk chunk = level.getChunk(x >> 4, z >> 4);
            int y = chunk.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z) + 1;
            BaseServant servant = SummonUtils.randomServant(level, new Vec3(x, y, z), null, null);
            if (servant != null) {
                SpawnPlacements.Type place = SpawnPlacements.getPlacementType(servant.getType());
                if (Platform.INSTANCE.canSpawnEvent(servant, level, x, y, z, null, MobSpawnType.TRIGGERED, place)) {
                    servant.finalizeSpawn(level, level.getCurrentDifficultyAt(servant.blockPosition()), MobSpawnType.NATURAL, null, null);
                    ChunkPos cpos = new ChunkPos(x >> 4, z >> 4);
                    level.getChunkSource().addRegionTicket(TicketType.UNKNOWN, cpos, 9, cpos);
                    level.addFreshEntity(servant);
                    this.addServant(servant);
                    this.timeToNextServant = Mth.nextInt(level.random, Config.Common.servantMinSpawnDelay, Config.Common.servantMaxSpawnDelay);
                    if (!this.notify(PlatformUtils.INSTANCE.entities().getIDFrom(servant.getType())))
                        if (Config.Common.notifyAll)
                            level.getServer().getPlayerList().broadcastMessage(new TranslatableComponent("fateubw.chat.grailwar.spawn", player.getName()).withStyle(ChatFormatting.GOLD), ChatType.SYSTEM, Util.NIL_UUID);
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
        ListTag tag = compound.getList("Players", Tag.TAG_INT_ARRAY);
        tag.forEach(s -> this.players.add(NbtUtils.loadUUID(s)));
        ListTag tag2 = compound.getList("ActiveServants", Tag.TAG_INT_ARRAY);
        tag2.forEach(s -> this.activeServants.add(NbtUtils.loadUUID(s)));
        ListTag list = compound.getList("Servants", Tag.TAG_STRING);
        list.forEach(s -> this.servantsTypes.add(new ResourceLocation(s.getAsString())));
        ListTag list2 = compound.getList("ServantClasses", Tag.TAG_STRING);
        list2.forEach(s -> this.servantClasses.add(EnumServantType.valueOf(s.getAsString())));
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
        this.players.forEach(uuid -> tag.add(NbtUtils.createUUID(uuid)));
        compound.put("Players", tag);
        ListTag tag2 = new ListTag();
        this.activeServants.forEach(uuid -> tag2.add(NbtUtils.createUUID(uuid)));
        compound.put("ActiveServants", tag2);
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
}