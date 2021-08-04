package io.github.flemmli97.fate.client.model;

import com.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import com.flemmli97.tenshilib.client.model.IResetModel;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.common.entity.minions.EntityPegasus;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

public class ModelPegasus extends EntityModel<EntityPegasus> implements IResetModel {
    public ModelRendererPlus body;
    public ModelRendererPlus neck;
    public ModelRendererPlus head;
    public ModelRendererPlus leftFrontLegBase;
    public ModelRendererPlus leftFrontLeg;
    public ModelRendererPlus leftFrontLegDown;
    public ModelRendererPlus leftFrontHoove;
    public ModelRendererPlus rightFrontLegBase;
    public ModelRendererPlus rightFrontLeg;
    public ModelRendererPlus rightFrontLegDown;
    public ModelRendererPlus rightFrontHoove;
    public ModelRendererPlus leftBackLegBase;
    public ModelRendererPlus leftBackLeg;
    public ModelRendererPlus leftBackLegDown;
    public ModelRendererPlus leftBackHoove;
    public ModelRendererPlus rightBackLegBase;
    public ModelRendererPlus rightBackLeg;
    public ModelRendererPlus rightBackLegDown;
    public ModelRendererPlus rightBackHoove;
    public ModelRendererPlus tail;
    public ModelRendererPlus tailTip;
    public ModelRendererPlus wingBase;
    public ModelRendererPlus bone;
    public ModelRendererPlus bone2;
    public ModelRendererPlus bone3;
    public ModelRendererPlus wingBase2;
    public ModelRendererPlus bone4;
    public ModelRendererPlus bone5;
    public ModelRendererPlus bone6;

    public final BlockBenchAnimations anim;

    public ModelPegasus() {
        textureWidth = 16;
        textureHeight = 16;

        body = new ModelRendererPlus(this);
        body.setDefaultRotPoint(0.0F, 5.0F, 0.0F);
        body.setTextureOffset(0, 0).addBox(-6.5F, -6.0F, -13.0F, 12.0F, 12.0F, 26.0F, 0.0F, false);

        neck = new ModelRendererPlus(this);
        neck.setDefaultRotPoint(-1.0F, 0.0F, -12.0F);
        body.addChild(neck);
        setRotationAngle(neck, 0.5236F, 0.0F, 0.0F);
        neck.setTextureOffset(0, 0).addBox(-2.0F, -11.0F, 0.0F, 5.0F, 10.0F, 7.0F, 0.0F, false);
        neck.setTextureOffset(0, 0).addBox(0.0F, -11.0F, 1.0F, 1.0F, 10.0F, 9.0F, 0.0F, false);

        head = new ModelRendererPlus(this);
        head.setDefaultRotPoint(1.0F, -14.0F, -2.0F);
        neck.addChild(head);
        head.setTextureOffset(0, 0).addBox(-5.0F, -5.0F, -1.0F, 9.0F, 8.0F, 12.0F, 0.0F, false);
        head.setTextureOffset(0, 0).addBox(1.0F, -8.0F, 7.0F, 3.0F, 3.0F, 2.0F, 0.0F, false);
        head.setTextureOffset(0, 0).addBox(-5.0F, -8.0F, 7.0F, 3.0F, 3.0F, 2.0F, 0.0F, false);
        head.setTextureOffset(0, 0).addBox(-1.0F, -8.0F, 3.0F, 1.0F, 11.0F, 10.0F, 0.0F, false);
        head.setTextureOffset(0, 0).addBox(-3.5F, -5.0F, -6.0F, 7.0F, 8.0F, 5.0F, 0.0F, false);
        head.setTextureOffset(0, 0).addBox(-2.5F, -4.5F, -10.0F, 5.0F, 7.0F, 4.0F, 0.0F, false);

        leftFrontLegBase = new ModelRendererPlus(this);
        leftFrontLegBase.setDefaultRotPoint(3.5F, 4.0F, -13.0F);
        body.addChild(leftFrontLegBase);
        leftFrontLegBase.setTextureOffset(0, 0).addBox(-1.5F, -1.0F, -0.5F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        leftFrontLeg = new ModelRendererPlus(this);
        leftFrontLeg.setDefaultRotPoint(0.0F, 3.0F, 3.0F);
        leftFrontLegBase.addChild(leftFrontLeg);
        leftFrontLeg.setTextureOffset(0, 0).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, 0.0F, false);

        leftFrontLegDown = new ModelRendererPlus(this);
        leftFrontLegDown.setDefaultRotPoint(0.5F, 5.0F, -3.0F);
        leftFrontLeg.addChild(leftFrontLegDown);
        leftFrontLegDown.setTextureOffset(0, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 3.0F, 0.0F, false);

        leftFrontHoove = new ModelRendererPlus(this);
        leftFrontHoove.setDefaultRotPoint(0.0F, 5.0F, 1.5F);
        leftFrontLegDown.addChild(leftFrontHoove);
        leftFrontHoove.setTextureOffset(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.0F, false);

        rightFrontLegBase = new ModelRendererPlus(this);
        rightFrontLegBase.setDefaultRotPoint(-5.5F, 4.0F, -13.0F);
        body.addChild(rightFrontLegBase);
        rightFrontLegBase.setTextureOffset(0, 0).addBox(-1.5F, -1.0F, -0.5F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        rightFrontLeg = new ModelRendererPlus(this);
        rightFrontLeg.setDefaultRotPoint(0.0F, 3.0F, 3.0F);
        rightFrontLegBase.addChild(rightFrontLeg);
        rightFrontLeg.setTextureOffset(0, 0).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, 0.0F, false);

        rightFrontLegDown = new ModelRendererPlus(this);
        rightFrontLegDown.setDefaultRotPoint(0.5F, 5.0F, -3.0F);
        rightFrontLeg.addChild(rightFrontLegDown);
        rightFrontLegDown.setTextureOffset(0, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 3.0F, 0.0F, false);

        rightFrontHoove = new ModelRendererPlus(this);
        rightFrontHoove.setDefaultRotPoint(0.0F, 5.0F, 1.5F);
        rightFrontLegDown.addChild(rightFrontHoove);
        rightFrontHoove.setTextureOffset(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.0F, false);

        leftBackLegBase = new ModelRendererPlus(this);
        leftBackLegBase.setDefaultRotPoint(3.5F, 4.0F, 9.6F);
        body.addChild(leftBackLegBase);
        leftBackLegBase.setTextureOffset(0, 0).addBox(-2.5F, -4.0F, -3.5F, 5.0F, 7.0F, 7.0F, 0.0F, false);

        leftBackLeg = new ModelRendererPlus(this);
        leftBackLeg.setDefaultRotPoint(0.0F, 3.0F, -1.5F);
        leftBackLegBase.addChild(leftBackLeg);
        setRotationAngle(leftBackLeg, 0.3927F, 0.0F, 0.0F);
        leftBackLeg.setTextureOffset(0, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 4.0F, 0.0F, false);

        leftBackLegDown = new ModelRendererPlus(this);
        leftBackLegDown.setDefaultRotPoint(0.0F, 6.0F, 4.0F);
        leftBackLeg.addChild(leftBackLegDown);
        setRotationAngle(leftBackLegDown, -0.3927F, 0.0F, 0.0F);
        leftBackLegDown.setTextureOffset(0, 0).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 7.0F, 3.0F, 0.0F, false);

        leftBackHoove = new ModelRendererPlus(this);
        leftBackHoove.setDefaultRotPoint(-0.5F, 6.3007F, -1.5463F);
        leftBackLegDown.addChild(leftBackHoove);
        leftBackHoove.setTextureOffset(0, 0).addBox(-1.5F, -0.3007F, -1.9537F, 4.0F, 2.0F, 4.0F, 0.0F, false);

        rightBackLegBase = new ModelRendererPlus(this);
        rightBackLegBase.setDefaultRotPoint(-4.5F, 4.0F, 9.6F);
        body.addChild(rightBackLegBase);
        rightBackLegBase.setTextureOffset(0, 0).addBox(-2.5F, -4.0F, -3.5F, 5.0F, 7.0F, 7.0F, 0.0F, false);

        rightBackLeg = new ModelRendererPlus(this);
        rightBackLeg.setDefaultRotPoint(0.0F, 3.0F, -1.5F);
        rightBackLegBase.addChild(rightBackLeg);
        setRotationAngle(rightBackLeg, 0.3927F, 0.0F, 0.0F);
        rightBackLeg.setTextureOffset(0, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 4.0F, 0.0F, false);

        rightBackLegDown = new ModelRendererPlus(this);
        rightBackLegDown.setDefaultRotPoint(0.0F, 6.0F, 4.0F);
        rightBackLeg.addChild(rightBackLegDown);
        setRotationAngle(rightBackLegDown, -0.3927F, 0.0F, 0.0F);
        rightBackLegDown.setTextureOffset(0, 0).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 7.0F, 3.0F, 0.0F, false);

        rightBackHoove = new ModelRendererPlus(this);
        rightBackHoove.setDefaultRotPoint(0.0F, 6.0F, -1.5F);
        rightBackLegDown.addChild(rightBackHoove);
        rightBackHoove.setTextureOffset(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.0F, false);

        tail = new ModelRendererPlus(this);
        tail.setDefaultRotPoint(0.0F, -4.5F, 12.0F);
        body.addChild(tail);
        setRotationAngle(tail, 0.6545F, 0.0F, 0.0F);
        tail.setTextureOffset(0, 0).addBox(-2.5F, 0.0F, -1.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

        tailTip = new ModelRendererPlus(this);
        tailTip.setDefaultRotPoint(0.0F, 6.0F, 3.0F);
        tail.addChild(tailTip);
        setRotationAngle(tailTip, -0.5236F, 0.0F, 0.0F);
        tailTip.setTextureOffset(0, 0).addBox(-1.5F, 0.0F, -3.5F, 2.0F, 13.0F, 3.0F, 0.0F, false);

        wingBase = new ModelRendererPlus(this);
        wingBase.setDefaultRotPoint(-6.0F, -4.0F, 0.0F);
        body.addChild(wingBase);
        setRotationAngle(wingBase, 0.0F, 0.0F, 0.3054F);
        wingBase.setTextureOffset(0, 0).addBox(-6.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);
        wingBase.setTextureOffset(0, 0).addBox(-6.0F, -0.5F, 1.0F, 6.0F, 0.0F, 7.0F, 0.0F, false);
        wingBase.setTextureOffset(0, 0).addBox(-6.0F, 0.5F, 1.0F, 6.0F, 0.0F, 7.0F, 0.0F, false);

        bone = new ModelRendererPlus(this);
        bone.setDefaultRotPoint(-6.0F, 1.0F, -1.0F);
        wingBase.addChild(bone);
        setRotationAngle(bone, 0.0F, 0.0F, 0.7854F);
        bone.setTextureOffset(0, 0).addBox(-12.0F, -2.0F, 0.0F, 12.0F, 2.0F, 2.0F, 0.0F, false);
        bone.setTextureOffset(0, 0).addBox(-12.0F, -1.5F, 2.0F, 12.0F, 0.0F, 13.0F, 0.0F, false);
        bone.setTextureOffset(0, 0).addBox(-12.0F, -0.5F, 2.0F, 12.0F, 0.0F, 13.0F, 0.0F, false);

        bone2 = new ModelRendererPlus(this);
        bone2.setDefaultRotPoint(-12.0F, -2.0F, 0.0F);
        bone.addChild(bone2);
        setRotationAngle(bone2, 0.0F, 0.0F, -0.5236F);
        bone2.setTextureOffset(0, 0).addBox(-15.0F, 0.0F, 0.0F, 15.0F, 2.0F, 2.0F, 0.0F, false);
        bone2.setTextureOffset(0, 0).addBox(-15.0F, 0.5F, 2.0F, 15.0F, 0.0F, 16.0F, 0.0F, false);
        bone2.setTextureOffset(0, 0).addBox(-15.0F, 1.5F, 2.0F, 15.0F, 0.0F, 16.0F, 0.0F, false);

        bone3 = new ModelRendererPlus(this);
        bone3.setDefaultRotPoint(-15.0F, 0.0F, 0.0F);
        bone2.addChild(bone3);
        setRotationAngle(bone3, 0.0F, 0.0F, -0.9599F);
        bone3.setTextureOffset(0, 0).addBox(-30.0F, 0.0F, 0.0F, 30.0F, 2.0F, 2.0F, 0.0F, false);
        bone3.setTextureOffset(0, 0).addBox(-30.0F, 0.5F, 2.0F, 30.0F, 0.0F, 20.0F, 0.0F, false);
        bone3.setTextureOffset(0, 0).addBox(-30.0F, 1.5F, 2.0F, 30.0F, 0.0F, 20.0F, 0.0F, false);

        wingBase2 = new ModelRendererPlus(this);
        wingBase2.setDefaultRotPoint(5.0F, -4.0F, 0.0F);
        body.addChild(wingBase2);
        setRotationAngle(wingBase2, 0.0F, 0.0F, -0.3054F);
        wingBase2.setTextureOffset(0, 0).addBox(0.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);
        wingBase2.setTextureOffset(0, 0).addBox(0.0F, -0.5F, 1.0F, 6.0F, 0.0F, 7.0F, 0.0F, false);
        wingBase2.setTextureOffset(0, 0).addBox(0.0F, 0.5F, 1.0F, 6.0F, 0.0F, 7.0F, 0.0F, false);

        bone4 = new ModelRendererPlus(this);
        bone4.setDefaultRotPoint(6.0F, 1.0F, -1.0F);
        wingBase2.addChild(bone4);
        setRotationAngle(bone4, 0.0F, 0.0F, -0.7854F);
        bone4.setTextureOffset(0, 0).addBox(0.0F, -2.0F, 0.0F, 12.0F, 2.0F, 2.0F, 0.0F, false);
        bone4.setTextureOffset(0, 0).addBox(0.0F, -1.5F, 2.0F, 12.0F, 0.0F, 13.0F, 0.0F, false);
        bone4.setTextureOffset(0, 0).addBox(0.0F, -0.5F, 2.0F, 12.0F, 0.0F, 13.0F, 0.0F, false);

        bone5 = new ModelRendererPlus(this);
        bone5.setDefaultRotPoint(12.0F, -2.0F, 0.0F);
        bone4.addChild(bone5);
        setRotationAngle(bone5, 0.0F, 0.0F, 0.5236F);
        bone5.setTextureOffset(0, 0).addBox(0.0F, 0.0F, 0.0F, 15.0F, 2.0F, 2.0F, 0.0F, false);
        bone5.setTextureOffset(0, 0).addBox(0.0F, 0.5F, 2.0F, 15.0F, 0.0F, 16.0F, 0.0F, false);
        bone5.setTextureOffset(0, 0).addBox(0.0F, 1.5F, 2.0F, 15.0F, 0.0F, 16.0F, 0.0F, false);

        bone6 = new ModelRendererPlus(this);
        bone6.setDefaultRotPoint(15.0F, 0.0F, 0.0F);
        bone5.addChild(bone6);
        setRotationAngle(bone6, 0.0F, 0.0F, 0.9599F);
        bone6.setTextureOffset(0, 0).addBox(0.0F, 0.0F, 0.0F, 30.0F, 2.0F, 2.0F, 0.0F, false);
        bone6.setTextureOffset(0, 0).addBox(0.0F, 0.5F, 2.0F, 30.0F, 0.0F, 20.0F, 0.0F, false);
        bone6.setTextureOffset(0, 0).addBox(0.0F, 1.5F, 2.0F, 30.0F, 0.0F, 20.0F, 0.0F, false);

        this.anim = new BlockBenchAnimations(this, new ResourceLocation(Fate.MODID, "models/entity/animation/pegasus.json"));
    }

    @Override
    public void setRotationAngles(EntityPegasus entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetModel();
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRendererPlus modelRenderer, float x, float y, float z) {
        modelRenderer.setDefaultRotAngle(x, y, z);
    }

    @Override
    public void resetModel() {
        this.body.reset();
        this.resetChild(this.body);
    }
}
