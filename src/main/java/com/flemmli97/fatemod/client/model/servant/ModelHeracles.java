package com.flemmli97.fatemod.client.model.servant;

import com.flemmli97.fatemod.common.entity.servant.EntityHeracles;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.client.model.TabulaAnimation;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.flemmli97.tenshilib.client.model.ModelUtils;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

/**
 * Heracles_Berserker - Black_Saturn
 * Created using Tabula 4.1.1
 */
public class ModelHeracles extends ModelServant {
		
    public ModelRendererPlus upperTorso;
    public ModelRendererPlus lowerTorso;
    public ModelRendererPlus chestMuscle1;
    public ModelRendererPlus chestMuscle2;
    public ModelRendererPlus chestMuscle3;
    public ModelRendererPlus chestMuscle4;
    public ModelRendererPlus rightArmUp;
    public ModelRendererPlus leftArmUp;
    public ModelRendererPlus neck;
    public ModelRendererPlus Waist;
    public ModelRendererPlus absMuscles1;
    public ModelRendererPlus absMuscles2;
    public ModelRendererPlus absMuscles3;
    public ModelRendererPlus absMuscles4;
    public ModelRendererPlus absMuscles5;
    public ModelRendererPlus absMuscles6;
    public ModelRendererPlus rightUpperThigh;
    public ModelRendererPlus leftUpperThigh;
    public ModelRendererPlus outerSkirt;
    public ModelRendererPlus rightAnkle;
    public ModelRendererPlus rightLowerThigh;
    public ModelRendererPlus rFootUp;
    public ModelRendererPlus rightShin;
    public ModelRendererPlus rightfoot;
    public ModelRendererPlus rFootShape1;
    public ModelRendererPlus rFootShape2;
    public ModelRendererPlus rightToe1;
    public ModelRendererPlus rightToe2;
    public ModelRendererPlus rightToe3;
    public ModelRendererPlus rightToe4;
    public ModelRendererPlus rightToe5;
    public ModelRendererPlus leftAnkle;
    public ModelRendererPlus leftLowerThigh;
    public ModelRendererPlus leftFootUp;
    public ModelRendererPlus leftShin;
    public ModelRendererPlus leftFoot;
    public ModelRendererPlus leftToe1;
    public ModelRendererPlus leftToe2;
    public ModelRendererPlus leftToe3;
    public ModelRendererPlus leftToe4;
    public ModelRendererPlus leftToe5;
    public ModelRendererPlus lFootShape1;
    public ModelRendererPlus lFootShape2;
    public ModelRendererPlus innerSkirt;
    public ModelRendererPlus rightShoulder;
    public ModelRendererPlus rightBiceps;
    public ModelRendererPlus rightBicepsJoint;
    public ModelRendererPlus rightElbow;
    public ModelRendererPlus rightLowerArm;
    public ModelRendererPlus rightWrist;
    public ModelRendererPlus rightElbowBone;
    public ModelRendererPlus rightHand;
    public ModelRendererPlus rightThumbUp;
    public ModelRendererPlus rightFinger1;
    public ModelRendererPlus rightFinger2;
    public ModelRendererPlus rightFinger3;
    public ModelRendererPlus rightFinger4;
    public ModelRendererPlus rightThumpDown;
    public ModelRendererPlus leftShoulder;
    public ModelRendererPlus leftBiceps;
    public ModelRendererPlus leftBicepsJoint;
    public ModelRendererPlus leftElbow;
    public ModelRendererPlus leftLowerArm;
    public ModelRendererPlus leftWrist;
    public ModelRendererPlus leftElbowBone;
    public ModelRendererPlus leftHand;
    public ModelRendererPlus leftThumpUp;
    public ModelRendererPlus leftFinger1;
    public ModelRendererPlus leftFinger2;
    public ModelRendererPlus leftFinger3;
    public ModelRendererPlus leftFinger4;
    public ModelRendererPlus leftThumpDown;
    public ModelRendererPlus head;
    public ModelRendererPlus nose;
    public ModelRendererPlus hair1;
    
    public ModelRendererPlus hair2;
    public ModelRendererPlus hair3;
    public ModelRendererPlus hair4;
    public ModelRendererPlus hair5;
    public ModelRendererPlus hair6;
    public ModelRendererPlus hair7;
    
    public TabulaAnimation swing_1;

    public ModelHeracles() {
        this.textureWidth = 76;
        this.textureHeight = 76;
        this.rightUpperThigh = new ModelRendererPlus(this, 0, 43);
        this.rightUpperThigh.setDefaultRotPoint(-3.0F, 3.8F, 0.0F);
        this.rightUpperThigh.addBox(-1.5F, -2.5F, -1.5F, 3, 4, 3, 0.0F);
        this.lFootShape1 = new ModelRendererPlus(this, 0, 0);
        this.lFootShape1.setDefaultRotPoint(0.0F, 0.0F, -1.9F);
        this.lFootShape1.addBox(-1.5F, 0.0F, 0.0F, 3, 1, 1, 0.0F);
        this.setRotateAngle(this.lFootShape1, 45F, 0.0F, 0.0F);
        this.rightWrist = new ModelRendererPlus(this, 59, 69);
        this.rightWrist.mirror=true;
        this.rightWrist.setDefaultRotPoint(0.0F, 3.5F, 0.0F);
        this.rightWrist.addBox(-1.5F, -1.5F, -2.0F, 3, 3, 4, 0.0F);
        this.leftFinger2 = new ModelRendererPlus(this, 0, 0);
        this.leftFinger2.setDefaultRotPoint(0.0F, 1.0F, -0.7F);
        this.leftFinger2.addBox(-2.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.nose = new ModelRendererPlus(this, 16, 60);
        this.nose.setDefaultRotPoint(0.0F, -0.2F, -3.5F);
        this.nose.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(this.nose, -24.6F, 0.0F, 0.0F);
        this.leftThumpUp = new ModelRendererPlus(this, 16, 60);
        this.leftThumpUp.setDefaultRotPoint(-1.0F, 0.5F, -1.7F);
        this.leftThumpUp.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(this.leftThumpUp, -45.32F, 0.0F, 0.0F);
        this.absMuscles6 = new ModelRendererPlus(this, 0, 0);
        this.absMuscles6.setDefaultRotPoint(1.1F, 1.0F, -2.3F);
        this.absMuscles6.addBox(-1.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.Waist = new ModelRendererPlus(this, 0, 35);
        this.Waist.setDefaultRotPoint(0.0F, 4.0F, 0.0F);
        this.Waist.addBox(-4.5F, -2.0F, -2.0F, 9, 4, 4, 0.0F);
        this.lFootShape2 = new ModelRendererPlus(this, 0, 0);
        this.lFootShape2.setDefaultRotPoint(0.0F, 0.0F, 1.1F);
        this.lFootShape2.addBox(-1.5F, 0.0F, 0.0F, 3, 1, 1, 0.0F);
        this.setRotateAngle(this.lFootShape2, 45F, 0.0F, 0.0F);
        this.rightToe4 = new ModelRendererPlus(this, 13, 66);
        this.rightToe4.setDefaultRotPoint(0.0F, 0.0F, -1.5F);
        this.rightToe4.addBox(-0.5F, 0.0F, -2.0F, 1, 1, 2, 0.0F);
        this.innerSkirt = new ModelRendererPlus(this, 0, 15);
        this.innerSkirt.setDefaultRotPoint(0.0F, 0.0F, -2.6F);
        this.innerSkirt.addBox(-5.0F, -3.0F, 0.0F, 10, 6, 0, 0.0F);
        this.leftFootUp = new ModelRendererPlus(this, 0, 64);
        this.leftFootUp.setDefaultRotPoint(0.0F, 4.0F, -0.4F);
        this.leftFootUp.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3, 0.0F);
        this.chestMuscle2 = new ModelRendererPlus(this, 0, 3);
        this.chestMuscle2.setDefaultRotPoint(-2.1F, 1.1F, -2.9F);
        this.chestMuscle2.addBox(-2.0F, -1.0F, -0.5F, 4, 2, 1, 0.0F);
        this.chestMuscle3 = new ModelRendererPlus(this, 0, 3);
        this.chestMuscle3.setDefaultRotPoint(2.6F, -1.1F, -2.9F);
        this.chestMuscle3.addBox(-2.0F, -1.0F, -0.5F, 4, 2, 1, 0.0F);
        this.absMuscles1 = new ModelRendererPlus(this, 0, 0);
        this.absMuscles1.setDefaultRotPoint(1.1F, -1.2F, -2.3F);
        this.absMuscles1.addBox(-1.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.chestMuscle4 = new ModelRendererPlus(this, 0, 3);
        this.chestMuscle4.setDefaultRotPoint(-2.6F, -1.1F, -2.9F);
        this.chestMuscle4.addBox(-2.0F, -1.0F, -0.5F, 4, 2, 1, 0.0F);
        this.leftToe2 = new ModelRendererPlus(this, 13, 66);
        this.leftToe2.setDefaultRotPoint(1.5F, 0.0F, -1.0F);
        this.leftToe2.addBox(-0.5F, 0.0F, -2.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(this.leftToe2, 0.0F, -28.7F, 0.0F);
        this.rightToe5 = new ModelRendererPlus(this, 13, 66);
        this.rightToe5.setDefaultRotPoint(-1.1F, 0.0F, -1.5F);
        this.rightToe5.addBox(-0.5F, 0.0F, -2.0F, 1, 1, 2, 0.0F);
        this.absMuscles5 = new ModelRendererPlus(this, 0, 0);
        this.absMuscles5.setDefaultRotPoint(1.1F, -0.1F, -2.3F);
        this.absMuscles5.addBox(-1.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.rFootShape2 = new ModelRendererPlus(this, 0, 0);
        this.rFootShape2.setDefaultRotPoint(0.0F, 0.0F, 1.1F);
        this.rFootShape2.addBox(-1.5F, 0.0F, 0.0F, 3, 1, 1, 0.0F);
        this.setRotateAngle(this.rFootShape2, 45F, 0.0F, 0.0F);
        this.leftArmUp = new ModelRendererPlus(this, 12, 43);
        this.leftArmUp.setDefaultRotPoint(6.0F, 0.0F, 0.0F);
        this.leftArmUp.addBox(0.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
        this.leftElbow = new ModelRendererPlus(this, 28, 48);
        this.leftElbow.setDefaultRotPoint(0.6F, 0.9F, 0.0F);
        this.leftElbow.addBox(-1.5F, -2.0F, -1.5F, 3, 4, 3, 0.0F);
        this.leftAnkle = new ModelRendererPlus(this, 0, 50);
        this.leftAnkle.setDefaultRotPoint(0.0F, 3.0F, -0.4F);
        this.leftAnkle.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3, 0.0F);
        this.hair4 = new ModelRendererPlus(this, 34, 16);
        this.hair4.setDefaultRotPoint(2.4F, -1.5F, 2.0F);
        this.hair4.addBox(0.0F, -5.5F, -5.5F, 0, 11, 11, 0.0F);
        this.hair1 = new ModelRendererPlus(this, 34, 16);
        this.hair1.setDefaultRotPoint(0.0F, -1.5F, 2.0F);
        this.hair1.addBox(0.0F, -5.5F, -5.5F, 0, 11, 11, 0.0F);
        this.upperTorso = new ModelRendererPlus(this, 23, 65);
        this.upperTorso.setDefaultRotPoint(0.0F, -1.6F, 0.0F);
        this.upperTorso.addBox(-6.0F, -2.5F, -3.0F, 12, 5, 6, 0.0F);
        this.rightFinger4 = new ModelRendererPlus(this, 0, 0);
        this.rightFinger4.mirror=true;
        this.rightFinger4.setDefaultRotPoint(0.0F, 1.0F, -1.8F);
        this.rightFinger4.addBox(0.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.leftFinger1 = new ModelRendererPlus(this, 0, 0);
        this.leftFinger1.setDefaultRotPoint(0.0F, 1.0F, -1.8F);
        this.leftFinger1.addBox(-2.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.rightFinger1 = new ModelRendererPlus(this, 0, 0);
        this.rightFinger1.mirror=true;
        this.rightFinger1.setDefaultRotPoint(0.0F, 1.0F, 1.5F);
        this.rightFinger1.addBox(0.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.leftShoulder = new ModelRendererPlus(this, 10, 51);
        this.leftShoulder.setDefaultRotPoint(2.4F, -2.0F, 0.0F);
        this.leftShoulder.addBox(-2.5F, -0.5F, -2.5F, 5, 1, 5, 0.0F);
        this.setRotateAngle(this.leftShoulder, 0.0F, 0.0F, 9F);
        this.leftToe4 = new ModelRendererPlus(this, 13, 66);
        this.leftToe4.setDefaultRotPoint(-1.1F, 0.0F, -1.5F);
        this.leftToe4.addBox(-0.5F, 0.0F, -2.0F, 1, 1, 2, 0.0F);
        this.hair6 = new ModelRendererPlus(this, 34, 5);
        this.hair6.setDefaultRotPoint(-4.0F, -1.5F, 2.0F);
        this.hair6.addBox(0.0F, -5.5F, -5.5F, 0, 11, 11, 0.0F);
        this.setRotateAngle(this.hair6, 0.0F, -13.04F, 0.0F);
        this.absMuscles2 = new ModelRendererPlus(this, 0, 0);
        this.absMuscles2.setDefaultRotPoint(-1.1F, -1.2F, -2.3F);
        this.absMuscles2.addBox(-1.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.leftToe1 = new ModelRendererPlus(this, 13, 66);
        this.leftToe1.setDefaultRotPoint(1.1F, 0.0F, -1.5F);
        this.leftToe1.addBox(-0.5F, 0.0F, -2.0F, 1, 1, 2, 0.0F);
        this.leftToe3 = new ModelRendererPlus(this, 13, 66);
        this.leftToe3.setDefaultRotPoint(0.0F, 0.0F, -1.5F);
        this.leftToe3.addBox(-0.5F, 0.0F, -2.0F, 1, 1, 2, 0.0F);
        this.lowerTorso = new ModelRendererPlus(this, 0, 6);
        this.lowerTorso.setDefaultRotPoint(0.0F, 4.5F, 0.0F);
        this.lowerTorso.addBox(-5.0F, -2.0F, -2.5F, 10, 4, 5, 0.0F);
        this.leftLowerThigh = new ModelRendererPlus(this, 0, 56);
        this.leftLowerThigh.setDefaultRotPoint(0.0F, 3.9F, 0.7F);
        this.leftLowerThigh.addBox(-1.5F, -2.5F, -1.5F, 3, 5, 3, 0.0F);
        this.rightBicepsJoint = new ModelRendererPlus(this, 0, 0);
        this.rightBicepsJoint.setDefaultRotPoint(-2.0F, 2.0F, 0.0F);
        this.rightBicepsJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(this.rightBicepsJoint, 0.0F, 0.0F, 50F);
        this.leftLowerArm = new ModelRendererPlus(this, 26, 38);
        this.leftLowerArm.setDefaultRotPoint(0.2F, 4.0F, 0.0F);
        this.leftLowerArm.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
        this.hair5 = new ModelRendererPlus(this, 34, 16);
        this.hair5.setDefaultRotPoint(-2.4F, -1.5F, 2.0F);
        this.hair5.addBox(0.0F, -5.5F, -5.5F, 0, 11, 11, 0.0F);
        this.rightFinger3 = new ModelRendererPlus(this, 0, 0);
        this.rightFinger3.mirror=true;
        this.rightFinger3.setDefaultRotPoint(0.0F, 1.0F, -0.7F);
        this.rightFinger3.addBox(0.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.head = new ModelRendererPlus(this, 30, 0);
        this.head.setDefaultRotPoint(0.0F, -4.5F, 0.0F);
        this.head.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, 0.0F);
        this.rightShin = new ModelRendererPlus(this, 18, 70);
        this.rightShin.setDefaultRotPoint(0.0F, 0.0F, -1.5F);
        this.rightShin.addBox(-0.5F, -2.5F, -0.5F, 1, 5, 1, 0.0F);
        this.setRotateAngle(this.rightShin, 2.61F, 0.0F, 0.0F);
        this.rightToe3 = new ModelRendererPlus(this, 13, 66);
        this.rightToe3.setDefaultRotPoint(1.1F, 0.0F, -1.5F);
        this.rightToe3.addBox(-0.5F, 0.0F, -2.0F, 1, 1, 2, 0.0F);
        this.leftElbowBone = new ModelRendererPlus(this, 10, 0);
        this.leftElbowBone.setDefaultRotPoint(2.0F, -4.0F, 0.0F);
        this.leftElbowBone.addBox(-0.5F, -2.0F, -1.0F, 1, 4, 2, 0.0F);
        this.setRotateAngle(this.leftElbowBone, 0.0F, 0.0F, 14.24F);
        this.leftFinger4 = new ModelRendererPlus(this, 0, 0);
        this.leftFinger4.setDefaultRotPoint(0.0F, 1.0F, 1.5F);
        this.leftFinger4.addBox(-2.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.rightFinger2 = new ModelRendererPlus(this, 0, 0);
        this.rightFinger2.mirror=true;
        this.rightFinger2.setDefaultRotPoint(0.0F, 1.0F, 0.4F);
        this.rightFinger2.addBox(0.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.rightToe2 = new ModelRendererPlus(this, 13, 66);
        this.rightToe2.setDefaultRotPoint(1.5F, 0.0F, -1.3F);
        this.rightToe2.addBox(-0.5F, 0.0F, -2.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(this.rightToe2, 0.0F, -28.7F, 0.0F);
        this.leftToe5 = new ModelRendererPlus(this, 13, 66);
        this.leftToe5.setDefaultRotPoint(-1.5F, 0.0F, -1.3F);
        this.leftToe5.addBox(-0.5F, 0.0F, -2.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(this.leftToe5, 0.0F, 28.7F, 0.0F);
        this.rightLowerThigh = new ModelRendererPlus(this, 0, 56);
        this.rightLowerThigh.setDefaultRotPoint(0.0F, 3.9F, 0.7F);
        this.rightLowerThigh.addBox(-1.5F, -2.5F, -1.5F, 3, 5, 3, 0.0F);
        this.rightThumpDown = new ModelRendererPlus(this, 16, 60);
        this.rightThumpDown.mirror=true;
        this.rightThumpDown.setDefaultRotPoint(0.0F, 1.6F, -0.2F);
        this.rightThumpDown.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(this.rightThumpDown, 45.32F, 0.0F, 0.0F);
        this.chestMuscle1 = new ModelRendererPlus(this, 0, 3);
        this.chestMuscle1.setDefaultRotPoint(2.1F, 1.1F, -2.9F);
        this.chestMuscle1.addBox(-2.0F, -1.0F, -0.5F, 4, 2, 1, 0.0F);
        this.rightToe1 = new ModelRendererPlus(this, 13, 66);
        this.rightToe1.setDefaultRotPoint(-1.5F, 0.0F, -1.0F);
        this.rightToe1.addBox(-0.5F, 0.0F, -2.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(this.rightToe1, 0.0F, 28.7F, 0.0F);
        this.leftUpperThigh = new ModelRendererPlus(this, 0, 43);
        this.leftUpperThigh.setDefaultRotPoint(3.0F, 3.8F, 0.0F);
        this.leftUpperThigh.addBox(-1.5F, -2.5F, -1.5F, 3, 4, 3, 0.0F);
        this.rightArmUp = new ModelRendererPlus(this, 12, 43);
        this.rightArmUp.mirror=true;
        this.rightArmUp.setDefaultRotPoint(-6.0F, 0.0F, 0.0F);
        this.rightArmUp.addBox(-4.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
        this.rightAnkle = new ModelRendererPlus(this, 0, 50);
        this.rightAnkle.setDefaultRotPoint(0.0F, 3.0F, -0.4F);
        this.rightAnkle.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3, 0.0F);
        this.rFootShape1 = new ModelRendererPlus(this, 0, 0);
        this.rFootShape1.setDefaultRotPoint(0.0F, 0.0F, -1.9F);
        this.rFootShape1.addBox(-1.5F, 0.0F, 0.0F, 3, 1, 1, 0.0F);
        this.setRotateAngle(this.rFootShape1, 45F, 0.0F, 0.0F);
        this.absMuscles3 = new ModelRendererPlus(this, 0, 0);
        this.absMuscles3.setDefaultRotPoint(-1.1F, -0.1F, -2.3F);
        this.absMuscles3.addBox(-1.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.hair3 = new ModelRendererPlus(this, 34, 5);
        this.hair3.setDefaultRotPoint(-1.2F, -1.5F, 2.0F);
        this.hair3.addBox(0.0F, -5.5F, -5.5F, 0, 11, 11, 0.0F);
        this.leftBicepsJoint = new ModelRendererPlus(this, 0, 0);
        this.leftBicepsJoint.setDefaultRotPoint(2.0F, 2.0F, 0.0F);
        this.leftBicepsJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(this.leftBicepsJoint, 0.0F, 0.0F, -50F);
        this.leftThumpDown = new ModelRendererPlus(this, 16, 60);
        this.leftThumpDown.setDefaultRotPoint(0.0F, 1.6F, -0.2F);
        this.leftThumpDown.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(this.leftThumpDown, 45.32F, 0.0F, 0.0F);
        this.rightElbow = new ModelRendererPlus(this, 28, 48);
        this.rightElbow.mirror=true;
        this.rightElbow.setDefaultRotPoint(-0.6F, 0.9F, 0.0F);
        this.rightElbow.addBox(-1.5F, -2.0F, -1.5F, 3, 4, 3, 0.0F);
        this.outerSkirt = new ModelRendererPlus(this, 0, 21);
        this.outerSkirt.setDefaultRotPoint(0.0F, 4.5F, 0.0F);
        this.outerSkirt.addBox(-5.5F, -3.5F, -3.0F, 11, 7, 6, 0.0F);
        this.absMuscles4 = new ModelRendererPlus(this, 0, 0);
        this.absMuscles4.setDefaultRotPoint(-1.1F, 1.0F, -2.3F);
        this.absMuscles4.addBox(-1.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.rFootUp = new ModelRendererPlus(this, 0, 64);
        this.rFootUp.setDefaultRotPoint(0.0F, 4.0F, -0.4F);
        this.rFootUp.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3, 0.0F);
        this.leftFinger3 = new ModelRendererPlus(this, 0, 0);
        this.leftFinger3.setDefaultRotPoint(0.0F, 1.0F, 0.4F);
        this.leftFinger3.addBox(-2.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.rightShoulder = new ModelRendererPlus(this, 10, 51);
        this.rightShoulder.mirror=true;
        this.rightShoulder.setDefaultRotPoint(-2.4F, -2.0F, 0.0F);
        this.rightShoulder.addBox(-2.5F, -0.5F, -2.5F, 5, 1, 5, 0.0F);
        this.setRotateAngle(this.rightShoulder, 0.0F, 0.0F, -9F);
        this.neck = new ModelRendererPlus(this, 26, 0);
        this.neck.setDefaultRotPoint(0.0F, -3.0F, 0.0F);
        this.neck.addBox(-1.0F, -0.5F, -1.0F, 2, 1, 2, 0.0F);
        this.hair2 = new ModelRendererPlus(this, 34, 5);
        this.hair2.setDefaultRotPoint(1.2F, -1.5F, 2.0F);
        this.hair2.addBox(0.0F, -5.5F, -5.5F, 0, 11, 11, 0.0F);
        this.rightLowerArm = new ModelRendererPlus(this, 26, 38);
        this.rightLowerArm.mirror=true;
        this.rightLowerArm.setDefaultRotPoint(-0.2F, 4.0F, 0.0F);
        this.rightLowerArm.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
        this.leftShin = new ModelRendererPlus(this, 18, 70);
        this.leftShin.setDefaultRotPoint(0.0F, 0.0F, -1.5F);
        this.leftShin.addBox(-0.5F, -2.5F, -0.5F, 1, 5, 1, 0.0F);
        this.setRotateAngle(this.leftShin, 2.61F, 0.0F, 0.0F);
        this.leftBiceps = new ModelRendererPlus(this, 26, 56);
        this.leftBiceps.setDefaultRotPoint(4.5F, 1.6F, 0.0F);
        this.leftBiceps.addBox(-2.5F, -2.0F, -2.5F, 5, 4, 5, 0.0F);
        this.setRotateAngle(this.leftBiceps, 0.0F, 0.0F, 50F);
        this.rightHand = new ModelRendererPlus(this, 16, 0);
        this.rightHand.mirror=true;
        this.rightHand.setDefaultRotPoint(-1.0F, 2.5F, 0.0F);
        this.rightHand.addBox(-0.5F, -1.0F, -2.0F, 1, 2, 4, 0.0F);
        this.hair7 = new ModelRendererPlus(this, 34, 5);
        this.hair7.setDefaultRotPoint(4.0F, -1.5F, 2.0F);
        this.hair7.addBox(0.0F, -5.5F, -5.5F, 0, 11, 11, 0.0F);
        this.setRotateAngle(this.hair7, 0.0F, 13.04F, 0.0F);
        this.rightThumbUp = new ModelRendererPlus(this, 16, 60);
        this.rightThumbUp.mirror=true;
        this.rightThumbUp.setDefaultRotPoint(1.0F, 0.5F, -1.7F);
        this.rightThumbUp.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(this.rightThumbUp, -45.32F, 0.0F, 0.0F);
        this.rightfoot = new ModelRendererPlus(this, 0, 70);
        this.rightfoot.setDefaultRotPoint(0.0F, 1.4F, -0.3F);
        this.rightfoot.addBox(-2.0F, 0.0F, -2.5F, 4, 1, 5, 0.0F);
        this.rightBiceps = new ModelRendererPlus(this, 26, 56);
        this.rightBiceps.mirror=true;
        this.rightBiceps.setDefaultRotPoint(-4.5F, 1.6F, 0.0F);
        this.rightBiceps.addBox(-2.5F, -2.0F, -2.5F, 5, 4, 5, 0.0F);
        this.setRotateAngle(this.rightBiceps, 0.0F, 0.0F, -50F);
        this.leftHand = new ModelRendererPlus(this, 16, 0);
        this.leftHand.setDefaultRotPoint(1.0F, 2.5F, 0.0F);
        this.leftHand.addBox(-0.5F, -1.0F, -2.0F, 1, 2, 4, 0.0F);
        this.rightElbowBone = new ModelRendererPlus(this, 10, 0);
        this.rightElbowBone.mirror=true;
        this.rightElbowBone.setDefaultRotPoint(-2.0F, -4.0F, 0.0F);
        this.rightElbowBone.addBox(-0.5F, -2.0F, -1.0F, 1, 4, 2, 0.0F);
        this.setRotateAngle(this.rightElbowBone, 0.0F, 0.0F, -14.24F);
        this.leftFoot = new ModelRendererPlus(this, 0, 70);
        this.leftFoot.setDefaultRotPoint(0.0F, 1.4F, -0.3F);
        this.leftFoot.addBox(-2.0F, 0.0F, -2.5F, 4, 1, 5, 0.0F);
        this.leftWrist = new ModelRendererPlus(this, 59, 69);
        this.leftWrist.setDefaultRotPoint(0.0F, 3.5F, 0.0F);
        this.leftWrist.addBox(-1.5F, -1.5F, -2.0F, 3, 3, 4, 0.0F);
        this.Waist.addChild(this.rightUpperThigh);
        this.leftFoot.addChild(this.lFootShape1);
        this.rightLowerArm.addChild(this.rightWrist);
        this.leftHand.addChild(this.leftFinger2);
        this.head.addChild(this.nose);
        this.leftWrist.addChild(this.leftThumpUp);
        this.lowerTorso.addChild(this.absMuscles6);
        this.lowerTorso.addChild(this.Waist);
        this.leftFoot.addChild(this.lFootShape2);
        this.rightfoot.addChild(this.rightToe4);
        this.outerSkirt.addChild(this.innerSkirt);
        this.leftLowerThigh.addChild(this.leftFootUp);
        this.upperTorso.addChild(this.chestMuscle2);
        this.upperTorso.addChild(this.chestMuscle3);
        this.lowerTorso.addChild(this.absMuscles1);
        this.upperTorso.addChild(this.chestMuscle4);
        this.leftFoot.addChild(this.leftToe2);
        this.rightfoot.addChild(this.rightToe5);
        this.lowerTorso.addChild(this.absMuscles5);
        this.rightfoot.addChild(this.rFootShape2);
        this.upperTorso.addChild(this.leftArmUp);
        this.leftBicepsJoint.addChild(this.leftElbow);
        this.leftUpperThigh.addChild(this.leftAnkle);
        this.head.addChild(this.hair4);
        this.head.addChild(this.hair1);
        this.rightHand.addChild(this.rightFinger4);
        this.leftHand.addChild(this.leftFinger1);
        this.rightHand.addChild(this.rightFinger1);
        this.leftArmUp.addChild(this.leftShoulder);
        this.leftFoot.addChild(this.leftToe4);
        this.head.addChild(this.hair6);
        this.lowerTorso.addChild(this.absMuscles2);
        this.leftFoot.addChild(this.leftToe1);
        this.leftFoot.addChild(this.leftToe3);
        this.upperTorso.addChild(this.lowerTorso);
        this.leftAnkle.addChild(this.leftLowerThigh);
        this.rightBiceps.addChild(this.rightBicepsJoint);
        this.leftElbow.addChild(this.leftLowerArm);
        this.head.addChild(this.hair5);
        this.rightHand.addChild(this.rightFinger3);
        this.neck.addChild(this.head);
        this.rightLowerThigh.addChild(this.rightShin);
        this.rightfoot.addChild(this.rightToe3);
        this.leftLowerArm.addChild(this.leftElbowBone);
        this.leftHand.addChild(this.leftFinger4);
        this.rightHand.addChild(this.rightFinger2);
        this.rightfoot.addChild(this.rightToe2);
        this.leftFoot.addChild(this.leftToe5);
        this.rightAnkle.addChild(this.rightLowerThigh);
        this.rightThumbUp.addChild(this.rightThumpDown);
        this.upperTorso.addChild(this.chestMuscle1);
        this.rightfoot.addChild(this.rightToe1);
        this.Waist.addChild(this.leftUpperThigh);
        this.upperTorso.addChild(this.rightArmUp);
        this.rightUpperThigh.addChild(this.rightAnkle);
        this.rightfoot.addChild(this.rFootShape1);
        this.lowerTorso.addChild(this.absMuscles3);
        this.head.addChild(this.hair3);
        this.leftBiceps.addChild(this.leftBicepsJoint);
        this.leftThumpUp.addChild(this.leftThumpDown);
        this.rightBicepsJoint.addChild(this.rightElbow);
        this.Waist.addChild(this.outerSkirt);
        this.lowerTorso.addChild(this.absMuscles4);
        this.rightLowerThigh.addChild(this.rFootUp);
        this.leftHand.addChild(this.leftFinger3);
        this.rightArmUp.addChild(this.rightShoulder);
        this.upperTorso.addChild(this.neck);
        this.head.addChild(this.hair2);
        this.rightElbow.addChild(this.rightLowerArm);
        this.leftLowerThigh.addChild(this.leftShin);
        this.leftArmUp.addChild(this.leftBiceps);
        this.rightWrist.addChild(this.rightHand);
        this.head.addChild(this.hair7);
        this.rightWrist.addChild(this.rightThumbUp);
        this.rFootUp.addChild(this.rightfoot);
        this.rightArmUp.addChild(this.rightBiceps);
        this.leftWrist.addChild(this.leftHand);
        this.rightLowerArm.addChild(this.rightElbowBone);
        this.leftFootUp.addChild(this.leftFoot);
        this.leftLowerArm.addChild(this.leftWrist);
        
        this.swing_1 = new TabulaAnimation(this, new ResourceLocation(LibReference.MODID, "models/entity/animation/heracles_swing_1.json"));
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) { 
    	this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        this.upperTorso.render(scale);
    }
    
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity)
    {
		if(!(entity instanceof EntityHeracles))
			return;
		this.setRotationAnglesPre(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);

		EntityHeracles heracles = (EntityHeracles)entity;
		
		if(heracles.isStaying())
		{
			
		}
		else
		{
			float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
			AnimatedAction anim = heracles.getAnimation();
	    	if(anim!=null)
	    	{
	    		if(anim.getID().equals("swing_1"))
	    			this.swing_1.animate(anim.getTick(), partialTicks);
	    	}
		}
		this.syncOverlay();	
	}
    
    @Override
    public void setRotationAnglesPre(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity) {    	
    	this.resetModel();
    	this.head.rotateAngleY = netHeadYaw / (180F / (float)Math.PI);
        this.head.rotateAngleX = headPitch / (180F / (float)Math.PI);
        
        this.rightArmUp.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        this.leftArmUp.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        this.rightArmUp.rotateAngleZ = 0.0F;
        this.leftArmUp.rotateAngleZ = 0.0F;
        this.rightUpperThigh.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount;
        this.leftUpperThigh.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
        
        if (this.isRiding)
        {
            this.rightArmUp.rotateAngleX += -((float)Math.PI / 5F);
            this.leftArmUp.rotateAngleX += -((float)Math.PI / 5F);
        }
        
        if (this.heldItemOff == 1)
        {
            this.leftArmUp.rotateAngleX = this.leftArmUp.rotateAngleX * 0.5F - ((float)Math.PI / 10F);
        }
        
        if (this.heldItemMain == 1)
        {
            this.rightArmUp.rotateAngleX = -(this.rightArmUp.rotateAngleX * 0.5F - ((float)Math.PI / 10F));
        }
        
        this.rightArmUp.rotateAngleY = 0.0F;
        this.leftArmUp.rotateAngleY = 0.0F;
        float var8;
        float var9;
        
        if (this.swingProgress > -9990.0F)
        {
            var8 = this.swingProgress;
            this.upperTorso.rotateAngleY = MathHelper.sin(MathHelper.sqrt(var8) * (float)Math.PI * 2.0F) * 0.2F;
            this.rightArmUp.rotationPointZ = MathHelper.sin(this.servantBody.rotateAngleY) * 5.0F;
            this.rightArmUp.rotationPointX = -MathHelper.cos(this.servantBody.rotateAngleY) * 5.0F;
            this.leftArmUp.rotationPointZ = -MathHelper.sin(this.servantBody.rotateAngleY) * 5.0F;
            this.leftArmUp.rotationPointX = MathHelper.cos(this.servantBody.rotateAngleY) * 5.0F;
            this.rightArmUp.rotateAngleY += this.servantBody.rotateAngleY;
            this.leftArmUp.rotateAngleY += this.servantBody.rotateAngleY;
            this.leftArmUp.rotateAngleX += this.servantBody.rotateAngleY;
            var8 = 1.0F - this.swingProgress;
            var8 *= var8;
            var8 *= var8;
            var8 = 1.0F - var8;
            var9 = MathHelper.sin(var8 * (float)Math.PI);
            float var10 = MathHelper.sin(this.swingProgress * (float)Math.PI) * -(this.servantHead.rotateAngleX - 0.7F) * 0.75F;
            this.rightArmUp.rotateAngleX = (float)((double)this.rightArmUp.rotateAngleX - ((double)var9 * 1.2D + (double)var10));
            this.rightArmUp.rotateAngleY += this.servantBody.rotateAngleY * 2.0F;
            this.rightArmUp.rotateAngleZ = MathHelper.sin(this.swingProgress * (float)Math.PI) * -0.4F;
        }
        
        this.upperTorso.rotateAngleX = 0.0F;
        
        this.rightArmUp.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.leftArmUp.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.rightArmUp.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        this.rightArmUp.rotateAngleX = -this.rightArmUp.rotateAngleX;
        this.leftArmUp.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
	}
    
    @Override
	public void postRenderArm(float scale, EnumHandSide side)
    {
        if(side == EnumHandSide.LEFT)
        {
        	this.leftArmUp.postRender(scale);
        	this.leftBiceps.postRender(scale);
        	this.leftBicepsJoint.postRender(0);
            this.leftElbow.postRender(scale);
            this.leftLowerArm.postRender(scale/2);
            this.leftWrist.postRender(scale/4);
            GlStateManager.translate(-0.05, 0, 0);
        }
        else if(side == EnumHandSide.RIGHT)
        {         
            this.rightArmUp.postRender(scale);
        	this.rightBiceps.postRender(scale);
        	this.rightBicepsJoint.postRender(0);
            this.rightElbow.postRender(scale);
            this.leftLowerArm.postRender(scale/2);
            this.rightWrist.postRender(scale/4);
            GlStateManager.translate(0.05, 0, 0);
        }
    }
    
    @Override
	public void resetModel()
	{
        this.upperTorso.reset();
        this.resetChild(this.upperTorso);
	}

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRendererPlus modelRenderer, float x, float y, float z) {
    	modelRenderer.setDefaultRotAngle(ModelUtils.degToRad(x), ModelUtils.degToRad(y), ModelUtils.degToRad(z));
    }
}
