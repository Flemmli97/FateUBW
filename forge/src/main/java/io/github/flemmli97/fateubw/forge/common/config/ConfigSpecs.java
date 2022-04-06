package io.github.flemmli97.fateubw.forge.common.config;

import com.google.common.collect.Lists;
import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.config.PotionEffectsConfig;
import io.github.flemmli97.fateubw.common.config.ServantProperties;
import io.github.flemmli97.fateubw.common.lib.LibEntities;
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

        public final ForgeConfigSpec.IntValue manaBarX;
        public final ForgeConfigSpec.IntValue manaBarY;

        public Client(ForgeConfigSpec.Builder builder) {
            this.manaBarX = builder.comment("X Position of the mana bar").defineInRange("Mana X", 2, 0, Integer.MAX_VALUE);
            this.manaBarY = builder.comment("Y Position of the mana bar").defineInRange("Mana Y", 2, 0, Integer.MAX_VALUE);
        }
    }

    public static class Common {

        //General
        public final ForgeConfigSpec.IntValue minPlayer;
        public final ForgeConfigSpec.IntValue maxPlayer;
        public final ForgeConfigSpec.IntValue joinTime;
        public final ForgeConfigSpec.IntValue rewardDelay;
        public final ForgeConfigSpec.IntValue charmSpawnRate;
        public final ForgeConfigSpec.IntValue gemSpawnRate;
        public final ForgeConfigSpec.BooleanValue allowDuplicateServant;
        public final ForgeConfigSpec.BooleanValue allowDuplicateClass;
        public final ForgeConfigSpec.BooleanValue fillMissingSlots;
        public final ForgeConfigSpec.IntValue maxServantCircle;
        public final ForgeConfigSpec.IntValue servantMinSpawnDelay;
        public final ForgeConfigSpec.IntValue servantMaxSpawnDelay;
        public final ForgeConfigSpec.BooleanValue punishTeleport;
        public final ForgeConfigSpec.ConfigValue<List<String>> notifyBlackList;
        public final ForgeConfigSpec.BooleanValue whiteList;
        public final ForgeConfigSpec.BooleanValue notifyAll;
        public final ForgeConfigSpec.ConfigValue<List<String>> npBoostEffect;

        //Servants
        public final Map<String, ServantConfSpec> attributes = new HashMap<>();
        public ForgeConfigSpec.DoubleValue lancelotReflectChance;
        public ForgeConfigSpec.IntValue hassanCopies;

        //Minions
        public final ForgeConfigSpec.IntValue gillesMinionDuration;
        public final ForgeConfigSpec.IntValue gillesMinionAmount;
        public final ForgeConfigSpec.DoubleValue smallMonsterDamage;
        public final ForgeConfigSpec.DoubleValue babylonScale;
        public final ForgeConfigSpec.DoubleValue eaDamage;
        public final ForgeConfigSpec.DoubleValue excaliburDamage;
        public final ForgeConfigSpec.DoubleValue caladBolgDmg;
        public final ForgeConfigSpec.DoubleValue magicBeam;
        public final ForgeConfigSpec.DoubleValue gaeBolgDmg;
        public final ForgeConfigSpec.ConfigValue<List<String>> gaeBolgEffect;
        public final ForgeConfigSpec.DoubleValue gordiusHealth;
        public final ForgeConfigSpec.DoubleValue gordiusDmg;
        public final ForgeConfigSpec.DoubleValue pegasusHealth;
        public final ForgeConfigSpec.IntValue medeaCircleSpan;
        public final ForgeConfigSpec.DoubleValue medeaCircleRange;
        public final ServantConfSpec hassanCopyProps;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("general");
            this.minPlayer = builder.comment("Minimum of player count required to start a grail war").defineInRange("Min Player", 1, 1, Integer.MAX_VALUE);
            this.maxPlayer = builder.comment("Maximum of player allowed in a grail war").defineInRange("Max Player", 7, 1, Integer.MAX_VALUE);
            this.joinTime = builder.comment("Time buffer in ticks to join a grail war after start").defineInRange("Join Time", 12000, 0, Integer.MAX_VALUE);
            this.rewardDelay = builder.comment("Delay after an ended grail war for getting the grail").defineInRange("Reward Delay", 500, 0, Integer.MAX_VALUE);
            this.charmSpawnRate = builder.comment("Legacy. Does Nothing").defineInRange("Charm Gen Rate", 2000, 0, Integer.MAX_VALUE);
            this.gemSpawnRate = builder.comment("Legacy. Does Nothing").defineInRange("Gem Gen Rate", 2000, 0, Integer.MAX_VALUE);
            this.allowDuplicateServant = builder.comment("Allow the summoning of duplicate servants during a grail war").define("Allow Duplicate Servants", false);
            this.allowDuplicateClass = builder.comment("Allow the summoning of duplicate servant classes during a grail war").define("Allow Duplicate Classes", false);
            this.fillMissingSlots = builder.comment("Fill in missing players till max allowed with npc").define("Fill Empty Slots", true);
            this.maxServantCircle = builder.comment("Amount of masterless servant that can spawn each time. (Fill Empty Slots needs to be true)").defineInRange("Servant Amount", 1, 0, Integer.MAX_VALUE);
            this.servantMinSpawnDelay = builder.comment("Minimum time between each attempt to spawn masterless servants. (Fill Empty Slots needs to be true)").defineInRange("Servant Spawn Delay (Min)", 3000, 0, Integer.MAX_VALUE);
            this.servantMaxSpawnDelay = builder.comment("Maximum time between each attempt to spawn masterless servants. (Fill Empty Slots needs to be true)").defineInRange("Servant Spawn Delay (Max)", 6000, 0, Integer.MAX_VALUE);
            this.punishTeleport = builder.comment("Should teleporting servants to the owner during a fight be punished").define("Punish Teleport", true);
            this.notifyBlackList = builder.comment("Servants that dont notify players when spawned (from filling missing slots)").define("Servant notification", Lists.newArrayList(LibEntities.hassan.toString()));
            this.whiteList = builder.comment("Turn servant notification list into a whitelist").define("Notify Whitelist", true);
            this.notifyAll = builder.comment("Notify everyone if a servant spawns. Else only the player the servant spawned on will be notified").define("Notify Everyone", true);
            this.npBoostEffect = builder.comment("Potions applied when boostin servants using a command seal. Usage: " + PotionEffectsConfig.usage()).define("NP Effects",
                    Lists.newArrayList("minecraft:resistance,12000,2", "minecraft:regeneration,12000,1", "minecraft:strength,12000,3", "minecraft:speed,12000,2"));
            builder.pop();

            builder.push("servants");
            this.attributes.clear();
            for (Map.Entry<String, ServantProperties> e : Config.Common.attributes.entrySet()) {
                builder.push(e.getKey());
                this.attributes.put(e.getKey(), new ServantConfSpec(builder, e.getValue()));
                if (e.getKey().equals(LibEntities.lancelot.toString()))
                    this.lancelotReflectChance = builder.comment("Chance for lancelot to reflect a blocked projectile").defineInRange("Projectile Reflect Chance", 0.3, 0, 1);
                if (e.getKey().equals(LibEntities.hassan.toString()))
                    this.hassanCopies = builder.comment("Amount of copies hassan can call").defineInRange("Hassan Copies", 5, 0, Integer.MAX_VALUE);
                builder.pop();
            }
            builder.pop();

            builder.push("minions");
            this.gillesMinionDuration = builder.comment("Living duration of gilles monster in ticks").defineInRange("Gilles Monster", 6000, 0, Integer.MAX_VALUE);
            this.gillesMinionAmount = builder.comment("Max amount gilles can have at once").defineInRange("Gilles Monster Max Amount", 6, 0, Integer.MAX_VALUE);
            this.smallMonsterDamage = builder.comment("Damage by gilles small monsters").defineInRange("Small Monster Damage", 14D, 0, Double.MAX_VALUE);
            this.babylonScale = builder.comment("Damage scaling for projectiles from the gate of babylon").defineInRange("Babylon Dmg Scale", 1.5, 0, Double.MAX_VALUE);
            this.eaDamage = builder.comment("Damage of EA").defineInRange("EA Dmg", 13D, 0, Double.MAX_VALUE);
            this.excaliburDamage = builder.comment("Damage of excalibur").defineInRange("Excalibur Dmg", 19D, 0, Double.MAX_VALUE);
            this.caladBolgDmg = builder.comment("Caladbolg damage").defineInRange("Caladbolg Dmg", 18D, 0, Double.MAX_VALUE);
            this.magicBeam = builder.comment("Damage of medeas magic beams").defineInRange("Magic Beam Dmg", 5D, 0, Double.MAX_VALUE);
            this.gaeBolgDmg = builder.comment("Damage of Gae Bolg").defineInRange("Gae Bolg Dmg", 10D, 0, Double.MAX_VALUE);
            this.gaeBolgEffect = builder.comment("Potions applied by Gae Bolg. Usage: " + PotionEffectsConfig.usage()).define("Gae Bolg Potions",
                    Lists.newArrayList("minecraft:wither,200,2", "minecraft:slowness,100,7", "minecraft:jump_boost,100,128"));
            this.gordiusHealth = builder.comment("Health of the Gordius Wheels").defineInRange("Gordius Health", 53D, 0, Double.MAX_VALUE);
            this.gordiusDmg = builder.comment("Dmg of the Gordius Wheel during charging").defineInRange("Gordius Dmg", 10D, 0, Double.MAX_VALUE);
            this.pegasusHealth = builder.comment("Health of Pegasus").defineInRange("Pegasus Health", 50D, 0, Double.MAX_VALUE);
            this.medeaCircleSpan = builder.comment("Time in ticks for medeas magic circle").defineInRange("Magic Circle Duration", 12000, 0, Integer.MAX_VALUE);
            this.medeaCircleRange = builder.comment("Range of medeas magic circle").defineInRange("Magic Circle Range", 32D, 0, Double.MAX_VALUE);
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
