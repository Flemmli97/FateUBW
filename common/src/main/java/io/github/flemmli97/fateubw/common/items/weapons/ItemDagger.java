package io.github.flemmli97.fateubw.common.items.weapons;

import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.entity.misc.ThrownItemEntity;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemDagger extends SwordItem {

    public ItemDagger(Tier tier, int baseDmg, float speed, Properties props) {
        super(tier, baseDmg, speed, props);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        if (Config.Common.daggerThrowMana > 0)
            tooltipComponents.add(new TranslatableComponent("fateubw.tooltip.item.mana", Config.Common.daggerThrowMana).withStyle(ChatFormatting.AQUA));
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            ItemStack stack = player.getItemInHand(hand);
            if (player.isCreative() || Platform.INSTANCE.getPlayerData(player).map(mana -> mana.useMana(player, Config.Common.daggerThrowMana)).orElse(false)) {
                ThrownItemEntity dagger = new ThrownItemEntity(level, player);
                dagger.setWeapon(stack.copy());
                dagger.shoot(player, player.getXRot(), player.getYRot(), 0, 1.5f, 0);
                level.addFreshEntity(dagger);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW, player.getSoundSource(), 1.0F, 1.0F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
                return InteractionResultHolder.success(stack);
            }
            player.sendMessage(new TranslatableComponent("fateubw.chat.mana.missing").withStyle(ChatFormatting.AQUA), Util.NIL_UUID);
            return InteractionResultHolder.fail(stack);
        }
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }
}
