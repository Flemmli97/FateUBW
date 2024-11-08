package io.github.flemmli97.fateubw.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.fateubw.common.network.S2CAttackDebug;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import io.github.flemmli97.tenshilib.common.utils.OrientedBoundingBox;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

import java.util.ArrayList;
import java.util.List;

public class AttackBBRender {

    private static final float[] ATTACK_RGB = new float[]{18 / 255f, 181 / 255f, 51 / 255f};
    private static final float[] ATTEMPT_RGB = new float[]{19 / 255f, 56 / 255f, 191 / 255f};
    public static final AttackBBRender INST = new AttackBBRender();
    private final List<RenderOBB> list = new ArrayList<>();
    private final List<RenderOBB> toAdd = new ArrayList<>();

    public void addNewAABB(OrientedBoundingBox aabb, int duration, S2CAttackDebug.EnumAABBType type) {
        this.toAdd.add(new RenderOBB(aabb, duration, type));
    }

    public void render(PoseStack stack, MultiBufferSource.BufferSource buffer) {
        this.list.addAll(this.toAdd);
        this.toAdd.clear();
        this.list.removeIf(r -> r.render(stack, buffer));
        buffer.endBatch(RenderType.LINES);
    }

    private static class RenderOBB {

        private final OrientedBoundingBox obb;
        private final S2CAttackDebug.EnumAABBType type;
        private int duration;

        public RenderOBB(OrientedBoundingBox aabb, int duration, S2CAttackDebug.EnumAABBType type) {
            this.obb = aabb;
            this.duration = duration;
            this.type = type;
        }

        public boolean render(PoseStack stack, MultiBufferSource buffer) {
            float[] color;
            if (this.type == S2CAttackDebug.EnumAABBType.ATTEMPT) {
                color = ATTEMPT_RGB;
            } else {
                color = ATTACK_RGB;
            }
            RenderUtils.renderOBB(stack, buffer, this.obb, color[0], color[1], color[2], 1, false);
            return this.duration-- < 0;
        }
    }
}
