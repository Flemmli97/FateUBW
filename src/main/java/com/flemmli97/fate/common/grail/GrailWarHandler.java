package com.flemmli97.fate.common.grail;

import com.flemmli97.fate.common.capability.PlayerCapProvider;
import com.flemmli97.fate.common.config.Config;
import com.flemmli97.fate.common.entity.servant.EntityServant;
import com.flemmli97.fate.common.utils.EnumServantType;
import com.flemmli97.fate.common.utils.Utils;
import com.flemmli97.tenshilib.common.entity.EntityUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class GrailWarHandler extends WorldSavedData {

    private static final String identifier = "GrailWarTracker";

    private Map<UUID, String> players = Maps.newLinkedHashMap();
    private Map<UUID, ResourceLocation> activeServants = Maps.newLinkedHashMap();

    private Set<ResourceLocation> servants = Sets.newHashSet();
    private Set<EnumServantType> servantClasses = Sets.newHashSet();

    private State state = State.NOTHING;

    private int joinTicker, winningDelay, timeToNextServant, spawnedServants;

    private List<UUID> sheduledPlayerRemoval = Lists.newArrayList();

    public GrailWarHandler() {
        this(identifier);
    }

    private GrailWarHandler(String identifier) {
        super(identifier);
    }

    public static GrailWarHandler get(World world) {
        return world.getServer().getOverworld().getSavedData().getOrCreate(GrailWarHandler::new, identifier);
    }

    public boolean join(ServerPlayerEntity player) {
        UUID uuid = player.getUniqueID();
        if (this.players.containsKey(uuid) && this.spawnedServants < Config.Common.maxPlayer) {
            EntityServant servant = player.getCapability(PlayerCapProvider.PlayerCap).map(cap -> cap.getServant(player)).orElse(null);
            if (servant != null) {
                if (this.addServant(servant)) {
                    this.players.put(uuid, player.getName().getString()); //<-----------------
                }
                if (this.state == State.NOTHING) {
                    this.joinTicker = Config.Common.joinTime;
                    player.world.getServer().getPlayerList().broadcastChatMessage(new TranslationTextComponent("chat.grailwar.init", this.joinTicker / 20).formatted(TextFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
                    this.state = State.JOIN;
                }
                this.markDirty();
                return true;
            }
        }
        return false;
    }

    public boolean canJoin(ServerPlayerEntity player) {
        return !this.players.containsKey(player.getUniqueID()) && this.state != State.RUN;
    }

    public boolean removePlayer(ServerPlayerEntity player) {
        if (this.hasPlayer(player)) {
            this.players.remove(player.getUniqueID());
            player.getCapability(PlayerCapProvider.PlayerCap).ifPresent(cap -> {
                cap.setCommandSeals(player, 0);
                cap.setServant(player, null);
            });
            TruceHandler.get(player.world).disbandAll(player);
            player.world.getServer().getPlayerList().broadcastChatMessage(new TranslationTextComponent("chat.grailwar.playerout", player.getName()).formatted(TextFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
            this.checkWinCondition(player.getServerWorld(), true);
            this.markDirty();
        }
        return false;
    }

    public void checkWinCondition(ServerWorld world, boolean announce) {
        if (this.state == State.RUN) {
            if (this.activeServants.size() == 1 && this.spawnedServants >= Config.Common.maxPlayer && this.players.size() == 1) {
                this.winningDelay = Config.Common.rewardDelay;
                this.state = State.FINISH;
                for (Map.Entry<UUID, String> entry : this.players.entrySet()) {
                    world.getServer().getPlayerList().broadcastChatMessage(new TranslationTextComponent("chat.grailwar.win", entry.getValue()).formatted(TextFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
                    PlayerEntity player = world.getPlayerByUuid(entry.getKey());
                    if (player != null)
                        ;//AdvancementRegister.grailWarTrigger.trigger(player, false);
                }
            } else if (this.players.size() == 0)
                this.reset(world);
        }
    }

    public boolean hasPlayer(ServerPlayerEntity player) {
        return this.players.containsKey(player.getUniqueID());
    }

    public boolean removeConnection(ServerPlayerEntity player) {
        return this.sheduledPlayerRemoval.remove(player.getUniqueID());
    }

    public Map<UUID, String> players() {
        return ImmutableMap.copyOf(this.players.entrySet());
    }

    public void clientSetPlayers(Map<UUID, String> map) {
        this.players = map;
    }

    public PlayerEntity winner(ServerWorld world) {
        if (this.players.size() != 1)
            return null;
        return world.getPlayerByUuid(this.players.keySet().iterator().next());
    }

    public boolean addServant(EntityServant servant) {
        return false;
    }

    public boolean removeServant(EntityServant servant) {
        return false;
    }

    public void start(World world) {
        this.state = State.RUN;
        if (this.players.size() >= Config.Common.minPlayer) {
            world.getServer().getPlayerList().broadcastChatMessage(new TranslationTextComponent("chat.grailwar.start").formatted(TextFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
        } else if (this.players.size() == 0)
            this.reset(world);
        else {
            this.joinTicker = Config.Common.joinTime;
            this.state = State.JOIN;
            world.getServer().getPlayerList().broadcastChatMessage(new TranslationTextComponent("chat.grailwar.missingplayer").formatted(TextFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
        }
        this.markDirty();
    }

    public void tick(World world) {

    }

    public void reset(World world) {
        if (!world.isRemote) {
            this.players.clear();
            this.joinTicker = 0;
            this.winningDelay = 0;
            this.timeToNextServant = 0;
            this.state = State.NOTHING;
            this.activeServants.forEach((uuid, res) -> {
                EntityServant servant = EntityUtil.findFromUUID(EntityServant.class, world, uuid);
                if (servant != null)
                    servant.attackEntityFrom(DamageSource.OUT_OF_WORLD, Integer.MAX_VALUE);
            });
            this.activeServants.clear();
            this.servants.clear();
            this.servantClasses.clear();
            this.spawnedServants = 0;
            world.getServer().getPlayerList().broadcastChatMessage(new TranslationTextComponent("chat.grailwar.end").formatted(TextFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
            //PacketHandler.sendToAll(new MessageWarTracker(world));
        }
        this.markDirty();
    }

    public boolean canSpawnMoreServants() {
        return false;
    }

    public boolean canSpawnServant(EntityServant servant) {
        if (!Config.Common.allowDuplicateServant && this.servants.contains(servant.getType().getRegistryName()))
            return false;
        if (!Config.Common.allowDuplicateClass && this.servantClasses.contains(servant.getServantType()))
            return false;
        return true;
    }

    private EntityServant tryGetServant(World world) {
        return null;
    }

    @Override
    public void read(CompoundNBT compound) {

    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        return compound;
    }

    enum State {
        JOIN,
        RUN,
        FINISH,
        NOTHING;
    }
}
