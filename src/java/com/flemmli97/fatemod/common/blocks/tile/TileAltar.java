package com.flemmli97.fatemod.common.blocks.tile;

import java.util.ArrayList;
import java.util.List;

import com.flemmli97.fatemod.common.blocks.BlockAltar;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.fatemod.network.MessageAltarUpdate;
import com.flemmli97.fatemod.network.PacketHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;

public final class TileAltar extends TileEntity implements ITickable{
	
	boolean isComplete=false;
	ItemStack inventoryCharm;
	ItemStack[] invCatalyst = new ItemStack[8];
	boolean isSummoning = false;
	int summoningTick = 0;
	EntityPlayer player = null;

	public ItemStack getCharm()
	{
		return inventoryCharm;
	}
	
	public ItemStack[] getCatalyst()
	{
		return invCatalyst;
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
		if(stack.getItem() == ModItems.charm && stack.getItemDamage()!=0)
		{
			if(this.inventoryCharm == null)
			{
				ItemStack stackToAdd = stack.copy();
				stackToAdd.stackSize = 1;
				inventoryCharm= stackToAdd;
				if(player != null && !player.capabilities.isCreativeMode)
				{
					stack.stackSize--;
					if(stack.stackSize == 0)
						player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
				}
				flag=true;
			}
		}
		else if(stack.getItem() == ModItems.crystalCluster)
		{
			ItemStack add = stack.copy();
			add.stackSize=1;
			for(int x = 0; x<invCatalyst.length; x++)
			{
				ItemStack inv = invCatalyst[x];
				if(inv==null)
				{
					invCatalyst[x]=add;
					if(player != null && !player.capabilities.isCreativeMode)
					{
						stack.stackSize--;
						if(stack.stackSize == 0)
							player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
					}
					flag=true;
					break;
				}
			}
		}
		this.markDirty();
		return flag;
	}
	
	private void setItem(List<ItemStack> stackList)
	{
		for(int x = 0; x < stackList.size(); x++)
		{
			ItemStack stack = stackList.get(x);
			if(stack!=null && stack.getItem() == ModItems.charm)
				this.inventoryCharm = stack;
			else
			{
				for(int y = 0; y<invCatalyst.length; y++)
				{
					ItemStack inv = invCatalyst[y];
					if(inv==null)
					{
						invCatalyst[y]=stack;
						break;
					}
				}
			}
				
		}
		this.markDirty();
	}
	
	public void removeItem(EntityPlayer player)
	{
		if(this.inventoryCharm != null) 
		{
			if(player!=null && !player.capabilities.isCreativeMode)
			{
				player.inventory.addItemStackToInventory(inventoryCharm);
			}
			inventoryCharm = null;		
		}
		else if(invCatalyst!=null)
		{
			for(int x = 0; x<invCatalyst.length; x++)
			{
				ItemStack inv = invCatalyst[x];
				if(inv!=null)
				{
					if(player != null && !player.capabilities.isCreativeMode)
					{
						player.inventory.addItemStackToInventory(inv);
					}
					invCatalyst[x]=null;
					break;
				}
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
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		if(compound.hasKey("Charm"))
		{
			NBTTagCompound tag = (NBTTagCompound) compound.getTag("Charm");
			ItemStack stack = ItemStack.loadItemStackFromNBT(tag);
			stackList.add(stack);	
		}
		if(compound.hasKey("Items"))
		{
			 NBTTagList nbttaglist = compound.getTagList("Items", 10);
	
	         for (int i = 0; i < nbttaglist.tagCount(); ++i)
	         {
	             NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
	             stackList.add(ItemStack.loadItemStackFromNBT(nbttagcompound));
	         }
		}
		this.setItem(stackList);
		this.isComplete=compound.getBoolean("complete");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
        if (inventoryCharm != null)
        {
    			NBTTagCompound tag =new NBTTagCompound();
        		compound.setTag("Charm",inventoryCharm.writeToNBT(tag));
        }
        NBTTagList list = new NBTTagList();
        for(int x = 0; x<invCatalyst.length; x++)
		{
			ItemStack inv = invCatalyst[x];
			if(inv!=null)
			{
				NBTTagCompound nbttagcompound = new NBTTagCompound();
                inv.writeToNBT(nbttagcompound);
                list.appendTag(nbttagcompound);
			}
		}
        compound.setTag("Items", list);
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
			if(!this.worldObj.isRemote)
			{
				if(this.summoningTick==1)
				{
					this.worldObj.playSound(null, pos, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.AMBIENT, 0.4F, 1F);
				}
				if(this.summoningTick>150)
				{
					{
						BlockAltar.summonRandomServant(this.inventoryCharm, this.player, this.pos, this.worldObj, this.player.getCapability(PlayerCapProvider.PlayerCap, null));	
						BlockAltar.removeStructure(this.worldObj, this.getPos());
					}
				}
			}
		}
	}

	public boolean isSummoning() {
		return this.isSummoning;
	}
}
