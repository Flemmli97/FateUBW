package com.flemmli97.fate.common.items;

import com.flemmli97.fate.common.registry.ModItems;
import com.flemmli97.fate.common.utils.EnumServantType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemServantCharm extends Item {

    public final EnumServantType type;

    public ItemServantCharm(EnumServantType type, Properties props) {
        super(props);
        this.type = type;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (this.type == EnumServantType.NOTASSIGNED) {
            if (!world.isRemote) {
                if (!player.isCreative())
                    stack.shrink(1);
                ItemEntity item = new ItemEntity(world, player.getX(), player.getY(), player.getZ(),
                        new ItemStack(ModItems.charms.get(world.rand.nextInt(ModItems.charms.size())).get()));
                item.setPickupDelay(0);
                world.addEntity(item);
            }
            return ActionResult.success(stack);

        }
        return ActionResult.pass(stack);
    }
}