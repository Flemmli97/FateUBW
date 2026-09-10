package io.github.flemmli97.fateubw.client.render.vertex;

import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.tenshilib.client.render.vertex.WrappedVertexConsumer;

public class AlphaVertexConsumerWrapper extends WrappedVertexConsumer {

    private final float alpha;

    public AlphaVertexConsumerWrapper(VertexConsumer wrapped, float alpha) {
        super(wrapped);
        this.alpha = alpha;
    }

    @Override
    public VertexConsumer setColor(int r, int g, int b, int alpha) {
        return super.setColor(r, g, b, (int) (alpha * this.alpha));
    }
}
