package com.flemmli97.fatemod.common.entity;

import com.flemmli97.fatemod.common.handler.ConfigHandler;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.flemmli97.tenshilib.common.entity.IAnimated;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityPegasus extends EntityAnimal implements IServantMinion, IAnimated{

    private AnimatedAction currentAnim;

    private static final AnimatedAction prepCharge = new AnimatedAction(20, 19, "prep_charge");
    
    private static final AnimatedAction[] anims = new AnimatedAction[] {};
	public EntityPegasus(World world) {
		super(world);
		this.stepHeight=1.0F;
		this.setSize(1.5F, 1.5F);
        this.moveHelper = new FlyingMoveHelper(this);
	}

	@Override
	protected void initEntityAI() {
	}
	
	@Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(ConfigHandler.pegasusHealth);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.22499999403953552D);
    }

	@Override
	public void fall(float distance, float damageMultiplier) {
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return super.attackEntityFrom(source, amount);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if(this.getRidingEntity()!=null && this.getRidingEntity() instanceof EntityLiving)
		{
			EntityLiving rider = (EntityLiving) this.getRidingEntity();
			Entity target = rider.getAttackTarget();
			if(target!=null)
			{
				this.getNavigator().getPathToEntityLiving(target);
			}
		}
	}
	
	@Override
	public double getMountedYOffset() {
		return super.getMountedYOffset();
	}
	
	@Override
	public void updatePassenger(Entity passenger)
    {
        super.updatePassenger(passenger);

        if (passenger instanceof EntityLiving)
        {
            EntityLiving entityliving = (EntityLiving)passenger;
            this.renderYawOffset = entityliving.renderYawOffset;
        }
    }
	
	private static class FlyingMoveHelper extends EntityMoveHelper
	{
		EntityPegasus pegasus;
		private int courseChangeCooldown;
		
		public FlyingMoveHelper(EntityLiving entityliving) {
			super(entityliving);
			this.pegasus = (EntityPegasus) entityliving;
		}

		@Override
		public void onUpdateMoveHelper() {
			 if (this.action == EntityMoveHelper.Action.MOVE_TO)
             {
                 double d0 = this.posX - this.pegasus.posX;
                 double d1 = this.posY - this.pegasus.posY;
                 double d2 = this.posZ - this.pegasus.posZ;
                 double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                 if (this.courseChangeCooldown-- <= 0)
                 {
                     this.courseChangeCooldown += this.pegasus.getRNG().nextInt(5) + 2;
                     d3 = (double)MathHelper.sqrt(d3);

                     if (this.isNotColliding(this.posX, this.posY, this.posZ, d3))
                     {
                         this.pegasus.motionX += d0 / d3 * 0.1D;
                         this.pegasus.motionY += d1 / d3 * 0.1D;
                         this.pegasus.motionZ += d2 / d3 * 0.1D;
                     }
                     else
                     {
                         this.action = EntityMoveHelper.Action.WAIT;
                     }
                 }
             }		 
		}
		
		private boolean isNotColliding(double x, double y, double z, double p_179926_7_)
        {
            double d0 = (x - this.pegasus.posX) / p_179926_7_;
            double d1 = (y - this.pegasus.posY) / p_179926_7_;
            double d2 = (z - this.pegasus.posZ) / p_179926_7_;
            AxisAlignedBB axisalignedbb = this.pegasus.getEntityBoundingBox();

            for (int i = 1; (double)i < p_179926_7_; ++i)
            {
                axisalignedbb = axisalignedbb.offset(d0, d1, d2);

                if (!this.pegasus.world.getCollisionBoxes(this.pegasus, axisalignedbb).isEmpty())
                {
                    return false;
                }
            }

            return true;
        }		
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return null;
	}
	
	public void prepCharge() {
	    if(this.getAnimation()==null)
	        this.setAnimation(prepCharge);
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
