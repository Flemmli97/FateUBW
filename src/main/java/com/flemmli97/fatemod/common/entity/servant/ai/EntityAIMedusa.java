package com.flemmli97.fatemod.common.entity.servant.ai;

import com.flemmli97.fatemod.common.entity.EntityPegasus;
import com.flemmli97.fatemod.common.entity.servant.EntityMedusa;
import com.flemmli97.fatemod.common.entity.servant.EntityServant.AttackType;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class EntityAIMedusa extends EntityAIAnimatedAttack<EntityMedusa>{

    private int flyTicker, coolDown;
    private double[] targetPos, arrivePos;
    
	public EntityAIMedusa(EntityMedusa selectedEntity) {
		super(selectedEntity, false, 1, 1);
	}

	@Override
	public void updateTask() {	
		EntityLivingBase target = this.attackingEntity.getAttackTarget();
        this.attackingEntity.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
		AnimatedAction anim = this.attackingEntity.getAnimation();
		if(anim==null && ((this.attackingEntity.canUseNP() && this.attackingEntity.getOwner() == null && this.attackingEntity.getMana()>=this.attackingEntity.props().hogouMana()) || this.attackingEntity.forcedNP))
		{
        	anim = this.attackingEntity.getRandomAttack(AttackType.NP);
        	this.attackingEntity.setAnimation(anim);		
		}
		if(anim!=null && this.attackingEntity.canUse(anim, AttackType.NP))
        {
        	if(anim.canAttack())
        	{
        		if(!this.attackingEntity.forcedNP)
        			this.attackingEntity.useMana(this.attackingEntity.props().hogouMana());
        		this.attackingEntity.attackWithNP();
        		this.attackingEntity.forcedNP = false;
        	}       		
        }
		else if(this.attackingEntity.getRidingEntity() instanceof EntityPegasus)
        {
		    EntityPegasus mount = (EntityPegasus) this.attackingEntity.getRidingEntity();
		    if(mount.onGround) {
		        this.flyTicker=-1;
		    }else {
		        this.flyTicker++;
		    }
		 
		    boolean tryCharge = mount.getAnimation()!=null && mount.getAnimation().getID().equals("prep_charge");
		    if(tryCharge) {
		        if(mount.getAnimation().canAttack()) {
		            mount.getNavigator().tryMoveToXYZ(this.targetPos[0], this.targetPos[1], this.targetPos[2], 1.2);
		            this.targetPos=null;
		        }
		    }
		    else {
		        if(--this.coolDown>0) {
		            //Stay on ground
		        } else {
		            ;
		        }
		        if(mount.getNavigator().noPath() && this.flyTicker>0) {
		            if(this.arrivePos!=null) {
		                  mount.getNavigator().tryMoveToXYZ(this.arrivePos[0], this.arrivePos[1], this.arrivePos[2], 1.2);
		                  this.arrivePos=null;
		                  this.coolDown=this.attackingEntity.getRNG().nextInt(60)+60;
		            }
		            else if(this.flyTicker>60){
		                mount.prepCharge();
		                Vec3d moveVec = mount.getPositionVector().add(target.getPositionVector().subtract(mount.getPositionVector()).normalize().scale(16));
		                this.targetPos=new double[] {target.posX,target.posY,target.posZ};
		                this.arrivePos=new double[] {moveVec.x,mount.posY,moveVec.z};
		            }
		        }
		    }
        }
		else
		{
			super.updateTask();
		}		
	}	
}
