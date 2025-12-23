package io.github.flemmli97.fateubw.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.flemmli97.fateubw.common.particles.StaticFacingParticleData;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import org.joml.Quaternionf;

public class MagicCircleParticle extends TextureSheetParticle {

    private final float yRot, xRot;

    private float tempAlpha;

    public MagicCircleParticle(ClientLevel level, double x, double y, double z, StaticFacingParticleData data, SpriteSet sprite) {
        super(level, x, y, z);
        this.yRot = data.rotY();
        this.xRot = data.rotX();
        this.lifetime = 10;
        this.setSpriteFromAge(sprite);
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        Quaternionf quaternion = new Quaternionf(0.0F, 0.0F, 0.0F, 1.0F);
        quaternion.mul(Axis.YP.rotationDegrees(-this.yRot));
        quaternion.mul(Axis.XP.rotationDegrees(this.xRot));
        this.renderRotatedQuad(buffer, renderInfo, quaternion, partialTicks);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<StaticFacingParticleData> {

        private final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(StaticFacingParticleData data, ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ) {
            return new MagicCircleParticle(level, x, y, z, data, this.sprite);
        }
    }
}
