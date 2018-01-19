package com.flemmli97.fatemod.common.entity.servant;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityGilles extends EntityServant {

	public EntityGilles(World world) {
		super(world, EnumServantType.CASTER, "", 0, new Item[] {});
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.ENCHANTED_BOOK));       
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(600.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
	}

	
	@Override
	protected void dropFewItems(boolean par1, int par2) {
		
	}


}
