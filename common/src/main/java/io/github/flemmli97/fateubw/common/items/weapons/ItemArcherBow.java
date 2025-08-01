package io.github.flemmli97.fateubw.common.items.weapons;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.entity.misc.ArcherArrow;
import io.github.flemmli97.fateubw.common.entity.misc.CaladBolg;
import io.github.flemmli97.fateubw.common.items.SwingItem;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemArcherBow extends BowItem implements SwingItem {


    public ItemArcherBow(Properties props) {
        super(props);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        if (CommonConfig.archerBowMana > 0)
            tooltipComponents.add(new TranslatableComponent("fateubw.tooltip.item.bow.arrow", CommonConfig.archerBowMana).withStyle(ChatFormatting.AQUA));
        if (CommonConfig.caladbolgMana > 0)
            tooltipComponents.add(new TranslatableComponent("fateubw.tooltip.item.caladbolg", CommonConfig.caladbolgMana).withStyle(ChatFormatting.AQUA));
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (!this.charged(stack) && !entity.level.isClientSide && entity instanceof Player player) {
            if (player.isCreative())
                this.setCharged(stack, true);
            else {
                if (player.isCreative() || Platform.INSTANCE.getPlayerData(player).map(cap -> cap.useMana(player, CommonConfig.caladbolgMana)).orElse(false)) {
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
        if (player.isCreative() || this.charged(player.getItemInHand(hand)) || Platform.INSTANCE.getPlayerData(player).map(cap -> cap.getMana() >= CommonConfig.archerBowMana).orElse(false)) {
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

    public void spawnCaladBolg(Level level, LivingEntity entityLiving, ItemStack stack, int timeLeft) {
        CaladBolg bolg = new CaladBolg(level, entityLiving);
        if (!level.isClientSide) {
            int i = this.getUseDuration(stack) - timeLeft;
            float f = getPowerForTime(i * 2);
            if (f >= 0.1D) {
                bolg.shoot(entityLiving, entityLiving.getXRot(), entityLiving.getYRot(), 0, f, 0);
                level.addFreshEntity(bolg);
                this.setCharged(stack, false);
            }
        }
    }

    public void spawnNormalArrow(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0
                    || Platform.INSTANCE.getPlayerData(player).map(cap -> cap.useMana(player, CommonConfig.archerBowMana)).orElse(false);
            int i = this.getUseDuration(stack) - timeLeft;

            if (flag) {
                float f = getPowerForTime(i * 2);
                if (f >= 0.1D) {
                    if (!level.isClientSide) {
                        AbstractArrow arrow = this.customArrow(new ArcherArrow(player.level, player)); // Forge
                        arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 2.5F, 1.0F);
                        if (f == 1.0F)
                            arrow.setCritArrow(true);

                        int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
                        if (j > 0)
                            arrow.setBaseDamage(arrow.getBaseDamage() + j * 0.5D + 0.5D);
                        int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
                        if (k > 0)
                            arrow.setKnockback(k);
                        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0)
                            arrow.setSecondsOnFire(100);

                        stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
                        level.addFreshEntity(arrow);
                    }

                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (player.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    public AbstractArrow customArrow(AbstractArrow arrow) {
        return new ArcherArrow(arrow.level, arrow.getOwner() instanceof LivingEntity owner ? owner : null);
    }

    public boolean charged(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getBoolean(Fate.MODID + ":Charged");
    }

    private void setCharged(ItemStack stack, boolean flag) {
        CompoundTag compound = stack.getOrCreateTag();
        compound.putBoolean(Fate.MODID + ":Charged", flag);
        stack.setTag(compound);
    }
}