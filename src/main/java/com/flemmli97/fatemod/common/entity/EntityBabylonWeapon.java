package com.flemmli97.fatemod.common.entity;

import java.util.List;
import java.util.Random;

import com.flemmli97.fatemod.common.handler.CustomDamageSource;
import com.flemmli97.tenshilib.common.entity.EntityProjectile;
import com.flemmli97.tenshilib.common.item.ItemUtil;
import com.flemmli97.tenshilib.common.world.RayTraceUtils;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityBabylonWeapon extends EntityProjectile{
	
    protected static final DataParameter<ItemStack> weaponType = EntityDataManager.createKey(EntityBabylonWeapon.class, DataSerializers.ITEM_STACK);
    protected static final DataParameter<Integer> shootTime = EntityDataManager.createKey(EntityBabylonWeapon.class, DataSerializers.VARINT);

    public boolean iddle=true;
	EntityLivingBase target;
	private double dmg;
	public EntityBabylonWeapon(World world)
	{
		super(world);
	}
	
	public EntityBabylonWeapon(World world, EntityLivingBase shootingEntity)
	{
		super(world, shootingEntity);
	}
	
	public EntityBabylonWeapon(World world, EntityLivingBase shootingEntity, EntityLivingBase target)
	{
		this(world, shootingEntity);
		this.target = target;
	}
	
	@Override
	public int livingTickMax()
	{
		return 200;
	}
	
	@Override
	protected void entityInit()
    {
		super.entityInit();
        this.dataManager.register(weaponType, ItemStack.EMPTY);
        this.dataManager.register(shootTime, this.rand.nextInt(20)+25);
    }

	@Override
	public void onUpdate() {
		EntityLivingBase thrower = this.getShooter();
		if(this.livingTicks<=this.dataManager.get(shootTime))
			this.livingTicks++;
		if(this.livingTicks() == this.dataManager.get(shootTime))
		{
			if(!this.world.isRemote)
			{
				if(thrower instanceof EntityPlayer)
				{
					RayTraceResult hit = RayTraceUtils.entityRayTrace(thrower, 64, true, true, false);
					this.shootAtPosition(hit.hitVec.x, hit.hitVec.y, hit.hitVec.z, 0.5F, 0.5F);
				}
				else if(this.target!=null)
				{
					this.shootAtPosition(this.target.posX, this.target.posY+target.height/2, this.target.posZ, 0.5F , 1);
				}
			}
		}
		else if(this.livingTicks()>this.dataManager.get(shootTime))
		{
			this.iddle=false;
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
	
	@Override
	protected float motionReduction()
	{
		return 1;
	}
	
	public void setEntityProperties()
	{	
		this.setWeapon(ItemUtil.getRandomFromSlot(EntityEquipmentSlot.MAINHAND));
		this.setProjectileAreaPosition(7);
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
			this.dmg=ItemUtil.damage(stack);
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote)
        {
    		if (result.entityHit != null)
            {
    			result.entityHit.attackEntityFrom(CustomDamageSource.babylon(this, this.getShooter()), (float)this.dmg*1.5F);
            }
			this.setDead();
        }
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("Weapon", this.getWeapon().writeToNBT(new NBTTagCompound()));
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.setWeapon(new ItemStack(compound.getCompoundTag("Weapon")));
	}
	
	/**for symmetry range should be odd number, minimum is 3*/
    public void setProjectileAreaPosition(int range)
    {		
		Random rand = new Random();
		EntityLivingBase thrower = this.getShooter();
		Vec3d pos = thrower.getPositionVector();
		Vec3d look = thrower.getLookVec();
		Vec3d vert = new Vec3d(0, 1, 0);
		if(-20<thrower.rotationPitch && thrower.rotationPitch>20)
			vert.rotatePitch(thrower.rotationPitch);
		if(-20>thrower.rotationPitch)
			vert.rotatePitch(-20);
		if(20<thrower.rotationPitch)
			vert.rotatePitch(20);
		Vec3d hor = look.crossProduct(vert);
		vert.normalize();
		hor.normalize();
		int scaleHor= rand.nextInt(range)-(range-1)/2;
		int scaleVert = rand.nextInt((range+1)/2);
		double distance = (scaleHor*scaleHor + scaleVert*scaleVert);
		float rangeSq = (range-1)/2*(range-1)/2;
		boolean playerSpace = (scaleVert==0 && scaleHor==0);
		if(distance<=rangeSq && !playerSpace)
		{
			Vec3d area = pos.add(hor.scale(scaleHor*2)).add(vert.scale(scaleVert*2+1));
			BlockPos spawnPos = new BlockPos(area);
			AxisAlignedBB axis = new AxisAlignedBB(spawnPos, spawnPos).grow(1.0);
			List<Entity> list = this.world.getEntitiesWithinAABB(EntityBabylonWeapon.class, axis);
			if(list.isEmpty())
			{
				this.shoot(thrower, thrower.rotationPitch, thrower.rotationYaw, 0, 0.5F, 0);	
				this.setPosition(area.x, area.y, area.z);
				this.world.spawnEntity(this);
			}
		}
		else
		{
			this.setProjectileAreaPosition(range);
		}
    }
}
