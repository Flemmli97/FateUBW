package io.github.flemmli97.fateubw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.minions.Gordius;
import io.github.flemmli97.tenshilib.client.AnimationManager;
import io.github.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * Gordius Wheel - Black_Saturn
 * Created using Tabula 6.0.0
 */

public class ModelGordiusWheel extends EntityModel<Gordius> implements ExtendedModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Fate.MODID, "gordius"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;
    private ModelPartHandler.ModelPartExtended frontAxel;
    private ModelPartHandler.ModelPartExtended backAxel;
    private ModelPartHandler.ModelPartExtended leftWheelWeaponMain;
    private ModelPartHandler.ModelPartExtended rightWheelWeaponMain;
    private ModelPartHandler.ModelPartExtended footLeftFront;
    private ModelPartHandler.ModelPartExtended footRightFront;
    private ModelPartHandler.ModelPartExtended footLeftFront2;
    private ModelPartHandler.ModelPartExtended footRightFront2;
    private ModelPartHandler.ModelPartExtended footLeftRear;
    private ModelPartHandler.ModelPartExtended footRightRear;
    private ModelPartHandler.ModelPartExtended footLeftRear2;
    private ModelPartHandler.ModelPartExtended footRightRear2;

    public ModelGordiusWheel(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root);
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(Fate.MODID, "gordius"));
        this.frontAxel = this.model.getPart("frontAxel");
        this.backAxel = this.model.getPart("backAxel");
        this.leftWheelWeaponMain = this.model.getPart("leftWheelWeaponMain");
        this.rightWheelWeaponMain = this.model.getPart("rightWheelWeaponMain");
        this.footLeftFront = this.model.getPart("footLeftFront");
        this.footRightFront = this.model.getPart("footRightFront");
        this.footLeftFront2 = this.model.getPart("footLeftFront2");
        this.footRightFront2 = this.model.getPart("footRightFront2");
        this.footLeftRear = this.model.getPart("footLeftRear");
        this.footRightRear = this.model.getPart("footRightRear");
        this.footLeftRear2 = this.model.getPart("footLeftRear2");
        this.footRightRear2 = this.model.getPart("footRightRear2");

    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 22.0F));

        PartDefinition bull1 = root.addOrReplaceChild("bull1", CubeListBuilder.create().texOffs(0, 145).addBox(-6.0F, -5.0F, -9.0F, 12.0F, 10.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.5F, -17.0F, -23.0F));

        PartDefinition equipmentLayer = bull1.addOrReplaceChild("equipmentLayer", CubeListBuilder.create().texOffs(59, 128).addBox(-6.5F, -6.3F, -9.5F, 13.0F, 13.0F, 19.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition footLeftFront = bull1.addOrReplaceChild("footLeftFront", CubeListBuilder.create().texOffs(0, 144).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 5.0F, -6.0F));

        PartDefinition footRightFront = bull1.addOrReplaceChild("footRightFront", CubeListBuilder.create().texOffs(0, 144).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 5.0F, -6.0F));

        PartDefinition footLeftRear = bull1.addOrReplaceChild("footLeftRear", CubeListBuilder.create().texOffs(0, 144).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 5.0F, 7.0F));

        PartDefinition footRightRear = bull1.addOrReplaceChild("footRightRear", CubeListBuilder.create().texOffs(0, 144).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 5.0F, 7.0F));

        PartDefinition head = bull1.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 128).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -9.0F));

        PartDefinition hornRight = head.addOrReplaceChild("hornRight", CubeListBuilder.create().texOffs(42, 140).addBox(-6.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -3.0F, -4.0F, 0.0F, 0.0F, 0.4363F));

        PartDefinition hornRightTip = hornRight.addOrReplaceChild("hornRightTip", CubeListBuilder.create().texOffs(42, 140).addBox(-4.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.4114F));

        PartDefinition hornLeft = head.addOrReplaceChild("hornLeft", CubeListBuilder.create().texOffs(42, 140).addBox(0.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -3.0F, -4.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition hornLeftTip = hornLeft.addOrReplaceChild("hornLeftTip", CubeListBuilder.create().texOffs(42, 140).addBox(0.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.4114F));

        PartDefinition lead = head.addOrReplaceChild("lead", CubeListBuilder.create().texOffs(0, 173).addBox(-4.0F, 0.0F, -0.5F, 8.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.7F, -0.5F));

        PartDefinition mouth = head.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(42, 128).addBox(-2.5F, -2.5F, -5.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -7.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition leadFront = mouth.addOrReplaceChild("leadFront", CubeListBuilder.create().texOffs(0, 167).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 1.0F, -3.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition leadFront2 = leadFront.addOrReplaceChild("leadFront2", CubeListBuilder.create().texOffs(0, 164).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 6.0F, 0.0F, -1.2217F, -0.5236F));

        PartDefinition leadFront3 = mouth.addOrReplaceChild("leadFront3", CubeListBuilder.create().texOffs(0, 167).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 1.0F, -3.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition leadFront4 = leadFront3.addOrReplaceChild("leadFront4", CubeListBuilder.create().texOffs(0, 164).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 6.0F, 0.0F, 1.2217F, 0.5236F));

        PartDefinition bull2 = root.addOrReplaceChild("bull2", CubeListBuilder.create().texOffs(0, 145).addBox(-6.0F, -5.0F, -9.0F, 12.0F, 10.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(8.5F, -17.0F, -23.0F));

        PartDefinition equipmentLayer2 = bull2.addOrReplaceChild("equipmentLayer2", CubeListBuilder.create().texOffs(59, 128).addBox(-6.5F, -6.3F, -9.5F, 13.0F, 13.0F, 19.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition footLeftFront2 = bull2.addOrReplaceChild("footLeftFront2", CubeListBuilder.create().texOffs(0, 144).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 5.0F, -6.0F));

        PartDefinition footRightFront2 = bull2.addOrReplaceChild("footRightFront2", CubeListBuilder.create().texOffs(0, 144).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 5.0F, -6.0F));

        PartDefinition footLeftRear2 = bull2.addOrReplaceChild("footLeftRear2", CubeListBuilder.create().texOffs(0, 144).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 5.0F, 7.0F));

        PartDefinition footRightRear2 = bull2.addOrReplaceChild("footRightRear2", CubeListBuilder.create().texOffs(0, 144).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 5.0F, 7.0F));

        PartDefinition head2 = bull2.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 128).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -9.0F));

        PartDefinition hornRight2 = head2.addOrReplaceChild("hornRight2", CubeListBuilder.create().texOffs(42, 140).addBox(-6.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -3.0F, -4.0F, 0.0F, 0.0F, 0.4363F));

        PartDefinition hornRightTip2 = hornRight2.addOrReplaceChild("hornRightTip2", CubeListBuilder.create().texOffs(42, 140).addBox(-4.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.4114F));

        PartDefinition hornLeft2 = head2.addOrReplaceChild("hornLeft2", CubeListBuilder.create().texOffs(42, 140).addBox(0.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -3.0F, -4.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition hornLeftTip2 = hornLeft2.addOrReplaceChild("hornLeftTip2", CubeListBuilder.create().texOffs(42, 140).addBox(0.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.4114F));

        PartDefinition lead2 = head2.addOrReplaceChild("lead2", CubeListBuilder.create().texOffs(0, 173).addBox(-4.0F, 0.0F, -0.5F, 8.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.7F, -0.5F));

        PartDefinition mouth2 = head2.addOrReplaceChild("mouth2", CubeListBuilder.create().texOffs(42, 128).addBox(-2.5F, -2.5F, -5.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -7.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition leadFront5 = mouth2.addOrReplaceChild("leadFront5", CubeListBuilder.create().texOffs(0, 167).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 1.0F, -3.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition leadFront6 = leadFront5.addOrReplaceChild("leadFront6", CubeListBuilder.create().texOffs(0, 164).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 6.0F, 0.0F, -1.2217F, -0.5236F));

        PartDefinition leadFront7 = mouth2.addOrReplaceChild("leadFront7", CubeListBuilder.create().texOffs(0, 167).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 1.0F, -3.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition leadFront8 = leadFront7.addOrReplaceChild("leadFront8", CubeListBuilder.create().texOffs(0, 164).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 6.0F, 0.0F, 1.2217F, 0.5236F));

        PartDefinition centerBeam = root.addOrReplaceChild("centerBeam", CubeListBuilder.create().texOffs(0, 95).addBox(-1.5F, -1.5F, -30.0F, 3.0F, 3.0F, 30.0F, new CubeDeformation(0.0F))
                .texOffs(0, 106).addBox(-2.0F, -2.0F, -28.5F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 106).addBox(-2.0F, -2.0F, -30.5F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(63, 120).addBox(-15.0F, -1.0F, -7.5F, 30.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -16.0F, -4.0F));

        PartDefinition backBeam = centerBeam.addOrReplaceChild("backBeam", CubeListBuilder.create().texOffs(99, 106).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -2.0F, -0.1396F, 0.0F, 0.0F));

        PartDefinition backBeamJoint = backBeam.addOrReplaceChild("backBeamJoint", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition chariotFloor = backBeamJoint.addOrReplaceChild("chariotFloor", CubeListBuilder.create().texOffs(0, 77).addBox(-14.0F, -0.5F, 0.0F, 14.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 111).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 43).addBox(-14.0F, -7.5F, -0.5F, 14.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(100, 62).addBox(-14.0F, -10.5F, 0.0F, 14.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(93, 65).addBox(-14.0F, -6.0F, -2.0F, 16.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 7.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition chariotFront2 = chariotFloor.addOrReplaceChild("chariotFront2", CubeListBuilder.create().texOffs(0, 52).addBox(0.0F, -4.0F, -0.5F, 14.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(100, 62).addBox(0.0F, -7.0F, 0.0F, 14.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.5F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition chariotDecor2Stick = chariotFront2.addOrReplaceChild("chariotDecor2Stick", CubeListBuilder.create().texOffs(93, 65).addBox(-1.0F, -0.5F, -1.0F, 16.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -2.0F, -1.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition chariotFloorJoint = chariotFloor.addOrReplaceChild("chariotFloorJoint", CubeListBuilder.create().texOffs(0, 61).addBox(-10.0F, -0.5F, 10.5F, 20.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(32, 20).addBox(9.5F, -7.5F, 10.0F, 1.0F, 8.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(0, 20).addBox(-10.5F, -7.5F, 10.0F, 1.0F, 8.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition chariotWall1Decor = chariotFloorJoint.addOrReplaceChild("chariotWall1Decor", CubeListBuilder.create().texOffs(96, 57).addBox(-16.0F, -1.5F, 0.0F, 16.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.0F, -9.0F, 9.8F, 0.0F, 1.5708F, 0.0F));

        PartDefinition chariotWall2Decor = chariotFloorJoint.addOrReplaceChild("chariotWall2Decor", CubeListBuilder.create().texOffs(96, 57).addBox(-16.0F, -1.5F, 0.0F, 16.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.0F, -9.0F, 9.8F, 0.0F, 1.5708F, 0.0F));

        PartDefinition chariotFloor3 = chariotFloorJoint.addOrReplaceChild("chariotFloor3", CubeListBuilder.create().texOffs(2, 61).addBox(-9.0F, -1.0F, 0.0F, 18.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(69, 53).addBox(9.0F, -6.0F, -1.0F, 1.0F, 6.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(69, 74).addBox(-10.0F, -6.0F, -1.0F, 1.0F, 6.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 24.5F, -0.1571F, 0.0F, 0.0F));

        PartDefinition chariotWall3Decor = chariotFloor3.addOrReplaceChild("chariotWall3Decor", CubeListBuilder.create().texOffs(100, 62).addBox(-14.0F, -1.5F, 0.0F, 14.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.5F, -7.5F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition chariotWall4Decor = chariotFloor3.addOrReplaceChild("chariotWall4Decor", CubeListBuilder.create().texOffs(100, 62).addBox(-14.0F, -1.5F, 0.0F, 14.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.5F, -7.5F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition chariotCenter = backBeam.addOrReplaceChild("chariotCenter", CubeListBuilder.create().texOffs(37, 105).addBox(-3.5F, -1.5F, -6.0F, 7.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 17.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition frontAxel = chariotCenter.addOrReplaceChild("frontAxel", CubeListBuilder.create().texOffs(51, 99).addBox(-18.0F, -1.5F, -1.5F, 36.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        PartDefinition leftWheelWeaponMain = frontAxel.addOrReplaceChild("leftWheelWeaponMain", CubeListBuilder.create().texOffs(108, 46).addBox(-1.0F, -1.4F, -8.0F, 2.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(18.0F, 0.0F, 0.0F, 3.1416F, 0.0F, -3.1416F));

        PartDefinition leftWheelWeaponHead = leftWheelWeaponMain.addOrReplaceChild("leftWheelWeaponHead", CubeListBuilder.create().texOffs(82, 26).addBox(-0.5F, -5.5F, -11.0F, 1.0F, 11.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(64, 32).addBox(-0.5F, -2.5F, 0.0F, 1.0F, 5.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 0.0F, -6.5F));

        PartDefinition leftWheelWeaponBlade = leftWheelWeaponHead.addOrReplaceChild("leftWheelWeaponBlade", CubeListBuilder.create().texOffs(49, 0).addBox(-7.0F, -0.5F, 0.0F, 7.0F, 1.0F, 24.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-6.0F, -0.5F, -1.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, -0.5F, -2.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, -0.5F, -3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, -0.5F, -4.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, -0.5F, -5.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, -0.5F, -6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -11.0F));

        PartDefinition rightWheelWeaponMain = frontAxel.addOrReplaceChild("rightWheelWeaponMain", CubeListBuilder.create().texOffs(108, 46).addBox(-1.0F, -1.6F, -8.0F, 2.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-18.0F, 0.0F, 0.0F, -3.1416F, 0.0F, 0.0F));

        PartDefinition rightWheelWeaponHead = rightWheelWeaponMain.addOrReplaceChild("rightWheelWeaponHead", CubeListBuilder.create().texOffs(82, 26).addBox(-0.5F, -5.5F, -11.0F, 1.0F, 11.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(64, 32).addBox(-0.5F, -2.5F, 0.0F, 1.0F, 5.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 0.0F, -6.5F));

        PartDefinition rightWheelWeaponBlade = rightWheelWeaponHead.addOrReplaceChild("rightWheelWeaponBlade", CubeListBuilder.create().texOffs(49, 0).addBox(-7.0F, -0.5F, 0.0F, 7.0F, 1.0F, 24.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-6.0F, -0.5F, -1.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, -0.5F, -2.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, -0.5F, -3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, -0.5F, -4.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, -0.5F, -5.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, -0.5F, -6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -11.0F));

        PartDefinition frontLeftWheelCenter = frontAxel.addOrReplaceChild("frontLeftWheelCenter", CubeListBuilder.create().texOffs(0, 27).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition frontLeftWheelRod1 = frontLeftWheelCenter.addOrReplaceChild("frontLeftWheelRod1", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, -1.5F, 2.3562F, 0.0F, 0.0F));

        PartDefinition frontLeftWheelFrame1 = frontLeftWheelRod1.addOrReplaceChild("frontLeftWheelFrame1", CubeListBuilder.create().texOffs(17, 26).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.4189F, 0.0F, 0.0F));

        PartDefinition frontLeftWheelRod2 = frontLeftWheelCenter.addOrReplaceChild("frontLeftWheelRod2", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, 1.5F, 0.7854F, 0.0F, 0.0F));

        PartDefinition frontLeftWheelFrame2 = frontLeftWheelRod2.addOrReplaceChild("frontLeftWheelFrame2", CubeListBuilder.create().texOffs(17, 26).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, -0.4189F, 0.0F, 0.0F));

        PartDefinition frontLeftWheelRod3 = frontLeftWheelCenter.addOrReplaceChild("frontLeftWheelRod3", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition frontLeftWheelFrame3 = frontLeftWheelRod3.addOrReplaceChild("frontLeftWheelFrame3", CubeListBuilder.create().texOffs(17, 26).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.4189F, 0.0F, 0.0F));

        PartDefinition frontLeftWheelRod4 = frontLeftWheelCenter.addOrReplaceChild("frontLeftWheelRod4", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition frontLeftWheelFrame4 = frontLeftWheelRod4.addOrReplaceChild("frontLeftWheelFrame4", CubeListBuilder.create().texOffs(17, 26).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.4014F, 0.0F, 0.0F));

        PartDefinition frontLeftWheelRod5 = frontLeftWheelCenter.addOrReplaceChild("frontLeftWheelRod5", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5F, 1.5F, 2.3562F, 0.0F, 0.0F));

        PartDefinition frontLeftWheelFrame5 = frontLeftWheelRod5.addOrReplaceChild("frontLeftWheelFrame5", CubeListBuilder.create().texOffs(17, 26).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.8F, 0.0F, -0.4014F, 0.0F, 0.0F));

        PartDefinition frontLeftWheelRod6 = frontLeftWheelCenter.addOrReplaceChild("frontLeftWheelRod6", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition frontLeftWheelFrame6 = frontLeftWheelRod6.addOrReplaceChild("frontLeftWheelFrame6", CubeListBuilder.create().texOffs(17, 26).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, -0.384F, 0.0F, 0.0F));

        PartDefinition frontLeftWheelRod7 = frontLeftWheelCenter.addOrReplaceChild("frontLeftWheelRod7", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5F, -1.5F, 0.7854F, 0.0F, 0.0F));

        PartDefinition frontLeftWheelFrame7 = frontLeftWheelRod7.addOrReplaceChild("frontLeftWheelFrame7", CubeListBuilder.create().texOffs(17, 26).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.4189F, 0.0F, 0.0F));

        PartDefinition frontLeftWheelRod8 = frontLeftWheelCenter.addOrReplaceChild("frontLeftWheelRod8", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition frontLeftWheelFrame8 = frontLeftWheelRod8.addOrReplaceChild("frontLeftWheelFrame8", CubeListBuilder.create().texOffs(17, 26).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, -0.4014F, 0.0F, 0.0F));

        PartDefinition frontRightWheelCenter = frontAxel.addOrReplaceChild("frontRightWheelCenter", CubeListBuilder.create().texOffs(0, 27).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition frontRightWheelRod1 = frontRightWheelCenter.addOrReplaceChild("frontRightWheelRod1", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition frontRightWheelFrame1 = frontRightWheelRod1.addOrReplaceChild("frontRightWheelFrame1", CubeListBuilder.create().texOffs(17, 26).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.4189F, 0.0F, 0.0F));

        PartDefinition frontRightWheelRod2 = frontRightWheelCenter.addOrReplaceChild("frontRightWheelRod2", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, 1.5F, 0.7854F, 0.0F, 0.0F));

        PartDefinition frontRightWheelFrame2 = frontRightWheelRod2.addOrReplaceChild("frontRightWheelFrame2", CubeListBuilder.create().texOffs(17, 26).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, -0.4189F, 0.0F, 0.0F));

        PartDefinition frontRightWheelRod3 = frontRightWheelCenter.addOrReplaceChild("frontRightWheelRod3", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5F, -1.5F, 0.7854F, 0.0F, 0.0F));

        PartDefinition frontRightWheelFrame3 = frontRightWheelRod3.addOrReplaceChild("frontRightWheelFrame3", CubeListBuilder.create().texOffs(17, 26).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.4189F, 0.0F, 0.0F));

        PartDefinition frontRightWheelRod4 = frontRightWheelCenter.addOrReplaceChild("frontRightWheelRod4", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5F, 1.5F, 2.3562F, 0.0F, 0.0F));

        PartDefinition frontRightWheelFrame4 = frontRightWheelRod4.addOrReplaceChild("frontRightWheelFrame4", CubeListBuilder.create().texOffs(17, 26).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.8F, 0.0F, -0.4014F, 0.0F, 0.0F));

        PartDefinition frontRightWheelRod5 = frontRightWheelCenter.addOrReplaceChild("frontRightWheelRod5", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, -1.5F, 2.3562F, 0.0F, 0.0F));

        PartDefinition frontRightWheelFrame5 = frontRightWheelRod5.addOrReplaceChild("frontRightWheelFrame5", CubeListBuilder.create().texOffs(17, 26).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.4189F, 0.0F, 0.0F));

        PartDefinition frontRightWheelRod6 = frontRightWheelCenter.addOrReplaceChild("frontRightWheelRod6", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition frontRightWheelFrame6 = frontRightWheelRod6.addOrReplaceChild("frontRightWheelFrame6", CubeListBuilder.create().texOffs(17, 26).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.4014F, 0.0F, 0.0F));

        PartDefinition frontRightWheelRod7 = frontRightWheelCenter.addOrReplaceChild("frontRightWheelRod7", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition frontRightWheelFrame7 = frontRightWheelRod7.addOrReplaceChild("frontRightWheelFrame7", CubeListBuilder.create().texOffs(17, 26).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, -0.4014F, 0.0F, 0.0F));

        PartDefinition frontRightWheelRod8 = frontRightWheelCenter.addOrReplaceChild("frontRightWheelRod8", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition frontRightWheelFrame8 = frontRightWheelRod8.addOrReplaceChild("frontRightWheelFrame8", CubeListBuilder.create().texOffs(17, 26).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, -0.384F, 0.0F, 0.0F));

        PartDefinition chariotBackBeam = chariotCenter.addOrReplaceChild("chariotBackBeam", CubeListBuilder.create().texOffs(79, 73).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 6.0F, -0.1745F, 0.0F, 0.0F));

        PartDefinition backAxel = chariotBackBeam.addOrReplaceChild("backAxel", CubeListBuilder.create().texOffs(0, 93).addBox(-13.0F, -0.5F, -0.5F, 26.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, 17.0F, 3.1416F, 0.0F, 0.0F));

        PartDefinition backLeftWheelCenter = backAxel.addOrReplaceChild("backLeftWheelCenter", CubeListBuilder.create().texOffs(4, 23).addBox(-0.4F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.5F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition backLeftWheelRod1 = backLeftWheelCenter.addOrReplaceChild("backLeftWheelRod1", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition backLeftWheelFrame1 = backLeftWheelRod1.addOrReplaceChild("backLeftWheelFrame1", CubeListBuilder.create().texOffs(19, 28).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.4189F, 0.0F, 0.0F));

        PartDefinition backLeftWheelRod2 = backLeftWheelCenter.addOrReplaceChild("backLeftWheelRod2", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition backLeftWheelFrame2 = backLeftWheelRod2.addOrReplaceChild("backLeftWheelFrame2", CubeListBuilder.create().texOffs(19, 28).addBox(-1.0F, -0.5F, -6.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, -0.384F, 0.0F, 0.0F));

        PartDefinition backLeftWheelRod3 = backLeftWheelCenter.addOrReplaceChild("backLeftWheelRod3", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.3562F, 0.0F, 0.0F));

        PartDefinition backLeftWheelFrame3 = backLeftWheelRod3.addOrReplaceChild("backLeftWheelFrame3", CubeListBuilder.create().texOffs(19, 28).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.4189F, 0.0F, 0.0F));

        PartDefinition backLeftWheelRod4 = backLeftWheelCenter.addOrReplaceChild("backLeftWheelRod4", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 2.3562F, 0.0F, 0.0F));

        PartDefinition backLeftWheelFrame4 = backLeftWheelRod4.addOrReplaceChild("backLeftWheelFrame4", CubeListBuilder.create().texOffs(19, 28).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.4189F, 0.0F, 0.0F));

        PartDefinition backLeftWheelRod5 = backLeftWheelCenter.addOrReplaceChild("backLeftWheelRod5", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition backLeftWheelFrame5 = backLeftWheelRod5.addOrReplaceChild("backLeftWheelFrame5", CubeListBuilder.create().texOffs(19, 28).addBox(-1.0F, -0.5F, -6.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, -0.384F, 0.0F, 0.0F));

        PartDefinition backLeftWheelRod6 = backLeftWheelCenter.addOrReplaceChild("backLeftWheelRod6", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition backLeftWheelFrame6 = backLeftWheelRod6.addOrReplaceChild("backLeftWheelFrame6", CubeListBuilder.create().texOffs(19, 28).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.4189F, 0.0F, 0.0F));

        PartDefinition backLeftWheelRod7 = backLeftWheelCenter.addOrReplaceChild("backLeftWheelRod7", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.1416F, 0.0F, 0.0F));

        PartDefinition backLeftWheelFrame7 = backLeftWheelRod7.addOrReplaceChild("backLeftWheelFrame7", CubeListBuilder.create().texOffs(19, 28).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.4189F, 0.0F, 0.0F));

        PartDefinition backLeftWheelRod8 = backLeftWheelCenter.addOrReplaceChild("backLeftWheelRod8", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition backLeftWheelFrame8 = backLeftWheelRod8.addOrReplaceChild("backLeftWheelFrame8", CubeListBuilder.create().texOffs(19, 28).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.4189F, 0.0F, 0.0F));

        PartDefinition backRightWheelCenter = backAxel.addOrReplaceChild("backRightWheelCenter", CubeListBuilder.create().texOffs(4, 23).addBox(-0.6F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.5F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition backRightWheelRod1 = backRightWheelCenter.addOrReplaceChild("backRightWheelRod1", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition backRightWheelFrame1 = backRightWheelRod1.addOrReplaceChild("backRightWheelFrame1", CubeListBuilder.create().texOffs(19, 28).addBox(-1.0F, -0.5F, -6.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, -0.384F, 0.0F, 0.0F));

        PartDefinition backRightWheelRod2 = backRightWheelCenter.addOrReplaceChild("backRightWheelRod2", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 2.3562F, 0.0F, 0.0F));

        PartDefinition backRightWheelFrame2 = backRightWheelRod2.addOrReplaceChild("backRightWheelFrame2", CubeListBuilder.create().texOffs(19, 28).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.4189F, 0.0F, 0.0F));

        PartDefinition backRightWheelRod3 = backRightWheelCenter.addOrReplaceChild("backRightWheelRod3", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.3562F, 0.0F, 0.0F));

        PartDefinition backRightWheelFrame3 = backRightWheelRod3.addOrReplaceChild("backRightWheelFrame3", CubeListBuilder.create().texOffs(19, 28).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.4189F, 0.0F, 0.0F));

        PartDefinition backRightWheelRod4 = backRightWheelCenter.addOrReplaceChild("backRightWheelRod4", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.1416F, 0.0F, 0.0F));

        PartDefinition backRightWheelFrame4 = backRightWheelRod4.addOrReplaceChild("backRightWheelFrame4", CubeListBuilder.create().texOffs(19, 28).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.4189F, 0.0F, 0.0F));

        PartDefinition backRightWheelRod5 = backRightWheelCenter.addOrReplaceChild("backRightWheelRod5", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition backRightWheelFrame5 = backRightWheelRod5.addOrReplaceChild("backRightWheelFrame5", CubeListBuilder.create().texOffs(19, 28).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.4189F, 0.0F, 0.0F));

        PartDefinition backRightWheelRod6 = backRightWheelCenter.addOrReplaceChild("backRightWheelRod6", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition backRightWheelFrame6 = backRightWheelRod6.addOrReplaceChild("backRightWheelFrame6", CubeListBuilder.create().texOffs(19, 28).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.4189F, 0.0F, 0.0F));

        PartDefinition backRightWheelRod7 = backRightWheelCenter.addOrReplaceChild("backRightWheelRod7", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition backRightWheelFrame7 = backRightWheelRod7.addOrReplaceChild("backRightWheelFrame7", CubeListBuilder.create().texOffs(19, 28).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.4189F, 0.0F, 0.0F));

        PartDefinition backRightWheelRod8 = backRightWheelCenter.addOrReplaceChild("backRightWheelRod8", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition backRightWheelFrame8 = backRightWheelRod8.addOrReplaceChild("backRightWheelFrame8", CubeListBuilder.create().texOffs(19, 28).addBox(-1.0F, -0.5F, -6.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, -0.384F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 129, 256);
    }

    @Override
    public void setupAnim(Gordius entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        this.frontAxel.xRot = Mth.cos(limbSwing * 0.3F) * (float) Math.PI;
        this.backAxel.xRot = Mth.cos(limbSwing * 0.4f) * (float) Math.PI;
        this.leftWheelWeaponMain.xRot = this.frontAxel.xRot;
        this.rightWheelWeaponMain.xRot = -this.frontAxel.xRot;
        this.footLeftFront.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
        this.footRightFront.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
        this.footLeftFront2.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
        this.footRightFront2.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
        this.footLeftRear.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
        this.footRightRear.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
        this.footLeftRear2.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
        this.footRightRear2.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
        float partialTicks = Minecraft.getInstance().getFrameTime();
        entity.getAnimationHandler().runIfNotNull(anim -> this.anim.doAnimation(this, anim.getAnimationClient(), anim.getTick(), partialTicks));
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.getMainPart().render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public ModelPartHandler getHandler() {
        return this.model;
    }
}