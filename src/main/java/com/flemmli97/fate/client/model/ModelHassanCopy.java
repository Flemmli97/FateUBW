package com.flemmli97.fate.client.model;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.entity.EntityHassanCopy;
import com.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import com.flemmli97.tenshilib.client.model.IResetModel;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class ModelHassanCopy extends EntityModel<EntityHassanCopy> implements IResetModel, IHasArm, IHasHead {

    public ModelRendererPlus servantHead;
    public ModelRendererPlus servantHeadOverlay;
    public ModelRendererPlus servantBody;
    public ModelRendererPlus servantBodyOverlay;

    public ModelRendererPlus servantRightArmUp;
    public ModelRendererPlus servantRightArmUpOverlay;
    public ModelRendererPlus servantRightArmJoint;
    public ModelRendererPlus servantRightArmOverlayJoint;
    public ModelRendererPlus servantRightArmDown;
    public ModelRendererPlus servantRightArmDownOverlay;

    public ModelRendererPlus servantLeftArmUp;
    public ModelRendererPlus servantLeftArmUpOverlay;
    public ModelRendererPlus servantLeftArmJoint;
    public ModelRendererPlus servantLeftArmOverlayJoint;
    public ModelRendererPlus servantLeftArmDown;
    public ModelRendererPlus servantLeftArmDownOverlay;

    public ModelRendererPlus servantRightLegUp;
    public ModelRendererPlus servantRightLegUpOverlay;
    public ModelRendererPlus servantRightLegDown;
    public ModelRendererPlus servantRightLegDownOverlay;

    public ModelRendererPlus servantLeftLegUp;
    public ModelRendererPlus servantLeftLegUpOverlay;
    public ModelRendererPlus servantLeftLegDown;
    public ModelRendererPlus servantLeftLegDownOverlay;

    public int heldItemMain, heldItemOff;

    public final BlockBenchAnimations anim;

    public ModelHassanCopy() {
        super(RenderType::getEntityTranslucent);
        this.textureWidth = 64;
        this.textureHeight = 64;

        this.servantHead = new ModelRendererPlus(this, 0, 0);
        this.servantHead.addCuboid(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0);
        this.servantHead.setDefaultRotPoint(0, 0, 0);
        this.servantHeadOverlay = new ModelRendererPlus(this, 32, 0);
        this.servantHeadOverlay.addCuboid(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
        this.servantHeadOverlay.setDefaultRotPoint(0, 0, 0);

        this.servantBody = new ModelRendererPlus(this, 16, 16);
        this.servantBody.addCuboid(-4.0F, 0, -2.0F, 8, 12, 4, 0);
        this.servantBody.setDefaultRotPoint(0, 0, 0);
        this.servantBodyOverlay = new ModelRendererPlus(this, 16, 32);
        this.servantBodyOverlay.addCuboid(-4.0F, 0, -2.0F, 8, 12, 4, 0.25F);
        this.servantBodyOverlay.setDefaultRotPoint(0, 0, 0);

        this.servantLeftArmUp = new ModelRendererPlus(this, 40, 16);
        this.servantLeftArmUp.mirror = true;
        this.servantLeftArmUp.addCuboid(-1.0F, -2.0F, -2.0F, 4, 6, 4, 0);
        this.servantLeftArmUp.setDefaultRotPoint(5.0F, 2.0F, 0);
        this.servantLeftArmUpOverlay = new ModelRendererPlus(this, 40, 32);
        this.servantLeftArmUpOverlay.mirror = true;
        this.servantLeftArmUpOverlay.addCuboid(-1.0F, -2.0F, -2.0F, 4, 6, 4, 0.25F);
        this.servantLeftArmUpOverlay.setDefaultRotPoint(5.0F, 2.0F, 0);

        this.servantLeftArmJoint = new ModelRendererPlus(this, 0, 0);
        this.servantLeftArmJoint.addCuboid(0, 0, 0, 0, 0, 0);
        this.servantLeftArmJoint.setDefaultRotPoint(3.0F, 4.0F, 0);
        this.servantLeftArmUp.addChild(this.servantLeftArmJoint);
        this.servantLeftArmOverlayJoint = new ModelRendererPlus(this, 0, 0);
        this.servantLeftArmOverlayJoint.addCuboid(0, 0, 0, 0, 0, 0);
        this.servantLeftArmOverlayJoint.setDefaultRotPoint(3.0F, 4.0F, 0);
        this.servantLeftArmUpOverlay.addChild(this.servantLeftArmOverlayJoint);

        this.servantLeftArmDown = new ModelRendererPlus(this, 32, 54);
        this.servantLeftArmDown.mirror = true;
        this.servantLeftArmDown.addCuboid(-4.0F, 0, -2.0F, 4, 6, 4, 0);
        this.servantLeftArmDown.setDefaultRotPoint(0, 0, 0);
        this.servantLeftArmJoint.addChild(this.servantLeftArmDown);
        this.servantLeftArmDownOverlay = new ModelRendererPlus(this, 48, 54);
        this.servantLeftArmDownOverlay.mirror = true;
        this.servantLeftArmDownOverlay.addCuboid(-4.0F, 0, -2.0F, 4, 6, 4, 0.25F);
        this.servantLeftArmDownOverlay.setDefaultRotPoint(0, 0, 0);
        this.servantLeftArmOverlayJoint.addChild(this.servantLeftArmDownOverlay);

        this.servantRightArmUp = new ModelRendererPlus(this, 40, 16);
        this.servantRightArmUp.addCuboid(-3.0F, -2.0F, -2.0F, 4, 6, 4, 0);
        this.servantRightArmUp.setDefaultRotPoint(-5.0F, 2.0F, 0);
        this.servantRightArmUpOverlay = new ModelRendererPlus(this, 40, 32);
        this.servantRightArmUpOverlay.addCuboid(-3.0F, -2.0F, -2.0F, 4, 6, 4, 0.25F);
        this.servantRightArmUpOverlay.setDefaultRotPoint(-5.0F, 2.0F, 0);

        this.servantRightArmJoint = new ModelRendererPlus(this, 0, 0);
        this.servantRightArmJoint.addCuboid(0, 0, 0, 0, 0, 0);
        this.servantRightArmJoint.setDefaultRotPoint(-3.0F, 4.0F, 0);
        this.servantRightArmUp.addChild(this.servantRightArmJoint);
        this.servantRightArmOverlayJoint = new ModelRendererPlus(this, 0, 0);
        this.servantRightArmOverlayJoint.addCuboid(0, 0, 0, 0, 0, 0);
        this.servantRightArmOverlayJoint.setDefaultRotPoint(-3.0F, 4.0F, 0);
        this.servantRightArmUpOverlay.addChild(this.servantRightArmOverlayJoint);

        this.servantRightArmDown = new ModelRendererPlus(this, 32, 54);
        this.servantRightArmDown.addCuboid(0, 0, -2.0F, 4, 6, 4, 0);
        this.servantRightArmDown.setDefaultRotPoint(0, 0, 0);
        this.servantRightArmJoint.addChild(this.servantRightArmDown);
        this.servantRightArmDownOverlay = new ModelRendererPlus(this, 48, 54);
        this.servantRightArmDownOverlay.addCuboid(0, 0, -2.0F, 4, 6, 4, 0.25F);
        this.servantRightArmDownOverlay.setDefaultRotPoint(0, 0, 0);
        this.servantRightArmOverlayJoint.addChild(this.servantRightArmDownOverlay);

        this.servantLeftLegUp = new ModelRendererPlus(this, 0, 16);
        this.servantLeftLegUp.mirror = true;
        this.servantLeftLegUp.addCuboid(-2.0F, 0, -2.0F, 4, 6, 4, 0);
        this.servantLeftLegUp.setDefaultRotPoint(1.9F, 12.0F, 0);
        this.servantLeftLegUpOverlay = new ModelRendererPlus(this, 0, 32);
        this.servantLeftLegUpOverlay.mirror = true;
        this.servantLeftLegUpOverlay.addCuboid(-2.0F, 0, -2.0F, 4, 6, 4, 0.25F);
        this.servantLeftLegUpOverlay.setDefaultRotPoint(1.9F, 12.0F, 0);

        this.servantLeftLegDown = new ModelRendererPlus(this, 16, 54);
        this.servantLeftLegDown.mirror = true;
        this.servantLeftLegDown.addCuboid(-2.0F, 0, 0, 4, 6, 4, 0);
        this.servantLeftLegDown.setDefaultRotPoint(0, 6.0F, -2.0F);
        this.servantLeftLegUp.addChild(this.servantLeftLegDown);
        this.servantLeftLegDownOverlay = new ModelRendererPlus(this, 0, 54);
        this.servantLeftLegDownOverlay.mirror = true;
        this.servantLeftLegDownOverlay.addCuboid(-2.0F, 0, 0, 4, 6, 4, 0.25F);
        this.servantLeftLegDownOverlay.setDefaultRotPoint(0, 6.0F, -2.0F);
        this.servantLeftLegUpOverlay.addChild(this.servantLeftLegDownOverlay);

        this.servantRightLegUp = new ModelRendererPlus(this, 0, 16);
        this.servantRightLegUp.addCuboid(-2.0F, 0, -2.0F, 4, 6, 4, 0);
        this.servantRightLegUp.setDefaultRotPoint(-1.9F, 12.0F, 0);
        this.servantRightLegUpOverlay = new ModelRendererPlus(this, 0, 32);
        this.servantRightLegUpOverlay.addCuboid(-2.0F, 0, -2.0F, 4, 6, 4, 0.25F);
        this.servantRightLegUpOverlay.setRotationPoint(-1.9F, 12.0F, 0);

        this.servantRightLegDown = new ModelRendererPlus(this, 16, 54);
        this.servantRightLegDown.addCuboid(-2.0F, 0, 0, 4, 6, 4, 0);
        this.servantRightLegDown.setDefaultRotPoint(0, 6.0F, -2.0F);
        this.servantRightLegUp.addChild(this.servantRightLegDown);
        this.servantRightLegDownOverlay = new ModelRendererPlus(this, 0, 54);
        this.servantRightLegDownOverlay.addCuboid(-2.0F, 0, 0, 4, 6, 4, 0.25F);
        this.servantRightLegDownOverlay.setDefaultRotPoint(0, 6.0F, -2.0F);
        this.servantRightLegUpOverlay.addChild(this.servantRightLegDownOverlay);

        this.servantBody.addChild(this.servantHead);
        this.servantBody.addChild(this.servantRightArmUp);
        this.servantBody.addChild(this.servantLeftArmUp);
        this.servantBody.addChild(this.servantRightLegUp);
        this.servantBody.addChild(this.servantLeftLegUp);

        this.servantBodyOverlay.addChild(this.servantHeadOverlay);
        this.servantBodyOverlay.addChild(this.servantRightArmUpOverlay);
        this.servantBodyOverlay.addChild(this.servantLeftArmUpOverlay);
        this.servantBodyOverlay.addChild(this.servantRightLegUpOverlay);
        this.servantBodyOverlay.addChild(this.servantLeftLegUpOverlay);

        this.anim = new BlockBenchAnimations(this, new ResourceLocation(Fate.MODID, "models/entity/animation/hassan.json"));
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.servantBody.render(matrixStack, buffer, packedLight, packedOverlay);
        this.servantBodyOverlay.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setAngles(EntityHassanCopy servant, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.setAnglesPre(servant, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
        AnimatedAction anim = servant.getAnimation();
        if (anim != null)
            this.anim.doAnimation(anim.getID(), anim.getTick(), partialTicks);
        this.syncOverlay();
    }

    public void setAnglesPre(EntityHassanCopy servant, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetModel();
        this.servantHead.rotateAngleY = netHeadYaw / (180F / (float) Math.PI);
        this.servantHead.rotateAngleX = headPitch / (180F / (float) Math.PI);
        this.servantHeadOverlay.rotateAngleY = this.servantHead.rotateAngleY;
        this.servantHeadOverlay.rotateAngleX = this.servantHead.rotateAngleX;

        this.servantRightArmUp.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        this.servantLeftArmUp.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        this.servantRightArmUp.rotateAngleZ = 0;
        this.servantLeftArmUp.rotateAngleZ = 0;
        this.servantRightLegUp.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.servantLeftLegUp.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.servantRightLegUp.rotateAngleY = 0;
        this.servantLeftLegUp.rotateAngleY = 0;

        if (this.isSitting) {
            this.servantRightArmUp.rotateAngleX += -((float) Math.PI / 5F);
            this.servantLeftArmUp.rotateAngleX += -((float) Math.PI / 5F);
            this.servantRightLegUp.rotateAngleX = -((float) Math.PI * 2F / 5F);
            this.servantLeftLegUp.rotateAngleX = -((float) Math.PI * 2F / 5F);
            this.servantRightLegUp.rotateAngleY = ((float) Math.PI / 10F);
            this.servantLeftLegUp.rotateAngleY = -((float) Math.PI / 10F);
        }

        if (this.heldItemOff == 1) {
            this.servantLeftArmUp.rotateAngleX = this.servantLeftArmUp.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
        }
        if (this.heldItemMain == 1) {
            this.servantRightArmUp.rotateAngleX = this.servantRightArmUp.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
        }

        this.servantRightArmUp.rotateAngleY = 0;
        this.servantLeftArmUp.rotateAngleY = 0;
        float var8;
        float var9;

        if (this.swingProgress > -9990) {
            var8 = this.swingProgress;
            this.servantBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt(var8) * (float) Math.PI * 2.0F) * 0.2F;
            this.servantRightArmUp.rotationPointZ = MathHelper.sin(this.servantBody.rotateAngleY) * 5.0F;
            this.servantRightArmUp.rotationPointX = -MathHelper.cos(this.servantBody.rotateAngleY) * 5.0F;
            this.servantLeftArmUp.rotationPointZ = -MathHelper.sin(this.servantBody.rotateAngleY) * 5.0F;
            this.servantLeftArmUp.rotationPointX = MathHelper.cos(this.servantBody.rotateAngleY) * 5.0F;
            this.servantRightArmUp.rotateAngleY += this.servantBody.rotateAngleY;
            this.servantLeftArmUp.rotateAngleY += this.servantBody.rotateAngleY;
            this.servantLeftArmUp.rotateAngleX += this.servantBody.rotateAngleY;
            var8 = 1.0F - this.swingProgress;
            var8 *= var8;
            var8 *= var8;
            var8 = 1.0F - var8;
            var9 = MathHelper.sin(var8 * (float) Math.PI);
            float var10 = MathHelper.sin(this.swingProgress * (float) Math.PI) * -(this.servantHead.rotateAngleX - 0.7F) * 0.75F;
            this.servantRightArmUp.rotateAngleX = (float) ((double) this.servantRightArmUp.rotateAngleX - ((double) var9 * 1.2D + (double) var10));
            this.servantRightArmUp.rotateAngleY += this.servantBody.rotateAngleY * 2.0F;
            this.servantRightArmUp.rotateAngleZ = MathHelper.sin(this.swingProgress * (float) Math.PI) * -0.4F;
        }

        this.servantBody.rotateAngleX = 0;
        this.servantRightLegUp.rotationPointZ = 0.1F;
        this.servantLeftLegUp.rotationPointZ = 0.1F;
        this.servantRightLegUp.rotationPointY = 12.0F;
        this.servantLeftLegUp.rotationPointY = 12.0F;
        this.servantHead.rotationPointY = 0;
        this.servantHeadOverlay.rotationPointY = 0;

        this.servantRightArmUp.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.servantLeftArmUp.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.servantRightArmUp.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        this.servantLeftArmUp.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
    }

    public void syncOverlay() {
        this.servantLeftLegUpOverlay.copyModelAngles(this.servantLeftLegUp);
        this.servantRightLegUpOverlay.copyModelAngles(this.servantRightLegUp);

        this.servantLeftArmUpOverlay.copyModelAngles(this.servantLeftArmUp);
        this.servantRightArmUpOverlay.copyModelAngles(this.servantRightArmUp);

        this.servantRightArmOverlayJoint.copyModelAngles(this.servantRightArmJoint);
        this.servantLeftArmOverlayJoint.copyModelAngles(this.servantLeftArmJoint);

        this.servantRightArmDownOverlay.copyModelAngles(this.servantRightArmDown);
        this.servantLeftArmDownOverlay.copyModelAngles(this.servantLeftArmDown);

        this.servantLeftLegDownOverlay.copyModelAngles(this.servantLeftLegDown);
        this.servantRightLegDownOverlay.copyModelAngles(this.servantRightLegDown);

        this.servantHeadOverlay.copyModelAngles(this.servantHead);

        this.servantBodyOverlay.copyModelAngles(this.servantBody);
    }

    @Override
    public void resetModel() {
        //Rotation point reset
        this.servantHead.reset();
        this.servantBody.reset();

        this.servantLeftArmUp.reset();
        this.servantLeftArmJoint.reset();
        this.servantLeftArmDown.reset();

        this.servantRightArmUp.reset();
        this.servantRightArmJoint.reset();
        this.servantRightArmDown.reset();

        this.servantLeftLegUp.reset();
        this.servantLeftLegDown.reset();
        this.servantRightLegUp.reset();
        this.servantRightLegDown.reset();
        this.syncOverlay();
    }

    @Override
    public void setArmAngle(HandSide side, MatrixStack stack) {
        if (side == HandSide.LEFT) {
            this.servantLeftArmUp.rotate(stack);
            this.servantLeftArmJoint.rotate(stack);
            this.servantLeftArmDown.rotate(stack);
        } else {
            this.servantRightArmUp.rotate(stack);
            this.servantRightArmJoint.rotate(stack);
            this.servantRightArmDown.rotate(stack);
        }
    }

    @Override
    public ModelRenderer func_205072_a() {
        return this.servantHead;
    }
}