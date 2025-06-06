package io.github.flemmli97.fateubw.fabric.common.config;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.config.PotionEffectsConfig;
import io.github.flemmli97.tenshilib.common.config.CommentedJsonConfig;
import io.github.flemmli97.tenshilib.common.config.JsonConfig;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ConfigSpecs {

    public static JsonConfig<CommentedJsonConfig> CLIENT_SPEC;
    public static Client CLIENT_CONFIG;

    public static JsonConfig<CommentedJsonConfig> COMMON_SPEC;
    public static Common COMMON_CONFIG;

    public static void initClientConfig() {
        Pair<JsonConfig<CommentedJsonConfig>, Client> pair = CommentedJsonConfig.Builder
                .create(FabricLoader.getInstance().getConfigDir().resolve(Fate.MODID).resolve("client.json"), 1, Client::new);
        CLIENT_SPEC = pair.getKey();
        CLIENT_CONFIG = pair.getValue();
    }

    public static void initCommonConfig() {
        Pair<JsonConfig<CommentedJsonConfig>, Common> pair = CommentedJsonConfig.Builder
                .create(FabricLoader.getInstance().getConfigDir().resolve(Fate.MODID).resolve("common.json"), 1, Common::new);
        COMMON_SPEC = pair.getKey();
        COMMON_CONFIG = pair.getValue();
    }

    public static class Client {

        public final CommentedJsonConfig.IntVal manaBarX;
        public final CommentedJsonConfig.IntVal manaBarY;

        public Client(CommentedJsonConfig.Builder builder) {
            this.manaBarX = builder.comment("X Position of the mana bar").defineInRange("Mana X", Config.Client.manaX, 0, Integer.MAX_VALUE);
            this.manaBarY = builder.comment("Y Position of the mana bar").defineInRange("Mana Y", Config.Client.manaY, 0, Integer.MAX_VALUE);
            builder.registerReloadHandler(ConfigLoader::loadClient);
        }
    }

    public static class Common {

        //General
        public final CommentedJsonConfig.IntVal minPlayer;
        public final CommentedJsonConfig.IntVal maxPlayer;
        public final CommentedJsonConfig.IntVal joinTime;
        public final CommentedJsonConfig.IntVal grailWarCooldown;
        public final CommentedJsonConfig.CommentedVal<Boolean> allowDuplicateServant;
        public final CommentedJsonConfig.CommentedVal<Boolean> allowDuplicateClass;
        public final CommentedJsonConfig.CommentedVal<Boolean> fillMissingSlots;
        public final CommentedJsonConfig.IntVal maxServantCircle;
        public final CommentedJsonConfig.IntVal servantMinSpawnDelay;
        public final CommentedJsonConfig.IntVal servantMaxSpawnDelay;

        public final CommentedJsonConfig.CommentedVal<Boolean> punishTeleport;
        public final CommentedJsonConfig.CommentedVal<List<String>> notifyBlackList;
        public final CommentedJsonConfig.CommentedVal<Boolean> whiteList;
        public final CommentedJsonConfig.CommentedVal<Boolean> notifyAll;
        public final CommentedJsonConfig.CommentedVal<List<String>> npBoostEffect;

        //Servants
        public CommentedJsonConfig.DoubleVal lancelotReflectChance;
        public CommentedJsonConfig.IntVal hassanCopies;

        //Minions
        public final CommentedJsonConfig.IntVal gillesMinionDuration;
        public final CommentedJsonConfig.IntVal gillesMinionAmount;
        public final CommentedJsonConfig.DoubleVal babylonScale;
        public final CommentedJsonConfig.CommentedVal<List<String>> babylonWeaponsBlacklist;
        public final CommentedJsonConfig.CommentedVal<Boolean> babylonWeaponsWhitelist;
        public final CommentedJsonConfig.DoubleVal eaDamage;
        public final CommentedJsonConfig.DoubleVal excaliburDamage;
        public final CommentedJsonConfig.DoubleVal caladBolgDmg;
        public final CommentedJsonConfig.DoubleVal magicBeam;
        public final CommentedJsonConfig.DoubleVal gaeBolgDmg;
        public final CommentedJsonConfig.CommentedVal<List<String>> gaeBolgEffect;
        public final CommentedJsonConfig.IntVal medeaCircleSpan;
        public final CommentedJsonConfig.DoubleVal medeaCircleRange;

        public final CommentedJsonConfig.IntVal excaliburMana;
        public final CommentedJsonConfig.IntVal eaMana;
        public final CommentedJsonConfig.IntVal archerBowMana;
        public final CommentedJsonConfig.IntVal caladbolgMana;
        public final CommentedJsonConfig.IntVal gaeBolgMana;
        public final CommentedJsonConfig.IntVal grimoireMana;
        public final CommentedJsonConfig.IntVal chainMana;
        public final CommentedJsonConfig.IntVal daggerThrowMana;
        public final CommentedJsonConfig.IntVal staffMana;

        public final CommentedJsonConfig.CommentedVal<Boolean> debugAttack;

        public Common(CommentedJsonConfig.Builder builder) {
            builder.push("general");
            this.minPlayer = builder.comment("Minimum of player count required to start a grail war").defineInRange("Min Player", Config.Common.minPlayer, 1, Integer.MAX_VALUE);
            this.maxPlayer = builder.comment("Maximum of player allowed in a grail war").defineInRange("Max Player", Config.Common.maxPlayer, 1, Integer.MAX_VALUE);
            this.grailWarCooldown = builder.comment("Time in minecraft days till the next grailwar after one has ended").defineInRange("Grailwar Cooldown", Config.Common.grailWarCooldown, 0, Integer.MAX_VALUE);
            this.joinTime = builder.comment("Time buffer in ticks to join a grail war after start").defineInRange("Join Time", Config.Common.joinTime, 0, Integer.MAX_VALUE);
            this.allowDuplicateServant = builder.comment("Allow the summoning of duplicate servants during a grail war").define("Allow Duplicate Servants", Config.Common.allowDuplicateClass);
            this.allowDuplicateClass = builder.comment("Allow the summoning of duplicate servant classes during a grail war").define("Allow Duplicate Classes", Config.Common.allowDuplicateClass);
            this.fillMissingSlots = builder.comment("Fill in missing players till max allowed with npc").define("Fill Empty Slots", Config.Common.fillMissingSlots);
            this.maxServantCircle = builder.comment("Amount of masterless servant that can spawn each time. (Fill Empty Slots needs to be true)").defineInRange("Servant Amount", Config.Common.maxServantCircle, 1, Integer.MAX_VALUE);
            this.servantMinSpawnDelay = builder.comment("Minimum time between each attempt to spawn masterless servants. (Fill Empty Slots needs to be true)").defineInRange("Servant Spawn Delay (Min)", Config.Common.servantMinSpawnDelay, 0, Integer.MAX_VALUE);
            this.servantMaxSpawnDelay = builder.comment("Maximum time between each attempt to spawn masterless servants. (Fill Empty Slots needs to be true)").defineInRange("Servant Spawn Delay (Max)", Config.Common.servantMaxSpawnDelay, 0, Integer.MAX_VALUE);

            this.punishTeleport = builder.comment("Should teleporting servants to the owner during a fight be punished").define("Punish Teleport", Config.Common.punishTeleport);
            this.notifyBlackList = builder.comment("Servants that dont notify players when spawned (from filling missing slots)").define("Servant notification", Config.Common.notifyBlacklist);
            this.whiteList = builder.comment("Turn servant notification list into a whitelist").define("Notify Whitelist", Config.Common.notificationWhitelist);
            this.notifyAll = builder.comment("Notify everyone if a servant spawns. Else only the player the servant spawned on will be notified").define("Notify Everyone", Config.Common.notifyAll);
            this.npBoostEffect = builder.comment("Potions applied when boostin servants using a command seal. Usage: " + PotionEffectsConfig.usage()).define("NP Effects",
                    Config.Common.npBoostEffect.writeToString());
            builder.pop();

            builder.push("servants");
            this.lancelotReflectChance = builder.comment("Chance for lancelot to reflect a blocked projectile").defineInRange("Projectile Reflect Chance", Config.Common.lancelotReflectChance, 0, 1);
            this.hassanCopies = builder.comment("Amount of copies hassan can call").defineInRange("Hassan Copies", Config.Common.hassanCopies, 0, Integer.MAX_VALUE);
            builder.pop();

            builder.push("misc");
            this.gillesMinionDuration = builder.comment("Living duration of gilles monster in ticks").defineInRange("Gilles Monster", Config.Common.gillesMinionDuration, 0, Integer.MAX_VALUE);
            this.gillesMinionAmount = builder.comment("Max amount gilles can have at once").defineInRange("Gilles Monster Max Amount", Config.Common.gillesMinionAmount, 0, Integer.MAX_VALUE);
            this.babylonScale = builder.comment("Damage scaling for projectiles from the gate of babylon").defineInRange("Babylon Dmg Scale", Config.Common.babylonScale, 0, Double.MAX_VALUE);
            this.babylonWeaponsBlacklist = builder.comment("Blacklist weapons for the gate of babylon here. You can also use the modid for a whole mod").define("Babylon Blacklist",
                    Config.Common.babylonWeapons.writeToString());
            this.babylonWeaponsWhitelist = builder.comment("Turn the blacklist into a whitelist").define("Babylon Whitelist", Config.Common.babylonWeapons.isWhiteList());
            this.eaDamage = builder.comment("Damage of EA").defineInRange("EA Dmg", Config.Common.eaDamage, 0, Double.MAX_VALUE);
            this.excaliburDamage = builder.comment("Damage of excalibur").defineInRange("Excalibur Dmg", Config.Common.excaliburDamage, 0, Double.MAX_VALUE);
            this.caladBolgDmg = builder.comment("Caladbolg damage").defineInRange("Caladbolg Dmg", Config.Common.caladBolgDmg, 0, Double.MAX_VALUE);
            this.magicBeam = builder.comment("Damage of medeas magic beams").defineInRange("Magic Beam Dmg", Config.Common.magicBeam, 0, Double.MAX_VALUE);
            this.gaeBolgDmg = builder.comment("Damage of Gae Bolg").defineInRange("Gae Bolg Dmg", Config.Common.gaeBolgDmg, 0, Double.MAX_VALUE);
            this.gaeBolgEffect = builder.comment("Potions applied by Gae Bolg. Usage: " + PotionEffectsConfig.usage()).define("Gae Bolg Potions",
                    Config.Common.gaeBolgEffect.writeToString());
            this.medeaCircleSpan = builder.comment("Time in ticks for medeas magic circle").defineInRange("Magic Circle Duration", Config.Common.medeaCircleSpan, 0, Integer.MAX_VALUE);
            this.medeaCircleRange = builder.comment("Range of medeas magic circle").defineInRange("Magic Circle Range", Config.Common.medeaCircleRange, 0, Double.MAX_VALUE);
            builder.pop();

            builder.push("weapons");
            this.excaliburMana = builder.comment("Mana cost for using excalibur").defineInRange("Excalibur Mana", Config.Common.excaliburMana, 0, Integer.MAX_VALUE);
            this.eaMana = builder.comment("Mana cost for using EA").defineInRange("EA Mana", Config.Common.eaMana, 0, Integer.MAX_VALUE);
            this.archerBowMana = builder.comment("Mana cost shooting arrows with archers bow").defineInRange("Archer Arrow Cost", Config.Common.archerBowMana, 0, Integer.MAX_VALUE);
            this.caladbolgMana = builder.comment("Mana cost charging archers bow with caladbolg").defineInRange("Caladbolg Mana", Config.Common.caladbolgMana, 0, Integer.MAX_VALUE);
            this.gaeBolgMana = builder.comment("Mana cost for throwing gae bolg").defineInRange("Gae Bolg Mana", Config.Common.gaeBolgMana, 0, Integer.MAX_VALUE);
            this.grimoireMana = builder.comment("Mana cost for summoning a monster using the spellbook").defineInRange("Monster Summon Mana", Config.Common.grimoireMana, 0, Integer.MAX_VALUE);
            this.chainMana = builder.comment("Mana cost for throwing the chain dagger").defineInRange("Chain Throw Mana", Config.Common.chainMana, 0, Integer.MAX_VALUE);
            this.daggerThrowMana = builder.comment("Mana cost for throwing hassans dagger").defineInRange("Dagger Throw Mana", Config.Common.daggerThrowMana, 0, Integer.MAX_VALUE);
            this.staffMana = builder.comment("Mana cost for using medeas staff").defineInRange("Staff Mana", Config.Common.staffMana, 0, Integer.MAX_VALUE);
            builder.pop();

            this.debugAttack = builder.comment("Turn on attack bounding box debugging").define("Debug Attack", Config.Common.debugAttack);
            builder.registerReloadHandler(ConfigLoader::loadCommon);
        }
    }
}
