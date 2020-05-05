package com.flemmli97.fatemod.common.entity.servant;

import com.flemmli97.fatemod.common.entity.servant.ai.EntityAIDiarmuid;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityDiarmuid extends EntityServant {

	public EntityAIDiarmuid attackAI = new EntityAIDiarmuid(this);
	
	private static final AnimatedAction npAttack = new AnimatedAction(20,0,"np");
	private static final AnimatedAction[] anims = new AnimatedAction[] {AnimatedAction.vanillaAttack, npAttack};

	public EntityDiarmuid(World world) {
		super(world, EnumServantType.LANCER, "", new ItemStack[] {new ItemStack(ModItems.gaebuidhe), new ItemStack(ModItems.gaedearg)});
        this.tasks.addTask(1, this.attackAI);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.gaedearg));
		this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(ModItems.gaebuidhe)); 
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
	public void fall(float damage, float damagemulti) {
	}
	
	@Override
	public void updateAI(int behaviour) {
		super.updateAI(behaviour);
		if(this.commandBehaviour == 3)
		{
			this.tasks.addTask(1, this.attackAI);
		}
		else if(this.commandBehaviour == 4)
		{
			this.tasks.removeTask(this.attackAI);
		}
	}
	
	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount)
    {
		super.damageEntity(damageSrc, damageAmount);
		if (!this.canUseNP && !this.isDead() && this.getHealth() < 0.5 * this.getMaxHealth())
		{
			this.canUseNP=true;
		}
    }
	
	@Override
	public void onLivingUpdate() {
		if (this.getHealth() < 0.25 * this.getMaxHealth() && this.getHealth()>0)
		{
			if(this.critHealth == false)	
			{
				this.critHealth = true;
			}
		}
		this.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:speed"), 1, 1, false, false));
		super.onLivingUpdate();
	}
	
	@Override
	public float damageModifier() {
		if(this.world.rand.nextFloat()<0.6)
			for(PotionEffect effect : this.getAttackTarget().getActivePotionEffects())
			{
				if(effect.getPotion().isBadEffect())
					return 2;
			}
		return super.damageModifier();
	}

	public void attackWithNP(EntityLivingBase target) {
		target.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:weakness"), 7200, 2));
		target.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:poison"), 800, 1));
		this.revealServant();
	}
}
