package com.flemmli97.fatemod.common.item;

import java.util.List;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.entity.EntityGem;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
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

public class ItemGemShard extends Item {
		
	private String[] name = {"fire", "water", "earth", "wind", "void"};

	public ItemGemShard()
    {
		super();
        this.setCreativeTab(Fate.customTab);
        this.setHasSubtypes(true);
		GameRegistry.register(this, new ResourceLocation(Fate.MODID, "gem_shard"));

    }
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
	    for (int i = 0; i < 5; i ++) {
	        list.add(new ItemStack(item, 1, i));
	    }
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String addName = name[stack.getMetadata()];
		return "item." + addName + "_gem_shard";		
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (!player.capabilities.isCreativeMode)
        {
            --stack.stackSize;
        }

		player.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote)
       	{       	
        	EntityGem gem = new EntityGem(world, player, stack);
        	gem.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0, 1.5F, 0);	
       		world.spawnEntityInWorld(gem);
       	}  
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
        for(int x=0;x<5;x++)
            ModelLoader.setCustomModelResourceLocation(this, x, new ModelResourceLocation(this.getRegistryName()+"_"+name[x], "inventory"));
    }

}
