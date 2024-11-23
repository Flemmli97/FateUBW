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
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.particles.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.TrailParticleData;
import io.github.flemmli97.fateubw.mixinhelper.Matrix4fTransformer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class SimpleTrailParticle extends Particle {

    private final TrailInfo trail;
    private final Vec3 originPos;

    private final TrailPosition startPos;
    private final TrailPosition[] lastPos;
    private int index;

    private final PoseStack rotationStack = new PoseStack();

    protected SimpleTrailParticle(ClientLevel level, double x, double y, double z, TrailInfo trail) {
        super(level, x, y, z);
        this.lifetime = trail.duration;
        this.trail = trail;
        this.originPos = new Vec3(x, y, z);

        this.x = this.xo;
        this.y = this.yo;
        this.z = this.zo;

        this.lastPos = new TrailPosition[trail.fadeTime + 2]; // Interpolation buffer
        this.lastPos[this.index] = calculatePos(this.trail, 0);
        this.startPos = this.lastPos[this.index];
        this.friction = 0;

        this.rotationStack.mulPose(Vector3f.YP.rotationDegrees(this.trail.yRot));
        this.rotationStack.mulPose(Vector3f.XP.rotationDegrees(this.trail.xRot));
        this.rotationStack.mulPose(Vector3f.ZP.rotationDegrees(this.trail.zRot));

        this.bbWidth = this.trail.scale;
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        PoseStack stack = new PoseStack();
        this.translate(stack, camera, partialTicks);
        Matrix4f mat = stack.last().pose();
        int light = this.getLightColor(partialTicks);
        for (int i = 0; i < this.trail.fadeTime; i++) {
            int indx = this.clampedIndex(this.index - i);
            TrailPosition pos = this.lastPos[indx];
            if (pos == null)
                continue;
            TrailPosition previous = this.lastPos[this.clampedIndex(indx - 1)];
            if (previous == null)
                previous = this.startPos;
            TrailPosition previouesTwo = this.lastPos[this.clampedIndex(indx - 2)];
            if (previouesTwo == null)
                previouesTwo = this.startPos;
            Vector4f[] vertices = this.vertices(pos, previous, previouesTwo, i, partialTicks);
            for (Vector4f vert : vertices) {
                vert.transform(mat);
            }

            float prog = (i + partialTicks) / this.trail.fadeTime;
            float progPre = Mth.clamp((i + 1 + partialTicks) / this.trail.fadeTime, 0, 1);
            float r = (this.trail.r2 - this.trail.r) * prog + this.trail.r;
            float g = (this.trail.g2 - this.trail.g) * prog + this.trail.g;
            float b = (this.trail.b2 - this.trail.b) * prog + this.trail.b;
            float a = (this.trail.a2 - this.trail.a) * prog + this.trail.a;

            float r2 = (this.trail.r2 - this.trail.r) * progPre + this.trail.r;
            float g2 = (this.trail.g2 - this.trail.g) * progPre + this.trail.g;
            float b2 = (this.trail.b2 - this.trail.b) * progPre + this.trail.b;
            float a2 = (this.trail.a2 - this.trail.a) * progPre + this.trail.a;

            this.draw(buffer, vertices, r, g, b, a, r2, g2, b2, a2, light);
        }
    }

    protected void translate(PoseStack stack, Camera camera, float partialTicks) {
        Vec3 vec3 = camera.getPosition();
        stack.translate(this.originPos.x() - vec3.x(), this.originPos.y() - vec3.y(), this.originPos.z() - vec3.z());
        stack.mulPoseMatrix(this.rotationStack.last().pose());
        if (this.trail.direct) {
            // Orient to camera
            double dX = vec3.x - Mth.lerp(partialTicks, this.xo, this.x);
            double dY = vec3.y - Mth.lerp(partialTicks, this.yo, this.y);
            double dZ = vec3.z - Mth.lerp(partialTicks, this.zo, this.z);
            float yRot = (float) Mth.wrapDegrees((Mth.atan2(dZ, dX) * Mth.RAD_TO_DEG) - 90);
            float targetXRot = (float) Mth.wrapDegrees((Mth.atan2(dY, Math.sqrt(dX * dX + dZ * dZ)) * Mth.RAD_TO_DEG));
            stack.mulPose(Vector3f.ZP.rotationDegrees(Mth.degreesDifference(yRot, Mth.wrapDegrees(-this.trail.yRot)) < 0 ? -targetXRot : targetXRot));
        }
    }

    protected void draw(VertexConsumer buffer, Vector4f[] vertices, float r, float g, float b, float a, float r2, float g2, float b2, float a2, int light) {
        buffer.vertex(vertices[0].x(), vertices[0].y(), vertices[0].z()).color(r2, g2, b2, a2).uv2(light).endVertex();
        buffer.vertex(vertices[1].x(), vertices[1].y(), vertices[1].z()).color(r2, g2, b2, a2).uv2(light).endVertex();
        buffer.vertex(vertices[2].x(), vertices[2].y(), vertices[2].z()).color(r, g, b, a).uv2(light).endVertex();
        buffer.vertex(vertices[3].x(), vertices[3].y(), vertices[3].z()).color(r, g, b, a).uv2(light).endVertex();
    }

    private Vector4f[] vertices(TrailPosition current, TrailPosition last, TrailPosition lastTwo, int count, float partialTicks) {
        float prog = Mth.clamp((count + partialTicks), 0, this.trail.fadeTime) / this.trail.fadeTime;
        float progPre = Mth.clamp((count + 1 + partialTicks) / this.trail.fadeTime, 0, 1);
        float progPreTwo = Mth.clamp((count + 2 + partialTicks) / this.trail.fadeTime, 0, 1);

        float scale = Mth.lerp(prog, this.trail.scale, this.trail.scale2) * 0.2f;
        float scalePre = Mth.lerp(progPre, this.trail.scale, this.trail.scale2) * 0.2f;
        float scalePreTwo = Mth.lerp(progPreTwo, this.trail.scale, this.trail.scale2) * 0.2f;

        Vec3 dir = current.widthDirection.scale(scale);
        Vec3 dirPre = last.widthDirection.scale(scalePre);
        Vec3 dirPreTwo = lastTwo.widthDirection.scale(scalePreTwo);

        Vector4f vert_1 = lerp(partialTicks, lastTwo.position.add(dirPreTwo.scale(-1)), last.position.add(dirPre.scale(-1)));
        Vector4f vert_2 = lerp(partialTicks, lastTwo.position.add(dirPreTwo), last.position.add(dirPre.scale(1)));
        Vector4f vert_3 = lerp(partialTicks, last.position.add(dirPre), current.position.add(dir.scale(1)));
        Vector4f vert_4 = lerp(partialTicks, last.position.add(dirPre.scale(-1)), current.position.add(dir.scale(-1)));
        return new Vector4f[]{vert_1, vert_2, vert_3, vert_4};
    }

    private int clampedIndex(int index) {
        return (index + this.lastPos.length) % this.lastPos.length;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        TrailPosition pos = calculatePos(this.trail, (float) this.age / this.lifetime);
        Vec3 off = Matrix4fTransformer.transformVec(pos.position, this.rotationStack.last().pose());
        this.setPos(this.originPos.x() + off.x(), this.originPos.y() + off.y(), this.originPos.z() + off.z());
        this.index = this.clampedIndex(this.index + 1);
        this.lastPos[this.index] = pos;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return COLOR_PARTICLE;
    }

    public static Vec3 bezierPos(Vec3 start, Vec3 control, Vec3 end, float progress) {
        float f = (1 - progress) * (1 - progress);
        float f2 = 2 * (1 - progress) * progress;
        float f3 = progress * progress;
        return start.scale(f).add(control.scale(f2)).add(end.scale(f3));
    }

    private static TrailPosition calculatePos(TrailInfo info, float progress) {
        if (info.direct) {
            Vec3 off = new Vec3(Mth.lerp(progress, info.start.x(), info.end.x()),
                    Mth.lerp(progress, info.start.y(), info.end.y()),
                    Mth.lerp(progress, info.start.z(), info.end.z()));
            return new TrailPosition(off, info.normalY);
        }
        Vec3 off = bezierPos(info.start, info.controlPoint, info.end, progress);
        Vec3 dir = bezierPos(info.start.add(info.normalY), info.controlPoint.add(info.normalY), info.end.add(info.normalY), progress);
        dir = dir.subtract(bezierPos(info.start.subtract(info.normalY), info.controlPoint.subtract(info.normalY), info.end.subtract(info.normalY), progress))
                .normalize().scale(0.5);
        return new TrailPosition(off, dir);
    }

    private static Vector4f lerp(float delta, Vec3 start, Vec3 end) {
        return new Vector4f((float) Mth.lerp(delta, start.x(), end.x()),
                (float) Mth.lerp(delta, start.y(), end.y()),
                (float) Mth.lerp(delta, start.z(), end.z()), 1);
    }

    record TrailPosition(Vec3 position, Vec3 widthDirection) {
    }

    public static final ParticleRenderType COLOR_PARTICLE = new ParticleRenderType() {

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
            return Fate.MODID + ":COLOR_PARTICLE";
        }
    };

    public record Factory(
            SpriteSet sprite) implements ParticleProvider<TrailParticleData> {

        @Override
        public Particle createParticle(TrailParticleData data, ClientLevel level, double x, double y, double z, double motX, double motY, double motZ) {
            return new SimpleTrailParticle(level, x, y, z, data.getTrailInfo());
        }
    }
}
