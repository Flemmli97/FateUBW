package io.github.flemmli97.fateubw.common.items.weapons;

import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.entity.misc.ArcherArrow;
import io.github.flemmli97.fateubw.common.entity.misc.CaladBolg;
import io.github.flemmli97.fateubw.common.items.SwingItem;
import io.github.flemmli97.fateubw.common.registry.FateDataComponents;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.List;

public class ArcherBowItem extends BowItem implements SwingItem {

    public ArcherBowItem(Properties props) {
        super(props);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
        if (CommonConfig.archerBowMana > 0)
            tooltipComponents.add(Component.translatable("fateubw.tooltip.item.bow.arrow", CommonConfig.archerBowMana).withStyle(ChatFormatting.AQUA));
        if (CommonConfig.caladbolgMana > 0)
            tooltipComponents.add(Component.translatable("fateubw.tooltip.item.caladbolg", CommonConfig.caladbolgMana).withStyle(ChatFormatting.AQUA));
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        if (!this.charged(stack) && !entity.level().isClientSide && entity instanceof Player player) {
            if (player.isCreative())
                this.setCharged(stack, true);
            else {
                if (player.isCreative() || Platform.INSTANCE.getPlayerData(player).useMana(CommonConfig.caladbolgMana)) {
                    this.setCharged(stack, true);
                }
            }
        }
        return false;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (this.charged(stack)) {
            this.spawnCaladBolg(level, entity, stack, timeLeft);
        } else {
            this.spawnNormalArrow(stack, level, entity, timeLeft);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (player.isCreative() || this.charged(player.getItemInHand(hand)) || Platform.INSTANCE.getPlayerData(player).getMana() >= CommonConfig.archerBowMana) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(player.getItemInHand(hand));
        } else {
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return this.charged(stack) || super.isFoil(stack);
    }

    public void spawnCaladBolg(Level level, LivingEntity entity, ItemStack stack, int timeLeft) {
        CaladBolg bolg = new CaladBolg(level, entity);
        if (!level.isClientSide) {
            int i = this.getUseDuration(stack, entity) - timeLeft;
            float f = getPowerForTime(i * 2);
            if (f >= 0.1D) {
                bolg.shoot(entity, entity.getXRot(), entity.getYRot(), 0, f, 0);
                level.addFreshEntity(bolg);
                this.setCharged(stack, false);
            }
        }
    }

    public void spawnNormalArrow(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (entity instanceof ServerPlayer player) {
            int ammoCount = player.hasInfiniteMaterials() ? 0 : EnchantmentHelper.processAmmoUse(player.serverLevel(), stack, new ItemStack(Items.ARROW), 1);
            boolean flag = ammoCount <= 0 || Platform.INSTANCE.getPlayerData(player).useMana(CommonConfig.archerBowMana * ammoCount);
            int i = this.getUseDuration(stack, entity) - timeLeft;

            if (flag) {
                float f = getPowerForTime(i * 2);
                if (f >= 0.1D) {
                    if (!level.isClientSide) {
                        AbstractArrow arrow = this.customArrow(new ArcherArrow(player.level(), player, stack), ItemStack.EMPTY, stack);
                        arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 2.5F, 1.0F);
                        if (f == 1.0F)
                            arrow.setCritArrow(true);
                        stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(entity.getUsedItemHand()));
                        level.addFreshEntity(arrow);
                    }
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (player.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    public boolean charged(ItemStack stack) {
        return stack.has(FateDataComponents.ARCHER_BOW_CHARGED.get());
    }

    private void setCharged(ItemStack stack, boolean flag) {
        if (flag)
            stack.set(FateDataComponents.ARCHER_BOW_CHARGED.get(), Unit.INSTANCE);
        else
            stack.remove(FateDataComponents.ARCHER_BOW_CHARGED.get());
    }

    // NeoForge delegate
    public AbstractArrow customArrow(AbstractArrow arrow, ItemStack projectile, ItemStack weaponStack) {
        if (arrow instanceof ArcherArrow)
            return arrow;
        return new ArcherArrow(arrow.level(), arrow.getOwner() instanceof LivingEntity owner ? owner : null, weaponStack);
    }
}