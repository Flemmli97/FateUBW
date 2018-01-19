package com.flemmli97.fatemod.common.handler.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class PlayerCapProvider implements ICapabilitySerializable<NBTBase>{

	@CapabilityInject(IPlayer.class)
    public static final Capability<IPlayer> PlayerCap = null;
    private IPlayer instance = PlayerCap.getDefaultInstance();
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == PlayerCap;
	}

	@Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        return capability == PlayerCap ? PlayerCap.<T> cast(this.instance) : null;
    }

    @Override
    public NBTBase serializeNBT()
    {
        return PlayerCap.getStorage().writeNBT(PlayerCap, this.instance, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
    		PlayerCap.getStorage().readNBT(PlayerCap, this.instance, null, nbt);
    }

}
