package io.github.flemmli97.fateubw.forge.common.capability;

import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerCap extends PlayerData implements ICapabilitySerializable<CompoundTag> {

    private final LazyOptional<PlayerData> instance = LazyOptional.of(() -> this);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CapabilityInsts.PLAYERCAP.orEmpty(cap, this.instance);
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.writeToNBT(new CompoundTag());
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.readFromNBT(nbt);
    }
}