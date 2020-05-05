package com.flemmli97.fatemod.common.entity.servant.ai;

import com.flemmli97.fatemod.common.entity.IServantMinion;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.utils.ServantUtils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAIRetaliate extends EntityAITarget {
	
	public EntityAIRetaliate(EntityServant servant)
	{
		super(servant, false);
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if(this.taskOwner.getRevengeTarget() instanceof IServantMinion || (this.taskOwner.getAttackTarget()==null && this.taskOwner.getRevengeTarget()!=null))
			return this.checkTarget(this.taskOwner.getRevengeTarget());
		return false;
	}
	
	private boolean isNotSameTarget()
	{
		if(this.taskOwner.getAttackTarget()==null)
			return true;
		return !this.taskOwner.getRevengeTarget().equals(this.taskOwner.getAttackTarget());
	}
	@Override
	public void startExecuting()
	{
		if(this.isNotSameTarget())
			this.taskOwner.setAttackTarget(this.taskOwner.getRevengeTarget());
        super.startExecuting();
	}
	
	protected boolean checkTarget(EntityLivingBase livingBase)
	{
		EntityServant servant = (EntityServant) this.taskOwner;
		if(servant.getOwner()!=null)
		{	
			if(servant.getOwner() == livingBase)
			{
				return false;
			}
			else if(livingBase instanceof EntityPlayer && ServantUtils.inSameTeam(servant.getOwner(), (EntityPlayer) livingBase))
			{
				return false;
			}
			else if(livingBase instanceof EntityServant && ((EntityServant)livingBase).getOwner()!=null && ServantUtils.inSameTeam(servant.getOwner(), ((EntityServant)livingBase).getOwner()) )
			{
				return false;
			}
			return super.isSuitableTarget(livingBase, true);

		}
		return super.isSuitableTarget(livingBase, false);
	}

}
