package com.flemmli97.fate.common.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class PlayerCapNetwork implements Capability.IStorage<IPlayer> {

    @Override
    public INBT writeNBT(Capability<IPlayer> capability, IPlayer instance, Direction side) {
        return instance.writeToNBT(new CompoundNBT());
    }

    @Override
    public void readNBT(Capability<IPlayer> capability, IPlayer instance, Direction side, INBT nbt) {
        instance.readFromNBT((CompoundNBT) nbt);
    }
}
