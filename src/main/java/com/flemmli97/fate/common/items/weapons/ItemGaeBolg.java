package com.flemmli97.fate.common.items.weapons;

import com.flemmli97.fate.common.capability.PlayerCapProvider;
import com.flemmli97.fate.common.entity.EntityGaeBolg;
import com.flemmli97.fate.common.lib.ItemTiers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ItemGaeBolg extends ClassSpear {

    public ItemGaeBolg(Item.Properties props) {
        super(ItemTiers.gae_bolg, props, -1.5f, 4);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            EntityGaeBolg gaeBolg = new EntityGaeBolg(world, player);
            gaeBolg.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1.5F, 0);

            if (player.isCreative()) {
                world.addEntity(gaeBolg);
                return ActionResult.resultSuccess(stack);
            } else {
                if (player.getCapability(PlayerCapProvider.PlayerCap).map(mana -> mana.useMana(player, 15)).orElse(false)) {
                    world.addEntity(gaeBolg);
                    stack.shrink(1);
                    player.sendMessage(new TranslationTextComponent("fate.mana.use").mergeStyle(TextFormatting.AQUA), Util.DUMMY_UUID);
                    return ActionResult.resultSuccess(stack);

                } else {
                    player.sendMessage(new TranslationTextComponent("fate.mana.no").mergeStyle(TextFormatting.AQUA), Util.DUMMY_UUID);
                }
            }
            return ActionResult.resultPass(stack);
        }
        return ActionResult.resultFail(stack);
    }
}
