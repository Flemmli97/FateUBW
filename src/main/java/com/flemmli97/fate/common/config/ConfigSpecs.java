package com.flemmli97.fate.common.config;

import com.flemmli97.fate.common.lib.LibEntities;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

public class ConfigSpecs {

    public static final ForgeConfigSpec clientSpec;
    public static final Client clientConf;

    public static final ForgeConfigSpec commonSpec;
    public static final Common commonConf;

    public static class Client {

        public Client(ForgeConfigSpec.Builder builder) {

        }
    }

    public static class Common {

        //General
        public final ForgeConfigSpec.IntValue minPlayer;
        public final ForgeConfigSpec.IntValue maxPlayer;
        public final ForgeConfigSpec.ConfigValue<Integer> joinTime;
        public final ForgeConfigSpec.ConfigValue<Integer> rewardDelay;
        public int charmSpawnRate = 1;
        public int gemSpawnRate = 10;
        public final ForgeConfigSpec.BooleanValue allowDuplicateServant;
        public final ForgeConfigSpec.BooleanValue allowDuplicateClass;
        public final ForgeConfigSpec.BooleanValue fillMissingSlots;
        public final ForgeConfigSpec.ConfigValue<Integer> maxServantCircle;
        public final ForgeConfigSpec.ConfigValue<Integer> servantMinSpawnDelay;
        public final ForgeConfigSpec.ConfigValue<Integer> servantMaxSpawnDelay;
        public boolean punishTeleport = true;
        public final ForgeConfigSpec.ConfigValue<List<String>> notifyWhiteList;
        public final ForgeConfigSpec.BooleanValue blackList;
        public final ForgeConfigSpec.BooleanValue notifyAll;
        //Servants
        public final Map<String, ServantConfSpec> attributes = Maps.newHashMap();
        public ForgeConfigSpec.ConfigValue<Double> lancelotReflectChance;
        //Minions
        public final ForgeConfigSpec.ConfigValue<Integer> gillesMinionDuration;
        public final ForgeConfigSpec.ConfigValue<Integer> gillesMinionAmount;
        public final ForgeConfigSpec.ConfigValue<Double> smallMonsterDamage;
        public final ForgeConfigSpec.ConfigValue<Double> babylonScale;
        public ForgeConfigSpec.ConfigValue<Double> eaDamage;
        public ForgeConfigSpec.ConfigValue<Double> excaliburDamage;
        public ForgeConfigSpec.ConfigValue<Double> caladBolgDmg;
        public ForgeConfigSpec.ConfigValue<Double> magicBeam;
        public ForgeConfigSpec.ConfigValue<Double> gaeBolgDmg;
        public ForgeConfigSpec.ConfigValue<List<String>> gaeBolgEffect;
        public ForgeConfigSpec.ConfigValue<Double> gordiusHealth;
        public ForgeConfigSpec.ConfigValue<Double> gordiusDmg;
        public ForgeConfigSpec.ConfigValue<Double> pegasusHealth;
        public ForgeConfigSpec.ConfigValue<Integer> medeaCircleSpan;
        public ForgeConfigSpec.ConfigValue<Double> medeaCircleRange;

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
            servantMinSpawnDelay = builder.comment("Minimum time between each attempt to spawn masterless servants. (Fill Empty Slots needs to be true)").define("Servant Spawn Delay (Min)", 3000);
            servantMaxSpawnDelay = builder.comment("Maximum time between each attempt to spawn masterless servants. (Fill Empty Slots needs to be true)").define("Servant Spawn Delay (Max)", 6000);
            //punishTeleport
            notifyWhiteList = builder.comment("Servants that dont notify players when spawned (from filling missing slots)").define("Servant notification", Lists.newArrayList(LibEntities.hassan.toString()));
            blackList = builder.comment("Turn servant notification list into a blacklist").define("Notify Blacklist", true);
            notifyAll = builder.comment("Notify everyone if a servant spawns. Else only the player the servant spawned on will be notified").define("Notify Everyone", true);
            builder.pop();

            builder.push("servants");
            attributes.clear();
            for (Map.Entry<String, ServantProperties> e : Config.Common.attributes.entrySet()) {
                builder.push(e.getKey());
                attributes.put(e.getKey(), new ServantConfSpec(builder, e.getValue()));
                if (e.getKey().equals(LibEntities.lancelot.toString()))
                    lancelotReflectChance = builder.comment("Chance for lancelot to reflect a blocked projectile").define("Projectile Reflect Chance", 0.3);
                builder.pop();
            }
            builder.pop();

            builder.push("minions");
            gillesMinionDuration = builder.comment("Living duration of gilles monster in ticks").define("Gilles Monster", 6000);
            gillesMinionAmount = builder.comment("Max amount gilles can have at once").define("Gilles Monster Max Amount", 6);
            smallMonsterDamage = builder.comment("Damage by gilles small monsters").define("Small Monster Damage", 14D);
            babylonScale = builder.comment("Damage scaling for projectiles from the gate of babylon").define("Babylon Dmg Scale", 1.5);
            eaDamage = builder.comment("Damage of EA").define("EA Dmg", 13D);
            excaliburDamage = builder.comment("Damage of excalibur").define("Excalibur Dmg", 19D);
            caladBolgDmg = builder.comment("Caladbolg damage").define("Caladbolg Dmg", 18D);
            magicBeam = builder.comment("Damage of medeas magic beams").define("Magic Beam Dmg", 5D);
            gaeBolgDmg = builder.comment("Damage of Gae Bolg").define("Gae Bolg Dmg", 10D);
            gaeBolgEffect = builder.comment("Potions applied by Gae Bolg").define("Gae Bolg Potions",
                    Lists.newArrayList("minecraft:wither,200,2", "minecraft:slowness,100,7", "minecraft:jump_boost,100,128"));
            gordiusHealth = builder.comment("Health of the Gordius Wheels").define("Gordius Health", 53D);
            gordiusDmg = builder.comment("Dmg of the Gordius Wheel during charging").define("Gordius Dmg", 10D);
            pegasusHealth = builder.comment("Health of Pegasus").define("Pegasus Health", 50D);
            medeaCircleSpan = builder.comment("Time in ticks for medeas magic circle").define("Magic Circle Duration", 12000);
            medeaCircleRange = builder.comment("Range of medeas magic circle").define("Magic Circle Range", 32D);
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
