package io.github.flemmli97.fateubw.neoforge.data;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.registry.FateBlocks;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Loottables extends LootTableProvider {

    public Loottables(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(EntityLoot::new, LootContextParamSets.ENTITY),
                new SubProviderEntry(BlockLoot::new, LootContextParamSets.BLOCK)
        ), registries);
    }

    @Override
    protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector) {
    }

    static class EntityLoot implements LootTableSubProvider {

        protected final Map<ResourceKey<LootTable>, LootTable.Builder> lootTables = new HashMap<>();

        protected final HolderLookup.Provider provider;

        EntityLoot(HolderLookup.Provider provider) {
            this.provider = provider;
        }

        private void init() {
            this.registerLootTable(FateEntities.ARTHUR.get(), this.getDefault(FateItems.EXCALIBUR.get()));
            this.registerLootTable(FateEntities.CUCHULAINN.get(), this.getDefault(FateItems.GAEBOLG.get()));
            this.registerLootTable(FateEntities.DIARMUID.get(), this.getDefault(FateItems.GAEBUIDHE.get(), FateItems.GAEDEARG.get()));
            this.registerLootTable(FateEntities.EMIYA.get(), this.getDefault(FateItems.EMIYAS_BOW.get(), FateItems.KANSHOU.get(), FateItems.BAKUYA.get()));
            this.registerLootTable(FateEntities.GILGAMESH.get(), this.getDefault(FateItems.ENUMAELISH.get()));
            this.registerLootTable(FateEntities.MEDEA.get(), this.getDefault(FateItems.STAFF.get(), FateItems.RULE_BREAKER.get()));
            this.registerLootTable(FateEntities.GILLES.get(), this.getDefault(FateItems.GRIMOIRE.get()));
            this.registerLootTable(FateEntities.HERACLES.get(), this.getDefault(FateItems.HERACLES_AXE.get()));
            this.registerLootTable(FateEntities.LANCELOT.get(), this.getDefault(FateItems.ARONDIGHT.get()));
            this.registerLootTable(FateEntities.ISKANDER.get(), this.getDefault(FateItems.KUPRIOTS.get()));
            this.registerLootTable(FateEntities.MEDUSA.get(), this.getDefault(FateItems.MEDUSA_DAGGER.get()));
            this.registerLootTable(FateEntities.HASSAN.get(), this.getDefault(FateItems.ASSASSIN_DAGGER.get()));
            this.registerLootTable(FateEntities.SASAKI.get(), this.getDefault(FateItems.MONOHOSHI_ZAO.get()));
            this.registerLootTable(FateEntities.NERO.get(), this.getDefault(FateItems.AESTUS_ESTUS.get()));
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
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
            this.init();
            this.lootTables.forEach(output);
        }
    }

    static class BlockLoot extends BlockLootSubProvider {

        private final Map<ResourceKey<LootTable>, LootTable.Builder> loots = new HashMap<>();

        BlockLoot(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlagSet.of(), registries);
        }

        @Override
        protected void generate() {
        }

        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
            this.dropSelf(FateBlocks.ALTAR.get());
            ResourceKey<LootTable> artifacts = ResourceKey.create(Registries.LOOT_TABLE, Fate.modRes("blocks/artifacts"));
            this.registerLootTable(artifacts, this.artifactLoot());
            this.add(FateBlocks.ARTIFACT_ORE.get(), drop -> this.createSilkTouchDispatchTable(drop, this.applyExplosionCondition(drop, NestedLootTable.lootTableReference(artifacts))));
            this.add(FateBlocks.DEEP_SLATE_ARTIFACT_ORE.get(), drop -> this.createSilkTouchDispatchTable(drop, this.applyExplosionCondition(drop, NestedLootTable.lootTableReference(artifacts))));
            ResourceKey<LootTable> crystal = ResourceKey.create(Registries.LOOT_TABLE, Fate.modRes("blocks/crystals"));
            this.registerLootTable(crystal, this.createOreLootPool(5, FateItems.CRYSTAL_YELLOW.get(), FateItems.CRYSTAL_GREEN.get(), FateItems.CRYSTAL_BLUE.get(), FateItems.CRYSTAL_BLACK.get(), FateItems.CRYSTAL_RED.get()));
            this.add(FateBlocks.GEM_ORE.get(), drop -> this.createSilkTouchDispatchTable(drop, NestedLootTable.lootTableReference(crystal)));
            this.add(FateBlocks.DEEP_SLATE_GEM_ORE.get(), drop -> this.createSilkTouchDispatchTable(drop, NestedLootTable.lootTableReference(crystal)));
            this.loots.forEach(output);
        }

        protected LootTable.Builder artifactLoot() {
            LootPool.Builder build = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
            build.add(LootItem.lootTableItem(FateItems.ARTIFACT_SABER.get()));
            build.add(LootItem.lootTableItem(FateItems.ARTIFACT_ARCHER.get()));
            build.add(LootItem.lootTableItem(FateItems.ARTIFACT_LANCER.get()));
            build.add(LootItem.lootTableItem(FateItems.ARTIFACT_CASTER.get()));
            build.add(LootItem.lootTableItem(FateItems.ARTIFACT_BERSERKER.get()));
            build.add(LootItem.lootTableItem(FateItems.ARTIFACT_RIDER.get()));
            build.add(LootItem.lootTableItem(FateItems.ARTIFACT_ASSASSIN.get()));
            return LootTable.lootTable().withPool(build);
        }

        protected LootTable.Builder createOreLootPool(int weight, ItemLike... items) {
            LootPool.Builder build = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
            for (ItemLike item : items)
                build.add(this.ore(weight, item));
            return LootTable.lootTable().withPool(build);
        }

        private LootPoolSingletonContainer.Builder<?> ore(int weight, ItemLike item) {
            return LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                    .apply(ApplyBonusCount.addOreBonusCount(this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE))).setWeight(weight);
        }

        @Override
        public void add(Block block, Function<Block, LootTable.Builder> function) {
            this.add(block, function.apply(block));
        }

        @Override
        public void add(Block block, LootTable.Builder builder) {
            this.loots.put(block.getLootTable(), builder);
        }

        protected void registerLootTable(ResourceKey<LootTable> id, LootTable.Builder builder) {
            this.loots.put(id, builder);
        }
    }
}
