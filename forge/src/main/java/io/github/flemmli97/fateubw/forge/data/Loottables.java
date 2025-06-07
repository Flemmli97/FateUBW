package io.github.flemmli97.fateubw.forge.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.registry.ModBlocks;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Loottables extends LootTableProvider {

    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> loot = ImmutableList.of(Pair.of(EntityLoot::new, LootContextParamSets.ENTITY), Pair.of(BlockLootProvider::new, LootContextParamSets.BLOCK));

    public Loottables(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return this.loot;
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {

    }

    static class EntityLoot implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {

        private final Map<ResourceLocation, LootTable.Builder> lootTables = new HashMap<>();

        private void init() {
            this.registerLootTable(ModEntities.ARTHUR.get(), this.getDefault(ModItems.EXCALIBUR.get()));
            this.registerLootTable(ModEntities.CUCHULAINN.get(), this.getDefault(ModItems.GAEBOLG.get()));
            this.registerLootTable(ModEntities.DIARMUID.get(), this.getDefault(ModItems.GAEBUIDHE.get(), ModItems.GAEDEARG.get()));
            this.registerLootTable(ModEntities.EMIYA.get(), this.getDefault(ModItems.ARCHBOW.get(), ModItems.KANSHOU.get(), ModItems.BAKUYA.get()));
            this.registerLootTable(ModEntities.GILGAMESH.get(), this.getDefault(ModItems.ENUMAELISH.get()));
            this.registerLootTable(ModEntities.MEDEA.get(), this.getDefault(ModItems.STAFF.get(), ModItems.RULE_BREAKER.get()));
            this.registerLootTable(ModEntities.GILLES.get(), this.getDefault(ModItems.GRIMOIRE.get()));
            this.registerLootTable(ModEntities.HERACLES.get(), this.getDefault(ModItems.HERACLES_AXE.get()));
            this.registerLootTable(ModEntities.LANCELOT.get(), this.getDefault(ModItems.ARONDIGHT.get()));
            this.registerLootTable(ModEntities.ISKANDER.get(), this.getDefault(ModItems.KUPRIOTS.get()));
            this.registerLootTable(ModEntities.MEDUSA.get(), this.getDefault(ModItems.MEDUSA_DAGGER.get()));
            this.registerLootTable(ModEntities.HASSAN.get(), this.getDefault(ModItems.ASSASSIN_DAGGER.get()));
            this.registerLootTable(ModEntities.SASAKI.get(), this.getDefault(ModItems.MONOHOSHI_ZAO.get()));
        }

        private LootTable.Builder getDefault(ItemLike... items) {
            LootPool.Builder build = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
            for (ItemLike item : items)
                build.add(LootItem.lootTableItem(item));
            return LootTable.lootTable().withPool(build);
        }

        protected void registerLootTable(EntityType<?> type, LootTable.Builder builder) {
            this.lootTables.put(type.getDefaultLootTable(), builder);
        }

        @Override
        public void accept(BiConsumer<ResourceLocation, LootTable.Builder> cons) {
            this.init();
            this.lootTables.forEach(cons);
        }
    }

    static class BlockLootProvider extends BlockLoot {

        private final Map<ResourceLocation, LootTable.Builder> loots = new HashMap<>();

        private static final LootItemCondition.Builder SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));

        @Override
        public void accept(BiConsumer<ResourceLocation, LootTable.Builder> cons) {
            this.dropSelf(ModBlocks.ALTAR.get());
            this.add(ModBlocks.ARTIFACT_ORE.get(), drop -> createSingleItemTableWithSilkTouch(drop, ModItems.CHARM_NONE.get()));
            this.add(ModBlocks.DEEP_SLATE_ARTIFACT_ORE.get(), drop -> createSingleItemTableWithSilkTouch(drop, ModItems.CHARM_NONE.get()));
            ResourceLocation crystal = new ResourceLocation(Fate.MODID, "blocks/crystals");
            this.registerLootTable(crystal, createLootPool(5, ModItems.CRYSTAL_YELLOW.get(), ModItems.CRYSTAL_GREEN.get(), ModItems.CRYSTAL_BLUE.get(), ModItems.CRYSTAL_BLACK.get(), ModItems.CRYSTAL_RED.get()));
            this.add(ModBlocks.GEM_ORE.get(), drop -> createSilkTouchDispatchTable(drop, LootTableReference.lootTableReference(crystal)));
            this.add(ModBlocks.DEEP_SLATE_GEM_ORE.get(), drop -> createSilkTouchDispatchTable(drop, LootTableReference.lootTableReference(crystal)));
            this.loots.forEach(cons);
        }

        protected static LootTable.Builder createLootPool(int weight, ItemLike... items) {
            LootPool.Builder build = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
            for (ItemLike item : items)
                build.add(ore(weight, item));
            return LootTable.lootTable().withPool(build);
        }

        private static LootPoolSingletonContainer.Builder<?> ore(int weight, ItemLike item) {
            return LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)).setWeight(weight);
        }

        @Override
        public void add(Block block, Function<Block, LootTable.Builder> function) {
            this.add(block, function.apply(block));
        }

        @Override
        public void add(Block block, LootTable.Builder builder) {
            this.loots.put(block.getLootTable(), builder);
        }

        protected void registerLootTable(ResourceLocation s, LootTable.Builder builder) {
            this.loots.put(s, builder);
        }
    }
}
