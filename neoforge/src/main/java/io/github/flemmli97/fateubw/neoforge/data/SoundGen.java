package io.github.flemmli97.fateubw.neoforge.data;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.registry.FateSounds;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class SoundGen extends SoundDefinitionsProvider {

    public SoundGen(PackOutput packOutput, ExistingFileHelper helper) {
        super(packOutput, Fate.MODID, helper);
    }

    @Override
    public void registerSounds() {
        this.add(FateSounds.ENTITY_BABYLON_SPAWN.get(), fromEvent(SoundEvents.BEACON_ACTIVATE.getLocation()));
        this.add(FateSounds.ENTITY_BABYLON_SHOOT.get(), fromEvent(SoundEvents.PLAYER_ATTACK_SWEEP.getLocation()), 7, false);
        this.add(FateSounds.ENTITY_EXCALIBUR_SHOOT.get(), ResourceLocation.withDefaultNamespace("random/explode"), 4, false);
        this.add(FateSounds.ENTITY_EA_SHOOT.get(), ResourceLocation.withDefaultNamespace("random/explode"), 4, false);
        this.add(FateSounds.HERACLES_ROAR.get());
    }

    private void add(SoundEvent event) {
        this.add(event, ResourceLocation.fromNamespaceAndPath(event.getLocation().getNamespace(), event.getLocation().getPath().replace(".", "/")));
    }

    private void add(SoundEvent event, ResourceLocation sound) {
        this.add(event, definition().subtitle(event.getLocation().toString()).with(SoundDefinition.Sound.sound(sound, SoundDefinition.SoundType.SOUND)));
    }

    private void add(SoundEvent event, ResourceLocation sound, int num, boolean underscore) {
        SoundDefinition def = definition().subtitle(event.getLocation().toString());
        for (int i = 0; i < num; i++)
            def.with(SoundDefinition.Sound.sound(ResourceLocation.fromNamespaceAndPath(sound.getNamespace(), sound.getPath() + (underscore ? "_" : "") + (i + 1)), SoundDefinition.SoundType.SOUND));
        this.add(event, def);
    }

    private static ResourceLocation fromEvent(ResourceLocation res) {
        return ResourceLocation.fromNamespaceAndPath(res.getNamespace(), res.getPath().replace(".", "/"));
    }
}
