package io.github.flemmli97.fateubw.neoforge.data;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.neoforge.data.tags.BlockTagGen;
import io.github.flemmli97.fateubw.neoforge.data.tags.EntityTagGen;
import io.github.flemmli97.fateubw.neoforge.data.tags.ItemTagGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import java.io.IOException;
import java.util.Collections;

@Mod.EventBusSubscriber(modid = Fate.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataEvent {

    @SubscribeEvent
    public static void data(GatherDataEvent event) {
        DataGenerator data = event.getGenerator();
        if (event.includeClient()) {
            data.addProvider(new BlockStates(data, event.getExistingFileHelper()));
            data.addProvider(new ItemModels(data, event.getExistingFileHelper()));
            data.addProvider(new Lang(data));
            data.addProvider(new ParticleGen(data));
            data.addProvider(new SoundGen(data, new IgnoreFileHelper(event.getExistingFileHelper(), true)));
        }
        if (event.includeServer()) {
            data.addProvider(new Loottables(data));
            BlockTagGen blocks = new BlockTagGen(data, event.getExistingFileHelper());
            data.addProvider(blocks);
            data.addProvider(new ItemTagGen(data, blocks, event.getExistingFileHelper()));
            data.addProvider(new EntityTagGen(data, event.getExistingFileHelper()));
            data.addProvider(new RecipesGen(data));
            data.addProvider(new GrailLoottables(data));
            data.addProvider(new AdvancementsGen(data));
            data.addProvider(new PatchouliGen(data));
            data.addProvider(new EntityPropsGen(data));
        }
    }


    protected static class IgnoreFileHelper extends ExistingFileHelper {

        private final ExistingFileHelper wrapper;
        private final boolean vanillaOnly;

        public IgnoreFileHelper(ExistingFileHelper wrapper, boolean vanillaOnly) {
            super(Collections.emptySet(), Collections.emptySet(), false, null, null);
            this.wrapper = wrapper;
            this.vanillaOnly = vanillaOnly;
        }

        @Override
        public boolean exists(ResourceLocation loc, PackType type, String pathSuffix, String pathPrefix) {
            if (!this.vanillaOnly || loc.getNamespace().equals("minecraft"))
                return true;
            return this.wrapper.exists(loc, type, pathSuffix, pathPrefix);
        }

        @Override
        public Resource getResource(ResourceLocation loc, PackType type, String pathSuffix, String pathPrefix) throws IOException {
            return this.wrapper.getResource(loc, type, pathSuffix, pathPrefix);
        }

        @Override
        public boolean isEnabled() {
            return this.wrapper.isEnabled();
        }
    }
}
