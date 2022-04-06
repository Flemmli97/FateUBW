package io.github.flemmli97.fateubw.common.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {

    public static class Client {
        public static int manaX;
        public static int manaY;
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

        public static final Map<String, ServantProperties> attributes = new HashMap<>();
        public static float lancelotReflectChance;
        public static int hassanCopies;

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
        public static float medeaDaggerDamage;
        public static double pegasusHealth;
        public static float pegasusDamage;
        public static int medeaCircleSpan;
        public static float medeaCircleRange;
        public static ServantProperties hassanCopyProps = new ServantProperties(50, 5, 6.5, 0, 12, 2, 0.34, 0);
    }
}
