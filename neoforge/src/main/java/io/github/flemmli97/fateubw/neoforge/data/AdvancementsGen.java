package io.github.flemmli97.fateubw.neoforge.data;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.advancements.DataComponentPresentPredicate;
import io.github.flemmli97.fateubw.common.registry.FateCriterionTriggers;
import io.github.flemmli97.fateubw.common.registry.FateDataComponents;
import io.github.flemmli97.fateubw.common.registry.FateItemSubPredicates;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AdvancementsGen extends AdvancementProvider {

    public AdvancementsGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new RunecraftoryAdvancements()));
    }

    public static class RunecraftoryAdvancements implements AdvancementGenerator {

        @Override
        public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> cons, ExistingFileHelper fileHelper) {
            AdvancementHolder root = Advancement.Builder.advancement().display(FateItems.ICON_0.get(), Component.translatable("fateubw.advancements.title"), Component.translatable("fateubw.advancements.description"), ResourceLocation.withDefaultNamespace("textures/gui/advancements/backgrounds/stone.png"), AdvancementType.TASK, true, true, false)
                    .addCriterion("gem_fire", InventoryChangeTrigger.TriggerInstance.hasItems(FateItems.CRYSTAL_RED.get()))
                    .addCriterion("gem_water", InventoryChangeTrigger.TriggerInstance.hasItems(FateItems.CRYSTAL_BLUE.get()))
                    .addCriterion("gem_earth", InventoryChangeTrigger.TriggerInstance.hasItems(FateItems.CRYSTAL_YELLOW.get()))
                    .addCriterion("gem_wind", InventoryChangeTrigger.TriggerInstance.hasItems(FateItems.CRYSTAL_GREEN.get()))
                    .addCriterion("gem_void", InventoryChangeTrigger.TriggerInstance.hasItems(FateItems.CRYSTAL_BLACK.get()))
                    .requirements(AdvancementRequirements.Strategy.OR).save(cons, Fate.MODID + ":root");
            AdvancementHolder artifact = Advancement.Builder.advancement().parent(root).display(FateItems.ARTIFACT_SABER.get(), Component.translatable("fateubw.advancements.artifact.title"), Component.translatable("fateubw.advancements.artifact.description"), null, AdvancementType.TASK, true, true, true)
                    .addCriterion("artifact", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item()
                            .withSubPredicate(FateItemSubPredicates.COMPONENT_PRESENT.get(), DataComponentPresentPredicate.builder().expect(FateDataComponents.CLASS_RELIC.get()).build()))).save(cons, Fate.MODID + ":artifact");
            AdvancementHolder summon = Advancement.Builder.advancement().parent(root).display(FateItems.ALTAR.get(), Component.translatable("fateubw.advancements.join.title"), Component.translatable("fateubw.advancements.join.description"), null, AdvancementType.TASK, true, false, true)
                    .addCriterion("join", FateCriterionTriggers.JOIN_GRAIL_WAR.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))).save(cons, Fate.MODID + ":summon");
            AdvancementHolder win = Advancement.Builder.advancement().parent(summon).display(FateItems.GRAIL.get(), Component.translatable("fateubw.advancements.win.title"), Component.translatable("fateubw.advancements.win.description"), null, AdvancementType.CHALLENGE, true, true, true)
                    .addCriterion("win", FateCriterionTriggers.WIN_GRAIL_WAR.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))).save(cons, Fate.MODID + ":win");
        }
    }
}
