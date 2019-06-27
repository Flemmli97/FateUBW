package com.flemmli97.fatemod.common.entity;

import java.util.UUID;

import javax.annotation.Nullable;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.handler.ConfigHandler;
import com.flemmli97.tenshilib.common.entity.EntityUtil;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityCasterCircle extends Entity{

	private EntityServant owner;
	private UUID ownerUUID;
	private int livingTick;
	private float range;
	
	public EntityCasterCircle(World world) {
		super(world);
		this.setSize(0.1f, 0.1f);
		this.noClip=true;
	}
	
	public EntityCasterCircle(World world, EntityServant owner, float range) {
		this(world);
		this.owner=owner;
		this.ownerUUID=owner.getUniqueID();
		this.range=range;
	}

	@Override
	protected void entityInit() {}

	@Nullable
	public EntityServant getOwner()
	{
		if(this.owner!=null)
			return this.owner;
		return EntityUtil.findFromUUID(EntityServant.class, this.world, this.ownerUUID);
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if(this.world.isRemote)
		{
			//Particles
		}
		if(!this.world.isRemote && (this.livingTick+=1)>ConfigHandler.medeaCircleSpan)
			this.setDead();
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		this.ownerUUID=UUID.fromString(compound.getString("Owner"));
		this.livingTick=compound.getInteger("Ticks");
		this.range=compound.getFloat("Range");

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setString("Owner", this.ownerUUID.toString());
		compound.setInteger("Ticks", this.livingTick);
		compound.setFloat("Range", this.range);
	}

}
