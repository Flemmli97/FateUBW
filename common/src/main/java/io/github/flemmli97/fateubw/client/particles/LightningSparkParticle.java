package io.github.flemmli97.fateubw.client.particles;

import io.github.flemmli97.tenshilib.client.particles.SpritedParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;

public class LightningSparkParticle extends SpritedParticle {

    public static final int TYPES = 2;

    private final int type;

    public LightningSparkParticle(ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ,
                                  SpriteSet sprite) {
        super(level, x, y, z, motionX, motionY, motionZ, sprite, ParticleRenderTypes.COLORIZE_PARTICLE);
        this.roll = level.getRandom().nextFloat() * 360 * Mth.DEG_TO_RAD;
        this.type = level.getRandom().nextInt(TYPES);
        this.oRoll = this.roll;
    }

    @Override
    public void setSpriteFromAge(SpriteSet sprite) {
        if (!this.removed) {
            this.setSprite(sprite.get(this.age + (this.lifetime * this.type), this.lifetime * TYPES));
        }
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
            return new LightningSparkParticle(level, x, y, z, motionX, motionY, motionZ, this.sprite);
        }
    }
}
