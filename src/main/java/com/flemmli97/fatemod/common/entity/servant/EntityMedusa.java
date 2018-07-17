package com.flemmli97.fatemod.common.entity.servant;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.common.entity.EntityPegasus;
import com.flemmli97.fatemod.common.entity.servant.ai.EntityAIMedusa;
import com.flemmli97.fatemod.common.init.ModItems;

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

	EntityAIMedusa attackAI = new EntityAIMedusa(this, false, 1, 1);
	public EntityMedusa(World world) {
		super(world, EnumServantType.RIDER, "Bellerophon", new ItemStack[] {new ItemStack(ModItems.medusaDagger)});
		this.tasks.addTask(1, attackAI);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.medusaDagger));       
	}
	
	@Override
	public Pair<Integer, Integer> attackTickerFromState(State state) {
		// TODO Auto-generated method stub
		return Pair.of(0, 0);
	}
	
	@Override
	protected void updateAITasks() {
		if(commandBehaviour == 3)
		{
			this.tasks.addTask(1, attackAI);
		}
		else if(commandBehaviour == 4)
		{
			this.tasks.removeTask(attackAI);
		}
		super.updateAITasks();
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
			for(int x=0;x < 5;x++)
			{
				EntityLightningBolt bolt = new EntityLightningBolt(this.world, this.posX, this.posY, this.posZ, true);
				this.world.spawnEntity(bolt);
			}
			List<Entity> list =this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand((double)15, 3.0D, (double)15));
			for(int x = 0; x< list.size();x++)
			{
				if(list.get(x) instanceof EntityLivingBase)
				{
					EntityLivingBase ent = (EntityLivingBase) list.get(x);
					//Vec3d posVec = ent.getPositionVector();
					//Vec3d distVec;
					//float degree;
	                ent.knockBack(this, 1.0F, (double)MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
				}
			}
			this.world.spawnEntity(peg);
		}
	}

}
