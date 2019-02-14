package com.flemmli97.fatemod.common.entity.servant;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.common.entity.servant.ai.EntityAIDiarmuid;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.fatemod.common.utils.ServantUtils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityDiarmuid extends EntityServant {

	public EntityAIDiarmuid attackAI = new EntityAIDiarmuid(this);
	
	public EntityDiarmuid(World world) {
		super(world, EnumServantType.LANCER, "", new ItemStack[] {new ItemStack(ModItems.gaebuidhe), new ItemStack(ModItems.gaedearg)});
        this.tasks.addTask(1, attackAI);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.gaedearg));
		this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(ModItems.gaebuidhe)); 
	}
	
	@Override
	public Pair<Integer, Integer> attackTickerFromState(State state) {
		return Pair.of(20, 20);
	}
	
	@Override
	public void fall(float damage, float damagemulti) {
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
		}
    }
	
	@Override
	public void onLivingUpdate() {
		if (this.getHealth() < 0.25 * this.getMaxHealth() && this.getHealth()>0)
		{
			if(this.critHealth == false)	
			{
				if(!world.isRemote)
				{
					//this.world.getMinecraftServer().getPlayerList().sendMessage(ServantUtils.setColor(new TextComponentTranslation("chat.servant.diarmuid"), TextFormatting.GOLD));
				}
				this.critHealth = true;
			}
			//this.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:speed"), 1, 2, false, false));
		}
		super.onLivingUpdate();
	}
	
	@Override
	public float damageModifier() {
		if(this.world.rand.nextFloat()<0.6)
			for(PotionEffect effect : this.getAttackTarget().getActivePotionEffects())
			{
				if(effect.getPotion().isBadEffect())
					return 2;
			}
		return super.damageModifier();
	}

	public void attackWithNP(EntityLivingBase target) {
		target.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:weakness"), 7200, 2));
		target.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:poison"), 800, 1));
		this.revealServant();
	}
}
