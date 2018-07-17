package com.flemmli97.fatemod.common.items;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemServantCharm extends Item implements IModelRegister{
	
	private String[] name = {"saber", "archer", "lancer", "rider", "berserker", "caster", "assassin"};
	public String playerName;
	
	public ItemServantCharm()
    {
        this.setCreativeTab(Fate.customTab);
        this.setHasSubtypes(true);
        this.setMaxStackSize(16);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "servant_medallion"));
		this.setUnlocalizedName(this.getRegistryName().toString());
    }

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
    	if(tab==this.getCreativeTab())
		    for (int i = 0; i < 8; i ++) {
		        list.add(new ItemStack(this, 1, i));
		    }
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String addName = null;
		if(stack.getItemDamage()==0)
		{
			return "item.servant_medallion";
		}
		else
		{
			addName = name[stack.getMetadata()-1];			
			return "item." + addName + "_servant_medallion";
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(!world.isRemote)
		{
			if(stack.getItemDamage()==0)
			{
				int i = world.rand.nextInt(7)+1;
				if(!player.capabilities.isCreativeMode)
					stack.shrink(1);
				ItemStack charm = new ItemStack(ModItems.charm, 1, i);
				EntityItem item = new EntityItem(world, player.posX, player.posY, player.posZ, charm);
				item.setPickupDelay(0);
				world.spawnEntity(item);	
		        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
	
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
		for(int x=1;x<8;x++)
        ModelLoader.setCustomModelResourceLocation(this, x, new ModelResourceLocation(this.getRegistryName()+"_"+name[x-1], "inventory"));
        
    }
}
