package com.flemmli97.fatemod.common.items;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.entity.EntityGem;
import com.flemmli97.fatemod.common.init.ModItems;
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

public class ItemGemShard extends Item{
		
	private ShardType type;
	
	public ItemGemShard(ShardType type)
    {
		this.type=type;
        this.setCreativeTab(Fate.customTab);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "gem_shard_"+type.getName()));
		this.setUnlocalizedName(this.getRegistryName().toString());
    }
	
	public static Item getItemFromType(ShardType type)
	{
		switch(type)
		{
			case EARTH:return ModItems.crystalEarth;
			case FIRE:return ModItems.crystalFire;
			case VOID:return ModItems.crystalVoid;
			case WATER:return ModItems.crystalWater;
			case WIND:return ModItems.crystalWind;
		}
		return ModItems.crystalFire;
	}
	
	public ShardType getType()
	{
		return this.type;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
        if (!player.capabilities.isCreativeMode)
        {
            stack.shrink(1);
        }
        switch(this.type)
        {
			case EARTH:
				break;
			case FIRE:
				break;
			case VOID:
				break;
			case WATER:
				break;
			case WIND:
				break;
        }

		player.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote)
       	{       	
        	EntityGem gem = new EntityGem(world, player, this);
        	gem.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0, 1.5F, 0);	
       		world.spawnEntity(gem);
       	}  
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	public static enum ShardType
	{
		FIRE("fire"),
		WATER("water"),
		EARTH("earth"),
		WIND("wind"),
		VOID("void");
		
		String name;
		ShardType(String s)
		{
			this.name=s;
		}
		
		protected String getName()
		{
			return this.name;
		}
	}
}
