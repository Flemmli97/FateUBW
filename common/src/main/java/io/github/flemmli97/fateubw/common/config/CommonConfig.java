package io.github.flemmli97.fateubw.common.config;

import com.google.common.collect.Lists;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class CommonConfig {

    public static int minPlayer = 1;
    public static int maxPlayer = 7;
    public static int grailWarCooldown = 5;
    public static int joinTime = 2400;
    public static boolean allowDuplicateServant;
    public static boolean allowDuplicateClass;
    public static boolean fillMissingSlots = true;
    public static int maxServantCircle = 1;
    public static int servantMinSpawnDelay = 3000;
    public static int servantMaxSpawnDelay = 6000;

    public static boolean punishTeleport = true;
    public static List<String> notifyBlacklist = Lists.newArrayList(ModEntities.HASSAN.getID().toString());
    public static boolean notificationWhitelist;
    public static boolean notifyAll = true;
    public static PotionEffectsConfig npBoostEffect = new PotionEffectsConfig(
            List.of(new PotionEffectsConfig.EffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 2),
                    new PotionEffectsConfig.EffectInstance(MobEffects.REGENERATION, 6000, 1),
                    new PotionEffectsConfig.EffectInstance(MobEffects.DAMAGE_BOOST, 6000, 2),
                    new PotionEffectsConfig.EffectInstance(MobEffects.MOVEMENT_SPEED, 6000, 2))
    );

    // Misc
    public static float babylonScale = 1.5f;
    public static WeaponList babylonWeapons = new WeaponList(ModItems.ENUMAELISH.getID().toString());
    public static float eaDamage = 50;
    public static float excaliburDamage = 35;
    public static float caladBolgDmg = 30;
    public static float magicBeam = 6;
    public static float gaeBolgDmg = 18;
    public static PotionEffectsConfig gaeBolgEffect = new PotionEffectsConfig(
            List.of(new PotionEffectsConfig.EffectInstance(MobEffects.WITHER, 600, 3),
                    new PotionEffectsConfig.EffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 7),
                    new PotionEffectsConfig.EffectInstance(MobEffects.JUMP, 200, 128))
    );

    public static int excaliburMana = 70;
    public static int eaMana = 70;
    public static int archerBowMana = 3;
    public static int caladbolgMana = 40;
    public static int gaeBolgMana = 15;
    public static int grimoireMana = 30;
    public static int chainMana = 0;
    public static int daggerThrowMana = 5;
    public static int staffMana = 10;

    public static boolean debugAttack = false;
}
