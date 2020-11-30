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

        this.point = new ModelRenderer(this);
        this.point.setRotationPoint(0.0F, 19.5F, -13.0F);
        this.point.setTextureOffset(0, 0).addCuboid(-0.5F, -0.5F, -5.0F, 1.0F, 1.0F, 5.0F, 0.0F, false);

        this.blade1 = new ModelRenderer(this);
        this.blade1.setRotationPoint(0.0F, 0.0F, 7.0F);
        this.point.addChild(this.blade1);
        this.blade1.setTextureOffset(0, 6).addCuboid(-1.5F, -1.5F, -7.0F, 3.0F, 3.0F, 7.0F, 0.0F, false);

        this.blade2 = new ModelRenderer(this);
        this.blade2.setRotationPoint(0.0F, 0.0F, 20.0F);
        this.point.addChild(this.blade2);
        this.blade2.setTextureOffset(0, 16).addCuboid(-2.5F, -2.5F, -13.0F, 5.0F, 5.0F, 13.0F, 0.0F, false);

        this.guard = new ModelRenderer(this);
        this.guard.setRotationPoint(0.0F, 0.0F, 21.0F);
        this.point.addChild(this.guard);
        this.guard.setTextureOffset(19, 0).addCuboid(-3.5F, -3.5F, -1.0F, 7.0F, 7.0F, 1.0F, 0.0F, false);

        this.handle = new ModelRenderer(this);
        this.handle.setRotationPoint(0.0F, 0.0F, 24.0F);
        this.point.addChild(this.handle);
        this.handle.setTextureOffset(19, 8).addCuboid(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);
    }

    @Override
    public void setAngles(EntityCaladBolg bolg, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.point.render(matrixStack, buffer, packedLight, packedOverlay);
    }
}
