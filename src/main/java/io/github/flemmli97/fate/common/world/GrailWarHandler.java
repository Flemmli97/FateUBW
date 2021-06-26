package io.github.flemmli97.fate.common.world;

import com.flemmli97.tenshilib.common.entity.EntityUtil;
import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import io.github.flemmli97.fate.common.capability.CapabilityInsts;
import io.github.flemmli97.fate.common.config.Config;
import io.github.flemmli97.fate.common.entity.servant.EntityServant;
import io.github.flemmli97.fate.common.registry.AdvancementRegister;
import io.github.flemmli97.fate.common.registry.ModEntities;
import io.github.flemmli97.fate.common.registry.ModItems;
import io.github.flemmli97.fate.common.utils.EnumServantType;
import io.github.flemmli97.fate.common.utils.SummonUtils;
import io.github.flemmli97.fate.common.utils.Utils;
import io.github.flemmli97.fate.network.PacketHandler;
import io.github.flemmli97.fate.network.S2CWarData;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.eventbus.api.Event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class GrailWarHandler extends WorldSavedData {

    private static final String identifier = "GrailWarTracker";

    private Set<UUID> players = new HashSet<>();

    private Set<UUID> activeServants = new HashSet<>();
    private Set<ResourceLocation> servants = new HashSet<>();
    private Set<EnumServantType> servantClasses = new HashSet<>();
    private int spawnedServants;

    private State state = State.NOTHING;

    private int joinTicker, winningDelay, timeToNextServant;

    private List<UUID> sheduledPlayerRemoval = new ArrayList<>();

    private final ServantTracker chunkReload = new ServantTracker();

    public GrailWarHandler() {
        this(identifier);
    }

    private GrailWarHandler(String identifier) {
        super(identifier);
    }

    public static GrailWarHandler get(ServerWorld world) {
        return world.getServer().func_241755_D_().getSavedData().getOrCreate(GrailWarHandler::new, identifier);
    }

    public boolean join(ServerPlayerEntity player) {
        UUID uuid = player.getUniqueID();
        if (this.canJoin(player)) {
            EntityServant servant = Utils.getServant(player);
            if (servant != null) {
                if (this.addServant(servant)) {
                    this.players.add(uuid); //<-----------------
                    if (this.state == State.NOTHING) {
                        this.joinTicker = Config.Common.joinTime;
                        player.world.getServer().getPlayerList().func_232641_a_(new TranslationTextComponent("chat.grailwar.init", this.joinTicker / 20).mergeStyle(TextFormatting.RED), ChatType.SYSTEM, Util.DUMMY_UUID);
                        this.state = State.JOIN;
                    }
                    this.markDirty();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canJoin(ServerPlayerEntity player) {
        return !this.players.contains(player.getUniqueID()) && this.state != State.RUN && this.spawnedServants < Config.Common.maxPlayer;
    }

    /**
     * Removes the player only
     */
    public boolean removePlayer(ServerPlayerEntity player) {
        if (this.hasPlayer(player)) {
            this.players.remove(player.getUniqueID());
            player.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> {
                cap.setCommandSeals(player, 0);
                cap.setServant(player, null);
            });
            TruceHandler.get(player.getServerWorld()).disbandAll(player);
            player.world.getServer().getPlayerList().func_232641_a_(new TranslationTextComponent("chat.grailwar.playerout", player.getName()).mergeStyle(TextFormatting.RED), ChatType.SYSTEM, Util.DUMMY_UUID);
            this.markDirty();
            return true;
        }
        return false;
    }

    public void checkWinCondition(ServerWorld world, boolean announce) {
        if (this.state == State.RUN) {
            if (this.activeServants.size() == 1 && this.spawnedServants >= Config.Common.maxPlayer && this.players.size() == 1) {
                this.winningDelay = Config.Common.rewardDelay;
                this.state = State.FINISH;
                for (UUID uuid : this.players) {
                    PlayerEntity player = world.getPlayerByUuid(uuid);
                    String name;
                    if (player instanceof ServerPlayerEntity) {
                        AdvancementRegister.grailWarTrigger.trigger((ServerPlayerEntity) player, false);
                        name = player.getGameProfile().getName();
                    } else {
                        GameProfile prof = player.getServer().getPlayerProfileCache().getProfileByUUID(uuid);
                        name = prof != null ? prof.getName() : "MISSINGNO";
                    }
                    world.getServer().getPlayerList().func_232641_a_(new TranslationTextComponent("chat.grailwar.win", name).mergeStyle(TextFormatting.RED), ChatType.SYSTEM, Util.DUMMY_UUID);
                }
                this.markDirty();
            } else if (this.players.size() == 0)
                this.reset(world);
        }
    }

    public boolean hasPlayer(ServerPlayerEntity player) {
        return this.players.contains(player.getUniqueID());
    }

    public boolean removeConnection(ServerPlayerEntity player) {
        return this.sheduledPlayerRemoval.remove(player.getUniqueID());
    }

    public Set<UUID> players() {
        return ImmutableSet.copyOf(this.players);
    }

    public PlayerEntity winner(ServerWorld world) {
        if (this.players.size() != 1)
            return null;
        return world.getPlayerByUuid(this.players.iterator().next());
    }

    public boolean addServant(EntityServant servant) {
        if (!this.activeServants.contains(servant.getUniqueID()) && this.canSpawnServant(servant)) {
            this.activeServants.add(servant.getUniqueID());
            this.servantClasses.add(servant.getServantType());
            this.servants.add(servant.getType().getRegistryName());
            this.spawnedServants++;
            return true;
        }
        return false;
    }

    public boolean removeServant(EntityServant servant) {
        boolean removed = this.activeServants.remove(servant.getUniqueID());
        if (removed && !servant.world.isRemote) {
            PlayerEntity player = servant.getOwner();
            if (player != null) {
                servant.setOwner(null);
                this.removePlayer((ServerPlayerEntity) player);
            } else if (servant.hasOwner())
                this.sheduledPlayerRemoval.add(servant.getOwnerUUID());
            this.checkWinCondition((ServerWorld) servant.world, true);
            this.markDirty();
            return true;
        }
        return false;
    }

    public void start(ServerWorld world) {
        this.state = State.RUN;
        if (this.players.size() >= Config.Common.minPlayer) {
            world.getServer().getPlayerList().func_232641_a_(new TranslationTextComponent("chat.grailwar.start").mergeStyle(TextFormatting.RED), ChatType.SYSTEM, Util.DUMMY_UUID);
        } else if (this.players.size() == 0)
            this.reset(world);
        else {
            this.joinTicker = Config.Common.joinTime;
            this.state = State.JOIN;
            world.getServer().getPlayerList().func_232641_a_(new TranslationTextComponent("chat.grailwar.missingplayer").mergeStyle(TextFormatting.RED), ChatType.SYSTEM, Util.DUMMY_UUID);
        }
        this.markDirty();
    }

    public void tick(ServerWorld world) {
        this.chunkReload.onLoad(world);
        if (this.state == State.JOIN) {
            if (--this.joinTicker == 0)
                this.start(world);
        } else if (this.state == State.RUN) {
            if (Config.Common.fillMissingSlots && this.spawnedServants < Config.Common.maxPlayer && --this.timeToNextServant <= 0) {
                this.trySpawnNPCServant(world);
            }
        } else if (this.state == State.FINISH) {
            if (--this.winningDelay <= 0) {
                PlayerEntity player = this.winner(world);
                if (player != null) {
                    ItemEntity holyGrail = new ItemEntity(player.world, player.getPosX() + world.rand.nextInt(9) - 4, player.getPosY(), player.getPosZ() + world.rand.nextInt(9) - 4, new ItemStack(ModItems.grail.get()));
                    holyGrail.lifespan = 6000;
                    holyGrail.setOwnerId(player.getUniqueID());
                    holyGrail.setInvulnerable(true);
                    holyGrail.setGlowing(true);
                    world.addEntity(holyGrail);
                    player.getCapability(CapabilityInsts.PlayerCap, null).ifPresent(cap -> cap.saveServant(player));
                }
                this.reset(world);
            }
        }
        this.markDirty();
    }

    public void reset(ServerWorld world) {
        if (!world.isRemote) {
            this.players.clear();
            this.joinTicker = 0;
            this.winningDelay = 0;
            this.timeToNextServant = 0;
            this.state = State.NOTHING;
            this.activeServants.forEach(uuid -> {
                EntityServant servant = EntityUtil.findFromUUID(EntityServant.class, world, uuid);
                if (servant != null)
                    servant.attackEntityFrom(DamageSource.OUT_OF_WORLD, Integer.MAX_VALUE);
            });
            this.activeServants.clear();
            this.servants.clear();
            this.servantClasses.clear();
            this.spawnedServants = 0;
            world.getServer().getPlayerList().func_232641_a_(new TranslationTextComponent("chat.grailwar.end").mergeStyle(TextFormatting.RED), ChatType.SYSTEM, Util.DUMMY_UUID);
            PacketHandler.sendToAll(new S2CWarData(world));
        }
        this.markDirty();
    }

    public boolean canSpawnMoreServants() {
        for (ResourceLocation loc : ModEntities.registeredServants()) {
            if (!Config.Common.allowDuplicateServant && this.servants.contains(loc))
                return false;
            if (!Config.Common.allowDuplicateClass && this.servantClasses.contains(ModEntities.get(loc)))
                return false;
        }
        return true;
    }

    public boolean canSpawnServant(EntityServant servant) {
        if (!Config.Common.allowDuplicateServant && this.servants.contains(servant.getType().getRegistryName()))
            return false;
        return Config.Common.allowDuplicateClass || !this.servantClasses.contains(servant.getServantType());
    }

    private void trySpawnNPCServant(ServerWorld world) {
        List<PlayerEntity> players = new ArrayList<>();
        world.getPlayers().forEach(player -> {
            if (this.players.contains(player.getUniqueID()))
                players.add(player);
        });
        int spawns = world.rand.nextInt(Config.Common.maxServantCircle) + 1;
        for (int i = 0; i < spawns; i++) {
            if (players.isEmpty())
                return;
            PlayerEntity player = players.remove(world.rand.nextInt(players.size()));
            int x = player.getPosition().getX() + world.rand.nextInt(64) + 48;
            int z = player.getPosition().getZ() + world.rand.nextInt(64) + 48;
            Chunk chunk = world.getChunk(x >> 4, z >> 4);
            int y = chunk.getTopBlockY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z) + 1;
            EntityServant servant = SummonUtils.randomServant(world);
            if (servant != null) {
                servant.setLocationAndAngles(x, y, z, world.rand.nextFloat() * 360.0F, 0);
                EntitySpawnPlacementRegistry.PlacementType place = EntitySpawnPlacementRegistry.getPlacementType(servant.getType());
                Event.Result canSpawn = net.minecraftforge.event.ForgeEventFactory.canEntitySpawn(servant, world, x, y, z, null, SpawnReason.TRIGGERED);
                if (canSpawn == Event.Result.ALLOW ||
                        (canSpawn == Event.Result.DEFAULT && (WorldEntitySpawner.canSpawnAtBody(place, world, servant.getPosition(), servant.getType()) && servant.isNotColliding(world)))) {
                    servant.onInitialSpawn(world, world.getDifficultyForLocation(servant.getPosition()), SpawnReason.NATURAL, null, null);
                    ChunkPos cpos = new ChunkPos(x >> 4, z >> 4);
                    world.getChunkProvider().registerTicket(TicketType.UNKNOWN, cpos, 9, cpos);
                    world.addEntity(servant);
                    this.addServant(servant);
                    this.timeToNextServant = MathHelper.nextInt(world.rand, Config.Common.servantMinSpawnDelay, Config.Common.servantMaxSpawnDelay);
                    if (!this.notify(servant.getType().getRegistryName()))
                        if (Config.Common.notifyAll)
                            world.getServer().getPlayerList().func_232641_a_(new TranslationTextComponent("chat.grailwar.spawn", player.getName()).mergeStyle(TextFormatting.GOLD), ChatType.SYSTEM, Util.DUMMY_UUID);
                        else
                            player.sendMessage(new TranslationTextComponent("chat.grailwar.spawn", player.getName()).mergeStyle(TextFormatting.GOLD), Util.DUMMY_UUID);
                }
            }
        }
    }

    private boolean notify(ResourceLocation loc) {
        return Config.Common.whiteList == Config.Common.notifyBlackList.contains(loc);
    }

    public void untrack(EntityServant servant) {
        this.chunkReload.remove(servant);
    }

    public void track(EntityServant servant) {
        this.chunkReload.add(servant);
    }

    @Override
    public void read(CompoundNBT compound) {
        ListNBT tag = compound.getList("Players", Constants.NBT.TAG_INT_ARRAY);
        tag.forEach(s -> this.players.add(NBTUtil.readUniqueId(s)));
        ListNBT tag2 = compound.getList("ActiveServants", Constants.NBT.TAG_INT_ARRAY);
        tag2.forEach(s -> this.activeServants.add(NBTUtil.readUniqueId(s)));
        ListNBT list = compound.getList("Servants", Constants.NBT.TAG_STRING);
        list.forEach(s -> this.servants.add(new ResourceLocation(s.getString())));
        ListNBT list2 = compound.getList("ServantClasses", Constants.NBT.TAG_STRING);
        list2.forEach(s -> this.servantClasses.add(EnumServantType.valueOf(s.getString())));
        ListNBT list3 = compound.getList("ToRemove", Constants.NBT.TAG_INT_ARRAY);
        list3.forEach(s -> this.sheduledPlayerRemoval.add(NBTUtil.readUniqueId(s)));
        this.joinTicker = compound.getInt("Ticker");
        this.winningDelay = compound.getInt("WinDelay");
        this.timeToNextServant = compound.getInt("SpawnTick");
        this.state = State.valueOf(compound.getString("State"));
        this.spawnedServants = compound.getInt("SpawnedServants");
        this.chunkReload.load(compound.getCompound("ChunkLoading"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ListNBT tag = new ListNBT();
        this.players.forEach(uuid -> tag.add(NBTUtil.func_240626_a_(uuid)));
        compound.put("Players", tag);
        ListNBT tag2 = new ListNBT();
        this.activeServants.forEach(uuid -> tag2.add(NBTUtil.func_240626_a_(uuid)));
        compound.put("ActiveServants", tag2);
        ListNBT list = new ListNBT();
        this.servants.forEach(res -> list.add(StringNBT.valueOf(res.toString())));
        compound.put("Servants", list);
        ListNBT list2 = new ListNBT();
        this.servantClasses.forEach(e -> list2.add(StringNBT.valueOf(e.toString())));
        compound.put("ServantClasses", list2);
        ListNBT list3 = new ListNBT();
        this.sheduledPlayerRemoval.forEach(uuid -> list3.add(NBTUtil.func_240626_a_(uuid)));
        compound.put("ToRemove", list3);
        compound.putInt("Ticker", this.joinTicker);
        compound.putInt("WinDelay", this.winningDelay);
        compound.putInt("SpawnTick", this.timeToNextServant);
        compound.putString("State", this.state.toString());
        compound.putInt("SpawnedServants", this.spawnedServants);
        compound.put("ChunkLoading", this.chunkReload.onSave(new CompoundNBT()));
        return compound;
    }

    enum State {
        JOIN,
        RUN,
        FINISH,
        NOTHING
    }
}