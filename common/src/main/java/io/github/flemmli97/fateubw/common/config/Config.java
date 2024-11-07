package io.github.flemmli97.fateubw.common.config;

import com.google.common.collect.Lists;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class Config {

    public static class Client {
        public static int manaX = 2;
        public static int manaY = 2;
    }

    public static class Common {
        public static int minPlayer = 1;
        public static int maxPlayer = 7;
        public static int joinTime = 12000;
        public static int rewardDelay = 500;
        public static int charmSpawnRate = 2000;
        public static int gemSpawnRate = 500;
        public static boolean allowDuplicateServant;
        public static boolean allowDuplicateClass;
        public static boolean fillMissingSlots = true;
        public static int maxServantCircle = 1;
        public static int servantMinSpawnDelay = 3000;
        public static int servantMaxSpawnDelay = 6000;
        public static boolean punishTeleport = true;
        public static List<String> notifyBlackList = Lists.newArrayList(ModEntities.HASSAN.getID().toString());
        public static boolean notificationWhitelist = true;
        public static boolean notifyAll = true;
        public static PotionEffectsConfig npBoostEffect = new PotionEffectsConfig(
                List.of(new PotionEffectsConfig.EffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 2),
                        new PotionEffectsConfig.EffectInstance(MobEffects.REGENERATION, 6000, 1),
                        new PotionEffectsConfig.EffectInstance(MobEffects.DAMAGE_BOOST, 6000, 2),
                        new PotionEffectsConfig.EffectInstance(MobEffects.MOVEMENT_SPEED, 6000, 2))
        );

        public static float lancelotReflectChance = 0.3f;
        public static int hassanCopies = 5;

        //Minions
        public static int gillesMinionDuration = 6000;
        public static int gillesMinionAmount = 6;
        public static float smallMonsterDamage = 14;
        public static float babylonScale = 1.5f;
        public static WeaponList babylonWeapons = new WeaponList(ModItems.ENUMAELISH.getID().toString());
        public static float eaDamage = 21;
        public static float excaliburDamage = 19;
        public static float caladBolgDmg = 18;
        public static float magicBeam = 6;
        public static float gaeBolgDmg = 10;
        public static PotionEffectsConfig gaeBolgEffect = new PotionEffectsConfig(
                List.of(new PotionEffectsConfig.EffectInstance(MobEffects.WITHER, 400, 2),
                        new PotionEffectsConfig.EffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 7),
                        new PotionEffectsConfig.EffectInstance(MobEffects.JUMP, 200, 128))
        );
        public static double gordiusHealth = 53;
        public static float gordiusDmg = 10;
        public static double pegasusHealth = 50;
        public static float pegasusDamage = 14;
        public static int medeaCircleSpan = 12000;
        public static float medeaCircleRange = 32;

        public static boolean debugAttack = false;
    }
}
