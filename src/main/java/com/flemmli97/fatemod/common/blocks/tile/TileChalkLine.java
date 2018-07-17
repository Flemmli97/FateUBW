package com.flemmli97.fatemod.common.blocks.tile;

import com.flemmli97.fatemod.common.blocks.EnumPositionChalk;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileChalkLine extends TileEntity{
        
	private EnumFacing facing = EnumFacing.NORTH;
	private EnumPositionChalk position = EnumPositionChalk.MIDDLE;
	
	public void setPropertiesFromState(EnumFacing facing, EnumPositionChalk position)
	{
		this.facing = facing;
		this.position = position;
		this.markDirty();
	} 
	
    public EnumFacing getFacing()
    {
    		return this.facing;
    }
    
    public EnumPositionChalk getPosition()
    {
    		return this.position;
    }
    
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) 
	{
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.facing = EnumFacing.VALUES[compound.getInteger("facing")];
		this.position = EnumPositionChalk.fromMeta(compound.getInteger("positionChalk"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("facing", this.facing.getIndex());
		compound.setInteger("positionChalk", this.position.getMetadata());
		return compound;
	}    
}
