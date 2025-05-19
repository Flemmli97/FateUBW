package io.github.flemmli97.fateubw.common.items.weapons;

import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.entity.misc.Excalibur;
import io.github.flemmli97.fateubw.common.lib.ItemTiers;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemExcalibur extends SwordItem {

    public ItemExcalibur(Item.Properties props) {
        super(ItemTiers.EXCALIBUR, 0, -2.4f, props);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        if (Config.Common.excaliburMana > 0)
            tooltipComponents.add(new TranslatableComponent("fateubw.tooltip.item.mana", Config.Common.excaliburMana).withStyle(ChatFormatting.AQUA));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            if (player.isCreative() || Platform.INSTANCE.getPlayerData(player).map(mana -> mana.getMana() >= Config.Common.excaliburMana).orElse(false)) {
                player.startUsingItem(hand);
                return InteractionResultHolder.consume(stack);
            }
            player.sendMessage(new TranslatableComponent("fateubw.chat.mana.missing").withStyle(ChatFormatting.AQUA), Util.NIL_UUID);
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        int i = this.getUseDuration(stack) - timeLeft;
        if (i < 40) {
            return;
        }
        if (!level.isClientSide) {
            if (!(entity instanceof Player player) || player.isCreative() || Platform.INSTANCE.getPlayerData(player).map(mana -> mana.useMana(player, Config.Common.excaliburMana)).orElse(false)) {
                Excalibur excalibur = new Excalibur(level, entity);
                level.addFreshEntity(excalibur);
            } else {
                player.sendMessage(new TranslatableComponent("fateubw.chat.mana.missing").withStyle(ChatFormatting.AQUA), Util.NIL_UUID);
            }
        }
        super.releaseUsing(stack, level, entity, timeLeft);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
}