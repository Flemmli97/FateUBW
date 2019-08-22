package com.flemmli97.fatemod.common.handler.capabilities;

import java.util.UUID;
import java.util.function.Predicate;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.network.MessageCommandSeals;
import com.flemmli97.fatemod.network.MessageMana;
import com.flemmli97.fatemod.network.MessageServantSync;
import com.flemmli97.fatemod.network.PacketHandler;
import com.flemmli97.tenshilib.common.entity.EntityUtil;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

public class PlayerCap implements IPlayer{
	
	private int currentMana = 0;
	private EntityServant servant;
	//Used during load
	private UUID servantUUID =null;
	private int commandSeals = 0;
	
	private NBTTagCompound savedServant;
	
	public PlayerCap() {}
	
	@Override
    public void setMana(EntityPlayer player, int mana) {
        this.currentMana = mana;
        if(player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new MessageMana(this), (EntityPlayerMP) player);
		}
    }

	@Override
    public int getMana() {
        return this.currentMana;
    }
    
	@Override
    public boolean useMana(EntityPlayer player, int amount) {
		boolean flag = currentMana>amount;
		if(flag)
			currentMana -= amount;
		if(player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new MessageMana(this), (EntityPlayerMP) player);
		}
		return flag;
	}
	
	private static final Predicate<EntityServant> notDead = new Predicate<EntityServant>() {
		@Override
		public boolean test(EntityServant t) {
			return !t.isDead();
		}};
		
	@Override
	public EntityServant getServant(EntityPlayer player) {
		if(this.servant==null && this.servantUUID!=null)
		{
			this.setServant(player, EntityUtil.findFromUUID(EntityServant.class, player.world, this.servantUUID, notDead));
		}
		return this.servant;
	}

	@Override
	public void setServant(EntityPlayer player, EntityServant servant) {
		this.servant = servant;	
		if(servant!=null)
			this.servantUUID=servant.getUniqueID();
		else
			this.servantUUID=null;
		if(player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new MessageServantSync(servant), (EntityPlayerMP) player);
		}
	}
	
	@Override
	public String getServantName() {
		if(servant!=null)
		{
			return I18n.format(servant.getName());
		}
		return "No servant";
	}
	
	@Override
	public void saveServant(EntityPlayer player) {
		if(this.getServant(player)!=null)
		{
			this.savedServant=this.getServant(player).writeToNBT(new NBTTagCompound());
			this.savedServant.removeTag("Pos");
			this.savedServant.removeTag("Motion");
			this.savedServant.removeTag("Rotation");
			this.savedServant.removeTag("UUIDMost");
			this.savedServant.removeTag("UUIDLeast");
			//Vanilla-fix incompability
			this.savedServant.removeTag("VFAABB");
		}
	}

	//TODO: to improve
	@Override
	public void restoreServant(EntityPlayer player) {
		/*if(this.savedServant!=null && !player.world.isRemote)
		{
			Entity e = EntityList.createEntityFromNBT(this.savedServant, player.world);
			if(e!=null)
			{
				Vec3d look = player.getLookVec();
				e.setPosition(player.posX+look.x, player.posY, player.posZ+look.z);
				player.world.spawnEntity(e);
			}
		}*/
	}
	
	@Override
	public int getCommandSeals() {
		return this.commandSeals;
	}

	@Override
	public boolean useCommandSeal(EntityPlayer player) {
		boolean flag = this.commandSeals>0;
		if(flag)
			this.commandSeals--;
		if(player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new MessageCommandSeals(this), (EntityPlayerMP) player);
		}
		return flag;
	}

	@Override
	public void setCommandSeals(EntityPlayer player, int amount) {
		this.commandSeals=Math.min(amount, 3);
		if(player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new MessageCommandSeals(this), (EntityPlayerMP) player);
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setInteger("Mana", this.currentMana);
		compound.setInteger("CommandSeal", this.commandSeals);
		if(this.servant!=null)
		{
			compound.setString("ServantUUID", this.servant.getCachedUniqueIdString());
		}
		if(this.savedServant!=null)
			compound.setTag("SavedServant", this.savedServant);
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		this.currentMana=compound.getInteger("Mana");
		this.commandSeals=compound.getInteger("CommandSeal");
		if(compound.hasKey("ServantUUID"))
		{
			this.servantUUID=UUID.fromString(compound.getString("ServantUUID"));
		}
		if(compound.hasKey("SavedServant"))
			this.savedServant=compound.getCompoundTag("SavedServant");
	}
}
