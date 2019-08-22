package com.flemmli97.fatemod.common.entity.servant;


import com.flemmli97.fatemod.common.entity.EntityCasterCircle;
import com.flemmli97.fatemod.common.entity.EntityMagicBeam;
import com.flemmli97.fatemod.common.entity.servant.ai.EntityAIMedea;
import com.flemmli97.fatemod.common.handler.ConfigHandler;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.google.common.base.Predicate;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityMedea extends EntityServant implements IRanged{

	EntityAIMedea attackAI = new EntityAIMedea(this);
	
	private static final AnimatedAction npAttack = new AnimatedAction(20,0,"np");
	private static final AnimatedAction[] anims = new AnimatedAction[] {AnimatedAction.vanillaAttack, npAttack};
	
	private boolean rangedAttack;
	private final Predicate<EntityCasterCircle> circlePred = (circle)->circle.getOwner()!=null && circle.getOwner()==EntityMedea.this;
	
	private int circleDelay;
	
	public EntityMedea(World world) {
		super(world, EnumServantType.CASTER, "Rule Breaker", new ItemStack[] {new ItemStack(ModItems.staff)});
		this.tasks.addTask(1, attackAI);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.staff));       
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
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if(!this.world.isRemote)
		{
			for(EntityCasterCircle e : this.world.getEntitiesWithinAABB(EntityCasterCircle.class, this.getEntityBoundingBox().grow(ConfigHandler.medeaCircleRange), circlePred))
				if(e.getDistanceSq(this.posX, e.posY, this.posZ)<=ConfigHandler.medeaCircleRange*ConfigHandler.medeaCircleRange)
					this.buff();
			this.circleDelay=Math.max(--circleDelay, 0);
		}
	}
	
	private void buff()
	{
		this.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:resistance"), 1, 2, true, false));
		if(!this.isPotionActive(Potion.getPotionFromResourceLocation("minecraft:regeneration")))
			this.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:regeneration"), 4, 1, true, false));
		this.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:strength"), 1, 2, true, false));
	}
	
	public void attackWithNP()
	{

	}

	@Override
	public void attackWithRangedAttack(EntityLivingBase target) {
		int strength = 0;
		PotionEffect eff = this.getActivePotionEffect(Potion.getPotionFromResourceLocation("minecraft:strength"));
		if(eff!=null)
			strength = eff.getAmplifier();
		EntityMagicBeam beam = new EntityMagicBeam(this.world, this, target, strength);
		//beam.setProjectileAreaPosition(3);
		this.world.spawnEntity(beam);
		this.revealServant();
	}
	
	public void makeCircle()
	{
		if(!this.world.isRemote && this.circleDelay==0)
		{
			this.world.spawnEntity(new EntityCasterCircle(this.world, this, ConfigHandler.medeaCircleRange));
			this.circleDelay=ConfigHandler.medeaCircleSpan;
			if(this.getOwner()!=null)
				this.getOwner().sendMessage(new TextComponentTranslation("chat.medea.circle.spawn"));
		}
	}

	@Override
	public String[] specialCommands() {
		return new String[] {"medea.circle"};
	}

	@Override
	public void doSpecialCommand(String s) {
		if(s.equals("medea.circle"))
			this.makeCircle();
	}

	@Override
	public boolean isDoingRangedAttack() {
		return this.rangedAttack;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		tag.setInteger("CircleDelay", this.circleDelay);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		this.circleDelay = tag.getInteger("CircleDelay");
	}
}
