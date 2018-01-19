package com.flemmli97.fatemod.common.entity;

import com.flemmli97.fatemod.common.handler.BabylonWeapon;
import com.flemmli97.fatemod.common.handler.CustomDamageSource;
import com.flemmli97.fatemod.network.CustomDataPacket;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityBabylonWeapon extends EntitySpecialProjectile{
	
    protected static final DataParameter<ItemStack> weaponType = EntityDataManager.<ItemStack>createKey(EntityBabylonWeapon.class, CustomDataPacket.ITEM_STACK);
	public boolean iddle;
	EntityLivingBase target;
	
	public EntityBabylonWeapon(World world)
	{
		super(world);
		this.livingTickMax=400;
	}
	
	public EntityBabylonWeapon(World world, EntityLivingBase shootingEntity)
	{
		super(world, shootingEntity, false, false);
		this.livingTickMax=400;
	}
	
	public EntityBabylonWeapon(World world, EntityLivingBase shootingEntity, EntityLivingBase target)
	{
		this(world, shootingEntity);
		this.target = target;
	}
	
	@Override
	protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(weaponType, null);
    }

	@Override
	public void onUpdate() {
		EntityLivingBase thrower = getThrower();
		if(livingTick<=50)
		{
			livingTick++;
			iddle=true;;
		}
		if(livingTick == 50)
		{
			if(!worldObj.isRemote)
			{
				if(thrower instanceof EntityPlayer)
				{
					RayTraceResult hit = this.entityRayTrace(64);
					this.setHeadingToPosition(hit.hitVec.xCoord, hit.hitVec.yCoord, hit.hitVec.zCoord, 0.5F, 0.5F);

				}
				else if(target!=null)
				{
					this.setHeadingToPosition(target.posX, target.posY+target.height/2, target.posZ, 0.5F , 1);
				}
			}
		}
		else if(livingTick>50)
		{
			iddle=false;
			if(!worldObj.isRemote) 
			{
				if(thrower == null || thrower.isDead) 
				{
					setDead();
					return;
				}
			}
			this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX, this.posY, this.posZ , 0, 0,0, Block.getStateId(Blocks.GOLD_BLOCK.getDefaultState()));
			super.onUpdate();
		}
	}
	
	@Override
	protected float getGravityVelocity() {
		return 0;
	}
	
	public void setEntityProperties()
	{	
		this.setWeapon(BabylonWeapon.getWeapon());
		super.setProjectileAreaPosition(7);
	}
	
	public ItemStack getWeapon()
	{
		return this.dataManager.get(weaponType);
	}
	
	public void setWeapon(ItemStack stack)
	{
		if(stack!=null)
			this.dataManager.set(weaponType, stack);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.worldObj.isRemote)
        {
    		if (result.entityHit != null && result.entityHit != this.getThrower())
            {
    			result.entityHit.attackEntityFrom(CustomDamageSource.babylon(this, this.getThrower()), 10.0F);
    			this.setDead();
            }
    		else if(result.typeOfHit == RayTraceResult.Type.BLOCK)
    		{
    			this.setDead();
    		}
        }
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagCompound tag = new NBTTagCompound();
		this.getWeapon().writeToNBT(tag);
		compound.setTag("Weapon", tag);
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		NBTTagCompound tag = (NBTTagCompound) compound.getTag("Weapon");
		this.setWeapon(ItemStack.loadItemStackFromNBT(tag));
		super.readFromNBT(compound);

	}
}
