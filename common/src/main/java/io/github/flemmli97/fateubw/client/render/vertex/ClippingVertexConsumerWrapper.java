package io.github.flemmli97.fateubw.client.render.vertex;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import io.github.flemmli97.tenshilib.client.render.vertex.VertexConsumerUtils;
import io.github.flemmli97.tenshilib.client.render.vertex.VertexUtils;
import io.github.flemmli97.tenshilib.client.render.vertex.WrappedVertexConsumer;
import org.joml.Vector4f;

public class ClippingVertexConsumerWrapper extends WrappedVertexConsumer {

    private static final Vector4f NO_COLOR = new Vector4f(0);

    public static final VertexUtils.VertexFormatElementData<Vector4f> CLIP_COLOR = VertexUtils.register(VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.GENERIC,
            4, VertexConsumerUtils.ConstantVec4fVertexConsumer::new);

    private final Vector4f clippingPlane, color;
    private final float width;

    private ClippingVertexConsumerWrapper(VertexConsumer wrapped, Vector4f clippingPlane, Vector4f color, float width) {
        super(wrapped);
        this.clippingPlane = clippingPlane;
        this.color = color;
        this.width = width;
    }

    public static VertexConsumer wrap(VertexConsumer wrapped, Vector4f clippingPlane, Vector4f color, float width) {
        return new ClippingVertexConsumerWrapper(wrapped, clippingPlane, color, width);
    }

    public static VertexConsumer wrap(VertexConsumer wrapped, Vector4f clippingPlane) {
        return new ClippingVertexConsumerWrapper(wrapped, clippingPlane, NO_COLOR, 0);
    }

    @Override
    public VertexConsumer addVertex(float x, float y, float z) {
        super.addVertex(x, y, z);
        VertexUtils.addVertexData(
                this.wrapped,
                VertexUtils.VEC4f.element().get(),
                this.clippingPlane.x, this.clippingPlane.y, this.clippingPlane.z, this.clippingPlane.w
        );
        VertexUtils.addVertexData(
                this.wrapped,
                CLIP_COLOR.element().get(),
                this.color.x, this.color.y, this.color.z, this.color.w
        );
        VertexUtils.addVertexData(
                this.wrapped,
                VertexUtils.SINGLE_FLOAT.element().get(),
                this.width
        );
        return this;
    }
}
