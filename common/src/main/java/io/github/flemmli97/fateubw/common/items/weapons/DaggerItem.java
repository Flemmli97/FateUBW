package io.github.flemmli97.fateubw.common.items.weapons;

import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.entity.misc.ThrownItemEntity;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class DaggerItem extends SwordItem {

    public DaggerItem(Tier tier, Properties props) {
        super(tier, props);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
        if (CommonConfig.daggerThrowMana > 0)
            tooltipComponents.add(Component.translatable("fateubw.tooltip.item.mana", CommonConfig.daggerThrowMana).withStyle(ChatFormatting.AQUA));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            ItemStack stack = player.getItemInHand(hand);
            if (player.isCreative() || Platform.INSTANCE.getPlayerData(player).useMana(CommonConfig.daggerThrowMana)) {
                ThrownItemEntity dagger = new ThrownItemEntity(level, player);
                dagger.setWeapon(stack.copy());
                dagger.shoot(player, player.getXRot(), player.getYRot(), 0, 1.5f, 0);
                level.addFreshEntity(dagger);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW, player.getSoundSource(), 1.0F, 1.0F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
                return InteractionResultHolder.success(stack);
            }
            player.sendSystemMessage(Component.translatable("fateubw.chat.mana.missing").withStyle(ChatFormatting.AQUA));
            return InteractionResultHolder.fail(stack);
        }
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }
}
