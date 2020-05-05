package com.flemmli97.fatemod.common.entity.servant;

import com.flemmli97.fatemod.common.entity.servant.ai.EntityAIHeracles;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

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

	private static final AnimatedAction swing_1 = new AnimatedAction(20,18,"swing_1");
	private static final AnimatedAction[] anims = new AnimatedAction[] {swing_1};

    protected static final DataParameter<Integer> deathCount = EntityDataManager.<Integer>createKey(EntityHeracles.class, DataSerializers.VARINT );
	private boolean voidDeath;
	public EntityAIHeracles attackAI = new EntityAIHeracles(this);

	public EntityHeracles(World world) {
		super(world, EnumServantType.BERSERKER, "God Hand", new ItemStack[] {new ItemStack(ModItems.heraclesAxe)});
		this.setSize(0.8F, 2.4F);
        this.tasks.addTask(1, this.attackAI);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.heraclesAxe));       
	}
	
	@Override
	public boolean canUse(AnimatedAction anim, AttackType type)
	{
		return true;
	}
	
	@Override
	public AnimatedAction[] getAnimations() {
		return anims;
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
		if(this.commandBehaviour == 3)
		{
			this.tasks.addTask(1, this.attackAI);
		}
		else if(this.commandBehaviour == 4)
		{
			this.tasks.removeTask(this.attackAI);
		}
	}
	
	public void setDeathNumber(int death)
	{
		this.dataManager.set(deathCount, death);
	}
	
	public int getDeaths()
	{
		return this.dataManager.get(deathCount);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float damage) {
		if(damageSource != DamageSource.OUT_OF_WORLD && damage<4)
			return false;
		return super.attackEntityFrom(damageSource, damage);
	}
	
	@Override
	public void onLivingUpdate() {
		if(this.getDeaths()>4 && this.getDeaths()<=8)
		{
			this.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:strength"), 1, 1, false, false));

		}
		else if(this.getDeaths()>8)
		{
			this.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:strength"), 1, 2, false, false));
		}
		super.onLivingUpdate();
	}

	@Override
	protected void onDeathUpdate() {
		if(this.getLastDamageSource()==DamageSource.OUT_OF_WORLD || this.voidDeath)
		{
			this.voidDeath = true;
			super.onDeathUpdate();
		}
		else if(!this.voidDeath)
		{
			if(this.getDeaths() < 12 )
			{
				this.deathTicks++;
				if(this.deathTicks == 40 && !this.world.isRemote)
				{
					this.setDeathNumber(this.getDeaths()+1);
					double heal = 1- this.getDeaths()*0.08;
					this.setHealth((float) (heal*this.getMaxHealth()));
					this.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:resistance"), 200, 3, false, false));
					this.deathTicks = 0;
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
		tag.setInteger("Deaths", this.getDeaths());
		tag.setBoolean("DeathType", this.voidDeath);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		this.setDeathNumber(tag.getInteger("Deaths"));
		this.voidDeath = tag.getBoolean("DeathType");
	}
	
	@Override
	public int attackCooldown()
	{
		return 7;
	}
}
