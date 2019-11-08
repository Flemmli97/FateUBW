package com.flemmli97.fatemod.common.entity.servant;

import com.flemmli97.fatemod.common.entity.EntityExcalibur;
import com.flemmli97.fatemod.common.entity.servant.ai.EntityAIArthur;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.tenshilib.common.TextHelper;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityArthur extends EntityServant{
	
	public EntityAIArthur attackAI = new EntityAIArthur(this);

	private static final AnimatedAction swing_1 = new AnimatedAction(18,15,"swing_1");

	private static final AnimatedAction npAttack = new AnimatedAction(21,17,"excalibur");
	private static final AnimatedAction[] anims = new AnimatedAction[] {swing_1, npAttack};

	public EntityArthur(World world)
	{
		super(world, EnumServantType.SABER, "Excalibur", new ItemStack[] {new ItemStack(ModItems.excalibur)});
        this.tasks.addTask(1, attackAI);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.invisexcalibur));       
	}

	@Override
	public boolean canUse(AnimatedAction anim, AttackType type)
	{
		if(type==AttackType.NP)
			return anim.getID().equals("excalibur");
		return anim.getID().equals("swing_1");
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
		if (!this.canUseNP && !this.isDead() && this.getHealth() < 0.5 * this.getMaxHealth())
		{
			this.canUseNP=true;
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.excalibur));
		}
    }

	@Override
	public void onLivingUpdate() {
		if (this.getHealth() < 0.25 * this.getMaxHealth() && this.getHealth()>0)
		{
			if(this.critHealth == false)	
			{
				if(!this.world.isRemote)
				{
					this.world.getMinecraftServer().getPlayerList().sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.servant.avalon"), TextFormatting.GOLD));
				}
				critHealth = true;
			}
			if(!this.isPotionActive(Potion.getPotionFromResourceLocation("minecraft:regeneration")))
			{
				this.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:regeneration"), 40, 1, false, false));
			}			
		}
		super.onLivingUpdate();
	}		
	
	public void attackWithNP(double[] pos)
	{
		EntityExcalibur excalibur = new EntityExcalibur(this.world, this);
		if(pos!=null)
		    excalibur.setRotationTo(pos[0], pos[1], pos[2], 0);
		this.world.spawnEntity(excalibur);
		this.revealServant();
	}
}
