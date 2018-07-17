package com.flemmli97.fatemod.common.entity.servant.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAIPatrol extends EntityAIBase{

	EntityServant theEntity;
	BlockPos centerPos;
	
	public EntityAIPatrol(EntityServant servant, float x, float y, float z)
	{
		this.theEntity = servant;
		centerPos = new BlockPos (x, y, z);
	}
	@Override
	public boolean shouldExecute() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean shouldContinueExecuting() {
		// TODO Auto-generated method stub
		return super.shouldContinueExecuting();
	}
	@Override
	public void startExecuting() {
		// TODO Auto-generated method stub
		super.startExecuting();
	}
	@Override
	public void resetTask() {
		// TODO Auto-generated method stub
		super.resetTask();
	}
	@Override
	public void updateTask() {
		// TODO Auto-generated method stub
		super.updateTask();
	}
	
	

}
