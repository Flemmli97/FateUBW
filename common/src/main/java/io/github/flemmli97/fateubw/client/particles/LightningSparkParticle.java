package io.github.flemmli97.fateubw.client.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import io.github.flemmli97.tenshilib.client.particles.SpritedParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;

public class LightningSparkParticle extends SpritedParticle {

    public static final int TYPES = 2;

    private final int type;

    public LightningSparkParticle(ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ,
                                  SpriteSet sprite) {
        super(level, x, y, z, motionX, motionY, motionZ, sprite, PARTICLE_LIGHTNING);
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

    public static final ParticleRenderType PARTICLE_LIGHTNING = new ParticleRenderType() {

        @SuppressWarnings("deprecation")
        @Override
        public BufferBuilder begin(Tesselator tesselator, TextureManager manager) {
            RenderSystem.depthMask(true);
            RenderSystem.setShader(FateRenders::getParticleColorizeShaderInstance);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        public String toString() {
            return "PARTICLE_SHEET_TRANSLUCENT";
        }
    };

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
