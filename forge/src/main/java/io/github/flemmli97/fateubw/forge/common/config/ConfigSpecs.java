package io.github.flemmli97.fateubw.forge.common.config;

import io.github.flemmli97.fateubw.common.config.ClientConfig;
import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.config.PotionEffectsConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Predicate;

public class ConfigSpecs {

    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Client CLIENT_CONFIG;

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON_CONFIG;

    public static class Client {

        public final ForgeConfigSpec.IntValue manaBarX;
        public final ForgeConfigSpec.IntValue manaBarY;

        public Client(ForgeConfigSpec.Builder builder) {
            this.manaBarX = builder.comment("X Position of the mana bar").defineInRange("Mana X", ClientConfig.manaX, 0, Integer.MAX_VALUE);
            this.manaBarY = builder.comment("Y Position of the mana bar").defineInRange("Mana Y", ClientConfig.manaY, 0, Integer.MAX_VALUE);
        }
    }

    public static class Common {

        //General
        public final ForgeConfigSpec.IntValue minPlayer;
        public final ForgeConfigSpec.IntValue maxPlayer;
        public final ForgeConfigSpec.IntValue joinTime;
        public final ForgeConfigSpec.IntValue grailWarCooldown;
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
        public ForgeConfigSpec.DoubleValue lancelotReflectChance;
        public ForgeConfigSpec.IntValue hassanCopies;

        //Minions
        public final ForgeConfigSpec.IntValue gillesMinionDuration;
        public final ForgeConfigSpec.IntValue gillesMinionAmount;
        public final ForgeConfigSpec.DoubleValue babylonScale;
        public final ForgeConfigSpec.ConfigValue<List<String>> babylonWeaponsBlacklist;
        public final ForgeConfigSpec.BooleanValue babylonWeaponsWhitelist;
        public final ForgeConfigSpec.DoubleValue eaDamage;
        public final ForgeConfigSpec.DoubleValue excaliburDamage;
        public final ForgeConfigSpec.DoubleValue caladBolgDmg;
        public final ForgeConfigSpec.DoubleValue magicBeam;
        public final ForgeConfigSpec.DoubleValue gaeBolgDmg;
        public final ForgeConfigSpec.ConfigValue<List<String>> gaeBolgEffect;
        public final ForgeConfigSpec.IntValue medeaCircleSpan;
        public final ForgeConfigSpec.DoubleValue medeaCircleRange;

        public final ForgeConfigSpec.IntValue excaliburMana;
        public final ForgeConfigSpec.IntValue eaMana;
        public final ForgeConfigSpec.IntValue archerBowMana;
        public final ForgeConfigSpec.IntValue caladbolgMana;
        public final ForgeConfigSpec.IntValue gaeBolgMana;
        public final ForgeConfigSpec.IntValue grimoireMana;
        public final ForgeConfigSpec.IntValue chainMana;
        public final ForgeConfigSpec.IntValue daggerThrowMana;
        public final ForgeConfigSpec.IntValue staffMana;

        public final ForgeConfigSpec.BooleanValue debugAttack;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("general");
            this.minPlayer = builder.comment("Minimum of player count required to start a grail war").defineInRange("Min Player", CommonConfig.minPlayer, 1, Integer.MAX_VALUE);
            this.maxPlayer = builder.comment("Maximum of player allowed in a grail war").defineInRange("Max Player", CommonConfig.maxPlayer, 1, Integer.MAX_VALUE);
            this.grailWarCooldown = builder.comment("Time in minecraft days till the next grailwar after one has ended").defineInRange("Grailwar Cooldown", CommonConfig.grailWarCooldown, 0, Integer.MAX_VALUE);
            this.joinTime = builder.comment("Time buffer in ticks to join a grail war after start").defineInRange("Join Time", CommonConfig.joinTime, 0, Integer.MAX_VALUE);
            this.allowDuplicateServant = builder.comment("Allow the summoning of duplicate servants during a grail war").define("Allow Duplicate Servants", CommonConfig.allowDuplicateClass);
            this.allowDuplicateClass = builder.comment("Allow the summoning of duplicate servant classes during a grail war").define("Allow Duplicate Classes", CommonConfig.allowDuplicateClass);
            this.fillMissingSlots = builder.comment("Fill in missing players till max allowed with npc").define("Fill Empty Slots", CommonConfig.fillMissingSlots);
            this.maxServantCircle = builder.comment("Amount of masterless servant that can spawn each time. (Fill Empty Slots needs to be true)").defineInRange("Servant Amount", CommonConfig.maxServantCircle, 1, Integer.MAX_VALUE);
            this.servantMinSpawnDelay = builder.comment("Minimum time between each attempt to spawn masterless servants. (Fill Empty Slots needs to be true)").defineInRange("Servant Spawn Delay (Min)", CommonConfig.servantMinSpawnDelay, 0, Integer.MAX_VALUE);
            this.servantMaxSpawnDelay = builder.comment("Maximum time between each attempt to spawn masterless servants. (Fill Empty Slots needs to be true)").defineInRange("Servant Spawn Delay (Max)", CommonConfig.servantMaxSpawnDelay, 0, Integer.MAX_VALUE);

            this.punishTeleport = builder.comment("Should teleporting servants to the owner during a fight be punished").define("Punish Teleport", CommonConfig.punishTeleport);
            this.notifyBlackList = builder.comment("Servants that dont notify players when spawned (from filling missing slots)").define("Servant notification", CommonConfig.notifyBlacklist);
            this.whiteList = builder.comment("Turn servant notification list into a whitelist").define("Notify Whitelist", CommonConfig.notificationWhitelist);
            this.notifyAll = builder.comment("Notify everyone if a servant spawns. Else only the player the servant spawned on will be notified").define("Notify Everyone", CommonConfig.notifyAll);
            this.npBoostEffect = builder.comment("Potions applied when boostin servants using a command seal. Usage: " + PotionEffectsConfig.usage()).define("NP Effects",
                    CommonConfig.npBoostEffect.writeToString());
            builder.pop();

            builder.push("servants");
            this.lancelotReflectChance = builder.comment("Chance for lancelot to reflect a blocked projectile").defineInRange("Projectile Reflect Chance", CommonConfig.lancelotReflectChance, 0, 1);
            this.hassanCopies = builder.comment("Amount of copies hassan can call").defineInRange("Hassan Copies", CommonConfig.hassanCopies, 0, Integer.MAX_VALUE);
            builder.pop();

            builder.push("misc");
            this.gillesMinionDuration = builder.comment("Living duration of gilles monster in ticks").defineInRange("Gilles Monster", CommonConfig.gillesMinionDuration, 0, Integer.MAX_VALUE);
            this.gillesMinionAmount = builder.comment("Max amount gilles can have at once").defineInRange("Gilles Monster Max Amount", CommonConfig.gillesMinionAmount, 0, Integer.MAX_VALUE);
            this.babylonScale = builder.comment("Damage scaling for projectiles from the gate of babylon").defineInRange("Babylon Dmg Scale", CommonConfig.babylonScale, 0, Double.MAX_VALUE);
            this.babylonWeaponsBlacklist = builder.comment("Blacklist weapons for the gate of babylon here. You can also use the modid for a whole mod").define("Babylon Blacklist",
                    CommonConfig.babylonWeapons.writeToString(), stringList());
            this.babylonWeaponsWhitelist = builder.comment("Turn the blacklist into a whitelist").define("Babylon Whitelist", CommonConfig.babylonWeapons.isWhiteList());
            this.eaDamage = builder.comment("Damage of EA").defineInRange("EA Dmg", CommonConfig.eaDamage, 0, Double.MAX_VALUE);
            this.excaliburDamage = builder.comment("Damage of excalibur").defineInRange("Excalibur Dmg", CommonConfig.excaliburDamage, 0, Double.MAX_VALUE);
            this.caladBolgDmg = builder.comment("Caladbolg damage").defineInRange("Caladbolg Dmg", CommonConfig.caladBolgDmg, 0, Double.MAX_VALUE);
            this.magicBeam = builder.comment("Damage of medeas magic beams").defineInRange("Magic Beam Dmg", CommonConfig.magicBeam, 0, Double.MAX_VALUE);
            this.gaeBolgDmg = builder.comment("Damage of Gae Bolg").defineInRange("Gae Bolg Dmg", CommonConfig.gaeBolgDmg, 0, Double.MAX_VALUE);
            this.gaeBolgEffect = builder.comment("Potions applied by Gae Bolg. Usage: " + PotionEffectsConfig.usage()).define("Gae Bolg Potions",
                    CommonConfig.gaeBolgEffect.writeToString());
            this.medeaCircleSpan = builder.comment("Time in ticks for medeas magic circle").defineInRange("Magic Circle Duration", CommonConfig.medeaCircleSpan, 0, Integer.MAX_VALUE);
            this.medeaCircleRange = builder.comment("Range of medeas magic circle").defineInRange("Magic Circle Range", CommonConfig.medeaCircleRange, 0, Double.MAX_VALUE);
            builder.pop();

            builder.push("weapons");
            this.excaliburMana = builder.comment("Mana cost for using excalibur").defineInRange("Excalibur Mana", CommonConfig.excaliburMana, 0, Integer.MAX_VALUE);
            this.eaMana = builder.comment("Mana cost for using EA").defineInRange("EA Mana", CommonConfig.eaMana, 0, Integer.MAX_VALUE);
            this.archerBowMana = builder.comment("Mana cost shooting arrows with archers bow").defineInRange("Archer Arrow Cost", CommonConfig.archerBowMana, 0, Integer.MAX_VALUE);
            this.caladbolgMana = builder.comment("Mana cost charging archers bow with caladbolg").defineInRange("Caladbolg Mana", CommonConfig.caladbolgMana, 0, Integer.MAX_VALUE);
            this.gaeBolgMana = builder.comment("Mana cost for throwing gae bolg").defineInRange("Gae Bolg Mana", CommonConfig.gaeBolgMana, 0, Integer.MAX_VALUE);
            this.grimoireMana = builder.comment("Mana cost for summoning a monster using the spellbook").defineInRange("Monster Summon Mana", CommonConfig.grimoireMana, 0, Integer.MAX_VALUE);
            this.chainMana = builder.comment("Mana cost for throwing the chain dagger").defineInRange("Chain Throw Mana", CommonConfig.chainMana, 0, Integer.MAX_VALUE);
            this.daggerThrowMana = builder.comment("Mana cost for throwing hassans dagger").defineInRange("Dagger Throw Mana", CommonConfig.daggerThrowMana, 0, Integer.MAX_VALUE);
            this.staffMana = builder.comment("Mana cost for using medeas staff").defineInRange("Staff Mana", CommonConfig.staffMana, 0, Integer.MAX_VALUE);
            builder.pop();

            this.debugAttack = builder.comment("Turn on attack bounding box debugging").define("Debug Attack", CommonConfig.debugAttack);
        }
    }

    private static Predicate<Object> stringList() {
        return p -> p instanceof List<?> list && list.stream().allMatch(e -> e instanceof String);
    }

    static {
        Pair<Client, ForgeConfigSpec> specPair1 = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair1.getRight();
        CLIENT_CONFIG = specPair1.getLeft();

        Pair<Common, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair2.getRight();
        COMMON_CONFIG = specPair2.getLeft();
    }
}
