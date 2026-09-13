package io.github.flemmli97.fateubw.client.particles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.TrailPositions;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class TrailRenderer {

    public static void render(Entity entity, TrailInfo info, TrailPositions positions, VertexConsumer consumer, float partialTick) {
        PoseStack stack = new PoseStack();
        Camera camera = Minecraft.getInstance().getEntityRenderDispatcher().camera;
        Vec3 vec3 = camera.getPosition();
        double dx = entity.getX() - vec3.x();
        double dy = entity.getY() - vec3.y();
        double dz = entity.getZ() - vec3.z();
        stack.translate(dx, dy, dz);
        TrailRenderer.render(info, positions, stack, consumer, camera,
                (float) entity.getX(), (float) entity.getY(), (float) entity.getZ(), partialTick,
                0, 1, 0, 1);
    }

    public static void render(TrailInfo info, TrailPositions position, PoseStack stack, VertexConsumer buffer, Camera camera,
                              float x, float y, float z, float partialTicks,
                              float u0, float u1, float v0, float v1) {
        if (position == null || position.size() < 2)
            return;
        Matrix4f mat = stack.last().pose();
        // Calculate interpolated positions and their normals
        List<TrailPosition3f> positions = new ArrayList<>();
        int interpolation = Math.max(1, info.interpolation());
        for (int i = 0; i < position.size() - 1; i++) {
            TrailPositions.TrailPosition[] current = extractPositionsFor(position, i, partialTicks);
            if (current == null)
                continue;
            float step = 1f / interpolation;
            for (float j = 0; j < 1; j += step) {
                Vector3f stepPos = catmullRom(j, current[0].pos(), current[1].pos(), current[2].pos(), current[3].pos()).sub(x, y, z);
                if (stepPos == null)
                    continue;
                Vector3f stepNormal = catmullRom(j, current[0].normal(), current[1].normal(), current[2].normal(), current[3].normal());
                if (positions.isEmpty()) {
                    positions.add(TrailPosition3f.of(stepPos, stepNormal, i));
                    break;
                }
                TrailPosition3f last = positions.getLast();
                Vector3f prevPos = last.pos();
                Vector3f previousNormal = last.normal();
                Vector3f normal = calculateNormal(prevPos, stepPos, stepNormal, previousNormal, camera);
                if (positions.size() == 1) {
                    // Update the first normal
                    positions.set(0, TrailPosition3f.of(prevPos, previousNormal == null ? normal : previousNormal, last.progressIdx()));
                }
                positions.add(TrailPosition3f.of(stepPos, normal, Math.max(0, i - partialTicks) + j));
                if (i == position.size() - 2) {
                    break;
                }
            }
        }
        TrailPositions.TrailPosition current = position.getLast(partialTicks);
        if (current != null) {
            Vector3f currentPos = current.pos().toVector3f().sub(x, y, z);
            TrailPosition3f last = positions.getLast();
            Vector3f normal = calculateNormal(last.pos(), currentPos, current.normal() != null ? current.normal().toVector3f() : null, last.normal(), camera);
            positions.add(TrailPosition3f.of(currentPos, normal, Mth.floor(last.progressIdx() + 1)));
        }
        if (positions.size() < 2) {
            return;
        }
        // Updates the normal of the first entry cause it can be null
        TrailPosition3f first = positions.getFirst();
        if (first.normal() == null) {
            // Update the first normal
            first.updateNormal(positions.get(1).normal());
        }
        // Finally render the trail data
        // This will make the trail keep its width shape even if it's not fully spawned in yet
        int maxSegIdx = Math.max(1, position.getLength() - 2);
        int diff = Math.abs(maxSegIdx - (Mth.ceil(positions.getLast().progressIdx())));
        for (int i = 0; i < positions.size() - 1; i++) {
            TrailPosition3f pos = positions.get(i);
            TrailPosition3f next = positions.get(i + 1);
            float prog = Mth.clamp((diff + pos.progressIdx()) / maxSegIdx, 0, 1);
            float progNext = Mth.clamp((diff + next.progressIdx()) / maxSegIdx, 0, 1);

            Vector4f[] vertices = vertices(info, pos.pos(), next.pos(), pos.normal(), next.normal(), prog, progNext);
            for (Vector4f vert : vertices) {
                vert.mul(mat);
            }

            float ulen = u1 - u0;
            float u0p = Mth.clamp(u0 + prog * ulen, u0, u1);
            float u1p = Mth.clamp(u0 + progNext * ulen, u0, u1);

            float r = Mth.lerp(prog, info.r2(), info.r());
            float g = Mth.lerp(prog, info.g2(), info.g());
            float b = Mth.lerp(prog, info.b2(), info.b());
            float a = Mth.lerp(prog, info.a2(), info.a());

            float r2 = Mth.lerp(progNext, info.r2(), info.r());
            float g2 = Mth.lerp(progNext, info.g2(), info.g());
            float b2 = Mth.lerp(progNext, info.b2(), info.b());
            float a2 = Mth.lerp(progNext, info.a2(), info.a());

            draw(buffer, vertices, u0p, u1p, v0, v1, r, g, b, a, r2, g2, b2, a2);
        }
    }

    private static TrailPositions.TrailPosition[] extractPositionsFor(TrailPositions positions, int idx, float partialTicks) {
        TrailPositions.TrailPosition current = positions.getAt(idx, partialTicks);
        if (current == null)
            return null;
        TrailPositions.TrailPosition next = positions.getAt(idx + 1, partialTicks);
        if (next == null)
            return null;
        TrailPositions.TrailPosition previous = positions.getAt(idx - 1, partialTicks);
        if (previous == null)
            previous = current;
        TrailPositions.TrailPosition next2 = positions.getAt(idx + 2, partialTicks);
        if (next2 == null)
            next2 = next;
        return new TrailPositions.TrailPosition[]{
                previous, current, next, next2
        };
    }

    protected static Vector3f catmullRom(float delta, @Nullable Vec3 p1, @Nullable Vec3 p2, @Nullable Vec3 p3, @Nullable Vec3 p4) {
        if (delta == 0)
            return p2 != null ? p2.toVector3f() : null;
        if (p1 == null || p2 == null || p3 == null || p4 == null)
            return null;
        return new Vector3f(Mth.catmullrom(delta, (float) p1.x(), (float) p2.x(), (float) p3.x(), (float) p4.x()),
                Mth.catmullrom(delta, (float) p1.y(), (float) p2.y(), (float) p3.y(), (float) p4.y()),
                Mth.catmullrom(delta, (float) p1.z(), (float) p2.z(), (float) p3.z(), (float) p4.z()));
    }

    protected static Vector3f calculateNormal(Vector3f from, Vector3f to, @Nullable Vector3f normal, @Nullable Vector3f previousNormal, Camera camera) {
        if (normal != null)
            return normal.normalize();
        if (from.equals(to)) {
            return previousNormal != null ? previousNormal : new Vector3f(0, 1, 0);
        }
        Vector3f direction = to.sub(from, new Vector3f()).normalize();
        Vector3f result = direction.cross(camera.getLookVector(), new Vector3f());
        if (result.length() < 1E-3)
            return new Vector3f(0, 1, 0.1f);
        return result.normalize();
    }

    protected static Vector4f[] vertices(TrailInfo info, Vector3f current, Vector3f next, Vector3f currentNorm, Vector3f nextNorm, float progPrev, float prog) {
        float scale = Mth.lerp(progPrev, info.width2(), info.width());
        float scaleNext = Mth.lerp(prog, info.width2(), info.width());

        Vector4f vert_1 = new Vector4f(current.x() - currentNorm.x() * scale, current.y() - currentNorm.y() * scale, current.z() - currentNorm.z() * scale, 1);
        Vector4f vert_2 = new Vector4f(current.x() + currentNorm.x() * scale, current.y() + currentNorm.y() * scale, current.z() + currentNorm.z() * scale, 1);
        Vector4f vert_3 = new Vector4f(next.x() + nextNorm.x() * scaleNext, next.y() + nextNorm.y() * scaleNext, next.z() + nextNorm.z() * scaleNext, 1);
        Vector4f vert_4 = new Vector4f(next.x() - nextNorm.x() * scaleNext, next.y() - nextNorm.y() * scaleNext, next.z() - nextNorm.z() * scaleNext, 1);
        return new Vector4f[]{vert_1, vert_2, vert_3, vert_4};
    }

    protected static void draw(VertexConsumer buffer, Vector4f[] vertices, float u0, float u1, float v0, float v1, float r, float g, float b, float a, float r2, float g2, float b2, float a2) {
        for (int i = 0; i < vertices.length; i += 4) {
            buffer.addVertex(vertices[i].x(), vertices[i].y(), vertices[i].z()).setUv(u0, v1).setColor(r, g, b, a).setLight(0xff00ff);
            buffer.addVertex(vertices[i + 1].x(), vertices[i + 1].y(), vertices[i + 1].z()).setUv(u0, v0).setColor(r, g, b, a).setLight(0xff00ff);
            buffer.addVertex(vertices[i + 2].x(), vertices[i + 2].y(), vertices[i + 2].z()).setUv(u1, v0).setColor(r2, g2, b2, a2).setLight(0xff00ff);
            buffer.addVertex(vertices[i + 3].x(), vertices[i + 3].y(), vertices[i + 3].z()).setUv(u1, v1).setColor(r2, g2, b2, a2).setLight(0xff00ff);
        }
    }

    public static class TrailPosition3f {

        private final Vector3f pos;
        private Vector3f normal;
        private final float progressIdx;

        private TrailPosition3f(Vector3f pos, Vector3f normal, float progressIdx) {
            this.pos = pos;
            this.normal = normal;
            this.progressIdx = progressIdx;
        }

        public static TrailPosition3f of(Vector3f pos, Vector3f normal) {
            return new TrailPosition3f(pos, normal, 0);
        }

        public static TrailPosition3f of(Vector3f pos, Vector3f normal, float progressIdx) {
            return new TrailPosition3f(pos, normal, progressIdx);
        }

        public Vector3f pos() {
            return this.pos;
        }

        public Vector3f normal() {
            return this.normal;
        }

        public float progressIdx() {
            return this.progressIdx;
        }

        public void updateNormal(Vector3f normal) {
            this.normal = normal;
        }
    }
}
