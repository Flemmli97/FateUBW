package io.github.flemmli97.fateubw.neoforge.data;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.registry.FateSounds;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class SoundGen extends SoundDefinitionsProvider {

    public SoundGen(PackOutput packOutput, ExistingFileHelper helper) {
        super(packOutput, Fate.MODID, helper);
    }

    @Override
    public void registerSounds() {
        for (RegistryEntrySupplier<SoundEvent, ?> sup : FateSounds.SOUND_EVENTS.getEntries()) {
            FateSounds.SoundHolder data = FateSounds.SOUND_DATA.get(sup.getID());
            if (data != null) {
                this.add(sup.get(), data);
            } else {
                this.add(sup.get());
            }
        }
    }

    private void add(SoundEvent event) {
        ResourceLocation path = event.getLocation();
        SoundDefinition def = definition().subtitle(event.getLocation().toString());
        def.with(SoundDefinition.Sound.sound(ResourceLocation.fromNamespaceAndPath(path.getNamespace(), path.getPath().replace(".", "/")), SoundDefinition.SoundType.SOUND));
        this.add(event, def);
    }

    private void add(SoundEvent event, FateSounds.SoundHolder data) {
        SoundDefinition def = definition().subtitle(event.getLocation().toString());
        for (ResourceLocation loc : data.locations()) {
            def.with(SoundDefinition.Sound.sound(ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), loc.getPath().replace(".", "/")), SoundDefinition.SoundType.SOUND)
                    .pitch(data.pitch()).volume(data.volume()));
        }
        this.add(event, def);
    }
}
