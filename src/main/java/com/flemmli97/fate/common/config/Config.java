package com.flemmli97.fate.common.config;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class Config {

    public static class Client {

    }

    public static class Common {
        public static int minPlayer;
        public static int maxPlayer;
        public static int joinTime;
        public static int rewardDelay;
        public static int charmSpawnRate;
        public static int gemSpawnRate;
        public static boolean allowDuplicateServant;
        public static boolean allowDuplicateClass;
        public static boolean fillMissingSlots;
        public static int maxServantCircle;
        public static int servantMinSpawnDelay;
        public static int servantMaxSpawnDelay;
        public static boolean punishTeleport;
        public static List<String> notifyBlackList;
        public static boolean whiteList;
        public static boolean notifyAll;
        public static final PotionEffectsConfig npBoostEffect = new PotionEffectsConfig();

        public static final Map<String, ServantProperties> attributes = Maps.newHashMap();
        public static float lancelotReflectChance;
        //Minions
        public static int gillesMinionDuration;
        public static int gillesMinionAmount;
        public static float smallMonsterDamage;
        public static float babylonScale;
        public static float eaDamage;
        public static float excaliburDamage;
        public static float caladBolgDmg;
        public static float magicBeam;
        public static float gaeBolgDmg;
        public static final PotionEffectsConfig gaeBolgEffect = new PotionEffectsConfig();
        public static double gordiusHealth;
        public static float gordiusDmg;
        public static double pegasusHealth;
        public static int medeaCircleSpan;
        public static double medeaCircleRange;

        public static void load() {
            minPlayer = ConfigSpecs.commonConf.minPlayer.get();
            maxPlayer = ConfigSpecs.commonConf.maxPlayer.get();
            joinTime = ConfigSpecs.commonConf.joinTime.get();
            rewardDelay = ConfigSpecs.commonConf.rewardDelay.get();
            charmSpawnRate = ConfigSpecs.commonConf.charmSpawnRate.get();
            gemSpawnRate = ConfigSpecs.commonConf.gemSpawnRate.get();
            allowDuplicateServant = ConfigSpecs.commonConf.allowDuplicateServant.get();
            allowDuplicateClass = ConfigSpecs.commonConf.allowDuplicateClass.get();
            fillMissingSlots = ConfigSpecs.commonConf.fillMissingSlots.get();
            maxServantCircle = ConfigSpecs.commonConf.maxServantCircle.get();
            servantMinSpawnDelay = ConfigSpecs.commonConf.servantMinSpawnDelay.get();
            servantMaxSpawnDelay = ConfigSpecs.commonConf.servantMaxSpawnDelay.get();
            punishTeleport = ConfigSpecs.commonConf.punishTeleport.get();
            notifyBlackList = ConfigSpecs.commonConf.notifyBlackList.get();
            whiteList = ConfigSpecs.commonConf.whiteList.get();
            notifyAll = ConfigSpecs.commonConf.notifyAll.get();
            npBoostEffect.readFromString(ConfigSpecs.commonConf.npBoostEffect.get());

            attributes.clear();
            for (Map.Entry<String, ServantConfSpec> e : ConfigSpecs.commonConf.attributes.entrySet())
                attributes.put(e.getKey(), ServantProperties.read(e.getValue()));
            lancelotReflectChance = ConfigSpecs.commonConf.lancelotReflectChance.get().floatValue();
            //Minions
            gillesMinionDuration = ConfigSpecs.commonConf.gillesMinionDuration.get();
            gillesMinionAmount = ConfigSpecs.commonConf.gillesMinionAmount.get();
            smallMonsterDamage = ConfigSpecs.commonConf.smallMonsterDamage.get().floatValue();
            babylonScale = ConfigSpecs.commonConf.babylonScale.get().floatValue();
            eaDamage = ConfigSpecs.commonConf.eaDamage.get().floatValue();
            excaliburDamage = ConfigSpecs.commonConf.excaliburDamage.get().floatValue();
            caladBolgDmg = ConfigSpecs.commonConf.caladBolgDmg.get().floatValue();
            magicBeam = ConfigSpecs.commonConf.magicBeam.get().floatValue();
            gaeBolgDmg = ConfigSpecs.commonConf.gaeBolgDmg.get().floatValue();
            gaeBolgEffect.readFromString(ConfigSpecs.commonConf.gaeBolgEffect.get());
            gordiusHealth = ConfigSpecs.commonConf.gordiusHealth.get();
            gordiusDmg = ConfigSpecs.commonConf.gordiusDmg.get().floatValue();
            pegasusHealth = ConfigSpecs.commonConf.pegasusHealth.get();
            medeaCircleSpan = ConfigSpecs.commonConf.medeaCircleSpan.get();
            medeaCircleRange = ConfigSpecs.commonConf.medeaCircleRange.get();
        }
    }
}
