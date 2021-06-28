package io.github.flemmli97.fate.data;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.common.advancements.GrailWarTrigger;
import io.github.flemmli97.fate.common.registry.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class AdvancementsGen implements IDataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final DataGenerator generator;
    private final List<Consumer<Consumer<Advancement>>> advancements = new ArrayList<>();

    public AdvancementsGen(DataGenerator generatorIn) {
        this.generator = generatorIn;
    }

    public void add() {
        this.advancements.add(cons -> {
            Advancement root = Advancement.Builder.builder().withDisplay(ModItems.icon0.get(), new TranslationTextComponent("advancements.fate.title"), new TranslationTextComponent("advancements.fate.description"), new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"), FrameType.TASK, true, true, false)
                    .withCriterion("gem_fire", InventoryChangeTrigger.Instance.forItems(ModItems.crystalFire.get()))
                    .withCriterion("gem_water", InventoryChangeTrigger.Instance.forItems(ModItems.crystalWater.get()))
                    .withCriterion("gem_earth", InventoryChangeTrigger.Instance.forItems(ModItems.crystalEarth.get()))
                    .withCriterion("gem_wind", InventoryChangeTrigger.Instance.forItems(ModItems.crystalWind.get()))
                    .withCriterion("gem_void", InventoryChangeTrigger.Instance.forItems(ModItems.crystalVoid.get()))
                    .withRequirementsStrategy(IRequirementsStrategy.OR).register(cons, Fate.MODID + ":root");
            Advancement charm = Advancement.Builder.builder().withParent(root).withDisplay(ModItems.charmNone.get(), new TranslationTextComponent("advancements.fate.charm.title"), new TranslationTextComponent("advancements.fate.charm.description"), null, FrameType.TASK, true, true, true)
                    .withCriterion("charm", InventoryChangeTrigger.Instance.forItems(ModItems.charmNone.get())).register(cons, Fate.MODID + ":charm");
            Advancement summon = Advancement.Builder.builder().withParent(root).withDisplay(ModItems.altar.get(), new TranslationTextComponent("advancements.fate.join.title"), new TranslationTextComponent("advancements.fate.join.description"), null, FrameType.TASK, true, true, true)
                    .withCriterion("join", new GrailWarTrigger.Instance(true, EntityPredicate.AndPredicate.ANY_AND)).register(cons, Fate.MODID + ":summon");
            Advancement win = Advancement.Builder.builder().withParent(summon).withDisplay(ModItems.grail.get(), new TranslationTextComponent("advancements.fate.win.title"), new TranslationTextComponent("advancements.fate.win.description"), null, FrameType.CHALLENGE, true, true, true)
                    .withCriterion("win", new GrailWarTrigger.Instance(false, EntityPredicate.AndPredicate.ANY_AND)).register(cons, Fate.MODID + ":win");
        });
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
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
                    IDataProvider.save(GSON, cache, advancement.copy().serialize(), path1);
                } catch (IOException ioexception) {
                    Fate.logger.error("Couldn't save advancement {}", path1, ioexception);
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
