package io.github.flemmli97.fateubw.common.entity;

import com.mojang.serialization.DynamicOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SwitchableWeapon<T extends LivingEntity> {

    private final T entity;
    private boolean switched;
    private ItemStack main, off;

    public SwitchableWeapon(T entity, ItemStack main, ItemStack off) {
        this.entity = entity;
        this.main = main;
        this.off = off;
    }

    public void switchItems(boolean alreadySwitched) {
        if (!this.needsSwap()) {
            return;
        }
        if (this.switched == alreadySwitched) {
            this.switched = !this.switched;
            ItemStack main = this.entity.getMainHandItem();
            ItemStack off = this.entity.getOffhandItem();
            this.entity.setItemInHand(InteractionHand.MAIN_HAND, this.main);
            this.entity.setItemInHand(InteractionHand.OFF_HAND, this.off);
            this.main = main;
            this.off = off;
        }
    }

    private boolean needsSwap() {
        if (this.switched)
            return true;
        ItemStack main = this.entity.getMainHandItem();
        if (!this.main.isEmpty() && !main.is(this.main.getItem()))
            return true;
        ItemStack off = this.entity.getOffhandItem();
        return !this.off.isEmpty() && !off.is(this.off.getItem());
    }

    public void save(CompoundTag nbt, HolderLookup.Provider provider) {
        CompoundTag save = new CompoundTag();
        save.putBoolean("Switched", this.switched);
        DynamicOps<Tag> ops = provider.createSerializationContext(NbtOps.INSTANCE);
        save.put("SwitchMain", ItemStack.OPTIONAL_CODEC.encodeStart(ops, this.main).getOrThrow());
        save.put("SwitchOff", ItemStack.OPTIONAL_CODEC.encodeStart(ops, this.off).getOrThrow());
        nbt.put("SwitchStates", save);
    }

    public void read(CompoundTag nbt, HolderLookup.Provider provider) {
        CompoundTag tag = nbt.getCompound("SwitchStates");
        this.switched = tag.getBoolean("Switched");
        DynamicOps<Tag> ops = provider.createSerializationContext(NbtOps.INSTANCE);
        this.main = ItemStack.OPTIONAL_CODEC.parse(ops, tag.get("SwitchMain")).getOrThrow();
        this.off = ItemStack.OPTIONAL_CODEC.parse(ops, tag.get("SwitchOff")).getOrThrow();
        if (this.switched)
            this.switchItems(true);
    }
}
