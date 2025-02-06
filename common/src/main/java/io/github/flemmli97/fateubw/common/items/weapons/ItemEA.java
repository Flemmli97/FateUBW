package io.github.flemmli97.fateubw.common.items.weapons;

import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.entity.misc.EnumaElish;
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

public class ItemEA extends SwordItem {

    public ItemEA(Item.Properties props) {
        super(ItemTiers.EA, 0, -2.4f, props);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        if (Config.Common.eaMana > 0)
            tooltipComponents.add(new TranslatableComponent("fateubw.tooltip.item.mana", Config.Common.eaMana).withStyle(ChatFormatting.AQUA));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            if (player.isCreative() || Platform.INSTANCE.getPlayerData(player).map(mana -> mana.getMana() >= Config.Common.eaMana).orElse(false)) {
                player.startUsingItem(hand);
                Platform.INSTANCE.getItemStackData(stack).ifPresent(data -> data.setInUse(player, true, hand == InteractionHand.MAIN_HAND));
                return InteractionResultHolder.consume(stack);
            }
            player.sendMessage(new TranslatableComponent("fateubw.chat.mana.missing").withStyle(ChatFormatting.AQUA), Util.NIL_UUID);
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity entityLiving, int timeLeft) {
        int i = this.getUseDuration(stack) - timeLeft;
        Platform.INSTANCE.getItemStackData(stack).ifPresent(data -> data.setInUse(entityLiving, false, entityLiving.getUsedItemHand() == InteractionHand.MAIN_HAND));
        if (i < 40) {
            return;
        }
        if (!world.isClientSide) {
            if (!(entityLiving instanceof Player player) || ((Player) entityLiving).isCreative()) {
                EnumaElish ea = new EnumaElish(world, entityLiving);
                world.addFreshEntity(ea);
            } else {
                if (Platform.INSTANCE.getPlayerData(player).map(mana -> mana.useMana(player, Config.Common.eaMana)).orElse(false)) {
                    EnumaElish ea = new EnumaElish(world, entityLiving);
                    world.addFreshEntity(ea);
                } else {
                    player.sendMessage(new TranslatableComponent("fateubw.chat.mana.missing").withStyle(ChatFormatting.AQUA), Util.NIL_UUID);
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