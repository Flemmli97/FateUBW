package com.flemmli97.fate.client.model;

import com.flemmli97.fate.common.entity.EntityCaladBolg;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * CaladBolg - Undefined
 * Created using Tabula 6.0.0
 */
public class ModelCaladBolg extends EntityModel<EntityCaladBolg> {

    public ModelRenderer point;
    public ModelRenderer blade1;
    public ModelRenderer blade2;
    public ModelRenderer guard;
    public ModelRenderer handle;

    public ModelCaladBolg() {
        this.textureWidth = 35;
        this.textureHeight = 34;

        point = new ModelRenderer(this);
        point.setRotationPoint(0.0F, 19.5F, -13.0F);
        point.setTextureOffset(0, 0).addCuboid(-0.5F, -0.5F, -5.0F, 1.0F, 1.0F, 5.0F, 0.0F, false);

        blade1 = new ModelRenderer(this);
        blade1.setRotationPoint(0.0F, 0.0F, 7.0F);
        point.addChild(blade1);
        blade1.setTextureOffset(0, 6).addCuboid(-1.5F, -1.5F, -7.0F, 3.0F, 3.0F, 7.0F, 0.0F, false);

        blade2 = new ModelRenderer(this);
        blade2.setRotationPoint(0.0F, 0.0F, 20.0F);
        point.addChild(blade2);
        blade2.setTextureOffset(0, 16).addCuboid(-2.5F, -2.5F, -13.0F, 5.0F, 5.0F, 13.0F, 0.0F, false);

        guard = new ModelRenderer(this);
        guard.setRotationPoint(0.0F, 0.0F, 21.0F);
        point.addChild(guard);
        guard.setTextureOffset(19, 0).addCuboid(-3.5F, -3.5F, -1.0F, 7.0F, 7.0F, 1.0F, 0.0F, false);

        handle = new ModelRenderer(this);
        handle.setRotationPoint(0.0F, 0.0F, 24.0F);
        point.addChild(handle);
        handle.setTextureOffset(19, 8).addCuboid(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);
    }

    @Override
    public void setAngles(EntityCaladBolg bolg, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.point.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
