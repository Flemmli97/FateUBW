package io.github.flemmli97.fateubw.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

/**
 * PoseStack that records all transform since its creation.
 * {@link DoublePoseStack#getApplied()} returns the applied transformations.
 */
public class DoublePoseStack extends PoseStack {

    private final PoseStack wrapped;
    private final PoseStack internal = new PoseStack();

    public DoublePoseStack(PoseStack wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void translate(double x, double y, double z) {
        this.wrapped.translate(x, y, z);
        this.internal.translate(x, y, z);
    }

    @Override
    public void translate(float x, float y, float z) {
        this.wrapped.translate(x, y, z);
        this.internal.translate(x, y, z);
    }

    @Override
    public void scale(float x, float y, float z) {
        this.wrapped.scale(x, y, z);
        this.internal.scale(x, y, z);
    }

    @Override
    public void mulPose(Quaternionf quaternion) {
        this.wrapped.mulPose(quaternion);
        this.internal.mulPose(quaternion);
    }

    @Override
    public void rotateAround(Quaternionf quaternion, float x, float y, float z) {
        this.wrapped.rotateAround(quaternion, x, y, z);
        this.internal.rotateAround(quaternion, x, y, z);
    }

    @Override
    public void pushPose() {
        this.wrapped.pushPose();
        this.internal.pushPose();
    }

    @Override
    public void popPose() {
        this.wrapped.popPose();
        this.internal.popPose();
    }

    @Override
    public Pose last() {
        return this.wrapped.last();
    }

    @Override
    public boolean clear() {
        this.internal.clear();
        return this.wrapped.clear();
    }

    @Override
    public void setIdentity() {
        this.wrapped.setIdentity();
        this.internal.clear();
    }

    @Override
    public void mulPose(Matrix4f pose) {
        this.wrapped.mulPose(pose);
        this.internal.mulPose(pose);
    }

    public PoseStack getApplied() {
        return this.internal;
    }
}
