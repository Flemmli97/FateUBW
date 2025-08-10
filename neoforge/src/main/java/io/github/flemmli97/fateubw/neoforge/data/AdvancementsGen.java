package io.github.flemmli97.fateubw.neoforge.data;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.advancements.GrailWarTrigger;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class AdvancementsGen implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final DataGenerator generator;
    private final List<Consumer<Consumer<Advancement>>> advancements = new ArrayList<>();

    public AdvancementsGen(DataGenerator generatorIn) {
        this.generator = generatorIn;
    }

    public void add() {
        this.advancements.add(cons -> {
            Advancement root = Advancement.Builder.advancement().display(FateItems.ICON_0.get(), Component.translatable("fateubw.advancements.title"), Component.translatable("fateubw.advancements.description"), new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"), FrameType.TASK, true, true, false)
                    .addCriterion("gem_fire", InventoryChangeTrigger.TriggerInstance.hasItems(FateItems.CRYSTAL_RED.get()))
                    .addCriterion("gem_water", InventoryChangeTrigger.TriggerInstance.hasItems(FateItems.CRYSTAL_BLUE.get()))
                    .addCriterion("gem_earth", InventoryChangeTrigger.TriggerInstance.hasItems(FateItems.CRYSTAL_YELLOW.get()))
                    .addCriterion("gem_wind", InventoryChangeTrigger.TriggerInstance.hasItems(FateItems.CRYSTAL_GREEN.get()))
                    .addCriterion("gem_void", InventoryChangeTrigger.TriggerInstance.hasItems(FateItems.CRYSTAL_BLACK.get()))
                    .requirements(RequirementsStrategy.OR).save(cons, Fate.MODID + ":root");
            Advancement charm = Advancement.Builder.advancement().parent(root).display(FateItems.CHARM_NONE.get(), Component.translatable("fateubw.advancements.charm.title"), Component.translatable("fateubw.advancements.charm.description"), null, FrameType.TASK, true, true, true)
                    .addCriterion("charm", InventoryChangeTrigger.TriggerInstance.hasItems(FateItems.CHARM_NONE.get())).save(cons, Fate.MODID + ":charm");
            Advancement summon = Advancement.Builder.advancement().parent(root).display(FateItems.ALTAR.get(), Component.translatable("fateubw.advancements.join.title"), Component.translatable("fateubw.advancements.join.description"), null, FrameType.TASK, true, false, true)
                    .addCriterion("join", new GrailWarTrigger.TriggerInstance(true, EntityPredicate.Composite.ANY)).save(cons, Fate.MODID + ":summon");
            Advancement win = Advancement.Builder.advancement().parent(summon).display(FateItems.GRAIL.get(), Component.translatable("fateubw.advancements.win.title"), Component.translatable("fateubw.advancements.win.description"), null, FrameType.CHALLENGE, true, true, true)
                    .addCriterion("win", new GrailWarTrigger.TriggerInstance(false, EntityPredicate.Composite.ANY)).save(cons, Fate.MODID + ":win");
        });
    }

    @Override
    public void run(HashCache cache) {
        this.advancements.clear();
        this.add();
        Path path = this.generator.getOutputFolder();
        Set<ResourceLocation> set = Sets.newHashSet();
        Consumer<Advancement> consumer = (advancement) -> {
            if (!set.add(advancement.getId())) {
                throw new IllegalStateException("Duplicate advancement " + advancement.getId());
            } else {
                Path path1 = getPath(path, advancement);

                try {
                    DataProvider.save(GSON, cache, advancement.deconstruct().serializeToJson(), path1);
                } catch (IOException ioexception) {
                    Fate.LOGGER.error("Couldn't save advancement {}", path1, ioexception);
                }
            }
        };

        for (Consumer<Consumer<Advancement>> consumer1 : this.advancements) {
            consumer1.accept(consumer);
        }

    }

    private static Path getPath(Path pathIn, Advancement advancementIn) {
        return pathIn.resolve("data/" + advancementIn.getId().getNamespace() + "/advancements/" + advancementIn.getId().getPath() + ".json");
    }

    @Override
    public String getName() {
        return "Advancements";
    }
}
