package io.github.flemmli97.fateubw.common.items.weapons;

import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.entity.misc.ChainDagger;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class MedusasDaggerItem extends SwordItem {

    public MedusasDaggerItem(Tier tier, Properties props) {
        super(tier, props);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
        if (CommonConfig.chainMana > 0)
            tooltipComponents.add(Component.translatable("fateubw.tooltip.item.mana", CommonConfig.chainMana).withStyle(ChatFormatting.AQUA));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            PlayerData data = Platform.INSTANCE.getPlayerData(player);
            ChainDagger thrownDagger = data.getThrownDagger();
            if (thrownDagger == null) {
                if (player.isCreative() || data.useMana(CommonConfig.chainMana)) {
                    ChainDagger dagger = new ChainDagger(level, player, hand == InteractionHand.MAIN_HAND);
                    dagger.shoot(player, player.getXRot(), player.getYRot(), 0, 1.5f, 0);
                    level.addFreshEntity(dagger);
                    data.setThrownDagger(dagger);
                    return InteractionResultHolder.consume(player.getItemInHand(hand));
                }
                player.sendSystemMessage(Component.translatable("fateubw.chat.mana.missing").withStyle(ChatFormatting.AQUA));
                return InteractionResultHolder.fail(player.getItemInHand(hand));
            }
            thrownDagger.retractHook();
            return InteractionResultHolder.consume(player.getItemInHand(hand));
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
