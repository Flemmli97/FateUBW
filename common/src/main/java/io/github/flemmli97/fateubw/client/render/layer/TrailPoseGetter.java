package io.github.flemmli97.fateubw.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;

public interface TrailPoseGetter {

    /**
     * @return A PoseStack that has all the model transformations applied but in relative format.
     * This means e.g. no translation of camera and entity positions are applied.
     */
    PoseStack getPlainStack();
}
