package com.flemmli97.fatemod.common.entity.servant;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.common.entity.EntityExcalibur;
import com.flemmli97.fatemod.common.entity.servant.ai.EntityAIArthur;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

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
		// TODO Auto-generated method stub
		return Pair.of(30, 20);
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
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.excalibur));
		}
    }

	@Override
	public void onLivingUpdate() {
		if (this.getHealth() < 0.25 * this.getMaxHealth())
		{
			if(critHealth == false)	
			{
				MinecraftServer minecraftserver = FMLCommonHandler.instance().getMinecraftServerInstance();
				if(!world.isRemote)
				{
					minecraftserver.getPlayerList().sendMessage(new TextComponentString(TextFormatting.GOLD + "Avalons healing ability has activated"));
				}
				critHealth = true;
			}
			if(!this.isPotionActive(Potion.getPotionById(10)))
			{
				this.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:regeneration"), 40, 3, false, false));
			}			
		}
		super.onLivingUpdate();
	}		
	
	public void attackWithNP()
	{
		EntityExcalibur excalibur = new EntityExcalibur(world, this);
		this.world.spawnEntity(excalibur);
		this.revealServant();
	}
}
