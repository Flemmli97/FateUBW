package io.github.flemmli97.fate.common.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;

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
            ItemStack main = this.entity.getHeldItemMainhand();
            ItemStack off = this.entity.getHeldItemOffhand();
            this.entity.setHeldItem(Hand.MAIN_HAND, this.main);
            this.entity.setHeldItem(Hand.OFF_HAND, this.off);
            this.main = main;
            this.off = off;
        }
    }

    public void save(CompoundNBT nbt) {
        CompoundNBT save = new CompoundNBT();
        save.putBoolean("Switched", this.switched);
        save.put("SwitchMain", this.main.serializeNBT());
        save.put("SwitchOff", this.off.serializeNBT());
        nbt.put("SwitchStates", save);
    }

    public void read(CompoundNBT nbt) {
        CompoundNBT tag = nbt.getCompound("SwitchStates");
        this.switched = tag.getBoolean("Switched");
        this.main = ItemStack.read(tag.getCompound("SwitchMain"));
        this.off = ItemStack.read(tag.getCompound("SwitchOff"));
        if (this.switched)
            this.switchItems(true);
    }
}
