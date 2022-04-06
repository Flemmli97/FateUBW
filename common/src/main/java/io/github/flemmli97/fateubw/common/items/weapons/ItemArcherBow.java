package io.github.flemmli97.fateubw.common.items.weapons;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.misc.ArcherArrow;
import io.github.flemmli97.fateubw.common.entity.misc.CaladBolg;
import io.github.flemmli97.fateubw.common.items.SwingItem;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class ItemArcherBow extends BowItem implements SwingItem {

    private final int arrowMana = 10, specialMana = 80;

    public ItemArcherBow(Properties props) {
        super(props);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (!this.charged(stack) && !entity.level.isClientSide && entity instanceof Player) {
            Player player = (Player) entity;
            if (player.isCreative())
                this.setCharged(stack, true);
            else {
                if (player.isCreative() || Platform.INSTANCE.getPlayerData(player).map(cap -> cap.useMana(player, this.specialMana)).orElse(false)) {
                    this.setCharged(stack, true);
                }
            }
        }
        return false;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int timeLeft) {
        if (this.charged(stack)) {
            this.spawnCaladBolg(world, entity, stack, timeLeft);
        } else {
            this.spawnNormalArrow(stack, world, entity, timeLeft);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (player.isCreative() || this.charged(player.getItemInHand(hand)) || Platform.INSTANCE.getPlayerData(player).map(cap -> cap.getMana() >= this.arrowMana).orElse(false)) {
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

    public void spawnCaladBolg(Level world, LivingEntity entityLiving, ItemStack stack, int timeLeft) {
        CaladBolg bolg = new CaladBolg(world, entityLiving);
        if (!world.isClientSide) {
            int i = this.getUseDuration(stack) - timeLeft;
            float f = getPowerForTime(i);
            if (f >= 0.1D) {
                bolg.shoot(entityLiving, entityLiving.getXRot(), entityLiving.getYRot(), 0, f * 2, 0);
                world.addFreshEntity(bolg);
                this.setCharged(stack, false);
            }
        }
    }

    public void spawnNormalArrow(ItemStack stack, Level world, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0
                    || Platform.INSTANCE.getPlayerData(player).map(cap -> cap.useMana(player, this.arrowMana)).orElse(false);
            int i = this.getUseDuration(stack) - timeLeft;

            if (flag) {
                float f = getPowerForTime(i) * 1.2f;
                if (f >= 0.1D) {
                    if (!world.isClientSide) {
                        ArcherArrow arrow = new ArcherArrow(player.level, player);
                        arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);
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
                        world.addFreshEntity(arrow);
                    }

                    world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (player.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    private boolean charged(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getBoolean(Fate.MODID + ":Charged");
    }

    private void setCharged(ItemStack stack, boolean flag) {
        CompoundTag compound = stack.getOrCreateTag();
        compound.putBoolean(Fate.MODID + ":Charged", flag);
        stack.setTag(compound);
    }
}