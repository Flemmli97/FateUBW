package com.flemmli97.fatemod.common.entity;

import java.util.List;

import com.flemmli97.fatemod.common.entity.servant.EntityIskander;
import com.flemmli97.fatemod.common.handler.CustomDamageSource;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityGordiusWheel extends EntityCreature implements IServantMinion{

	//static AxisAlignedBB wheelBox = new AxisAlignedBB(0, 0, 0, 2.5, 1.5, 3.25);
	//static AxisAlignedBB bullBox = new AxisAlignedBB(0, 0, 0, 2.25, 1.5, 1.75);
	static AxisAlignedBB full = new AxisAlignedBB(0, 0, 0, 2.5, 1.5, 5);
    protected static final DataParameter<Boolean> CHARGING = EntityDataManager.<Boolean>createKey(EntityIronGolem.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> START = EntityDataManager.<Boolean>createKey(EntityIronGolem.class, DataSerializers.BOOLEAN);

	public EntityGordiusWheel(World world) {
		super(world);
		this.stepHeight=1.0F;
		this.setSize(2.5F, 1.5F);
	}
	
	@Override
	protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(CHARGING, false);
        this.dataManager.register(START, false);
    }
	
	
	@Override
	protected void setSize(float width, float height)
    {
        if (width != this.width || height != this.height)
        {
            float f = this.width;
            this.width = width;
            this.height = height;
            this.setEntityBoundingBox(full);
            if (this.width > f && !this.firstUpdate && !this.world.isRemote)
            {
                this.move(MoverType.SELF, f - this.width, 0.0D, f - this.width);
            }
        }
    }
	
	@Override
	public void setPosition(double x, double y, double z)
    {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.updateAABB();
    }
	
	@Override
	public boolean shouldRiderSit()
    {
        return false;
    }

	@Override
	protected void initEntityAI() {
	}
	
	@Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(53.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.22499999403953552D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
    }
	
	public void setCharging(boolean flag)
	{
		this.dataManager.set(CHARGING, flag);
		this.dataManager.set(START, false);
	}
	
	public boolean isCharging()
	{
		return this.dataManager.get(CHARGING);
	}
	
	public void startCharging()
	{
		this.dataManager.set(START, true);
	}
	
	public boolean isPreparingCharge()
	{
		return this.dataManager.get(START);
	}

	@Override
	public void fall(float distance, float damageMultiplier) {}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if(this.getNavigator().noPath() && this.isCharging())
		{
			this.setCharging(false);
		}
		if(!this.world.isRemote && this.isCharging() && this.getPassengers().get(0) instanceof EntityIskander)
		{
			List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(0.5));
			for(EntityLivingBase e : list)
			{
				if(this.getPassengers().get(0)!=e && e != this)
				{
					e.attackEntityFrom(CustomDamageSource.gordiusTrample(this, (EntityLivingBase) this.getPassengers().get(0)), 5);
				}
			}
	        this.playSound(SoundEvents.ENTITY_COW_STEP, 0.4F, 0.4F);
		}
	}
	
	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if(this.isBeingRidden())
		{
			EntityLivingBase rider = (EntityLivingBase) this.getPassengers().get(0);
			this.rotationYaw = rider.rotationYaw;
			if(this.rotationYaw!=this.prevRotationYaw)
				this.updateAABB();
            this.prevRotationYaw = this.rotationYaw;
            this.rotationPitch = rider.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.renderYawOffset;
		}
	}

	@Override
	public void updatePassenger(Entity passenger)
    {
        if (this.isPassenger(passenger))
        {
        		Vec3d offSet = this.getVectorForRotation(0, this.rotationYaw).scale(1.2);
            passenger.setPosition(this.posX-offSet.x, this.posY + this.getMountedYOffset() + passenger.getYOffset(), this.posZ-offSet.z);
        }
    }
	
	@Override
	public double getMountedYOffset()
    {
        return (double)this.height * 0.6D;
    }
	
	@Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_COW_AMBIENT;
    }

	@Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
		return SoundEvents.ENTITY_COW_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_COW_DEATH;
	}

    @Override
	protected float getSoundPitch()
    {
        return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
    }
    
    private void updateAABB()
    {
    		EnumFacing facing = this.getHorizontalFacing();
        AxisAlignedBB aabb = this.rotateAxis(full, facing);
        double xOff = aabb.maxX/2.0;
        double yOff = aabb.maxY;
        double zOff = aabb.maxZ/2.0;     
        this.setEntityBoundingBox(new AxisAlignedBB(this.posX - xOff, this.posY, this.posZ - zOff, this.posX + xOff, this.posY + yOff, this.posZ + zOff));
    }
    
    private AxisAlignedBB rotateAxis(AxisAlignedBB aabb, EnumFacing facing)
    {
    		if(facing == EnumFacing.WEST || facing == EnumFacing.EAST)
    		{
    			return new AxisAlignedBB(aabb.minZ, aabb.minY, aabb.minX, aabb.maxZ, aabb.maxY, aabb.maxX);
    		}
    		return aabb;
    }
}
