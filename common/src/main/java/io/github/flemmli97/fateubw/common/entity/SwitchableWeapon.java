package io.github.flemmli97.fateubw.common.entity;

import net.minecraft.nbt.CompoundTag;
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
        this.switchItems(alreadySwitched, false);
    }

    public void switchItems(boolean alreadySwitched, boolean clientUpdateFast) {
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

    public void save(CompoundTag nbt) {
        CompoundTag save = new CompoundTag();
        save.putBoolean("Switched", this.switched);
        save.put("SwitchMain", this.main.save(new CompoundTag()));
        save.put("SwitchOff", this.off.save(new CompoundTag()));
        nbt.put("SwitchStates", save);
    }

    public void read(CompoundTag nbt) {
        CompoundTag tag = nbt.getCompound("SwitchStates");
        this.switched = tag.getBoolean("Switched");
        this.main = ItemStack.of(tag.getCompound("SwitchMain"));
        this.off = ItemStack.of(tag.getCompound("SwitchOff"));
        if (this.switched)
            this.switchItems(true);
    }
}
