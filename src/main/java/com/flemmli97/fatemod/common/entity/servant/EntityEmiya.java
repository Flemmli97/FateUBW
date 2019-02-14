package com.flemmli97.fatemod.common.entity.servant;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.common.entity.EntityArcherArrow;
import com.flemmli97.fatemod.common.entity.EntityCaladBolg;
import com.flemmli97.fatemod.common.entity.servant.ai.EntityAIEmiya;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityEmiya extends EntityServant implements IRanged{
	
	EntityAIEmiya attackMelee = new EntityAIEmiya(this, false, 1, 1);
	EntityAIEmiya attackRanged = new EntityAIEmiya(this, true, 1, 16);

	private boolean rangedAttack;
	
	public EntityEmiya(World world) {
		super(world, EnumServantType.ARCHER, "Calad Bolg II", new ItemStack[] {new ItemStack(ModItems.archbow), new ItemStack(ModItems.bakuya), new ItemStack(ModItems.kanshou)});
		this.tasks.addTask(1, attackMelee);
	}	
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.kanshou));
		this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(ModItems.bakuya));
	}
	
	@Override
	public Pair<Integer, Integer> attackTickerFromState(State state) {
		if(this.rangedAttack)
			return Pair.of(30, 20);
		return Pair.of(20, 20);
	}
	
	@Override
	public int attackCooldown()
	{
		return this.rangedAttack?40:0;
	}
	
	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount)
    {
		super.damageEntity(damageSrc, damageAmount);
		if (!this.canUseNP && !this.isDead() && this.getHealth() < 0.5 * this.getMaxHealth())
		{
			this.canUseNP=true;
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.archbow));
			this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
			this.setAttackAI();
		}
    }
	
	private void setAttackAI() {
		this.tasks.removeTask(attackMelee);
		this.tasks.removeTask(attackRanged);
        ItemStack itemstack = this.getHeldItemMainhand();

		if(itemstack!=null && itemstack.getItem() == ModItems.archbow)
		{
			this.tasks.addTask(1, attackRanged);
			this.rangedAttack=true;
		}
		else
		{
			this.tasks.addTask(1, attackMelee);
			this.rangedAttack=false;
		}
	}
	
	@Override
	public void updateAI(int behaviour) {
		super.updateAI(behaviour);
		if(commandBehaviour == 3)
		{
			this.setAttackAI();
		}
		else if(commandBehaviour == 4)
		{
			this.tasks.removeTask(attackMelee);
			this.tasks.removeTask(attackRanged);
		}
	}

	@Override
	public void attackWithRangedAttack(EntityLivingBase target) {
		EntityArcherArrow arrow = new EntityArcherArrow(this.world, this);
		if(!world.isRemote)
		{
			double d0 = target.posX - this.posX;
			double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - arrow.posY;
			double d2 = target.posZ - this.posZ;
			double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
			arrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, 2);	       
			arrow.setDamage(arrow.getDamage()+5.0);	        
			arrow.setKnockbackStrength(0);	       
			this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));	        
			this.world.spawnEntity(arrow);
		}
    }

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		this.setAttackAI();
	}

	public void attackWithNP() {
		EntityLivingBase target = this.getAttackTarget();
		if(target != null)
		{
			EntityCaladBolg bolg = new EntityCaladBolg(this.world, this);
			bolg.shootAtPosition(target.posX, target.posY+target.getEyeHeight(), target.posZ, 2F, 0);
			this.world.spawnEntity(bolg);
			this.revealServant();
		}
	}

	@Override
	public boolean isDoingRangedAttack() {
		return this.rangedAttack;
	}
}
