package io.github.flemmli97.fateubw.forge.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.registry.ModBlocks;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import xyz.brassgoggledcoders.patchouliprovider.AbstractPageBuilder;
import xyz.brassgoggledcoders.patchouliprovider.BookBuilder;
import xyz.brassgoggledcoders.patchouliprovider.EntryBuilder;
import xyz.brassgoggledcoders.patchouliprovider.PatchouliBookProvider;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PatchouliGen extends PatchouliBookProvider {

    public PatchouliGen(DataGenerator gen) {
        super(gen, Fate.MODID, "en_us");
    }

    @Override
    protected void addBooks(Consumer<BookBuilder> consumer) {
        EntryBuilder builder;
        consumer.accept((builder = this.createBookBuilder("fate_book", "fate_book", "fate.patchouli.landing")
                .setCreativeTab(Fate.TAB.getRecipeFolderName())
                .setVersion("1.0")
                .setI18n(true)
                .setShowProgress(false)
                .addCategory("category.start", "fate.patchouli.category.start", "fate.patchouli.category.start.desc", new ItemStack(ModItems.crystalCluster.get()))
                .setSortnum(0)
                .addEntry("entry.ores", "fate.patchouli.entry.ores", ModBlocks.gemOre.get().getRegistryName().toString())
                .setSortnum(0)
                .addSpotlightPage(new ItemStack(ModBlocks.gemOre.get()))
                .setText("fate.patchouli.entry.ores." + ModBlocks.gemOre.getID().getPath())
                .build()
                .addSpotlightPage(new ItemStack(ModBlocks.artifactOre.get()))
                .setText("fate.patchouli.entry.ores." + ModBlocks.artifactOre.getID().getPath())
                .build()
                .build()
                .addEntry("entry.altar", "fate.patchouli.entry.altar", ModBlocks.altar.get().getRegistryName().toString()))
                .setSortnum(1)
                .addCraftingPage(new ResourceLocation(Fate.MODID, "altar"))
                .setText("fate.patchouli.entry.altar.1")
                .build()
                .addPage(new MultiBlockPage("fate.patchouli.entry.altar", true, builder))
                .addPattern("CCCCC", "CCCCC", "CC0CC", "CCCCC", "CCCCC")
                .addMapping("C", ModBlocks.chalk.getID().toString())
                .addMapping("0", ModBlocks.altar.getID().toString())
                .setText("fate.patchouli.entry.altar.2")
                .build()
                .addSimpleTextPage("fate.patchouli.entry.altar.3")
                .build()
                .build()
                .addCategory("category.war", "fate.patchouli.category.war", "fate.patchouli.category.war.desc", new ItemStack(ModItems.grail.get()))
                .setSortnum(1)
                .addEntry("entry.servant", "fate.patchouli.entry.servant", ModItems.charmNone.get().getRegistryName().toString())
                .addSimpleTextPage("fate.patchouli.entry.servant.1")
                .addSimpleTextPage("fate.patchouli.entry.servant.2")
                .build()
                .addEntry("entry.grail", "fate.patchouli.entry.grail", ModItems.grail.get().getRegistryName().toString())
                .addSpotlightPage(new ItemStack(ModItems.grail.get()))
                .setText("fate.patchouli.entry.grail.1")
                .build()
                .build()
                .build()
                .addCategory("category.loot", "fate.patchouli.category.loot", "fate.patchouli.category.loot.desc", new ItemStack(Items.STICK))
                .setSortnum(2)
                .addEntry("entry.item", "fate.patchouli.entry.item", Items.STICK.getRegistryName().toString())
                .addSimpleTextPage("fate.patchouli.entry.item.1")
                .build()
                .addEntry("entry.attribute", "fate.patchouli.entry.attribute", Items.IRON_SWORD.getRegistryName().toString())
                .addSimpleTextPage("fate.patchouli.entry.attribute.1")
                .build()
                .addEntry("entry.loot.servant", "fate.patchouli.entry.loot.servant", Items.BARRIER.getRegistryName().toString())
                .addSimpleTextPage("fate.patchouli.entry.loot.servant.1")
                .build()
                .addEntry("entry.xp", "fate.patchouli.entry.xp", Items.EXPERIENCE_BOTTLE.getRegistryName().toString())
                .addSimpleTextPage("fate.patchouli.entry.xp.1")
                .build()
                //.addEntry("entry.astral", "fate.patchouli.entry.astral", Items.BARRIER.getRegistryName().toString())
                //.addSimpleTextPage("fate.patchouli.entry.astral.1")
                //.build()
                .build()
        );
    }

    /**
     * Simple builder. No multiblock verification etc.
     */
    static class MultiBlockPage extends AbstractPageBuilder<MultiBlockPage> {

        private final String name;
        private String text = "";

        private final List<String[]> pattern = new ArrayList<>();
        private final Map<String, String> mapping = new LinkedHashMap<>();
        private final boolean symmetrical;

        protected MultiBlockPage(String name, boolean symmetrical, EntryBuilder parent) {
            super("patchouli:multiblock", parent);
            this.name = name;
            this.symmetrical = symmetrical;
        }

        @Override
        protected void serialize(JsonObject json) {
            json.addProperty("name", this.name);
            JsonObject multiblock = new JsonObject();
            JsonArray patt = new JsonArray();
            for (String[] ss : this.pattern) {
                JsonArray v = new JsonArray();
                for (String s : ss)
                    v.add(s);
                patt.add(v);
            }
            multiblock.add("pattern", patt);
            JsonObject map = new JsonObject();
            this.mapping.forEach(map::addProperty);
            multiblock.add("mapping", map);
            multiblock.addProperty("symmetrical", this.symmetrical);
            json.add("multiblock", multiblock);
            json.addProperty("text", this.text);
        }

        public MultiBlockPage setText(String txt) {
            this.text = txt;
            return this;
        }

        public MultiBlockPage addPattern(String... pattern) {
            this.pattern.add(pattern);
            return this;
        }

        public MultiBlockPage addMapping(String pattern, String state) {
            this.mapping.put(pattern, state);
            return this;
        }
    }
}
