package io.github.flemmli97.fateubw.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.client.model.IArmModel;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

public abstract class LayerOverlay<T extends BaseServant, M extends EntityModel<T> & IArmModel> extends RenderLayer<T, M> {

    protected final M model;

    public LayerOverlay(RenderLayerParent<T, M> entityRendererIn, M model) {
        super(entityRendererIn);
        this.model = model;
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.prepareMobModel(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
        this.model.copyPropertiesTo(this.getParentModel());
        this.model.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.getType(entitylivingbaseIn, partialTicks));
        this.model.transform(HumanoidArm.RIGHT, matrixStackIn);
        this.model.getHand(InteractionHand.MAIN_HAND).render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY);
    }

    public abstract RenderType getType(T entity, float partialTicks);
}
