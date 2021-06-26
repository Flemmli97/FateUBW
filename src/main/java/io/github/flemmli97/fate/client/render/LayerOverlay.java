package io.github.flemmli97.fate.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.flemmli97.fate.client.model.IArmModel;
import io.github.flemmli97.fate.common.entity.servant.EntityServant;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;

public abstract class LayerOverlay<T extends EntityServant, M extends EntityModel<T> & IArmModel> extends LayerRenderer<T, M> {

    protected final M model;

    public LayerOverlay(IEntityRenderer<T, M> entityRendererIn, M model) {
        super(entityRendererIn);
        this.model = model;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
        this.model.copyModelAttributesTo(this.getEntityModel());
        this.model.setRotationAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.getType(entitylivingbaseIn, partialTicks));
        this.model.transform(HandSide.RIGHT, matrixStackIn);
        this.model.getHand(Hand.MAIN_HAND).render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY);
    }

    public abstract RenderType getType(T entity, float partialTicks);
}
