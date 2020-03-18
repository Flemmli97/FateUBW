package com.flemmli97.fatemod.common.entity.servant;

import java.util.ArrayList;
import java.util.List;

import com.flemmli97.fatemod.common.entity.servant.ai.EntityAIHassan;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.fatemod.common.utils.ServantUtils;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class EntityHassan extends EntityServant {

	public EntityAIHassan attackAI = new EntityAIHassan(this);
	
	private List<String> copieUUID = new ArrayList<String>();
	
	private static final AnimatedAction npAttack = new AnimatedAction(20,0,"np");
	private static final AnimatedAction[] anims = new AnimatedAction[] {AnimatedAction.vanillaAttack, npAttack};

	public static final int maxCopies = 5;
	
	public EntityHassan(World world) {
		super(world, EnumServantType.ASSASSIN, "Delusional Illusion", new ItemStack[] {new ItemStack(ModItems.dagger)});
        this.tasks.addTask(1, attackAI);
	    this.targetTasks.removeTask(targetServant);
	    this.targetServant = new EntityAINearestAttackableTarget<EntityServant>(this, EntityServant.class, 10, true, true, 
	    		(living)->living != null && !ServantUtils.inSameTeam(living, EntityHassan.this) && !EntityHassan.this.copieUUID.contains(living.getCachedUniqueIdString()));

	    this.targetTasks.addTask(0, targetServant);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.dagger));       
	}
	
	@Override
	public boolean canUse(AnimatedAction anim, AttackType type)
	{
		if(type==AttackType.NP)
			return anim.getID().equals("np");
		return anim.getID().equals("vanilla");
	}
	
	@Override
	public AnimatedAction[] getAnimations() {
		return anims;
	}
	
	public void removeCopy(EntityHassanCopy copy)
	{
		this.copieUUID.remove(copy.getCachedUniqueIdString());
	}
	
	public boolean addCopy(EntityHassanCopy copy)
	{
		if(this.copieUUID.size()<maxCopies)
		{
			this.copieUUID.add(copy.getCachedUniqueIdString());
			return true;
		}
		return false;
	}
	
	private List<EntityHassanCopy> gatherCopies()
	{
		ArrayList<EntityHassanCopy> list = new ArrayList<EntityHassanCopy>();
		for(EntityHassanCopy e : this.world.getEntitiesWithinAABB(EntityHassanCopy.class, this.getEntityBoundingBox().grow(32)))
		{
			if(this.copieUUID.contains(e.getCachedUniqueIdString()))
			{
				e.setOriginal(this);
				list.add(e);
			}
		}
		return list;
	}
	
	@Override
	public void setAttackTarget(EntityLivingBase entitylivingbaseIn) {
		for(EntityHassanCopy hassan : this.gatherCopies())
		{
			if(hassan!=null && hassan.isEntityAlive())
				hassan.setAttackTarget(entitylivingbaseIn);
		}
		super.setAttackTarget(entitylivingbaseIn);
	}

	@Override
	public void updateAI(int behaviour) {
	    super.updateAI(behaviour);
		if(behaviour == 3)
		{
			this.tasks.addTask(1, attackAI);
		}
		else if(behaviour == 4)
		{
			this.tasks.removeTask(attackAI);
		}
	}
	
	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount)
    {
		super.damageEntity(damageSrc, damageAmount);
		if (!this.dead && this.getHealth() < 0.5 * this.getMaxHealth())
		{
			this.canUseNP=true;
		}
    }

	public void attackWithNP()
	{
		if(this.gatherCopies().isEmpty())
		{
			this.copieUUID.clear();
			for(int i = 0; i < maxCopies; i++)
			{
				EntityHassanCopy hassan = new EntityHassanCopy(world, this);
				hassan.setLocationAndAngles(this.posX, this.posY,this.posZ, MathHelper.wrapDegrees(this.world.rand.nextFloat() * 360.0F), 0.0F);
				hassan.setAttackTarget(this.getAttackTarget());
				hassan.onInitialSpawn(this.world.getDifficultyForLocation(this.getPosition()), null);
				this.world.spawnEntity(hassan); 
				this.addCopy(hassan);
			}
			this.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:invisibility"),3600,1,true,false));
			this.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:resistance"),100,2,true,false));
			if(this.getAttackTarget() instanceof EntityLiving)
				((EntityLiving) this.getAttackTarget()).setAttackTarget(null);
			this.revealServant();
		}
	}

	@Override
	protected void onDeathUpdate() {
		for(EntityHassanCopy hassan : this.gatherCopies())
		{
			if(hassan!=null)
				hassan.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
		}
		super.onDeathUpdate();
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		NBTTagList copies = new NBTTagList();
		for(String hassan : this.copieUUID)
		{
			copies.appendTag(new NBTTagString(hassan));
		}
		tag.setTag("Copies", copies);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		NBTTagList list = tag.getTagList("Copies", Constants.NBT.TAG_STRING);
		for(int i = 0 ; i < list.tagCount(); i++)
		{
			this.copieUUID.add(list.getStringTagAt(i));
		}
		this.gatherCopies();
	}
}
