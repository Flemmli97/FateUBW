package com.flemmli97.fatemod.common.blocks.tile;

import com.flemmli97.fatemod.common.blocks.BlockAltar;
import com.flemmli97.fatemod.common.entity.servant.EntityServant.EnumServantType;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.fatemod.common.items.ItemServantCharm;
import com.flemmli97.fatemod.network.MessageAltarUpdate;
import com.flemmli97.fatemod.network.PacketHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;

public final class TileAltar extends TileEntity implements ITickable{
	
	private boolean isComplete, isSummoning;
	private ItemStack inventoryCharm = ItemStack.EMPTY;
	private NonNullList<ItemStack> invCatalyst = NonNullList.withSize(8, ItemStack.EMPTY);
	private int summoningTick;
	private EntityPlayer player;

	public ItemStack getCharm()
	{
		return this.inventoryCharm;
	}
	
	public NonNullList<ItemStack> getCatalyst()
	{
		return this.invCatalyst;
	}
	
	public boolean isComplete()
	{
		return this.isComplete;
	}
	
	public void setComplete(boolean flag)
	{
		this.isComplete=flag;
	}
	
	public boolean addItem(EntityPlayer player, ItemStack stack)
	{
		boolean flag = false;
		if(stack.getItem() instanceof ItemServantCharm && ((ItemServantCharm)stack.getItem()).type()!=EnumServantType.NOTASSIGNED)
		{
			if(this.inventoryCharm.isEmpty())
			{
				ItemStack stackToAdd = stack.copy();
				stackToAdd.setCount(1);
				this.inventoryCharm= stackToAdd;
				if(player != null && !player.capabilities.isCreativeMode)
				{
					stack.shrink(1);
				}
				flag=true;
			}
		}
		else if(stack.getItem() == ModItems.crystalCluster)
		{
			ItemStack add = stack.copy();
			add.setCount(1);
			for(int x = 0; x<this.invCatalyst.size(); x++)
			{
				if(this.invCatalyst.get(x).isEmpty())
				{
					this.invCatalyst.set(x, add);
					if(player != null && !player.capabilities.isCreativeMode)
					{
						stack.shrink(1);
					}
					flag=true;
					break;
				}
			}
		}
		this.markDirty();
		return flag;
	}
	
	public void removeItem(EntityPlayer player)
	{
		if(!this.inventoryCharm.isEmpty()) 
		{
			if(player!=null && !player.capabilities.isCreativeMode)
			{
				player.inventory.addItemStackToInventory(this.inventoryCharm);
			}
            this.inventoryCharm =ItemStack.EMPTY;
		}
		for(int x = 0; x< this.invCatalyst.size(); x++)
		{
			ItemStack inv = this.invCatalyst.get(x);
			if(!inv.isEmpty())
			{
				if(player != null && !player.capabilities.isCreativeMode)
				{
					player.inventory.addItemStackToInventory(inv);
				}
                this.invCatalyst.set(x, ItemStack.EMPTY);
				break;
			}
		}
		this.markDirty();
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) 
	{
		this.readFromNBT(pkt.getNbtCompound());
		this.getWorld().notifyBlockUpdate(this.pos, this.getWorld().getBlockState(this.pos), this.getWorld().getBlockState(this.pos), 3);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if(compound.hasKey("Charm"))
		{
			NBTTagCompound tag = (NBTTagCompound) compound.getTag("Charm");
			this.inventoryCharm=new ItemStack(tag);	
		}
		ItemStackHelper.loadAllItems(compound, this.invCatalyst);
		this.isComplete=compound.getBoolean("complete");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if(!this.inventoryCharm.isEmpty())
			compound.setTag("Charm", this.inventoryCharm.writeToNBT(new NBTTagCompound()));
        ItemStackHelper.saveAllItems(compound, this.invCatalyst);
        compound.setBoolean("complete", this.isComplete);
        return compound;
	}
	
	public void setSummoning(EntityPlayer player)
	{
		this.isSummoning=true;
		this.player = player;
		PacketHandler.sendToAll(new MessageAltarUpdate(this.getPos(), true));
	}
	
	/**client side only for packet*/
	public void updateSummoning(boolean flag)
	{
		this.isSummoning = flag;
	}

	@Override
	public void update() {
		if(this.isSummoning)
		{
			this.summoningTick++;
			if(!this.world.isRemote)
			{
				if(this.summoningTick==1)
				{
					this.world.playSound(null, this.pos, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.AMBIENT, 0.4F, 1F);
				}
				if(this.summoningTick>150)
				{
					BlockAltar.summonRandomServant(this.inventoryCharm, this.player, this.pos, this.world, this.player.getCapability(PlayerCapProvider.PlayerCap, null));	
					BlockAltar.removeStructure(this.world, this.getPos());
				}
			}
		}
	}

	public boolean isSummoning() {
		return this.isSummoning;
	}
}
