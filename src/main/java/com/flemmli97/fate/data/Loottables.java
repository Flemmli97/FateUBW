package com.flemmli97.fate.data;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.registry.ModBlocks;
import com.flemmli97.fate.common.registry.ModEntities;
import com.flemmli97.fate.common.registry.ModItems;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.StandaloneLootEntry;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.LootingEnchantBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ForgeLootTableProvider;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Loottables extends ForgeLootTableProvider {

    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> loot = ImmutableList.of(Pair.of(EntityLoot::new, LootParameterSets.ENTITY), Pair.of(BlockLoot::new, LootParameterSets.BLOCK));

    public Loottables(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return loot;
    }

    static class EntityLoot implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {

        private final Map<ResourceLocation, LootTable.Builder> lootTables = Maps.newHashMap();

        private void init() {
            this.registerLootTable(ModEntities.arthur.get(), this.getDefault(ModItems.excalibur.get()));
            this.registerLootTable(ModEntities.cuchulainn.get(), this.getDefault(ModItems.gaebolg.get()));
            this.registerLootTable(ModEntities.diarmuid.get(), this.getDefault(ModItems.gaebuidhe.get(), ModItems.gaedearg.get()));
            this.registerLootTable(ModEntities.emiya.get(), this.getDefault(ModItems.archbow.get(), ModItems.kanshou.get(), ModItems.bakuya.get()));
            this.registerLootTable(ModEntities.gilgamesh.get(), this.getDefault(ModItems.enumaelish.get()));
            this.registerLootTable(ModEntities.medea.get(), this.getDefault(ModItems.staff.get(), ModItems.ruleBreaker.get()));
            this.registerLootTable(ModEntities.gilles.get(), this.getDefault(ModItems.grimoire.get()));
            this.registerLootTable(ModEntities.heracles.get(), this.getDefault(ModItems.heraclesAxe.get()));
            this.registerLootTable(ModEntities.lancelot.get(), this.getDefault(ModItems.arondight.get()));
            this.registerLootTable(ModEntities.iskander.get(), this.getDefault(ModItems.kupriots.get()));
            this.registerLootTable(ModEntities.medusa.get(), this.getDefault(ModItems.medusaDagger.get()));
            this.registerLootTable(ModEntities.hassan.get(), this.getDefault(ModItems.assassinDagger.get()));
            this.registerLootTable(ModEntities.sasaki.get(), this.getDefault(ModItems.katana.get()));
        }

        private LootTable.Builder getDefault(IItemProvider... items) {
            LootPool.Builder build = LootPool.builder().rolls(ConstantRange.of(1));
            LootPool.builder().rolls(ConstantRange.of(1));
            for (IItemProvider item : items)
                build.addEntry(ItemLootEntry.builder(item))
                        .acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 0.1F)))
                        .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 0.05F)).func_216072_a(1));
            return LootTable.builder().addLootPool(build);
        }

        protected void registerLootTable(EntityType<?> type, LootTable.Builder builder) {
            this.lootTables.put(type.getLootTable(), builder);
        }

        @Override
        public void accept(BiConsumer<ResourceLocation, LootTable.Builder> cons) {
            this.init();
            this.lootTables.forEach(cons::accept);
        }
    }

    static class BlockLoot extends BlockLootTables {

        private final Map<ResourceLocation, LootTable.Builder> loots = Maps.newHashMap();

        private static final ILootCondition.IBuilder SILK_TOUCH = MatchTool.builder(ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1))));

        @Override
        public void accept(BiConsumer<ResourceLocation, LootTable.Builder> cons) {
            this.registerDropSelfLootTable(ModBlocks.altar.get());
            this.registerLootTable(ModBlocks.charmOre.get(), drop -> droppingWithSilkTouch(drop, ModItems.charmNone.get()));
            ResourceLocation crystal = new ResourceLocation(Fate.MODID, "blocks/crystals");
            this.registerLootTable(crystal, createLootPool(5, ModItems.crystalEarth.get(), ModItems.crystalWind.get(), ModItems.crystalWater.get(), ModItems.crystalVoid.get(), ModItems.crystalFire.get()));
            this.registerLootTable(ModBlocks.crystalOre.get(), drop -> droppingWithSilkTouch(drop, TableLootEntry.builder(crystal)));
            this.loots.forEach(cons::accept);
        }

        protected static LootTable.Builder createLootPool(int weight, IItemProvider... items) {
            LootPool.Builder build = LootPool.builder().rolls(ConstantRange.of(1));
            for (IItemProvider item : items)
                build.addEntry(ore(weight, item));
            return LootTable.builder().addLootPool(build);
        }

        private static StandaloneLootEntry.Builder<?> ore(int weight, IItemProvider item) {
            return ItemLootEntry.builder(item).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 3.0F))).acceptFunction(ApplyBonus.oreDrops(Enchantments.FORTUNE)).weight(weight);
        }

        @Override
        protected void registerLootTable(Block block, Function<Block, LootTable.Builder> function) {
            this.registerLootTable(block, function.apply(block));
        }

        @Override
        protected void registerLootTable(Block block, LootTable.Builder builder) {
            this.loots.put(block.getLootTable(), builder);
        }

        protected void registerLootTable(ResourceLocation s, LootTable.Builder builder) {
            this.loots.put(s, builder);
        }
    }
}
