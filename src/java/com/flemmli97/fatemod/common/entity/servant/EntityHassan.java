package com.flemmli97.fatemod.common.entity.servant;

import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class EntityHassan extends EntityServant {

	private boolean isOriginal;
	
	public EntityHassan(World world) {
		super(world, EnumServantType.ASSASSIN, "", 0, new Item[] {});
       // this.setCurrentItemOrArmor(0, new ItemStack(ModItems.hassanKnife));
	}
	
	public EntityHassan(World world, boolean isOriginal)
	{
		this(world);
		this.isOriginal = isOriginal;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		if(isOriginal)
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(300.0D);
		else
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.7D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
	}
	
	@Override
	protected void dropFewItems(boolean par1, int par2) {
	}

}
