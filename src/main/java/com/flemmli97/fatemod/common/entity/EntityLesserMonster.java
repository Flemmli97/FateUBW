package com.flemmli97.fatemod.common.entity;

import java.util.UUID;

import com.flemmli97.fatemod.common.entity.ai.EntityAIStarfishAttack;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.handler.ConfigHandler;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.flemmli97.tenshilib.common.entity.EntityUtil;
import com.flemmli97.tenshilib.common.entity.IAnimated;
import com.flemmli97.tenshilib.common.world.RayTraceUtils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityLesserMonster extends EntityCreature implements IServantMinion, IAnimated{

	private UUID ownerUUID;
	private EntityLivingBase owner;
	private int livingTicks;
	private AnimatedAction currentAnim;
	public static final AnimatedAction walk = new AnimatedAction(31, 0, "walk");
	public static final AnimatedAction attack = new AnimatedAction(20, 15, "attack");
	private static final AnimatedAction[] anims = new AnimatedAction[] {walk, attack};
	public EntityAINearestAttackableTarget<EntityLivingBase> target = new EntityAINearestAttackableTarget<EntityLivingBase>(this, EntityLivingBase.class, 10, true, true, 
			(living)->EntityLesserMonster.this.canAttackTarget(living));
	
	public EntityLesserMonster(World worldIn) {
		super(worldIn);
		this.tasks.addTask(2, new EntityAIStarfishAttack(this, 1));
	    this.tasks.addTask(3, new EntityAISwimming(this));
	    this.tasks.addTask(4, new EntityAILookIdle(this));
	    this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
	    this.targetTasks.addTask(0, this.target);
	}
	
	public EntityLesserMonster(World worldIn, EntityLivingBase owner) {
		this(worldIn);
		BlockPos pos = RayTraceUtils.randomPosAround(worldIn, this, owner.getPosition(), 9, true, this.getRNG());
		this.setLocationAndAngles(pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
		this.owner=owner;
		this.ownerUUID=owner.getUniqueID();
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(ConfigHandler.smallMonsterDamage);
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(18);;
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if(!this.world.isRemote )
		{
			this.livingTicks++;
			if(this.livingTicks>ConfigHandler.gillesMinionDuration)
				this.setDead();
			if(this.currentAnim==null && (this.motionX!=0||this.motionZ!=0))
				this.setAnimation(walk);
			if(this.getOwner()!=null && this.getOwner().getRevengeTarget()!=null && this.getAttackTarget()==null)
				this.setAttackTarget(this.getOwner().getRevengeTarget());
		}
		this.tickAnimation();
	}
	
	@Override
	public void tickAnimation()
	{
		if(this.getAnimation()!=null && this.getAnimation().tick())
		{
			if(this.getAnimation().getID().equals("walk") && this.motionX!=0||this.motionZ!=0)
				this.getAnimation().reset();
			else
				this.setAnimation(null);
		}
	}
	
	public EntityLivingBase getOwner()
	{
		if(this.owner==null && this.ownerUUID!=null)
		{
			this.owner=EntityUtil.findFromUUID(EntityLivingBase.class, this.world, this.ownerUUID);
		}
		return this.owner;
	}
	
	@Override
	public boolean attackEntityAsMob(Entity entityIn)
    {
        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag)
        {
            this.applyEnchantments(this, entityIn);
        }

        return flag;
    }
	
	protected boolean canAttackTarget(EntityLivingBase e)
	{
		if(this.getClass().equals(e.getClass()) || e.getUniqueID().equals(this.ownerUUID))
			return false;	
		if((this.getOwner() instanceof EntityServant && ((EntityServant)this.getOwner()).getOwner()!=null && ((EntityServant)this.getOwner()).getOwner().getUniqueID().equals(e.getUniqueID())))
			return false;
		return true;
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		if(tag.hasKey("Owner"))
			this.ownerUUID=UUID.fromString(tag.getString("Owner"));
	}
	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		if(this.ownerUUID!=null)
			tag.setString("Owner", this.ownerUUID.toString());
	}

	@Override
	public AnimatedAction getAnimation() {
		return this.currentAnim;
	}

	@Override
	public void setAnimation(AnimatedAction anim) {
		this.currentAnim=anim==null?null:anim.create();
		IAnimated.sentToClient(this);
	}

	@Override
	public AnimatedAction[] getAnimations() {
		return anims;
	}
}
