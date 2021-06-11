package com.flemmli97.fate.common.items;

import com.flemmli97.fate.common.entity.EntityGem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class ItemCrystal extends Item {

    public ItemCrystal(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (world.isRemote)
            return ActionResult.resultSuccess(player.getHeldItem(hand));
        ItemStack stack = player.getHeldItem(hand);
        if (!player.isCreative())
            stack.shrink(1);
        player.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 0.5F, 0.4F / (player.getRNG().nextFloat() * 0.4F + 0.8F));
        EntityGem gem = new EntityGem(world, player);
        gem.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1.5F, 0);
        world.addEntity(gem);
        return ActionResult.resultSuccess(player.getHeldItem(hand));
    }
}
