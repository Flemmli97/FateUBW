package io.github.flemmli97.fateubw.common.entity;

import com.mojang.serialization.DynamicOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class HeldEquipmentHandler {

    private final LivingEntity entity;
    private boolean inUse;
    @Nullable
    private ItemStack main, off;
    @Nullable
    private ItemStack originalMain, originalOff;

    public HeldEquipmentHandler(LivingEntity entity, @Nullable ItemStack main, @Nullable ItemStack off) {
        this.entity = entity;
        this.main = main;
        this.off = off;
    }

    public void setInUse(boolean inUse) {
        boolean prev = this.inUse;
        this.inUse = inUse;
        if (prev != this.inUse) {
            this.handleSwap();
        }
    }

    private void handleSwap() {
        if (this.inUse) {
            ItemStack toSetMain = this.getMainHandStack();
            ItemStack current = this.entity.getMainHandItem();
            if (toSetMain == null) {
                toSetMain = current;
            }
            ItemStack toSetOff = this.getOffHandStack();
            ItemStack currentOff = this.entity.getOffhandItem();
            if (toSetOff == null) {
                toSetOff = currentOff;
            }
            this.originalMain = current;
            this.originalOff = currentOff;
            this.entity.setItemSlot(EquipmentSlot.MAINHAND, toSetMain.copy());
            this.entity.setItemSlot(EquipmentSlot.OFFHAND, toSetOff.copy());
        } else {
            // Copy over changes
            ItemStack current = this.entity.getMainHandItem();
            if (this.main != null && current.is(this.main.getItem())) {
                this.main = current.copy();
            }
            ItemStack currentOff = this.entity.getOffhandItem();
            if (this.off != null && currentOff.is(this.off.getItem())) {
                this.off = currentOff.copy();
            }
            if (this.originalMain != null) {
                this.entity.setItemSlot(EquipmentSlot.MAINHAND, this.originalMain);
            }
            if (this.originalOff != null) {
                this.entity.setItemSlot(EquipmentSlot.OFFHAND, this.originalOff);
            }
            this.originalMain = null;
            this.originalOff = null;
        }
    }

    @Nullable
    public ItemStack getMainHandStack() {
        if (!this.inUse)
            return null;
        if (this.main == null)
            return null;
        ItemStack stack = this.entity.getMainHandItem();
        if (!stack.is(this.main.getItem()))
            return this.main;
        return null;
    }

    @Nullable
    public ItemStack getOffHandStack() {
        if (!this.inUse)
            return null;
        if (this.off == null)
            return null;
        ItemStack stack = this.entity.getOffhandItem();
        if (!stack.is(this.off.getItem()))
            return this.off;
        return null;
    }

    public CompoundTag save(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("InUse", this.inUse);
        DynamicOps<Tag> ops = provider.createSerializationContext(NbtOps.INSTANCE);
        if (this.main != null) {
            tag.put("MainStack", ItemStack.OPTIONAL_CODEC.encodeStart(ops, this.main).getOrThrow());
        }
        if (this.off != null) {
            tag.put("OffStack", ItemStack.OPTIONAL_CODEC.encodeStart(ops, this.off).getOrThrow());
        }
        if (this.originalMain != null) {
            tag.put("OriginalMainStack", ItemStack.OPTIONAL_CODEC.encodeStart(ops, this.originalMain).getOrThrow());
        }
        if (this.originalOff != null) {
            tag.put("OriginalOffStack", ItemStack.OPTIONAL_CODEC.encodeStart(ops, this.originalOff).getOrThrow());
        }
        return tag;
    }

    public void read(CompoundTag tag, HolderLookup.Provider provider) {
        this.inUse = tag.getBoolean("InUse");
        DynamicOps<Tag> ops = provider.createSerializationContext(NbtOps.INSTANCE);
        if (tag.contains("MainStack")) {
            ItemStack.OPTIONAL_CODEC.parse(ops, tag.get("MainStack"))
                    .ifSuccess(s -> this.main = s);
        }
        if (tag.contains("OffStack")) {
            ItemStack.OPTIONAL_CODEC.parse(ops, tag.get("OffStack"))
                    .ifSuccess(s -> this.off = s);
        }
        if (tag.contains("OriginalMainStack")) {
            ItemStack.OPTIONAL_CODEC.parse(ops, tag.get("OriginalMainStack"))
                    .ifSuccess(s -> this.originalMain = s);
        }
        if (tag.contains("OriginalOffStack")) {
            ItemStack.OPTIONAL_CODEC.parse(ops, tag.get("OriginalOffStack"))
                    .ifSuccess(s -> this.originalOff = s);
        }
    }
}
