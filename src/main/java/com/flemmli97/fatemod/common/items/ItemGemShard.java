package com.flemmli97.fatemod.common.items;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.entity.EntityGem;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
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

public class ItemGemShard extends Item implements IModelRegister{
		
	private String[] name = {"fire", "water", "earth", "wind", "void"};

	public ItemGemShard()
    {
        this.setCreativeTab(Fate.customTab);
        this.setHasSubtypes(true);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "gem_shard"));
		this.setUnlocalizedName(this.getRegistryName().toString());
    }
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
    	if(tab==this.getCreativeTab())
		    for (int i = 0; i < 5; i ++) {
		        list.add(new ItemStack(this, 1, i));
		    }
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String addName = name[stack.getMetadata()];
		return "item." + addName + "_gem_shard";		
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
        if (!player.capabilities.isCreativeMode)
        {
            stack.shrink(1);
        }

		player.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote)
       	{       	
        	EntityGem gem = new EntityGem(world, player, stack);
        	gem.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0, 1.5F, 0);	
       		world.spawnEntity(gem);
       	}  
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void initModel() {
        for(int x=0;x<5;x++)
            ModelLoader.setCustomModelResourceLocation(this, x, new ModelResourceLocation(this.getRegistryName()+"_"+name[x], "inventory"));
    }

}
