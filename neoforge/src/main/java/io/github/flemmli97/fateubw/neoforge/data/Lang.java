package io.github.flemmli97.fateubw.neoforge.data;

import com.google.gson.JsonObject;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.servant.Medea;
import io.github.flemmli97.fateubw.common.registry.FateAttributes;
import io.github.flemmli97.fateubw.common.registry.FateBlocks;
import io.github.flemmli97.fateubw.common.registry.FateCreativeTab;
import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.registry.FateMobEffects;
import io.github.flemmli97.fateubw.common.registry.FateSounds;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Same as LanguageProvider but with a linked hashmap and reading from old lang file
 */
public class Lang implements DataProvider {

    private final Map<String, String> data = new HashMap<>();
    private final PackOutput packOutput;
    private final String modid, locale;

    private final List<String> servantClasses = FateEntities.DEFAULT_SERVANT_PROPERTIES
            .values().stream().map(b -> b.build().servantClass().getPath()).toList();

    public Lang(PackOutput packOutput) {
        this.packOutput = packOutput;
        this.modid = Fate.MODID;
        this.locale = "en_us";
    }

    protected void addTranslations() {
        for (RegistryEntrySupplier<Item, ?> reg : FateItems.ITEMS.getEntries()) {
            if (reg.get() instanceof SpawnEgg || reg.getID().getPath().startsWith("gem"))
                continue;
            if (reg == FateItems.ENUMAELISH)
                this.add(reg.get(), "EA");
            else
                this.add(reg.get(), this.simpleTranslation(reg.getID()));
        }

        this.add(FateItems.CRYSTAL_RED.get(), "Red Gem Shard");
        this.add(FateItems.CRYSTAL_BLUE.get(), "Blue Gem Shard");
        this.add(FateItems.CRYSTAL_YELLOW.get(), "Green Gem Shard");
        this.add(FateItems.CRYSTAL_GREEN.get(), "Yellow Gem Shard");
        this.add(FateItems.CRYSTAL_BLACK.get(), "Black Gem Shard");

        for (RegistryEntrySupplier<EntityType<?>, ?> type : FateEntities.ENTITIES.getEntries()) {
            SpawnEgg.fromType(type.get()).ifPresent(egg -> this.add(egg, "%s" + " Spawn Egg"));
        }

        for (RegistryEntrySupplier<Block, ?> type : FateBlocks.BLOCKS.getEntries()) {
            this.add(type.get(), this.simpleTranslation(type.getID()));
        }

        this.add(FateEntities.HASSAN_COPY.get(), this.simpleTranslation(FateEntities.HASSAN.getID()));
        for (RegistryEntrySupplier<EntityType<?>, ?> reg : FateEntities.ENTITIES.getEntries()) {
            if (!this.data.containsKey(reg.get().getDescriptionId())) {
                this.add(reg.get(), this.simpleTranslation(reg.getID()));
            }
            if (FateEntities.SERVANT_NOBEL_PHANTASM.containsKey(reg.getID())) {
                this.add(reg.getID() + ".hogou", FateEntities.SERVANT_NOBEL_PHANTASM.get(reg.getID()));
            }
        }
        this.add(Medea.CIRCLE_COMMAND, "Magic Circle");

        for (RegistryEntrySupplier<Attribute, ?> reg : FateAttributes.ATTRIBUTES.getEntries()) {
            this.add(reg.get().getDescriptionId(), this.simpleTranslation(reg.getID()));
        }

        for (RegistryEntrySupplier<MobEffect, ?> reg : FateMobEffects.EFFECTS.getEntries()) {
            this.add(reg.get().getDescriptionId(), this.simpleTranslation(reg.getID()));
        }

        for (RegistryEntrySupplier<SoundEvent, ? extends SoundEvent> reg : FateSounds.SOUND_EVENTS.getEntries()) {
            FateSounds.SoundHolder data = FateSounds.SOUND_DATA.get(reg.getID());
            if (data != null && data.defaultTranslation() != null) {
                this.add(reg.getID().toString(), data.defaultTranslation());
            } else {
                this.add(reg.asHolder());
            }
        }

        this.add("itemGroup." + FateCreativeTab.TAB.getID().getNamespace() + "." + FateCreativeTab.TAB.getID().getPath(), "Fate Unlimited Block Works");

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

        this.add("fateubw.gui.no_servant", "No Servant");
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

        FateDamageTypes.TRANSLATIONS.values().forEach(t -> t.add(this::add));

        this.add("fateubw.advancements.title", "Welcome to the §k__§r grailwar");
        this.add("fateubw.advancements.description", "Mine some gem shards to start");
        this.add("fateubw.advancements.artifact.title", "To get the strongest servant");
        this.add("fateubw.advancements.artifact.description", "Find a class artifact to increase the odds of that specific class");
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

        this.add("fateubw.book.title", "Grail War Chronicles");
        this.add("fateubw.book.landing", "The Holy Grail War... a battle between players who yearn for the power of the wish granting Holy Grail. " +
                "This book serves as a guide if you wish to also participate in it.");
    }

    private String simpleTranslation(ResourceLocation res) {
        String s = res.getPath();
        return this.simpleTranslation(s);
    }

    private String simpleTranslation(String s) {
        return Stream.of(this.capitalizeHyphen(s).trim().split("_"))
                .filter(word -> !word.isEmpty())
                .map(this::process)
                .collect(Collectors.joining(" "));
    }

    private String capitalizeHyphen(String s) {
        return Stream.of(s.trim().split("-"))
                .filter(word -> !word.isEmpty())
                .map(this::process)
                .collect(Collectors.joining("-"));
    }

    private String process(String word) {
        if (word.equals("i") || word.equals("de"))
            return word;
        String capitalized = word.substring(0, 1).toUpperCase() + word.substring(1);
        if (this.servantClasses.contains(word)) {
            return String.format("(%s)", capitalized);
        }
        return capitalized;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        this.addTranslations();
        if (!this.data.isEmpty()) {
            Path path = this.packOutput.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(this.modid + "/lang/" + this.locale + ".json");
            JsonObject json = new JsonObject();
            this.data.forEach(json::addProperty);
            return DataProvider.saveStable(cache, json, path);
        }
        return CompletableFuture.allOf();
    }

    @Override
    public String getName() {
        return "Languages: " + this.locale;
    }

    public void add(Block key, String name) {
        if (!this.data.containsKey(key.getDescriptionId()))
            this.add(key.getDescriptionId(), name);
    }

    public void add(Item key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void add(EntityType<?> key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void add(Holder<SoundEvent> key) {
        String path = key.getKey().location().getPath();
        path.substring(path.indexOf(".")).replace(".", "_");
        this.add(key.getKey().location().toString(), this.simpleTranslation(path.substring(path.indexOf(".")).replace(".", "_")));
    }

    public void add(String key, String value) {
        if (this.data.put(key, value) != null)
            throw new IllegalStateException("Duplicate translation key " + key);
    }
}
