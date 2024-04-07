package io.github.flemmli97.fateubw.forge.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.lib.FateTags;
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
        ShapelessRecipeBuilder.shapeless(ModItems.CRYSTAL_CLUSTER.get())
                .requires(ModItems.CRYSTAL_RED.get())
                .requires(ModItems.CRYSTAL_BLACK.get())
                .requires(ModItems.CRYSTAL_BLUE.get())
                .requires(ModItems.CRYSTAL_GREEN.get())
                .requires(ModItems.CRYSTAL_YELLOW.get())
                .unlockedBy("dummy", new ImpossibleTrigger.TriggerInstance()).save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.ALTAR.get())
                .define('T', Items.TORCH)
                .define('R', Items.RED_WOOL)
                .define('L', FateTags.FABRIC_LAPIS_BLOCK)
                .define('G', ModItems.CRYSTAL_CLUSTER.get())
                .define('D', FateTags.FABRIC_DIAMOND_BLOCK)
                .pattern("RTR").pattern("GDG").pattern("LGL")
                .unlockedBy("dummy", new ImpossibleTrigger.TriggerInstance()).save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.CHALK.get())
                .define('B', FateTags.FABRIC_DYE_TAG)
                .define('S', FateTags.FABRIC_STICK_TAG)
                .define('C', FateTags.CRYSTALS)
                .pattern("  B").pattern(" S ").pattern("C  ")
                .unlockedBy("dummy", new ImpossibleTrigger.TriggerInstance()).save(consumer);
        consumer.accept(new FinishedRecipe() {
            @Override
            public void serializeRecipeData(JsonObject json) {
                JsonArray jsonArray = new JsonArray();
                List<Ingredient> ings = List.of(Ingredient.of(FateTags.CRYSTALS), Ingredient.of(Items.BOOK));

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
