package io.github.flemmli97.fateubw.forge.common.capability;

import io.github.flemmli97.fateubw.common.attachment.ItemStackData;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemStackCap extends ItemStackData implements ICapabilityProvider {

    private final LazyOptional<ItemStackData> instance = LazyOptional.of(() -> this);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityInsts.ITEMSTACKCAP.orEmpty(cap, this.instance);
    }
}
