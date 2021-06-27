package io.github.flemmli97.fate.data;

import io.github.flemmli97.fate.Fate;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Fate.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataEvent {

    @SubscribeEvent
    public static void data(GatherDataEvent event) {
        DataGenerator data = event.getGenerator();
        if (event.includeClient()) {
            data.addProvider(new BlockStates(data, event.getExistingFileHelper()));
            data.addProvider(new ItemModels(data, event.getExistingFileHelper()));
            data.addProvider(new Lang(data, event.getExistingFileHelper()));
        }
        if (event.includeServer()) {
            data.addProvider(new Loottables(data));
            BlockTagGen blocks = new BlockTagGen(data, event.getExistingFileHelper());
            data.addProvider(blocks);
            data.addProvider(new ItemTagGen(data, blocks, event.getExistingFileHelper()));
            data.addProvider(new RecipesGen(data));
            data.addProvider(new GrailLoottables(data, Fate.MODID));

        }
    }

}
