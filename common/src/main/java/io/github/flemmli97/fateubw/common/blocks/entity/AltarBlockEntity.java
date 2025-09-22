package io.github.flemmli97.fateubw.common.blocks.entity;

import io.github.flemmli97.fateubw.api.entity.ServantLike;
import io.github.flemmli97.fateubw.common.network.S2CAltarUpdate;
import io.github.flemmli97.fateubw.common.registry.FateBlocks;
import io.github.flemmli97.fateubw.common.registry.FateDataComponents;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import io.github.flemmli97.fateubw.platform.Platform;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class AltarBlockEntity extends BlockEntity {

    private boolean isComplete, isSummoning;
    private ItemStack inventoryCharm = ItemStack.EMPTY;
    private final NonNullList<ItemStack> invCatalyst = NonNullList.withSize(8, ItemStack.EMPTY);
    private int summoningTick, tick;
    private ServantLike<?> servant;

    public AltarBlockEntity(BlockPos pos, BlockState state) {
        super(FateBlocks.ALTAR_BLOCK_ENTITY.get(), pos, state);
    }

    public static void ticker(Level level, BlockPos pos, BlockState state, AltarBlockEntity altar) {
        if (level instanceof ServerLevel) {
            if (altar.isSummoning) {
                altar.summoningTick++;
                if (altar.summoningTick == 1) {
                    level.playSound(null, altar.worldPosition, SoundEvents.PORTAL_TRAVEL, SoundSource.AMBIENT, 0.4F, 1F);
                }
                if (altar.summoningTick == 150) {
                    if (altar.servant != null) {
                        if (altar.servant.getOwner() != null)
                            altar.servant.get().lookAt(EntityAnchorArgument.Anchor.EYES, altar.servant.getOwner().position());
                        level.addFreshEntity(altar.servant.get());
                    }
                    level.destroyBlock(pos, false);
                }
            }
        } else {
            altar.tick++;
            if (altar.tick > 360)
                altar.tick = 0;
            if (altar.isSummoning)
                altar.summoningTick++;
        }
    }

    public ItemStack getCharm() {
        return this.inventoryCharm;
    }

    public NonNullList<ItemStack> getCatalyst() {
        return this.invCatalyst;
    }

    public boolean isComplete() {
        return this.isComplete;
    }

    public boolean canSummon() {
        return this.isComplete() && !this.isSummoning() && this.invCatalyst.stream().noneMatch(ItemStack::isEmpty);
    }

    public void setComplete(boolean flag) {
        this.isComplete = flag;
        if (!this.level.isClientSide)
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }

    public boolean addItem(Player player, ItemStack stack) {
        if (stack.has(FateDataComponents.CLASS_RELIC.get()) || stack.has(FateDataComponents.SERVANT_RELIC.get())) {
            if (this.inventoryCharm.isEmpty()) {
                ItemStack stackToAdd = stack.copy();
                stackToAdd.setCount(1);
                this.inventoryCharm = stackToAdd;
                if (player != null && !player.isCreative()) {
                    stack.shrink(1);
                }
                this.setChanged();
                if (!this.level.isClientSide)
                    this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
                return true;
            }
        } else if (stack.getItem() == FateItems.MANA_GEM.get()) {
            ItemStack add = stack.copy();
            add.setCount(1);
            for (int x = 0; x < this.invCatalyst.size(); x++) {
                if (this.invCatalyst.get(x).isEmpty()) {
                    this.invCatalyst.set(x, add);
                    if (player != null && !player.isCreative()) {
                        stack.shrink(1);
                    }
                    this.setChanged();
                    if (!this.level.isClientSide)
                        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeItem(Player player) {
        if (!this.inventoryCharm.isEmpty()) {
            if (player != null && !player.isCreative()) {
                player.getInventory().add(this.inventoryCharm);
            }
            this.inventoryCharm = ItemStack.EMPTY;
            this.setChanged();
            if (!this.level.isClientSide)
                this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
            return true;
        }
        for (int x = 0; x < this.invCatalyst.size(); x++) {
            ItemStack inv = this.invCatalyst.get(x);
            if (!inv.isEmpty()) {
                if (player != null && !player.isCreative()) {
                    player.getInventory().add(inv);
                }
                this.invCatalyst.set(x, ItemStack.EMPTY);
                this.setChanged();
                if (!this.level.isClientSide)
                    this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
                return true;
            }
        }
        return false;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::getUpdateTag);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveWithoutMetadata(provider);
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        super.loadAdditional(compound, provider);
        if (compound.contains("Charm")) {
            this.inventoryCharm = ItemStack.CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), compound.get("Charm")).getOrThrow();
        }
        this.invCatalyst.clear();
        ContainerHelper.loadAllItems(compound, this.invCatalyst, provider);
        this.isComplete = compound.getBoolean("complete");
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);
        if (!this.inventoryCharm.isEmpty())
            compound.put("Charm", ItemStack.CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), this.inventoryCharm).getOrThrow());
        ContainerHelper.saveAllItems(compound, this.invCatalyst, provider);
        compound.putBoolean("complete", this.isComplete);
    }

    public boolean setSummoning(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            ServantLike<?> servant = GrailWarHandler.get(serverPlayer.getServer())
                    .summonRandomServant(serverPlayer.serverLevel(), Vec3.atCenterOf(this.worldPosition), serverPlayer, this.inventoryCharm, false, false);
            if (servant != null) {
                this.isSummoning = true;
                LoaderNetwork.INSTANCE.sendToTracking(new S2CAltarUpdate(this.getBlockPos(), this.isSummoning), serverPlayer.serverLevel(), new ChunkPos(this.getBlockPos()));
                Platform.INSTANCE.getPlayerData(serverPlayer).setCommandSeals(3);
                this.servant = servant;
                return true;
            }
        }
        return false;
    }

    public boolean isSummoning() {
        return this.isSummoning;
    }

    public int getSummoningTick() {
        return this.summoningTick;
    }

    public int ticker() {
        return this.tick;
    }

    /**
     * client side only for packet
     */
    public void updateSummoning(boolean flag) {
        this.isSummoning = flag;
    }

    /**
     * ====== Forge
     */
    public AABB getRenderBoundingBox() {
        return new AABB(this.getBlockPos()).inflate(4);
    }
}