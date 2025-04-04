package io.github.flemmli97.fateubw.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import io.github.flemmli97.fateubw.common.particles.RingParticleData;
import io.github.flemmli97.tenshilib.client.particles.ColoredParticle;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class RingParticle extends ColoredParticle {

    private final float yRot, xRot, growth;

    public RingParticle(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, RingParticleData data, SpriteSet sprite, int maxAge, float minAgeRand, float maxAgeRand, boolean collide, boolean randomMovements, boolean gravity) {
        super(world, x, y, z, motionX, motionY, motionZ, data, sprite, maxAge, minAgeRand, maxAgeRand, collide, randomMovements, gravity);
        this.yRot = data.getRotY();
        this.xRot = data.getRotX();
        this.growth = data.getGrowth();
        this.lifetime = 6;
    }

    @Override
    public void tick() {
        super.tick();
        this.quadSize += this.growth;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        Quaternion quaternion = new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);
        Vec3 vec3 = renderInfo.getPosition();
        float x = (float) (Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
        float y = (float) (Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
        float z = (float) (Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());

        quaternion.mul(Vector3f.YP.rotationDegrees(-this.yRot));
        quaternion.mul(Vector3f.XP.rotationDegrees(this.xRot));

        Vector3f[] vertices = new Vector3f[]{new Vector3f(-1.0f, -1.0f, 0.0f), new Vector3f(-1.0f, 1.0f, 0.0f), new Vector3f(1.0f, 1.0f, 0.0f), new Vector3f(1.0f, -1.0f, 0.0f)};
        float scale = this.getQuadSize(partialTicks);
        for (int k = 0; k < 4; ++k) {
            Vector3f vertice = vertices[k];
            vertice.transform(quaternion);
            vertice.mul(scale);
            vertice.add(x, y, z);
        }
        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int light = this.getLightColor(partialTicks);
        float alpha = this.alpha * (1 - this.fadeProgress(partialTicks));
        buffer.vertex(vertices[0].x(), vertices[0].y(), vertices[0].z()).uv(u1, v1).color(this.rCol, this.gCol, this.bCol, alpha).uv2(light).endVertex();
        buffer.vertex(vertices[1].x(), vertices[1].y(), vertices[1].z()).uv(u1, v0).color(this.rCol, this.gCol, this.bCol, alpha).uv2(light).endVertex();
        buffer.vertex(vertices[2].x(), vertices[2].y(), vertices[2].z()).uv(u0, v0).color(this.rCol, this.gCol, this.bCol, alpha).uv2(light).endVertex();
        buffer.vertex(vertices[3].x(), vertices[3].y(), vertices[3].z()).uv(u0, v1).color(this.rCol, this.gCol, this.bCol, alpha).uv2(light).endVertex();

        buffer.vertex(vertices[3].x(), vertices[3].y(), vertices[3].z()).uv(u0, v1).color(this.rCol, this.gCol, this.bCol, alpha).uv2(light).endVertex();
        buffer.vertex(vertices[2].x(), vertices[2].y(), vertices[2].z()).uv(u0, v0).color(this.rCol, this.gCol, this.bCol, alpha).uv2(light).endVertex();
        buffer.vertex(vertices[1].x(), vertices[1].y(), vertices[1].z()).uv(u1, v0).color(this.rCol, this.gCol, this.bCol, alpha).uv2(light).endVertex();
        buffer.vertex(vertices[0].x(), vertices[0].y(), vertices[0].z()).uv(u1, v1).color(this.rCol, this.gCol, this.bCol, alpha).uv2(light).endVertex();
    }

    public float fadeProgress(float partialTicks) {
        return Mth.clamp((this.age + partialTicks) / this.lifetime, 0, 1);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<RingParticleData> {

        private final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(RingParticleData data, ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ) {
            return new RingParticle(world, x, y, z, motionX, motionY, motionZ, data, this.sprite, 40, 0.7F, 1.3F, false, true, false);
        }
    }
}
