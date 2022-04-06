package io.github.flemmli97.fateubw.common.blocks.tile;

import io.github.flemmli97.fateubw.common.items.ItemServantCharm;
import io.github.flemmli97.fateubw.common.network.S2CAltarUpdate;
import io.github.flemmli97.fateubw.common.registry.ModBlocks;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.EnumServantType;
import io.github.flemmli97.fateubw.common.utils.SummonUtils;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AltarBlockEntity extends BlockEntity {

    private boolean isComplete, isSummoning;
    private ItemStack inventoryCharm = ItemStack.EMPTY;
    private final NonNullList<ItemStack> invCatalyst = NonNullList.withSize(8, ItemStack.EMPTY);
    private int summoningTick, tick;
    private Player player;

    public AltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.tileAltar.get(), pos, state);
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

    public void setComplete(boolean flag) {
        this.isComplete = flag;
    }

    public boolean addItem(Player player, ItemStack stack) {
        if (stack.getItem() instanceof ItemServantCharm && ((ItemServantCharm) stack.getItem()).type != EnumServantType.NOTASSIGNED) {
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
        } else if (stack.getItem() == ModItems.crystalCluster.get()) {
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
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (compound.contains("Charm")) {
            CompoundTag tag = compound.getCompound("Charm");
            this.inventoryCharm = ItemStack.of(tag);
        }
        ContainerHelper.loadAllItems(compound, this.invCatalyst);
        this.isComplete = compound.getBoolean("complete");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        if (!this.inventoryCharm.isEmpty())
            compound.put("Charm", this.inventoryCharm.save(new CompoundTag()));
        ContainerHelper.saveAllItems(compound, this.invCatalyst);
        compound.putBoolean("complete", this.isComplete);
    }

    public void setSummoning(Player player) {
        this.isSummoning = true;
        this.player = player;
        if (this.getLevel() instanceof ServerLevel serverLevel)
            NetworkCalls.INSTANCE.sendToTracking(new S2CAltarUpdate(this.getBlockPos(), this.isSummoning), serverLevel, this.getBlockPos());
    }

    public static void ticker(Level level, BlockPos pos, BlockState state, AltarBlockEntity altar) {
        if (level instanceof ServerLevel serverLevel) {
            if (altar.isSummoning) {
                altar.summoningTick++;
                if (altar.summoningTick == 1) {
                    level.playSound(null, altar.worldPosition, SoundEvents.PORTAL_TRAVEL, SoundSource.AMBIENT, 0.4F, 1F);
                }
                if (altar.summoningTick > 150) {
                    SummonUtils.summonRandomServant(altar.inventoryCharm, (ServerPlayer) altar.player, altar.worldPosition, serverLevel);
                    SummonUtils.removeSummoningStructure(level, pos);
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
}