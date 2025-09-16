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
import io.github.flemmli97.fateubw.client.render.FateRenders;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.TrailParticleData;
import io.github.flemmli97.fateubw.common.particles.trail.TrailPositions;
import io.github.flemmli97.fateubw.common.particles.trail.provider.TrailProvider;
import io.github.flemmli97.fateubw.mixinhelper.SpriteList;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class TrailParticle extends TextureSheetParticle {

    private final TrailInfo trail;
    @Nullable
    private final TrailProvider trailProvider;
    private final SpriteSet spriteSet;

    public TrailParticle(ClientLevel level, SpriteSet spriteSet, double x, double y, double z, TrailInfo trail) {
        super(level, x, y, z);
        this.lifetime = 40;
        this.gravity = 0;
        this.hasPhysics = false;
        this.x = this.xo;
        this.y = this.yo;
        this.z = this.zo;
        this.trail = trail;
        this.spriteSet = spriteSet;
        this.bbWidth = Math.max(this.trail.width(), this.trail.width2()) * 2;
        this.trailProvider = this.trail.data().createProvider(level, () -> new Vec3(this.x, this.y, this.z), this::getLifetime);
        if (this.trailProvider != null) {
            TrailPositions pos = this.trailProvider.positions();
            if (pos != null)
                this.setBoundingBox(pos.getBounds(Math.max(this.trail.width(), this.trail.width2())));
            this.pickSprite(spriteSet);
            this.setSprite(this.trail.textureIndex());
        }
    }

    private void setSprite(int index) {
        if (this.spriteSet instanceof SpriteList accessor && index < accessor.fateUBW$getSprites().size())
            this.setSprite(accessor.fateUBW$getSprites().get(index));
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        if (this.trailProvider == null)
            return;
        partialTicks = this.trailProvider.adjustedPartialTicks(partialTicks);
        PoseStack stack = new PoseStack();
        this.translate(stack, camera, partialTicks);
        TrailRenderer.render(this.trail, this.trailProvider.positions(), stack, buffer, camera,
                (float) this.x, (float) this.y, (float) this.z,
                (float) this.x, (float) this.y, (float) this.z,
                this.getU0(), this.getU1(), this.getV0(), this.getV1());
    }

    protected void translate(PoseStack stack, Camera camera, float partialTicks) {
        Vec3 vec3 = camera.getPosition();
        double dx = Mth.lerp(partialTicks, this.xo, this.x) - vec3.x();
        double dy = Mth.lerp(partialTicks, this.yo, this.y) - vec3.y();
        double dz = Mth.lerp(partialTicks, this.zo, this.z) - vec3.z();
        stack.translate(dx, dy, dz);
    }

    @Override
    public void tick() {
        if (this.trailProvider == null || this.trailProvider.removed()) {
            this.remove();
            return;
        }
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.yd -= 0.04 * (double) this.gravity;
        this.x += this.xd;
        this.y += this.yd;
        this.z += this.zd;
        this.xd *= this.friction;
        this.yd *= this.friction;
        this.zd *= this.friction;
        Vec3 pos = this.trailProvider.particleTick();
        if (pos != null) {
            this.setPos(pos.x(), pos.y(), pos.z());
        }
        TrailPositions poss = this.trailProvider.positions();
        if (poss != null) {
            this.setBoundingBox(poss.getBounds(Math.max(this.trail.width(), this.trail.width2())));
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return this.trail.visual() == TrailInfo.Visual.SOLID ? SOLID_COLOR_PARTICLE : COLOR_PARTICLE;
    }

    /**
     * NeoForge culling
     */
    public AABB getRenderBoundingBox(float partialTicks) {
        return this.getBoundingBox().inflate(1);
    }

    public static final ParticleRenderType SOLID_COLOR_PARTICLE = new ParticleRenderType() {

        @Override
        public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
            RenderSystem.depthMask(true);
            RenderSystem.enableBlend();
            RenderSystem.disableCull();
            RenderSystem.setShader(GameRenderer::getPositionColorLightmapShader);
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_LIGHTMAP);
        }

        public String toString() {
            return Fate.MODID + ":SOLID_COLOR_PARTICLE";
        }
    };

    public static final ParticleRenderType COLOR_PARTICLE = new ParticleRenderType() {

        @SuppressWarnings("deprecation")
        @Override
        public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
            RenderSystem.depthMask(true);
            RenderSystem.disableCull();
            RenderSystem.setShader(FateRenders::getParticleColorAddShaderInstance);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        public String toString() {
            return Fate.MODID + ":COLOR_PARTICLE";
        }
    };

    public record Factory(SpriteSet sprite) implements ParticleProvider<TrailParticleData> {

        @Override
        public Particle createParticle(TrailParticleData data, ClientLevel level, double x, double y, double z, double motX, double motY, double motZ) {
            return new TrailParticle(level, this.sprite, x, y, z, data.getTrailInfo());
        }
    }
}
