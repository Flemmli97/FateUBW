package com.flemmli97.fatemod.common.entity.servant;

import com.flemmli97.fatemod.common.entity.EntityGaeBolg;
import com.flemmli97.fatemod.common.entity.ai.EntityAICuchulainn;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.entity.EntityLivingBase;
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

public class EntityCuchulainn extends EntityServant {

	public EntityAICuchulainn attackAI = new EntityAICuchulainn(this);
	
	public EntityCuchulainn(World world) {
		super(world, EnumServantType.LANCER, "Gae Bolg", new Item[] {ModItems.gaebolg});
        this.tasks.addTask(1, attackAI);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.gaebolg));       
	}

	@Override
	public void fall(float distance, float damageMultiplier) {}

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
		if (this.getHealth()-damage < 0.5 * this.getMaxHealth())
		{
			canUseNP = true;
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
					minecraftserver.getPlayerList().sendChatMsg(new TextComponentString(TextFormatting.GOLD + "Cuchulainn's speed increased"));
				}
				critHealth = true;
			}
			this.addPotionEffect(new PotionEffect(Potion.getPotionById(1), 1, 2,false,false));
			
		}
		super.onLivingUpdate();
	}
	
	public void attackWithNP()
	{
		EntityLivingBase target = this.getAttackTarget();
		if(target != null)
		{
			EntityGaeBolg gaeBolg = new EntityGaeBolg(worldObj, this);
			gaeBolg.setHeadingToPosition(target.posX, target.posY+target.getEyeHeight(), target.posZ, 1.5F, 0);
			this.worldObj.spawnEntityInWorld(gaeBolg);
		}
	}
}
