package com.flemmli97.fatemod.common.entity;

import com.flemmli97.fatemod.common.items.ItemGemShard;
import com.flemmli97.fatemod.common.items.ItemGemShard.ShardType;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityGem extends EntitySpecialProjectile {

	private int stackMeta;
    protected static final DataParameter<Integer> meta = EntityDataManager.<Integer>createKey(EntityBabylonWeapon.class, DataSerializers.VARINT);

	public EntityGem(World world) {
		super(world);
	}
	
    public EntityGem(World world, EntityLivingBase thrower, ItemGemShard item)
    {
        super(world, thrower, false, false);
        this.dataManager.set(meta, item.getType().ordinal());
    }

    public EntityGem(World world, double d1, double d2, double d3)
    {
        super(world, d1, d2, d3);
    }
    
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(meta, Integer.valueOf(0));
    }
    
    public ShardType getType()
    {
    	return ShardType.values()[this.dataManager.get(meta)];
    }

	@Override
	protected void onImpact(RayTraceResult result) {
		
        if (!this.world.isRemote)
        {
        	world.createExplosion(this, this.posX, this.posY, this.posZ, 2.0F, false);
            this.setDead();
        }		
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("StackMeta", this.stackMeta);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.stackMeta=compound.getInteger("StackMeta");
	}

	
}
