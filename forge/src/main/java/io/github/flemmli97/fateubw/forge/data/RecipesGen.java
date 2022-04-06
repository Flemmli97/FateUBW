package io.github.flemmli97.fateubw.forge.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.registry.FateTags;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.common.recipe.ShapelessBookRecipe;

import java.util.List;
import java.util.function.Consumer;

public class RecipesGen extends RecipeProvider {

    public RecipesGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(ModItems.crystalCluster.get())
                .requires(ModItems.crystalFire.get())
                .requires(ModItems.crystalVoid.get())
                .requires(ModItems.crystalWater.get())
                .requires(ModItems.crystalWind.get())
                .requires(ModItems.crystalEarth.get())
                .unlockedBy("dummy", new ImpossibleTrigger.TriggerInstance()).save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.altar.get())
                .define('T', Items.TORCH)
                .define('R', Items.RED_WOOL)
                .define('L', FateTags.fabricLapisBlock)
                .define('G', ModItems.crystalCluster.get())
                .define('D', FateTags.fabricDiamondBlock)
                .pattern("RTR").pattern("GDG").pattern("LGL")
                .unlockedBy("dummy", new ImpossibleTrigger.TriggerInstance()).save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.chalk.get())
                .define('B', FateTags.fabricDyeTag)
                .define('S', FateTags.fabricStickTag)
                .define('C', FateTags.crystals)
                .pattern("  B").pattern(" S ").pattern("C  ")
                .unlockedBy("dummy", new ImpossibleTrigger.TriggerInstance()).save(consumer);
        consumer.accept(new FinishedRecipe() {
            @Override
            public void serializeRecipeData(JsonObject json) {
                JsonArray jsonArray = new JsonArray();
                List<Ingredient> ings = List.of(Ingredient.of(FateTags.crystals), Ingredient.of(Items.BOOK));

                for (Ingredient ing : ings)
                    jsonArray.add(ing.toJson());

                json.add("ingredients", jsonArray);
                json.addProperty("book", new ResourceLocation(Fate.MODID, "fate_book").toString());
            }

            @Override
            public ResourceLocation getId() {
                return new ResourceLocation(Fate.MODID, "guide");
            }

            @Override
            public RecipeSerializer<?> getType() {
                return ShapelessBookRecipe.SERIALIZER;
            }

            @Nullable
            @Override
            public JsonObject serializeAdvancement() {
                return null;
            }

            @Nullable
            @Override
            public ResourceLocation getAdvancementId() {
                return null;
            }
        });
    }
}
