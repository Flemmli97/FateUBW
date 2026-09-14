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
import io.github.flemmli97.fateubw.common.particles.SwirlingCylinderData;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public class SwirlingCylinderParticle extends Particle {

    private static final ResourceLocation TEXTURE = Fate.modRes("textures/particle/swirling_noise.png");
    private static final ResourceLocation TEXTURE_2 = Fate.modRes("textures/particle/swirling_noise_base.png");

    private final SwirlingCylinderData data;
    private float scale = 1;
    private final float baseOffset;

    public SwirlingCylinderParticle(ClientLevel level, double x, double y, double z, SwirlingCylinderData data) {
        super(level, x, y, z, 0, 0, 0);
        this.data = data;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.baseOffset = level.getRandom().nextFloat();
        this.lifetime = this.data.duration();
    }

    public static ParticleRenderType[] getRenderTypes() {
        return new ParticleRenderType[]{SWIRLING_CYLINDER_PARTICLE.apply(TEXTURE), SWIRLING_CYLINDER_PARTICLE.apply(TEXTURE_2)};
    }

    @Override
    public Particle scale(float scale) {
        this.scale = scale;
        this.setSize(scale * Math.max(this.data.width1(), this.data.width2()), scale * this.data.height());
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
    public void render(VertexConsumer buffer, Camera camera, float partialTick) {
        Vec3 vec3 = camera.getPosition();
        float x = (float) (Mth.lerp(partialTick, this.xo, this.x) - vec3.x());
        float y = (float) (Mth.lerp(partialTick, this.yo, this.y) - vec3.y());
        float z = (float) (Mth.lerp(partialTick, this.zo, this.z) - vec3.z());
        PoseStack stack = new PoseStack();
        stack.translate(x, y, z);
        float offset = (this.age + partialTick) / 50;
        float prog = Mth.clamp((this.age + partialTick) / (this.lifetime * 0.7f), 0, 1);
        // Texture is fixed per render type so need to have 2 particle types
        // Can't use atlas due to wrapping
        float alphaMult = this.alpha * Mth.sin(prog * Mth.PI);
        if (this.data.base()) {
            RenderUtils.renderCylinder(buffer, stack,
                    this.rCol * this.data.r1(), this.gCol * this.data.g1(), this.bCol * this.data.b1(), alphaMult * this.data.a1(),
                    this.rCol * this.data.r2(), this.gCol * this.data.g2(), this.bCol * this.data.b2(), alphaMult * this.data.a2(),
                    this.scale * this.data.width1(), this.scale * this.data.width2(), 20, this.scale * this.data.height(),
                    LightTexture.FULL_BRIGHT,
                    this.baseOffset + offset, 0, this.baseOffset + 1 + offset, 1);
        } else {
            RenderUtils.renderCylinder(buffer, stack,
                    this.rCol * this.data.r1(), this.gCol * this.data.g1(), this.bCol * this.data.b1(), alphaMult * this.data.a1(),
                    this.rCol * this.data.r2(), this.gCol * this.data.g2(), this.bCol * this.data.b2(), alphaMult * this.data.a2(),
                    this.scale * this.data.width1(), this.scale * this.data.width2(), 20, this.scale * this.data.height(),
                    LightTexture.FULL_BRIGHT,
                    this.baseOffset, this.baseOffset + offset, this.baseOffset + 1, this.baseOffset + 1 + offset);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return SWIRLING_CYLINDER_PARTICLE.apply(this.data.base() ? TEXTURE_2 : TEXTURE);
    }

    public static final Function<ResourceLocation, ParticleRenderType> SWIRLING_CYLINDER_PARTICLE = Util.memoize(texture -> new ParticleRenderType() {

        @Override
        public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.disableCull();
            RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
            RenderSystem.setShaderTexture(0, texture);
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            return tesselator.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_TEX_COLOR);
        }

        public String toString() {
            return Fate.MODID + ":SWIRLING_CYLINDER_PARTICLE";
        }
    });

    public record Factory<T extends SwirlingCylinderData>(SpriteSet sprite) implements ParticleProvider<T> {

        @Override
        public Particle createParticle(T data, ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ) {
            return new SwirlingCylinderParticle(level, x, y, z, data);
        }
    }
}
