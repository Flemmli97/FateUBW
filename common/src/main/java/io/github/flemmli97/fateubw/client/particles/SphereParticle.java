package io.github.flemmli97.fateubw.client.particles;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SphereParticle extends Particle {

    private float scale = 1;

    public SphereParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z, 0, 0, 0);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
    }

    @Override
    public Particle scale(float scale) {
        this.scale = scale;
        this.setSize(scale, scale);
        return this;
    }

    @Override
    protected void setSize(float width, float height) {
        if (width != this.bbWidth || height != this.bbHeight) {
            this.bbWidth = width;
            this.bbHeight = height;
            this.setBoundingBox(new AABB(-this.bbWidth, -this.bbHeight, -this.bbWidth, this.bbWidth, this.bbHeight, this.bbWidth)
                    .move(this.x, this.y, this.z));
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        Vec3 vec3 = camera.getPosition();
        float x = (float) (Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
        float y = (float) (Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
        float z = (float) (Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());
        PoseStack stack = new PoseStack();
        stack.translate(x, y, z);
        RenderUtils.renderSphere(buffer, false, stack, this.rCol, this.gCol, this.bCol, this.alpha, this.scale, 20, LightTexture.FULL_BRIGHT);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return SPHERE_RENDER_TYPE;
    }

    public static final ParticleRenderType SPHERE_RENDER_TYPE = new ParticleRenderType() {

        @Override
        public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.disableCull();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            return tesselator.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        }

        public String toString() {
            return Fate.MODID + ":SPHERE_PARTICLE";
        }
    };

    public record Factory<T extends ParticleOptions>(SpriteSet sprite) implements ParticleProvider<T> {

        @Override
        public Particle createParticle(T data, ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ) {
            return new SphereParticle(level, x, y, z);
        }
    }
}
