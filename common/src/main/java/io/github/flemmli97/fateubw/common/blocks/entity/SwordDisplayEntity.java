package io.github.flemmli97.fateubw.common.blocks.entity;

import io.github.flemmli97.fateubw.common.blocks.SwordDisplayBlock;
import io.github.flemmli97.fateubw.common.registry.FateBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SwordDisplayEntity extends BlockEntity {

    private Rotation rotation = Rotation.ROTATION_UP;
    private ItemStack display = ItemStack.EMPTY;

    public SwordDisplayEntity(BlockPos pos, BlockState state) {
        super(FateBlocks.SWORD_DISPLAY_BLOCK_ENTITY.get(), pos, state);
    }

    public Rotation rotation() {
        return this.rotation;
    }

    public void flipRotation() {
        this.rotation = this.rotation.cycle();
        this.sendUpdatePacket();
    }

    public ItemStack item() {
        return this.display;
    }

    public boolean setItem(Player player, ItemStack stack) {
        if (this.display.isEmpty() && stack.is(ItemTags.SWORDS)) {
            ItemStack toDisplay = stack.copy();
            toDisplay.setCount(1);
            this.setItem(toDisplay);
            if (player != null && !player.isCreative()) {
                stack.shrink(1);
            }
            this.setChanged();
            this.sendUpdatePacket();
            return true;
        }
        return false;
    }

    public void setItemDirect(ItemStack stack) {
        stack.setCount(1);
        this.setItem(stack);
        this.setChanged();
        this.sendUpdatePacket();
    }

    public boolean removeItem(Player player) {
        if (!this.display.isEmpty()) {
            if (player != null && !player.isCreative()) {
                player.getInventory().add(this.display);
            }
            this.setItem(ItemStack.EMPTY);
            this.setChanged();
            this.sendUpdatePacket();
            return true;
        }
        return false;
    }

    private void setItem(ItemStack stack) {
        this.display = stack;
        if (this.getLevel() != null && !this.getLevel().isClientSide()) {
            this.getLevel().setBlock(this.getBlockPos(), this.getBlockState().setValue(SwordDisplayBlock.FILLED, !this.display.isEmpty()), Block.UPDATE_ALL);
        }
    }

    private void sendUpdatePacket() {
        if (this.level != null && !this.level.isClientSide) {
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
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
        if (compound.contains("Display")) {
            this.display = ItemStack.CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), compound.get("Display")).getOrThrow();
        } else {
            this.display = ItemStack.EMPTY;
        }
        try {
            this.rotation = Rotation.values()[compound.getInt("Rotation")];
        } catch (ArrayIndexOutOfBoundsException e) {
            this.rotation = Rotation.ROTATION_UP;
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);
        if (!this.display.isEmpty())
            compound.put("Display", ItemStack.CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), this.display).getOrThrow());
        compound.putInt("Rotation", this.rotation.ordinal());
    }

    public enum Rotation {
        ROTATION_UP,
        ROTATION_LEFT,
        ROTATION_RIGHT;

        public Rotation cycle() {
            return switch (this) {
                case ROTATION_UP -> ROTATION_LEFT;
                case ROTATION_LEFT -> ROTATION_RIGHT;
                case ROTATION_RIGHT -> ROTATION_UP;
            };
        }
    }
}