package io.github.flemmli97.fateubw.forge.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.registry.ModBlocks;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.JavaUnicodeEscaper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
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

    private static final Comparator<String> order = Comparator.comparingInt(o -> LangType.get(o).ordinal());

    public Lang(DataGenerator gen, ExistingFileHelper existing) {
        this.gen = gen;
        this.modid = Fate.MODID;
        this.locale = "en_us";
    }

    protected void addTranslations() {
        for (RegistryEntrySupplier<Item> reg : ModItems.ITEMS.getEntries()) {
            if (reg.get() instanceof BlockItem || reg.get() instanceof SpawnEgg || reg.getID().getPath().startsWith("gem") || reg.getID().getPath().startsWith("servant_artifact"))
                continue;
            if (reg == ModItems.excalibur)
                this.add(reg.get(), "Holy Sword Excalibur");
            else if (reg == ModItems.enumaelish)
                this.add(reg.get(), "EA");
            else if (reg == ModItems.katana)
                this.add(reg.get(), "Monohoshi Zao");
            else
                this.add(reg.get(), this.simpleOfRegName(reg.getID()));
        }

        this.add(ModItems.crystalFire.get(), "Red Gem Shard");
        this.add(ModItems.crystalWater.get(), "Blue Gem Shard");
        this.add(ModItems.crystalEarth.get(), "Green Gem Shard");
        this.add(ModItems.crystalWind.get(), "Yellow Gem Shard");
        this.add(ModItems.crystalVoid.get(), "Black Gem Shard");
        this.add(ModItems.crystalCluster.get(), "Synthesized Gem");
        this.add(ModItems.charmNone.get(), "Servant Artifact");
        this.add(ModItems.charmSaber.get(), "Servant Artifact (Saber)");
        this.add(ModItems.charmArcher.get(), "Servant Artifact (Archer)");
        this.add(ModItems.charmLancer.get(), "Servant Artifact (Lancer)");
        this.add(ModItems.charmBerserker.get(), "Servant Artifact (Berserker)");
        this.add(ModItems.charmAssassin.get(), "Servant Artifact (Assassin)");
        this.add(ModItems.charmCaster.get(), "Servant Artifact (Caster)");
        this.add(ModItems.charmRider.get(), "Servant Artifact (Rider)");

        for (RegistryEntrySupplier<EntityType<?>> type : ModEntities.ENTITIES.getEntries()) {
            SpawnEgg.fromType(type.get()).ifPresent(egg -> this.add(egg, "%s" + " Spawn Egg"));
        }

        this.add(ModBlocks.altar.get(), "Summoning Altar");
        this.add(ModBlocks.gemOre.get(), "Magic Gem Ore");
        this.add(ModBlocks.artifactOre.get(), "Artifact Ore");
        this.add(ModBlocks.deepSlateGemOre.get(), "Deepslate Magic Gem Ore");
        this.add(ModBlocks.deepSlateArtifactOre.get(), "Deepslate Artifact Ore");
        this.add(ModBlocks.chalk.get(), "Chalk Line");

        this.add(ModEntities.arthur.get(), "King Arthur");
        this.add(ModEntities.arthur.getID() + ".hogou", "Excalibur");
        this.add(ModEntities.cuchulainn.get(), "Cuchulainn");
        this.add(ModEntities.cuchulainn.getID() + ".hogou", "Gae Bolg");
        this.add(ModEntities.diarmuid.get(), "Diarmuid ua Duibhne");
        this.add(ModEntities.diarmuid.getID() + ".hogou", "Gae Dearg/Buidhe");
        this.add(ModEntities.emiya.get(), "Archer Emiya");
        this.add(ModEntities.emiya.getID() + ".hogou", "Caladbolg");
        this.add(ModEntities.gilgamesh.get(), "King Gilgamesh");
        this.add(ModEntities.gilgamesh.getID() + ".hogou", "Gate of Babylon");
        this.add(ModEntities.medea.get(), "Medea");
        this.add(ModEntities.medea.getID() + ".hogou", "Rule Breaker");
        this.add(ModEntities.medea.getID() + ".circle", "Magic Circle");
        this.add(ModEntities.gilles.get(), "Gilles de Rais");
        this.add(ModEntities.gilles.getID() + ".hogou", "Prelati's Spellbook");
        this.add(ModEntities.heracles.get(), "Heracles");
        this.add(ModEntities.heracles.getID() + ".hogou", "God Hand");
        this.add(ModEntities.lancelot.get(), "Sir Lancelot");
        this.add(ModEntities.lancelot.getID() + ".hogou", "Knight of Owner");
        this.add(ModEntities.iskander.get(), "Alexander the Great");
        this.add(ModEntities.iskander.getID() + ".hogou", "Gordius Bulls");
        this.add(ModEntities.medusa.get(), "Medusa");
        this.add(ModEntities.medusa.getID() + ".hogou", "Bellerophon");
        this.add(ModEntities.hassan.get(), "Hassan-i-Sabbah");
        this.add(ModEntities.hassan.getID() + ".hogou", "Delusional Illusion");
        this.add(ModEntities.sasaki.get(), "Sasaki Kojiro");
        this.add(ModEntities.sasaki.getID() + ".hogou", "Tsubame Gaeshi");

        this.add(ModEntities.lesserMonster.get(), "Monster");
        this.add(ModEntities.gordiusWheel.get(), "Gordius Wheel");
        this.add(ModEntities.hassanCopy.get(), "Hassan-i-Sabbah");
        this.add(ModEntities.pegasus.get(), "Pegasus");

        this.add("itemGroup." + Fate.MODID + ".tab", "The Fate Universe");

        this.add("chat.item.spawn", "You already have a servant, spawned a masterless one");
        this.add("chat.mana.missing", "You don't have enough mana");
        this.add("chat.grailwar.init", "A grailwar has been initialized. You have %1$s seconds to join it.");
        this.add("chat.grailwar.start", "Joining is now locked.");
        this.add("chat.grailwar.missingplayer", "Not enough player, restarting timer");
        this.add("chat.grailwar.win", "%1$s won the grail war.");
        this.add("chat.grailwar.playerout", "%1$s is out.");
        this.add("chat.grailwar.spawn", "A servant has spawned near %1$s.");
        this.add("chat.grailwar.end", "The grailwar has ended");
        this.add("chat.truce.send", "Send a truce request to %1$s");
        this.add("chat.truce.pending", "Pending truce request from %1$s");
        this.add("chat.truce.request", "%1$s send you a truce request");
        this.add("chat.truce.requestsuccess", "%1$s accepted your a truce request");
        this.add("chat.truce.accept", "You accepted %1$s's truces request");
        this.add("chat.servant.death", "A servant has been killed");
        this.add("chat.servant.avalon", "Avalons healing ability has activated");
        this.add("chat.servant.cuchulainn", "Cuchulainn's speed increased");
        this.add("chat.servant.diarmuid", "Diarmuid's speed increased");
        this.add("chat.altar.fail", "Summon failed either because you can't join the current war or joined already");
        this.add("chat.altar.incomplete", "Incomplete Structure");
        this.add("chat.altar.existing", "How dare you to summon another servant");
        this.add("chat.command.attackall", "Your servant now attacks every mob");
        this.add("chat.command.attackservant", "Your servant now only attacks other servants");
        this.add("chat.command.defensive", "Your servant now only fights back when attacked");
        this.add("chat.command.npfail", "Seems like you don't have enough mana or command spells");
        this.add("chat.command.npsuccess", "You commanded your servant to use a Nobel Phantasm");
        this.add("chat.command.npprep", "Your servant is already preparing for an attack");
        this.add("chat.command.follow", "Your servant now follows you");
        this.add("chat.command.stay", "You told your servant to hold their position");
        this.add("chat.command.patrol", "Your servant now protects this area");
        this.add("chat.command.kill", "You killed your servant");
        this.add("chat.command.spellsuccess", "You buffed your servant using a command spell");
        this.add("chat.command.spellfail", "You don't have any command spells anymore");
        this.add("chat.medea.circle.spawn", "Medea created a magic circle");

        this.add("fate.gui.command.attack", "Attack");
        this.add("fate.gui.command.movement", "Movement");
        this.add("fate.gui.command.truce", "Truce");
        this.add("fate.gui.command.kill", "Kill");
        this.add("fate.gui.command.special", "Special");
        this.add("fate.gui.command.back", "Back");
        this.add("fate.gui.command.aggressive", "Aggressive");
        this.add("fate.gui.command.normal", "Normal");
        this.add("fate.gui.command.defensive", "Defensive");
        this.add("fate.gui.command.follow", "Follow");
        this.add("fate.gui.command.stay", "Stay");
        this.add("fate.gui.command.protect", "Guard");
        this.add("fate.gui.command.call", "Call");
        this.add("fate.gui.truce.request", "Request");
        this.add("fate.gui.truce.accept", "Accept");
        this.add("fate.gui.truce.remove", "Remove/Deny");

        this.add("death.attack.excalibur", "%1$s was vaporized by %2$s with excalibur");
        this.add("death.attack.babylon", "%1$s was impaled by %2$s with the gate of babylon");
        this.add("death.attack.gaeBolg", "%1$s's heart was pierced with gae bolg");
        this.add("death.attack.arrow", "%1$s was shoot by %2$s");

        this.add("config.fateubw.general", "General");
        this.add("config.fateubw.general.tooltip", "");
        this.add("config.fateubw.servants", "Servants");
        this.add("config.fateubw.servants.tooltip", "Configure individual servants");
        this.add("config.fateubw.minions", "Servant minions");
        this.add("config.fateubw.hassancopy", "Hassan Clone");

        this.add("advancements.fate.title", "Welcome to the §k__§r grailwar");
        this.add("advancements.fate.description", "Mine some gem shards to start");
        this.add("advancements.fate.charm.title", "To get the strongest servant");
        this.add("advancements.fate.charm.description", "Find a charm to increase the odds of a class");
        this.add("advancements.fate.join.title", "A fight between heroes");
        this.add("advancements.fate.join.description", "Join or start a grailwar");
        this.add("advancements.fate.win.title", "People die if they are killed");
        this.add("advancements.fate.win.description", "Win a grailwar. Sounds easy right");

        this.add("tooltip.item.spawn", "Rename to \"Summon\" to spawn as your servant");

        this.add("fate_book", "Fate Guidebook");
        this.add("fate.patchouli.landing", "");
        this.add("fate.patchouli.category.start", "How to start");
        this.add("fate.patchouli.category.start.desc", "");
        this.add("fate.patchouli.entry.ores", "Ores");
        this.add("fate.patchouli.entry.ores." + ModBlocks.gemOre.getID().getPath(), "Gem shards that are common underground. " +
                "Combine 5 different types to create a cluster. You need a bit of them to start. Gem clusters are throwable that create an explosion on impact");
        this.add("fate.patchouli.entry.ores." + ModBlocks.artifactOre.getID().getPath(), "Rarely found underground. Right click an empty one to get a random " +
                "artifact for a class. Using an artifact during summoning increases your chances of getting a servant of that class");
        this.add("fate.patchouli.entry.altar", "Summoning Altar");
        this.add("fate.patchouli.entry.altar.1", "To use the summoning altar you need to make a drawing chalk and use it to create a 5x5 field of chalk with the altar in the center. " +
                "After that right click the altar with the drawing chalk and it should form a proper red magic circle.");
        this.add("fate.patchouli.entry.altar.2", "Now put 8 gem clusters into the altar and right click it with another cluster to start the summoning");
        this.add("fate.patchouli.entry.altar.3", "If you use an artifact before the summoning you can increase your chances of getting a servant of said class");
        this.add("fate.patchouli.category.war", "Grail War");
        this.add("fate.patchouli.category.war.desc", "During a grailwar (depending on the config) enemy servants without players might also spawn. Defeating every servant and being the last one standing will grant the player the holy grail rewarding the player with various loot.");
        this.add("fate.patchouli.entry.servant", "Servant");
        this.add("fate.patchouli.entry.servant.1", "After you used the $(l:entry.altar)summoning altar$(/l) to summon your servant you can press $(4)($(k:fate.key.gui))$() to open the servant gui from which you can issue various orders to your servant. " +
                "Additionally pressing $(li)$(4)($(k:fate.key.np))$() commands them to use their nobel phantasm at the cost of using up a command spell and your own mana.");
        this.add("fate.patchouli.entry.servant.2", "$(li)$(4)($(k:fate.key.boost))$() uses up a command spell to boost your servant temporary." +
                "$(li)$(4)($(k:fate.key.target))$() while looking at an entity makes your servant attack said entity.");
        this.add("fate.patchouli.entry.grail", "Holy Grail");
        this.add("fate.patchouli.entry.grail.1", "By being victorious in the grail war you will be awarded with the holy grail. An object said to be able to grant any wish you want. " +
                "Though for gameplays sake this is sadly not the case. By using it you will be able to select a from a pool of loottables.");
        this.add("fate.patchouli.category.loot", "Loot");
        this.add("fate.patchouli.category.loot.desc", "Overview of possible loot to be granted. The actual loot depends on the selected loottable. The server can define custom loottables via datapacks.");
        this.add("fate.patchouli.entry.item", "Items");
        this.add("fate.patchouli.entry.item.1", "Items as per defined in the loot table");
        this.add("fate.patchouli.entry.attribute", "Attributes");
        this.add("fate.patchouli.entry.attribute.1", "Can grant attributes like permanent extra health or attack damage etc.");
        this.add("fate.patchouli.entry.loot.servant", "Servant");
        this.add("fate.patchouli.entry.loot.servant.1", "Not implemented yet");
        this.add("fate.patchouli.entry.xp", "XP");
        this.add("fate.patchouli.entry.xp.1", "Grants random amount of xp points");
        //this.add("fate.patchouli.entry.astral", "Astral Sorcery");
        //this.add("fate.patchouli.entry.astral.1", "Astral sorcery missing.");

    }

    private String simpleOfRegName(ResourceLocation res) {
        return StringUtils.capitalize(res.getPath().replace("_", " "));
    }

    private String capitalize(String s, List<String> dont) {
        return Stream.of(s.trim().split("\\s"))
                .filter(word -> word.length() > 0)
                .map(word -> dont.contains(word) ? word : word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    @Override
    public void run(HashCache cache) throws IOException {
        this.addTranslations();
        Map<String, String> sort = this.data.entrySet().stream().sorted((e, e2) -> order.compare(e.getKey(), e2.getKey()))
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
