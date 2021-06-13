package io.github.flemmli97.fate.data;

import io.github.flemmli97.fate.common.registry.FateTags;
import io.github.flemmli97.fate.common.registry.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ForgeRecipeProvider;

import java.util.function.Consumer;

public class RecipesGen extends ForgeRecipeProvider {

    public RecipesGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.crystalCluster.get())
                .addIngredient(ModItems.crystalFire.get())
                .addIngredient(ModItems.crystalVoid.get())
                .addIngredient(ModItems.crystalWater.get())
                .addIngredient(ModItems.crystalWind.get())
                .addIngredient(ModItems.crystalEarth.get())
                .addCriterion("has_crystal", hasItem(FateTags.crystals)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModItems.altar.get())
                .key('T', Items.TORCH)
                .key('R', Items.RED_WOOL)
                .key('L', Tags.Items.STORAGE_BLOCKS_LAPIS)
                .key('G', ModItems.crystalCluster.get())
                .key('D', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                .patternLine("RTR").patternLine("GDG").patternLine("LGL")
                .addCriterion("has_crystal", hasItem(ModItems.crystalCluster.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModItems.chalk.get())
                .key('B', Tags.Items.DYES)
                .key('S', Tags.Items.RODS_WOODEN)
                .key('C', FateTags.crystals)
                .patternLine("  B").patternLine(" S ").patternLine("C  ")
                .addCriterion("has_crystal", hasItem(ModItems.crystalCluster.get())).build(consumer);
    }
}
