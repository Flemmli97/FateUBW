package io.github.flemmli97.fateubw.common.entity;

import com.mojang.serialization.DynamicOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class HeldEquipmentHandler {

    private final LivingEntity entity;
    private boolean inUse;
    @Nullable
    private ItemStack main, off;

    public HeldEquipmentHandler(LivingEntity entity, @Nullable ItemStack main, @Nullable ItemStack off) {
        this.entity = entity;
        this.main = main;
        this.off = off;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
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
        if (this.main != null)
            tag.put("MainStack", ItemStack.OPTIONAL_CODEC.encodeStart(ops, this.main).getOrThrow());
        if (this.off != null)
            tag.put("OffStack", ItemStack.OPTIONAL_CODEC.encodeStart(ops, this.off).getOrThrow());
        return tag;
    }

    public void read(CompoundTag tag, HolderLookup.Provider provider) {
        this.inUse = tag.getBoolean("InUse");
        DynamicOps<Tag> ops = provider.createSerializationContext(NbtOps.INSTANCE);
        if (tag.contains("MainStack"))
            ItemStack.OPTIONAL_CODEC.parse(ops, tag.get("MainStack"))
                    .ifSuccess(s -> this.main = s);
        if (tag.contains("OffStack"))
            ItemStack.OPTIONAL_CODEC.parse(ops, tag.get("OffStack"))
                    .ifSuccess(s -> this.off = s);
    }
}
