package io.github.flemmli97.fate.common.capability;

import io.github.flemmli97.fate.network.PacketHandler;
import io.github.flemmli97.fate.network.S2CItemInUse;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemStackCap implements ICapabilityProvider {

    private final LazyOptional<ItemStackCap> instance = LazyOptional.of(() -> this);

    private boolean inUse;

    public void setInUse(LivingEntity entity, boolean inUse, boolean mainHand) {
        this.inUse = inUse;
        if (!entity.world.isRemote) {
            PacketHandler.sendToTracking(new S2CItemInUse(entity.getEntityId(), this.inUse, mainHand), entity);
        }
    }

    public boolean inUse() {
        return this.inUse;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityInsts.ItemStackCap.orEmpty(cap, this.instance);
    }
}
