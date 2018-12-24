package com.flemmli97.fatemod.common.entity.servant;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.common.entity.EntityGaeBolg;
import com.flemmli97.fatemod.common.entity.servant.ai.EntityAICuchulainn;
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
		return Pair.of(15, 10);
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
		if (this.getHealth() < 0.25 * this.getMaxHealth() && this.getHealth()>0)
		{
			if(this.critHealth == false)	
			{
				if(!this.world.isRemote)
				{
					this.world.getMinecraftServer().getPlayerList().sendMessage(ServantUtils.setColor(new TextComponentTranslation("chat.servant.cuchulainn"), TextFormatting.GOLD));
				}
				this.critHealth = true;
			}
			this.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:speed"), 1, 2,false,false));
			
		}
		super.onLivingUpdate();
	}
	
	public void attackWithNP()
	{
		EntityLivingBase target = this.getAttackTarget();
		if(target != null)
		{
			EntityGaeBolg gaeBolg = new EntityGaeBolg(this.world, this);
			gaeBolg.setHeadingToPosition(target.posX, target.posY+target.getEyeHeight(), target.posZ, 1.5F, 0);
			this.world.spawnEntity(gaeBolg);
			this.revealServant();
		}
	}
}
