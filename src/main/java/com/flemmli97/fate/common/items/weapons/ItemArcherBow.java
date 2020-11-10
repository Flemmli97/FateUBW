package com.flemmli97.fate.common.items.weapons;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.capability.PlayerCapProvider;
import com.flemmli97.fate.common.entity.EntityCaladBolg;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class ItemArcherBow extends BowItem {

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
                if (player.isCreative() || player.getCapability(PlayerCapProvider.PlayerCap).map(cap -> cap.useMana(player, 80)).orElse(false)) {
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
        //IPlayer mana = player.getCapability(PlayerCapProvider.PlayerCap, null);
        if (player.isCreative() || /*mana.getMana()>10 ||*/ this.charged(player.getHeldItem(hand))) {
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
            PlayerEntity playerentity = (PlayerEntity) entity;
            //IPlayer mana = entityplayer.getCapability(PlayerCapProvider.PlayerCap, null);
            boolean flag = playerentity.abilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemstack = playerentity.findAmmo(stack);

            int i = this.getUseDuration(stack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, world, playerentity, i, !itemstack.isEmpty() || flag);
            if (i < 0) return;

            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float f = getArrowVelocity(i);
                if (!((double) f < 0.1D)) {
                    boolean flag1 = playerentity.abilities.isCreativeMode || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem) itemstack.getItem()).isInfinite(itemstack, stack, playerentity));
                    if (!world.isRemote) {
                        ArrowItem arrowitem = (ArrowItem) (itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
                        AbstractArrowEntity abstractarrowentity = arrowitem.createArrow(world, itemstack, playerentity);
                        abstractarrowentity = customArrow(abstractarrowentity);
                        abstractarrowentity.setProperties(playerentity, playerentity.rotationPitch, playerentity.rotationYaw, 0.0F, f * 3.0F, 1.0F);
                        if (f == 1.0F) {
                            abstractarrowentity.setIsCritical(true);
                        }

                        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
                        if (j > 0) {
                            abstractarrowentity.setDamage(abstractarrowentity.getDamage() + (double) j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
                        if (k > 0) {
                            abstractarrowentity.setKnockbackStrength(k);
                        }

                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
                            abstractarrowentity.setFire(100);
                        }

                        stack.damageItem(1, playerentity, (p_220009_1_) -> {
                            p_220009_1_.sendBreakAnimation(playerentity.getActiveHand());
                        });
                        if (flag1 || playerentity.abilities.isCreativeMode && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
                            abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                        }

                        world.addEntity(abstractarrowentity);
                    }

                    world.playSound((PlayerEntity) null, playerentity.getX(), playerentity.getY(), playerentity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!flag1 && !playerentity.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            playerentity.inventory.deleteStack(itemstack);
                        }
                    }

                    playerentity.addStat(Stats.ITEM_USED.get(this));
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