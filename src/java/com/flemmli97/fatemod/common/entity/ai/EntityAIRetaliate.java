package com.flemmli97.fatemod.common.entity.ai;

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
		int revengeTimer = this.taskOwner.getRevengeTimer();
        return revengeTimer != this.revengeTimer && this.checkTarget(this.taskOwner.getAITarget());
	}
	
	public void startExecuting()
	{
        this.taskOwner.setAttackTarget(this.taskOwner.getAITarget());
        this.revengeTimer = this.taskOwner.getRevengeTimer();
        this.unseenMemoryTicks = 300;

        super.startExecuting();
        
	}
	
	protected boolean checkTarget(EntityLivingBase livingBase)
	{
		EntityServant servant = (EntityServant) taskOwner;
		IPlayer capSync = servant.getOwner().getCapability(PlayerCapProvider.PlayerCap, null);

		if(servant.getOwner() == livingBase)
		{
			return false;
		}
		else if(capSync.isPlayerTruce((EntityPlayer) livingBase))
		{
			return false;
		}
		else if(capSync.getServantTruce().contains(livingBase))
		{
			return false;
		}
		else
		{
			return super.isSuitableTarget(livingBase, true);
		}
	}

}
