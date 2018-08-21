package com.flemmli97.fatemod.common.entity.servant.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAIRetaliate extends EntityAITarget {
	
	private int revengeTimer;
	public EntityAIRetaliate(EntityServant servant)
	{
		super(servant, false);
		this.setMutexBits(1);
	}

	public boolean shouldExecute() {
		if(this.taskOwner.getAttackTarget()!=null)
			return false;
		int revengeTimer = this.taskOwner.getRevengeTimer();
        return revengeTimer != this.revengeTimer && this.checkTarget(this.taskOwner.getAttackTarget());
	}
	
	public void startExecuting()
	{
        this.taskOwner.setAttackTarget(this.taskOwner.getAttackTarget());
        this.revengeTimer = this.taskOwner.getRevengeTimer();
        this.unseenMemoryTicks = 300;

        super.startExecuting();
        
	}
	
	protected boolean checkTarget(EntityLivingBase livingBase)
	{
		EntityServant servant = (EntityServant) taskOwner;
		if(servant.getOwner()!=null)
		{
			IPlayer capSync = servant.getOwner().getCapability(PlayerCapProvider.PlayerCap, null);
	
			if(servant.getOwner() == livingBase)
			{
				return false;
			}
			else if(livingBase instanceof EntityPlayer && capSync.isPlayerTruce((EntityPlayer) livingBase))
			{
				return false;
			}
			else if(livingBase instanceof EntityServant && ((EntityServant)livingBase).getOwner()!=null && capSync.isPlayerTruce(((EntityServant)livingBase).getOwner()) )
			{
				return false;
			}
			return super.isSuitableTarget(livingBase, true);

		}
		return super.isSuitableTarget(livingBase, true);
	}

}
