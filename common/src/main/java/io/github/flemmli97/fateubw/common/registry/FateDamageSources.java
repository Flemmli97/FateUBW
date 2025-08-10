package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.summons.GordiusWheel;
import io.github.flemmli97.fateubw.common.entity.summons.Pegasus;
import io.github.flemmli97.fateubw.common.utils.CustomDamageSource;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class FateDamageSources {

    public static final DamageSource GRAIL_DAMAGE = new CustomDamageSource.GrailDamageSource();

    private static class GrailDamageSource extends DamageSource {

        private GrailDamageSource() {
            super(Fate.MODID + ".grail");
            this.bypassArmor();
            this.bypassInvul();
        }
    }

    public static DamageSource excalibur(Entity source, Entity sourceEntity) {
        return Platform.INSTANCE.setBypassArmor(new IndirectEntityDamageSource(Fate.MODID + ".excalibur", source, sourceEntity)).setMagic();
    }

    public static DamageSource ea(Entity source, Entity sourceEntity) {
        return Platform.INSTANCE.setBypassArmor(new IndirectEntityDamageSource(Fate.MODID + ".ea", source, sourceEntity)).setMagic();
    }

    public static DamageSource babylon(Entity source, Entity sourceEntity) {
        return new IndirectEntityDamageSource(Fate.MODID + ".babylon", source, sourceEntity).setProjectile();
    }

    public static DamageSource thrownItem(Entity source, Entity sourceEntity) {
        return new IndirectEntityDamageSource(Fate.MODID + ".thrown_item", source, sourceEntity).setProjectile();
    }

    public static DamageSource gaeBolg(Entity source, Entity sourceEntity) {
        return Platform.INSTANCE.setBypassArmor(new IndirectEntityDamageSource(Fate.MODID + ".gae_bolg", source, sourceEntity)).setMagic();
    }

    public static DamageSource caladBolg(Entity source, Entity sourceEntity) {
        return Platform.INSTANCE.setBypassArmor(new IndirectEntityDamageSource(Fate.MODID + ".caladbolg", source, sourceEntity)).setMagic();
    }

    public static DamageSource archerNormal(Entity source, Entity sourceEntity) {
        return new IndirectEntityDamageSource(Fate.MODID + ".arrow", source, sourceEntity).setProjectile();
    }

    public static DamageSource magicBeam(Entity source, Entity sourceEntity) {
        return new IndirectEntityDamageSource(Fate.MODID + ".magic_beam", source, sourceEntity).setMagic();
    }

    public static DamageSource magicShot(Entity source, Entity sourceEntity) {
        return new IndirectEntityDamageSource(Fate.MODID + ".magic_shot", source, sourceEntity).setMagic();
    }

    public static DamageSource hiKen(LivingEntity sourceEntity) {
        return Platform.INSTANCE.setBypassArmor(new EntityDamageSource(Fate.MODID + ".tsubame", sourceEntity));
    }

    public static DamageSource gaeDearg(LivingEntity sourceEntity) {
        return Platform.INSTANCE.setBypassArmor(new EntityDamageSource(Fate.MODID + ".gae_dearg", sourceEntity));
    }

    public static DamageSource gordiusTrample(GordiusWheel source) {
        return new EntityDamageSource(Fate.MODID + ".gordius", source);
    }

    public static DamageSource pegasusCharge(Pegasus source) {
        return new EntityDamageSource(Fate.MODID + ".pegasus", source).setMagic();
    }
}
