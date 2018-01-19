package com.flemmli97.fatemod.common.item;

import java.util.List;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemServantCharm extends Item {
	
	private String[] name = {"saber", "archer", "lancer", "rider", "berserker", "caster", "assassin"};
	public String playerName;
	
	public ItemServantCharm()
    {
		super();
        this.setCreativeTab(Fate.customTab);
        this.setHasSubtypes(true);
        this.setMaxStackSize(16);
    		GameRegistry.register(this, new ResourceLocation(Fate.MODID, "servant_medallion"));
    }


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
	    for (int i = 0; i < 8; i ++) {
	        list.add(new ItemStack(item, 1, i));
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
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if(!world.isRemote)
		{
			if(stack.getItemDamage()==0)
			{
				int i = world.rand.nextInt(7)+1;
				if(!player.capabilities.isCreativeMode)
				--stack.stackSize;
				ItemStack charm = new ItemStack(ModItems.charm, 1, i);
				EntityItem item = new EntityItem(world, player.posX, player.posY, player.posZ, charm);
				item.setPickupDelay(0);
				world.spawnEntityInWorld(item);	
		        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
	
	}
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
		for(int x=1;x<8;x++)
        ModelLoader.setCustomModelResourceLocation(this, x, new ModelResourceLocation(this.getRegistryName()+"_"+name[x-1], "inventory"));
        
    }
}
