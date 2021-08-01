package io.github.flemmli97.fate.client.model;

import com.flemmli97.tenshilib.client.model.IResetModel;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.flemmli97.fate.common.entity.EntityGordiusBulls;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.math.MathHelper;

/**
 * Gordius Wheel - Black_Saturn
 * Created using Tabula 6.0.0
 */

public class ModelGordiusBulls extends EntityModel<EntityGordiusBulls> implements IResetModel {

    public ModelRendererPlus bull1;
    public ModelRendererPlus equipmentLayer;
    public ModelRendererPlus footLeftFront;
    public ModelRendererPlus footRightFront;
    public ModelRendererPlus footLeftRear;
    public ModelRendererPlus footRightRear;
    public ModelRendererPlus head;
    public ModelRendererPlus hornRight;
    public ModelRendererPlus hornRightTip;
    public ModelRendererPlus hornLeft;
    public ModelRendererPlus hornLeftTip;
    public ModelRendererPlus lead;
    public ModelRendererPlus mouth;
    public ModelRendererPlus leadFront;
    public ModelRendererPlus leadFront2;
    public ModelRendererPlus leadFront3;
    public ModelRendererPlus leadFront4;
    public ModelRendererPlus bull2;
    public ModelRendererPlus equipmentLayer2;
    public ModelRendererPlus footLeftFront2;
    public ModelRendererPlus footRightFront2;
    public ModelRendererPlus footLeftRear2;
    public ModelRendererPlus footRightRear2;
    public ModelRendererPlus head2;
    public ModelRendererPlus hornRight2;
    public ModelRendererPlus hornRightTip2;
    public ModelRendererPlus hornLeft2;
    public ModelRendererPlus hornLeftTip2;
    public ModelRendererPlus lead2;
    public ModelRendererPlus mouth2;
    public ModelRendererPlus leadFront5;
    public ModelRendererPlus leadFront6;
    public ModelRendererPlus leadFront7;
    public ModelRendererPlus leadFront8;

    public ModelGordiusBulls() {
        this.textureWidth = 129;
        this.textureHeight = 256;
        bull1 = new ModelRendererPlus(this);
        bull1.setDefaultRotPoint(-8.5F, 7.0F, -4.0F);
        bull1.setTextureOffset(0, 145).addBox(-6.0F, -5.0F, -9.0F, 12.0F, 10.0F, 18.0F, 0.0F, false);

        equipmentLayer = new ModelRendererPlus(this);
        equipmentLayer.setDefaultRotPoint(0.0F, 1.0F, 0.0F);
        bull1.addChild(equipmentLayer);
        equipmentLayer.setTextureOffset(59, 128).addBox(-6.5F, -6.3F, -9.5F, 13.0F, 13.0F, 19.0F, 0.0F, false);

        footLeftFront = new ModelRendererPlus(this);
        footLeftFront.setDefaultRotPoint(4.0F, 5.0F, -6.0F);
        bull1.addChild(footLeftFront);
        footLeftFront.setTextureOffset(0, 144).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        footRightFront = new ModelRendererPlus(this);
        footRightFront.setDefaultRotPoint(-4.0F, 5.0F, -6.0F);
        bull1.addChild(footRightFront);
        footRightFront.setTextureOffset(0, 144).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        footLeftRear = new ModelRendererPlus(this);
        footLeftRear.setDefaultRotPoint(4.0F, 5.0F, 7.0F);
        bull1.addChild(footLeftRear);
        footLeftRear.setTextureOffset(0, 144).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        footRightRear = new ModelRendererPlus(this);
        footRightRear.setDefaultRotPoint(-4.0F, 5.0F, 7.0F);
        bull1.addChild(footRightRear);
        footRightRear.setTextureOffset(0, 144).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        head = new ModelRendererPlus(this);
        head.setDefaultRotPoint(0.0F, -1.0F, -9.0F);
        bull1.addChild(head);
        head.setTextureOffset(0, 128).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

        hornRight = new ModelRendererPlus(this);
        hornRight.setDefaultRotPoint(-3.0F, -3.0F, -4.0F);
        head.addChild(hornRight);
        setRotationAngle(hornRight, 0.0F, 0.0F, 0.4363F);
        hornRight.setTextureOffset(42, 140).addBox(-6.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);

        hornRightTip = new ModelRendererPlus(this);
        hornRightTip.setDefaultRotPoint(-5.0F, 0.0F, 0.0F);
        hornRight.addChild(hornRightTip);
        setRotationAngle(hornRightTip, 0.0F, 0.0F, 1.4114F);
        hornRightTip.setTextureOffset(42, 140).addBox(-4.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);

        hornLeft = new ModelRendererPlus(this);
        hornLeft.setDefaultRotPoint(3.0F, -3.0F, -4.0F);
        head.addChild(hornLeft);
        setRotationAngle(hornLeft, 0.0F, 0.0F, -0.4363F);
        hornLeft.setTextureOffset(42, 140).addBox(0.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);

        hornLeftTip = new ModelRendererPlus(this);
        hornLeftTip.setDefaultRotPoint(5.0F, 0.0F, 0.0F);
        hornLeft.addChild(hornLeftTip);
        setRotationAngle(hornLeftTip, 0.0F, 0.0F, -1.4114F);
        hornLeftTip.setTextureOffset(42, 140).addBox(0.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);

        lead = new ModelRendererPlus(this);
        lead.setDefaultRotPoint(0.0F, -5.7F, -0.5F);
        head.addChild(lead);
        lead.setTextureOffset(0, 173).addBox(-4.0F, 0.0F, -0.5F, 8.0F, 0.0F, 1.0F, 0.0F, false);

        mouth = new ModelRendererPlus(this);
        mouth.setDefaultRotPoint(0.0F, 1.0F, -7.0F);
        head.addChild(mouth);
        setRotationAngle(mouth, 0.4363F, 0.0F, 0.0F);
        mouth.setTextureOffset(42, 128).addBox(-2.5F, -2.5F, -5.0F, 5.0F, 5.0F, 5.0F, 0.0F, false);

        leadFront = new ModelRendererPlus(this);
        leadFront.setDefaultRotPoint(2.5F, 1.0F, -3.0F);
        mouth.addChild(leadFront);
        setRotationAngle(leadFront, 0.0F, 0.7854F, 0.0F);
        leadFront.setTextureOffset(0, 167).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 6.0F, 0.0F, false);

        leadFront2 = new ModelRendererPlus(this);
        leadFront2.setDefaultRotPoint(0.0F, 0.0F, 6.0F);
        leadFront.addChild(leadFront2);
        setRotationAngle(leadFront2, 0.0F, -1.2217F, -0.5236F);
        leadFront2.setTextureOffset(0, 164).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 9.0F, 0.0F, false);

        leadFront3 = new ModelRendererPlus(this);
        leadFront3.setDefaultRotPoint(-2.5F, 1.0F, -3.0F);
        mouth.addChild(leadFront3);
        setRotationAngle(leadFront3, 0.0F, -0.7854F, 0.0F);
        leadFront3.setTextureOffset(0, 167).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 6.0F, 0.0F, false);

        leadFront4 = new ModelRendererPlus(this);
        leadFront4.setDefaultRotPoint(0.0F, 0.0F, 6.0F);
        leadFront3.addChild(leadFront4);
        setRotationAngle(leadFront4, 0.0F, 1.2217F, 0.5236F);
        leadFront4.setTextureOffset(0, 164).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 9.0F, 0.0F, false);

        bull2 = new ModelRendererPlus(this);
        bull2.setDefaultRotPoint(8.5F, 7.0F, -4.0F);
        bull2.setTextureOffset(0, 145).addBox(-6.0F, -5.0F, -9.0F, 12.0F, 10.0F, 18.0F, 0.0F, false);

        equipmentLayer2 = new ModelRendererPlus(this);
        equipmentLayer2.setDefaultRotPoint(0.0F, 1.0F, 0.0F);
        bull2.addChild(equipmentLayer2);
        equipmentLayer2.setTextureOffset(59, 128).addBox(-6.5F, -6.3F, -9.5F, 13.0F, 13.0F, 19.0F, 0.0F, false);

        footLeftFront2 = new ModelRendererPlus(this);
        footLeftFront2.setDefaultRotPoint(4.0F, 5.0F, -6.0F);
        bull2.addChild(footLeftFront2);
        footLeftFront2.setTextureOffset(0, 144).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        footRightFront2 = new ModelRendererPlus(this);
        footRightFront2.setDefaultRotPoint(-4.0F, 5.0F, -6.0F);
        bull2.addChild(footRightFront2);
        footRightFront2.setTextureOffset(0, 144).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        footLeftRear2 = new ModelRendererPlus(this);
        footLeftRear2.setDefaultRotPoint(4.0F, 5.0F, 7.0F);
        bull2.addChild(footLeftRear2);
        footLeftRear2.setTextureOffset(0, 144).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        footRightRear2 = new ModelRendererPlus(this);
        footRightRear2.setDefaultRotPoint(-4.0F, 5.0F, 7.0F);
        bull2.addChild(footRightRear2);
        footRightRear2.setTextureOffset(0, 144).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        head2 = new ModelRendererPlus(this);
        head2.setDefaultRotPoint(0.0F, -1.0F, -9.0F);
        bull2.addChild(head2);
        head2.setTextureOffset(0, 128).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

        hornRight2 = new ModelRendererPlus(this);
        hornRight2.setDefaultRotPoint(-3.0F, -3.0F, -4.0F);
        head2.addChild(hornRight2);
        setRotationAngle(hornRight2, 0.0F, 0.0F, 0.4363F);
        hornRight2.setTextureOffset(42, 140).addBox(-6.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);

        hornRightTip2 = new ModelRendererPlus(this);
        hornRightTip2.setDefaultRotPoint(-5.0F, 0.0F, 0.0F);
        hornRight2.addChild(hornRightTip2);
        setRotationAngle(hornRightTip2, 0.0F, 0.0F, 1.4114F);
        hornRightTip2.setTextureOffset(42, 140).addBox(-4.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);

        hornLeft2 = new ModelRendererPlus(this);
        hornLeft2.setDefaultRotPoint(3.0F, -3.0F, -4.0F);
        head2.addChild(hornLeft2);
        setRotationAngle(hornLeft2, 0.0F, 0.0F, -0.4363F);
        hornLeft2.setTextureOffset(42, 140).addBox(0.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);

        hornLeftTip2 = new ModelRendererPlus(this);
        hornLeftTip2.setDefaultRotPoint(5.0F, 0.0F, 0.0F);
        hornLeft2.addChild(hornLeftTip2);
        setRotationAngle(hornLeftTip2, 0.0F, 0.0F, -1.4114F);
        hornLeftTip2.setTextureOffset(42, 140).addBox(0.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);

        lead2 = new ModelRendererPlus(this);
        lead2.setDefaultRotPoint(0.0F, -5.7F, -0.5F);
        head2.addChild(lead2);
        lead2.setTextureOffset(0, 173).addBox(-4.0F, 0.0F, -0.5F, 8.0F, 0.0F, 1.0F, 0.0F, false);

        mouth2 = new ModelRendererPlus(this);
        mouth2.setDefaultRotPoint(0.0F, 1.0F, -7.0F);
        head2.addChild(mouth2);
        setRotationAngle(mouth2, 0.4363F, 0.0F, 0.0F);
        mouth2.setTextureOffset(42, 128).addBox(-2.5F, -2.5F, -5.0F, 5.0F, 5.0F, 5.0F, 0.0F, false);

        leadFront5 = new ModelRendererPlus(this);
        leadFront5.setDefaultRotPoint(2.5F, 1.0F, -3.0F);
        mouth2.addChild(leadFront5);
        setRotationAngle(leadFront5, 0.0F, 0.7854F, 0.0F);
        leadFront5.setTextureOffset(0, 167).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 6.0F, 0.0F, false);

        leadFront6 = new ModelRendererPlus(this);
        leadFront6.setDefaultRotPoint(0.0F, 0.0F, 6.0F);
        leadFront5.addChild(leadFront6);
        setRotationAngle(leadFront6, 0.0F, -1.2217F, -0.5236F);
        leadFront6.setTextureOffset(0, 164).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 9.0F, 0.0F, false);

        leadFront7 = new ModelRendererPlus(this);
        leadFront7.setDefaultRotPoint(-2.5F, 1.0F, -3.0F);
        mouth2.addChild(leadFront7);
        setRotationAngle(leadFront7, 0.0F, -0.7854F, 0.0F);
        leadFront7.setTextureOffset(0, 167).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 6.0F, 0.0F, false);

        leadFront8 = new ModelRendererPlus(this);
        leadFront8.setDefaultRotPoint(0.0F, 0.0F, 6.0F);
        leadFront7.addChild(leadFront8);
        setRotationAngle(leadFront8, 0.0F, 1.2217F, 0.5236F);
        leadFront8.setTextureOffset(0, 164).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 9.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(EntityGordiusBulls entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetModel();
        this.footLeftFront.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount;
        this.footRightFront.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
        this.footLeftFront2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
        this.footRightFront2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
        this.footLeftRear.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount;
        this.footRightRear.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
        this.footLeftRear2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount;
        this.footRightRear2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.bull1.render(matrixStack, buffer, packedLight, packedOverlay);
        this.bull2.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRendererPlus model, float x, float y, float z) {
        model.setDefaultRotAngle(x, y, z);
    }

    @Override
    public void resetModel() {
        this.bull1.reset();
        this.bull2.reset();
        this.resetChild(this.bull1);
        this.resetChild(this.bull2);
    }
}