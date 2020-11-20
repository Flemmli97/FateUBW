package com.flemmli97.fate.common.items.weapons;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.capability.PlayerCapProvider;
import com.flemmli97.fate.common.entity.EntityArcherArrow;
import com.flemmli97.fate.common.entity.EntityCaladBolg;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class ItemArcherBow extends BowItem {

    private final int arrowMana = 10, specialMana = 80;

    public ItemArcherBow(Properties props) {
        super(props);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (!this.charged(stack) && !entity.world.isRemote && entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (player.isCreative())
                this.setCharged(stack, true);
            else {
                if (player.isCreative() || player.getCapability(PlayerCapProvider.PlayerCap).map(cap -> cap.useMana(player, this.specialMana)).orElse(false)) {
                    this.setCharged(stack, true);
                }
            }
        }
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity entity, int timeLeft) {
        if (this.charged(stack)) {
            this.spawnCaladBolg(world, entity, stack, timeLeft);
        } else {
            this.spawnNormalArrow(stack, world, entity, timeLeft);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (player.isCreative() || this.charged(player.getHeldItem(hand)) || player.getCapability(PlayerCapProvider.PlayerCap).map(cap -> cap.getMana() >= this.arrowMana).orElse(false)) {
            player.setActiveHand(hand);
            return ActionResult.consume(player.getHeldItem(hand));
        } else {
            return ActionResult.fail(player.getHeldItem(hand));
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return this.charged(stack) || super.hasEffect(stack);
    }

    public void spawnCaladBolg(World world, LivingEntity entityLiving, ItemStack stack, int timeLeft) {
        EntityCaladBolg bolg = new EntityCaladBolg(world, entityLiving);
        if (!world.isRemote) {
            int i = this.getUseDuration(stack) - timeLeft;
            float f = getArrowVelocity(i);
            if (f >= 0.1D) {
                bolg.shoot(entityLiving, entityLiving.rotationPitch, entityLiving.rotationYaw, 0, f * 2, 0);
                world.addEntity(bolg);
                this.setCharged(stack, false);
            }
        }
    }

    public void spawnNormalArrow(ItemStack stack, World world, LivingEntity entity, int timeLeft) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            boolean flag = player.abilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0
                    || player.getCapability(PlayerCapProvider.PlayerCap).map(cap -> cap.useMana(player, this.arrowMana)).orElse(false);
            int i = this.getUseDuration(stack) - timeLeft;

            if (flag) {
                float f = getArrowVelocity(i) * 1.2f;
                if (f >= 0.1D) {
                    if (!world.isRemote) {
                        EntityArcherArrow arrow = new EntityArcherArrow(player.world, player);
                        arrow.setProperties(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3.0F, 1.0F);
                        if (f == 1.0F)
                            arrow.setIsCritical(true);

                        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
                        if (j > 0)
                            arrow.setDamage(arrow.getDamage() + j * 0.5D + 0.5D);
                        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
                        if (k > 0)
                            arrow.setKnockbackStrength(k);
                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0)
                            arrow.setFire(100);

                        stack.damageItem(1, player, (p) -> p.sendBreakAnimation(player.getActiveHand()));
                        world.addEntity(arrow);
                    }

                    world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    player.addStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    private boolean charged(ItemStack stack) {
        return stack.hasTag() ? stack.getTag().getBoolean(Fate.MODID + ":Charged") : false;
    }

    private void setCharged(ItemStack stack, boolean flag) {
        CompoundNBT compound = stack.getOrCreateTag();
        compound.putBoolean(Fate.MODID + ":Charged", flag);
        stack.setTag(compound);
    }
}