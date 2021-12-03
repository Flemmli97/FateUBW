package io.github.flemmli97.fate.data;

import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.common.registry.ModBlocks;
import io.github.flemmli97.fate.common.registry.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.patchouliprovider.BookBuilder;
import xyz.brassgoggledcoders.patchouliprovider.PatchouliBookProvider;

import java.util.function.Consumer;

public class PatchouliGen extends PatchouliBookProvider {

    public PatchouliGen(DataGenerator gen) {
        super(gen, Fate.MODID, "en_us");
    }

    @Override
    protected void addBooks(Consumer<BookBuilder> consumer) {
        consumer.accept(this.createBookBuilder("fate_guidebook", "fate_guidebook", "fate.patchouli.landing")
                .setCreativeTab(Fate.TAB.getPath())
                .setVersion("1.0")
                .setI18n(true)
                .setShowProgress(false)
            .addCategory("category.start", "fate.patchouli.category.start", "fate.patchouli.category.start.desc", new ItemStack(ModItems.crystalCluster.get()))
                .addEntry("entry.ores", "fate.patchouli.entry.ores", ModBlocks.crystalOre.get().getRegistryName().toString())
                    .addSpotlightPage(new ItemStack(ModBlocks.crystalOre.get()))
                        .setText("fate.patchouli.entry.ores.crystal")
                        .build()
                    .addSpotlightPage(new ItemStack(ModBlocks.charmOre.get()))
                        .setText("fate.patchouli.entry.ores.charm")
                        .build()
                    .setSortnum(0)
                    .build()
                .addEntry("entry.altar", "fate.patchouli.entry.altar", ModBlocks.altar.get().getRegistryName().toString())
                    .addCraftingPage(new ResourceLocation(Fate.MODID, "altar"))
                        .setText("fate.patchouli.entry.altar")
                        .build()
                    .addImagePage(new ResourceLocation(Fate.MODID, "textures/img/altar.png"))
                        .setText("fate.patchouli.entry.altar.struct")
                        .build()
                    .addSimpleTextPage("fate.patchouli.entry.altar.charm")
                    .setSortnum(1)
                    .build()
                .setSortnum(0)
                .build()
            .addCategory("category.war", "fate.patchouli.category.war", "fate.patchouli.category.war.desc", new ItemStack(ModItems.grail.get()))
                .addEntry("entry.servant", "fate.patchouli.entry.servant", ModItems.charmNone.get().getRegistryName().toString())
                    .addSimpleTextPage("fate.patchouli.entry.servant.desc")
                    .setSortnum(0)
                    .build()
                .addEntry("entry.grail", "fate.patchouli.entry.grail", ModItems.grail.get().getRegistryName().toString())
                    .addSpotlightPage(new ItemStack(ModItems.grail.get()))
                    .setText("fate.patchouli.entry.grail.item")
                    .build()
                    .setSortnum(1)
                    .build()
                .setSortnum(2)
                .build()
            .addCategory("category.loot", "fate.patchouli.category.loot", "fate.patchouli.category.loot.desc", new ItemStack(Items.STICK))
                .addEntry("entry.attribute", "fate.patchouli.entry.attribute", Items.STICK.getRegistryName().toString())
                    .addSimpleTextPage("fate.patchouli.entry.attribute.desc")
                    .build()
                .addEntry("entry.astral", "fate.patchouli.entry.servant", Items.STICK.getRegistryName().toString())
                    .addSimpleTextPage("fate.patchouli.entry.servant.desc")
                    .build()
                .addEntry("entry.xp", "fate.patchouli.entry.xp", Items.STICK.getRegistryName().toString())
                    .addSimpleTextPage("fate.patchouli.entry.xp.desc")
                    .build()
                .addEntry("entry.astral", "fate.patchouli.entry.astral", Items.STICK.getRegistryName().toString())
                    .addSimpleTextPage("fate.patchouli.entry.astral.desc")
                    .build()
                .build()
            );
    }
}
