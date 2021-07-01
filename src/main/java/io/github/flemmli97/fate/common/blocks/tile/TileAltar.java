package io.github.flemmli97.fate.common.blocks.tile;

import io.github.flemmli97.fate.common.items.ItemServantCharm;
import io.github.flemmli97.fate.common.registry.ModBlocks;
import io.github.flemmli97.fate.common.registry.ModItems;
import io.github.flemmli97.fate.common.utils.EnumServantType;
import io.github.flemmli97.fate.common.utils.SummonUtils;
import io.github.flemmli97.fate.network.PacketHandler;
import io.github.flemmli97.fate.network.S2CAltarUpdate;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;

public class TileAltar extends TileEntity implements ITickableTileEntity {

    private boolean isComplete, isSummoning;
    private ItemStack inventoryCharm = ItemStack.EMPTY;
    private NonNullList<ItemStack> invCatalyst = NonNullList.withSize(8, ItemStack.EMPTY);
    private int summoningTick, tick;
    private PlayerEntity player;

    public TileAltar() {
        super(ModBlocks.tileAltar.get());
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

    public boolean addItem(PlayerEntity player, ItemStack stack) {
        if (stack.getItem() instanceof ItemServantCharm && ((ItemServantCharm) stack.getItem()).type != EnumServantType.NOTASSIGNED) {
            if (this.inventoryCharm.isEmpty()) {
                ItemStack stackToAdd = stack.copy();
                stackToAdd.setCount(1);
                this.inventoryCharm = stackToAdd;
                if (player != null && !player.isCreative()) {
                    stack.shrink(1);
                }
                this.markDirty();
                if (!this.world.isRemote)
                    PacketHandler.vanillaChunkPkt(this.getUpdatePacket(), (ServerWorld) this.getWorld(), this.getPos());
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
                    this.markDirty();
                    if (!this.world.isRemote)
                        PacketHandler.vanillaChunkPkt(this.getUpdatePacket(), (ServerWorld) this.getWorld(), this.getPos());
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeItem(PlayerEntity player) {
        if (!this.inventoryCharm.isEmpty()) {
            if (player != null && !player.isCreative()) {
                player.inventory.addItemStackToInventory(this.inventoryCharm);
            }
            this.inventoryCharm = ItemStack.EMPTY;
            this.markDirty();
            if (!this.world.isRemote)
                PacketHandler.vanillaChunkPkt(this.getUpdatePacket(), (ServerWorld) this.getWorld(), this.getPos());
            return true;
        }
        for (int x = 0; x < this.invCatalyst.size(); x++) {
            ItemStack inv = this.invCatalyst.get(x);
            if (!inv.isEmpty()) {
                if (player != null && !player.isCreative()) {
                    player.inventory.addItemStackToInventory(inv);
                }
                this.invCatalyst.set(x, ItemStack.EMPTY);
                this.markDirty();
                if (!this.world.isRemote)
                    PacketHandler.vanillaChunkPkt(this.getUpdatePacket(), (ServerWorld) this.getWorld(), this.getPos());
                return true;
            }
        }
        return false;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.read(this.getBlockState(), pkt.getNbtCompound());
        this.getWorld().notifyBlockUpdate(this.pos, this.getWorld().getBlockState(this.pos), this.getWorld().getBlockState(this.pos), 3);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        if (compound.contains("Charm")) {
            CompoundNBT tag = compound.getCompound("Charm");
            this.inventoryCharm = ItemStack.read(tag);
        }
        ItemStackHelper.loadAllItems(compound, this.invCatalyst);
        this.isComplete = compound.getBoolean("complete");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        if (!this.inventoryCharm.isEmpty())
            compound.put("Charm", this.inventoryCharm.write(new CompoundNBT()));
        ItemStackHelper.saveAllItems(compound, this.invCatalyst);
        compound.putBoolean("complete", this.isComplete);
        return compound;
    }

    public void setSummoning(PlayerEntity player) {
        this.isSummoning = true;
        this.player = player;
        PacketHandler.sendToAll(new S2CAltarUpdate(this.getPos(), true));
    }

    @Override
    public void tick() {
        if (!this.world.isRemote) {
            if (this.isSummoning) {
                this.summoningTick++;
                if (this.summoningTick == 1) {
                    this.world.playSound(null, this.pos, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.AMBIENT, 0.4F, 1F);
                }
                if (this.summoningTick > 150) {
                    SummonUtils.summonRandomServant(this.inventoryCharm, (ServerPlayerEntity) this.player, this.pos, (ServerWorld) this.world);
                    SummonUtils.removeSummoningStructure(this.world, this.getPos());
                }
            }
        } else {
            this.tick++;
            if (this.tick > 360)
                this.tick = 0;
            if (this.isSummoning)
                this.summoningTick++;
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