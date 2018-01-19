package com.flemmli97.fatemod.common.entity.servant;

import com.flemmli97.fatemod.common.entity.ai.EntityAIArthur;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
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
		super(world, EnumServantType.SABER, "Excalibur", 100, new Item[] {ModItems.excalibur});
        this.tasks.addTask(1, attackAI);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.invisexcabibur));       
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(300.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(15.0D);
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
	public boolean attackEntityFrom(DamageSource damageSource, float damage) {
		if (!this.dead && this.getHealth()-(float) Math.min(50, damage) < 0.5 * this.getMaxHealth())
		{
			this.canUseNP=true;
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.excalibur));
		}
		return super.attackEntityFrom(damageSource, damage);
	}

	@Override
	public void onLivingUpdate() {
		if (this.getHealth() < 0.25 * this.getMaxHealth())
		{
			if(critHealth == false)	
			{
				MinecraftServer minecraftserver = FMLCommonHandler.instance().getMinecraftServerInstance();
				if(!worldObj.isRemote)
				{
					minecraftserver.getPlayerList().sendChatMsg(new TextComponentString(TextFormatting.GOLD + "Avalons healing ability has activated"));
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
		//EntityExcalibur excalibur = new EntityExcalibur(worldObj, this);
		//this.worldObj.spawnEntityInWorld(excalibur);			
	}
}
