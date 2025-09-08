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
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.ImageBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.MultiblockBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.SeparatorBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.ShowcaseBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.TextBuilder;
import net.favouriteless.modopedia.api.datagen.builders.templates.FramedItemGalleryBuilder;
import net.favouriteless.modopedia.api.datagen.builders.templates.page.HeaderedTextBuilder;
import net.favouriteless.modopedia.api.datagen.builders.templates.recipes.CraftingRecipeBuilder;
import net.favouriteless.modopedia.api.datagen.providers.ContentSetProvider;
import net.favouriteless.modopedia.client.multiblock.DenseMultiblock;
import net.favouriteless.modopedia.client.multiblock.state_matchers.SimpleStateMatcher;
import net.favouriteless.modopedia.client.page_components.item_displays.CyclingItemDisplay;
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
                .page(HeaderBuilder.of(this.get("fateubw.book.entry.altar")),
                        SeparatorBuilder.of().y(10),
                        TextBuilder.of(this.get("fateubw.book.entry.altar.1")).y(17),
                        CraftingRecipeBuilder.of(Fate.modRes("summoning_altar")).y(90))
                .page(MultiblockBuilder.of().multiblock(new DenseMultiblock(List.of(List.of(
                                "ccccc",
                                "ccccc",
                                "ccacc",
                                "ccccc",
                                "ccccc")),
                                Map.of('a', new SimpleStateMatcher(List.of(FateBlocks.ALTAR.get().defaultBlockState())),
                                        'c', new SimpleStateMatcher(List.of(FateBlocks.CHALK.get().defaultBlockState()))))),
                        TextBuilder.of(this.get("fateubw.book.entry.altar.2")).y(90))
                .page(TextBuilder.of(this.get("fateubw.book.entry.altar.3")),
                        FramedItemGalleryBuilder.of(new CyclingItemDisplay(List.of(FateItems.ARTIFACT_SABER.get().getDefaultInstance(),
                                        FateItems.ARTIFACT_ARCHER.get().getDefaultInstance(),
                                        FateItems.ARTIFACT_LANCER.get().getDefaultInstance(),
                                        FateItems.ARTIFACT_CASTER.get().getDefaultInstance(),
                                        FateItems.ARTIFACT_BERSERKER.get().getDefaultInstance(),
                                        FateItems.ARTIFACT_RIDER.get().getDefaultInstance(),
                                        FateItems.ARTIFACT_ASSASSIN.get().getDefaultInstance())))
                                .y(115).x(50 - 8))
                .page(TextBuilder.of(this.get("fateubw.book.entry.altar.4")))
                .build("entry_altar", output, "category_start");

        EntryBuilder.of(this.get("fateubw.book.entry.servant"))
                .icon(new ItemStack(FateItems.ARTIFACT_SABER.get()))
                .page(HeaderedTextBuilder.of(this.get("fateubw.book.entry.servant"), this.get("fateubw.book.entry.servant.1")))
                .page(TextBuilder.of(this.get("fateubw.book.entry.servant.2")).y(32),
                        ImageBuilder.of(Fate.modRes("textures/book/command_seal.png"))
                                .width(32).height(32).x(100 - 32))
                .page(TextBuilder.of(this.get("fateubw.book.entry.servant.3")))
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
                        ShowcaseBuilder.of(stack).y(-5).scale(0.5f),
                        TextBuilder.of(this.get(text))
                                .y(y + 60));
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
        this.add("fateubw.book.category.start.desc", "Grail wars happen $(c:gold)regulary$() in the world. The grail will announce when players are able to join one. " +
                "During a grailwar enemy servants without players might also spawn. Defeating every servant and being the last one standing will grant the player the holy grail rewarding the player with various loot.");

        this.add("fateubw.book.entry.ores", "Ores");
        this.add("fateubw.book.entry.ores.1.title", "Gem Ores");
        this.add("fateubw.book.entry.ores.1", "Combining different types of shards with a bit of mana fuses them into a larger and stronger mana crystal. " +
                "While it explodes violently when hurled as a projectile its true purpose lies in the summoning ritual.");
        this.add("fateubw.book.entry.ores.2.title", "Artifact Ores");
        this.add("fateubw.book.entry.ores.2", "Rarely found underground these ores yield forgotten artifacts of servant classes. " +
                "When used during a summoning ritual they increase the chance that a servant of matching class will heed your call.");

        this.add("fateubw.book.entry.altar", "Summoning Altar");
        this.add("fateubw.book.entry.altar.1", "At the heart of all Grail rituals lies the Summoning Altar, a carefully constructed array designed to bridge the gap between the mortal world and the Throne of Heroes.");
        this.add("fateubw.book.entry.altar.2", "A proper setup requires you to inscribe a 5x5 area using chalk centered around the altar. Right clicking the altar should then complete it.");
        this.add("fateubw.book.entry.altar.3", """
                By offering 8 mana gems and right clicking once again will start the summoning process calling forth your servant.
                If you possess an artifact you may place it on the altar before activation. \
                These can boost you chance of increasing the odds that a servant of that class will heed your call.""");
        this.add("fateubw.book.entry.altar.4", "This procedure only works with a current grailwar ongoing which happens every few days.");
        this.add("fateubw.book.entry.servant", "Servant");
        this.add("fateubw.book.entry.servant.1", """
                Servants are the physical embodiments of Heroic Spirits, summoned via the $(el:entry_altar)summoning altar$() to serve a Master in battle.
                
                To manage and issue orders to your Servant, press the $(c:darkpurple)$(t:Default: h)GUI key$() to open a GUI allowing you to command basic behaviors—such as follow, hold position etc.
                """);
        this.add("fateubw.book.entry.servant.2", """
                There are also some additional actions you can use for more advanced controls during battle:
                 ▶ You can command your servant to use their nobel phantasm at the cost of using up a command spell and your own mana ($(c:darkpurple)Default: j$()).
                """);
        this.add("fateubw.book.entry.servant.3", """
                 ▶ Pressing the $(c:darkpurple)$(t:Default: m)boost key$() will release a surge of magical energy that greatly enhances your Servant’s combat abilities for a short time at the cost of a command spell.
                 ▶ Pressing the $(c:darkpurple)$(t:Default: b)target key$() while looking at an entity makes your servant prioritize and attack said entity.
                """);
        this.add("fateubw.book.entry.grail", "The Holy Grail");
        this.add("fateubw.book.entry.grail.1", """
                This is it! The holy relic everyone sought after.
                By being victorious in the grail war you will be awarded with the all powerful grail.
                
                Or at least something close to it as this one isn't able to grant any wish you desire.
                Upon use you may choose between multiple possible powerful rewards instead.""");
        this.add("fateubw.book.category.loot", "Loot");
        this.add("fateubw.book.category.loot.desc", """
                The grail is able to provide you with multiple possible loot.
                Through various wars fought these loot types seem to be the only ones the grail is able to grant.""");
        this.add("fateubw.book.entry.loot.item", "Item Loot");
        this.add("fateubw.book.entry.loot.item.1", "Materialistic desire is often a target of wishes. The grail can provide you with various items to fullfil that desire.");
        this.add("fateubw.book.entry.loot.attribute", "Attributes");
        this.add("fateubw.book.entry.loot.attribute.1", "If you want to get permanently stronger the grail can grant this wish to you through increases in various attributes.");
        this.add("fateubw.book.entry.loot.servant", "Servant");
        this.add("fateubw.book.entry.loot.servant.1", """
                If you desire the bonds you forged with your servants during battle you might wish for them to remain in this world for longer. The grail can resummon your servant you had in the grailwar.
                Or only their weapons if thats all you want...""");
        this.add("fateubw.book.entry.loot.commands", "Commands");
        this.add("fateubw.book.entry.loot.commands.1", "The grail is powerful enough to run commands.");
        this.add("fateubw.book.entry.loot.xp", "XP");
        this.add("fateubw.book.entry.loot.xp.1", "If you want more knowledge the grail can give this to your via experience levels.");
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
