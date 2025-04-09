package io.github.flemmli97.fateubw.client.particles;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector4f;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.TrailParticleData;
import io.github.flemmli97.fateubw.common.particles.trail.TrailPositions;
import io.github.flemmli97.fateubw.mixinhelper.SpriteList;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class TrailParticle extends TextureSheetParticle {

    private final TrailInfo trail;
    private final Vec3 originPos;
    private final SpriteSet spriteSet;

    private int sizeO;

    protected TrailParticle(ClientLevel level, SpriteSet spriteSet, double x, double y, double z, TrailInfo trail) {
        super(level, x, y, z);
        this.trail = trail;
        this.originPos = new Vec3(x, y, z);
        this.spriteSet = spriteSet;
        this.x = this.xo;
        this.y = this.yo;
        this.z = this.zo;
        this.friction = 0;
        this.bbWidth = Math.max(this.trail.width, this.trail.width2) * 2;
        TrailPositions pos = this.trail.provider.positions(level);
        if (pos != null)
            this.setBoundingBox(pos.getBounds(Math.max(this.trail.width, this.trail.width2))
                    .move(this.originPos.x(), this.originPos.y(), this.originPos.z()));
        this.pickSprite(spriteSet);
        this.setSprite(this.trail.textureIndex);
    }

    private void setSprite(int index) {
        if (this.spriteSet instanceof SpriteList accessor && index < accessor.fateUBW$getSprites().size())
            this.setSprite(accessor.fateUBW$getSprites().get(index));
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        TrailPositions position = this.trail.provider.positions(this.level);
        if (position == null)
            return;
        PoseStack stack = new PoseStack();
        this.translate(stack, camera);
        Matrix4f mat = stack.last().pose();
        int light = this.getLightColor(partialTicks);
        for (int i = 0; i < position.size(); i++) {
            TrailPositions.TrailPosition pos = position.getAt(-i);
            if (pos == null)
                continue;
            TrailPositions.TrailPosition previous = position.getAt(-i - 1);
            if (previous == null)
                previous = pos;
            TrailPositions.TrailPosition previousTwo = position.getAt(-i - 2);
            if (previousTwo == null)
                previousTwo = previous;
            Vector4f[] vertices = this.vertices(pos, previous, previousTwo, i, partialTicks, position.size());
            for (Vector4f vert : vertices) {
                vert.transform(mat);
            }

            float size = Mth.lerp(partialTicks, this.sizeO, position.size());
            float prog = Mth.clamp(i / size, 0, 1);
            float progPre = Mth.clamp((i + 1) / size, 0, 1);
            float u0Orig = this.getU0();
            float u1Orig = this.getU1();
            float ulen = u1Orig - u0Orig;
            float u0 = Mth.clamp(u0Orig + (1 - progPre) * ulen, u0Orig, u1Orig);
            float u1 = Mth.clamp(u0Orig + (1 - prog) * ulen, u0Orig, u1Orig);
            float v0 = this.getV0();
            float v1 = this.getV1();

            float r = (this.trail.r2 - this.trail.r) * prog + this.trail.r;
            float g = (this.trail.g2 - this.trail.g) * prog + this.trail.g;
            float b = (this.trail.b2 - this.trail.b) * prog + this.trail.b;
            float a = (this.trail.a2 - this.trail.a) * prog + this.trail.a;

            float r2 = (this.trail.r2 - this.trail.r) * progPre + this.trail.r;
            float g2 = (this.trail.g2 - this.trail.g) * progPre + this.trail.g;
            float b2 = (this.trail.b2 - this.trail.b) * progPre + this.trail.b;
            float a2 = (this.trail.a2 - this.trail.a) * progPre + this.trail.a;

            this.draw(buffer, vertices, u0, u1, v0, v1, r, g, b, a, r2, g2, b2, a2, light);
        }
    }

    protected void translate(PoseStack stack, Camera camera) {
        Vec3 vec3 = camera.getPosition();
        stack.translate(this.originPos.x() - vec3.x(), this.originPos.y() - vec3.y(), this.originPos.z() - vec3.z());
    }

    protected void draw(VertexConsumer buffer, Vector4f[] vertices, float u0, float u1, float v0, float v1, float r, float g, float b, float a, float r2, float g2, float b2, float a2, int light) {
        buffer.vertex(vertices[0].x(), vertices[0].y(), vertices[0].z()).uv(u0, v1).color(r2, g2, b2, a2).uv2(0xff00ff).endVertex();
        buffer.vertex(vertices[1].x(), vertices[1].y(), vertices[1].z()).uv(u0, v0).color(r2, g2, b2, a2).uv2(0xff00ff).endVertex();
        buffer.vertex(vertices[2].x(), vertices[2].y(), vertices[2].z()).uv(u1, v0).color(r, g, b, a).uv2(0xff00ff).endVertex();
        buffer.vertex(vertices[3].x(), vertices[3].y(), vertices[3].z()).uv(u1, v1).color(r, g, b, a).uv2(0xff00ff).endVertex();
    }

    private Vector4f[] vertices(TrailPositions.TrailPosition current, TrailPositions.TrailPosition last, TrailPositions.TrailPosition lastTwo,
                                int currentIdx, float partialTicks, int length) {
        length += 2;
        float prog = Mth.clamp((currentIdx + partialTicks) / length, 0, 1);
        float progPre = Mth.clamp((currentIdx + 1 + partialTicks) / length, 0, 1);
        float progPreTwo = Mth.clamp((currentIdx + 2 + partialTicks) / length, 0, 1);

        float scale = Mth.lerp(prog, this.trail.width, this.trail.width2);
        float scalePre = Mth.lerp(progPre, this.trail.width, this.trail.width2);
        float scalePreTwo = Mth.lerp(progPreTwo, this.trail.width, this.trail.width2);

        Vec3 dir = (current.normal() == null ? MathUtils.NORMAL_Y : current.normal()).scale(scale);
        Vec3 dirPre = (last.normal() == null ? MathUtils.NORMAL_Y : last.normal()).scale(scalePre);
        Vec3 dirPreTwo = (lastTwo.normal() == null ? MathUtils.NORMAL_Y : lastTwo.normal()).scale(scalePreTwo);

        Vector4f vert_1 = lerp(partialTicks, lastTwo.pos().add(-dirPreTwo.x(), -dirPreTwo.y(), -dirPreTwo.z()), last.pos().add(-dirPre.x(), -dirPre.y(), -dirPre.z()));
        Vector4f vert_2 = lerp(partialTicks, lastTwo.pos().add(dirPreTwo), last.pos().add(dirPre));
        Vector4f vert_3 = lerp(partialTicks, last.pos().add(dirPre), current.pos().add(dir));
        Vector4f vert_4 = lerp(partialTicks, last.pos().add(-dirPre.x(), -dirPre.y(), -dirPre.z()), current.pos().add(-dir.x(), -dir.y(), -dir.z()));
        return new Vector4f[]{vert_1, vert_2, vert_3, vert_4};
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.trail.provider.removed(this.level)) {
            this.remove();
            return;
        }
        Vec3 pos = this.trail.provider.particleTick(this.level);
        if (pos != null) {
            this.setPos(this.originPos.x() + pos.x(), this.originPos.y() + pos.y(), this.originPos.z() + pos.z());
        }
        TrailPositions poss = this.trail.provider.positions(this.level);
        if (poss != null) {
            this.sizeO = poss.size();
            this.setBoundingBox(poss.getBounds(Math.max(this.trail.width, this.trail.width2))
                    .move(this.originPos.x(), this.originPos.y(), this.originPos.z()));
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return this.trail.visual == TrailInfo.Visual.SOLID ? SOLID_COLOR_PARTICLE : COLOR_PARTICLE;
    }

    private static Vector4f lerp(float delta, Vec3 start, Vec3 end) {
        return new Vector4f((float) Mth.lerp(delta, start.x(), end.x()),
                (float) Mth.lerp(delta, start.y(), end.y()),
                (float) Mth.lerp(delta, start.z(), end.z()), 1);
    }

    public static final ParticleRenderType SOLID_COLOR_PARTICLE = new ParticleRenderType() {

        @Override
        public void begin(BufferBuilder builder, TextureManager textureManager) {
            RenderSystem.depthMask(true);
            RenderSystem.enableBlend();
            RenderSystem.disableCull();
            RenderSystem.setShader(GameRenderer::getPositionColorLightmapShader);
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_LIGHTMAP);
        }

        @Override
        public void end(Tesselator tesselator) {
            tesselator.end();
        }

        public String toString() {
            return Fate.MODID + ":SOLID_COLOR_PARTICLE";
        }
    };

    public static final ParticleRenderType COLOR_PARTICLE = new ParticleRenderType() {

        @Override
        public void begin(BufferBuilder builder, TextureManager textureManager) {
            RenderSystem.depthMask(true);
            RenderSystem.enableBlend();
            RenderSystem.disableCull();
            RenderSystem.setShader(FateRenders::getParticleColorAddShaderInstance);
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator tesselator) {
            tesselator.end();
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
