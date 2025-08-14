package io.github.flemmli97.fateubw.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

/**
 * PoseStack that records all transform since its creation till {@link TransformRecordingStack#applyWrapped()} is called.
 * {@link TransformRecordingStack#getApplied()} returns the applied transformations provided {@link TransformRecordingStack#applyWrapped()} has NOT been called yet.
 */
public class TransformRecordingStack extends PoseStack {

    private final PoseStack wrapped;
    private PoseStack internal = new PoseStack();

    public TransformRecordingStack(PoseStack wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void translate(double x, double y, double z) {
        this.internal.translate((float) x, (float) y, (float) z);
    }

    @Override
    public void translate(float x, float y, float z) {
        this.internal.translate(x, y, z);
    }

    @Override
    public void scale(float x, float y, float z) {
        this.internal.scale(x, y, z);
    }

    @Override
    public void mulPose(Quaternionf quaternion) {
        this.internal.mulPose(quaternion);
    }

    @Override
    public void rotateAround(Quaternionf quaternion, float x, float y, float z) {
        this.internal.rotateAround(quaternion, x, y, z);
    }

    @Override
    public void pushPose() {
        this.internal.pushPose();
    }

    @Override
    public void popPose() {
        this.internal.popPose();
    }

    @Override
    public Pose last() {
        return this.internal.last();
    }

    @Override
    public boolean clear() {
        return this.internal.clear();
    }

    @Override
    public void setIdentity() {
        this.internal.setIdentity();
    }

    @Override
    public void mulPose(Matrix4f pose) {
        this.internal.mulPose(pose);
    }

    public PoseStack getApplied() {
        return this.internal;
    }

    public void applyWrapped() {
        this.wrapped.last().pose().mul(this.internal.last().pose());
        this.wrapped.last().normal().mul(this.internal.last().normal());
        this.internal = this.wrapped;
    }
}
