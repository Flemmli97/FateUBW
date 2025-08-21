package io.github.flemmli97.fateubw.neoforge.data;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.neoforge.data.book.BookContentGen;
import io.github.flemmli97.fateubw.neoforge.data.book.BookGen;
import io.github.flemmli97.fateubw.neoforge.data.tags.BlockTagGen;
import io.github.flemmli97.fateubw.neoforge.data.tags.DamageTypeTagGen;
import io.github.flemmli97.fateubw.neoforge.data.tags.EntityTagGen;
import io.github.flemmli97.fateubw.neoforge.data.tags.ItemTagGen;
import io.github.flemmli97.fateubw.neoforge.data.tags.MobEffectTagGen;
import io.github.flemmli97.fateubw.neoforge.data.worldgen.FeatureWorldGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Fate.MODID)
public class DataEvent {

    @SubscribeEvent
    public static void data(GatherDataEvent event) {
        DataGenerator data = event.getGenerator();
        PackOutput output = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();

        FeatureWorldGen.createWorldgenFeatures(event::createDatapackRegistryObjects);

        data.addProvider(true, new AdvancementsGen(output, provider, event.getExistingFileHelper()));
        data.addProvider(true, new BlockStatesGen(output, event.getExistingFileHelper()));
        data.addProvider(true, new DamageTypeGen(output, provider, event.getExistingFileHelper()));
        data.addProvider(true, new EntityPropsGen(output, provider));
        data.addProvider(true, new Loottables(output, provider));
        data.addProvider(true, new GrailLoottables(output, provider));
        data.addProvider(true, new ItemModels(output, event.getExistingFileHelper()));
        data.addProvider(true, new Lang(output));
        data.addProvider(true, new ParticleGen(output, event.getExistingFileHelper()));
        data.addProvider(true, new RecipesGen(output, provider));
        data.addProvider(true, new SoundGen(output, event.getExistingFileHelper()));

        BlockTagGen blocks = new BlockTagGen(output, provider, event.getExistingFileHelper());
        data.addProvider(true, blocks);
        data.addProvider(true, new DamageTypeTagGen(output, provider, event.getExistingFileHelper()));
        data.addProvider(true, new EntityTagGen(output, provider, event.getExistingFileHelper()));
        data.addProvider(true, new ItemTagGen(output, provider, blocks.contentsGetter(), event.getExistingFileHelper()));
        data.addProvider(true, new MobEffectTagGen(output, provider, event.getExistingFileHelper()));

        data.addProvider(true, new BookGen(provider, output));
        data.addProvider(true, new BookContentGen(provider, output));
    }
}
