package io.github.flemmli97.fateubw.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.tenshilib.client.particles.SpritedParticle;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class LightningParticle extends SpritedParticle {

    public LightningParticle(ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ,
                             SpriteSet sprite) {
        super(level, x, y, z, motionX, motionY, motionZ, sprite, ParticleRenderTypes.COLORIZE_PARTICLE);
    }

    @Override
    public SingleQuadParticle.FacingCameraMode getFacingCameraMode() {
        return FacingCameraMode.LOOKAT_Y;
    }

    /**
     * Requires override cause... sodium breaks this otherwise since they do their own quad rendering
     */
    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        Quaternionf quaternionf = new Quaternionf();
        this.getFacingCameraMode().setRotation(quaternionf, renderInfo, partialTicks);
        if (this.roll != 0.0F) {
            quaternionf.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));
        }
        this.renderRotatedQuad(buffer, renderInfo, quaternionf, partialTicks);
    }

    /**
     * Requires override cause... sodium breaks this otherwise since they do their own quad rendering
     */
    @Override
    protected void renderRotatedQuad(VertexConsumer buffer, Camera camera, Quaternionf quaternion, float partialTicks) {
        Vec3 vec3 = camera.getPosition();
        float f = (float)(Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
        float f1 = (float)(Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
        float f2 = (float)(Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());
        this.renderRotatedQuad(buffer, quaternion, f, f1, f2, partialTicks);
    }

    @Override
    protected void renderRotatedQuad(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float partialTicks) {
        float size = this.getQuadSize(partialTicks);
        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int light = this.getLightColor(partialTicks);
        this.renderVertex(buffer, quaternion, x, y, z, 1.0F, 0, size, u1, v1, light);
        this.renderVertex(buffer, quaternion, x, y, z, 1.0F, 6.0F, size, u1, v0, light);
        this.renderVertex(buffer, quaternion, x, y, z, -1.0F, 6.0F, size, u0, v0, light);
        this.renderVertex(buffer, quaternion, x, y, z, -1.0F, 0, size, u0, v1, light);
    }

    private void renderVertex(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float quadSize, float u, float v, int packedLight) {
        Vector3f vector3f = (new Vector3f(xOffset, yOffset, 0.0F)).rotate(quaternion).mul(quadSize).add(x, y, z);
        buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z()).setUv(u, v).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(packedLight);
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
