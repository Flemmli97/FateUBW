package io.github.flemmli97.fateubw.client.particles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.TrailPositions;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class TrailRenderer {

    public static void render(TrailInfo info, TrailPositions position, PoseStack stack, VertexConsumer buffer, Camera camera,
                              float x, float y, float z,
                              float u0, float u1, float v0, float v1) {
        if (position == null || position.size() < 2)
            return;
        Matrix4f mat = stack.last().pose();
        List<Pair<Vector3f, Vector3f>> positions = new ArrayList<>();
        for (int i = 0; i < position.size() - 1; i++) {
            TrailPositions.TrailPosition pos = position.getAt(i);
            if (pos == null)
                continue;
            TrailPositions.TrailPosition previous = i == 0 ? pos : position.getAt(i - 1);
            if (previous == null)
                previous = pos;
            TrailPositions.TrailPosition next = position.getAt(i + 1);
            if (next == null)
                next = pos;
            TrailPositions.TrailPosition next2 = position.getAt(i + 2);
            if (next2 == null)
                next2 = next;
            if (i == 0) {
                Vector3f start = pos.pos().toVector3f().sub(x, y, z);
                positions.add(Pair.of(start, pos.normal() == null ? new Vector3f(0, 1, 0) : pos.normal().toVector3f()));
            }
            float step = 1f / info.interpolation();
            int prev = 0;
            for (float j = step; j <= 1; j += step) {
                Vector3f stepPos = catmullRom(j, previous.pos(), pos.pos(), next.pos(), next2.pos())
                        .sub(x, y, z);
                if (stepPos == null)
                    continue;
                if (prev == 0) {
                    Pair<Vector3f, Vector3f> start = positions.getFirst();
                    positions.set(0, Pair.of(start.getFirst(), calculateNormal(start.getFirst(), stepPos, start.getSecond(), camera)));
                }
                Vector3f stepNormal = catmullRom(j, previous.normal(), pos.normal(), next.normal(), next2.normal());
                positions.add(Pair.of(stepPos, calculateNormal(positions.get(prev).getFirst(), stepPos, stepNormal, camera)));
                prev++;
            }
        }
        for (int i = 0; i < positions.size() - 1; i++) {
            Pair<Vector3f, Vector3f> pos = positions.get(i);
            Pair<Vector3f, Vector3f> next = positions.get(i + 1);
            float prog = Mth.clamp((float) i / (positions.size() - 1), 0, 1);
            float progNext = Mth.clamp((float) (i + 1) / (positions.size() - 1), 0, 1);

            Vector4f[] vertices = vertices(info, pos.getFirst(), next.getFirst(), pos.getSecond(), next.getSecond(), prog, progNext);
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

    protected static Vector3f catmullRom(float delta, @Nullable Vec3 p1, @Nullable Vec3 p2, @Nullable Vec3 p3, @Nullable Vec3 p4) {
        if (p1 == null || p2 == null || p3 == null || p4 == null)
            return null;
        if (p2.equals(p3))
            return p2.toVector3f();
        return new Vector3f(Mth.catmullrom(delta, (float) p1.x(), (float) p2.x(), (float) p3.x(), (float) p4.x()),
                Mth.catmullrom(delta, (float) p1.y(), (float) p2.y(), (float) p3.y(), (float) p4.y()),
                Mth.catmullrom(delta, (float) p1.z(), (float) p2.z(), (float) p3.z(), (float) p4.z()));
    }

    protected static Vector3f calculateNormal(Vector3f from, Vector3f to, @Nullable Vector3f normal, Camera camera) {
        if (normal != null)
            return normal.normalize();
        Vector3f target = to.add(from, new Vector3f()).mul(0.5f);
        return target.cross(camera.getLookVector()).normalize();
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
}
