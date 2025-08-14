package io.github.flemmli97.fateubw.neoforge.data;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.lib.FateTags;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import net.favouriteless.modopedia.common.init.MDataComponents;
import net.favouriteless.modopedia.common.init.MItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class RecipesGen extends RecipeProvider {

    public RecipesGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, FateItems.MANA_GEM.get())
                .requires(FateItems.CRYSTAL_RED.get())
                .requires(FateItems.CRYSTAL_BLACK.get())
                .requires(FateItems.CRYSTAL_BLUE.get())
                .requires(FateItems.CRYSTAL_GREEN.get())
                .requires(FateItems.CRYSTAL_YELLOW.get())
                .unlockedBy("dummy", trigger()).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FateItems.ALTAR.get())
                .define('T', net.minecraft.world.item.Items.TORCH)
                .define('R', net.minecraft.world.item.Items.RED_WOOL)
                .define('L', Tags.Items.STORAGE_BLOCKS_LAPIS)
                .define('G', FateItems.MANA_GEM.get())
                .define('D', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                .pattern("RTR").pattern("GDG").pattern("LGL")
                .unlockedBy("dummy", trigger()).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FateItems.CHALK.get())
                .define('B', Tags.Items.DYES)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('C', FateTags.Items.CRYSTALS)
                .pattern("  B").pattern(" S ").pattern("C  ")
                .unlockedBy("dummy", trigger()).save(output);

        ItemStack book = new ItemStack(MItems.BOOK.get());
        book.set(MDataComponents.BOOK.get(), Fate.modRes("fateubw_book"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, book)
                .requires(Ingredient.of(FateTags.Items.CRYSTALS))
                .requires(Ingredient.of(Items.BOOK))
                .unlockedBy("book", has(Items.BOOK))
                .save(output, Fate.MODID + ":fateubw_book");
    }

    protected static Criterion<?> trigger() {
        return CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance());
    }
}
