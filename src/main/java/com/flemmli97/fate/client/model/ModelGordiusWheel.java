package com.flemmli97.fate.client.model;

import com.flemmli97.fate.common.entity.EntityGordiusWheel;
import com.flemmli97.tenshilib.client.model.IResetModel;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.math.MathHelper;

/**
 * Gordius Wheel - Black_Saturn
 * Created using Tabula 6.0.0
 */

public class ModelGordiusWheel extends EntityModel<EntityGordiusWheel> implements IResetModel {

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
    public ModelRendererPlus centerBeam;
    public ModelRendererPlus backBeam;
    public ModelRendererPlus backBeamJoint;
    public ModelRendererPlus chariotFloor;
    public ModelRendererPlus chariotFront2;
    public ModelRendererPlus chariotDecor2Stick;
    public ModelRendererPlus chariotFloorJoint;
    public ModelRendererPlus chariotWall1Decor;
    public ModelRendererPlus chariotWall2Decor;
    public ModelRendererPlus chariotFloor3;
    public ModelRendererPlus chariotWall3Decor;
    public ModelRendererPlus chariotWall4Decor;
    public ModelRendererPlus chariotCenter;
    public ModelRendererPlus frontAxel;
    public ModelRendererPlus leftWheelWeaponMain;
    public ModelRendererPlus leftWheelWeaponHead;
    public ModelRendererPlus leftWheelWeaponBlade;
    public ModelRendererPlus rightWheelWeaponMain;
    public ModelRendererPlus rightWheelWeaponHead;
    public ModelRendererPlus rightWheelWeaponBlade;
    public ModelRendererPlus frontLeftWheelCenter;
    public ModelRendererPlus frontLeftWheelRod1;
    public ModelRendererPlus frontLeftWheelFrame1;
    public ModelRendererPlus frontLeftWheelRod2;
    public ModelRendererPlus frontLeftWheelFrame2;
    public ModelRendererPlus frontLeftWheelRod3;
    public ModelRendererPlus frontLeftWheelFrame3;
    public ModelRendererPlus frontLeftWheelRod4;
    public ModelRendererPlus frontLeftWheelFrame4;
    public ModelRendererPlus frontLeftWheelRod5;
    public ModelRendererPlus frontLeftWheelFrame5;
    public ModelRendererPlus frontLeftWheelRod6;
    public ModelRendererPlus frontLeftWheelFrame6;
    public ModelRendererPlus frontLeftWheelRod7;
    public ModelRendererPlus frontLeftWheelFrame7;
    public ModelRendererPlus frontLeftWheelRod8;
    public ModelRendererPlus frontLeftWheelFrame8;
    public ModelRendererPlus frontRightWheelCenter;
    public ModelRendererPlus frontRightWheelRod1;
    public ModelRendererPlus frontRightWheelFrame1;
    public ModelRendererPlus frontRightWheelRod2;
    public ModelRendererPlus frontRightWheelFrame2;
    public ModelRendererPlus frontRightWheelRod3;
    public ModelRendererPlus frontRightWheelFrame3;
    public ModelRendererPlus frontRightWheelRod4;
    public ModelRendererPlus frontRightWheelFrame4;
    public ModelRendererPlus frontRightWheelRod5;
    public ModelRendererPlus frontRightWheelFrame5;
    public ModelRendererPlus frontRightWheelRod6;
    public ModelRendererPlus frontRightWheelFrame6;
    public ModelRendererPlus frontRightWheelRod7;
    public ModelRendererPlus frontRightWheelFrame7;
    public ModelRendererPlus frontRightWheelRod8;
    public ModelRendererPlus frontRightWheelFrame8;
    public ModelRendererPlus chariotBackBeam;
    public ModelRendererPlus backAxel;
    public ModelRendererPlus backLeftWheelCenter;
    public ModelRendererPlus backLeftWheelRod1;
    public ModelRendererPlus backLeftWheelFrame1;
    public ModelRendererPlus backLeftWheelRod2;
    public ModelRendererPlus backLeftWheelFrame2;
    public ModelRendererPlus backLeftWheelRod3;
    public ModelRendererPlus backLeftWheelFrame3;
    public ModelRendererPlus backLeftWheelRod4;
    public ModelRendererPlus backLeftWheelFrame4;
    public ModelRendererPlus backLeftWheelRod5;
    public ModelRendererPlus backLeftWheelFrame5;
    public ModelRendererPlus backLeftWheelRod6;
    public ModelRendererPlus backLeftWheelFrame6;
    public ModelRendererPlus backLeftWheelRod7;
    public ModelRendererPlus backLeftWheelFrame7;
    public ModelRendererPlus backLeftWheelRod8;
    public ModelRendererPlus backLeftWheelFrame8;
    public ModelRendererPlus backRightWheelCenter;
    public ModelRendererPlus backRightWheelRod1;
    public ModelRendererPlus backRightWheelFrame1;
    public ModelRendererPlus backRightWheelRod2;
    public ModelRendererPlus backRightWheelFrame2;
    public ModelRendererPlus backRightWheelRod3;
    public ModelRendererPlus backRightWheelFrame3;
    public ModelRendererPlus backRightWheelRod4;
    public ModelRendererPlus backRightWheelFrame4;
    public ModelRendererPlus backRightWheelRod5;
    public ModelRendererPlus backRightWheelFrame5;
    public ModelRendererPlus backRightWheelRod6;
    public ModelRendererPlus backRightWheelFrame6;
    public ModelRendererPlus backRightWheelRod7;
    public ModelRendererPlus backRightWheelFrame7;
    public ModelRendererPlus backRightWheelRod8;
    public ModelRendererPlus backRightWheelFrame8;

    public ModelGordiusWheel() {
        this.textureWidth = 129;
        this.textureHeight = 256;

        this.bull1 = new ModelRendererPlus(this);
        this.bull1.setDefaultRotPoint(-8.5F, 7.0F, -23.0F);
        this.bull1.setTextureOffset(0, 145).addCuboid(-6.0F, -5.0F, -9.0F, 12.0F, 10.0F, 18.0F, 0.0F, false);

        this.equipmentLayer = new ModelRendererPlus(this);
        this.equipmentLayer.setDefaultRotPoint(0.0F, 1.0F, 0.0F);
        this.bull1.addChild(this.equipmentLayer);
        this.equipmentLayer.setTextureOffset(59, 128).addCuboid(-6.5F, -6.3F, -9.5F, 13.0F, 13.0F, 19.0F, 0.0F, false);

        this.footLeftFront = new ModelRendererPlus(this);
        this.footLeftFront.setDefaultRotPoint(4.0F, 5.0F, -6.0F);
        this.bull1.addChild(this.footLeftFront);
        this.footLeftFront.setTextureOffset(0, 144).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        this.footRightFront = new ModelRendererPlus(this);
        this.footRightFront.setDefaultRotPoint(-4.0F, 5.0F, -6.0F);
        this.bull1.addChild(this.footRightFront);
        this.footRightFront.setTextureOffset(0, 144).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        this.footLeftRear = new ModelRendererPlus(this);
        this.footLeftRear.setDefaultRotPoint(4.0F, 5.0F, 7.0F);
        this.bull1.addChild(this.footLeftRear);
        this.footLeftRear.setTextureOffset(0, 144).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        this.footRightRear = new ModelRendererPlus(this);
        this.footRightRear.setDefaultRotPoint(-4.0F, 5.0F, 7.0F);
        this.bull1.addChild(this.footRightRear);
        this.footRightRear.setTextureOffset(0, 144).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        this.head = new ModelRendererPlus(this);
        this.head.setDefaultRotPoint(0.0F, -1.0F, -9.0F);
        this.bull1.addChild(this.head);
        this.head.setTextureOffset(0, 128).addCuboid(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

        this.hornRight = new ModelRendererPlus(this);
        this.hornRight.setDefaultRotPoint(-3.0F, -3.0F, -4.0F);
        this.head.addChild(this.hornRight);
        this.setRotationAngle(this.hornRight, 0.0F, 0.0F, 0.4363F);
        this.hornRight.setTextureOffset(42, 140).addCuboid(-6.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);

        this.hornRightTip = new ModelRendererPlus(this);
        this.hornRightTip.setDefaultRotPoint(-5.0F, 0.0F, 0.0F);
        this.hornRight.addChild(this.hornRightTip);
        this.setRotationAngle(this.hornRightTip, 0.0F, 0.0F, 1.4114F);
        this.hornRightTip.setTextureOffset(42, 140).addCuboid(-4.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);

        this.hornLeft = new ModelRendererPlus(this);
        this.hornLeft.setDefaultRotPoint(3.0F, -3.0F, -4.0F);
        this.head.addChild(this.hornLeft);
        this.setRotationAngle(this.hornLeft, 0.0F, 0.0F, -0.4363F);
        this.hornLeft.setTextureOffset(42, 140).addCuboid(0.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);

        this.hornLeftTip = new ModelRendererPlus(this);
        this.hornLeftTip.setDefaultRotPoint(5.0F, 0.0F, 0.0F);
        this.hornLeft.addChild(this.hornLeftTip);
        this.setRotationAngle(this.hornLeftTip, 0.0F, 0.0F, -1.4114F);
        this.hornLeftTip.setTextureOffset(42, 140).addCuboid(0.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);

        this.lead = new ModelRendererPlus(this);
        this.lead.setDefaultRotPoint(0.0F, -5.7F, -0.5F);
        this.head.addChild(this.lead);
        this.lead.setTextureOffset(0, 173).addCuboid(-4.0F, 0.0F, -0.5F, 8.0F, 0.0F, 1.0F, 0.0F, false);

        this.mouth = new ModelRendererPlus(this);
        this.mouth.setDefaultRotPoint(0.0F, 1.0F, -7.0F);
        this.head.addChild(this.mouth);
        this.setRotationAngle(this.mouth, 0.4363F, 0.0F, 0.0F);
        this.mouth.setTextureOffset(42, 128).addCuboid(-2.5F, -2.5F, -5.0F, 5.0F, 5.0F, 5.0F, 0.0F, false);

        this.leadFront = new ModelRendererPlus(this);
        this.leadFront.setDefaultRotPoint(2.5F, 1.0F, -3.0F);
        this.mouth.addChild(this.leadFront);
        this.setRotationAngle(this.leadFront, 0.0F, 0.7854F, 0.0F);
        this.leadFront.setTextureOffset(0, 167).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 6.0F, 0.0F, false);

        this.leadFront2 = new ModelRendererPlus(this);
        this.leadFront2.setDefaultRotPoint(0.0F, 0.0F, 6.0F);
        this.leadFront.addChild(this.leadFront2);
        this.setRotationAngle(this.leadFront2, 0.0F, -1.2217F, -0.5236F);
        this.leadFront2.setTextureOffset(0, 164).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 9.0F, 0.0F, false);

        this.leadFront3 = new ModelRendererPlus(this);
        this.leadFront3.setDefaultRotPoint(-2.5F, 1.0F, -3.0F);
        this.mouth.addChild(this.leadFront3);
        this.setRotationAngle(this.leadFront3, 0.0F, -0.7854F, 0.0F);
        this.leadFront3.setTextureOffset(0, 167).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 6.0F, 0.0F, false);

        this.leadFront4 = new ModelRendererPlus(this);
        this.leadFront4.setDefaultRotPoint(0.0F, 0.0F, 6.0F);
        this.leadFront3.addChild(this.leadFront4);
        this.setRotationAngle(this.leadFront4, 0.0F, 1.2217F, 0.5236F);
        this.leadFront4.setTextureOffset(0, 164).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 9.0F, 0.0F, false);

        this.bull2 = new ModelRendererPlus(this);
        this.bull2.setDefaultRotPoint(8.5F, 7.0F, -23.0F);
        this.bull2.setTextureOffset(0, 145).addCuboid(-6.0F, -5.0F, -9.0F, 12.0F, 10.0F, 18.0F, 0.0F, false);

        this.equipmentLayer2 = new ModelRendererPlus(this);
        this.equipmentLayer2.setDefaultRotPoint(0.0F, 1.0F, 0.0F);
        this.bull2.addChild(this.equipmentLayer2);
        this.equipmentLayer2.setTextureOffset(59, 128).addCuboid(-6.5F, -6.3F, -9.5F, 13.0F, 13.0F, 19.0F, 0.0F, false);

        this.footLeftFront2 = new ModelRendererPlus(this);
        this.footLeftFront2.setDefaultRotPoint(4.0F, 5.0F, -6.0F);
        this.bull2.addChild(this.footLeftFront2);
        this.footLeftFront2.setTextureOffset(0, 144).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        this.footRightFront2 = new ModelRendererPlus(this);
        this.footRightFront2.setDefaultRotPoint(-4.0F, 5.0F, -6.0F);
        this.bull2.addChild(this.footRightFront2);
        this.footRightFront2.setTextureOffset(0, 144).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        this.footLeftRear2 = new ModelRendererPlus(this);
        this.footLeftRear2.setDefaultRotPoint(4.0F, 5.0F, 7.0F);
        this.bull2.addChild(this.footLeftRear2);
        this.footLeftRear2.setTextureOffset(0, 144).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        this.footRightRear2 = new ModelRendererPlus(this);
        this.footRightRear2.setDefaultRotPoint(-4.0F, 5.0F, 7.0F);
        this.bull2.addChild(this.footRightRear2);
        this.footRightRear2.setTextureOffset(0, 144).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        this.head2 = new ModelRendererPlus(this);
        this.head2.setDefaultRotPoint(0.0F, -1.0F, -9.0F);
        this.bull2.addChild(this.head2);
        this.head2.setTextureOffset(0, 128).addCuboid(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

        this.hornRight2 = new ModelRendererPlus(this);
        this.hornRight2.setDefaultRotPoint(-3.0F, -3.0F, -4.0F);
        this.head2.addChild(this.hornRight2);
        this.setRotationAngle(this.hornRight2, 0.0F, 0.0F, 0.4363F);
        this.hornRight2.setTextureOffset(42, 140).addCuboid(-6.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);

        this.hornRightTip2 = new ModelRendererPlus(this);
        this.hornRightTip2.setDefaultRotPoint(-5.0F, 0.0F, 0.0F);
        this.hornRight2.addChild(this.hornRightTip2);
        this.setRotationAngle(this.hornRightTip2, 0.0F, 0.0F, 1.4114F);
        this.hornRightTip2.setTextureOffset(42, 140).addCuboid(-4.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);

        this.hornLeft2 = new ModelRendererPlus(this);
        this.hornLeft2.setDefaultRotPoint(3.0F, -3.0F, -4.0F);
        this.head2.addChild(this.hornLeft2);
        this.setRotationAngle(this.hornLeft2, 0.0F, 0.0F, -0.4363F);
        this.hornLeft2.setTextureOffset(42, 140).addCuboid(0.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);

        this.hornLeftTip2 = new ModelRendererPlus(this);
        this.hornLeftTip2.setDefaultRotPoint(5.0F, 0.0F, 0.0F);
        this.hornLeft2.addChild(this.hornLeftTip2);
        this.setRotationAngle(this.hornLeftTip2, 0.0F, 0.0F, -1.4114F);
        this.hornLeftTip2.setTextureOffset(42, 140).addCuboid(0.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);

        this.lead2 = new ModelRendererPlus(this);
        this.lead2.setDefaultRotPoint(0.0F, -5.7F, -0.5F);
        this.head2.addChild(this.lead2);
        this.lead2.setTextureOffset(0, 173).addCuboid(-4.0F, 0.0F, -0.5F, 8.0F, 0.0F, 1.0F, 0.0F, false);

        this.mouth2 = new ModelRendererPlus(this);
        this.mouth2.setDefaultRotPoint(0.0F, 1.0F, -7.0F);
        this.head2.addChild(this.mouth2);
        this.setRotationAngle(this.mouth2, 0.4363F, 0.0F, 0.0F);
        this.mouth2.setTextureOffset(42, 128).addCuboid(-2.5F, -2.5F, -5.0F, 5.0F, 5.0F, 5.0F, 0.0F, false);

        this.leadFront5 = new ModelRendererPlus(this);
        this.leadFront5.setDefaultRotPoint(2.5F, 1.0F, -3.0F);
        this.mouth2.addChild(this.leadFront5);
        this.setRotationAngle(this.leadFront5, 0.0F, 0.7854F, 0.0F);
        this.leadFront5.setTextureOffset(0, 167).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 6.0F, 0.0F, false);

        this.leadFront6 = new ModelRendererPlus(this);
        this.leadFront6.setDefaultRotPoint(0.0F, 0.0F, 6.0F);
        this.leadFront5.addChild(this.leadFront6);
        this.setRotationAngle(this.leadFront6, 0.0F, -1.2217F, -0.5236F);
        this.leadFront6.setTextureOffset(0, 164).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 9.0F, 0.0F, false);

        this.leadFront7 = new ModelRendererPlus(this);
        this.leadFront7.setDefaultRotPoint(-2.5F, 1.0F, -3.0F);
        this.mouth2.addChild(this.leadFront7);
        this.setRotationAngle(this.leadFront7, 0.0F, -0.7854F, 0.0F);
        this.leadFront7.setTextureOffset(0, 167).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 6.0F, 0.0F, false);

        this.leadFront8 = new ModelRendererPlus(this);
        this.leadFront8.setDefaultRotPoint(0.0F, 0.0F, 6.0F);
        this.leadFront7.addChild(this.leadFront8);
        this.setRotationAngle(this.leadFront8, 0.0F, 1.2217F, 0.5236F);
        this.leadFront8.setTextureOffset(0, 164).addCuboid(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 9.0F, 0.0F, false);

        this.centerBeam = new ModelRendererPlus(this);
        this.centerBeam.setDefaultRotPoint(0.0F, 8.0F, -4.0F);
        this.centerBeam.setTextureOffset(0, 95).addCuboid(-1.5F, -1.5F, -30.0F, 3.0F, 3.0F, 30.0F, 0.0F, false);
        this.centerBeam.setTextureOffset(0, 106).addCuboid(-2.0F, -2.0F, -28.5F, 4.0F, 4.0F, 1.0F, 0.0F, false);
        this.centerBeam.setTextureOffset(0, 106).addCuboid(-2.0F, -2.0F, -30.5F, 4.0F, 4.0F, 1.0F, 0.0F, false);
        this.centerBeam.setTextureOffset(63, 120).addCuboid(-15.0F, -1.0F, -7.5F, 30.0F, 2.0F, 3.0F, 0.0F, false);

        this.backBeam = new ModelRendererPlus(this);
        this.backBeam.setDefaultRotPoint(0.0F, 1.0F, -2.0F);
        this.centerBeam.addChild(this.backBeam);
        this.setRotationAngle(this.backBeam, -0.1396F, 0.0F, 0.0F);
        this.backBeam.setTextureOffset(99, 106).addCuboid(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 12.0F, 0.0F, false);

        this.backBeamJoint = new ModelRendererPlus(this);
        this.backBeamJoint.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.backBeam.addChild(this.backBeamJoint);
        this.setRotationAngle(this.backBeamJoint, 0.0873F, 0.0F, 0.0F);


        this.chariotFloor = new ModelRendererPlus(this);
        this.chariotFloor.setDefaultRotPoint(0.0F, -1.0F, 7.0F);
        this.backBeamJoint.addChild(this.chariotFloor);
        this.setRotationAngle(this.chariotFloor, 0.0F, 0.7854F, 0.0F);
        this.chariotFloor.setTextureOffset(0, 77).addCuboid(-14.0F, -0.5F, 0.0F, 14.0F, 2.0F, 14.0F, 0.0F, false);
        this.chariotFloor.setTextureOffset(0, 111).addCuboid(-1.0F, -11.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.0F, false);
        this.chariotFloor.setTextureOffset(0, 43).addCuboid(-14.0F, -7.5F, -0.5F, 14.0F, 8.0F, 1.0F, 0.0F, false);
        this.chariotFloor.setTextureOffset(100, 62).addCuboid(-14.0F, -10.5F, 0.0F, 14.0F, 3.0F, 0.0F, 0.0F, false);
        this.chariotFloor.setTextureOffset(93, 65).addCuboid(-14.0F, -6.0F, -2.0F, 16.0F, 1.0F, 2.0F, 0.0F, false);

        this.chariotFront2 = new ModelRendererPlus(this);
        this.chariotFront2.setDefaultRotPoint(0.0F, -3.5F, 0.0F);
        this.chariotFloor.addChild(this.chariotFront2);
        this.setRotationAngle(this.chariotFront2, 0.0F, -1.5708F, 0.0F);
        this.chariotFront2.setTextureOffset(0, 52).addCuboid(0.0F, -4.0F, -0.5F, 14.0F, 8.0F, 1.0F, 0.0F, false);
        this.chariotFront2.setTextureOffset(100, 62).addCuboid(0.0F, -7.0F, 0.0F, 14.0F, 3.0F, 0.0F, 0.0F, false);

        this.chariotDecor2Stick = new ModelRendererPlus(this);
        this.chariotDecor2Stick.setDefaultRotPoint(1.0F, -2.0F, -1.0F);
        this.chariotFront2.addChild(this.chariotDecor2Stick);
        this.setRotationAngle(this.chariotDecor2Stick, -0.2618F, 0.0F, 0.0F);
        this.chariotDecor2Stick.setTextureOffset(93, 65).addCuboid(-1.0F, -0.5F, -1.0F, 16.0F, 1.0F, 2.0F, 0.0F, false);

        this.chariotFloorJoint = new ModelRendererPlus(this);
        this.chariotFloorJoint.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.chariotFloor.addChild(this.chariotFloorJoint);
        this.setRotationAngle(this.chariotFloorJoint, 0.0F, -0.7854F, 0.0F);
        this.chariotFloorJoint.setTextureOffset(0, 61).addCuboid(-10.0F, -0.5F, 10.5F, 20.0F, 2.0F, 14.0F, 0.0F, false);
        this.chariotFloorJoint.setTextureOffset(32, 20).addCuboid(9.5F, -7.5F, 10.0F, 1.0F, 8.0F, 15.0F, 0.0F, false);
        this.chariotFloorJoint.setTextureOffset(0, 20).addCuboid(-10.5F, -7.5F, 10.0F, 1.0F, 8.0F, 15.0F, 0.0F, false);

        this.chariotWall1Decor = new ModelRendererPlus(this);
        this.chariotWall1Decor.setDefaultRotPoint(-10.0F, -9.0F, 9.8F);
        this.chariotFloorJoint.addChild(this.chariotWall1Decor);
        this.setRotationAngle(this.chariotWall1Decor, 0.0F, 1.5708F, 0.0F);
        this.chariotWall1Decor.setTextureOffset(96, 57).addCuboid(-16.0F, -1.5F, 0.0F, 16.0F, 5.0F, 0.0F, 0.0F, false);

        this.chariotWall2Decor = new ModelRendererPlus(this);
        this.chariotWall2Decor.setDefaultRotPoint(10.0F, -9.0F, 9.8F);
        this.chariotFloorJoint.addChild(this.chariotWall2Decor);
        this.setRotationAngle(this.chariotWall2Decor, 0.0F, 1.5708F, 0.0F);
        this.chariotWall2Decor.setTextureOffset(96, 57).addCuboid(-16.0F, -1.5F, 0.0F, 16.0F, 5.0F, 0.0F, 0.0F, false);

        this.chariotFloor3 = new ModelRendererPlus(this);
        this.chariotFloor3.setDefaultRotPoint(0.0F, 0.5F, 24.5F);
        this.chariotFloorJoint.addChild(this.chariotFloor3);
        this.setRotationAngle(this.chariotFloor3, -0.1571F, 0.0F, 0.0F);
        this.chariotFloor3.setTextureOffset(2, 61).addCuboid(-9.0F, -1.0F, 0.0F, 18.0F, 2.0F, 14.0F, 0.0F, false);
        this.chariotFloor3.setTextureOffset(69, 53).addCuboid(9.0F, -6.0F, -1.0F, 1.0F, 6.0F, 15.0F, 0.0F, false);
        this.chariotFloor3.setTextureOffset(69, 74).addCuboid(-10.0F, -6.0F, -1.0F, 1.0F, 6.0F, 15.0F, 0.0F, false);

        this.chariotWall3Decor = new ModelRendererPlus(this);
        this.chariotWall3Decor.setDefaultRotPoint(9.5F, -7.5F, 0.0F);
        this.chariotFloor3.addChild(this.chariotWall3Decor);
        this.setRotationAngle(this.chariotWall3Decor, 0.0F, 1.5708F, 0.0F);
        this.chariotWall3Decor.setTextureOffset(100, 62).addCuboid(-14.0F, -1.5F, 0.0F, 14.0F, 3.0F, 0.0F, 0.0F, false);

        this.chariotWall4Decor = new ModelRendererPlus(this);
        this.chariotWall4Decor.setDefaultRotPoint(-9.5F, -7.5F, 0.0F);
        this.chariotFloor3.addChild(this.chariotWall4Decor);
        this.setRotationAngle(this.chariotWall4Decor, 0.0F, 1.5708F, 0.0F);
        this.chariotWall4Decor.setTextureOffset(100, 62).addCuboid(-14.0F, -1.5F, 0.0F, 14.0F, 3.0F, 0.0F, 0.0F, false);

        this.chariotCenter = new ModelRendererPlus(this);
        this.chariotCenter.setDefaultRotPoint(0.0F, 0.0F, 17.0F);
        this.backBeam.addChild(this.chariotCenter);
        this.setRotationAngle(this.chariotCenter, 0.1745F, 0.0F, 0.0F);
        this.chariotCenter.setTextureOffset(37, 105).addCuboid(-3.5F, -1.5F, -6.0F, 7.0F, 3.0F, 12.0F, 0.0F, false);

        this.frontAxel = new ModelRendererPlus(this);
        this.frontAxel.setDefaultRotPoint(0.0F, 2.0F, 0.0F);
        this.chariotCenter.addChild(this.frontAxel);
        this.setRotationAngle(this.frontAxel, 3.1416F, 0.0F, 0.0F);
        this.frontAxel.setTextureOffset(51, 99).addCuboid(-18.0F, -1.5F, -1.5F, 36.0F, 3.0F, 3.0F, 0.0F, false);

        this.leftWheelWeaponMain = new ModelRendererPlus(this);
        this.leftWheelWeaponMain.setDefaultRotPoint(18.0F, 0.0F, 0.0F);
        this.frontAxel.addChild(this.leftWheelWeaponMain);
        this.setRotationAngle(this.leftWheelWeaponMain, 3.1416F, 0.0F, -3.1416F);
        this.leftWheelWeaponMain.setTextureOffset(108, 46).addCuboid(-1.0F, -1.4F, -8.0F, 2.0F, 3.0F, 8.0F, 0.0F, false);

        this.leftWheelWeaponHead = new ModelRendererPlus(this);
        this.leftWheelWeaponHead.setDefaultRotPoint(-1.0F, 0.0F, -6.5F);
        this.leftWheelWeaponMain.addChild(this.leftWheelWeaponHead);
        this.leftWheelWeaponHead.setTextureOffset(82, 26).addCuboid(-0.5F, -5.5F, -11.0F, 1.0F, 11.0F, 11.0F, 0.0F, false);
        this.leftWheelWeaponHead.setTextureOffset(64, 32).addCuboid(-0.5F, -2.5F, 0.0F, 1.0F, 5.0F, 16.0F, 0.0F, false);

        this.leftWheelWeaponBlade = new ModelRendererPlus(this);
        this.leftWheelWeaponBlade.setDefaultRotPoint(0.0F, 0.0F, -11.0F);
        this.leftWheelWeaponHead.addChild(this.leftWheelWeaponBlade);
        this.leftWheelWeaponBlade.setTextureOffset(49, 0).addCuboid(-7.0F, -0.5F, 0.0F, 7.0F, 1.0F, 24.0F, 0.0F, false);
        this.leftWheelWeaponBlade.setTextureOffset(0, 0).addCuboid(-6.0F, -0.5F, -1.0F, 5.0F, 1.0F, 1.0F, 0.0F, false);
        this.leftWheelWeaponBlade.setTextureOffset(0, 0).addCuboid(-7.0F, -0.5F, -2.0F, 5.0F, 1.0F, 1.0F, 0.0F, false);
        this.leftWheelWeaponBlade.setTextureOffset(0, 0).addCuboid(-7.0F, -0.5F, -3.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        this.leftWheelWeaponBlade.setTextureOffset(0, 0).addCuboid(-7.0F, -0.5F, -4.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        this.leftWheelWeaponBlade.setTextureOffset(0, 0).addCuboid(-7.0F, -0.5F, -5.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        this.leftWheelWeaponBlade.setTextureOffset(0, 0).addCuboid(-7.0F, -0.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        this.rightWheelWeaponMain = new ModelRendererPlus(this);
        this.rightWheelWeaponMain.setDefaultRotPoint(-18.0F, 0.0F, 0.0F);
        this.frontAxel.addChild(this.rightWheelWeaponMain);
        this.setRotationAngle(this.rightWheelWeaponMain, -3.1416F, 0.0F, 0.0F);
        this.rightWheelWeaponMain.setTextureOffset(108, 46).addCuboid(-1.0F, -1.6F, -8.0F, 2.0F, 3.0F, 8.0F, 0.0F, false);

        this.rightWheelWeaponHead = new ModelRendererPlus(this);
        this.rightWheelWeaponHead.setDefaultRotPoint(-1.0F, 0.0F, -6.5F);
        this.rightWheelWeaponMain.addChild(this.rightWheelWeaponHead);
        this.rightWheelWeaponHead.setTextureOffset(82, 26).addCuboid(-0.5F, -5.5F, -11.0F, 1.0F, 11.0F, 11.0F, 0.0F, false);
        this.rightWheelWeaponHead.setTextureOffset(64, 32).addCuboid(-0.5F, -2.5F, 0.0F, 1.0F, 5.0F, 16.0F, 0.0F, false);

        this.rightWheelWeaponBlade = new ModelRendererPlus(this);
        this.rightWheelWeaponBlade.setDefaultRotPoint(0.0F, 0.0F, -11.0F);
        this.rightWheelWeaponHead.addChild(this.rightWheelWeaponBlade);
        this.rightWheelWeaponBlade.setTextureOffset(49, 0).addCuboid(-7.0F, -0.5F, 0.0F, 7.0F, 1.0F, 24.0F, 0.0F, false);
        this.rightWheelWeaponBlade.setTextureOffset(0, 0).addCuboid(-6.0F, -0.5F, -1.0F, 5.0F, 1.0F, 1.0F, 0.0F, false);
        this.rightWheelWeaponBlade.setTextureOffset(0, 0).addCuboid(-7.0F, -0.5F, -2.0F, 5.0F, 1.0F, 1.0F, 0.0F, false);
        this.rightWheelWeaponBlade.setTextureOffset(0, 0).addCuboid(-7.0F, -0.5F, -3.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        this.rightWheelWeaponBlade.setTextureOffset(0, 0).addCuboid(-7.0F, -0.5F, -4.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        this.rightWheelWeaponBlade.setTextureOffset(0, 0).addCuboid(-7.0F, -0.5F, -5.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        this.rightWheelWeaponBlade.setTextureOffset(0, 0).addCuboid(-7.0F, -0.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        this.frontLeftWheelCenter = new ModelRendererPlus(this);
        this.frontLeftWheelCenter.setDefaultRotPoint(14.0F, 0.0F, 0.0F);
        this.frontAxel.addChild(this.frontLeftWheelCenter);
        this.setRotationAngle(this.frontLeftWheelCenter, 0.7854F, 0.0F, 0.0F);
        this.frontLeftWheelCenter.setTextureOffset(0, 27).addCuboid(-1.0F, -2.0F, -2.0F, 2.0F, 4.0F, 4.0F, 0.0F, false);

        this.frontLeftWheelRod1 = new ModelRendererPlus(this);
        this.frontLeftWheelRod1.setDefaultRotPoint(0.0F, 1.5F, -1.5F);
        this.frontLeftWheelCenter.addChild(this.frontLeftWheelRod1);
        this.setRotationAngle(this.frontLeftWheelRod1, 2.3562F, 0.0F, 0.0F);
        this.frontLeftWheelRod1.setTextureOffset(0, 18).addCuboid(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.frontLeftWheelFrame1 = new ModelRendererPlus(this);
        this.frontLeftWheelFrame1.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
        this.frontLeftWheelRod1.addChild(this.frontLeftWheelFrame1);
        this.setRotationAngle(this.frontLeftWheelFrame1, -0.4189F, 0.0F, 0.0F);
        this.frontLeftWheelFrame1.setTextureOffset(17, 26).addCuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 8.0F, 0.0F, false);

        this.frontLeftWheelRod2 = new ModelRendererPlus(this);
        this.frontLeftWheelRod2.setDefaultRotPoint(0.0F, 1.5F, 1.5F);
        this.frontLeftWheelCenter.addChild(this.frontLeftWheelRod2);
        this.setRotationAngle(this.frontLeftWheelRod2, 0.7854F, 0.0F, 0.0F);
        this.frontLeftWheelRod2.setTextureOffset(0, 18).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.frontLeftWheelFrame2 = new ModelRendererPlus(this);
        this.frontLeftWheelFrame2.setDefaultRotPoint(0.0F, 8.0F, 0.0F);
        this.frontLeftWheelRod2.addChild(this.frontLeftWheelFrame2);
        this.setRotationAngle(this.frontLeftWheelFrame2, -0.4189F, 0.0F, 0.0F);
        this.frontLeftWheelFrame2.setTextureOffset(17, 26).addCuboid(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 8.0F, 0.0F, false);

        this.frontLeftWheelRod3 = new ModelRendererPlus(this);
        this.frontLeftWheelRod3.setDefaultRotPoint(0.0F, -2.0F, 0.0F);
        this.frontLeftWheelCenter.addChild(this.frontLeftWheelRod3);
        this.frontLeftWheelRod3.setTextureOffset(0, 18).addCuboid(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.frontLeftWheelFrame3 = new ModelRendererPlus(this);
        this.frontLeftWheelFrame3.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
        this.frontLeftWheelRod3.addChild(this.frontLeftWheelFrame3);
        this.setRotationAngle(this.frontLeftWheelFrame3, -0.4189F, 0.0F, 0.0F);
        this.frontLeftWheelFrame3.setTextureOffset(17, 26).addCuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 8.0F, 0.0F, false);

        this.frontLeftWheelRod4 = new ModelRendererPlus(this);
        this.frontLeftWheelRod4.setDefaultRotPoint(0.0F, 0.0F, 2.0F);
        this.frontLeftWheelCenter.addChild(this.frontLeftWheelRod4);
        this.setRotationAngle(this.frontLeftWheelRod4, -1.5708F, 0.0F, 0.0F);
        this.frontLeftWheelRod4.setTextureOffset(0, 18).addCuboid(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.frontLeftWheelFrame4 = new ModelRendererPlus(this);
        this.frontLeftWheelFrame4.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
        this.frontLeftWheelRod4.addChild(this.frontLeftWheelFrame4);
        this.setRotationAngle(this.frontLeftWheelFrame4, -0.4014F, 0.0F, 0.0F);
        this.frontLeftWheelFrame4.setTextureOffset(17, 26).addCuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 8.0F, 0.0F, false);

        this.frontLeftWheelRod5 = new ModelRendererPlus(this);
        this.frontLeftWheelRod5.setDefaultRotPoint(0.0F, -1.5F, 1.5F);
        this.frontLeftWheelCenter.addChild(this.frontLeftWheelRod5);
        this.setRotationAngle(this.frontLeftWheelRod5, 2.3562F, 0.0F, 0.0F);
        this.frontLeftWheelRod5.setTextureOffset(0, 18).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.frontLeftWheelFrame5 = new ModelRendererPlus(this);
        this.frontLeftWheelFrame5.setDefaultRotPoint(0.0F, 7.8F, 0.0F);
        this.frontLeftWheelRod5.addChild(this.frontLeftWheelFrame5);
        this.setRotationAngle(this.frontLeftWheelFrame5, -0.4014F, 0.0F, 0.0F);
        this.frontLeftWheelFrame5.setTextureOffset(17, 26).addCuboid(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 8.0F, 0.0F, false);

        this.frontLeftWheelRod6 = new ModelRendererPlus(this);
        this.frontLeftWheelRod6.setDefaultRotPoint(0.0F, 0.0F, -2.0F);
        this.frontLeftWheelCenter.addChild(this.frontLeftWheelRod6);
        this.setRotationAngle(this.frontLeftWheelRod6, -1.5708F, 0.0F, 0.0F);
        this.frontLeftWheelRod6.setTextureOffset(0, 18).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.frontLeftWheelFrame6 = new ModelRendererPlus(this);
        this.frontLeftWheelFrame6.setDefaultRotPoint(0.0F, 8.0F, 0.0F);
        this.frontLeftWheelRod6.addChild(this.frontLeftWheelFrame6);
        this.setRotationAngle(this.frontLeftWheelFrame6, -0.384F, 0.0F, 0.0F);
        this.frontLeftWheelFrame6.setTextureOffset(17, 26).addCuboid(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 8.0F, 0.0F, false);

        this.frontLeftWheelRod7 = new ModelRendererPlus(this);
        this.frontLeftWheelRod7.setDefaultRotPoint(0.0F, -1.5F, -1.5F);
        this.frontLeftWheelCenter.addChild(this.frontLeftWheelRod7);
        this.setRotationAngle(this.frontLeftWheelRod7, 0.7854F, 0.0F, 0.0F);
        this.frontLeftWheelRod7.setTextureOffset(0, 18).addCuboid(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.frontLeftWheelFrame7 = new ModelRendererPlus(this);
        this.frontLeftWheelFrame7.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
        this.frontLeftWheelRod7.addChild(this.frontLeftWheelFrame7);
        this.setRotationAngle(this.frontLeftWheelFrame7, -0.4189F, 0.0F, 0.0F);
        this.frontLeftWheelFrame7.setTextureOffset(17, 26).addCuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 8.0F, 0.0F, false);

        this.frontLeftWheelRod8 = new ModelRendererPlus(this);
        this.frontLeftWheelRod8.setDefaultRotPoint(0.0F, 2.0F, 0.0F);
        this.frontLeftWheelCenter.addChild(this.frontLeftWheelRod8);
        this.frontLeftWheelRod8.setTextureOffset(0, 18).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.frontLeftWheelFrame8 = new ModelRendererPlus(this);
        this.frontLeftWheelFrame8.setDefaultRotPoint(0.0F, 8.0F, 0.0F);
        this.frontLeftWheelRod8.addChild(this.frontLeftWheelFrame8);
        this.setRotationAngle(this.frontLeftWheelFrame8, -0.4014F, 0.0F, 0.0F);
        this.frontLeftWheelFrame8.setTextureOffset(17, 26).addCuboid(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 8.0F, 0.0F, false);

        this.frontRightWheelCenter = new ModelRendererPlus(this);
        this.frontRightWheelCenter.setDefaultRotPoint(-14.0F, 0.0F, 0.0F);
        this.frontAxel.addChild(this.frontRightWheelCenter);
        this.setRotationAngle(this.frontRightWheelCenter, 0.7854F, 0.0F, 0.0F);
        this.frontRightWheelCenter.setTextureOffset(0, 27).addCuboid(-1.0F, -2.0F, -2.0F, 2.0F, 4.0F, 4.0F, 0.0F, false);

        this.frontRightWheelRod1 = new ModelRendererPlus(this);
        this.frontRightWheelRod1.setDefaultRotPoint(0.0F, -2.0F, 0.0F);
        this.frontRightWheelCenter.addChild(this.frontRightWheelRod1);
        this.frontRightWheelRod1.setTextureOffset(0, 18).addCuboid(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.frontRightWheelFrame1 = new ModelRendererPlus(this);
        this.frontRightWheelFrame1.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
        this.frontRightWheelRod1.addChild(this.frontRightWheelFrame1);
        this.setRotationAngle(this.frontRightWheelFrame1, -0.4189F, 0.0F, 0.0F);
        this.frontRightWheelFrame1.setTextureOffset(17, 26).addCuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 8.0F, 0.0F, false);

        this.frontRightWheelRod2 = new ModelRendererPlus(this);
        this.frontRightWheelRod2.setDefaultRotPoint(0.0F, 1.5F, 1.5F);
        this.frontRightWheelCenter.addChild(this.frontRightWheelRod2);
        this.setRotationAngle(this.frontRightWheelRod2, 0.7854F, 0.0F, 0.0F);
        this.frontRightWheelRod2.setTextureOffset(0, 18).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.frontRightWheelFrame2 = new ModelRendererPlus(this);
        this.frontRightWheelFrame2.setDefaultRotPoint(0.0F, 8.0F, 0.0F);
        this.frontRightWheelRod2.addChild(this.frontRightWheelFrame2);
        this.setRotationAngle(this.frontRightWheelFrame2, -0.4189F, 0.0F, 0.0F);
        this.frontRightWheelFrame2.setTextureOffset(17, 26).addCuboid(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 8.0F, 0.0F, false);

        this.frontRightWheelRod3 = new ModelRendererPlus(this);
        this.frontRightWheelRod3.setDefaultRotPoint(0.0F, -1.5F, -1.5F);
        this.frontRightWheelCenter.addChild(this.frontRightWheelRod3);
        this.setRotationAngle(this.frontRightWheelRod3, 0.7854F, 0.0F, 0.0F);
        this.frontRightWheelRod3.setTextureOffset(0, 18).addCuboid(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.frontRightWheelFrame3 = new ModelRendererPlus(this);
        this.frontRightWheelFrame3.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
        this.frontRightWheelRod3.addChild(this.frontRightWheelFrame3);
        this.setRotationAngle(this.frontRightWheelFrame3, -0.4189F, 0.0F, 0.0F);
        this.frontRightWheelFrame3.setTextureOffset(17, 26).addCuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 8.0F, 0.0F, false);

        this.frontRightWheelRod4 = new ModelRendererPlus(this);
        this.frontRightWheelRod4.setDefaultRotPoint(0.0F, -1.5F, 1.5F);
        this.frontRightWheelCenter.addChild(this.frontRightWheelRod4);
        this.setRotationAngle(this.frontRightWheelRod4, 2.3562F, 0.0F, 0.0F);
        this.frontRightWheelRod4.setTextureOffset(0, 18).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.frontRightWheelFrame4 = new ModelRendererPlus(this);
        this.frontRightWheelFrame4.setDefaultRotPoint(0.0F, 7.8F, 0.0F);
        this.frontRightWheelRod4.addChild(this.frontRightWheelFrame4);
        this.setRotationAngle(this.frontRightWheelFrame4, -0.4014F, 0.0F, 0.0F);
        this.frontRightWheelFrame4.setTextureOffset(17, 26).addCuboid(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 8.0F, 0.0F, false);

        this.frontRightWheelRod5 = new ModelRendererPlus(this);
        this.frontRightWheelRod5.setDefaultRotPoint(0.0F, 1.5F, -1.5F);
        this.frontRightWheelCenter.addChild(this.frontRightWheelRod5);
        this.setRotationAngle(this.frontRightWheelRod5, 2.3562F, 0.0F, 0.0F);
        this.frontRightWheelRod5.setTextureOffset(0, 18).addCuboid(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.frontRightWheelFrame5 = new ModelRendererPlus(this);
        this.frontRightWheelFrame5.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
        this.frontRightWheelRod5.addChild(this.frontRightWheelFrame5);
        this.setRotationAngle(this.frontRightWheelFrame5, -0.4189F, 0.0F, 0.0F);
        this.frontRightWheelFrame5.setTextureOffset(17, 26).addCuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 8.0F, 0.0F, false);

        this.frontRightWheelRod6 = new ModelRendererPlus(this);
        this.frontRightWheelRod6.setDefaultRotPoint(0.0F, 0.0F, 2.0F);
        this.frontRightWheelCenter.addChild(this.frontRightWheelRod6);
        this.setRotationAngle(this.frontRightWheelRod6, -1.5708F, 0.0F, 0.0F);
        this.frontRightWheelRod6.setTextureOffset(0, 18).addCuboid(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.frontRightWheelFrame6 = new ModelRendererPlus(this);
        this.frontRightWheelFrame6.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
        this.frontRightWheelRod6.addChild(this.frontRightWheelFrame6);
        this.setRotationAngle(this.frontRightWheelFrame6, -0.4014F, 0.0F, 0.0F);
        this.frontRightWheelFrame6.setTextureOffset(17, 26).addCuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 8.0F, 0.0F, false);

        this.frontRightWheelRod7 = new ModelRendererPlus(this);
        this.frontRightWheelRod7.setDefaultRotPoint(0.0F, 2.0F, 0.0F);
        this.frontRightWheelCenter.addChild(this.frontRightWheelRod7);
        this.frontRightWheelRod7.setTextureOffset(0, 18).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.frontRightWheelFrame7 = new ModelRendererPlus(this);
        this.frontRightWheelFrame7.setDefaultRotPoint(0.0F, 8.0F, 0.0F);
        this.frontRightWheelRod7.addChild(this.frontRightWheelFrame7);
        this.setRotationAngle(this.frontRightWheelFrame7, -0.4014F, 0.0F, 0.0F);
        this.frontRightWheelFrame7.setTextureOffset(17, 26).addCuboid(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 8.0F, 0.0F, false);

        this.frontRightWheelRod8 = new ModelRendererPlus(this);
        this.frontRightWheelRod8.setDefaultRotPoint(0.0F, 0.0F, -2.0F);
        this.frontRightWheelCenter.addChild(this.frontRightWheelRod8);
        this.setRotationAngle(this.frontRightWheelRod8, -1.5708F, 0.0F, 0.0F);
        this.frontRightWheelRod8.setTextureOffset(0, 18).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.frontRightWheelFrame8 = new ModelRendererPlus(this);
        this.frontRightWheelFrame8.setDefaultRotPoint(0.0F, 8.0F, 0.0F);
        this.frontRightWheelRod8.addChild(this.frontRightWheelFrame8);
        this.setRotationAngle(this.frontRightWheelFrame8, -0.384F, 0.0F, 0.0F);
        this.frontRightWheelFrame8.setTextureOffset(17, 26).addCuboid(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 8.0F, 0.0F, false);

        this.chariotBackBeam = new ModelRendererPlus(this);
        this.chariotBackBeam.setDefaultRotPoint(0.0F, 1.0F, 6.0F);
        this.chariotCenter.addChild(this.chariotBackBeam);
        this.setRotationAngle(this.chariotBackBeam, -0.1745F, 0.0F, 0.0F);
        this.chariotBackBeam.setTextureOffset(79, 73).addCuboid(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 22.0F, 0.0F, false);

        this.backAxel = new ModelRendererPlus(this);
        this.backAxel.setDefaultRotPoint(0.0F, 1.5F, 17.0F);
        this.chariotBackBeam.addChild(this.backAxel);
        this.setRotationAngle(this.backAxel, 3.1416F, 0.0F, 0.0F);
        this.backAxel.setTextureOffset(0, 93).addCuboid(-13.0F, -0.5F, -0.5F, 26.0F, 1.0F, 1.0F, 0.0F, false);

        this.backLeftWheelCenter = new ModelRendererPlus(this);
        this.backLeftWheelCenter.setDefaultRotPoint(11.5F, 0.0F, 0.0F);
        this.backAxel.addChild(this.backLeftWheelCenter);
        this.setRotationAngle(this.backLeftWheelCenter, 0.1745F, 0.0F, 0.0F);
        this.backLeftWheelCenter.setTextureOffset(4, 23).addCuboid(-0.4F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        this.backLeftWheelRod1 = new ModelRendererPlus(this);
        this.backLeftWheelRod1.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.backLeftWheelCenter.addChild(this.backLeftWheelRod1);
        this.backLeftWheelRod1.setTextureOffset(0, 18).addCuboid(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.backLeftWheelFrame1 = new ModelRendererPlus(this);
        this.backLeftWheelFrame1.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
        this.backLeftWheelRod1.addChild(this.backLeftWheelFrame1);
        this.setRotationAngle(this.backLeftWheelFrame1, -0.4189F, 0.0F, 0.0F);
        this.backLeftWheelFrame1.setTextureOffset(19, 28).addCuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);

        this.backLeftWheelRod2 = new ModelRendererPlus(this);
        this.backLeftWheelRod2.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.backLeftWheelCenter.addChild(this.backLeftWheelRod2);
        this.setRotationAngle(this.backLeftWheelRod2, -1.5708F, 0.0F, 0.0F);
        this.backLeftWheelRod2.setTextureOffset(0, 18).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.backLeftWheelFrame2 = new ModelRendererPlus(this);
        this.backLeftWheelFrame2.setDefaultRotPoint(0.0F, 8.0F, 0.0F);
        this.backLeftWheelRod2.addChild(this.backLeftWheelFrame2);
        this.setRotationAngle(this.backLeftWheelFrame2, -0.384F, 0.0F, 0.0F);
        this.backLeftWheelFrame2.setTextureOffset(19, 28).addCuboid(-1.0F, -0.5F, -6.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);

        this.backLeftWheelRod3 = new ModelRendererPlus(this);
        this.backLeftWheelRod3.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.backLeftWheelCenter.addChild(this.backLeftWheelRod3);
        this.setRotationAngle(this.backLeftWheelRod3, -2.3562F, 0.0F, 0.0F);
        this.backLeftWheelRod3.setTextureOffset(0, 18).addCuboid(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.backLeftWheelFrame3 = new ModelRendererPlus(this);
        this.backLeftWheelFrame3.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
        this.backLeftWheelRod3.addChild(this.backLeftWheelFrame3);
        this.setRotationAngle(this.backLeftWheelFrame3, -0.4189F, 0.0F, 0.0F);
        this.backLeftWheelFrame3.setTextureOffset(19, 28).addCuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);

        this.backLeftWheelRod4 = new ModelRendererPlus(this);
        this.backLeftWheelRod4.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.backLeftWheelCenter.addChild(this.backLeftWheelRod4);
        this.setRotationAngle(this.backLeftWheelRod4, 2.3562F, 0.0F, 0.0F);
        this.backLeftWheelRod4.setTextureOffset(0, 18).addCuboid(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.backLeftWheelFrame4 = new ModelRendererPlus(this);
        this.backLeftWheelFrame4.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
        this.backLeftWheelRod4.addChild(this.backLeftWheelFrame4);
        this.setRotationAngle(this.backLeftWheelFrame4, -0.4189F, 0.0F, 0.0F);
        this.backLeftWheelFrame4.setTextureOffset(19, 28).addCuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);

        this.backLeftWheelRod5 = new ModelRendererPlus(this);
        this.backLeftWheelRod5.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.backLeftWheelCenter.addChild(this.backLeftWheelRod5);
        this.setRotationAngle(this.backLeftWheelRod5, 1.5708F, 0.0F, 0.0F);
        this.backLeftWheelRod5.setTextureOffset(0, 18).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.backLeftWheelFrame5 = new ModelRendererPlus(this);
        this.backLeftWheelFrame5.setDefaultRotPoint(0.0F, 8.0F, 0.0F);
        this.backLeftWheelRod5.addChild(this.backLeftWheelFrame5);
        this.setRotationAngle(this.backLeftWheelFrame5, -0.384F, 0.0F, 0.0F);
        this.backLeftWheelFrame5.setTextureOffset(19, 28).addCuboid(-1.0F, -0.5F, -6.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);

        this.backLeftWheelRod6 = new ModelRendererPlus(this);
        this.backLeftWheelRod6.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.backLeftWheelCenter.addChild(this.backLeftWheelRod6);
        this.setRotationAngle(this.backLeftWheelRod6, 0.7854F, 0.0F, 0.0F);
        this.backLeftWheelRod6.setTextureOffset(0, 18).addCuboid(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.backLeftWheelFrame6 = new ModelRendererPlus(this);
        this.backLeftWheelFrame6.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
        this.backLeftWheelRod6.addChild(this.backLeftWheelFrame6);
        this.setRotationAngle(this.backLeftWheelFrame6, -0.4189F, 0.0F, 0.0F);
        this.backLeftWheelFrame6.setTextureOffset(19, 28).addCuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);

        this.backLeftWheelRod7 = new ModelRendererPlus(this);
        this.backLeftWheelRod7.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.backLeftWheelCenter.addChild(this.backLeftWheelRod7);
        this.setRotationAngle(this.backLeftWheelRod7, -3.1416F, 0.0F, 0.0F);
        this.backLeftWheelRod7.setTextureOffset(0, 18).addCuboid(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.backLeftWheelFrame7 = new ModelRendererPlus(this);
        this.backLeftWheelFrame7.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
        this.backLeftWheelRod7.addChild(this.backLeftWheelFrame7);
        this.setRotationAngle(this.backLeftWheelFrame7, -0.4189F, 0.0F, 0.0F);
        this.backLeftWheelFrame7.setTextureOffset(19, 28).addCuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);

        this.backLeftWheelRod8 = new ModelRendererPlus(this);
        this.backLeftWheelRod8.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.backLeftWheelCenter.addChild(this.backLeftWheelRod8);
        this.setRotationAngle(this.backLeftWheelRod8, -0.7854F, 0.0F, 0.0F);
        this.backLeftWheelRod8.setTextureOffset(0, 18).addCuboid(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.backLeftWheelFrame8 = new ModelRendererPlus(this);
        this.backLeftWheelFrame8.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
        this.backLeftWheelRod8.addChild(this.backLeftWheelFrame8);
        this.setRotationAngle(this.backLeftWheelFrame8, -0.4189F, 0.0F, 0.0F);
        this.backLeftWheelFrame8.setTextureOffset(19, 28).addCuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);

        this.backRightWheelCenter = new ModelRendererPlus(this);
        this.backRightWheelCenter.setDefaultRotPoint(-11.5F, 0.0F, 0.0F);
        this.backAxel.addChild(this.backRightWheelCenter);
        this.setRotationAngle(this.backRightWheelCenter, 0.1745F, 0.0F, 0.0F);
        this.backRightWheelCenter.setTextureOffset(4, 23).addCuboid(-0.6F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        this.backRightWheelRod1 = new ModelRendererPlus(this);
        this.backRightWheelRod1.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.backRightWheelCenter.addChild(this.backRightWheelRod1);
        this.setRotationAngle(this.backRightWheelRod1, 1.5708F, 0.0F, 0.0F);
        this.backRightWheelRod1.setTextureOffset(0, 18).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.backRightWheelFrame1 = new ModelRendererPlus(this);
        this.backRightWheelFrame1.setDefaultRotPoint(0.0F, 8.0F, 0.0F);
        this.backRightWheelRod1.addChild(this.backRightWheelFrame1);
        this.setRotationAngle(this.backRightWheelFrame1, -0.384F, 0.0F, 0.0F);
        this.backRightWheelFrame1.setTextureOffset(19, 28).addCuboid(-1.0F, -0.5F, -6.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);

        this.backRightWheelRod2 = new ModelRendererPlus(this);
        this.backRightWheelRod2.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.backRightWheelCenter.addChild(this.backRightWheelRod2);
        this.setRotationAngle(this.backRightWheelRod2, 2.3562F, 0.0F, 0.0F);
        this.backRightWheelRod2.setTextureOffset(0, 18).addCuboid(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.backRightWheelFrame2 = new ModelRendererPlus(this);
        this.backRightWheelFrame2.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
        this.backRightWheelRod2.addChild(this.backRightWheelFrame2);
        this.setRotationAngle(this.backRightWheelFrame2, -0.4189F, 0.0F, 0.0F);
        this.backRightWheelFrame2.setTextureOffset(19, 28).addCuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);

        this.backRightWheelRod3 = new ModelRendererPlus(this);
        this.backRightWheelRod3.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.backRightWheelCenter.addChild(this.backRightWheelRod3);
        this.setRotationAngle(this.backRightWheelRod3, -2.3562F, 0.0F, 0.0F);
        this.backRightWheelRod3.setTextureOffset(0, 18).addCuboid(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.backRightWheelFrame3 = new ModelRendererPlus(this);
        this.backRightWheelFrame3.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
        this.backRightWheelRod3.addChild(this.backRightWheelFrame3);
        this.setRotationAngle(this.backRightWheelFrame3, -0.4189F, 0.0F, 0.0F);
        this.backRightWheelFrame3.setTextureOffset(19, 28).addCuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);

        this.backRightWheelRod4 = new ModelRendererPlus(this);
        this.backRightWheelRod4.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.backRightWheelCenter.addChild(this.backRightWheelRod4);
        this.setRotationAngle(this.backRightWheelRod4, -3.1416F, 0.0F, 0.0F);
        this.backRightWheelRod4.setTextureOffset(0, 18).addCuboid(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.backRightWheelFrame4 = new ModelRendererPlus(this);
        this.backRightWheelFrame4.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
        this.backRightWheelRod4.addChild(this.backRightWheelFrame4);
        this.setRotationAngle(this.backRightWheelFrame4, -0.4189F, 0.0F, 0.0F);
        this.backRightWheelFrame4.setTextureOffset(19, 28).addCuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);

        this.backRightWheelRod5 = new ModelRendererPlus(this);
        this.backRightWheelRod5.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.backRightWheelCenter.addChild(this.backRightWheelRod5);
        this.backRightWheelRod5.setTextureOffset(0, 18).addCuboid(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.backRightWheelFrame5 = new ModelRendererPlus(this);
        this.backRightWheelFrame5.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
        this.backRightWheelRod5.addChild(this.backRightWheelFrame5);
        this.setRotationAngle(this.backRightWheelFrame5, -0.4189F, 0.0F, 0.0F);
        this.backRightWheelFrame5.setTextureOffset(19, 28).addCuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);

        this.backRightWheelRod6 = new ModelRendererPlus(this);
        this.backRightWheelRod6.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.backRightWheelCenter.addChild(this.backRightWheelRod6);
        this.setRotationAngle(this.backRightWheelRod6, 0.7854F, 0.0F, 0.0F);
        this.backRightWheelRod6.setTextureOffset(0, 18).addCuboid(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.backRightWheelFrame6 = new ModelRendererPlus(this);
        this.backRightWheelFrame6.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
        this.backRightWheelRod6.addChild(this.backRightWheelFrame6);
        this.setRotationAngle(this.backRightWheelFrame6, -0.4189F, 0.0F, 0.0F);
        this.backRightWheelFrame6.setTextureOffset(19, 28).addCuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);

        this.backRightWheelRod7 = new ModelRendererPlus(this);
        this.backRightWheelRod7.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.backRightWheelCenter.addChild(this.backRightWheelRod7);
        this.setRotationAngle(this.backRightWheelRod7, -0.7854F, 0.0F, 0.0F);
        this.backRightWheelRod7.setTextureOffset(0, 18).addCuboid(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.backRightWheelFrame7 = new ModelRendererPlus(this);
        this.backRightWheelFrame7.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
        this.backRightWheelRod7.addChild(this.backRightWheelFrame7);
        this.setRotationAngle(this.backRightWheelFrame7, -0.4189F, 0.0F, 0.0F);
        this.backRightWheelFrame7.setTextureOffset(19, 28).addCuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);

        this.backRightWheelRod8 = new ModelRendererPlus(this);
        this.backRightWheelRod8.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.backRightWheelCenter.addChild(this.backRightWheelRod8);
        this.setRotationAngle(this.backRightWheelRod8, -1.5708F, 0.0F, 0.0F);
        this.backRightWheelRod8.setTextureOffset(0, 18).addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.backRightWheelFrame8 = new ModelRendererPlus(this);
        this.backRightWheelFrame8.setDefaultRotPoint(0.0F, 8.0F, 0.0F);
        this.backRightWheelRod8.addChild(this.backRightWheelFrame8);
        this.setRotationAngle(this.backRightWheelFrame8, -0.384F, 0.0F, 0.0F);
        this.backRightWheelFrame8.setTextureOffset(19, 28).addCuboid(-1.0F, -0.5F, -6.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);
    }

    @Override
    public void setAngles(EntityGordiusWheel entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetModel();
        this.frontAxel.rotateAngleX = MathHelper.cos(limbSwing * 0.3F) * (float) Math.PI;
        this.backAxel.rotateAngleX = MathHelper.cos(limbSwing * 0.4f) * (float) Math.PI;
        this.leftWheelWeaponMain.rotateAngleX = this.frontAxel.rotateAngleX;
        this.rightWheelWeaponMain.rotateAngleX = -this.frontAxel.rotateAngleX;
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
        this.centerBeam.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRendererPlus model, float x, float y, float z) {
        model.setDefaultRotAngle(x, y, z);
    }

    @Override
    public void resetModel() {
        this.bull1.reset();
        this.bull2.reset();
        this.centerBeam.reset();
        this.resetChild(this.bull1);
        this.resetChild(this.bull2);
        this.resetChild(this.centerBeam);
    }
}