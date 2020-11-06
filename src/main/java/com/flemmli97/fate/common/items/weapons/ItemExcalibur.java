package com.flemmli97.fate.common.items.weapons;

import com.flemmli97.fate.common.entity.EntityExcalibur;
import com.flemmli97.fate.common.lib.ItemTiers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemExcalibur extends SwordItem {

    public ItemExcalibur(Item.Properties props) {
        super(ItemTiers.excalibur, 0, -2.4f, props);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (!world.isRemote) {
            EntityExcalibur excalibur = new EntityExcalibur(world, player);

            //IPlayer mana = player.getCapability(PlayerCapProvider.PlayerCap, null);
            if (player.isCreative()) {
                world.addEntity(excalibur);
            } else {
				/*if (mana.useMana(player, 15)) {
					world.addEntity(excalibur);
					player.sendMessage(new TranslationTextComponent(TextFormatting.AQUA + "Used Mana"), Util.NIL_UUID);
				} else {
					player.sendMessage(new TranslationTextComponent(TextFormatting.AQUA + "You don't have enough mana"), Util.NIL_UUID);
				}*/
            }
            return ActionResult.success(player.getHeldItem(hand));
        }
        return ActionResult.pass(player.getHeldItem(hand));
    }
}
