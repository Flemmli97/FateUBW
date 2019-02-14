package com.flemmli97.fatemod.common.items;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.entity.EntityGem;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemGemCluster extends Item{
		
	public ItemGemCluster()
    {
        this.setCreativeTab(Fate.customTab);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "gem_cluster"));
		this.setUnlocalizedName(this.getRegistryName().toString());
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
        	EntityGem gem = new EntityGem(world, player);
        	gem.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1.5F, 0);	
       		world.spawnEntity(gem);
       	}  
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
}
