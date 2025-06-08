package io.github.flemmli97.fateubw.forge.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.registry.ModAttributes;
import io.github.flemmli97.fateubw.common.registry.ModBlocks;
import io.github.flemmli97.fateubw.common.registry.ModEffects;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.CustomDamageSource;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.text.translate.JavaUnicodeEscaper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Same as LanguageProvider but with a linked hashmap and reading from old lang file
 */
public class Lang implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private final Map<String, String> data = new LinkedHashMap<>();
    private final DataGenerator gen;
    private final String modid;
    private final String locale;

    private static final Comparator<String> ORDER = Comparator.comparingInt(o -> LangType.get(o).ordinal());

    public Lang(DataGenerator gen) {
        this.gen = gen;
        this.modid = Fate.MODID;
        this.locale = "en_us";
    }

    protected void addTranslations() {
        for (RegistryEntrySupplier<Item> reg : ModItems.ITEMS.getEntries()) {
            if (reg.get() instanceof SpawnEgg || reg.getID().getPath().startsWith("gem") || reg.getID().getPath().startsWith("artifact"))
                continue;
            if (reg == ModItems.ENUMAELISH)
                this.add(reg.get(), "EA");
            else
                this.add(reg.get(), this.simpleOfRegName(reg.getID()));
        }

        this.add(ModItems.CRYSTAL_RED.get(), "Red Gem Shard");
        this.add(ModItems.CRYSTAL_BLUE.get(), "Blue Gem Shard");
        this.add(ModItems.CRYSTAL_YELLOW.get(), "Green Gem Shard");
        this.add(ModItems.CRYSTAL_GREEN.get(), "Yellow Gem Shard");
        this.add(ModItems.CRYSTAL_BLACK.get(), "Black Gem Shard");
        this.add(ModItems.CHARM_NONE.get(), "Artifact");
        this.add(ModItems.CHARM_SABER.get(), "Artifact (Saber)");
        this.add(ModItems.CHARM_ARCHER.get(), "Artifact (Archer)");
        this.add(ModItems.CHARM_LANCER.get(), "Artifact (Lancer)");
        this.add(ModItems.CHARM_BERSERKER.get(), "Artifact (Berserker)");
        this.add(ModItems.CHARM_ASSASSIN.get(), "Artifact (Assassin)");
        this.add(ModItems.CHARM_CASTER.get(), "Artifact (Caster)");
        this.add(ModItems.CHARM_RIDER.get(), "Artifact (Rider)");

        for (RegistryEntrySupplier<EntityType<?>> type : ModEntities.ENTITIES.getEntries()) {
            SpawnEgg.fromType(type.get()).ifPresent(egg -> this.add(egg, "%s" + " Spawn Egg"));
        }

        for (RegistryEntrySupplier<Block> type : ModBlocks.BLOCKS.getEntries()) {
            this.add(type.get(), this.simpleOfRegName(type.getID()));
        }

        this.add(ModEntities.ARTHUR.get(), "King Arthur");
        this.add(ModEntities.ARTHUR.getID() + ".hogou", "Excalibur");
        this.add(ModEntities.CUCHULAINN.get(), "Cuchulainn");
        this.add(ModEntities.CUCHULAINN.getID() + ".hogou", "Gae Bolg");
        this.add(ModEntities.DIARMUID.get(), "Diarmuid ua Duibhne");
        this.add(ModEntities.DIARMUID.getID() + ".hogou", "Gae Dearg/Buidhe");
        this.add(ModEntities.EMIYA.get(), "Archer Emiya");
        this.add(ModEntities.EMIYA.getID() + ".hogou", "Caladbolg");
        this.add(ModEntities.GILGAMESH.get(), "King Gilgamesh");
        this.add(ModEntities.GILGAMESH.getID() + ".hogou", "Gate of Babylon");
        this.add(ModEntities.MEDEA.get(), "Medea");
        this.add(ModEntities.MEDEA.getID() + ".hogou", "Rule Breaker");
        this.add(ModEntities.MEDEA.getID() + ".circle", "Magic Circle");
        this.add(ModEntities.GILLES.get(), "Gilles de Rais");
        this.add(ModEntities.GILLES.getID() + ".hogou", "Prelati's Spellbook");
        this.add(ModEntities.HERACLES.get(), "Heracles");
        this.add(ModEntities.HERACLES.getID() + ".hogou", "God Hand");
        this.add(ModEntities.LANCELOT.get(), "Sir Lancelot");
        this.add(ModEntities.LANCELOT.getID() + ".hogou", "Knight of Owner");
        this.add(ModEntities.LANCELOT.getID() + ".drop", "Drop Inventory");
        this.add(ModEntities.ISKANDER.get(), "Alexander the Great");
        this.add(ModEntities.ISKANDER.getID() + ".hogou", "Gordius Bulls");
        this.add(ModEntities.MEDUSA.get(), "Medusa");
        this.add(ModEntities.MEDUSA.getID() + ".hogou", "Bellerophon");
        this.add(ModEntities.HASSAN.get(), "Hassan-i-Sabbah");
        this.add(ModEntities.HASSAN.getID() + ".hogou", "Delusional Illusion");
        this.add(ModEntities.SASAKI.get(), "Sasaki Kojiro");
        this.add(ModEntities.SASAKI.getID() + ".hogou", "Tsubame Gaeshi");

        this.add(ModEntities.LESSER_MONSTER.get(), "Monster");
        this.add(ModEntities.GORDIUS_WHEEL.get(), "Gordius Wheel");
        this.add(ModEntities.HASSAN_COPY.get(), "Hassan-i-Sabbah");
        this.add(ModEntities.PEGASUS.get(), "Pegasus");

        for (RegistryEntrySupplier<EntityType<?>> reg : ModEntities.ENTITIES.getEntries()) {
            if (!this.data.containsKey(reg.get().getDescriptionId())) {
                this.add(reg.get(), this.simpleOfRegName(reg.getID()));
            }
        }

        for (RegistryEntrySupplier<Attribute> reg : ModAttributes.ATTRIBUTES.getEntries()) {
            this.add(reg.get().getDescriptionId(), this.simpleOfRegName(reg.getID()));
        }

        for (RegistryEntrySupplier<MobEffect> reg : ModEffects.EFFECTS.getEntries()) {
            this.add(reg.get().getDescriptionId(), this.simpleOfRegName(reg.getID()));
        }

        this.add("itemGroup." + Fate.MODID + ".tab", "The Fate Universe");

        this.add("fateubw.chat.item.spawn", "You already have a servant, spawned a masterless one");
        this.add("fateubw.chat.mana.missing", "You don't have enough mana");
        this.add("fateubw.chat.grailwar.init", "A grailwar is starting soon. You have %1$s seconds to join it.");
        this.add("fateubw.chat.grailwar.start", "A grailwar has now started. Joining it is not possible anymore");
        this.add("fateubw.chat.grailwar.players.none", "The grailwar doesn't have any player participants. The grail refuses to start the war!");
        this.add("fateubw.chat.grailwar.players.missing", "Not enough player, restarting timer");
        this.add("fateubw.chat.grailwar.players.dead", "All player participants are dead. Grailwar has ended with no winner.");
        this.add("fateubw.chat.grailwar.win", "%1$s won the grail war.");
        this.add("fateubw.chat.grailwar.win.none", "Unable to determine a winner for the grailwar");
        this.add("fateubw.chat.grailwar.win.spawn", "The grail appeared near you");
        this.add("fateubw.chat.grailwar.player.out", "%1$s is out.");
        this.add("fateubw.chat.grailwar.spawn", "A servant has spawned near %1$s.");
        this.add("fateubw.chat.grailwar.end", "The grailwar has ended");
        this.add(GrailWarHandler.JoinResult.WRONG_STATE.translationKey, "Now is not the time to join a grailwar!");
        this.add(GrailWarHandler.JoinResult.FULL.translationKey, "The grail doesn't accept more participants!");
        this.add(GrailWarHandler.JoinResult.JOINED.translationKey, "You already joined this grailwar");
        this.add(GrailWarHandler.JoinResult.WRONG_SERVANT.translationKey, "This servant can't join the grailwar!");
        this.add(GrailWarHandler.JoinResult.NO_MORE_SERVANTS.translationKey, "No more servants can join this grailwar");
        this.add(GrailWarHandler.JoinResult.SUCCESS.translationKey, "Success");

        this.add("fateubw.chat.servant.death", "A servant has been killed");
        this.add("fateubw.chat.servant.avalon", "Avalons healing ability has activated");
        this.add("fateubw.chat.servant.cuchulainn", "Cuchulainn's speed increased");
        this.add("fateubw.chat.servant.diarmuid", "Diarmuid's speed increased");
        this.add("fateubw.chat.altar.incomplete", "Incomplete Structure");
        this.add("fateubw.chat.altar.missing.catalyst", "Missing gems!");
        this.add("fateubw.chat.altar.servant.existing", "How dare you to summon another servant!");
        this.add("fateubw.chat.command.attackall", "Your servant now attacks every mob");
        this.add("fateubw.chat.command.attackservant", "Your servant now only attacks other servants");
        this.add("fateubw.chat.command.defensive", "Your servant now only fights back when attacked");
        this.add("fateubw.chat.command.npfail", "Seems like you don't have enough mana or command spells");
        this.add("fateubw.chat.command.npsuccess", "You commanded your servant to use a Nobel Phantasm");
        this.add("fateubw.chat.command.npprep", "Your servant is already preparing for an attack");
        this.add("fateubw.chat.command.follow", "Your servant now follows you");
        this.add("fateubw.chat.command.stay", "You told your servant to hold their position");
        this.add("fateubw.chat.command.patrol", "Your servant now protects this area");
        this.add("fateubw.chat.command.kill", "You killed your servant");
        this.add("fateubw.chat.command.spell.success", "You buffed your servant using a command spell");
        this.add("fateubw.chat.command.spell.fail", "You don't have any command spells anymore");
        this.add("fateubw.chat.medea.circle.spawn", "Medea created a magic circle");
        this.add("fateubw.chat.item.command.fail", "What are you doing???");
        this.add("fateubw.chat.team.permission.no", "You don't have the permission for this action!");
        this.add("fateubw.chat.team.self.no", "You can't target yourself!");
        this.add("fateubw.chat.team.missing", "No such team %s exists!");
        this.add("fateubw.chat.team.player.exist", "%s is already in a team!");
        this.add("fateubw.chat.team.promote", "%s promoted you to admin");
        this.add("fateubw.chat.team.promote.user", "You promoted %s to admin");
        this.add("fateubw.chat.team.demote", "%s demoted you");
        this.add("fateubw.chat.team.demote.user", "You demoted %s");
        this.add("fateubw.chat.team.kicked", "%s kicked you from team %s");
        this.add("fateubw.chat.team.invite.pending", "You have pending invites from %s");
        this.add("fateubw.chat.team.invite.sent", "You sent out an invite to %s");
        this.add("fateubw.chat.team.invite.received", "You received an invite from team %s");
        this.add("fateubw.chat.team.alliance.start", "%s and %s started an alliance!");
        this.add("fateubw.chat.team.alliance.dissolved", "%s of team %s dissolved the alliance!");
        this.add("fateubw.chat.team.alliance.dissolved.with", "%s dissolved the alliance with team %s!");
        this.add("fateubw.chat.team.alliance.denied", "%s denied your alliance request...");
        this.add("fateubw.chat.team.alliance.pending", "You have pending alliance requests from [%s]");
        this.add("fateubw.chat.team.alliance.sent", "You sent out an ally request to team %s");
        this.add("fateubw.chat.team.alliance.received", "You received an ally request from team %s");

        this.add("fateubw.gui.name", "Name:");
        this.add("fateubw.gui.nobel_phantasm", "Nobel Phantasm");
        this.add("fateubw.gui.nobel_phantasm_cost", "Mana Cost");
        this.add("fateubw.gui.save", "Save");
        this.add("fateubw.gui.back", "Back");
        this.add("fateubw.gui.command", "Command GUI");
        this.add("fateubw.gui.command.attack", "Attack");
        this.add("fateubw.gui.command.movement", "Movement");
        this.add("fateubw.gui.command.kill", "Kill");
        this.add("fateubw.gui.command.special", "Special");
        this.add("fateubw.gui.command.aggressive", "Aggressive");
        this.add("fateubw.gui.command.normal", "Normal");
        this.add("fateubw.gui.command.defensive", "Defensive");
        this.add("fateubw.gui.command.follow", "Follow");
        this.add("fateubw.gui.command.stay", "Stay");
        this.add("fateubw.gui.command.protect", "Guard");
        this.add("fateubw.gui.command.call", "Call");

        this.add("fateubw.gui.spawn.master", "Master");
        this.add("fateubw.gui.spawn.war", "Create/Join grailwar");
        this.add("fateubw.gui.spawn.war.help", "Requires being master");

        this.add("fateubw.gui.team", "Team");
        this.add("fateubw.gui.team.name", "%s");
        this.add("fateubw.gui.team.invites", "Invites");
        this.add("fateubw.gui.team.allies", "Allies");
        this.add("fateubw.gui.team.members", "Members");
        this.add("fateubw.gui.team.leave", "Leave");
        this.add("fateubw.gui.team.disband", "Disband");
        this.add("fateubw.gui.team.create", "Create");
        this.add("fateubw.gui.team.none", "You currently are not in a team! Either create a new one or get invited to one.");
        this.add("fateubw.gui.team.invite", "Invite");
        this.add("fateubw.gui.team.retract", "Retract");
        this.add("fateubw.gui.team.accept", "Accept");
        this.add("fateubw.gui.team.deny", "Deny");
        this.add("fateubw.gui.team.dissolve", "Dissolve");
        this.add("fateubw.gui.team.kick", "Kick");
        this.add("fateubw.gui.team.promote", "Promote");
        this.add("fateubw.gui.team.demote", "Demote");
        this.add("fateubw.gui.team.request", "Request");
        this.add("fateubw.gui.team.rename", "Click to rename team");

        this.add("fateubw.gui.holy_grail", "Holy Grail");

        CustomDamageSource.defaultTranslations().forEach(this::add);

        this.add("fateubw.advancements.title", "Welcome to the §k__§r grailwar");
        this.add("fateubw.advancements.description", "Mine some gem shards to start");
        this.add("fateubw.advancements.charm.title", "To get the strongest servant");
        this.add("fateubw.advancements.charm.description", "Find a charm to increase the odds of a class");
        this.add("fateubw.advancements.join.title", "A fight between heroes");
        this.add("fateubw.advancements.join.description", "Join or start a grailwar");
        this.add("fateubw.advancements.win.title", "People die if they are killed");
        this.add("fateubw.advancements.win.description", "Win a grailwar. Sounds easy right");

        this.add("fateubw.command.war.start", "Manually started a grailwar");
        this.add("fateubw.command.war.start.fail", "Could not start a grailwar cause one is already running. Stop the current one first");
        this.add("fateubw.command.loot.give", "Gave loot %1$s to %2$s players");
        this.add("fateubw.command.loot.give.single", "Gave loot %1$s to %2$s");
        this.add("fateubw.command.loot.none", "No such loot %s exists");
        this.add("fateubw.command.attributes.reset", "Reset all attributes obtained through grail loots for %s");
        this.add("fateubw.command.spells.set", "Set command spells for %2$s to %1$s");
        this.add("fateubw.command.spells.take", "Took %2$s command spells from %1$s");
        this.add("fateubw.command.spells.add", "Gave %2$s command spells to %1$s");

        this.add("fateubw.tooltip.item.spawn", "Right click in air to change data");
        this.add("fateubw.tooltip.item.command", "Allows commanding your servant");
        this.add("fateubw.tooltip.item.mana", "Consumes %s mana on use");
        this.add("fateubw.tooltip.item.bow.arrow", "Consumes %s mana per arrow");
        this.add("fateubw.tooltip.item.caladbolg", "Left click to charge an explosive shot. Consumes %s mana");

        this.add("fateubw.loot.grails_blessing", "Grails Blessing");
        this.add("fateubw.loot.explorers_dream", "Explorers Dream");
        this.add("fateubw.loot.grail_empowerment", "Empowerment of the Grail");
        this.add("fateubw.loot.divine_protection", "Divine Protection");
        this.add("fateubw.loot.eternal_pact", "Eternal Pact");
        this.add("fateubw.loot.legendary_armaments", "Legendary Armaments");

        this.add("fateubw.keycategory", "Fate UBW");
        this.add("fateubw.key.gui", "Gui");
        this.add("fateubw.key.np", "Noble Phantasm");
        this.add("fateubw.key.boost", "Command Boost");
        this.add("fateubw.key.target", "Target");

        this.add("fateubw_book", "Fate Guidebook");
        this.add("fateubw.patchouli.landing", "The Holy Grail War... a battle between players who yearn for the power of the wish granting Holy Grail. " +
                "This book serves as a guide if you wish to also participate in it.");
        this.add("fateubw.patchouli.category.start", "Getting started");
        this.add("fateubw.patchouli.category.start.desc", "Grail wars happen regulary in the world. The grail will announce when players are able to join one. " +
                "During a grailwar enemy servants without players might also spawn. Defeating every servant and being the last one standing will grant the player the holy grail rewarding the player with various loot.");
        this.add("fateubw.patchouli.entry.ores", "Ores");

        this.add("fateubw.patchouli.entry.ores." + ModBlocks.GEM_ORE.getID().getPath(), "These ores pulse faintly with residual mana. When mined, it yields small pieces of mana shards. " +
                "Combining the different types of shards and a bit of mana one can create a larger and stronger mana crystal. " +
                "The created gem itself explodes violently when hurled as a projectile but its true purpose lies in the summoning ritual.");
        this.add("fateubw.patchouli.entry.ores." + ModBlocks.ARTIFACT_ORE.getID().getPath(), "Deeper still lies the much rarer Artifact Ore. These stones will yield forgotten relics of specific servant classes. " +
                "These artifacts can be used during a summoning ritual to increasing the chance that a Servant of matching class will heed your call.");
        this.add("fateubw.patchouli.entry.altar", "Summoning Altar");
        this.add("fateubw.patchouli.entry.altar.1", "At the heart of all Grail rituals lies the Summoning Altar—a carefully constructed array designed to bridge the gap between the mortal world and the Throne of Heroes. ");
        this.add("fateubw.patchouli.entry.altar.2", "To begin inscribe a 5x5 area using chalk centered around the altar. Right clicking the altar should then complete it.");
        this.add("fateubw.patchouli.entry.altar.3", "By offering 8 mana crystals and right clicking once again will start the summoning process calling forth your servant. If you possess an artifact you may place it on the altar before activation. " +
                "These can boost you chance of increasing the odds that a servant of that class will heed your call.");
        this.add("fateubw.patchouli.entry.servant", "Servant");
        this.add("fateubw.patchouli.entry.servant.1", "Servants are the physical embodiments of Heroic Spirits, summoned via the $(l:entry.altar)summoning altar$(/l) to serve a Master in battle.$(br)$(br) " +
                "To manage and issue orders to your Servant, press $(4)($(k:fateubw.key.gui))$() to open a GUI allowing you to command basic behaviors—such as follow, hold position etc. " +
                "Additionally several keybindings grant you more advanced control during battle:");
        this.add("fateubw.patchouli.entry.servant.2", "$(li)$(4)($(k:fateubw.key.np))$() commands them to use their nobel phantasm at the cost of using up a command spell and your own mana. " +
                "$(li)$(4)($(k:fateubw.key.boost))$() to expend a Command Spell, releasing a surge of magical energy that greatly enhances your Servant’s combat abilities for a short time." +
                "$(li)$(4)($(k:fateubw.key.target))$() while looking at an entity makes your servant prioritize and attack said entity.");
        this.add("fateubw.patchouli.entry.grail", "The Holy Grail");
        this.add("fateubw.patchouli.entry.grail.1", "By being victorious in the grail war you will be awarded with the holy grail. An object said to be able to grant any wish you want. " +
                "Upon use you may choose between multiple possible powerful rewards.");
        this.add("fateubw.patchouli.category.loot", "Loot");
        this.add("fateubw.patchouli.category.loot.desc", "This section is more addressed for pack devs and contains an overview of possible loot to be granted. The actual loot depends on the selected loottable. The server can define custom loottables via datapacks.");
        this.add("fateubw.patchouli.entry.item", "Items");
        this.add("fateubw.patchouli.entry.item.1", "Various items as per defined in the loot table");
        this.add("fateubw.patchouli.entry.attribute", "Attributes");
        this.add("fateubw.patchouli.entry.attribute.1", "Can grant permant attributes increases like extra health, attack damage etc.");
        this.add("fateubw.patchouli.entry.loot.servant", "Servant");
        this.add("fateubw.patchouli.entry.loot.servant.1", "Resummons the servant used in the last grailwar. Or drops the servants loot (i.e. their weapon)");
        this.add("fateubw.patchouli.entry.commands", "Commands");
        this.add("fateubw.patchouli.entry.commands.1", "Allows executing of commands");
        this.add("fateubw.patchouli.entry.xp", "XP");
        this.add("fateubw.patchouli.entry.xp.1", "Grants random amount of xp points");
    }

    private String simpleOfRegName(ResourceLocation res) {
        String s = res.getPath();
        return Stream.of(s.trim().split("_"))
                .filter(word -> !word.isEmpty())
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    @Override
    public void run(HashCache cache) throws IOException {
        this.addTranslations();
        Map<String, String> sort = this.data.entrySet().stream().sorted((e, e2) -> ORDER.compare(e.getKey(), e2.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (old, v) -> old, LinkedHashMap::new));
        if (!this.data.isEmpty())
            this.save(cache, sort, this.gen.getOutputFolder().resolve("assets/" + this.modid + "/lang/" + this.locale + ".json"));
    }

    @Override
    public String getName() {
        return "Languages: " + this.locale;
    }

    @SuppressWarnings("deprecation")
    private void save(HashCache cache, Object object, Path target) throws IOException {
        String data = GSON.toJson(object);
        data = JavaUnicodeEscaper.outsideOf(0, 0x7f).translate(data); // Escape unicode after the fact so that it's not double escaped by GSON
        String hash = DataProvider.SHA1.hashUnencodedChars(data).toString();
        if (!Objects.equals(cache.getHash(target), hash) || !Files.exists(target)) {
            Files.createDirectories(target.getParent());

            try (BufferedWriter bufferedwriter = Files.newBufferedWriter(target)) {
                bufferedwriter.write(data);
            }
        }

        cache.putNew(target, hash);
    }

    public void addBlock(Supplier<? extends Block> key, String name) {
        this.add(key.get(), name);
    }

    public void add(Block key, String name) {
        if (!this.data.containsKey(key.getDescriptionId()))
            this.add(key.getDescriptionId(), name);
    }

    public void addItem(Supplier<? extends Item> key, String name) {
        this.add(key.get(), name);
    }

    public void add(Item key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void addItemStack(Supplier<ItemStack> key, String name) {
        this.add(key.get(), name);
    }

    public void add(ItemStack key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void addEnchantment(Supplier<? extends Enchantment> key, String name) {
        this.add(key.get(), name);
    }

    public void add(Enchantment key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void addEffect(Supplier<? extends MobEffect> key, String name) {
        this.add(key.get(), name);
    }

    public void add(MobEffect key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void addEntityType(Supplier<? extends EntityType<?>> key, String name) {
        this.add(key.get(), name);
    }

    public void add(EntityType<?> key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void add(String key, String value) {
        if (this.data.put(key, value) != null)
            throw new IllegalStateException("Duplicate translation key " + key);
    }

    enum LangType {
        ITEM,
        BLOCK,
        ENTITY,
        CONTAINER,
        TOOLTIP,
        DEATH,
        ITEMGROUP,
        OTHER;

        public static LangType get(String s) {
            if (s.startsWith("item."))
                return ITEM;
            if (s.startsWith("block."))
                return BLOCK;
            if (s.startsWith("entity."))
                return ENTITY;
            if (s.startsWith("container."))
                return CONTAINER;
            if (s.startsWith("tooltip."))
                return TOOLTIP;
            if (s.startsWith("death."))
                return DEATH;
            if (s.startsWith("itemGroup."))
                return ITEMGROUP;
            return OTHER;
        }
    }
}
