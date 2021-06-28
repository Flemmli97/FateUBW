package io.github.flemmli97.fate.common.config;

import com.google.common.collect.Lists;
import io.github.flemmli97.fate.common.lib.LibEntities;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigSpecs {

    public static final ForgeConfigSpec clientSpec;
    public static final Client clientConf;

    public static final ForgeConfigSpec commonSpec;
    public static final Common commonConf;

    public static class Client {

        public final ForgeConfigSpec.ConfigValue<Integer> manaBarX;
        public final ForgeConfigSpec.ConfigValue<Integer> manaBarY;

        public Client(ForgeConfigSpec.Builder builder) {
            this.manaBarX = builder.comment("X Position of the mana bar").define("Mana X", 2);
            this.manaBarY = builder.comment("Y Position of the mana bar").define("Mana Y", 2);
        }
    }

    public static class Common {

        //General
        public final ForgeConfigSpec.IntValue minPlayer;
        public final ForgeConfigSpec.IntValue maxPlayer;
        public final ForgeConfigSpec.ConfigValue<Integer> joinTime;
        public final ForgeConfigSpec.ConfigValue<Integer> rewardDelay;
        public final ForgeConfigSpec.ConfigValue<Integer> charmSpawnRate;
        public final ForgeConfigSpec.ConfigValue<Integer> gemSpawnRate;
        public final ForgeConfigSpec.BooleanValue allowDuplicateServant;
        public final ForgeConfigSpec.BooleanValue allowDuplicateClass;
        public final ForgeConfigSpec.BooleanValue fillMissingSlots;
        public final ForgeConfigSpec.ConfigValue<Integer> maxServantCircle;
        public final ForgeConfigSpec.ConfigValue<Integer> servantMinSpawnDelay;
        public final ForgeConfigSpec.ConfigValue<Integer> servantMaxSpawnDelay;
        public final ForgeConfigSpec.BooleanValue punishTeleport;
        public final ForgeConfigSpec.ConfigValue<List<String>> notifyBlackList;
        public final ForgeConfigSpec.BooleanValue whiteList;
        public final ForgeConfigSpec.BooleanValue notifyAll;
        public final ForgeConfigSpec.ConfigValue<List<String>> npBoostEffect;

        //Servants
        public final Map<String, ServantConfSpec> attributes = new HashMap<>();
        public ForgeConfigSpec.ConfigValue<Double> lancelotReflectChance;
        public ForgeConfigSpec.ConfigValue<Integer> hassanCopies;

        //Minions
        public final ForgeConfigSpec.ConfigValue<Integer> gillesMinionDuration;
        public final ForgeConfigSpec.ConfigValue<Integer> gillesMinionAmount;
        public final ForgeConfigSpec.ConfigValue<Double> smallMonsterDamage;
        public final ForgeConfigSpec.ConfigValue<Double> babylonScale;
        public final ForgeConfigSpec.ConfigValue<Double> eaDamage;
        public final ForgeConfigSpec.ConfigValue<Double> excaliburDamage;
        public final ForgeConfigSpec.ConfigValue<Double> caladBolgDmg;
        public final ForgeConfigSpec.ConfigValue<Double> magicBeam;
        public final ForgeConfigSpec.ConfigValue<Double> gaeBolgDmg;
        public final ForgeConfigSpec.ConfigValue<List<String>> gaeBolgEffect;
        public final ForgeConfigSpec.ConfigValue<Double> gordiusHealth;
        public final ForgeConfigSpec.ConfigValue<Double> gordiusDmg;
        public final ForgeConfigSpec.ConfigValue<Double> pegasusHealth;
        public final ForgeConfigSpec.ConfigValue<Integer> medeaCircleSpan;
        public final ForgeConfigSpec.ConfigValue<Double> medeaCircleRange;
        public final ServantConfSpec hassanCopyProps;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("general");
            this.minPlayer = builder.comment("Minimum of player count required to start a grail war").defineInRange("Min Player", 1, 1, Integer.MAX_VALUE);
            this.maxPlayer = builder.comment("Maximum of player allowed in a grail war").defineInRange("Max Player", 7, 1, Integer.MAX_VALUE);
            this.joinTime = builder.comment("Time buffer in ticks to join a grail war after start").define("Join Time", 12000);
            this.rewardDelay = builder.comment("Delay after an ended grail war for getting the grail").define("Reward Delay", 500);
            this.charmSpawnRate = builder.comment("").define("Charm Gen Rate", 2000);
            this.gemSpawnRate = builder.comment("").define("Gem Gen Rate", 2000);
            this.allowDuplicateServant = builder.comment("Allow the summoning of duplicate servants during a grail war").define("Allow Duplicate Servants", false);
            this.allowDuplicateClass = builder.comment("Allow the summoning of duplicate servant classes during a grail war").define("Allow Duplicate Classes", false);
            this.fillMissingSlots = builder.comment("Fill in missing players till max allowed with npc").define("Fill Empty Slots", true);
            this.maxServantCircle = builder.comment("Amount of masterless servant that can spawn each time. (Fill Empty Slots needs to be true)").define("Servant Amount", 1);
            this.servantMinSpawnDelay = builder.comment("Minimum time between each attempt to spawn masterless servants. (Fill Empty Slots needs to be true)").define("Servant Spawn Delay (Min)", 3000);
            this.servantMaxSpawnDelay = builder.comment("Maximum time between each attempt to spawn masterless servants. (Fill Empty Slots needs to be true)").define("Servant Spawn Delay (Max)", 6000);
            this.punishTeleport = builder.comment("").define("Punish Teleport", true);
            this.notifyBlackList = builder.comment("Servants that dont notify players when spawned (from filling missing slots)").define("Servant notification", Lists.newArrayList(LibEntities.hassan.toString()));
            this.whiteList = builder.comment("Turn servant notification list into a whitelist").define("Notify Whitelist", true);
            this.notifyAll = builder.comment("Notify everyone if a servant spawns. Else only the player the servant spawned on will be notified").define("Notify Everyone", true);
            this.npBoostEffect = builder.comment("Potions applied when boostin servants using a command seal. Usage: " + PotionEffectsConfig.usage()).define("NP Effects",
                    Lists.newArrayList("minecraft:resistance,6000,2", "minecraft:regeneration,6000,1", "minecraft:strength,6000,2", "minecraft:speed,6000,2"));
            builder.pop();

            builder.push("servants");
            this.attributes.clear();
            for (Map.Entry<String, ServantProperties> e : Config.Common.attributes.entrySet()) {
                builder.push(e.getKey());
                this.attributes.put(e.getKey(), new ServantConfSpec(builder, e.getValue()));
                if (e.getKey().equals(LibEntities.lancelot.toString()))
                    this.lancelotReflectChance = builder.comment("Chance for lancelot to reflect a blocked projectile").define("Projectile Reflect Chance", 0.3);
                if (e.getKey().equals(LibEntities.hassan.toString()))
                    this.hassanCopies = builder.comment("Amount of copies hassan can call").define("Hassan Copies", 5);
                builder.pop();
            }
            builder.pop();

            builder.push("minions");
            this.gillesMinionDuration = builder.comment("Living duration of gilles monster in ticks").define("Gilles Monster", 6000);
            this.gillesMinionAmount = builder.comment("Max amount gilles can have at once").define("Gilles Monster Max Amount", 6);
            this.smallMonsterDamage = builder.comment("Damage by gilles small monsters").define("Small Monster Damage", 14D);
            this.babylonScale = builder.comment("Damage scaling for projectiles from the gate of babylon").define("Babylon Dmg Scale", 1.5);
            this.eaDamage = builder.comment("Damage of EA").define("EA Dmg", 13D);
            this.excaliburDamage = builder.comment("Damage of excalibur").define("Excalibur Dmg", 19D);
            this.caladBolgDmg = builder.comment("Caladbolg damage").define("Caladbolg Dmg", 18D);
            this.magicBeam = builder.comment("Damage of medeas magic beams").define("Magic Beam Dmg", 5D);
            this.gaeBolgDmg = builder.comment("Damage of Gae Bolg").define("Gae Bolg Dmg", 10D);
            this.gaeBolgEffect = builder.comment("Potions applied by Gae Bolg. Usage: " + PotionEffectsConfig.usage()).define("Gae Bolg Potions",
                    Lists.newArrayList("minecraft:wither,200,2", "minecraft:slowness,100,7", "minecraft:jump_boost,100,128"));
            this.gordiusHealth = builder.comment("Health of the Gordius Wheels").define("Gordius Health", 53D);
            this.gordiusDmg = builder.comment("Dmg of the Gordius Wheel during charging").define("Gordius Dmg", 10D);
            this.pegasusHealth = builder.comment("Health of Pegasus").define("Pegasus Health", 50D);
            this.medeaCircleSpan = builder.comment("Time in ticks for medeas magic circle").define("Magic Circle Duration", 12000);
            this.medeaCircleRange = builder.comment("Range of medeas magic circle").define("Magic Circle Range", 32D);
            builder.push("hassanCopy");
            this.hassanCopyProps = new ServantConfSpec(builder, Config.Common.hassanCopyProps);
            builder.pop();
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
