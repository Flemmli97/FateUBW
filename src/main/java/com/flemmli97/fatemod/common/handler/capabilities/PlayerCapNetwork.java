package com.flemmli97.fatemod.common.handler.capabilities;

import java.util.UUID;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class PlayerCapNetwork implements IStorage<IPlayer>{
	
	@Override
	public NBTBase writeNBT(Capability<IPlayer> capability, IPlayer instance, EnumFacing side) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("commands", instance.getCommandSeals());
		compound.setFloat("mana", instance.getMana());
		if(instance.getServant()!=null)
		{
			compound.setString("servant", instance.getServant().getUniqueID().toString());
		}
		return compound;
	}

	@Override
	public void readNBT(Capability<IPlayer> capability, IPlayer instance, EnumFacing side, NBTBase nbt) {
		instance.setMana(null, ((NBTTagCompound) nbt).getFloat("mana"));
		instance.setCommandSeals(null, ((NBTTagCompound) nbt).getInteger("commands"));
		World world = FMLCommonHandler.instance().getMinecraftServerInstance().getServer().getEntityWorld();
		if (!world.isRemote)
		{
			for (final Object obj : world.loadedEntityList)
			{
				if (obj instanceof EntityServant)
				{
					EntityServant servant = (EntityServant)obj;
					if(((NBTTagCompound)nbt).hasKey("servant") && ((NBTTagCompound)nbt).getString("servant") != null)
					{
						UUID loadedUUID = UUID.fromString(((NBTTagCompound)nbt).getString("servant"));
						if (servant.getUniqueID().equals(loadedUUID))
						{
							instance.setServant(servant);
						}
					}
				}
			}
		}
	}
}
