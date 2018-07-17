package com.flemmli97.fatemod.common.entity.servant.ai;

import com.flemmli97.fatemod.common.entity.EntityGordiusWheel;
import com.flemmli97.fatemod.common.entity.servant.EntityIskander;
import com.flemmli97.fatemod.common.entity.servant.EntityServant.State;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;

public class EntityAIIskander extends EntityAIAnimatedAttack{
	
	private int prepare = 0;

	public EntityAIIskander(EntityIskander selectedEntity)
	{
		super(selectedEntity,false, 1, 1);
	}

	@Override
	public void updateTask() {	
		EntityLivingBase var1 = this.attackingEntity.getAttackTarget();
		if((attackingEntity.canUseNP() && attackingEntity.getOwner() == null && attackingEntity.getMana()>=attackingEntity.props().hogouMana()) || attackingEntity.forcedNP)
		{
	        State state = this.attackingEntity.entityState();
	        if(state==State.IDDLE)
	        {
	        	this.attackingEntity.setState(State.NP);
	        }
        	else if(state==State.NP)
            {
            	if(this.attackingEntity.canAttack())
            	{
            		if(!attackingEntity.forcedNP)
            			attackingEntity.useMana(attackingEntity.props().hogouMana());
            		((EntityIskander)attackingEntity).attackWithNP();
            		attackingEntity.forcedNP = false;
            	}       		
            }
		}
		else if(this.attackingEntity.getRidingEntity() instanceof EntityGordiusWheel)
		{
			EntityGordiusWheel mount = (EntityGordiusWheel) this.attackingEntity.getRidingEntity();
	        this.attackingEntity.getLookHelper().setLookPositionWithEntity(var1, 30.0F, 30.0F);
			if(!mount.isCharging()  && !mount.isPreparingCharge() && this.attackingEntity.getRNG().nextFloat()<0.2)
			{
				mount.startCharging();
				this.attackingEntity.world.playSound(null, this.attackingEntity.getPosition(), SoundEvents.ENTITY_COW_AMBIENT, SoundCategory.NEUTRAL, 1, (this.attackingEntity.getRNG().nextFloat() - this.attackingEntity.getRNG().nextFloat()) * 0.4F);
			}
			if(mount.isPreparingCharge())
			{
				this.prepare++;
				if(this.prepare>=20)
				{
					Vec3d moveVec = mount.getPositionVector().add(var1.getPositionVector().subtract(mount.getPositionVector()).normalize().scale(16));
			        this.attackingEntity.getLookHelper().setLookPosition(moveVec.x, moveVec.y, moveVec.z, 30.0F, 30.0F);
					this.attackingEntity.getNavigator().tryMoveToXYZ(moveVec.x, moveVec.y, moveVec.z, this.speedTowardsTarget);
					mount.setCharging(true);
					this.prepare=0;
				}
			}
		}
		else if(!(this.attackingEntity.getRidingEntity() instanceof EntityGordiusWheel))
			super.updateTask();
	}	
}
