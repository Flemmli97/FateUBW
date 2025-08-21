package io.github.flemmli97.fateubw.neoforge.data.book;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.registry.FateBlocks;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import net.favouriteless.modopedia.api.datagen.BookContentOutput;
import net.favouriteless.modopedia.api.datagen.builders.CategoryBuilder;
import net.favouriteless.modopedia.api.datagen.builders.EntryBuilder;
import net.favouriteless.modopedia.api.datagen.builders.PageBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.HeaderBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.MultiblockBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.SeparatorBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.ShowcaseBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.TextBuilder;
import net.favouriteless.modopedia.api.datagen.builders.templates.page.HeaderedTextBuilder;
import net.favouriteless.modopedia.api.datagen.builders.templates.recipes.CraftingRecipeBuilder;
import net.favouriteless.modopedia.api.datagen.providers.ContentSetProvider;
import net.favouriteless.modopedia.client.multiblock.DenseMultiblock;
import net.favouriteless.modopedia.client.multiblock.state_matchers.SimpleStateMatcher;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BookContentGen extends ContentSetProvider {

    private final Map<String, String> translations = new HashMap<>();

    public BookContentGen(CompletableFuture<HolderLookup.Provider> registries, PackOutput output) {
        super(Fate.MODID, "fateubw_book", "en_us", registries, output);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        this.createTranslations();
        return super.run(output);
    }

    @Override
    public void buildEntries(HolderLookup.Provider registries, BookContentOutput output) {
        EntryBuilder.of(this.get("fateubw.book.entry.ores"))
                .icon(new ItemStack(FateBlocks.GEM_ORE.get()))
                .pages(this.displayItem("fateubw.book.entry.ores.1.title", new ItemStack(FateBlocks.GEM_ORE.get()),
                        "fateubw.book.entry.ores.1"))
                .pages(this.displayItem("fateubw.book.entry.ores.2.title", new ItemStack(FateBlocks.ARTIFACT_ORE.get()),
                        "fateubw.book.entry.ores.2"))
                .build("entry_ores", output, "category_start");
        EntryBuilder.of(this.get("fateubw.book.entry.altar"))
                .icon(new ItemStack(FateBlocks.ALTAR.get()))
                .page(TextBuilder.of(this.get("fateubw.book.entry.altar.1")),
                        CraftingRecipeBuilder.of(Fate.modRes("summoning_altar")).y(80))
                .page(MultiblockBuilder.of().multiblock(new DenseMultiblock(List.of(List.of(
                                "ccccc",
                                "ccccc",
                                "ccacc",
                                "ccccc",
                                "ccccc")),
                                Map.of('a', new SimpleStateMatcher(List.of(FateBlocks.ALTAR.get().defaultBlockState())),
                                        'c', new SimpleStateMatcher(List.of(FateBlocks.CHALK.get().defaultBlockState()))))),
                        TextBuilder.of(this.get("fateubw.book.entry.altar.2")).y(90))
                .page(TextBuilder.of(this.get("fateubw.book.entry.altar.3")))
                .build("entry_altar", output, "category_start");

        EntryBuilder.of(this.get("fateubw.book.entry.servant"))
                .icon(new ItemStack(FateItems.ARTIFACT_SABER.get()))
                .page(HeaderedTextBuilder.of(this.get("fateubw.book.entry.servant"), this.get("fateubw.book.entry.servant.1")))
                .page(TextBuilder.of(this.get("fateubw.book.entry.servant.2")))
                .build("entry_servant", output, "category_start");

        EntryBuilder.of(this.get("fateubw.book.entry.grail"))
                .icon(new ItemStack(FateItems.GRAIL.get()))
                .page(HeaderBuilder.of(this.get("fateubw.book.entry.grail")),
                        SeparatorBuilder.of().y(10),
                        ShowcaseBuilder.of(new ItemStack(FateItems.GRAIL.get())).y(30))
                .page(TextBuilder.of(this.get("fateubw.book.entry.grail.1")))
                .build("entry_grail", output, "category_start");

        EntryBuilder.of(this.get("fateubw.book.entry.loot.item"))
                .icon(new ItemStack(Items.STICK))
                .page(HeaderedTextBuilder.of(this.get("fateubw.book.entry.loot.item"), this.get("fateubw.book.entry.loot.item.1")))
                .build("entry_item", output, "category_loot");
        EntryBuilder.of(this.get("fateubw.book.entry.loot.attribute"))
                .icon(new ItemStack(Items.IRON_SWORD))
                .page(HeaderedTextBuilder.of(this.get("fateubw.book.entry.loot.attribute"), this.get("fateubw.book.entry.loot.attribute.1")))
                .build("entry_attribute", output, "category_loot");
        EntryBuilder.of(this.get("fateubw.book.entry.loot.servant"))
                .icon(new ItemStack(SpawnEgg.fromType(FateEntities.ARTHUR.get()).orElseThrow()))
                .page(HeaderedTextBuilder.of(this.get("fateubw.book.entry.loot.servant"), this.get("fateubw.book.entry.loot.servant.1")))
                .build("entry_loot_servant", output, "category_loot");
        EntryBuilder.of(this.get("fateubw.book.entry.loot.commands"))
                .icon(new ItemStack(Items.COMMAND_BLOCK))
                .page(HeaderedTextBuilder.of(this.get("fateubw.book.entry.loot.commands"), this.get("fateubw.book.entry.loot.commands.1")))
                .build("entry_commands", output, "category_loot");
        EntryBuilder.of(this.get("fateubw.book.entry.loot.xp"))
                .icon(new ItemStack(Items.EXPERIENCE_BOTTLE))
                .page(HeaderedTextBuilder.of(this.get("fateubw.book.entry.loot.xp"), this.get("fateubw.book.entry.loot.xp.1")))
                .build("entry_xp", output, "category_loot");
    }

    protected PageBuilder displayItem(String header, ItemStack stack, String text) {
        int y = 0;
        return PageBuilder.of()
                .components(HeaderBuilder.of(this.get(header)),
                        SeparatorBuilder.of().y(y += 10),
                        ShowcaseBuilder.of(stack).scale(0.6f),
                        TextBuilder.of(this.get(text))
                                .y(y + 60 + 6));
    }

    @Override
    public void buildCategories(HolderLookup.Provider registries, BookContentOutput output) {
        int sort = 0;
        CategoryBuilder.of(this.get("fateubw.book.category.start"))
                .landingText(this.get("fateubw.book.category.start.desc"))
                .icon(new ItemStack(FateItems.MANA_GEM.get()))
                .sortNum(sort++)
                .build("category_start", output);

        CategoryBuilder.of(this.get("fateubw.book.category.loot"))
                .landingText(this.get("fateubw.book.category.loot.desc"))
                .icon(new ItemStack(Blocks.CHEST))
                .sortNum(sort++)
                .build("category_loot", output);

    }
    protected void createTranslations() {
        this.add("fateubw.book.category.start", "Getting started");
        this.add("fateubw.book.category.start.desc", "Grail wars happen regulary in the world. The grail will announce when players are able to join one. " +
                "During a grailwar enemy servants without players might also spawn. Defeating every servant and being the last one standing will grant the player the holy grail rewarding the player with various loot.");

        this.add("fateubw.book.entry.ores", "Ores");
        this.add("fateubw.book.entry.ores.1.title", "Gem Ores");
        this.add("fateubw.book.entry.ores.1", "These ores pulse faintly with residual mana. When mined, it yields small pieces of mana shards. " +
                "Combining the different types of shards and a bit of mana one can create a larger and stronger mana crystal. " +
                "The created gem itself explodes violently when hurled as a projectile but its true purpose lies in the summoning ritual.");
        this.add("fateubw.book.entry.ores.2.title", "Artifact Ores");
        this.add("fateubw.book.entry.ores.2", "Deeper still lies the much rarer Artifact Ore. These stones will yield forgotten relics of specific servant classes. " +
                "These artifacts can be used during a summoning ritual to increasing the chance that a Servant of matching class will heed your call.");

        this.add("fateubw.book.entry.altar", "Summoning Altar");
        this.add("fateubw.book.entry.altar.1", "At the heart of all Grail rituals lies the Summoning Altar—a carefully constructed array designed to bridge the gap between the mortal world and the Throne of Heroes. ");
        this.add("fateubw.book.entry.altar.2", "To begin inscribe a 5x5 area using chalk centered around the altar. Right clicking the altar should then complete it.");
        this.add("fateubw.book.entry.altar.3", "By offering 8 mana crystals and right clicking once again will start the summoning process calling forth your servant. If you possess an artifact you may place it on the altar before activation. " +
                "These can boost you chance of increasing the odds that a servant of that class will heed your call.");
        this.add("fateubw.book.entry.servant", "Servant");
        this.add("fateubw.book.entry.servant.1", "Servants are the physical embodiments of Heroic Spirits, summoned via the $(l:entry.altar)summoning altar$(/l) to serve a Master in battle.$(br)$(br) " +
                "To manage and issue orders to your Servant, press $(4)($(k:fateubw.key.gui))$() to open a GUI allowing you to command basic behaviors—such as follow, hold position etc. " +
                "Additionally several keybindings grant you more advanced control during battle:");
        this.add("fateubw.book.entry.servant.2", "$(li)$(4)($(k:fateubw.key.np))$() commands them to use their nobel phantasm at the cost of using up a command spell and your own mana. " +
                "$(li)$(4)($(k:fateubw.key.boost))$() to expend a Command Spell, releasing a surge of magical energy that greatly enhances your Servant’s combat abilities for a short time." +
                "$(li)$(4)($(k:fateubw.key.target))$() while looking at an entity makes your servant prioritize and attack said entity.");
        this.add("fateubw.book.entry.grail", "The Holy Grail");
        this.add("fateubw.book.entry.grail.1", "By being victorious in the grail war you will be awarded with the holy grail. An object said to be able to grant any wish you want. " +
                "Upon use you may choose between multiple possible powerful rewards.");
        this.add("fateubw.book.category.loot", "Loot");
        this.add("fateubw.book.category.loot.desc", "This section is more addressed for pack devs and contains an overview of possible loot to be granted. The actual loot depends on the selected loottable. The server can define custom loottables via datapacks.");
        this.add("fateubw.book.entry.loot.item", "Item Loot");
        this.add("fateubw.book.entry.loot.item.1", "Various items as per defined in the loot table");
        this.add("fateubw.book.entry.loot.attribute", "Attributes");
        this.add("fateubw.book.entry.loot.attribute.1", "Can grant permant attributes increases like extra health, attack damage etc.");
        this.add("fateubw.book.entry.loot.servant", "Servant");
        this.add("fateubw.book.entry.loot.servant.1", "Resummons the servant used in the last grailwar. Or drops the servants loot (i.e. their weapon)");
        this.add("fateubw.book.entry.loot.commands", "Commands");
        this.add("fateubw.book.entry.loot.commands.1", "Allows executing of commands");
        this.add("fateubw.book.entry.loot.xp", "XP");
        this.add("fateubw.book.entry.loot.xp.1", "Grants random amount of xp points");
    }

    public void add(String key, String value) {
        if (this.translations.put(key, value) != null)
            throw new IllegalStateException("Duplicate translation key " + key);
    }

    protected String get(String key) {
        String val = this.translations.get(key);
        if (val == null)
            throw new IllegalStateException("No translation for " + key);
        return val;
    }
}
