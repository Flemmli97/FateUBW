package com.flemmli97.fate.common.entity.servant;

import com.flemmli97.fate.common.entity.servant.ai.DiarmuidAttackGoal;
import com.flemmli97.fate.common.registry.ModItems;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityDiarmuid extends EntityServant {

	public final DiarmuidAttackGoal attackAI = new DiarmuidAttackGoal(this);

	private static final AnimatedAction npAttack = new AnimatedAction(20, 0, "np");
	private static final AnimatedAction[] anims = new AnimatedAction[]{AnimatedAction.vanillaAttack, npAttack};

	public EntityDiarmuid(EntityType<? extends EntityDiarmuid> entityType, World world) {
		super(entityType, world, "");
		this.goalSelector.addGoal(1, this.attackAI);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.gaedearg));
		this.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(ModItems.gaebuidhe));
	}

	@Override
	public boolean canUse(AnimatedAction anim, AttackType type) {
		if (type == AttackType.NP)
			return anim.getID().equals("np");
		return anim.getID().equals("vanilla");
	}

	@Override
	public AnimatedAction[] getAnimations() {
		return anims;
	}

	@Override
	public boolean handleFallDamage(float distance, float damageMultiplier) {
		return false;
	}

	@Override
	public void updateAI(int behaviour) {
		super.updateAI(behaviour);
		if (this.commandBehaviour == 3) {
			this.goalSelector.addGoal(1, this.attackAI);
		} else if (this.commandBehaviour == 4) {
			this.goalSelector.removeGoal(this.attackAI);
		}
	}

	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount) {
		super.damageEntity(damageSrc, damageAmount);
		if (!this.canUseNP && !this.isDead() && this.getHealth() < 0.5 * this.getMaxHealth()) {
			this.canUseNP = true;
		}
	}

	@Override
	public void livingTick() {
		if (this.getHealth() < 0.25 * this.getMaxHealth() && this.getHealth() > 0) {
			if (this.critHealth == false) {
				this.critHealth = true;
			}
		}
		this.addPotionEffect(new EffectInstance(Effects.SPEED, 1, 1, false, false));
		super.livingTick();
	}

	@Override
	public float damageModifier(Entity target) {
		if(target instanceof LivingEntity) {
			for (EffectInstance effect : ((LivingEntity) target).getActivePotionEffects()) {
				if (!effect.getPotion().isBeneficial())
					return 2;
			}
		}
		return super.damageModifier(target);
	}

	public void attackWithNP(LivingEntity target) {
		target.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 7200, 2));
		target.addPotionEffect(new EffectInstance(Effects.POISON, 800, 1));
		this.attackEntityAsMob(target);
		this.revealServant();
	}
}
