package io.github.flemmli97.fate.common.items.weapons;

import io.github.flemmli97.fate.common.capability.CapabilityInsts;
import io.github.flemmli97.fate.common.entity.EntityExcalibur;
import io.github.flemmli97.fate.common.lib.ItemTiers;
import net.minecraft.entity.LivingEntity;
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

public class ItemExcalibur extends SwordItem {

    public ItemExcalibur(Item.Properties props) {
        super(ItemTiers.excalibur, 0, -2.4f, props);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        player.setActiveHand(hand);
        return ActionResult.resultConsume(player.getHeldItem(hand));
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity entityLiving, int timeLeft) {
        int i = this.getUseDuration(stack) - timeLeft;
        if (i < 40) {
            return;
        }
        if (!world.isRemote) {
            EntityExcalibur excalibur = new EntityExcalibur(world, entityLiving);

            if (!(entityLiving instanceof PlayerEntity) || ((PlayerEntity) entityLiving).isCreative()) {
                world.addEntity(excalibur);
            } else {
                PlayerEntity player = (PlayerEntity) entityLiving;
                if (player.getCapability(CapabilityInsts.PlayerCap).map(mana -> mana.useMana(player, 30)).orElse(false)) {
                    world.addEntity(excalibur);
                    player.sendMessage(new TranslationTextComponent("fate.mana.use").mergeStyle(TextFormatting.AQUA), Util.DUMMY_UUID);
                } else {
                    player.sendMessage(new TranslationTextComponent("fate.mana.no").mergeStyle(TextFormatting.AQUA), Util.DUMMY_UUID);
                }
            }
        }
        super.onPlayerStoppedUsing(stack, world, entityLiving, timeLeft);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
}