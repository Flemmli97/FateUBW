package com.flemmli97.fatemod.common.entity.servant.ai;

import com.flemmli97.fatemod.common.entity.servant.EntityEmiya;
import com.flemmli97.fatemod.common.entity.servant.EntityServant.AttackType;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class EntityAIEmiya extends EntityAIAnimatedAttack<EntityEmiya>{
	
	private ItemStack main = ItemStack.EMPTY;
	private ItemStack off = ItemStack.EMPTY;

	public EntityAIEmiya(EntityEmiya selectedEntity, boolean isRanged, double speedToTarget, double rangeModifier) {
		super(selectedEntity, isRanged, speedToTarget, rangeModifier);
	}

	@Override
	public void updateTask() {	
		EntityLivingBase var1 = this.attackingEntity.getAttackTarget();
        this.attackingEntity.getLookHelper().setLookPositionWithEntity(var1, 30.0F, 30.0F);
        AnimatedAction anim = this.attackingEntity.getAnimation();
		if(anim==null && ((this.attackingEntity.canUseNP() && this.attackingEntity.getOwner() == null && this.attackingEntity.getMana()>= this.attackingEntity.props().hogouMana()) || this.attackingEntity.forcedNP))
		{
        	anim = this.attackingEntity.getRandomAttack(AttackType.NP);
        	this.attackingEntity.setAnimation(anim);		
        	if(this.attackingEntity.getHeldItem(EnumHand.MAIN_HAND).getItem()!=ModItems.archbow)
        	{
	        	this.main = this.attackingEntity.getHeldItem(EnumHand.MAIN_HAND).copy();
	        	this.off = this.attackingEntity.getHeldItem(EnumHand.OFF_HAND).copy();
	        	this.attackingEntity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.archbow));
	        	this.attackingEntity.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
        	}
		}
		if(anim!=null && this.attackingEntity.canUse(anim, AttackType.NP))
        {
    		this.attackingEntity.getMoveHelper().strafe(-2, 0);

        	if(anim.canAttack())
        	{
        		if(!this.attackingEntity.forcedNP)
        			this.attackingEntity.useMana(this.attackingEntity.props().hogouMana());
                this.attackingEntity.attackWithNP();
        		this.attackingEntity.forcedNP = false;
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
		else
		{
			super.updateTask();
		}		
	}
}
