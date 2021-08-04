package io.github.flemmli97.fate.common.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CapabilityInsts {

    @CapabilityInject(PlayerCap.class)
    public static final Capability<PlayerCap> PlayerCap = null;
    @CapabilityInject(ItemStackCap.class)
    public static final Capability<ItemStackCap> ItemStackCap = null;

    public static class PlayerCapNetwork implements Capability.IStorage<PlayerCap> {

        @Override
        public INBT writeNBT(Capability<PlayerCap> capability, PlayerCap instance, Direction side) {
            return instance.writeToNBT(new CompoundNBT());
        }

        @Override
        public void readNBT(Capability<PlayerCap> capability, PlayerCap instance, Direction side, INBT nbt) {
            instance.readFromNBT((CompoundNBT) nbt);
        }
    }

    public static class ItemStackNetwork implements Capability.IStorage<ItemStackCap> {

        @Override
        public INBT writeNBT(Capability<ItemStackCap> capability, ItemStackCap instance, Direction side) {
            return null;
        }

        @Override
        public void readNBT(Capability<ItemStackCap> capability, ItemStackCap instance, Direction side, INBT nbt) {

        }
    }
}
