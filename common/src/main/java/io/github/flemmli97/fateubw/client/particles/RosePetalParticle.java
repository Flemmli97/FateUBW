package io.github.flemmli97.fateubw.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleOptions;

public class RosePetalParticle extends TextureSheetParticle {

    public RosePetalParticle(ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ,
                             SpriteSet sprite) {
        super(level, x, y, z, motionX, motionY, motionZ);
        this.setSprite(sprite.get(this.random.nextInt(6), 6));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Factory<T extends ParticleOptions> implements ParticleProvider<T> {

        private final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(T data, ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ) {
            return new RosePetalParticle(level, x, y, z, motionX, motionY, motionZ, this.sprite);
        }
    }
}
