package com.flemmli97.fatemod.common.entity.servant;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.common.entity.servant.ai.EntityAIHeracles;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityHeracles extends EntityServant {

    protected static final DataParameter<Integer> deathCount = EntityDataManager.<Integer>createKey(EntityHeracles.class, DataSerializers.VARINT );
	private boolean voidDeath;
	public EntityAIHeracles attackAI = new EntityAIHeracles(this);

	public EntityHeracles(World world) {
		super(world, EnumServantType.BERSERKER, "God Hand", new ItemStack[] {new ItemStack(ModItems.heraclesAxe)});
		this.setSize(0.8F, 2.4F);
        this.tasks.addTask(1, attackAI);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.heraclesAxe));       
	}
	
	@Override
	public Pair<Integer, Integer> attackTickerFromState(State state) {
		return Pair.of(20, 20);
	}
	
	@Override
	protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(deathCount, 0);
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
	
	public void setDeathNumber(int death)
	{
		dataManager.set(deathCount, death);
	}
	
	public int getDeaths()
	{
		return dataManager.get(deathCount);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float damage) {
		if(damageSource != DamageSource.OUT_OF_WORLD && damage<4)
			return false;
		return super.attackEntityFrom(damageSource, damage);
	}
	
	@Override
	public void onLivingUpdate() {
		if(getDeaths()>4 && getDeaths()<=8)
		{
			this.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:strength"), 1, 1, false, false));

		}
		else if(getDeaths()>8)
		{
			this.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:strength"), 1, 2, false, false));
		}
		super.onLivingUpdate();
	}

	@Override
	protected void onDeathUpdate() {
		if(this.getLastDamageSource()==DamageSource.OUT_OF_WORLD || voidDeath)
		{
			this.voidDeath = true;
			super.onDeathUpdate();
		}
		else if(!voidDeath)
		{
			if(getDeaths() < 12 )
			{
				deathTicks++;
				if(deathTicks == 40 && !world.isRemote)
				{
					this.setDeathNumber(this.getDeaths()+1);
					double heal = 1-getDeaths()*0.08;
					this.setHealth((float) (heal*this.getMaxHealth()));
					this.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:resistance"), 200, 3, false, false));
					deathTicks = 0;
					this.revealServant();
				}
			}
			else
			{
				super.onDeathUpdate();
			}
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		tag.setInteger("Deaths", getDeaths());
		tag.setBoolean("DeathType", this.voidDeath);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		this.setDeathNumber(tag.getInteger("Deaths"));
		this.voidDeath = tag.getBoolean("DeathType");
	}
	
	
}
