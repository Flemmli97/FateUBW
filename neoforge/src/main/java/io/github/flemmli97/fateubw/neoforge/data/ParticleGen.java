package io.github.flemmli97.fateubw.neoforge.data;

import io.github.flemmli97.fateubw.common.registry.FateParticles;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;

public class ParticleGen extends ParticleDescriptionProvider {

    public ParticleGen(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, fileHelper);
    }

    @Override
    public void addDescriptions() {
        this.spriteSet(FateParticles.LIGHT.get());
        this.spriteSet(FateParticles.TRAIL.get(), 5);
        this.spriteSet(FateParticles.RING.get());
        this.spriteSet(FateParticles.FLASH.get());
        this.spriteSet(FateParticles.MAGIC_CIRCLE_1.get());
        this.spriteSet(FateParticles.MAGIC_CIRCLE_2.get());
        this.spriteSet(FateParticles.GLOWING_RING.get());
        this.spriteSet(FateParticles.ROSE_PETAL.get(), 6);
    }

    public void spriteSet(ParticleType<?> type) {
        this.spriteSet(type, BuiltInRegistries.PARTICLE_TYPE.getKey(type));
    }

    public void spriteSet(ParticleType<?> type, int num) {
        this.spriteSet(type, BuiltInRegistries.PARTICLE_TYPE.getKey(type), num, false);
    }
}
