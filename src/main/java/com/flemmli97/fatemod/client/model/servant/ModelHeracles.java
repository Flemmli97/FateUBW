package com.flemmli97.fatemod.client.model.servant;

import com.flemmli97.fatemod.client.model.ModelUtils;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;

/**
 * Heracles_Berserker - Black_Saturn
 * Created using Tabula 4.1.1
 */
public class ModelHeracles extends ModelServant {
    public ModelRenderer upperTorso;
    public ModelRenderer lowerTorso;
    public ModelRenderer chestMuscle1;
    public ModelRenderer chestMuscle2;
    public ModelRenderer chestMuscle3;
    public ModelRenderer chestMuscle4;
    public ModelRenderer rightArmUp;
    public ModelRenderer leftArmUp;
    public ModelRenderer neck;
    public ModelRenderer Waist;
    public ModelRenderer absMuscles1;
    public ModelRenderer absMuscles2;
    public ModelRenderer absMuscles3;
    public ModelRenderer absMuscles4;
    public ModelRenderer absMuscles5;
    public ModelRenderer absMuscles_6;
    public ModelRenderer rightUpperThigh;
    public ModelRenderer leftUpperThigh;
    public ModelRenderer outerSkirt;
    public ModelRenderer rightAnkle;
    public ModelRenderer rightLowerThigh;
    public ModelRenderer rFootUp;
    public ModelRenderer rightShin;
    public ModelRenderer rightfoot;
    public ModelRenderer rFootShape1;
    public ModelRenderer rFootShape2;
    public ModelRenderer rightToe1;
    public ModelRenderer rightToe2;
    public ModelRenderer rightToe3;
    public ModelRenderer rightToe4;
    public ModelRenderer rightToe5;
    public ModelRenderer leftAnkle;
    public ModelRenderer leftLowerThigh;
    public ModelRenderer leftFootUp;
    public ModelRenderer leftShin;
    public ModelRenderer leftFoot;
    public ModelRenderer leftToe1;
    public ModelRenderer leftToe2;
    public ModelRenderer leftToe3;
    public ModelRenderer leftToe4;
    public ModelRenderer leftToe5;
    public ModelRenderer lFootShape1;
    public ModelRenderer lFootShape2;
    public ModelRenderer innerSkirt;
    public ModelRenderer rightShoulder;
    public ModelRenderer rightBicep;
    public ModelRenderer rightBicepJoint;
    public ModelRenderer rightElbow;
    public ModelRenderer rightLowerArm;
    public ModelRenderer rightWrist;
    public ModelRenderer rightElbowBone;
    public ModelRenderer rightHand;
    public ModelRenderer rightThumbUp;
    public ModelRenderer rightFinger1;
    public ModelRenderer rightFinger2;
    public ModelRenderer rightFinger3;
    public ModelRenderer rightFinger4;
    public ModelRenderer rightThumpDown;
    public ModelRenderer leftShoulder;
    public ModelRenderer leftBicep;
    public ModelRenderer leftBicepJoint;
    public ModelRenderer leftElbow;
    public ModelRenderer leftLowerArm;
    public ModelRenderer leftWrist;
    public ModelRenderer leftElbowBone;
    public ModelRenderer leftHand;
    public ModelRenderer leftThumpUp;
    public ModelRenderer leftFinger1;
    public ModelRenderer leftFinger2;
    public ModelRenderer leftFinger3;
    public ModelRenderer leftFinger4;
    public ModelRenderer leftThumpDown;
    public ModelRenderer head;
    public ModelRenderer nose;
    public ModelRenderer hair1;
    public ModelRenderer hair2;
    public ModelRenderer hair3;
    public ModelRenderer hair4;
    public ModelRenderer hair5;
    public ModelRenderer hair6;
    public ModelRenderer hair7;
    
    public ModelHeracles() {
        this.textureWidth = 76;
        this.textureHeight = 76;
        this.rightUpperThigh = new ModelRenderer(this, 0, 43);
        this.rightUpperThigh.setRotationPoint(-3.0F, 3.8F, 0.0F);
        this.rightUpperThigh.addBox(-1.5F, -2.5F, -1.5F, 3, 4, 3, 0.0F);
        this.lFootShape1 = new ModelRenderer(this, 0, 0);
        this.lFootShape1.setRotationPoint(0.0F, 0.0F, -1.9F);
        this.lFootShape1.addBox(-1.5F, 0.0F, 0.0F, 3, 1, 1, 0.0F);
        this.setRotateAngle(lFootShape1, 45F, 0.0F, 0.0F);
        this.rightWrist = new ModelRenderer(this, 59, 69);
        this.rightWrist.mirror=true;
        this.rightWrist.setRotationPoint(0.0F, 3.5F, 0.0F);
        this.rightWrist.addBox(-1.5F, -1.5F, -2.0F, 3, 3, 4, 0.0F);
        this.leftFinger2 = new ModelRenderer(this, 0, 0);
        this.leftFinger2.setRotationPoint(0.0F, 1.0F, -0.7F);
        this.leftFinger2.addBox(-2.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.nose = new ModelRenderer(this, 16, 60);
        this.nose.setRotationPoint(0.0F, -0.2F, -3.5F);
        this.nose.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(nose, -24.6F, 0.0F, 0.0F);
        this.leftThumpUp = new ModelRenderer(this, 16, 60);
        this.leftThumpUp.setRotationPoint(-1.0F, 0.5F, -1.7F);
        this.leftThumpUp.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(leftThumpUp, -45.32F, 0.0F, 0.0F);
        this.absMuscles_6 = new ModelRenderer(this, 0, 0);
        this.absMuscles_6.setRotationPoint(1.1F, 1.0F, -2.3F);
        this.absMuscles_6.addBox(-1.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.Waist = new ModelRenderer(this, 0, 35);
        this.Waist.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.Waist.addBox(-4.5F, -2.0F, -2.0F, 9, 4, 4, 0.0F);
        this.lFootShape2 = new ModelRenderer(this, 0, 0);
        this.lFootShape2.setRotationPoint(0.0F, 0.0F, 1.1F);
        this.lFootShape2.addBox(-1.5F, 0.0F, 0.0F, 3, 1, 1, 0.0F);
        this.setRotateAngle(lFootShape2, 45F, 0.0F, 0.0F);
        this.rightToe4 = new ModelRenderer(this, 13, 66);
        this.rightToe4.setRotationPoint(0.0F, 0.0F, -1.5F);
        this.rightToe4.addBox(-0.5F, 0.0F, -2.0F, 1, 1, 2, 0.0F);
        this.innerSkirt = new ModelRenderer(this, 0, 15);
        this.innerSkirt.setRotationPoint(0.0F, 0.0F, -2.6F);
        this.innerSkirt.addBox(-5.0F, -3.0F, 0.0F, 10, 6, 0, 0.0F);
        this.leftFootUp = new ModelRenderer(this, 0, 64);
        this.leftFootUp.setRotationPoint(0.0F, 4.0F, -0.4F);
        this.leftFootUp.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3, 0.0F);
        this.chestMuscle2 = new ModelRenderer(this, 0, 3);
        this.chestMuscle2.setRotationPoint(-2.1F, 1.1F, -2.9F);
        this.chestMuscle2.addBox(-2.0F, -1.0F, -0.5F, 4, 2, 1, 0.0F);
        this.chestMuscle3 = new ModelRenderer(this, 0, 3);
        this.chestMuscle3.setRotationPoint(2.6F, -1.1F, -2.9F);
        this.chestMuscle3.addBox(-2.0F, -1.0F, -0.5F, 4, 2, 1, 0.0F);
        this.absMuscles1 = new ModelRenderer(this, 0, 0);
        this.absMuscles1.setRotationPoint(1.1F, -1.2F, -2.3F);
        this.absMuscles1.addBox(-1.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.chestMuscle4 = new ModelRenderer(this, 0, 3);
        this.chestMuscle4.setRotationPoint(-2.6F, -1.1F, -2.9F);
        this.chestMuscle4.addBox(-2.0F, -1.0F, -0.5F, 4, 2, 1, 0.0F);
        this.leftToe2 = new ModelRenderer(this, 13, 66);
        this.leftToe2.setRotationPoint(1.5F, 0.0F, -1.0F);
        this.leftToe2.addBox(-0.5F, 0.0F, -2.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(leftToe2, 0.0F, -28.7F, 0.0F);
        this.rightToe5 = new ModelRenderer(this, 13, 66);
        this.rightToe5.setRotationPoint(-1.1F, 0.0F, -1.5F);
        this.rightToe5.addBox(-0.5F, 0.0F, -2.0F, 1, 1, 2, 0.0F);
        this.absMuscles5 = new ModelRenderer(this, 0, 0);
        this.absMuscles5.setRotationPoint(1.1F, -0.1F, -2.3F);
        this.absMuscles5.addBox(-1.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.rFootShape2 = new ModelRenderer(this, 0, 0);
        this.rFootShape2.setRotationPoint(0.0F, 0.0F, 1.1F);
        this.rFootShape2.addBox(-1.5F, 0.0F, 0.0F, 3, 1, 1, 0.0F);
        this.setRotateAngle(rFootShape2, 45F, 0.0F, 0.0F);
        this.leftArmUp = new ModelRenderer(this, 12, 43);
        this.leftArmUp.setRotationPoint(6.0F, 0.0F, 0.0F);
        this.leftArmUp.addBox(0.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
        this.leftElbow = new ModelRenderer(this, 28, 48);
        this.leftElbow.setRotationPoint(0.6F, 0.9F, 0.0F);
        this.leftElbow.addBox(-1.5F, -2.0F, -1.5F, 3, 4, 3, 0.0F);
        this.leftAnkle = new ModelRenderer(this, 0, 50);
        this.leftAnkle.setRotationPoint(0.0F, 3.0F, -0.4F);
        this.leftAnkle.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3, 0.0F);
        this.hair4 = new ModelRenderer(this, 34, 16);
        this.hair4.setRotationPoint(2.4F, -1.5F, 2.0F);
        this.hair4.addBox(0.0F, -5.5F, -5.5F, 0, 11, 11, 0.0F);
        this.hair1 = new ModelRenderer(this, 34, 16);
        this.hair1.setRotationPoint(0.0F, -1.5F, 2.0F);
        this.hair1.addBox(0.0F, -5.5F, -5.5F, 0, 11, 11, 0.0F);
        this.upperTorso = new ModelRenderer(this, 23, 65);
        this.upperTorso.setRotationPoint(0.0F, -1.6F, 0.0F);
        this.upperTorso.addBox(-6.0F, -2.5F, -3.0F, 12, 5, 6, 0.0F);
        this.rightFinger4 = new ModelRenderer(this, 0, 0);
        this.rightFinger4.mirror=true;
        this.rightFinger4.setRotationPoint(0.0F, 1.0F, -1.8F);
        this.rightFinger4.addBox(0.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.leftFinger1 = new ModelRenderer(this, 0, 0);
        this.leftFinger1.setRotationPoint(0.0F, 1.0F, -1.8F);
        this.leftFinger1.addBox(-2.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.rightFinger1 = new ModelRenderer(this, 0, 0);
        this.rightFinger1.mirror=true;
        this.rightFinger1.setRotationPoint(0.0F, 1.0F, 1.5F);
        this.rightFinger1.addBox(0.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.leftShoulder = new ModelRenderer(this, 10, 51);
        this.leftShoulder.setRotationPoint(2.4F, -2.0F, 0.0F);
        this.leftShoulder.addBox(-2.5F, -0.5F, -2.5F, 5, 1, 5, 0.0F);
        this.setRotateAngle(leftShoulder, 0.0F, 0.0F, 9F);
        this.leftToe4 = new ModelRenderer(this, 13, 66);
        this.leftToe4.setRotationPoint(-1.1F, 0.0F, -1.5F);
        this.leftToe4.addBox(-0.5F, 0.0F, -2.0F, 1, 1, 2, 0.0F);
        this.hair6 = new ModelRenderer(this, 34, 5);
        this.hair6.setRotationPoint(-4.0F, -1.5F, 2.0F);
        this.hair6.addBox(0.0F, -5.5F, -5.5F, 0, 11, 11, 0.0F);
        this.setRotateAngle(hair6, 0.0F, -13.04F, 0.0F);
        this.absMuscles2 = new ModelRenderer(this, 0, 0);
        this.absMuscles2.setRotationPoint(-1.1F, -1.2F, -2.3F);
        this.absMuscles2.addBox(-1.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.leftToe1 = new ModelRenderer(this, 13, 66);
        this.leftToe1.setRotationPoint(1.1F, 0.0F, -1.5F);
        this.leftToe1.addBox(-0.5F, 0.0F, -2.0F, 1, 1, 2, 0.0F);
        this.leftToe3 = new ModelRenderer(this, 13, 66);
        this.leftToe3.setRotationPoint(0.0F, 0.0F, -1.5F);
        this.leftToe3.addBox(-0.5F, 0.0F, -2.0F, 1, 1, 2, 0.0F);
        this.lowerTorso = new ModelRenderer(this, 0, 6);
        this.lowerTorso.setRotationPoint(0.0F, 4.5F, 0.0F);
        this.lowerTorso.addBox(-5.0F, -2.0F, -2.5F, 10, 4, 5, 0.0F);
        this.leftLowerThigh = new ModelRenderer(this, 0, 56);
        this.leftLowerThigh.setRotationPoint(0.0F, 3.9F, 0.7F);
        this.leftLowerThigh.addBox(-1.5F, -2.5F, -1.5F, 3, 5, 3, 0.0F);
        this.rightBicepJoint = new ModelRenderer(this, 0, 0);
        this.rightBicepJoint.setRotationPoint(-2.0F, 2.0F, 0.0F);
        this.rightBicepJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(rightBicepJoint, 0.0F, 0.0F, 50F);
        this.leftLowerArm = new ModelRenderer(this, 26, 38);
        this.leftLowerArm.setRotationPoint(0.2F, 4.0F, 0.0F);
        this.leftLowerArm.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
        this.hair5 = new ModelRenderer(this, 34, 16);
        this.hair5.setRotationPoint(-2.4F, -1.5F, 2.0F);
        this.hair5.addBox(0.0F, -5.5F, -5.5F, 0, 11, 11, 0.0F);
        this.rightFinger3 = new ModelRenderer(this, 0, 0);
        this.rightFinger3.mirror=true;
        this.rightFinger3.setRotationPoint(0.0F, 1.0F, -0.7F);
        this.rightFinger3.addBox(0.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.head = new ModelRenderer(this, 30, 0);
        this.head.setRotationPoint(0.0F, -4.5F, 0.0F);
        this.head.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, 0.0F);
        this.rightShin = new ModelRenderer(this, 18, 70);
        this.rightShin.setRotationPoint(0.0F, 0.0F, -1.5F);
        this.rightShin.addBox(-0.5F, -2.5F, -0.5F, 1, 5, 1, 0.0F);
        this.setRotateAngle(rightShin, 2.61F, 0.0F, 0.0F);
        this.rightToe3 = new ModelRenderer(this, 13, 66);
        this.rightToe3.setRotationPoint(1.1F, 0.0F, -1.5F);
        this.rightToe3.addBox(-0.5F, 0.0F, -2.0F, 1, 1, 2, 0.0F);
        this.leftElbowBone = new ModelRenderer(this, 10, 0);
        this.leftElbowBone.setRotationPoint(2.0F, -4.0F, 0.0F);
        this.leftElbowBone.addBox(-0.5F, -2.0F, -1.0F, 1, 4, 2, 0.0F);
        this.setRotateAngle(leftElbowBone, 0.0F, 0.0F, 14.24F);
        this.leftFinger4 = new ModelRenderer(this, 0, 0);
        this.leftFinger4.setRotationPoint(0.0F, 1.0F, 1.5F);
        this.leftFinger4.addBox(-2.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.rightFinger2 = new ModelRenderer(this, 0, 0);
        this.rightFinger2.mirror=true;
        this.rightFinger2.setRotationPoint(0.0F, 1.0F, 0.4F);
        this.rightFinger2.addBox(0.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.rightToe2 = new ModelRenderer(this, 13, 66);
        this.rightToe2.setRotationPoint(1.5F, 0.0F, -1.3F);
        this.rightToe2.addBox(-0.5F, 0.0F, -2.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(rightToe2, 0.0F, -28.7F, 0.0F);
        this.leftToe5 = new ModelRenderer(this, 13, 66);
        this.leftToe5.setRotationPoint(-1.5F, 0.0F, -1.3F);
        this.leftToe5.addBox(-0.5F, 0.0F, -2.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(leftToe5, 0.0F, 28.7F, 0.0F);
        this.rightLowerThigh = new ModelRenderer(this, 0, 56);
        this.rightLowerThigh.setRotationPoint(0.0F, 3.9F, 0.7F);
        this.rightLowerThigh.addBox(-1.5F, -2.5F, -1.5F, 3, 5, 3, 0.0F);
        this.rightThumpDown = new ModelRenderer(this, 16, 60);
        this.rightThumpDown.mirror=true;
        this.rightThumpDown.setRotationPoint(0.0F, 1.6F, -0.2F);
        this.rightThumpDown.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(rightThumpDown, 45.32F, 0.0F, 0.0F);
        this.chestMuscle1 = new ModelRenderer(this, 0, 3);
        this.chestMuscle1.setRotationPoint(2.1F, 1.1F, -2.9F);
        this.chestMuscle1.addBox(-2.0F, -1.0F, -0.5F, 4, 2, 1, 0.0F);
        this.rightToe1 = new ModelRenderer(this, 13, 66);
        this.rightToe1.setRotationPoint(-1.5F, 0.0F, -1.0F);
        this.rightToe1.addBox(-0.5F, 0.0F, -2.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(rightToe1, 0.0F, 28.7F, 0.0F);
        this.leftUpperThigh = new ModelRenderer(this, 0, 43);
        this.leftUpperThigh.setRotationPoint(3.0F, 3.8F, 0.0F);
        this.leftUpperThigh.addBox(-1.5F, -2.5F, -1.5F, 3, 4, 3, 0.0F);
        this.rightArmUp = new ModelRenderer(this, 12, 43);
        this.rightArmUp.mirror=true;
        this.rightArmUp.setRotationPoint(-6.0F, 0.0F, 0.0F);
        this.rightArmUp.addBox(-4.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
        this.rightAnkle = new ModelRenderer(this, 0, 50);
        this.rightAnkle.setRotationPoint(0.0F, 3.0F, -0.4F);
        this.rightAnkle.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3, 0.0F);
        this.rFootShape1 = new ModelRenderer(this, 0, 0);
        this.rFootShape1.setRotationPoint(0.0F, 0.0F, -1.9F);
        this.rFootShape1.addBox(-1.5F, 0.0F, 0.0F, 3, 1, 1, 0.0F);
        this.setRotateAngle(rFootShape1, 45F, 0.0F, 0.0F);
        this.absMuscles3 = new ModelRenderer(this, 0, 0);
        this.absMuscles3.setRotationPoint(-1.1F, -0.1F, -2.3F);
        this.absMuscles3.addBox(-1.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.hair3 = new ModelRenderer(this, 34, 5);
        this.hair3.setRotationPoint(-1.2F, -1.5F, 2.0F);
        this.hair3.addBox(0.0F, -5.5F, -5.5F, 0, 11, 11, 0.0F);
        this.leftBicepJoint = new ModelRenderer(this, 0, 0);
        this.leftBicepJoint.setRotationPoint(2.0F, 2.0F, 0.0F);
        this.leftBicepJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(leftBicepJoint, 0.0F, 0.0F, -50F);
        this.leftThumpDown = new ModelRenderer(this, 16, 60);
        this.leftThumpDown.setRotationPoint(0.0F, 1.6F, -0.2F);
        this.leftThumpDown.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(leftThumpDown, 45.32F, 0.0F, 0.0F);
        this.rightElbow = new ModelRenderer(this, 28, 48);
        this.rightElbow.mirror=true;
        this.rightElbow.setRotationPoint(-0.6F, 0.9F, 0.0F);
        this.rightElbow.addBox(-1.5F, -2.0F, -1.5F, 3, 4, 3, 0.0F);
        this.outerSkirt = new ModelRenderer(this, 0, 21);
        this.outerSkirt.setRotationPoint(0.0F, 4.5F, 0.0F);
        this.outerSkirt.addBox(-5.5F, -3.5F, -3.0F, 11, 7, 6, 0.0F);
        this.absMuscles4 = new ModelRenderer(this, 0, 0);
        this.absMuscles4.setRotationPoint(-1.1F, 1.0F, -2.3F);
        this.absMuscles4.addBox(-1.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.rFootUp = new ModelRenderer(this, 0, 64);
        this.rFootUp.setRotationPoint(0.0F, 4.0F, -0.4F);
        this.rFootUp.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3, 0.0F);
        this.leftFinger3 = new ModelRenderer(this, 0, 0);
        this.leftFinger3.setRotationPoint(0.0F, 1.0F, 0.4F);
        this.leftFinger3.addBox(-2.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.rightShoulder = new ModelRenderer(this, 10, 51);
        this.rightShoulder.mirror=true;
        this.rightShoulder.setRotationPoint(-2.4F, -2.0F, 0.0F);
        this.rightShoulder.addBox(-2.5F, -0.5F, -2.5F, 5, 1, 5, 0.0F);
        this.setRotateAngle(rightShoulder, 0.0F, 0.0F, -9F);
        this.neck = new ModelRenderer(this, 26, 0);
        this.neck.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.neck.addBox(-1.0F, -0.5F, -1.0F, 2, 1, 2, 0.0F);
        this.hair1 = new ModelRenderer(this, 34, 5);
        this.hair1.setRotationPoint(1.2F, -1.5F, 2.0F);
        this.hair1.addBox(0.0F, -5.5F, -5.5F, 0, 11, 11, 0.0F);
        this.rightLowerArm = new ModelRenderer(this, 26, 38);
        this.rightLowerArm.mirror=true;
        this.rightLowerArm.setRotationPoint(-0.2F, 4.0F, 0.0F);
        this.rightLowerArm.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
        this.leftShin = new ModelRenderer(this, 18, 70);
        this.leftShin.setRotationPoint(0.0F, 0.0F, -1.5F);
        this.leftShin.addBox(-0.5F, -2.5F, -0.5F, 1, 5, 1, 0.0F);
        this.setRotateAngle(leftShin, 2.61F, 0.0F, 0.0F);
        this.leftBicep = new ModelRenderer(this, 26, 56);
        this.leftBicep.setRotationPoint(4.5F, 1.6F, 0.0F);
        this.leftBicep.addBox(-2.5F, -2.0F, -2.5F, 5, 4, 5, 0.0F);
        this.setRotateAngle(leftBicep, 0.0F, 0.0F, 50F);
        this.rightHand = new ModelRenderer(this, 16, 0);
        this.rightHand.mirror=true;
        this.rightHand.setRotationPoint(-1.0F, 2.5F, 0.0F);
        this.rightHand.addBox(-0.5F, -1.0F, -2.0F, 1, 2, 4, 0.0F);
        this.hair7 = new ModelRenderer(this, 34, 5);
        this.hair7.setRotationPoint(4.0F, -1.5F, 2.0F);
        this.hair7.addBox(0.0F, -5.5F, -5.5F, 0, 11, 11, 0.0F);
        this.setRotateAngle(hair7, 0.0F, 13.04F, 0.0F);
        this.rightThumbUp = new ModelRenderer(this, 16, 60);
        this.rightThumbUp.mirror=true;
        this.rightThumbUp.setRotationPoint(1.0F, 0.5F, -1.7F);
        this.rightThumbUp.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(rightThumbUp, -45.32F, 0.0F, 0.0F);
        this.rightfoot = new ModelRenderer(this, 0, 70);
        this.rightfoot.setRotationPoint(0.0F, 1.4F, -0.3F);
        this.rightfoot.addBox(-2.0F, 0.0F, -2.5F, 4, 1, 5, 0.0F);
        this.rightBicep = new ModelRenderer(this, 26, 56);
        this.rightBicep.mirror=true;
        this.rightBicep.setRotationPoint(-4.5F, 1.6F, 0.0F);
        this.rightBicep.addBox(-2.5F, -2.0F, -2.5F, 5, 4, 5, 0.0F);
        this.setRotateAngle(rightBicep, 0.0F, 0.0F, -50F);
        this.leftHand = new ModelRenderer(this, 16, 0);
        this.leftHand.setRotationPoint(1.0F, 2.5F, 0.0F);
        this.leftHand.addBox(-0.5F, -1.0F, -2.0F, 1, 2, 4, 0.0F);
        this.rightElbowBone = new ModelRenderer(this, 10, 0);
        this.rightElbowBone.mirror=true;
        this.rightElbowBone.setRotationPoint(-2.0F, -4.0F, 0.0F);
        this.rightElbowBone.addBox(-0.5F, -2.0F, -1.0F, 1, 4, 2, 0.0F);
        this.setRotateAngle(rightElbowBone, 0.0F, 0.0F, -14.24F);
        this.leftFoot = new ModelRenderer(this, 0, 70);
        this.leftFoot.setRotationPoint(0.0F, 1.4F, -0.3F);
        this.leftFoot.addBox(-2.0F, 0.0F, -2.5F, 4, 1, 5, 0.0F);
        this.leftWrist = new ModelRenderer(this, 59, 69);
        this.leftWrist.setRotationPoint(0.0F, 3.5F, 0.0F);
        this.leftWrist.addBox(-1.5F, -1.5F, -2.0F, 3, 3, 4, 0.0F);
        this.Waist.addChild(this.rightUpperThigh);
        this.leftFoot.addChild(this.lFootShape1);
        this.rightLowerArm.addChild(this.rightWrist);
        this.leftHand.addChild(this.leftFinger2);
        this.head.addChild(this.nose);
        this.leftWrist.addChild(this.leftThumpUp);
        this.lowerTorso.addChild(this.absMuscles_6);
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
        this.leftBicepJoint.addChild(this.leftElbow);
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
        this.rightBicep.addChild(this.rightBicepJoint);
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
        this.leftBicep.addChild(this.leftBicepJoint);
        this.leftThumpUp.addChild(this.leftThumpDown);
        this.rightBicepJoint.addChild(this.rightElbow);
        this.Waist.addChild(this.outerSkirt);
        this.lowerTorso.addChild(this.absMuscles4);
        this.rightLowerThigh.addChild(this.rFootUp);
        this.leftHand.addChild(this.leftFinger3);
        this.rightArmUp.addChild(this.rightShoulder);
        this.upperTorso.addChild(this.neck);
        this.head.addChild(this.hair1);
        this.rightElbow.addChild(this.rightLowerArm);
        this.leftLowerThigh.addChild(this.leftShin);
        this.leftArmUp.addChild(this.leftBicep);
        this.rightWrist.addChild(this.rightHand);
        this.head.addChild(this.hair7);
        this.rightWrist.addChild(this.rightThumbUp);
        this.rFootUp.addChild(this.rightfoot);
        this.rightArmUp.addChild(this.rightBicep);
        this.leftWrist.addChild(this.leftHand);
        this.rightLowerArm.addChild(this.rightElbowBone);
        this.leftFootUp.addChild(this.leftFoot);
        this.leftLowerArm.addChild(this.leftWrist);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) { 
    	this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        this.upperTorso.render(scale);
    }
    
    @Override
    public void setRotationAnglesPre(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity) {    	
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
        	this.leftBicep.postRender(scale);
        	this.leftBicepJoint.postRender(0);
            this.leftElbow.postRender(scale);
            this.leftLowerArm.postRender(scale/2);
            this.leftWrist.postRender(scale/4);
            GlStateManager.translate(-0.05, 0, 0);
        }
        else if(side == EnumHandSide.RIGHT)
        {         
            this.rightArmUp.postRender(scale);
        	this.rightBicep.postRender(scale);
        	this.rightBicepJoint.postRender(0);
            this.rightElbow.postRender(scale);
            this.leftLowerArm.postRender(scale/2);
            this.rightWrist.postRender(scale/4);
            GlStateManager.translate(0.05, 0, 0);
        }
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = ModelUtils.degToRad(x);
        modelRenderer.rotateAngleY = ModelUtils.degToRad(y);
        modelRenderer.rotateAngleZ = ModelUtils.degToRad(z);
    }
}
