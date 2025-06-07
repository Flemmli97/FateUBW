package io.github.flemmli97.fateubw.fabric.common.config;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.config.ClientConfig;
import io.github.flemmli97.fateubw.common.config.CommonConfig;
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
        public final CommentedJsonConfig.CommentedVal<ClientConfig.DisplayPosition> manaBarPosition;

        public Client(CommentedJsonConfig.Builder builder) {
            this.manaBarX = builder.comment("X Position of the mana bar").defineInRange("Mana X", ClientConfig.manaX, 0, Integer.MAX_VALUE);
            this.manaBarY = builder.comment("Y Position of the mana bar").defineInRange("Mana Y", ClientConfig.manaY, 0, Integer.MAX_VALUE);
            this.manaBarPosition = builder.comment("Relative Position of the mana bar in regards to the screen").define("Mana Bar Anchor", ClientConfig.manaBarPosition);
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

        // Misc
        public final CommentedJsonConfig.DoubleVal babylonScale;
        public final CommentedJsonConfig.CommentedVal<List<String>> babylonWeaponsBlacklist;
        public final CommentedJsonConfig.CommentedVal<Boolean> babylonWeaponsWhitelist;
        public final CommentedJsonConfig.DoubleVal eaDamage;
        public final CommentedJsonConfig.DoubleVal excaliburDamage;
        public final CommentedJsonConfig.DoubleVal caladBolgDmg;
        public final CommentedJsonConfig.DoubleVal magicBeam;
        public final CommentedJsonConfig.DoubleVal gaeBolgDmg;
        public final CommentedJsonConfig.CommentedVal<List<String>> gaeBolgEffect;

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

            builder.push("misc");
            this.babylonScale = builder.comment("Damage scaling for projectiles from the gate of babylon").defineInRange("Babylon Dmg Scale", CommonConfig.babylonScale, 0, Double.MAX_VALUE);
            this.babylonWeaponsBlacklist = builder.comment("Blacklist weapons for the gate of babylon here. You can also use the modid for a whole mod").define("Babylon Blacklist",
                    CommonConfig.babylonWeapons.writeToString());
            this.babylonWeaponsWhitelist = builder.comment("Turn the blacklist into a whitelist").define("Babylon Whitelist", CommonConfig.babylonWeapons.isWhiteList());
            this.eaDamage = builder.comment("Damage of EA").defineInRange("EA Dmg", CommonConfig.eaDamage, 0, Double.MAX_VALUE);
            this.excaliburDamage = builder.comment("Damage of excalibur").defineInRange("Excalibur Dmg", CommonConfig.excaliburDamage, 0, Double.MAX_VALUE);
            this.caladBolgDmg = builder.comment("Caladbolg damage").defineInRange("Caladbolg Dmg", CommonConfig.caladBolgDmg, 0, Double.MAX_VALUE);
            this.magicBeam = builder.comment("Damage of medeas magic beams").defineInRange("Magic Beam Dmg", CommonConfig.magicBeam, 0, Double.MAX_VALUE);
            this.gaeBolgDmg = builder.comment("Damage of Gae Bolg").defineInRange("Gae Bolg Dmg", CommonConfig.gaeBolgDmg, 0, Double.MAX_VALUE);
            this.gaeBolgEffect = builder.comment("Potions applied by Gae Bolg. Usage: " + PotionEffectsConfig.usage()).define("Gae Bolg Potions",
                    CommonConfig.gaeBolgEffect.writeToString());
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
            builder.registerReloadHandler(ConfigLoader::loadCommon);
        }
    }
}
