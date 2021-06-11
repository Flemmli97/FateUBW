package com.flemmli97.fate.common.items.weapons;

import com.flemmli97.fate.common.capability.PlayerCapProvider;
import com.flemmli97.fate.common.entity.EntityEnumaElish;
import com.flemmli97.fate.common.lib.ItemTiers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ItemEA extends SwordItem {

    public ItemEA(Item.Properties props) {
        super(ItemTiers.ea, 0, -2.4f, props);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (!world.isRemote) {
            EntityEnumaElish ea = new EntityEnumaElish(world, player);

            if (player.isCreative()) {
                world.addEntity(ea);
                return ActionResult.resultSuccess(player.getHeldItem(hand));
            } else {
                if (player.getCapability(PlayerCapProvider.PlayerCap).map(mana -> mana.useMana(player, 30)).orElse(false)) {
                    world.addEntity(ea);
                    player.sendMessage(new TranslationTextComponent("fate.mana.use").mergeStyle(TextFormatting.AQUA), Util.DUMMY_UUID);
                    return ActionResult.resultSuccess(player.getHeldItem(hand));
                } else {
                    player.sendMessage(new TranslationTextComponent("fate.mana.no").mergeStyle(TextFormatting.AQUA), Util.DUMMY_UUID);
                }
            }
            return ActionResult.resultPass(player.getHeldItem(hand));
        }
        return ActionResult.resultPass(player.getHeldItem(hand));
    }
}