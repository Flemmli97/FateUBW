package io.github.flemmli97.fateubw.forge.data;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.registry.ModSounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class SoundGen extends SoundDefinitionsProvider {

    public SoundGen(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, Fate.MODID, helper);
    }

    @Override
    public void registerSounds() {
        this.add(ModSounds.ENTITY_BABYLON_SPAWN.get(), fromEvent(SoundEvents.BEACON_ACTIVATE.getLocation()));
        this.add(ModSounds.ENTITY_BABYLON_SHOOT.get(), fromEvent(SoundEvents.PLAYER_ATTACK_SWEEP.getLocation()), 7, false);
        this.add(ModSounds.ENTITY_EXCALIBUR_SHOOT.get(), new ResourceLocation("random/explode"), 4, false);
        this.add(ModSounds.ENTITY_EA_SHOOT.get(), new ResourceLocation("random/explode"), 4, false);
        this.add(ModSounds.HERACLES_ROAR.get());
    }

    private void add(SoundEvent event) {
        this.add(event, new ResourceLocation(event.getLocation().getNamespace(), event.getLocation().getPath().replace(".", "/")));
    }

    private void add(SoundEvent event, ResourceLocation sound) {
        this.add(event, definition().subtitle(event.getLocation().toString()).with(SoundDefinition.Sound.sound(sound, SoundDefinition.SoundType.SOUND)));
    }

    private void add(SoundEvent event, ResourceLocation sound, int num, boolean underscore) {
        SoundDefinition def = definition().subtitle(event.getLocation().toString());
        for (int i = 0; i < num; i++)
            def.with(SoundDefinition.Sound.sound(new ResourceLocation(sound.getNamespace(), sound.getPath() + (underscore ? "_" : "") + (i + 1)), SoundDefinition.SoundType.SOUND));
        this.add(event, def);
    }

    private static ResourceLocation fromEvent(ResourceLocation res) {
        return new ResourceLocation(res.getNamespace(), res.getPath().replace(".", "/"));
    }
}
