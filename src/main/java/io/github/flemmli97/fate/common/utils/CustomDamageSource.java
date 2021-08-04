package io.github.flemmli97.fate.common.utils;

import io.github.flemmli97.fate.common.entity.minions.EntityGordius;
import io.github.flemmli97.fate.common.entity.minions.EntityPegasus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IndirectEntityDamageSource;

public class CustomDamageSource {

    public static DamageSource excalibur(Entity source, LivingEntity sourceEntity) {
        return (new IndirectEntityDamageSource("excalibur", source, sourceEntity)).setDamageBypassesArmor().setMagicDamage();
    }

    public static DamageSource babylon(Entity source, LivingEntity sourceEntity) {
        return (new IndirectEntityDamageSource("babylon", source, sourceEntity).setProjectile());
    }

    public static DamageSource gaeBolg(Entity source, LivingEntity sourceEntity) {
        return (new IndirectEntityDamageSource("gaeBolg", source, sourceEntity)).setDamageBypassesArmor().setMagicDamage();
    }

    public static DamageSource caladBolg(Entity source, LivingEntity sourceEntity) {
        return (new IndirectEntityDamageSource("caladBolg", source, sourceEntity)).setDamageBypassesArmor().setMagicDamage();
    }

    public static DamageSource archerNormal(Entity source, Entity sourceEntity) {
        return (new IndirectEntityDamageSource("arrow", source, sourceEntity)).setProjectile();
    }

    public static DamageSource magicBeam(Entity source, LivingEntity sourceEntity) {
        return (new IndirectEntityDamageSource("beam", source, sourceEntity));
    }

    public static DamageSource hiKen(LivingEntity sourceEntity) {
        return (new EntityDamageSource("tsubame", sourceEntity)).setDamageBypassesArmor();
    }

    public static DamageSource gordiusTrample(EntityGordius source, LivingEntity sourceEntity) {
        return (new EntityDamageSource("gordius", source));
    }

    public static DamageSource pegasusCharge(EntityPegasus source, LivingEntity sourceEntity) {
        return (new EntityDamageSource("pegasus", source).setMagicDamage());
    }
}
