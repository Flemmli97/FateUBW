package io.github.flemmli97.fateubw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.servant.EntityHeracles;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.client.model.ModelPartHandler;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

public class ModelHeracles<T extends EntityHeracles & IAnimated> extends ModelServant<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Fate.MODID, "heracles"), "main");

    private ModelPartHandler.ModelPartExtended upperTorso;
    private ModelPartHandler.ModelPartExtended head;
    private ModelPartHandler.ModelPartExtended rightArmUp;
    private ModelPartHandler.ModelPartExtended leftArmUp;
    private ModelPartHandler.ModelPartExtended rightUpperThigh;
    private ModelPartHandler.ModelPartExtended leftUpperThigh;
    private ModelPartHandler.ModelPartExtended leftBiceps;
    private ModelPartHandler.ModelPartExtended leftBicepsJoint;
    private ModelPartHandler.ModelPartExtended leftElbow;
    private ModelPartHandler.ModelPartExtended leftLowerArm;
    private ModelPartHandler.ModelPartExtended leftWrist;
    private ModelPartHandler.ModelPartExtended rightBiceps;
    private ModelPartHandler.ModelPartExtended rightBicepsJoint;
    private ModelPartHandler.ModelPartExtended rightElbow;
    private ModelPartHandler.ModelPartExtended rightLowerArm;
    private ModelPartHandler.ModelPartExtended rightWrist;

    public ModelHeracles(ModelPart root) {
        super(root, "heracles");
        this.upperTorso = this.model.getPart("upperTorso");
        this.head = this.model.getPart("head");
        this.rightArmUp = this.model.getPart("rightArmUp");
        this.leftArmUp = this.model.getPart("leftArmUp");
        this.rightUpperThigh = this.model.getPart("rightUpperThigh");
        this.leftUpperThigh = this.model.getPart("leftUpperThigh");

        this.leftBiceps = this.model.getPart("leftBiceps");
        this.leftBicepsJoint = this.model.getPart("leftBicepsJoint");
        this.leftElbow = this.model.getPart("leftElbow");
        this.leftLowerArm = this.model.getPart("leftLowerArm");
        this.leftWrist = this.model.getPart("leftWrist");

        this.rightBiceps = this.model.getPart("rightBiceps");
        this.rightBicepsJoint = this.model.getPart("rightBicepsJoint");
        this.rightElbow = this.model.getPart("rightElbow");
        this.rightLowerArm = this.model.getPart("rightLowerArm");
        this.rightWrist = this.model.getPart("rightWrist");
        this.servantBody.visible = false;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = ModelServant.mesh(new CubeDeformation(0));
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition upperTorso = partdefinition.addOrReplaceChild("upperTorso", CubeListBuilder.create().texOffs(23, 65).mirror().addBox(-6.0F, -2.5F, -3.0F, 12.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 3).mirror().addBox(0.6F, -2.1F, -3.4F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 3).mirror().addBox(-4.6F, -2.1F, -3.4F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 3).mirror().addBox(0.1F, 0.1F, -3.4F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 3).mirror().addBox(-4.1F, 0.1F, -3.4F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -1.6F, 0.0F));

        PartDefinition lowerTorso = upperTorso.addOrReplaceChild("lowerTorso", CubeListBuilder.create().texOffs(0, 6).mirror().addBox(-5.0F, -2.0F, -2.5F, 10.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-2.1F, -1.7F, -2.8F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(0.1F, -1.7F, -2.8F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(0.1F, -0.6F, -2.8F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(0.1F, 0.5F, -2.8F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-2.1F, -0.6F, -2.8F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-2.1F, 0.5F, -2.8F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 4.5F, 0.0F));

        PartDefinition waist = lowerTorso.addOrReplaceChild("waist", CubeListBuilder.create().texOffs(0, 35).mirror().addBox(-4.5F, -2.0F, -2.0F, 9.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 4.0F, 0.0F));

        PartDefinition rightUpperThigh = waist.addOrReplaceChild("rightUpperThigh", CubeListBuilder.create().texOffs(0, 43).mirror().addBox(-1.5F, -2.5F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.0F, 3.8F, 0.0F, 0.0F, -0.0456F, -0.0175F));

        PartDefinition rightKnee = rightUpperThigh.addOrReplaceChild("rightKnee", CubeListBuilder.create().texOffs(0, 50).mirror().addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 3.0F, -0.4F));

        PartDefinition rightLowerThigh = rightKnee.addOrReplaceChild("rightLowerThigh", CubeListBuilder.create().texOffs(0, 56).mirror().addBox(-1.5F, -2.5F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 3.9F, 0.7F));

        PartDefinition rightAnkle = rightLowerThigh.addOrReplaceChild("rightAnkle", CubeListBuilder.create().texOffs(0, 64).mirror().addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 4.0F, -0.4F));

        PartDefinition rightFoot = rightAnkle.addOrReplaceChild("rightFoot", CubeListBuilder.create().texOffs(0, 70).mirror().addBox(-2.0F, 0.0F, -2.5F, 4.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 1.4F, -0.3F));

        PartDefinition rightToe1 = rightFoot.addOrReplaceChild("rightToe1", CubeListBuilder.create().texOffs(13, 66).mirror().addBox(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.5F, 0.0F, -1.0F, 0.0F, -0.5009F, 0.0F));

        PartDefinition rightToe2 = rightFoot.addOrReplaceChild("rightToe2", CubeListBuilder.create().texOffs(13, 66).mirror().addBox(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.5F, 0.0F, -1.3F, 0.0F, 0.5009F, 0.0F));

        PartDefinition rightToe3 = rightFoot.addOrReplaceChild("rightToe3", CubeListBuilder.create().texOffs(13, 66).mirror().addBox(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.1F, 0.0F, -1.5F));

        PartDefinition rightToe4 = rightFoot.addOrReplaceChild("rightToe4", CubeListBuilder.create().texOffs(13, 66).mirror().addBox(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, -1.5F));

        PartDefinition rightToe5 = rightFoot.addOrReplaceChild("rightToe5", CubeListBuilder.create().texOffs(13, 66).mirror().addBox(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.1F, 0.0F, -1.5F));

        PartDefinition rFootShape1 = rightFoot.addOrReplaceChild("rFootShape1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -1.9F, 0.7854F, 0.0F, 0.0F));

        PartDefinition rFootShape2 = rightFoot.addOrReplaceChild("rFootShape2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 1.1F, 0.7854F, 0.0F, 0.0F));

        PartDefinition rightShin = rightLowerThigh.addOrReplaceChild("rightShin", CubeListBuilder.create().texOffs(18, 70).mirror().addBox(-0.5F, -2.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -1.5F, 0.0456F, 0.0F, 0.0F));

        PartDefinition leftUpperThigh = waist.addOrReplaceChild("leftUpperThigh", CubeListBuilder.create().texOffs(0, 43).mirror().addBox(-1.5F, -2.5F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, 3.8F, 0.0F));

        PartDefinition leftKnee = leftUpperThigh.addOrReplaceChild("leftKnee", CubeListBuilder.create().texOffs(0, 50).mirror().addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 3.0F, -0.4F));

        PartDefinition leftLowerThigh = leftKnee.addOrReplaceChild("leftLowerThigh", CubeListBuilder.create().texOffs(0, 56).mirror().addBox(-1.5F, -2.5F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 3.9F, 0.7F));

        PartDefinition leftAnkle = leftLowerThigh.addOrReplaceChild("leftAnkle", CubeListBuilder.create().texOffs(0, 64).mirror().addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 4.0F, -0.4F));

        PartDefinition leftFoot = leftAnkle.addOrReplaceChild("leftFoot", CubeListBuilder.create().texOffs(0, 70).mirror().addBox(-2.0F, 0.0F, -2.5F, 4.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 1.4F, -0.3F));

        PartDefinition leftToe1 = leftFoot.addOrReplaceChild("leftToe1", CubeListBuilder.create().texOffs(13, 66).mirror().addBox(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.1F, 0.0F, -1.5F));

        PartDefinition leftToe2 = leftFoot.addOrReplaceChild("leftToe2", CubeListBuilder.create().texOffs(13, 66).mirror().addBox(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.5F, 0.0F, -1.0F, 0.0F, 0.5009F, 0.0F));

        PartDefinition leftToe3 = leftFoot.addOrReplaceChild("leftToe3", CubeListBuilder.create().texOffs(13, 66).mirror().addBox(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, -1.5F));

        PartDefinition leftToe4 = leftFoot.addOrReplaceChild("leftToe4", CubeListBuilder.create().texOffs(13, 66).mirror().addBox(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.1F, 0.0F, -1.5F));

        PartDefinition leftToe5 = leftFoot.addOrReplaceChild("leftToe5", CubeListBuilder.create().texOffs(13, 66).mirror().addBox(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.5F, 0.0F, -1.3F, 0.0F, -0.5009F, 0.0F));

        PartDefinition lFootShape1 = leftFoot.addOrReplaceChild("lFootShape1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -1.9F, 0.7854F, 0.0F, 0.0F));

        PartDefinition lFootShape2 = leftFoot.addOrReplaceChild("lFootShape2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 1.1F, 0.7854F, 0.0F, 0.0F));

        PartDefinition leftShin = leftLowerThigh.addOrReplaceChild("leftShin", CubeListBuilder.create().texOffs(18, 70).mirror().addBox(-0.5F, -2.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -1.5F, 0.0456F, 0.0F, 0.0F));

        PartDefinition outerSkirt = waist.addOrReplaceChild("outerSkirt", CubeListBuilder.create().texOffs(0, 21).mirror().addBox(-5.5F, -3.5F, -3.0F, 11.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 4.5F, 0.0F));

        PartDefinition innerSkirt = outerSkirt.addOrReplaceChild("innerSkirt", CubeListBuilder.create().texOffs(0, 15).mirror().addBox(-5.0F, -3.0F, 0.0F, 10.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, -2.6F));

        PartDefinition neck = upperTorso.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(26, 0).mirror().addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -3.0F, 0.0F));

        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(30, 0).mirror().addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -4.5F, 0.0F));

        PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(16, 60).mirror().addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -0.2F, -3.5F, -0.4294F, 0.0F, 0.0F));

        PartDefinition hair1 = head.addOrReplaceChild("hair1", CubeListBuilder.create().texOffs(34, 16).mirror().addBox(0.0F, -5.5F, -5.5F, 0.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -1.5F, 2.0F));

        PartDefinition hair2 = head.addOrReplaceChild("hair2", CubeListBuilder.create().texOffs(34, 5).mirror().addBox(0.0F, -5.5F, -5.5F, 0.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.2F, -1.5F, 2.0F));

        PartDefinition hair3 = head.addOrReplaceChild("hair3", CubeListBuilder.create().texOffs(34, 5).mirror().addBox(0.0F, -5.5F, -5.5F, 0.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.2F, -1.5F, 2.0F));

        PartDefinition hair4 = head.addOrReplaceChild("hair4", CubeListBuilder.create().texOffs(34, 16).mirror().addBox(0.0F, -5.5F, -5.5F, 0.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.4F, -1.5F, 2.0F));

        PartDefinition hair5 = head.addOrReplaceChild("hair5", CubeListBuilder.create().texOffs(34, 16).mirror().addBox(0.0F, -5.5F, -5.5F, 0.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.4F, -1.5F, 2.0F));

        PartDefinition hair6 = head.addOrReplaceChild("hair6", CubeListBuilder.create().texOffs(34, 5).mirror().addBox(0.0F, -5.5F, -5.5F, 0.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, -1.5F, 2.0F, 0.0F, 0.2276F, 0.0F));

        PartDefinition hair7 = head.addOrReplaceChild("hair7", CubeListBuilder.create().texOffs(34, 5).mirror().addBox(0.0F, -5.5F, -5.5F, 0.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -1.5F, 2.0F, 0.0F, -0.2276F, 0.0F));

        PartDefinition leftArmUp = upperTorso.addOrReplaceChild("leftArmUp", CubeListBuilder.create().texOffs(12, 43).addBox(0.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 0.0F, 0.0F));

        PartDefinition leftShoulder = leftArmUp.addOrReplaceChild("leftShoulder", CubeListBuilder.create().texOffs(10, 51).mirror().addBox(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.4F, -2.0F, 0.0F, 0.0F, 0.0F, 0.1571F));

        PartDefinition leftBiceps = leftArmUp.addOrReplaceChild("leftBiceps", CubeListBuilder.create().texOffs(26, 56).addBox(-2.5F, -2.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, 1.6F, 0.0F, 0.0F, 0.0F, 0.8727F));

        PartDefinition leftBicepsJoint = leftBiceps.addOrReplaceChild("leftBicepsJoint", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.0F, 2.0F, 0.0F, 0.0F, 0.0F, -0.8727F));

        PartDefinition leftElbow = leftBicepsJoint.addOrReplaceChild("leftElbow", CubeListBuilder.create().texOffs(28, 48).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.6F, 0.9F, 0.0F));

        PartDefinition leftLowerArm = leftElbow.addOrReplaceChild("leftLowerArm", CubeListBuilder.create().texOffs(26, 38).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.2F, 4.0F, 0.0F));

        PartDefinition leftWrist = leftLowerArm.addOrReplaceChild("leftWrist", CubeListBuilder.create().texOffs(59, 69).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.5F, 0.0F));

        PartDefinition leftHand = leftWrist.addOrReplaceChild("leftHand", CubeListBuilder.create().texOffs(16, 0).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 2.5F, 0.0F));

        PartDefinition leftFinger1 = leftHand.addOrReplaceChild("leftFinger1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -1.8F));

        PartDefinition leftFinger2 = leftHand.addOrReplaceChild("leftFinger2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -0.7F));

        PartDefinition leftFinger3 = leftHand.addOrReplaceChild("leftFinger3", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.4F));

        PartDefinition leftFinger4 = leftHand.addOrReplaceChild("leftFinger4", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 1.5F));

        PartDefinition leftThumbUp = leftWrist.addOrReplaceChild("leftThumbUp", CubeListBuilder.create().texOffs(16, 60).mirror().addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 0.5F, -1.7F, -0.791F, 0.0F, 0.0F));

        PartDefinition leftThumbDown = leftThumbUp.addOrReplaceChild("leftThumbDown", CubeListBuilder.create().texOffs(16, 60).mirror().addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 1.6F, -0.2F, 0.791F, 0.0F, 0.0F));

        PartDefinition leftElbowBone = leftLowerArm.addOrReplaceChild("leftElbowBone", CubeListBuilder.create().texOffs(10, 0).mirror().addBox(-0.5F, -2.0F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.0F, -4.0F, 0.0F, 0.0F, 0.0F, 0.2485F));

        PartDefinition rightArmUp = upperTorso.addOrReplaceChild("rightArmUp", CubeListBuilder.create().texOffs(12, 43).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 0.0F, 0.0F));

        PartDefinition rightShoulder = rightArmUp.addOrReplaceChild("rightShoulder", CubeListBuilder.create().texOffs(10, 51).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.4F, -2.0F, 0.0F, 0.0F, 0.0F, -0.1571F));

        PartDefinition rightBiceps = rightArmUp.addOrReplaceChild("rightBiceps", CubeListBuilder.create().texOffs(26, 56).mirror().addBox(-2.5F, -2.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.5F, 1.6F, 0.0F, 0.0F, 0.0F, -0.8727F));

        PartDefinition rightBicepsJoint = rightBiceps.addOrReplaceChild("rightBicepsJoint", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.8727F));

        PartDefinition rightElbow = rightBicepsJoint.addOrReplaceChild("rightElbow", CubeListBuilder.create().texOffs(28, 48).mirror().addBox(-1.5F, -2.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-0.6F, 0.9F, 0.0F));

        PartDefinition rightLowerArm = rightElbow.addOrReplaceChild("rightLowerArm", CubeListBuilder.create().texOffs(26, 38).mirror().addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-0.2F, 4.0F, 0.0F));

        PartDefinition rightWrist = rightLowerArm.addOrReplaceChild("rightWrist", CubeListBuilder.create().texOffs(59, 69).mirror().addBox(-1.5F, -1.5F, -2.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 3.5F, 0.0F));

        PartDefinition rightHand = rightWrist.addOrReplaceChild("rightHand", CubeListBuilder.create().texOffs(16, 0).mirror().addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.0F, 2.5F, 0.0F));

        PartDefinition rightFinger1 = rightHand.addOrReplaceChild("rightFinger1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 1.0F, -1.8F));

        PartDefinition rightFinger2 = rightHand.addOrReplaceChild("rightFinger2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 1.0F, -0.7F));

        PartDefinition rightFinger3 = rightHand.addOrReplaceChild("rightFinger3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 1.0F, 0.4F));

        PartDefinition rightFinger4 = rightHand.addOrReplaceChild("rightFinger4", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 1.0F, 1.5F));

        PartDefinition rightThumbUp = rightWrist.addOrReplaceChild("rightThumbUp", CubeListBuilder.create().texOffs(16, 60).mirror().addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 0.5F, -1.7F, -0.791F, 0.0F, 0.0F));

        PartDefinition rightThumbDown = rightThumbUp.addOrReplaceChild("rightThumbDown", CubeListBuilder.create().texOffs(16, 60).mirror().addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 1.6F, -0.2F, 0.791F, 0.0F, 0.0F));

        PartDefinition rightElbowBonee = rightLowerArm.addOrReplaceChild("rightElbowBonee", CubeListBuilder.create().texOffs(10, 0).mirror().addBox(-0.5F, -2.0F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, -4.0F, 0.0F, 0.0F, 0.0F, -0.2485F));

        return LayerDefinition.create(meshdefinition, 76, 76);
    }

    @Override
    public ModelPartHandler.ModelPartExtended getHand(InteractionHand side) {
        return side == InteractionHand.MAIN_HAND ? this.rightArmUp : this.leftArmUp;
    }

    @Override
    public ModelPartHandler getHandler() {
        return this.model;
    }

    @Override
    public void transform(HumanoidArm humanoidArm, PoseStack poseStack) {
        if (humanoidArm == HumanoidArm.LEFT) {
            this.rotate(poseStack, this.upperTorso, this.leftArmUp, this.leftBiceps, this.leftBicepsJoint, this.leftElbow, this.leftLowerArm, this.leftWrist);
        } else {
            this.rotate(poseStack, this.upperTorso, this.rightArmUp, this.rightBiceps, this.rightBicepsJoint, this.rightElbow, this.rightLowerArm, this.rightWrist);
        }
    }

    @Override
    public void postTransform(boolean leftSide, PoseStack stack) {
        stack.translate(0, 0, -2 / 16d);
    }

    @Override
    public void preAnimSetup(T servant, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.upperTorso.visible = true;

        this.model.resetPoses();
        this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
        this.head.xRot = headPitch / (180F / (float) Math.PI);

        this.rightArmUp.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        this.leftArmUp.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        this.rightArmUp.zRot = 0;
        this.leftArmUp.zRot = 0;
        this.rightUpperThigh.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftUpperThigh.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.rightUpperThigh.yRot = 0;
        this.leftUpperThigh.yRot = 0;

        if (this.riding) {
            this.rightArmUp.xRot += -((float) Math.PI / 5F);
            this.leftArmUp.xRot += -((float) Math.PI / 5F);
            this.rightUpperThigh.xRot = -((float) Math.PI * 2F / 5F);
            this.leftUpperThigh.xRot = -((float) Math.PI * 2F / 5F);
            this.rightUpperThigh.yRot = ((float) Math.PI / 10F);
            this.leftUpperThigh.yRot = -((float) Math.PI / 10F);
        }

        if (this.heldItemOff == 1)
            this.leftArmUp.xRot = this.leftArmUp.xRot * 0.5F - ((float) Math.PI / 10F);
        if (this.heldItemMain == 1)
            this.rightArmUp.xRot = this.rightArmUp.xRot * 0.5F - ((float) Math.PI / 10F);

        this.rightArmUp.yRot = 0;
        this.leftArmUp.yRot = 0;
        if (this.attackTime > -9990) {
            float swingProgress = this.attackTime;
            this.upperTorso.yRot = Mth.sin(Mth.sqrt(swingProgress) * (float) Math.PI * 2.0F) * 0.2F;
            this.rightArmUp.yRot += this.upperTorso.yRot;
            this.leftArmUp.yRot += this.upperTorso.yRot;
            this.leftArmUp.xRot += this.upperTorso.yRot;
            swingProgress = 1.0F - this.attackTime;
            swingProgress *= swingProgress;
            swingProgress *= swingProgress;
            swingProgress = 1.0F - swingProgress;
            float var9 = Mth.sin(swingProgress * (float) Math.PI);
            float var10 = Mth.sin(this.attackTime * (float) Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
            this.rightArmUp.xRot = (float) ((double) this.rightArmUp.xRot - ((double) var9 * 1.2D + (double) var10));
            this.rightArmUp.yRot += this.upperTorso.yRot * 2.0F;
            this.rightArmUp.zRot = Mth.sin(this.attackTime * (float) Math.PI) * -0.4F;
        }

        this.upperTorso.xRot = 0;

        this.rightArmUp.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.leftArmUp.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.rightArmUp.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
        this.leftArmUp.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;
    }

    @Override
    public ModelPart getHead() {
        this.dummyHead.loadPose(this.head.storePose());
        return this.dummyHead;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.upperTorso.render(poseStack, buffer, packedLight, packedOverlay);
    }
}
