package io.github.flemmli97.fateubw.client.particles;

import io.github.flemmli97.tenshilib.client.particles.SpritedParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;

public class LightningParticle extends SpritedParticle {

    public LightningParticle(ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ,
                             SpriteSet sprite) {
        super(level, x, y, z, motionX, motionY, motionZ, sprite, ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT);
        this.roll = level.getRandom().nextFloat() * 360 * Mth.DEG_TO_RAD;
        this.oRoll = this.roll;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return LightTexture.FULL_BRIGHT;
    }

    public static class Factory<T extends ParticleOptions> implements ParticleProvider<T> {

        private final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(T data, ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ) {
            return new LightningParticle(level, x, y, z, motionX, motionY, motionZ, this.sprite);
        }
    }
}
