package com.flemmli97.fatemod.common.entity;

import java.util.Collection;

import com.flemmli97.fatemod.common.handler.CustomDamageSource;
import com.flemmli97.tenshilib.common.item.ItemUtil;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityBabylonWeapon extends EntitySpecialProjectile{
	
    protected static final DataParameter<ItemStack> weaponType = EntityDataManager.<ItemStack>createKey(EntityBabylonWeapon.class, DataSerializers.ITEM_STACK);
    protected static final DataParameter<Integer> shootTime = EntityDataManager.<Integer>createKey(EntityBabylonWeapon.class, DataSerializers.VARINT);

    public boolean iddle;
	EntityLivingBase target;
	private double dmg;
	public EntityBabylonWeapon(World world)
	{
		super(world);
		this.livingTickMax=400;
		this.dataManager.set(shootTime, this.rand.nextInt(10)+25);
	}
	
	public EntityBabylonWeapon(World world, EntityLivingBase shootingEntity)
	{
		super(world, shootingEntity, false, false);
		this.livingTickMax=400;
		this.dataManager.set(shootTime, this.rand.nextInt(20)+25);
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
        this.dataManager.register(shootTime, 0);
    }

	@Override
	public void onUpdate() {
		EntityLivingBase thrower = getThrower();
		if(livingTick<=this.dataManager.get(shootTime))
		{
			livingTick++;
			iddle=true;;
		}
		if(livingTick == this.dataManager.get(shootTime))
		{
			if(!world.isRemote)
			{
				if(thrower instanceof EntityPlayer)
				{
					RayTraceResult hit = this.entityRayTrace(64);
					this.setHeadingToPosition(hit.hitVec.x, hit.hitVec.y, hit.hitVec.z, 0.5F, 0.5F);
				}
				else if(target!=null)
				{
					this.setHeadingToPosition(target.posX, target.posY+target.height/2, target.posZ, 0.5F , 1);
				}
			}
		}
		else if(livingTick>this.dataManager.get(shootTime))
		{
			iddle=false;
			if(!world.isRemote) 
			{
				if(thrower == null || thrower.isDead) 
				{
					setDead();
					return;
				}
			}
			this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX, this.posY, this.posZ , 0, 0,0, Block.getStateId(Blocks.GOLD_BLOCK.getDefaultState()));
			super.onUpdate();
		}
	}
	
	@Override
	protected float getGravityVelocity() {
		return 0;
	}
	
	public void setEntityProperties()
	{	
		this.setWeapon(ItemUtil.getRandomFromSlot(EntityEquipmentSlot.MAINHAND));
		super.setProjectileAreaPosition(7);
	}
	
	public ItemStack getWeapon()
	{
		return this.dataManager.get(weaponType);
	}
	
	public void setWeapon(ItemStack stack)
	{
		if(!stack.isEmpty())
		{
			this.dataManager.set(weaponType, stack);
			Collection<AttributeModifier> atts = this.getWeapon().getAttributeModifiers(EntityEquipmentSlot.MAINHAND).get(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
			for(AttributeModifier mod : atts)
			{
				if(mod.getOperation()==0)
					dmg+=mod.getAmount();
			}
			double value = dmg;
			for(AttributeModifier mod : atts)
			{
				if(mod.getOperation()==1)
					value+=dmg*mod.getAmount();
			}
			for(AttributeModifier mod : atts)
			{
				if(mod.getOperation()==2)
					value*=1+mod.getAmount();
			}
			dmg=SharedMonsterAttributes.ATTACK_DAMAGE.clampValue(value);
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote)
        {
    		if (result.entityHit != null && result.entityHit != this.getThrower())
            {
    			result.entityHit.attackEntityFrom(CustomDamageSource.babylon(this, this.getThrower()), (float)this.dmg*1.5F);
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
		this.setWeapon(new ItemStack(tag));
		super.readFromNBT(compound);

	}
}
