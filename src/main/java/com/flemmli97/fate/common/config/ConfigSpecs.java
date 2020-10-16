package com.flemmli97.fate.common.config;

import com.flemmli97.fate.common.lib.LibEntities;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import sun.invoke.empty.Empty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigSpecs {

    public static final ForgeConfigSpec clientSpec;
    public static final Client clientConf;

    public static final ForgeConfigSpec commonSpec;
    public static final Common commonConf;

    public static class Client{

        public Client(ForgeConfigSpec.Builder builder) {

        }
    }

    public static class Common{

        //General
        public final ForgeConfigSpec.IntValue minPlayer;
        public final ForgeConfigSpec.IntValue maxPlayer;
        public final ForgeConfigSpec.ConfigValue<Integer> joinTime;
        public final ForgeConfigSpec.ConfigValue<Integer> rewardDelay;
        public int charmSpawnRate=1;
        public int gemSpawnRate=10;
        public final ForgeConfigSpec.BooleanValue allowDuplicateServant;
        public final ForgeConfigSpec.BooleanValue allowDuplicateClass;
        public final ForgeConfigSpec.BooleanValue fillMissingSlots;
        public final ForgeConfigSpec.ConfigValue<Integer> maxServantCircle;
        public final ForgeConfigSpec.ConfigValue<Integer> servantMinSpawnDelay;
        public final ForgeConfigSpec.ConfigValue<Integer> servantMaxSpawnDelay;
        public boolean punishTeleport=true;
        public final ForgeConfigSpec.ConfigValue<List<String>> notifyWhiteList;
        public final ForgeConfigSpec.BooleanValue blackList;
        public final ForgeConfigSpec.BooleanValue notifyAll;
        //Servants
        public final Map<String, ServantConfSpec> attributes = Maps.newHashMap();
        public float lancelotReflectChance = 0.3f;
        //Minions
        public int gillesMinionDuration=6000;
        public int gillesMinionAmount=6;
        public float smallMonsterDamage=14;
        public ServantProperties hassanCopy = new ServantProperties(50, 4, 3.5, 0, 12, 2, 0.34, 0, 10, 5);
        public float babylonScale = 1.5f;
        public float eaDamage=13;
        public float excaliburDamage=19;
        public float caladBolgDmg = 18;
        public float magicBeam = 5;
        public float gaeBolgDmg = 10;
        public PotionEffectsConfig gaeBolgEffect = new PotionEffectsConfig()
                .addEffect(Potion.getPotionFromResourceLocation("minecraft:wither"), 200, 2)
                .addEffect(Potion.getPotionFromResourceLocation("minecraft:slowness"), 100, 7)
                .addEffect(Potion.getPotionFromResourceLocation("minecraft:jump_boost"), 100, 128);
        public float gordiusHealth = 53;
        public float gordiusDmg = 10;
        public float pegasusHealth = 53;
        public int medeaCircleSpan = 12000;
        public float medeaCircleRange=32;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("general");
            minPlayer = builder.comment("Minimum of player count required to start a grail war").defineInRange("Min Player", 1, 1, Integer.MAX_VALUE);
            maxPlayer = builder.comment("Maximum of player allowed in a grail war").defineInRange("Max Player", 1, 1, Integer.MAX_VALUE);
            joinTime = builder.comment("Time buffer in ticks to join a grail war after start").define("Join Time", 12000);
            rewardDelay = builder.comment("Delay after an ended grail war for getting the grail").define("Reward Delay", 2000);
            //public int charmSpawnRate=1;
            //public int gemSpawnRate=10;
            allowDuplicateServant = builder.comment("Allow the summoning of duplicate servants during a grail war").define("Allow Duplicate Servants", false);
            allowDuplicateClass = builder.comment("Allow the summoning of duplicate servant classes during a grail war").define("Allow Duplicate Classes", false);
            fillMissingSlots = builder.comment("Fill in missing players till max allowed with npc").define("Fill Empty Slots", true);
            maxServantCircle = builder.comment("Amount of masterless servant that can spawn each time. (Fill Empty Slots needs to be true)").define("Servant Amount", 1);
            servantMinSpawnDelay = builder.comment("Minimum time between each attempt to spawn masterless servants. (Fill Empty Slots needs to be true)").define("Servant Spawn Delay (Min)", 3000)
            servantMaxSpawnDelay = builder.comment("Maximum time between each attempt to spawn masterless servants. (Fill Empty Slots needs to be true)").define("Servant Spawn Delay (Max)", 6000);
            //punishTeleport
            notifyWhiteList = builder.comment("Servants that dont notify players when spawned (from filling missing slots)").define("Servant notification", Lists.newArrayList(LibEntities.hassan.toString()));
            blackList = builder.comment("Turn servant notification list into a blacklist").define("Notify Blacklist", true);
            notifyAll = builder.comment("Notify everyone if a servant spawns. Else only the player the servant spawned on will be notified").define("Notify Everyone", true);
            builder.pop();
        }
    }

    static {
        Pair<Client, ForgeConfigSpec> specPair1 = new ForgeConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair1.getRight();
        clientConf = specPair1.getLeft();

        Pair<Common, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = specPair2.getRight();
        commonConf = specPair2.getLeft();
    }
}
