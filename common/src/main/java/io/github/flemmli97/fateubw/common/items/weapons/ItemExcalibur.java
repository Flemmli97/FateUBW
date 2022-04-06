package io.github.flemmli97.fateubw.common.items.weapons;

import io.github.flemmli97.fateubw.common.entity.misc.Excalibur;
import io.github.flemmli97.fateubw.common.lib.ItemTiers;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;

public class ItemExcalibur extends SwordItem {

    public ItemExcalibur(Item.Properties props) {
        super(ItemTiers.excalibur, 0, -2.4f, props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity entityLiving, int timeLeft) {
        int i = this.getUseDuration(stack) - timeLeft;
        if (i < 40) {
            return;
        }
        if (!world.isClientSide) {
            Excalibur excalibur = new Excalibur(world, entityLiving);

            if (!(entityLiving instanceof Player) || ((Player) entityLiving).isCreative()) {
                world.addFreshEntity(excalibur);
            } else {
                Player player = (Player) entityLiving;
                if (Platform.INSTANCE.getPlayerData(player).map(mana -> mana.useMana(player, 30)).orElse(false)) {
                    world.addFreshEntity(excalibur);
                    player.sendMessage(new TranslatableComponent("fate.mana.use").withStyle(ChatFormatting.AQUA), Util.NIL_UUID);
                } else {
                    player.sendMessage(new TranslatableComponent("fate.mana.no").withStyle(ChatFormatting.AQUA), Util.NIL_UUID);
                }
            }
        }
        super.releaseUsing(stack, world, entityLiving, timeLeft);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
}