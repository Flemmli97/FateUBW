package com.flemmli97.fate.client.model;

import com.flemmli97.fate.common.entity.servant.EntityHeracles;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

/**
 * Starfish Demons - Black_Saturn
 * Created using Tabula 7.0.0
 */
public class ModelHeracles extends ModelServant<EntityHeracles> {

    public ModelRendererPlus upperTorso;
    public ModelRendererPlus lowerTorso;
    public ModelRendererPlus waist;
    public ModelRendererPlus rightUpperThigh;
    public ModelRendererPlus rightKnee;
    public ModelRendererPlus rightLowerThigh;
    public ModelRendererPlus rightAnkle;
    public ModelRendererPlus rightFoot;
    public ModelRendererPlus rightToe1;
    public ModelRendererPlus rightToe2;
    public ModelRendererPlus rightToe3;
    public ModelRendererPlus rightToe4;
    public ModelRendererPlus rightToe5;
    public ModelRendererPlus rFootShape1;
    public ModelRendererPlus rFootShape2;
    public ModelRendererPlus rightShin;
    public ModelRendererPlus leftUpperThigh;
    public ModelRendererPlus leftKnee;
    public ModelRendererPlus leftLowerThigh;
    public ModelRendererPlus leftAnkle;
    public ModelRendererPlus leftFoot;
    public ModelRendererPlus leftToe1;
    public ModelRendererPlus leftToe2;
    public ModelRendererPlus leftToe3;
    public ModelRendererPlus leftToe4;
    public ModelRendererPlus leftToe5;
    public ModelRendererPlus lFootShape1;
    public ModelRendererPlus lFootShape2;
    public ModelRendererPlus leftShin;
    public ModelRendererPlus outerSkirt;
    public ModelRendererPlus innerSkirt;
    public ModelRendererPlus neck;
    public ModelRendererPlus head;
    public ModelRendererPlus nose;
    public ModelRendererPlus hair1;
    public ModelRendererPlus hair2;
    public ModelRendererPlus hair3;
    public ModelRendererPlus hair4;
    public ModelRendererPlus hair5;
    public ModelRendererPlus hair6;
    public ModelRendererPlus hair7;
    public ModelRendererPlus leftArmUp;
    public ModelRendererPlus leftShoulder;
    public ModelRendererPlus leftBiceps;
    public ModelRendererPlus leftBicepsJoint;
    public ModelRendererPlus leftElbow;
    public ModelRendererPlus leftLowerArm;
    public ModelRendererPlus leftWrist;
    public ModelRendererPlus leftHand;
    public ModelRendererPlus leftFinger1;
    public ModelRendererPlus leftFinger2;
    public ModelRendererPlus leftFinger3;
    public ModelRendererPlus leftFinger4;
    public ModelRendererPlus leftThumbUp;
    public ModelRendererPlus leftThumbDown;
    public ModelRendererPlus leftElbowBone;
    public ModelRendererPlus rightArmUp;
    public ModelRendererPlus rightShoulder;
    public ModelRendererPlus rightBiceps;
    public ModelRendererPlus rightBicepsJoint;
    public ModelRendererPlus rightElbow;
    public ModelRendererPlus rightLowerArm;
    public ModelRendererPlus rightWrist;
    public ModelRendererPlus rightHand;
    public ModelRendererPlus rightFinger1;
    public ModelRendererPlus rightFinger2;
    public ModelRendererPlus rightFinger3;
    public ModelRendererPlus rightFinger4;
    public ModelRendererPlus rightThumbUp;
    public ModelRendererPlus rightThumbDown;
    public ModelRendererPlus rightElbowBonee;

    public ModelHeracles() {
        super("heracles");
        this.textureWidth = 76;
        this.textureHeight = 76;

        this.upperTorso = new ModelRendererPlus(this);
        this.upperTorso.setDefaultRotPoint(0.0F, -1.6F, 0.0F);
        this.upperTorso.setTextureOffset(23, 65).addCuboid(-6.0F, -2.5F, -3.0F, 12.0F, 5.0F, 6.0F, 0.0F, true);
        this.upperTorso.setTextureOffset(0, 3).addCuboid(0.6F, -2.1F, -3.4F, 4.0F, 2.0F, 1.0F, 0.0F, true);
        this.upperTorso.setTextureOffset(0, 3).addCuboid(-4.6F, -2.1F, -3.4F, 4.0F, 2.0F, 1.0F, 0.0F, true);
        this.upperTorso.setTextureOffset(0, 3).addCuboid(0.1F, 0.1F, -3.4F, 4.0F, 2.0F, 1.0F, 0.0F, true);
        this.upperTorso.setTextureOffset(0, 3).addCuboid(-4.1F, 0.1F, -3.4F, 4.0F, 2.0F, 1.0F, 0.0F, true);

        this.lowerTorso = new ModelRendererPlus(this);
        this.lowerTorso.setDefaultRotPoint(0.0F, 4.5F, 0.0F);
        this.upperTorso.addChild(this.lowerTorso);
        this.lowerTorso.setTextureOffset(0, 6).addCuboid(-5.0F, -2.0F, -2.5F, 10.0F, 4.0F, 5.0F, 0.0F, true);
        this.lowerTorso.setTextureOffset(0, 0).addCuboid(-2.1F, -1.7F, -2.8F, 2.0F, 1.0F, 1.0F, 0.0F, true);
        this.lowerTorso.setTextureOffset(0, 0).addCuboid(0.1F, -1.7F, -2.8F, 2.0F, 1.0F, 1.0F, 0.0F, true);
        this.lowerTorso.setTextureOffset(0, 0).addCuboid(0.1F, -0.6F, -2.8F, 2.0F, 1.0F, 1.0F, 0.0F, true);
        this.lowerTorso.setTextureOffset(0, 0).addCuboid(0.1F, 0.5F, -2.8F, 2.0F, 1.0F, 1.0F, 0.0F, true);
        this.lowerTorso.setTextureOffset(0, 0).addCuboid(-2.1F, -0.6F, -2.8F, 2.0F, 1.0F, 1.0F, 0.0F, true);
        this.lowerTorso.setTextureOffset(0, 0).addCuboid(-2.1F, 0.5F, -2.8F, 2.0F, 1.0F, 1.0F, 0.0F, true);

        this.waist = new ModelRendererPlus(this);
        this.waist.setDefaultRotPoint(0.0F, 4.0F, 0.0F);
        this.lowerTorso.addChild(this.waist);
        this.waist.setTextureOffset(0, 35).addCuboid(-4.5F, -2.0F, -2.0F, 9.0F, 4.0F, 4.0F, 0.0F, true);

        this.rightUpperThigh = new ModelRendererPlus(this);
        this.rightUpperThigh.setDefaultRotPoint(3.0F, 3.8F, 0.0F);
        this.waist.addChild(this.rightUpperThigh);
        this.setRotationAngle(this.rightUpperThigh, 0.0F, -0.0456F, -0.0175F);
        this.rightUpperThigh.setTextureOffset(0, 43).addCuboid(-1.5F, -2.5F, -1.5F, 3.0F, 4.0F, 3.0F, 0.0F, true);

        this.rightKnee = new ModelRendererPlus(this);
        this.rightKnee.setDefaultRotPoint(0.0F, 3.0F, -0.4F);
        this.rightUpperThigh.addChild(this.rightKnee);
        this.rightKnee.setTextureOffset(0, 50).addCuboid(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, 0.0F, true);

        this.rightLowerThigh = new ModelRendererPlus(this);
        this.rightLowerThigh.setDefaultRotPoint(0.0F, 3.9F, 0.7F);
        this.rightKnee.addChild(this.rightLowerThigh);
        this.rightLowerThigh.setTextureOffset(0, 56).addCuboid(-1.5F, -2.5F, -1.5F, 3.0F, 5.0F, 3.0F, 0.0F, true);

        this.rightAnkle = new ModelRendererPlus(this);
        this.rightAnkle.setDefaultRotPoint(0.0F, 4.0F, -0.4F);
        this.rightLowerThigh.addChild(this.rightAnkle);
        this.rightAnkle.setTextureOffset(0, 64).addCuboid(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, 0.0F, true);

        this.rightFoot = new ModelRendererPlus(this);
        this.rightFoot.setDefaultRotPoint(0.0F, 1.4F, -0.3F);
        this.rightAnkle.addChild(this.rightFoot);
        this.rightFoot.setTextureOffset(0, 70).addCuboid(-2.0F, 0.0F, -2.5F, 4.0F, 1.0F, 5.0F, 0.0F, true);

        this.rightToe1 = new ModelRendererPlus(this);
        this.rightToe1.setDefaultRotPoint(1.5F, 0.0F, -1.0F);
        this.rightFoot.addChild(this.rightToe1);
        this.setRotationAngle(this.rightToe1, 0.0F, -0.5009F, 0.0F);
        this.rightToe1.setTextureOffset(13, 66).addCuboid(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);

        this.rightToe2 = new ModelRendererPlus(this);
        this.rightToe2.setDefaultRotPoint(-1.5F, 0.0F, -1.3F);
        this.rightFoot.addChild(this.rightToe2);
        this.setRotationAngle(this.rightToe2, 0.0F, 0.5009F, 0.0F);
        this.rightToe2.setTextureOffset(13, 66).addCuboid(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);

        this.rightToe3 = new ModelRendererPlus(this);
        this.rightToe3.setDefaultRotPoint(-1.1F, 0.0F, -1.5F);
        this.rightFoot.addChild(this.rightToe3);
        this.rightToe3.setTextureOffset(13, 66).addCuboid(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);

        this.rightToe4 = new ModelRendererPlus(this);
        this.rightToe4.setDefaultRotPoint(0.0F, 0.0F, -1.5F);
        this.rightFoot.addChild(this.rightToe4);
        this.rightToe4.setTextureOffset(13, 66).addCuboid(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);

        this.rightToe5 = new ModelRendererPlus(this);
        this.rightToe5.setDefaultRotPoint(1.1F, 0.0F, -1.5F);
        this.rightFoot.addChild(this.rightToe5);
        this.rightToe5.setTextureOffset(13, 66).addCuboid(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);

        this.rFootShape1 = new ModelRendererPlus(this);
        this.rFootShape1.setDefaultRotPoint(0.0F, 0.0F, -1.9F);
        this.rightFoot.addChild(this.rFootShape1);
        this.setRotationAngle(this.rFootShape1, 0.7854F, 0.0F, 0.0F);
        this.rFootShape1.setTextureOffset(0, 0).addCuboid(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 1.0F, 0.0F, true);

        this.rFootShape2 = new ModelRendererPlus(this);
        this.rFootShape2.setDefaultRotPoint(0.0F, 0.0F, 1.1F);
        this.rightFoot.addChild(this.rFootShape2);
        this.setRotationAngle(this.rFootShape2, 0.7854F, 0.0F, 0.0F);
        this.rFootShape2.setTextureOffset(0, 0).addCuboid(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 1.0F, 0.0F, true);

        this.rightShin = new ModelRendererPlus(this);
        this.rightShin.setDefaultRotPoint(0.0F, 0.0F, -1.5F);
        this.rightLowerThigh.addChild(this.rightShin);
        this.setRotationAngle(this.rightShin, 0.0456F, 0.0F, 0.0F);
        this.rightShin.setTextureOffset(18, 70).addCuboid(-0.5F, -2.5F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, true);

        this.leftUpperThigh = new ModelRendererPlus(this);
        this.leftUpperThigh.setDefaultRotPoint(-3.0F, 3.8F, 0.0F);
        this.waist.addChild(this.leftUpperThigh);
        this.leftUpperThigh.setTextureOffset(0, 43).addCuboid(-1.5F, -2.5F, -1.5F, 3.0F, 4.0F, 3.0F, 0.0F, true);

        this.leftKnee = new ModelRendererPlus(this);
        this.leftKnee.setDefaultRotPoint(0.0F, 3.0F, -0.4F);
        this.leftUpperThigh.addChild(this.leftKnee);
        this.leftKnee.setTextureOffset(0, 50).addCuboid(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, 0.0F, true);

        this.leftLowerThigh = new ModelRendererPlus(this);
        this.leftLowerThigh.setDefaultRotPoint(0.0F, 3.9F, 0.7F);
        this.leftKnee.addChild(this.leftLowerThigh);
        this.leftLowerThigh.setTextureOffset(0, 56).addCuboid(-1.5F, -2.5F, -1.5F, 3.0F, 5.0F, 3.0F, 0.0F, true);

        this.leftAnkle = new ModelRendererPlus(this);
        this.leftAnkle.setDefaultRotPoint(0.0F, 4.0F, -0.4F);
        this.leftLowerThigh.addChild(this.leftAnkle);
        this.leftAnkle.setTextureOffset(0, 64).addCuboid(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, 0.0F, true);

        this.leftFoot = new ModelRendererPlus(this);
        this.leftFoot.setDefaultRotPoint(0.0F, 1.4F, -0.3F);
        this.leftAnkle.addChild(this.leftFoot);
        this.leftFoot.setTextureOffset(0, 70).addCuboid(-2.0F, 0.0F, -2.5F, 4.0F, 1.0F, 5.0F, 0.0F, true);

        this.leftToe1 = new ModelRendererPlus(this);
        this.leftToe1.setDefaultRotPoint(-1.1F, 0.0F, -1.5F);
        this.leftFoot.addChild(this.leftToe1);
        this.leftToe1.setTextureOffset(13, 66).addCuboid(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);

        this.leftToe2 = new ModelRendererPlus(this);
        this.leftToe2.setDefaultRotPoint(-1.5F, 0.0F, -1.0F);
        this.leftFoot.addChild(this.leftToe2);
        this.setRotationAngle(this.leftToe2, 0.0F, 0.5009F, 0.0F);
        this.leftToe2.setTextureOffset(13, 66).addCuboid(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);

        this.leftToe3 = new ModelRendererPlus(this);
        this.leftToe3.setDefaultRotPoint(0.0F, 0.0F, -1.5F);
        this.leftFoot.addChild(this.leftToe3);
        this.leftToe3.setTextureOffset(13, 66).addCuboid(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);

        this.leftToe4 = new ModelRendererPlus(this);
        this.leftToe4.setDefaultRotPoint(1.1F, 0.0F, -1.5F);
        this.leftFoot.addChild(this.leftToe4);
        this.leftToe4.setTextureOffset(13, 66).addCuboid(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);

        this.leftToe5 = new ModelRendererPlus(this);
        this.leftToe5.setDefaultRotPoint(1.5F, 0.0F, -1.3F);
        this.leftFoot.addChild(this.leftToe5);
        this.setRotationAngle(this.leftToe5, 0.0F, -0.5009F, 0.0F);
        this.leftToe5.setTextureOffset(13, 66).addCuboid(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);

        this.lFootShape1 = new ModelRendererPlus(this);
        this.lFootShape1.setDefaultRotPoint(0.0F, 0.0F, -1.9F);
        this.leftFoot.addChild(this.lFootShape1);
        this.setRotationAngle(this.lFootShape1, 0.7854F, 0.0F, 0.0F);
        this.lFootShape1.setTextureOffset(0, 0).addCuboid(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 1.0F, 0.0F, true);

        this.lFootShape2 = new ModelRendererPlus(this);
        this.lFootShape2.setDefaultRotPoint(0.0F, 0.0F, 1.1F);
        this.leftFoot.addChild(this.lFootShape2);
        this.setRotationAngle(this.lFootShape2, 0.7854F, 0.0F, 0.0F);
        this.lFootShape2.setTextureOffset(0, 0).addCuboid(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 1.0F, 0.0F, true);

        this.leftShin = new ModelRendererPlus(this);
        this.leftShin.setDefaultRotPoint(0.0F, 0.0F, -1.5F);
        this.leftLowerThigh.addChild(this.leftShin);
        this.setRotationAngle(this.leftShin, 0.0456F, 0.0F, 0.0F);
        this.leftShin.setTextureOffset(18, 70).addCuboid(-0.5F, -2.5F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, true);

        this.outerSkirt = new ModelRendererPlus(this);
        this.outerSkirt.setDefaultRotPoint(0.0F, 4.5F, 0.0F);
        this.waist.addChild(this.outerSkirt);
        this.outerSkirt.setTextureOffset(0, 21).addCuboid(-5.5F, -3.5F, -3.0F, 11.0F, 7.0F, 6.0F, 0.0F, true);

        this.innerSkirt = new ModelRendererPlus(this);
        this.innerSkirt.setDefaultRotPoint(0.0F, 0.0F, -2.6F);
        this.outerSkirt.addChild(this.innerSkirt);
        this.innerSkirt.setTextureOffset(0, 15).addCuboid(-5.0F, -3.0F, 0.0F, 10.0F, 6.0F, 0.0F, 0.0F, true);

        this.neck = new ModelRendererPlus(this);
        this.neck.setDefaultRotPoint(0.0F, -3.0F, 0.0F);
        this.upperTorso.addChild(this.neck);
        this.neck.setTextureOffset(26, 0).addCuboid(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, 0.0F, true);

        this.head = new ModelRendererPlus(this);
        this.head.setDefaultRotPoint(0.0F, -4.5F, 0.0F);
        this.neck.addChild(this.head);
        this.head.setTextureOffset(30, 0).addCuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, true);

        this.nose = new ModelRendererPlus(this);
        this.nose.setDefaultRotPoint(0.0F, -0.2F, -3.5F);
        this.head.addChild(this.nose);
        this.setRotationAngle(this.nose, -0.4294F, 0.0F, 0.0F);
        this.nose.setTextureOffset(16, 60).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        this.hair1 = new ModelRendererPlus(this);
        this.hair1.setDefaultRotPoint(0.0F, -1.5F, 2.0F);
        this.head.addChild(this.hair1);
        this.hair1.setTextureOffset(34, 16).addCuboid(0.0F, -5.5F, -5.5F, 0.0F, 11.0F, 11.0F, 0.0F, true);

        this.hair2 = new ModelRendererPlus(this);
        this.hair2.setDefaultRotPoint(-1.2F, -1.5F, 2.0F);
        this.head.addChild(this.hair2);
        this.hair2.setTextureOffset(34, 5).addCuboid(0.0F, -5.5F, -5.5F, 0.0F, 11.0F, 11.0F, 0.0F, true);

        this.hair3 = new ModelRendererPlus(this);
        this.hair3.setDefaultRotPoint(1.2F, -1.5F, 2.0F);
        this.head.addChild(this.hair3);
        this.hair3.setTextureOffset(34, 5).addCuboid(0.0F, -5.5F, -5.5F, 0.0F, 11.0F, 11.0F, 0.0F, true);

        this.hair4 = new ModelRendererPlus(this);
        this.hair4.setDefaultRotPoint(-2.4F, -1.5F, 2.0F);
        this.head.addChild(this.hair4);
        this.hair4.setTextureOffset(34, 16).addCuboid(0.0F, -5.5F, -5.5F, 0.0F, 11.0F, 11.0F, 0.0F, true);

        this.hair5 = new ModelRendererPlus(this);
        this.hair5.setDefaultRotPoint(2.4F, -1.5F, 2.0F);
        this.head.addChild(this.hair5);
        this.hair5.setTextureOffset(34, 16).addCuboid(0.0F, -5.5F, -5.5F, 0.0F, 11.0F, 11.0F, 0.0F, true);

        this.hair6 = new ModelRendererPlus(this);
        this.hair6.setDefaultRotPoint(4.0F, -1.5F, 2.0F);
        this.head.addChild(this.hair6);
        this.setRotationAngle(this.hair6, 0.0F, 0.2276F, 0.0F);
        this.hair6.setTextureOffset(34, 5).addCuboid(0.0F, -5.5F, -5.5F, 0.0F, 11.0F, 11.0F, 0.0F, true);

        this.hair7 = new ModelRendererPlus(this);
        this.hair7.setDefaultRotPoint(-4.0F, -1.5F, 2.0F);
        this.head.addChild(this.hair7);
        this.setRotationAngle(this.hair7, 0.0F, -0.2276F, 0.0F);
        this.hair7.setTextureOffset(34, 5).addCuboid(0.0F, -5.5F, -5.5F, 0.0F, 11.0F, 11.0F, 0.0F, true);

        this.leftArmUp = new ModelRendererPlus(this);
        this.leftArmUp.setDefaultRotPoint(6.0F, 0.0F, 0.0F);
        this.upperTorso.addChild(this.leftArmUp);
        this.leftArmUp.setTextureOffset(12, 43).addCuboid(0.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        this.leftShoulder = new ModelRendererPlus(this);
        this.leftShoulder.setDefaultRotPoint(2.4F, -2.0F, 0.0F);
        this.leftArmUp.addChild(this.leftShoulder);
        this.setRotationAngle(this.leftShoulder, 0.0F, 0.0F, 0.1571F);
        this.leftShoulder.setTextureOffset(10, 51).addCuboid(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, 0.0F, true);

        this.leftBiceps = new ModelRendererPlus(this);
        this.leftBiceps.setDefaultRotPoint(4.5F, 1.6F, 0.0F);
        this.leftArmUp.addChild(this.leftBiceps);
        this.setRotationAngle(this.leftBiceps, 0.0F, 0.0F, 0.8727F);
        this.leftBiceps.setTextureOffset(26, 56).addCuboid(-2.5F, -2.0F, -2.5F, 5.0F, 4.0F, 5.0F, 0.0F, false);

        this.leftBicepsJoint = new ModelRendererPlus(this);
        this.leftBicepsJoint.setDefaultRotPoint(2.0F, 2.0F, 0.0F);
        this.leftBiceps.addChild(this.leftBicepsJoint);
        this.setRotationAngle(this.leftBicepsJoint, 0.0F, 0.0F, -0.8727F);
        this.leftBicepsJoint.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, true);

        this.leftElbow = new ModelRendererPlus(this);
        this.leftElbow.setDefaultRotPoint(0.6F, 0.9F, 0.0F);
        this.leftBicepsJoint.addChild(this.leftElbow);
        this.leftElbow.setTextureOffset(28, 48).addCuboid(-1.5F, -2.0F, -1.5F, 3.0F, 4.0F, 3.0F, 0.0F, false);

        this.leftLowerArm = new ModelRendererPlus(this);
        this.leftLowerArm.setDefaultRotPoint(0.2F, 4.0F, 0.0F);
        this.leftElbow.addChild(this.leftLowerArm);
        this.leftLowerArm.setTextureOffset(26, 38).addCuboid(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        this.leftWrist = new ModelRendererPlus(this);
        this.leftWrist.setDefaultRotPoint(0.0F, 3.5F, 0.0F);
        this.leftLowerArm.addChild(this.leftWrist);
        this.leftWrist.setTextureOffset(59, 69).addCuboid(-1.5F, -1.5F, -2.0F, 3.0F, 3.0F, 4.0F, 0.0F, false);

        this.leftHand = new ModelRendererPlus(this);
        this.leftHand.setDefaultRotPoint(1.0F, 2.5F, 0.0F);
        this.leftWrist.addChild(this.leftHand);
        this.leftHand.setTextureOffset(16, 0).addCuboid(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);

        this.leftFinger1 = new ModelRendererPlus(this);
        this.leftFinger1.setDefaultRotPoint(0.0F, 1.0F, -1.8F);
        this.leftHand.addChild(this.leftFinger1);
        this.leftFinger1.setTextureOffset(0, 0).addCuboid(-2.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        this.leftFinger2 = new ModelRendererPlus(this);
        this.leftFinger2.setDefaultRotPoint(0.0F, 1.0F, -0.7F);
        this.leftHand.addChild(this.leftFinger2);
        this.leftFinger2.setTextureOffset(0, 0).addCuboid(-2.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        this.leftFinger3 = new ModelRendererPlus(this);
        this.leftFinger3.setDefaultRotPoint(0.0F, 1.0F, 0.4F);
        this.leftHand.addChild(this.leftFinger3);
        this.leftFinger3.setTextureOffset(0, 0).addCuboid(-2.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        this.leftFinger4 = new ModelRendererPlus(this);
        this.leftFinger4.setDefaultRotPoint(0.0F, 1.0F, 1.5F);
        this.leftHand.addChild(this.leftFinger4);
        this.leftFinger4.setTextureOffset(0, 0).addCuboid(-2.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        this.leftThumbUp = new ModelRendererPlus(this);
        this.leftThumbUp.setDefaultRotPoint(-1.0F, 0.5F, -1.7F);
        this.leftWrist.addChild(this.leftThumbUp);
        this.setRotationAngle(this.leftThumbUp, -0.791F, 0.0F, 0.0F);
        this.leftThumbUp.setTextureOffset(16, 60).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        this.leftThumbDown = new ModelRendererPlus(this);
        this.leftThumbDown.setDefaultRotPoint(0.0F, 1.6F, -0.2F);
        this.leftThumbUp.addChild(this.leftThumbDown);
        this.setRotationAngle(this.leftThumbDown, 0.791F, 0.0F, 0.0F);
        this.leftThumbDown.setTextureOffset(16, 60).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        this.leftElbowBone = new ModelRendererPlus(this);
        this.leftElbowBone.setDefaultRotPoint(2.0F, -4.0F, 0.0F);
        this.leftLowerArm.addChild(this.leftElbowBone);
        this.setRotationAngle(this.leftElbowBone, 0.0F, 0.0F, 0.2485F);
        this.leftElbowBone.setTextureOffset(10, 0).addCuboid(-0.5F, -2.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, true);

        this.rightArmUp = new ModelRendererPlus(this);
        this.rightArmUp.setDefaultRotPoint(-6.0F, 0.0F, 0.0F);
        this.upperTorso.addChild(this.rightArmUp);
        this.rightArmUp.setTextureOffset(12, 43).addCuboid(-4.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        this.rightShoulder = new ModelRendererPlus(this);
        this.rightShoulder.setDefaultRotPoint(-2.4F, -2.0F, 0.0F);
        this.rightArmUp.addChild(this.rightShoulder);
        this.setRotationAngle(this.rightShoulder, 0.0F, 0.0F, -0.1571F);
        this.rightShoulder.setTextureOffset(10, 51).addCuboid(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, 0.0F, false);

        this.rightBiceps = new ModelRendererPlus(this);
        this.rightBiceps.setDefaultRotPoint(-4.5F, 1.6F, 0.0F);
        this.rightArmUp.addChild(this.rightBiceps);
        this.setRotationAngle(this.rightBiceps, 0.0F, 0.0F, -0.8727F);
        this.rightBiceps.setTextureOffset(26, 56).addCuboid(-2.5F, -2.0F, -2.5F, 5.0F, 4.0F, 5.0F, 0.0F, true);

        this.rightBicepsJoint = new ModelRendererPlus(this);
        this.rightBicepsJoint.setDefaultRotPoint(-2.0F, 2.0F, 0.0F);
        this.rightBiceps.addChild(this.rightBicepsJoint);
        this.setRotationAngle(this.rightBicepsJoint, 0.0F, 0.0F, 0.8727F);
        this.rightBicepsJoint.setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, true);

        this.rightElbow = new ModelRendererPlus(this);
        this.rightElbow.setDefaultRotPoint(-0.6F, 0.9F, 0.0F);
        this.rightBicepsJoint.addChild(this.rightElbow);
        this.rightElbow.setTextureOffset(28, 48).addCuboid(-1.5F, -2.0F, -1.5F, 3.0F, 4.0F, 3.0F, 0.0F, true);

        this.rightLowerArm = new ModelRendererPlus(this);
        this.rightLowerArm.setDefaultRotPoint(-0.2F, 4.0F, 0.0F);
        this.rightElbow.addChild(this.rightLowerArm);
        this.rightLowerArm.setTextureOffset(26, 38).addCuboid(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, true);

        this.rightWrist = new ModelRendererPlus(this);
        this.rightWrist.setDefaultRotPoint(0.0F, 3.5F, 0.0F);
        this.rightLowerArm.addChild(this.rightWrist);
        this.rightWrist.setTextureOffset(59, 69).addCuboid(-1.5F, -1.5F, -2.0F, 3.0F, 3.0F, 4.0F, 0.0F, true);

        this.rightHand = new ModelRendererPlus(this);
        this.rightHand.setDefaultRotPoint(-1.0F, 2.5F, 0.0F);
        this.rightWrist.addChild(this.rightHand);
        this.rightHand.setTextureOffset(16, 0).addCuboid(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, 0.0F, true);

        this.rightFinger1 = new ModelRendererPlus(this);
        this.rightFinger1.setDefaultRotPoint(0.0F, 1.0F, -1.8F);
        this.rightHand.addChild(this.rightFinger1);
        this.rightFinger1.setTextureOffset(0, 0).addCuboid(0.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, true);

        this.rightFinger2 = new ModelRendererPlus(this);
        this.rightFinger2.setDefaultRotPoint(0.0F, 1.0F, -0.7F);
        this.rightHand.addChild(this.rightFinger2);
        this.rightFinger2.setTextureOffset(0, 0).addCuboid(0.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, true);

        this.rightFinger3 = new ModelRendererPlus(this);
        this.rightFinger3.setDefaultRotPoint(0.0F, 1.0F, 0.4F);
        this.rightHand.addChild(this.rightFinger3);
        this.rightFinger3.setTextureOffset(0, 0).addCuboid(0.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, true);

        this.rightFinger4 = new ModelRendererPlus(this);
        this.rightFinger4.setDefaultRotPoint(0.0F, 1.0F, 1.5F);
        this.rightHand.addChild(this.rightFinger4);
        this.rightFinger4.setTextureOffset(0, 0).addCuboid(0.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, true);

        this.rightThumbUp = new ModelRendererPlus(this);
        this.rightThumbUp.setDefaultRotPoint(1.0F, 0.5F, -1.7F);
        this.rightWrist.addChild(this.rightThumbUp);
        this.setRotationAngle(this.rightThumbUp, -0.791F, 0.0F, 0.0F);
        this.rightThumbUp.setTextureOffset(16, 60).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        this.rightThumbDown = new ModelRendererPlus(this);
        this.rightThumbDown.setDefaultRotPoint(0.0F, 1.6F, -0.2F);
        this.rightThumbUp.addChild(this.rightThumbDown);
        this.setRotationAngle(this.rightThumbDown, 0.791F, 0.0F, 0.0F);
        this.rightThumbDown.setTextureOffset(16, 60).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        this.rightElbowBonee = new ModelRendererPlus(this);
        this.rightElbowBonee.setDefaultRotPoint(-2.0F, -4.0F, 0.0F);
        this.rightLowerArm.addChild(this.rightElbowBonee);
        this.setRotationAngle(this.rightElbowBonee, 0.0F, 0.0F, -0.2485F);
        this.rightElbowBonee.setTextureOffset(10, 0).addCuboid(-0.5F, -2.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, true);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.upperTorso.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setAnglesPre(EntityHeracles servant, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!this.show) {
            this.servantBody.showModel = true;
            this.servantBodyOverlay.showModel = true;
            this.upperTorso.showModel = false;
            super.setAnglesPre(servant, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        } else {
            this.servantBody.showModel = false;
            this.servantBodyOverlay.showModel = false;
            this.upperTorso.showModel = true;

            this.resetModel();
            this.head.rotateAngleY = netHeadYaw / (180F / (float) Math.PI);
            this.head.rotateAngleX = headPitch / (180F / (float) Math.PI);

            this.rightArmUp.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
            this.leftArmUp.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
            this.rightArmUp.rotateAngleZ = 0;
            this.leftArmUp.rotateAngleZ = 0;
            this.rightUpperThigh.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.leftUpperThigh.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            this.rightUpperThigh.rotateAngleY = 0;
            this.leftUpperThigh.rotateAngleY = 0;

            if (this.isSitting) {
                this.rightArmUp.rotateAngleX += -((float) Math.PI / 5F);
                this.leftArmUp.rotateAngleX += -((float) Math.PI / 5F);
                this.rightUpperThigh.rotateAngleX = -((float) Math.PI * 2F / 5F);
                this.leftUpperThigh.rotateAngleX = -((float) Math.PI * 2F / 5F);
                this.rightUpperThigh.rotateAngleY = ((float) Math.PI / 10F);
                this.leftUpperThigh.rotateAngleY = -((float) Math.PI / 10F);
            }

            if (this.heldItemOff == 1)
                this.leftArmUp.rotateAngleX = this.leftArmUp.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
            if (this.heldItemMain == 1)
                this.rightArmUp.rotateAngleX = this.rightArmUp.rotateAngleX * 0.5F - ((float) Math.PI / 10F);

            this.rightArmUp.rotateAngleY = 0;
            this.leftArmUp.rotateAngleY = 0;
            if (this.swingProgress > -9990) {
                float swingProgress = this.swingProgress;
                this.upperTorso.rotateAngleY = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI * 2.0F) * 0.2F;
                this.rightArmUp.rotateAngleY += this.upperTorso.rotateAngleY;
                this.leftArmUp.rotateAngleY += this.upperTorso.rotateAngleY;
                this.leftArmUp.rotateAngleX += this.upperTorso.rotateAngleY;
                swingProgress = 1.0F - this.swingProgress;
                swingProgress *= swingProgress;
                swingProgress *= swingProgress;
                swingProgress = 1.0F - swingProgress;
                float var9 = MathHelper.sin(swingProgress * (float) Math.PI);
                float var10 = MathHelper.sin(this.swingProgress * (float) Math.PI) * -(this.head.rotateAngleX - 0.7F) * 0.75F;
                this.rightArmUp.rotateAngleX = (float) ((double) this.rightArmUp.rotateAngleX - ((double) var9 * 1.2D + (double) var10));
                this.rightArmUp.rotateAngleY += this.upperTorso.rotateAngleY * 2.0F;
                this.rightArmUp.rotateAngleZ = MathHelper.sin(this.swingProgress * (float) Math.PI) * -0.4F;
            }

            this.upperTorso.rotateAngleX = 0;

            this.rightArmUp.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            this.leftArmUp.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            this.rightArmUp.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
            this.leftArmUp.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        }
    }

    @Override
    public void resetModel() {
        if (this.show) {
            this.upperTorso.reset();
            this.resetChild(this.upperTorso);
        } else {
            super.resetModel();
        }
    }

    @Override
    public void transform(HandSide side, MatrixStack stack) {
        if (this.show) {
            if (side == HandSide.LEFT) {
                this.rotate(stack, this.upperTorso, this.leftArmUp, this.leftBiceps, this.leftBicepsJoint, this.leftElbow, this.leftLowerArm, this.leftWrist);
            } else {
                this.rotate(stack, this.upperTorso, this.rightArmUp, this.rightBiceps, this.rightBicepsJoint, this.rightElbow, this.rightLowerArm, this.rightWrist);
            }
        } else
            super.transform(side, stack);
    }

    @Override
    public void postTransform(boolean leftSide, MatrixStack stack) {
        if (this.show) {
            stack.translate(0, 0, -2 / 16d);
        } else
            super.postTransform(leftSide, stack);
    }

    @Override
    public ModelRenderer func_205072_a() {
        return this.head;
    }

    public void setRotationAngle(ModelRendererPlus modelRenderer, float x, float y, float z) {
        modelRenderer.setDefaultRotAngle(x, y, z);
    }
}
