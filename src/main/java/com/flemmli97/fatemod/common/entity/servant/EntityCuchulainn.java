package com.flemmli97.fatemod.common.entity.servant;

import com.flemmli97.fatemod.common.entity.EntityGaeBolg;
import com.flemmli97.fatemod.common.entity.servant.ai.EntityAICuchulainn;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.tenshilib.common.TextHelper;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCuchulainn extends EntityServant {

	public EntityAICuchulainn attackAI = new EntityAICuchulainn(this);
	
	private int gaeBolgThrowTick;
	
	private static final AnimatedAction npAttack = new AnimatedAction(19,15,"gae_bolg");
	private static final AnimatedAction[] anims = new AnimatedAction[] {AnimatedAction.vanillaAttack, npAttack};

	public EntityCuchulainn(World world) {
		super(world, EnumServantType.LANCER, "Gae Bolg", new ItemStack[] {new ItemStack(ModItems.gaebolg)});
        this.tasks.addTask(1, attackAI);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.gaebolg));       
	}
	
	@Override
	public boolean canUse(AnimatedAction anim, AttackType type)
	{
		if(type==AttackType.NP)
			return anim.getID().equals("gae_bolg");
		return anim.getID().equals("vanilla");
	}
	
	@Override
	public AnimatedAction[] getAnimations() {
		return anims;
	}

	@Override
	public void fall(float distance, float damageMultiplier) {}

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
		if (!this.canUseNP && !this.isDead() && this.getHealth() < 0.5 * this.getMaxHealth())
		{
			this.canUseNP=true;
		}
    }
	
	@Override
	public void onLivingUpdate() {
		if(!this.world.isRemote)
		{
			this.gaeBolgThrowTick=Math.max(0, --this.gaeBolgThrowTick);
			if(this.gaeBolgThrowTick==1 && this.getHeldItemMainhand().getItem()!=ModItems.gaebolg)
				this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(ModItems.gaebolg));
			if (this.getHealth() < 0.25 * this.getMaxHealth() && this.getHealth()>0)
			{
				if(this.critHealth == false)	
				{
					this.world.getMinecraftServer().getPlayerList().sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.servant.cuchulainn"), TextFormatting.GOLD));
					this.critHealth = true;
				}
				this.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:speed"), 1, 2,false,false));
			}
		}
		super.onLivingUpdate();
	}
	
	public void attackWithNP()
	{
		EntityLivingBase target = this.getAttackTarget();
		if(target != null)
		{
			EntityGaeBolg gaeBolg = new EntityGaeBolg(this.world, this);
			gaeBolg.shootAtPosition(target.posX, target.posY+target.getEyeHeight(), target.posZ, 1.5F, 0);
			this.world.spawnEntity(gaeBolg);
			this.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
			this.gaeBolgThrowTick=100;
			this.revealServant();
		}
	}
	
	public void retrieveGaeBolg()
	{
		this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(ModItems.gaebolg));
		this.gaeBolgThrowTick=0;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		tag.setInteger("GaeBolgTick", this.gaeBolgThrowTick);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		this.gaeBolgThrowTick=tag.getInteger("GaeBolgTick");
	}
}
