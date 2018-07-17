package com.flemmli97.fatemod.common.entity.servant.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityEmiya;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.entity.servant.EntityServant.State;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class EntityAIEmiya extends EntityAIAnimatedAttack{
	
	private ItemStack main = ItemStack.EMPTY;
	private ItemStack off = ItemStack.EMPTY;

	public EntityAIEmiya(EntityServant selectedEntity, boolean isRanged, double speedToTarget, double rangeModifier) {
		super(selectedEntity, isRanged, speedToTarget, rangeModifier);
	}

	@Override
	public void updateTask() {	
		EntityLivingBase var1 = this.attackingEntity.getAttackTarget();
        this.attackingEntity.getLookHelper().setLookPositionWithEntity(var1, 30.0F, 30.0F);
        if((attackingEntity.canUseNP() && attackingEntity.getOwner() == null && attackingEntity.getMana()>=attackingEntity.props().hogouMana()) || attackingEntity.forcedNP)
		{
        	State state = this.attackingEntity.entityState();
 	        if(state==State.IDDLE)
 	        {
 	        	this.attackingEntity.setState(State.NP);
	        	//Temporary replaces held items with his bow
	        	if(this.attackingEntity.getHeldItem(EnumHand.MAIN_HAND).getItem()!=ModItems.archbow)
	        	{
		        	this.main = this.attackingEntity.getHeldItem(EnumHand.MAIN_HAND).copy();
		        	this.off = this.attackingEntity.getHeldItem(EnumHand.MAIN_HAND).copy();
		        	this.attackingEntity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.archbow));
		        	this.attackingEntity.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
	        	}
 	        }
 	        else if(state==State.NP)
 	        {
        		this.attackingEntity.getMoveHelper().strafe(-2, 0);
 	        	if(this.attackingEntity.canAttack())
 	        	{
            		if(!attackingEntity.forcedNP)
            			attackingEntity.useMana(attackingEntity.props().hogouMana());
            		((EntityEmiya)attackingEntity).attackWithNP();
            		attackingEntity.forcedNP = false;
            		//Switch items back
    	        	if(!this.main.isEmpty()||!this.off.isEmpty())
    	        	{
    		        	this.attackingEntity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, this.main.copy());
    		        	this.attackingEntity.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, this.off.copy());
    		        	this.main = ItemStack.EMPTY;
    		        	this.off = ItemStack.EMPTY;
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
