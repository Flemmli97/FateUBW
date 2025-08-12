package io.github.flemmli97.fateubw.neoforge.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.lib.FateTags;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.common.Tags;
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
        ShapelessRecipeBuilder.shapeless(FateItems.MANA_GEM.get())
                .requires(FateItems.CRYSTAL_RED.get())
                .requires(FateItems.CRYSTAL_BLACK.get())
                .requires(FateItems.CRYSTAL_BLUE.get())
                .requires(FateItems.CRYSTAL_GREEN.get())
                .requires(FateItems.CRYSTAL_YELLOW.get())
                .unlockedBy("dummy", new ImpossibleTrigger.TriggerInstance()).save(consumer);
        ShapedRecipeBuilder.shaped(FateItems.ALTAR.get())
                .define('T', net.minecraft.world.item.Items.TORCH)
                .define('R', net.minecraft.world.item.Items.RED_WOOL)
                .define('L', Tags.Items.STORAGE_BLOCKS_LAPIS)
                .define('G', FateItems.MANA_GEM.get())
                .define('D', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                .pattern("RTR").pattern("GDG").pattern("LGL")
                .unlockedBy("dummy", new ImpossibleTrigger.TriggerInstance()).save(consumer);
        ShapedRecipeBuilder.shaped(FateItems.CHALK.get())
                .define('B', Tags.Items.DYES)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('C', FateTags.Items.CRYSTALS)
                .pattern("  B").pattern(" S ").pattern("C  ")
                .unlockedBy("dummy", new ImpossibleTrigger.TriggerInstance()).save(consumer);

        consumer.accept(new FinishedRecipe() {
            @Override
            public void serializeRecipeData(JsonObject json) {
                JsonArray jsonArray = new JsonArray();
                List<Ingredient> ings = List.of(Ingredient.of(FateTags.Items.CRYSTALS), Ingredient.of(net.minecraft.world.item.Items.BOOK));

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
