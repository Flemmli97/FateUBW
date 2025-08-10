package io.github.flemmli97.fateubw.common.items.weapons;

import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.lib.ItemTiers;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
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

public class ExcaliburItem extends SwordItem {

    public ExcaliburItem(Item.Properties props) {
        super(ItemTiers.EXCALIBUR, 0, -2.4f, props);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        if (CommonConfig.excaliburMana > 0)
            tooltipComponents.add(Component.translatable("fateubw.tooltip.item.mana", CommonConfig.excaliburMana).withStyle(ChatFormatting.AQUA));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            if (player.isCreative() || Platform.INSTANCE.getPlayerData(player).map(mana -> mana.getMana() >= CommonConfig.excaliburMana).orElse(false)) {
                player.startUsingItem(hand);
                return InteractionResultHolder.consume(stack);
            }
            player.sendSystemMessage(Component.translatable("fateubw.chat.mana.missing").withStyle(ChatFormatting.AQUA));
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
            if (!(entity instanceof Player player) || player.isCreative() || Platform.INSTANCE.getPlayerData(player).map(mana -> mana.useMana(player, CommonConfig.excaliburMana)).orElse(false)) {
                io.github.flemmli97.fateubw.common.entity.misc.Excalibur excalibur = new io.github.flemmli97.fateubw.common.entity.misc.Excalibur(level, entity);
                level.addFreshEntity(excalibur);
            } else {
                player.sendSystemMessage(Component.translatable("fateubw.chat.mana.missing").withStyle(ChatFormatting.AQUA));
            }
        }
        super.releaseUsing(stack, level, entity, timeLeft);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
}