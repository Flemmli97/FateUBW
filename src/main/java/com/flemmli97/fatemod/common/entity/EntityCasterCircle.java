package com.flemmli97.fatemod.common.entity;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.handler.ConfigHandler;
import com.flemmli97.tenshilib.common.entity.EntityUtil;
import com.flemmli97.tenshilib.common.javahelper.MathUtils;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityCasterCircle extends Entity{

	private EntityServant owner;
	private UUID ownerUUID;
	private int livingTick;
    protected static final DataParameter<Float> range = EntityDataManager.createKey(EntityCasterCircle.class, DataSerializers.FLOAT);
	private List<float[]> circlePoints;
	
	public EntityCasterCircle(World world) {
		super(world);
		this.setSize(0.1f, 0.1f);
		this.noClip=true;
	}
	
	public EntityCasterCircle(World world, EntityServant owner, float r) {
		this(world);
		this.setPosition(owner.posX, owner.posY, owner.posZ);
		this.owner=owner;
		this.ownerUUID=owner.getUniqueID();
		this.dataManager.set(range, r);
	}

	@Override
	protected void entityInit() {
		this.dataManager.register(range, 1f);
	}

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
		this.livingTick++;
		if(this.world.isRemote && this.livingTick%5==0)
		{
			if(this.circlePoints==null)
				this.circlePoints=MathUtils.pointsOfCircle(this.dataManager.get(range), 10);
			for(float[] f : this.circlePoints)
				this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX+f[0], this.posY+0.2, this.posZ+f[1], 0, 0.1, 0);
		}
		if(!this.world.isRemote)
			if(this.livingTick>ConfigHandler.medeaCircleSpan || this.getOwner()==null || this.getOwner().isDead())
				this.setDead();
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		this.ownerUUID=UUID.fromString(compound.getString("Owner"));
		this.livingTick=compound.getInteger("Ticks");
		this.dataManager.set(range, compound.getFloat("Range"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setString("Owner", this.ownerUUID.toString());
		compound.setInteger("Ticks", this.livingTick);
		compound.setFloat("Range", this.dataManager.get(range));
	}

}
