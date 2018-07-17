package com.flemmli97.fatemod.common.entity.servant;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.common.entity.EntityGaeBolg;
import com.flemmli97.fatemod.common.entity.servant.ai.EntityAICuchulainn;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.entity.EntityLivingBase;
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

public class EntityCuchulainn extends EntityServant {

	public EntityAICuchulainn attackAI = new EntityAICuchulainn(this);
	
	public EntityCuchulainn(World world) {
		super(world, EnumServantType.LANCER, "Gae Bolg", new ItemStack[] {new ItemStack(ModItems.gaebolg)});
        this.tasks.addTask(1, attackAI);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.gaebolg));       
	}
	
	@Override
	public Pair<Integer, Integer> attackTickerFromState(State state) {
		// TODO Auto-generated method stub
		return Pair.of(0, 0);
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
		if (this.getHealth() < 0.25 * this.getMaxHealth())
		{
			if(critHealth == false)	
			{
				MinecraftServer minecraftserver = FMLCommonHandler.instance().getMinecraftServerInstance();
				if(!world.isRemote)
				{
					minecraftserver.getPlayerList().sendMessage(new TextComponentString(TextFormatting.GOLD + "Cuchulainn's speed increased"));
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
			EntityGaeBolg gaeBolg = new EntityGaeBolg(world, this);
			gaeBolg.setHeadingToPosition(target.posX, target.posY+target.getEyeHeight(), target.posZ, 1.5F, 0);
			this.world.spawnEntity(gaeBolg);
		}
	}
}
