package io.github.flemmli97.fateubw.common.entity.servant.ai;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class LancelotAttackAI {

    private static final List<OrderedResource> ORDERED_RESOURCES = new ArrayList<>();
    private static final Map<ResourceLocation, ItemAI> ITEM_AI_MAP = new HashMap<>();

    public static void register(Function<String, Boolean> modChecker) {
        add(new ResourceLocation(Fate.MODID, "trident"), new ItemAI() {
            @Override
            public boolean attack(Mob entity, LivingEntity target, InteractionHand hand) {
                ItemStack stack = entity.getItemInHand(hand).copy();
                ThrownTrident tridententity = new ThrownTrident(entity.level, entity, stack);
                double d0 = target.getX() - entity.getX();
                double d1 = target.getY(0.3333333333333333D) - tridententity.getY();
                double d2 = target.getZ() - entity.getZ();
                double d3 = Math.sqrt(d0 * d0 + d2 * d2);
                tridententity.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, (float) (14 - entity.level.getDifficulty().getId() * 4));
                entity.playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (entity.getRandom().nextFloat() * 0.4F + 0.8F));
                entity.level.addFreshEntity(tridententity);
                return true;
            }

            @Override
            public boolean test(ItemStack stack) {
                return stack.getItem() instanceof TridentItem;
            }
        });
        add(new ResourceLocation(Fate.MODID, "bow"), new ItemAI() {
            @Override
            public boolean attack(Mob entity, LivingEntity target, InteractionHand hand) {
                ItemStack stack = entity.getItemInHand(hand);
                ItemStack itemstack = entity.getProjectile(entity.getItemInHand(ProjectileUtil.getWeaponHoldingHand(entity, Items.BOW)));
                AbstractArrow abstractarrowentity = ProjectileUtil.getMobArrow(entity, itemstack, BowItem.getPowerForTime(entity.getTicksUsingItem()));
                if (entity.getMainHandItem().getItem() instanceof BowItem)
                    abstractarrowentity = Platform.INSTANCE.customBowArrow((BowItem) entity.getMainHandItem().getItem(), abstractarrowentity);
                double dX = target.getX() - entity.getX();
                double dY = target.getY(0.3) - abstractarrowentity.getY();
                double dZ = target.getZ() - entity.getZ();
                double l = Math.sqrt(dX * dX + dZ * dZ);
                abstractarrowentity.setCritArrow(true);
                int j;
                if ((j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack)) > 0) {
                    abstractarrowentity.setBaseDamage(abstractarrowentity.getBaseDamage() + (double) j * 0.5 + 0.5);
                }
                if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
                    abstractarrowentity.setSecondsOnFire(100);
                }
                abstractarrowentity.shoot(dX, dY + l * 0.13, dZ, 2.2F, (8 - entity.level.getDifficulty().getId() * 2));
                abstractarrowentity.setBaseDamage(abstractarrowentity.getBaseDamage() + entity.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.5);
                abstractarrowentity.setKnockback(0);
                entity.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (entity.getRandom().nextFloat() * 0.4F + 0.8F));
                entity.level.addFreshEntity(abstractarrowentity);
                return false;
            }

            @Override
            public boolean test(ItemStack stack) {
                return stack.getItem() instanceof BowItem;
            }
        });
        add(new ResourceLocation(Fate.MODID, "crossbow"), new ItemAI() {
            @Override
            public boolean attack(Mob entity, LivingEntity target, InteractionHand hand) {
                ItemStack stack = entity.getItemInHand(hand);
                float vel = CrossbowItem.containsChargedProjectile(stack, Items.FIREWORK_ROCKET) ? 1.6F : 3.4F;
                CrossbowItem.performShooting(entity.level, entity, hand, stack, vel, 1);
                CrossbowItem.setCharged(stack, false);
                return false;
            }

            @Override
            public boolean test(ItemStack stack) {
                return stack.getItem() instanceof CrossbowItem;
            }
        });
    }

    public static synchronized void add(ResourceLocation id, ItemAI ai) {
        add(id, ORDERED_RESOURCES.size(), ai);
    }

    public static synchronized void add(ResourceLocation id, int order, ItemAI ai) {
        ORDERED_RESOURCES.add(new OrderedResource(order, id));
        ORDERED_RESOURCES.sort(Collections.reverseOrder());
        ITEM_AI_MAP.put(id, ai);
    }

    @Nullable
    public static ItemAI getFor(ItemStack stack) {
        for (OrderedResource r : ORDERED_RESOURCES) {
            ItemAI v = ITEM_AI_MAP.get(r.res);
            if (v.test(stack))
                return v;
        }
        return null;
    }

    public interface ItemAI extends Predicate<ItemStack> {

        boolean attack(Mob entity, LivingEntity target, InteractionHand hand);

    }

    record OrderedResource(int order, ResourceLocation res) implements Comparable<OrderedResource> {

        @Override
        public int compareTo(@NotNull OrderedResource o) {
            return this.order == o.order ? this.res.compareTo(o.res) : Integer.compare(this.order, o.order);
        }
    }
}
