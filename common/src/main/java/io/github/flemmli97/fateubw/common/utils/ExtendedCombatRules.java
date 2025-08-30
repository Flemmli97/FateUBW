package io.github.flemmli97.fateubw.common.utils;

import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.registry.FateAttributes;
import io.github.flemmli97.tenshilib.common.utils.math.parser.VariableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class ExtendedCombatRules {

    public static float getDamageAfterArmor(LivingEntity entity, float damage, DamageSource damageSource, double armorValue, double armorToughness) {
        VariableMap map = new VariableMap();
        map.setVariable("damage", damage);
        map.setVariable("armor", armorValue);
        map.setVariable("armor_toughness", armorToughness);
        map.setVariable("effective_armor", CommonConfig.effectiveArmor.get(map));
        float reduction = (float) Mth.clamp(CommonConfig.armorReduction.get(map), 0, 1);
        ItemStack itemStack = damageSource.getWeaponItem();
        if (itemStack != null && entity.level() instanceof ServerLevel serverLevel) {
            reduction = Mth.clamp(EnchantmentHelper.modifyArmorEffectiveness(serverLevel, itemStack, entity, damageSource, reduction), 0, 1);
        }
        return damage * (1 - reduction);
    }

    public static float projectileReduce(LivingEntity entity, float damage) {
        AttributeInstance inst = entity.getAttribute(FateAttributes.PROJECTILE_RESISTANCE.asHolder());
        if (inst == null)
            return damage;
        VariableMap map = new VariableMap();
        map.setVariable("damage", damage);
        map.setVariable("projectile_protection", inst.getValue());
        float reduction = (float) Mth.clamp(CommonConfig.projectileReduction.get(map), 0, 1);
        return damage * (1 - reduction);
    }

    public static float getDamageAfterMagicProtection(LivingEntity entity, float damage) {
        AttributeInstance inst = entity.getAttribute(FateAttributes.MAGIC_RESISTANCE.asHolder());
        if (inst == null)
            return damage;
        VariableMap map = new VariableMap();
        map.setVariable("damage", damage);
        map.setVariable("magic_protection", inst.getValue());
        float reduction = (float) Mth.clamp(CommonConfig.magicReduction.get(map), 0, 1);
        return damage * (1 - reduction);
    }
}
