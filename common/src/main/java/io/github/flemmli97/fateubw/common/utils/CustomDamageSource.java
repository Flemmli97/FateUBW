package io.github.flemmli97.fateubw.common.utils;

import io.github.flemmli97.fateubw.common.entity.minions.Gordius;
import io.github.flemmli97.fateubw.common.entity.minions.Pegasus;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class CustomDamageSource {

    public static DamageSource excalibur(Entity source, Entity sourceEntity) {
        return Platform.INSTANCE.setBypassArmor(new IndirectEntityDamageSource("excalibur", source, sourceEntity)).setMagic();
    }

    public static DamageSource babylon(Entity source, Entity sourceEntity) {
        return (new IndirectEntityDamageSource("babylon", source, sourceEntity).setProjectile());
    }

    public static DamageSource gaeBolg(Entity source, Entity sourceEntity) {
        return Platform.INSTANCE.setBypassArmor(new IndirectEntityDamageSource("gaeBolg", source, sourceEntity)).setMagic();
    }

    public static DamageSource caladBolg(Entity source, Entity sourceEntity) {
        return Platform.INSTANCE.setBypassArmor(new IndirectEntityDamageSource("caladBolg", source, sourceEntity)).setMagic();
    }

    public static DamageSource archerNormal(Entity source, Entity sourceEntity) {
        return (new IndirectEntityDamageSource("arrow", source, sourceEntity)).setProjectile();
    }

    public static DamageSource magicBeam(Entity source, Entity sourceEntity) {
        return (new IndirectEntityDamageSource("beam", source, sourceEntity));
    }

    public static DamageSource hiKen(LivingEntity sourceEntity) {
        return Platform.INSTANCE.setBypassArmor(new EntityDamageSource("tsubame", sourceEntity));
    }

    public static DamageSource gordiusTrample(Gordius source, LivingEntity sourceEntity) {
        return (new EntityDamageSource("gordius", source));
    }

    public static DamageSource pegasusCharge(Pegasus source, LivingEntity sourceEntity) {
        return (new EntityDamageSource("pegasus", source).setMagic());
    }
}
