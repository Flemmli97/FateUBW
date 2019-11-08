package com.flemmli97.fatemod.common.entity.servant;

import com.flemmli97.fatemod.common.entity.EntityBabylonWeapon;
import com.flemmli97.fatemod.common.entity.EntityEnumaElish;
import com.flemmli97.fatemod.common.entity.servant.ai.EntityAIGilgamesh;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityGilgamesh extends EntityServant implements IRanged{

	public EntityAIGilgamesh attackRanged = new EntityAIGilgamesh(this,true, 1, 16);
	public EntityAIGilgamesh attackMelee = new EntityAIGilgamesh(this,false, 1, 1);

	private boolean doRangedAttack;
	private static final AnimatedAction rangedAttack = new AnimatedAction(40, 10, "babylon1");
	private static final AnimatedAction rangedAttack2 = new AnimatedAction(40, 10, "babylon2");
	private static final AnimatedAction npAttack = new AnimatedAction(20,10,"np");
	private static final AnimatedAction[] anims = new AnimatedAction[] {AnimatedAction.vanillaAttack, rangedAttack, npAttack, rangedAttack2};
	
	public EntityGilgamesh(World world) {
		super(world, EnumServantType.ARCHER, "Enuma Elish", new ItemStack[] {new ItemStack(ModItems.enumaelish)/*babylon=*/});
        this.tasks.addTask(1, attackRanged);
        this.revealServant();
	}
	
	@Override
	public boolean canUse(AnimatedAction anim, AttackType type)
	{
		if(type==AttackType.RANGED)
			return anim.getID().equals("babylon1")||anim.getID().equals("babylon2");
		else if(type==AttackType.NP)
			return anim.getID().equals("np");
		return anim.getID().equals("vanilla");
	}
	
	@Override
	public AnimatedAction[] getAnimations() {
		return anims;
	}
	
	@Override
	public int attackCooldown()
	{
		return this.doRangedAttack?40:0;
	}

	@Override
	public void updateAI(int behaviour) {
		super.updateAI(behaviour);
		if(commandBehaviour == 3)
		{
			this.updateAI();
		}
		else if(commandBehaviour == 4)
		{
			this.tasks.removeTask(attackRanged);
			this.tasks.removeTask(attackMelee);
		}
	}
	
	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount)
    {
		super.damageEntity(damageSrc, damageAmount);
		if (!this.canUseNP && !this.isDead() && this.getHealth() < 0.5 * this.getMaxHealth())
		{
			this.canUseNP = true;
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.enumaelish));
			this.updateAI();
		}
    }
	
	private void updateAI() {
		if(this.getHeldItemMainhand().getItem()==ModItems.enumaelish)
		{
			this.tasks.removeTask(attackRanged);
			this.tasks.addTask(1, attackMelee);
			this.doRangedAttack=false;
		}
		else
		{
			this.tasks.removeTask(attackMelee);
			this.tasks.addTask(1, attackRanged);
			this.doRangedAttack=true;
		}
	}
	
	public void attackWithNP(double[] pos)
    {
        EntityEnumaElish ea = new EntityEnumaElish(world, this);
        if(pos!=null)
            ea.setRotationTo(pos[0], pos[1], pos[2], 0);
        this.world.spawnEntity(ea);
        this.revealServant();
    }
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		this.updateAI();
	}

	@Override
	public void attackWithRangedAttack(EntityLivingBase target) {
		int weaponAmount = this.getRNG().nextInt(15)+4;
		if(this.getAnimations()==null||this.getAnimation().getID().equals("babylon1"))
			this.spawnBehind(target, weaponAmount);
		else if(this.getAnimation().getID().equals("babylon2"))
			this.spawnAroundTarget(target, weaponAmount);
	}
	
	private void spawnBehind(EntityLivingBase target, int amount)
	{
		for(int x = 0; x < amount; x++)
		{
			EntityBabylonWeapon weapon = new EntityBabylonWeapon(this.world, this, target);
			if(!world.isRemote)
			{
				weapon.setEntityProperties();
			}
		}
	}
	
	private void spawnAroundTarget(EntityLivingBase target, int amount)
	{
		for(int x = 0; x < amount; x++)
		{
			EntityBabylonWeapon weapon = new EntityBabylonWeapon(this.world, this, target);
			if(!world.isRemote)
			{
				weapon.setEntityProperties();
			}
		}
	}
	
	@Override
	public boolean isDoingRangedAttack() {
		return this.doRangedAttack;
	}
}
