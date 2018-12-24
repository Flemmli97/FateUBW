package com.flemmli97.fatemod.common.entity.servant;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.common.entity.EntityExcalibur;
import com.flemmli97.fatemod.common.entity.servant.ai.EntityAIArthur;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.fatemod.common.utils.ServantUtils;

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
	public Pair<Integer, Integer> attackTickerFromState(State state) {
		return Pair.of(18, 10);
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
					this.world.getMinecraftServer().getPlayerList().sendMessage(ServantUtils.setColor(new TextComponentTranslation("chat.servant.avalon"), TextFormatting.GOLD));
				}
				critHealth = true;
			}
			if(!this.isPotionActive(Potion.getPotionFromResourceLocation("minecraft:regeneration")))
			{
				this.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:regeneration"), 40, 3, false, false));
			}			
		}
		super.onLivingUpdate();
	}		
	
	public void attackWithNP()
	{
		EntityExcalibur excalibur = new EntityExcalibur(this.world, this);
		this.world.spawnEntity(excalibur);
		this.revealServant();
	}
}
