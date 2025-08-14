//package io.github.flemmli97.fateubw.neoforge.data.book;
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import io.github.flemmli97.fateubw.Fate;
//import io.github.flemmli97.fateubw.common.registry.FateBlocks;
//import io.github.flemmli97.fateubw.common.registry.FateEntities;
//import io.github.flemmli97.fateubw.common.registry.FateItems;
//import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
//import net.minecraft.data.DataGenerator;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import xyz.brassgoggledcoders.patchouliprovider.AbstractPageBuilder;
//import xyz.brassgoggledcoders.patchouliprovider.BookBuilder;
//import xyz.brassgoggledcoders.patchouliprovider.EntryBuilder;
//import xyz.brassgoggledcoders.patchouliprovider.PatchouliBookProvider;
//
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Consumer;
//
//public class PatchouliGen extends PatchouliBookProvider {
//
//    public PatchouliGen(DataGenerator gen) {
//        super(gen, Fate.MODID, "en_us");
//    }
//
//    @Override
//    protected void addBooks(Consumer<BookBuilder> consumer) {
//        EntryBuilder builder;
//        consumer.accept((builder = this.createBookBuilder("fateubw_book", "fateubw_book", "fateubw.patchouli.landing")
//                .setCreativeTab(Fate.TAB.getRecipeFolderName())
//                .setVersion("1.1")
//                .setI18n(true)
//                .setShowProgress(false)
//                .addCategory("category.start", "fateubw.patchouli.category.start", "fateubw.patchouli.category.start.desc", new ItemStack(FateItems.MANA_GEM.get()))
//                .setSortnum(0)
//                .addEntry("entry.ores", "fateubw.patchouli.entry.ores", FateBlocks.GEM_ORE.get().getRegistryName().toString())
//                .setSortnum(0)
//                .addSpotlightPage(new ItemStack(FateBlocks.GEM_ORE.get()))
//                .setText("fateubw.patchouli.entry.ores." + FateBlocks.GEM_ORE.getID().getPath())
//                .build()
//                .addSpotlightPage(new ItemStack(FateBlocks.ARTIFACT_ORE.get()))
//                .setText("fateubw.patchouli.entry.ores." + FateBlocks.ARTIFACT_ORE.getID().getPath())
//                .build()
//                .build()
//                .addEntry("entry.altar", "fateubw.patchouli.entry.altar", FateBlocks.ALTAR.get().getRegistryName().toString()))
//                .setSortnum(1)
//                .addCraftingPage(Fate.modRes("summoning_altar"))
//                .setText("fateubw.patchouli.entry.altar.1")
//                .build()
//                .addPage(new MultiBlockPage("fateubw.patchouli.entry.altar", true, builder))
//                .addPattern("CCCCC", "CCCCC", "CC0CC", "CCCCC", "CCCCC")
//                .addMapping("C", FateBlocks.CHALK.getID().toString())
//                .addMapping("0", FateBlocks.ALTAR.getID().toString())
//                .setText("fateubw.patchouli.entry.altar.2")
//                .build()
//                .addSimpleTextPage("fateubw.patchouli.entry.altar.3")
//                .build()
//                .addEntry("entry.servant", "fateubw.patchouli.entry.servant", FateItems.CHARM_NONE.get().getRegistryName().toString())
//                .setSortnum(2)
//                .addSimpleTextPage("fateubw.patchouli.entry.servant.1")
//                .addSimpleTextPage("fateubw.patchouli.entry.servant.2")
//                .build()
//                .addEntry("entry.grail", "fateubw.patchouli.entry.grail", FateItems.GRAIL.get().getRegistryName().toString())
//                .addSpotlightPage(new ItemStack(FateItems.GRAIL.get()))
//                .setText("fateubw.patchouli.entry.grail.1")
//                .build()
//                .build()
//                .build()
//                .addCategory("category.loot", "fateubw.patchouli.category.loot", "fateubw.patchouli.category.loot.desc", new ItemStack(Items.STICK))
//                .setSortnum(1)
//                .addEntry("entry.item", "fateubw.patchouli.entry.item", Items.STICK.getRegistryName().toString())
//                .addSimpleTextPage("fateubw.patchouli.entry.item.1")
//                .build()
//                .addEntry("entry.attribute", "fateubw.patchouli.entry.attribute", Items.IRON_SWORD.getRegistryName().toString())
//                .addSimpleTextPage("fateubw.patchouli.entry.attribute.1")
//                .build()
//                .addEntry("entry.loot.servant", "fateubw.patchouli.entry.loot.servant", SpawnEgg.fromType(FateEntities.ARTHUR.get()).get().getRegistryName().toString())
//                .addSimpleTextPage("fateubw.patchouli.entry.loot.servant.1")
//                .build()
//                .addEntry("entry.loot.commands", "fateubw.patchouli.entry.commands", Items.COMMAND_BLOCK.getRegistryName().toString())
//                .addSimpleTextPage("fateubw.patchouli.entry.commands.1")
//                .build()
//                .addEntry("entry.xp", "fateubw.patchouli.entry.xp", Items.EXPERIENCE_BOTTLE.getRegistryName().toString())
//                .addSimpleTextPage("fateubw.patchouli.entry.xp.1")
//                .build()
//                .build()
//        );
//    }
//
//    /**
//     * Simple builder. No multiblock verification etc.
//     */
//    static class MultiBlockPage extends AbstractPageBuilder<MultiBlockPage> {
//
//        private final String name;
//        private String text = "";
//
//        private final List<String[]> pattern = new ArrayList<>();
//        private final Map<String, String> mapping = new LinkedHashMap<>();
//        private final boolean symmetrical;
//
//        protected MultiBlockPage(String name, boolean symmetrical, EntryBuilder parent) {
//            super("patchouli:multiblock", parent);
//            this.name = name;
//            this.symmetrical = symmetrical;
//        }
//
//        @Override
//        protected void serialize(JsonObject json) {
//            json.addProperty("name", this.name);
//            JsonObject multiblock = new JsonObject();
//            JsonArray patt = new JsonArray();
//            for (String[] ss : this.pattern) {
//                JsonArray v = new JsonArray();
//                for (String s : ss)
//                    v.add(s);
//                patt.add(v);
//            }
//            multiblock.add("pattern", patt);
//            JsonObject map = new JsonObject();
//            this.mapping.forEach(map::addProperty);
//            multiblock.add("mapping", map);
//            multiblock.addProperty("symmetrical", this.symmetrical);
//            json.add("multiblock", multiblock);
//            json.addProperty("text", this.text);
//        }
//
//        public MultiBlockPage setText(String txt) {
//            this.text = txt;
//            return this;
//        }
//
//        public MultiBlockPage addPattern(String... pattern) {
//            this.pattern.add(pattern);
//            return this;
//        }
//
//        public MultiBlockPage addMapping(String pattern, String state) {
//            this.mapping.put(pattern, state);
//            return this;
//        }
//    }
//}
