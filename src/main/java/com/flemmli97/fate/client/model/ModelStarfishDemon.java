package com.flemmli97.fate.client.model;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.entity.EntityLesserMonster;
import com.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import com.flemmli97.tenshilib.client.model.IResetModel;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

/**
 * Starfish Demons - Black_Saturn
 * Created using Tabula 7.0.0
 */
public class ModelStarfishDemon<T extends EntityLesserMonster> extends EntityModel<T> implements IResetModel {

    public ModelRendererPlus mouthBottom;
    public ModelRendererPlus mouthSide1;
    public ModelRendererPlus tentacleP1S1;
    public ModelRendererPlus tentacleP2S1;
    public ModelRendererPlus tentacleP3S1;
    public ModelRendererPlus mouthSide2;
    public ModelRendererPlus tentacleP1S2;
    public ModelRendererPlus tentacleP2S2;
    public ModelRendererPlus tentacleP3S2;
    public ModelRendererPlus mouthSide3;
    public ModelRendererPlus tentacleP1S3;
    public ModelRendererPlus tentacleP2S3;
    public ModelRendererPlus tentacleP3S3;
    public ModelRendererPlus mouthSide4;
    public ModelRendererPlus tentacleP1S4;
    public ModelRendererPlus tentacleP2S4;
    public ModelRendererPlus tentacleP3S4;
    public ModelRendererPlus mouthSide5;
    public ModelRendererPlus tentacleP1S5;
    public ModelRendererPlus tentacleP2S5;
    public ModelRendererPlus tentacleP3S5;
    public ModelRendererPlus mouthSide6;
    public ModelRendererPlus tentacleP1S6;
    public ModelRendererPlus tentacleP2S6;
    public ModelRendererPlus tentacleP3S6;

    public final BlockBenchAnimations anim;

    public ModelStarfishDemon() {
        this.textureWidth = 64;
        this.textureHeight = 64;

        this.mouthBottom = new ModelRendererPlus(this);
        this.mouthBottom.setDefaultRotPoint(0.0F, 13.0F, 0.0F);
        this.mouthBottom.setDefaultRotAngle(1.5708F, 0.0F, 0.5236F);
        this.mouthBottom.setTextureOffset(0, 3).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 1.0F, 6.0F, 0.0F, false);

        this.mouthSide1 = new ModelRendererPlus(this);
        this.mouthSide1.setDefaultRotPoint(0.0F, 0.0F, -3.0F);
        this.mouthBottom.addChild(this.mouthSide1);
        this.mouthSide1.setTextureOffset(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, 0.0F, true);

        this.tentacleP1S1 = new ModelRendererPlus(this);
        this.tentacleP1S1.setDefaultRotPoint(0.0F, 0.0F, -0.1F);
        this.mouthSide1.addChild(this.tentacleP1S1);
        this.tentacleP1S1.setDefaultRotAngle(-0.5236F, 0.0F, 0.0F);
        this.tentacleP1S1.setTextureOffset(16, 10).addBox(-2.0F, -1.0F, -5.0F, 4.0F, 2.0F, 5.0F, 0.0F, true);
        this.tentacleP1S1.setTextureOffset(0, 10).addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        this.tentacleP1S1.setTextureOffset(0, 10).addBox(-0.5F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        this.tentacleP2S1 = new ModelRendererPlus(this);
        this.tentacleP2S1.setDefaultRotPoint(0.0F, 0.0F, -4.6F);
        this.tentacleP1S1.addChild(this.tentacleP2S1);
        this.tentacleP2S1.setDefaultRotAngle(0.8727F, 0.0F, 0.0F);
        this.tentacleP2S1.setTextureOffset(0, 10).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 2.0F, 5.0F, 0.0F, true);
        this.tentacleP2S1.setTextureOffset(0, 10).addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        this.tentacleP2S1.setTextureOffset(0, 10).addBox(-0.5F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        this.tentacleP3S1 = new ModelRendererPlus(this);
        this.tentacleP3S1.setDefaultRotPoint(0.0F, 0.0F, -4.5F);
        this.tentacleP2S1.addChild(this.tentacleP3S1);
        this.tentacleP3S1.setDefaultRotAngle(-1.5708F, 0.0F, 0.0F);
        this.tentacleP3S1.setTextureOffset(0, 17).addBox(-1.0F, -0.5F, -4.0F, 2.0F, 1.0F, 4.0F, 0.0F, true);
        this.tentacleP3S1.setTextureOffset(0, 10).addBox(-0.5F, -1.0F, -2.2F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        this.tentacleP3S1.setTextureOffset(0, 10).addBox(-0.5F, -1.0F, -3.8F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        this.mouthSide2 = new ModelRendererPlus(this);
        this.mouthSide2.setDefaultRotPoint(2.6F, 0.0F, -1.5F);
        this.mouthBottom.addChild(this.mouthSide2);
        this.mouthSide2.setDefaultRotAngle(0.0F, -1.0472F, 0.0F);
        this.mouthSide2.setTextureOffset(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, 0.0F, true);

        this.tentacleP1S2 = new ModelRendererPlus(this);
        this.tentacleP1S2.setDefaultRotPoint(0.0F, 0.0F, -0.1F);
        this.mouthSide2.addChild(this.tentacleP1S2);
        this.tentacleP1S2.setDefaultRotAngle(-0.5236F, 0.0F, 0.0F);
        this.tentacleP1S2.setTextureOffset(16, 10).addBox(-2.0F, -1.0F, -5.0F, 4.0F, 2.0F, 5.0F, 0.0F, true);
        this.tentacleP1S2.setTextureOffset(0, 10).addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        this.tentacleP1S2.setTextureOffset(0, 10).addBox(-0.5F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        this.tentacleP2S2 = new ModelRendererPlus(this);
        this.tentacleP2S2.setDefaultRotPoint(0.0F, 0.0F, -4.6F);
        this.tentacleP1S2.addChild(this.tentacleP2S2);
        this.tentacleP2S2.setDefaultRotAngle(0.8727F, 0.0F, 0.0F);
        this.tentacleP2S2.setTextureOffset(0, 10).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 2.0F, 5.0F, 0.0F, true);
        this.tentacleP2S2.setTextureOffset(0, 10).addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        this.tentacleP2S2.setTextureOffset(0, 10).addBox(-0.5F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        this.tentacleP3S2 = new ModelRendererPlus(this);
        this.tentacleP3S2.setDefaultRotPoint(0.0F, 0.0F, -4.5F);
        this.tentacleP2S2.addChild(this.tentacleP3S2);
        this.tentacleP3S2.setDefaultRotAngle(-1.5708F, 0.0F, 0.0F);
        this.tentacleP3S2.setTextureOffset(0, 17).addBox(-1.0F, -0.5F, -4.0F, 2.0F, 1.0F, 4.0F, 0.0F, true);
        this.tentacleP3S2.setTextureOffset(0, 10).addBox(-0.5F, -1.0F, -2.2F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        this.tentacleP3S2.setTextureOffset(0, 10).addBox(-0.5F, -1.0F, -3.8F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        this.mouthSide3 = new ModelRendererPlus(this);
        this.mouthSide3.setDefaultRotPoint(2.6F, 0.0F, 1.5F);
        this.mouthBottom.addChild(this.mouthSide3);
        this.mouthSide3.setDefaultRotAngle(0.0F, -2.0944F, 0.0F);
        this.mouthSide3.setTextureOffset(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, 0.0F, true);

        this.tentacleP1S3 = new ModelRendererPlus(this);
        this.tentacleP1S3.setDefaultRotPoint(0.0F, 0.0F, -0.1F);
        this.mouthSide3.addChild(this.tentacleP1S3);
        this.tentacleP1S3.setDefaultRotAngle(-0.1745F, 0.0F, 0.0F);
        this.tentacleP1S3.setTextureOffset(16, 10).addBox(-2.0F, -1.0F, -5.0F, 4.0F, 2.0F, 5.0F, 0.0F, true);
        this.tentacleP1S3.setTextureOffset(0, 10).addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        this.tentacleP1S3.setTextureOffset(0, 10).addBox(-0.5F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        this.tentacleP2S3 = new ModelRendererPlus(this);
        this.tentacleP2S3.setDefaultRotPoint(0.0F, 0.0F, -4.6F);
        this.tentacleP1S3.addChild(this.tentacleP2S3);
        this.tentacleP2S3.setDefaultRotAngle(-0.3491F, 0.0F, 0.0F);
        this.tentacleP2S3.setTextureOffset(0, 10).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 2.0F, 5.0F, 0.0F, true);
        this.tentacleP2S3.setTextureOffset(0, 10).addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        this.tentacleP2S3.setTextureOffset(0, 10).addBox(-0.5F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        this.tentacleP3S3 = new ModelRendererPlus(this);
        this.tentacleP3S3.setDefaultRotPoint(0.0F, 0.0F, -4.5F);
        this.tentacleP2S3.addChild(this.tentacleP3S3);
        this.tentacleP3S3.setDefaultRotAngle(-0.5236F, 0.0F, 0.0F);
        this.tentacleP3S3.setTextureOffset(0, 17).addBox(-1.0F, -0.5F, -4.0F, 2.0F, 1.0F, 4.0F, 0.0F, true);
        this.tentacleP3S3.setTextureOffset(0, 10).addBox(-0.5F, -1.0F, -2.2F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        this.tentacleP3S3.setTextureOffset(0, 10).addBox(-0.5F, -1.0F, -3.8F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        this.mouthSide4 = new ModelRendererPlus(this);
        this.mouthSide4.setDefaultRotPoint(0.0F, 0.0F, 3.0F);
        this.mouthBottom.addChild(this.mouthSide4);
        this.mouthSide4.setDefaultRotAngle(0.0F, -3.1416F, 0.0F);
        this.mouthSide4.setTextureOffset(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, 0.0F, true);

        this.tentacleP1S4 = new ModelRendererPlus(this);
        this.tentacleP1S4.setDefaultRotPoint(0.0F, 0.0F, -0.1F);
        this.mouthSide4.addChild(this.tentacleP1S4);
        this.tentacleP1S4.setDefaultRotAngle(-0.1745F, 0.0F, 0.0F);
        this.tentacleP1S4.setTextureOffset(16, 10).addBox(-2.0F, -1.0F, -5.0F, 4.0F, 2.0F, 5.0F, 0.0F, true);
        this.tentacleP1S4.setTextureOffset(0, 10).addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        this.tentacleP1S4.setTextureOffset(0, 10).addBox(-0.5F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        this.tentacleP2S4 = new ModelRendererPlus(this);
        this.tentacleP2S4.setDefaultRotPoint(0.0F, 0.0F, -4.6F);
        this.tentacleP1S4.addChild(this.tentacleP2S4);
        this.tentacleP2S4.setDefaultRotAngle(-0.3491F, 0.0F, 0.0F);
        this.tentacleP2S4.setTextureOffset(0, 10).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 2.0F, 5.0F, 0.0F, true);
        this.tentacleP2S4.setTextureOffset(0, 10).addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        this.tentacleP2S4.setTextureOffset(0, 10).addBox(-0.5F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        this.tentacleP3S4 = new ModelRendererPlus(this);
        this.tentacleP3S4.setDefaultRotPoint(0.0F, 0.0F, -4.5F);
        this.tentacleP2S4.addChild(this.tentacleP3S4);
        this.tentacleP3S4.setDefaultRotAngle(-0.5236F, 0.0F, 0.0F);
        this.tentacleP3S4.setTextureOffset(0, 17).addBox(-1.0F, -0.5F, -4.0F, 2.0F, 1.0F, 4.0F, 0.0F, true);
        this.tentacleP3S4.setTextureOffset(0, 10).addBox(-0.5F, -1.0F, -2.2F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        this.tentacleP3S4.setTextureOffset(0, 10).addBox(-0.5F, -1.0F, -3.8F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        this.mouthSide5 = new ModelRendererPlus(this);
        this.mouthSide5.setDefaultRotPoint(-2.6F, 0.0F, 1.5F);
        this.mouthBottom.addChild(this.mouthSide5);
        this.mouthSide5.setDefaultRotAngle(0.0F, -4.1888F, 0.0F);
        this.mouthSide5.setTextureOffset(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, 0.0F, true);

        this.tentacleP1S5 = new ModelRendererPlus(this);
        this.tentacleP1S5.setDefaultRotPoint(0.0F, 0.0F, -0.1F);
        this.mouthSide5.addChild(this.tentacleP1S5);
        this.tentacleP1S5.setDefaultRotAngle(-0.1745F, 0.0F, 0.0F);
        this.tentacleP1S5.setTextureOffset(16, 10).addBox(-2.0F, -1.0F, -5.0F, 4.0F, 2.0F, 5.0F, 0.0F, true);
        this.tentacleP1S5.setTextureOffset(0, 10).addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        this.tentacleP1S5.setTextureOffset(0, 10).addBox(-0.5F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        this.tentacleP2S5 = new ModelRendererPlus(this);
        this.tentacleP2S5.setDefaultRotPoint(0.0F, 0.0F, -4.6F);
        this.tentacleP1S5.addChild(this.tentacleP2S5);
        this.tentacleP2S5.setDefaultRotAngle(-0.3491F, 0.0F, 0.0F);
        this.tentacleP2S5.setTextureOffset(0, 10).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 2.0F, 5.0F, 0.0F, true);
        this.tentacleP2S5.setTextureOffset(0, 10).addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        this.tentacleP2S5.setTextureOffset(0, 10).addBox(-0.5F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        this.tentacleP3S5 = new ModelRendererPlus(this);
        this.tentacleP3S5.setDefaultRotPoint(0.0F, 0.0F, -4.5F);
        this.tentacleP2S5.addChild(this.tentacleP3S5);
        this.tentacleP3S5.setDefaultRotAngle(-0.5236F, 0.0F, 0.0F);
        this.tentacleP3S5.setTextureOffset(0, 17).addBox(-1.0F, -0.5F, -4.0F, 2.0F, 1.0F, 4.0F, 0.0F, true);
        this.tentacleP3S5.setTextureOffset(0, 10).addBox(-0.5F, -1.0F, -2.2F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        this.tentacleP3S5.setTextureOffset(0, 10).addBox(-0.5F, -1.0F, -3.8F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        this.mouthSide6 = new ModelRendererPlus(this);
        this.mouthSide6.setDefaultRotPoint(-2.6F, 0.0F, -1.5F);
        this.mouthBottom.addChild(this.mouthSide6);
        this.mouthSide6.setDefaultRotAngle(0.0F, -5.236F, 0.0F);
        this.mouthSide6.setTextureOffset(0, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, 0.0F, true);

        this.tentacleP1S6 = new ModelRendererPlus(this);
        this.tentacleP1S6.setDefaultRotPoint(0.0F, 0.0F, -0.1F);
        this.mouthSide6.addChild(this.tentacleP1S6);
        this.tentacleP1S6.setDefaultRotAngle(-0.1745F, 0.0F, 0.0F);
        this.tentacleP1S6.setTextureOffset(16, 10).addBox(-2.0F, -1.0F, -5.0F, 4.0F, 2.0F, 5.0F, 0.0F, true);
        this.tentacleP1S6.setTextureOffset(0, 10).addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        this.tentacleP1S6.setTextureOffset(0, 10).addBox(-0.5F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        this.tentacleP2S6 = new ModelRendererPlus(this);
        this.tentacleP2S6.setDefaultRotPoint(0.0F, 0.0F, -4.6F);
        this.tentacleP1S6.addChild(this.tentacleP2S6);
        this.tentacleP2S6.setDefaultRotAngle(-0.3491F, 0.0F, 0.0F);
        this.tentacleP2S6.setTextureOffset(0, 10).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 2.0F, 5.0F, 0.0F, true);
        this.tentacleP2S6.setTextureOffset(0, 10).addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        this.tentacleP2S6.setTextureOffset(0, 10).addBox(-0.5F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        this.tentacleP3S6 = new ModelRendererPlus(this);
        this.tentacleP3S6.setDefaultRotPoint(0.0F, 0.0F, -4.5F);
        this.tentacleP2S6.addChild(this.tentacleP3S6);
        this.tentacleP3S6.setDefaultRotAngle(-0.5236F, 0.0F, 0.0F);
        this.tentacleP3S6.setTextureOffset(0, 17).addBox(-1.0F, -0.5F, -4.0F, 2.0F, 1.0F, 4.0F, 0.0F, true);
        this.tentacleP3S6.setTextureOffset(0, 10).addBox(-0.5F, -1.0F, -2.2F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        this.tentacleP3S6.setTextureOffset(0, 10).addBox(-0.5F, -1.0F, -3.8F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        this.anim = new BlockBenchAnimations(this, new ResourceLocation(Fate.MODID, "models/entity/animation/starfish.json"));
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.mouthBottom.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setRotationAngles(T monster, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetModel();
        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
        AnimatedAction anim = monster.getAnimation();
        if (anim != null)
            this.anim.doAnimation(anim.getID(), anim.getTick(), partialTicks);
        else if (monster.getMotion().x != 0 || monster.getMotion().z != 0)
            this.anim.doAnimation("walk", monster.ticksExisted, partialTicks);
        else
            this.anim.doAnimation("iddle", monster.ticksExisted, partialTicks);
    }

    @Override
    public void resetModel() {
        this.mouthBottom.reset();
        this.resetChild(this.mouthBottom);
    }
}
