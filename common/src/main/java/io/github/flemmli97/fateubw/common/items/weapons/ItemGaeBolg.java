package io.github.flemmli97.fateubw.common.items.weapons;

import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.entity.misc.GaeBolg;
import io.github.flemmli97.fateubw.common.lib.ItemTiers;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemGaeBolg extends ClassSpear {

    public ItemGaeBolg(Item.Properties props) {
        super(ItemTiers.GAE_BOLG, props, -2f, 4);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        if (CommonConfig.gaeBolgMana > 0)
            tooltipComponents.add(new TranslatableComponent("fateubw.tooltip.item.mana", CommonConfig.gaeBolgMana).withStyle(ChatFormatting.AQUA));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            if (player.isCreative() || Platform.INSTANCE.getPlayerData(player).map(mana -> mana.useMana(player, CommonConfig.gaeBolgMana)).orElse(false)) {
                GaeBolg gaeBolg = new GaeBolg(level, player);
                gaeBolg.shoot(player, player.getXRot(), player.getYRot(), 0, 1.5F, 0);
                level.addFreshEntity(gaeBolg);
                stack.shrink(1);
                return InteractionResultHolder.consume(stack);

            }
            player.sendMessage(new TranslatableComponent("fateubw.chat.mana.missing").withStyle(ChatFormatting.AQUA), Util.NIL_UUID);
            return InteractionResultHolder.fail(stack);
        }
        return InteractionResultHolder.success(stack);
    }
}
