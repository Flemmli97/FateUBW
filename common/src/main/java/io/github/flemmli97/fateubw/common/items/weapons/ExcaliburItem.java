package io.github.flemmli97.fateubw.common.items.weapons;

import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.entity.misc.Excalibur;
import io.github.flemmli97.fateubw.common.lib.ItemTiers;
import io.github.flemmli97.fateubw.common.registry.FateDataComponents;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ExcaliburItem extends SwordItem {

    public ExcaliburItem(Item.Properties props) {
        super(ItemTiers.EXCALIBUR, props);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
        if (CommonConfig.excaliburMana > 0)
            tooltipComponents.add(Component.translatable("fateubw.tooltip.item.mana", CommonConfig.excaliburMana).withStyle(ChatFormatting.AQUA));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            if (player.isCreative() || Platform.INSTANCE.getPlayerData(player).getMana() >= CommonConfig.excaliburMana) {
                player.startUsingItem(hand);
                stack.set(FateDataComponents.GLOWING_ITEM.get(), Unit.INSTANCE);
                return InteractionResultHolder.consume(stack);
            }
            player.sendSystemMessage(Component.translatable("fateubw.chat.mana.missing").withStyle(ChatFormatting.AQUA));
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        stack.remove(FateDataComponents.GLOWING_ITEM.get());
        int duration = this.getUseDuration(stack, entity) - timeLeft;
        if (duration < 40) {
            return;
        }
        if (!level.isClientSide) {
            if (!(entity instanceof Player player) || player.isCreative() || Platform.INSTANCE.getPlayerData(player).useMana(CommonConfig.excaliburMana)) {
                Excalibur excalibur = new Excalibur(level, entity);
                level.addFreshEntity(excalibur);
            } else {
                player.sendSystemMessage(Component.translatable("fateubw.chat.mana.missing").withStyle(ChatFormatting.AQUA));
            }
        }
        super.releaseUsing(stack, level, entity, timeLeft);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof LivingEntity living && living.getUseItem() != stack && stack.has(FateDataComponents.GLOWING_ITEM.get())) {
            stack.remove(FateDataComponents.GLOWING_ITEM.get());
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }
}