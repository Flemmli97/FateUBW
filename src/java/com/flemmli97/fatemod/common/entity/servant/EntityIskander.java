package com.flemmli97.fatemod.common.entity.servant;

import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class EntityIskander extends EntityServant {

	//public EntityAIAnimatedAttack attackAI = new EntityAIAnimatedAttack(this);
	
	public EntityIskander(World world) {
		super(world, EnumServantType.RIDER, "", 0, new Item[] {});
        //this.setCurrentItemOrArmor(0, new ItemStack(ModItems.alexanderSpatha));
		//this.tasks.addTask(1, attackAI);
        this.targetTasks.addTask(1, targetServant);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(500.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
	}
	
	/*@Override
	public void onLivingUpdate()
	{
		if(!worldObj.isRemote)
		{
			if (this.isRiding() && this.getRidingEntity() instanceof EntityBukephalos)
			{			
				System.out.println(getRidingEntity() );
				System.out.println(this.getNavigator().getPath());
				((EntityLiving) this.getRidingEntity()).getNavigator().getPathToEntityLiving(this.getAttackTarget());
			}
		}
		super.onLivingUpdate();
	}
	
	boolean wasRiding = false;
	
	@Override
	public boolean attackEntityFrom(DamageSource par1damageSource, float par2) {
		super.attackEntityFrom(par1damageSource, par2);
		float healthPoint;
		float maxHealth;
		healthPoint = this.getHealth();
		maxHealth = this.getMaxHealth();
		if (!worldObj.isRemote)
		if (healthPoint < 0.5 * maxHealth)
		{ 
			if(!wasRiding)
			{					
			EntityBukephalos var9 = new EntityBukephalos(this.worldObj);
            var9.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            this.worldObj.spawnEntityInWorld(var9);
            this.startRiding(var9); 
            this.wasRiding = true;
			}
			
		}
		
		return super.attackEntityFrom(par1damageSource, par2);
	}*/
}
