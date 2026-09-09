package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.VertexConsumer;

record AlphaVertexConsumerWrapper(VertexConsumer parent, float alpha) implements VertexConsumer {

    @Override
    public VertexConsumer addVertex(float x, float y, float z) {
        return this.parent.addVertex(x, y, z);
    }

    @Override
    public VertexConsumer setColor(int r, int g, int b, int alpha) {
        return this.parent.setColor(r, g, b, (int) (alpha * this.alpha));
    }

    @Override
    public VertexConsumer setUv(float u, float v) {
        return this.parent.setUv(u, v);
    }

    @Override
    public VertexConsumer setUv1(int u, int v) {
        return this.parent.setUv1(u, v);
    }

    @Override
    public VertexConsumer setUv2(int u, int v) {
        return this.parent.setUv2(u, v);
    }

    @Override
    public VertexConsumer setNormal(float x, float y, float z) {
        return this.parent.setNormal(x, y, z);
    }
}
