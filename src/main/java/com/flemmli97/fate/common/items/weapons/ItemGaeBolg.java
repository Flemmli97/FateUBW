package com.flemmli97.fate.common.items.weapons;

import com.flemmli97.fate.common.entity.EntityGaeBolg;
import com.flemmli97.fate.common.lib.ItemTiers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemGaeBolg extends ClassSpear {

    public ItemGaeBolg(Item.Properties props) {
        super(ItemTiers.gae_bolg, props, -1.5F, 4);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            EntityGaeBolg gaeBolg = new EntityGaeBolg(world, player);
            gaeBolg.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1.5F, 0);
            //IPlayer mana = player.getCapability(PlayerCapProvider.PlayerCap, null);
            if (player.isCreative()) {
                world.addEntity(gaeBolg);
                return ActionResult.success(stack);
            } else {
				/*if (mana.useMana(player, 15)) {
					world.addEntity(gaeBolg);
					stack.shrink(1);
					return ActionResult.success(stack);
				} else {
					player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.mana.missing"), TextFormatting.AQUA));
				}*/
            }
        }
        return ActionResult.fail(stack);
    }
}
