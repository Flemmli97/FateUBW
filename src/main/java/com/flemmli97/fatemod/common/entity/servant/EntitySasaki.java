package com.flemmli97.fatemod.common.entity.servant;

import com.flemmli97.fatemod.common.entity.servant.ai.EntityAISasaki;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntitySasaki extends EntityServant {

	public EntityAISasaki attackAI = new EntityAISasaki(this);
	
	private static final AnimatedAction npAttack = new AnimatedAction(40,0,"np");
	private static final AnimatedAction[] anims = new AnimatedAction[] {AnimatedAction.vanillaAttack, npAttack};

	public EntitySasaki(World world) {
		super(world, EnumServantType.ASSASSIN, "Tsubame Gaeshi", new ItemStack[] {new ItemStack(ModItems.katana)});
        this.tasks.addTask(1, attackAI);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.katana));       
	}
	
	@Override
	public boolean canUse(AnimatedAction anim, AttackType type)
	{
		if(type==AttackType.NP)
			return anim.getID().equals("np");
		return anim.getID().equals("vanilla");
	}
	
	@Override
	public AnimatedAction[] getAnimations() {
		return anims;
	}
	
	@Override
	public void updateAI(int behaviour) {
		super.updateAI(behaviour);
		if(commandBehaviour == 3)
		{
			this.tasks.addTask(1, attackAI);
		}
		else if(commandBehaviour == 4)
		{
			this.tasks.removeTask(attackAI);
		}
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float damage)
    {
		if(this.getAnimation()!=null && this.canUse(this.getAnimation(), AttackType.NP))
			return false;
		return super.attackEntityFrom(damageSource, damage);
    }
	
	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount)
    {
		super.damageEntity(damageSrc, damageAmount);
		if (!this.canUseNP && !this.dead && this.getHealth() < 0.5 * this.getMaxHealth())
		{
			this.canUseNP=true;
		}
    }
	
	public void attackWithNP(EntityLivingBase living)
	{
		this.revealServant();
	}

	public boolean canAttackNP() {
		if(this.getAnimation()==null || !this.canUse(this.getAnimation(), AttackType.NP))
			return false;
		int i = this.getAnimation().getTick();
		return i==30||i==20||i==10;
	}

}
