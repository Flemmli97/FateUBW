package com.flemmli97.fatemod.common.entity.servant;

import java.util.List;

import com.flemmli97.fatemod.common.entity.EntityPegasus;
import com.flemmli97.fatemod.common.entity.servant.ai.EntityAIMedusa;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityMedusa extends EntityServant {

	public EntityAIMedusa attackAI = new EntityAIMedusa(this);
	
	private static final AnimatedAction npAttack = new AnimatedAction(20,0,"np");
	private static final AnimatedAction[] anims = new AnimatedAction[] {AnimatedAction.vanillaAttack, npAttack};
	
	public EntityMedusa(World world) {
		super(world, EnumServantType.RIDER, "Bellerophon", new ItemStack[] {new ItemStack(ModItems.medusaDagger)});
		this.tasks.addTask(1, attackAI);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.medusaDagger));       
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
	protected void damageEntity(DamageSource damageSrc, float damageAmount)
    {
		super.damageEntity(damageSrc, damageAmount);
		if (!this.canUseNP && !this.dead && this.getHealth() < 0.5 * this.getMaxHealth())
		{
			this.canUseNP=true;
		}
    }
	
	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float damage) {
		if(this.getRidingEntity()!=null && this.getRidingEntity() instanceof EntityLiving)
		{
			EntityLiving mount = (EntityLiving) this.getRidingEntity();
			return mount.attackEntityFrom(damageSource, damage);
		}
		return super.attackEntityFrom(damageSource, damage);
	}
	
	public void attackWithNP()
	{
		if(!this.world.isRemote)
		{
			EntityPegasus peg = new EntityPegasus(this.world);
			peg.setPosition(this.posX, this.posY, this.posZ);
			for(int x=0;x < 5;x++)
			{
				this.world.addWeatherEffect(new EntityLightningBolt(this.world, this.posX, this.posY, this.posZ, true));
			}
			List<Entity> list =this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(5, 3.0D, 5));
			for(int x = 0; x< list.size();x++)
			{
				if(list.get(x) instanceof EntityLivingBase)
				{
					EntityLivingBase ent = (EntityLivingBase) list.get(x);
	                ent.knockBack(this, 5.0F, MathHelper.sin(this.rotationYaw * 0.017453292F), (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
				}
			}
			this.world.spawnEntity(peg);
			this.startRiding(peg);
			this.revealServant();
		}
	}

}
