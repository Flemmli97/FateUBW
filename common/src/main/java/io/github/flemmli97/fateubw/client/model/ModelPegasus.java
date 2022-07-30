package io.github.flemmli97.fateubw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.minions.Pegasus;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
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

public class ModelPegasus extends EntityModel<Pegasus> implements ExtendedModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Fate.MODID, "pegasus"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended head;

    public ModelPegasus(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root.getChild("body"), "body");
        this.head = this.model.getPart("head");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(Fate.MODID, "pegasus"));
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 21).addBox(-6.5F, -6.0F, -13.0F, 12.0F, 12.0F, 26.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.0F));

        PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(33, 77).addBox(-2.0F, -11.0F, 0.0F, 5.0F, 10.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(58, 79).addBox(0.0F, -11.0F, 1.0F, 1.0F, 10.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, -12.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(57, 60).addBox(-5.0F, -8.0F, -11.0F, 9.0F, 8.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(70, 79).addBox(1.0F, -11.0F, -5.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(56, 60).addBox(-5.0F, -11.0F, -5.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 21).addBox(-1.0F, -11.0F, -9.0F, 1.0F, 11.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 88).addBox(-3.5F, -8.0F, -16.0F, 7.0F, 8.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(86, 57).addBox(-2.5F, -7.5F, -21.0F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -11.0F, 10.0F));

        PartDefinition leftFrontLegBase = body.addOrReplaceChild("leftFrontLegBase", CubeListBuilder.create().texOffs(79, 94).addBox(-1.5F, -1.0F, -0.5F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 4.0F, -13.0F));

        PartDefinition leftFrontLeg = leftFrontLegBase.addOrReplaceChild("leftFrontLeg", CubeListBuilder.create().texOffs(0, 102).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 3.0F));

        PartDefinition leftFrontLegDown = leftFrontLeg.addOrReplaceChild("leftFrontLegDown", CubeListBuilder.create().texOffs(52, 99).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 5.0F, -3.0F));

        PartDefinition leftFrontHoove = leftFrontLegDown.addOrReplaceChild("leftFrontHoove", CubeListBuilder.create().texOffs(97, 77).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 1.5F));

        PartDefinition rightFrontLegBase = body.addOrReplaceChild("rightFrontLegBase", CubeListBuilder.create().texOffs(51, 32).addBox(-1.5F, -1.0F, -0.5F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.5F, 4.0F, -13.0F));

        PartDefinition rightFrontLeg = rightFrontLegBase.addOrReplaceChild("rightFrontLeg", CubeListBuilder.create().texOffs(65, 99).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 3.0F));

        PartDefinition rightFrontLegDown = rightFrontLeg.addOrReplaceChild("rightFrontLegDown", CubeListBuilder.create().texOffs(13, 21).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 5.0F, -3.0F));

        PartDefinition rightFrontHoove = rightFrontLegDown.addOrReplaceChild("rightFrontHoove", CubeListBuilder.create().texOffs(96, 70).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 1.5F));

        PartDefinition leftBackLegBase = body.addOrReplaceChild("leftBackLegBase", CubeListBuilder.create().texOffs(81, 0).addBox(-2.5F, -4.0F, -3.5F, 5.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 4.0F, 9.6F));

        PartDefinition leftBackLeg = leftBackLegBase.addOrReplaceChild("leftBackLeg", CubeListBuilder.create().texOffs(41, 60).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -1.5F, 0.3927F, 0.0F, 0.0F));

        PartDefinition leftBackLegDown = leftBackLeg.addOrReplaceChild("leftBackLegDown", CubeListBuilder.create().texOffs(96, 94).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 4.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition leftBackHoove = leftBackLegDown.addOrReplaceChild("leftBackHoove", CubeListBuilder.create().texOffs(38, 95).addBox(-1.5F, -0.3007F, -1.9537F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.3007F, -1.5463F));

        PartDefinition rightBackLegBase = body.addOrReplaceChild("rightBackLegBase", CubeListBuilder.create().texOffs(79, 79).addBox(-2.5F, -4.0F, -3.5F, 5.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.5F, 4.0F, 9.6F));

        PartDefinition rightBackLeg = rightBackLegBase.addOrReplaceChild("rightBackLeg", CubeListBuilder.create().texOffs(0, 60).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -1.5F, 0.3927F, 0.0F, 0.0F));

        PartDefinition rightBackLegDown = rightBackLeg.addOrReplaceChild("rightBackLegDown", CubeListBuilder.create().texOffs(25, 95).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 4.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition rightBackHoove = rightBackLegDown.addOrReplaceChild("rightBackHoove", CubeListBuilder.create().texOffs(23, 77).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, -1.5F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(51, 21).addBox(-2.5F, 0.0F, -1.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.5F, 12.0F, 0.6545F, 0.0F, 0.0F));

        PartDefinition tailTip = tail.addOrReplaceChild("tailTip", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -3.5F, 2.0F, 13.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 3.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition rightWingBase = body.addOrReplaceChild("rightWingBase", CubeListBuilder.create().texOffs(99, 0).addBox(-6.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 77).mirror().addBox(-6.0F, -0.5F, 1.0F, 6.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 77).mirror().addBox(-6.0F, 0.5F, 1.0F, 6.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-6.0F, -4.0F, 0.0F));

        PartDefinition rightWing = rightWingBase.addOrReplaceChild("rightWing", CubeListBuilder.create().texOffs(81, 15).addBox(-12.0F, -2.0F, 0.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 60).mirror().addBox(-12.0F, -1.5F, 2.0F, 12.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 60).mirror().addBox(-12.0F, -0.5F, 2.0F, 12.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.5F, 1.0F, -1.0F));

        PartDefinition rightWing2 = rightWing.addOrReplaceChild("rightWing2", CubeListBuilder.create().texOffs(77, 52).addBox(-15.0F, 0.0F, 0.0F, 15.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(51, 21).mirror().addBox(-15.0F, 0.5F, 2.0F, 15.0F, 0.0F, 20.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(51, 21).mirror().addBox(-15.0F, 1.5F, 2.0F, 15.0F, 0.0F, 20.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-11.5F, -2.0F, 0.0F));

        PartDefinition rightWing3 = rightWing2.addOrReplaceChild("rightWing3", CubeListBuilder.create().texOffs(51, 42).addBox(-27.0F, 0.0F, 0.0F, 27.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).mirror().addBox(-30.0F, 0.5F, 2.0F, 30.0F, 0.0F, 20.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-30.0F, 1.5F, 2.0F, 30.0F, 0.0F, 20.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-14.5F, 0.0F, 0.0F));

        PartDefinition leftWingBase = body.addOrReplaceChild("leftWingBase", CubeListBuilder.create().texOffs(99, 0).addBox(0.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 77).addBox(0.0F, -0.5F, 1.0F, 6.0F, 0.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 77).addBox(0.0F, 0.5F, 1.0F, 6.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -4.0F, 0.0F));

        PartDefinition leftWing = leftWingBase.addOrReplaceChild("leftWing", CubeListBuilder.create().texOffs(81, 15).addBox(0.0F, -1.0F, 0.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 60).addBox(0.0F, -0.5F, 2.0F, 12.0F, 0.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 60).addBox(0.0F, 0.5F, 2.0F, 12.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(5.5F, 0.0F, -1.0F));

        PartDefinition leftWing2 = leftWing.addOrReplaceChild("leftWing2", CubeListBuilder.create().texOffs(77, 47).addBox(0.0F, -1.0F, 0.0F, 15.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(51, 21).addBox(0.0F, -0.5F, 2.0F, 15.0F, 0.0F, 20.0F, new CubeDeformation(0.0F))
                .texOffs(51, 21).addBox(0.0F, 0.5F, 2.0F, 15.0F, 0.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(11.5F, 0.0F, 0.0F));

        PartDefinition leftWing3 = leftWing2.addOrReplaceChild("leftWing3", CubeListBuilder.create().texOffs(51, 42).addBox(0.0F, -1.0F, 0.0F, 27.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(0.0F, -0.5F, 2.0F, 30.0F, 0.0F, 20.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(0.0F, 0.5F, 2.0F, 30.0F, 0.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public ModelPartHandler getHandler() {
        return this.model;
    }

    @Override
    public void setupAnim(Pegasus entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        this.head.yRot += netHeadYaw * Mth.DEG_TO_RAD * 0.3f;
        this.head.xRot += headPitch * Mth.DEG_TO_RAD * 0.1f;
        AnimatedAction anim = entity.getAnimationHandler().getAnimation();
        float partialTicks = Minecraft.getInstance().getFrameTime();
        if (anim == null) {
            if (!entity.isOnGround())
                this.anim.doAnimation(this, "fly", entity.tickCount, partialTicks);
            else if (entity.isSprinting())
                this.anim.doAnimation(this, "run", entity.tickCount, partialTicks);
            else if (limbSwingAmount > 0.1)
                this.anim.doAnimation(this, "walk", entity.tickCount, partialTicks);
            else
                this.anim.doAnimation(this, "iddle", entity.tickCount, partialTicks);
        } else
            this.anim.doAnimation(this, anim.getAnimationClient(), anim.getTick(), partialTicks);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.getMainPart().render(poseStack, buffer, packedLight, packedOverlay);
    }
}
