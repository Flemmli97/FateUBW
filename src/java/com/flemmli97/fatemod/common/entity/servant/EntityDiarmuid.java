package com.flemmli97.fatemod.common.entity.servant;

import com.flemmli97.fatemod.common.entity.ai.EntityAIDiarmuid;
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

public class EntityDiarmuid extends EntityServant {

	public EntityAIDiarmuid attackAI = new EntityAIDiarmuid(this);
	
	public EntityDiarmuid(World world) {
		super(world, EnumServantType.LANCER, "", 100, new Item[] {ModItems.gaebuidhe, ModItems.gaedearg});
        this.tasks.addTask(1, attackAI);
        this.targetTasks.addTask(1, targetServant);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.gaedearg));
		this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(ModItems.gaebuidhe)); 
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(310.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30.0D);
	}
	
	@Override
	public int getTotalArmorValue() {
		return 5;
	}
	
	@Override
	public void fall(float damage, float damagemulti) {
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
		/*if(specialAnimation == 5)
		{
			attackWithNP(true, 0);
		}
		if(specialAnimation == 2)
		{
			attackWithNP(true, 1);
		}
		if(forcedNP)
		{
			specialAnimation = specialAnimationValue;
			worldObj.setEntityState(this, (byte)5);
			forcedNP = false;
		}*/
		super.updateAITasks();
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float damage) {
		if (this.getHealth() < 0.5 * this.getMaxHealth())
		{
			canUseNP = true;
		}
		if(damageSource == DamageSource.outOfWorld)
		{
			return super.attackEntityFrom(damageSource, damage);
		}
		else
		{
			return super.attackEntityFrom(damageSource, (float) Math.min(this.getMaxHealth() * 0.1, damage));
		}
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
			this.addPotionEffect(new PotionEffect(Potion.getPotionById(1), 1, 4));
			
		}
		super.onLivingUpdate();
	}

	@Override
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
		int chance = rand.nextInt(2);
		if(chance == 0)
		{
		this.dropItem(ModItems.gaedearg, 1);
		this.dropItem(ModItems.gaebuidhe, 1);
		}
		super.dropLoot(wasRecentlyHit, lootingModifier, source);
	}

	public void attackWithNP() {
		// TODO Auto-generated method stub
		
	}
}
