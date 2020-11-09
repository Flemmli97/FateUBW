package com.flemmli97.fate.client.model;

import com.flemmli97.fate.common.entity.EntityCaladBolg;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
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
        this.point = new ModelRenderer(this, 0, 0);
        this.point.setRotationPoint(0.0F, 0F, 0.0F);
        this.point.addCuboid(0.0F, 0.0F, 0.0F, 1, 1, 5, 0.0F);
        this.blade1 = new ModelRenderer(this, 0, 6);
        this.blade1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.blade1.addCuboid(-1.0F, -1.0F, 5.0F, 3, 3, 7, 0.0F);
        this.blade2 = new ModelRenderer(this, 0, 16);
        this.blade2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.blade2.addCuboid(-2.0F, -2.0F, 12.0F, 5, 5, 13, 0.0F);
        this.guard = new ModelRenderer(this, 19, 0);
        this.guard.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.guard.addCuboid(-3.0F, -3.0F, 25.0F, 7, 7, 1, 0.0F);
        this.handle = new ModelRenderer(this, 19, 8);
        this.handle.setRotationPoint(0F, 0F, 0F);
        this.handle.addCuboid(-0.5F, -0.5F, 26.0F, 2, 2, 3, 0.0F);
        this.point.addChild(this.blade1);
        this.point.addChild(this.blade2);
        this.point.addChild(this.guard);
        this.point.addChild(this.handle);
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
