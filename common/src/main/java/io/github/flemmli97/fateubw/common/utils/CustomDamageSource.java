package io.github.flemmli97.fateubw.common.utils;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.minions.GordiusWheel;
import io.github.flemmli97.fateubw.common.entity.minions.Pegasus;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomDamageSource {

    public static DamageSource excalibur(Entity source, Entity sourceEntity) {
        return Platform.INSTANCE.setBypassArmor(new IndirectEntityDamageSource(Fate.MODID + ".excalibur", source, sourceEntity)).setMagic();
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
        return new IndirectEntityDamageSource(Fate.MODID + ".magic_beam", source, sourceEntity);
    }

    public static DamageSource magicShot(Entity source, Entity sourceEntity) {
        return new IndirectEntityDamageSource(Fate.MODID + ".magic_shot", source, sourceEntity);
    }

    public static DamageSource hiKen(LivingEntity sourceEntity) {
        return Platform.INSTANCE.setBypassArmor(new EntityDamageSource(Fate.MODID + ".tsubame", sourceEntity));
    }

    public static DamageSource gordiusTrample(GordiusWheel source) {
        return new EntityDamageSource(Fate.MODID + ".gordius", source);
    }

    public static DamageSource pegasusCharge(Pegasus source) {
        return new EntityDamageSource(Fate.MODID + ".pegasus", source).setMagic();
    }

    public static Map<String, String> defaultTranslations() {
        Map<String, String> translations = new LinkedHashMap<>();
        add(translations, "excalibur", new Translation(Type.DEFAULT, "%1$s was vaporized by %2$s with excalibur"),
                new Translation(Type.ITEM, "%1$s was vaporized by %2$s using %3$s"));
        add(translations, "babylon",
                new Translation(Type.DEFAULT, "%1$s was impaled by %2$s with the gate of babylon"),
                new Translation(Type.ITEM, "%1$s was impaled by %2$s with the gate of babylon"));
        add(translations, "thrown_item",
                new Translation(Type.DEFAULT, "%1$s was killed by %2$s"),
                new Translation(Type.ITEM, "%1$s was killed by %2$s using %3$s"));
        add(translations, "gae_bolg",
                new Translation(Type.DEFAULT, "%1$s's heart was pierced with gae bolg"),
                new Translation(Type.ITEM, "%1$s's heart was pierced with gae bolg"));
        add(translations, "caladbolg",
                new Translation(Type.DEFAULT, "%1$s died from a powerful... arrow?"),
                new Translation(Type.ITEM, "%1$s died from a powerful... arrow?"));
        add(translations, "arrow",
                new Translation(Type.DEFAULT, "%1$s was shot by %2$s"),
                new Translation(Type.ITEM, "%1$s was shot by %2$s using %3$s"));
        add(translations, "magic_beam",
                new Translation(Type.DEFAULT, "%1$s was killed by magic from %2$s"),
                new Translation(Type.ITEM, "%1$s was killed by magic from %2$s using %3$s"));
        add(translations, "magic_shot",
                new Translation(Type.DEFAULT, "%1$s was killed by magic from %2$s"),
                new Translation(Type.ITEM, "%1$s was killed by magic from %2$s using %3$s"));
        add(translations, "tsubame",
                new Translation(Type.DEFAULT, "%1$s fell victim to the mighty swallow"),
                new Translation(Type.ITEM, "%1$s fell victim to the mighty swallow"));
        add(translations, "gordius",
                new Translation(Type.DEFAULT, "%1$s was trampled by the divine bulls"),
                new Translation(Type.ITEM, "%1$s was trampled by the divine bulls"));
        add(translations, "pegasus",
                new Translation(Type.DEFAULT, "%1$s thought they could take on pegasus charge"),
                new Translation(Type.ITEM, "%1$s thought they could take on pegasus charge"));
        return translations;
    }

    private static void add(Map<String, String> translationMap, String key, Translation... translations) {
        for (Translation translation : translations) {
            String suffix = switch (translation.type) {
                case DEFAULT -> "";
                case PLAYER -> "player.";
                case ITEM -> "item.";
            };
            translationMap.put("death.attack." + suffix + Fate.MODID + "." + key, translation.translation);
        }
    }

    private record Translation(Type type, String translation) {
    }

    private enum Type {
        DEFAULT,
        PLAYER,
        ITEM
    }
}
