package com.flemmli97.fate.common.capability;

import java.util.UUID;
import java.util.function.Predicate;

import com.flemmli97.fate.common.entity.servant.EntityServant;
import com.flemmli97.tenshilib.common.entity.EntityUtil;

import com.flemmli97.tenshilib.common.network.PacketHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class PlayerCap implements IPlayer {

	private static final Predicate<EntityServant> notDead = t -> !t.isDead();

	private int currentMana = 0;
	private EntityServant servant;
	//Used during load
	private UUID servantUUID = null;
	private int commandSeals = 0;

	private CompoundNBT savedServant;

	public PlayerCap() {
	}

	@Override
	public void setMana(PlayerEntity player, int mana) {
		this.currentMana = mana;
		if (player instanceof ServerPlayerEntity)
			PacketHandler.sendTo(new MessageMana(this), (ServerPlayerEntity) player);
	}

	@Override
	public int getMana() {
		return this.currentMana;
	}

	@Override
	public boolean useMana(PlayerEntity player, int amount) {
		boolean flag = this.currentMana > amount;
		if (flag)
			this.currentMana -= amount;
		if (player instanceof ServerPlayerEntity)
			PacketHandler.sendTo(new MessageMana(this), (ServerPlayerEntity) player);
		return flag;
	}

	@Override
	public EntityServant getServant(PlayerEntity player) {
		if (this.servant == null && this.servantUUID != null)
			this.setServant(player, EntityUtil.findFromUUID(EntityServant.class, player.world, this.servantUUID, notDead));
		return this.servant;
	}

	@Override
	public void setServant(PlayerEntity player, EntityServant servant) {
		this.servant = servant;
		if (servant != null)
			this.servantUUID = servant.getUniqueID();
		else
			this.servantUUID = null;
		if (player instanceof ServerPlayerEntity)
			PacketHandler.sendTo(new MessageServantSync(servant), (ServerPlayerEntity) player);
	}

	@Override
	public ITextComponent getServantName() {
		if (this.servant != null) {
			return this.servant.getName();
		}
		return new StringTextComponent("No servant");
	}

	@Override
	public void saveServant(PlayerEntity player) {
		if (this.getServant(player) != null) {
			this.savedServant = this.getServant(player).writeToNBT(new CompoundNBT());
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
	public void restoreServant(PlayerEntity player) {
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
	public boolean useCommandSeal(PlayerEntity player) {
		boolean flag = this.commandSeals > 0;
		if (flag)
			this.commandSeals--;
		if (player instanceof ServerPlayerEntity)
			PacketHandler.sendTo(new MessageCommandSeals(this), (ServerPlayerEntity) player);
		return flag;
	}

	@Override
	public void setCommandSeals(PlayerEntity player, int amount) {
		this.commandSeals = Math.min(amount, 3);
		if (player instanceof ServerPlayerEntity)
			PacketHandler.sendTo(new MessageCommandSeals(this), (ServerPlayerEntity) player);
	}

	@Override
	public CompoundNBT writeToNBT(CompoundNBT compound) {
		compound.putInt("Mana", this.currentMana);
		compound.putInt("CommandSeal", this.commandSeals);
		if (this.servant != null)
			compound.putUniqueId("ServantUUID", this.servant.getUniqueID());
		if (this.savedServant != null)
			compound.put("SavedServant", this.savedServant);
		return compound;
	}

	@Override
	public void readFromNBT(CompoundNBT compound) {
		this.currentMana = compound.getInt("Mana");
		this.commandSeals = compound.getInt("CommandSeal");
		if (compound.contains("ServantUUID"))
			this.servantUUID = compound.getUniqueId("ServantUUID");
		if (compound.contains("SavedServant"))
			this.savedServant = compound.getCompound("SavedServant");
	}
}