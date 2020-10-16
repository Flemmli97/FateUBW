package com.flemmli97.fate.common.config;

import com.google.common.collect.Maps;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;
import java.util.Map;

public class Config {

    public static class Client{

    }

    public static class Common{
        public static int minPlayer;
        public static int maxPlayer;
        public static int joinTime;
        public static int rewardDelay;
        public static int charmSpawnRate=1;
        public static int gemSpawnRate=10;
        public static boolean allowDuplicateServant;
        public static boolean allowDuplicateClass;
        public static boolean fillMissingSlots;
        public static int maxServantCircle;
        public static int servantMinSpawnDelay;
        public static int servantMaxSpawnDelay;
        public static boolean punishTeleport=true;
        public static List<String> notifyWhiteList;
        public static boolean blackList;
        public static boolean notifyAll;

        public static final Map<String, ServantProperties> attributes = Maps.newHashMap();

        public static void load(){
            minPlayer = ConfigSpecs.commonConf.minPlayer.get();
        }
    }
}
