package io.github.flemmli97.fateubw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.minions.Pegasus;
import io.github.flemmli97.tenshilib.client.AnimationManager;
import io.github.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartHandler;
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

public class ModelPegasus extends EntityModel<Pegasus> implements ExtendedModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Fate.MODID, "pegasus"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;
    private ModelPartHandler.ModelPartExtended body;

    public ModelPegasus(ModelPart root) {
        super();
        this.model = new ModelPartHandler(root.getChild("body"), "body");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(Fate.MODID, "pegasus"));
        this.body = this.model.getMainPart();
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.5F, -6.0F, -13.0F, 12.0F, 12.0F, 26.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.0F));

        PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 38).addBox(-2.0F, -11.0F, 0.0F, 5.0F, 10.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 55).addBox(0.0F, -11.0F, 1.0F, 1.0F, 10.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, -12.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(24, 38).addBox(-5.0F, -5.0F, -1.0F, 9.0F, 8.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(46, 58).addBox(1.0F, -8.0F, 7.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(46, 58).mirror().addBox(-5.0F, -8.0F, 7.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(24, 58).addBox(-1.0F, -8.0F, 3.0F, 1.0F, 11.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(66, 38).addBox(-3.5F, -5.0F, -6.0F, 7.0F, 8.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(66, 51).addBox(-2.5F, -4.5F, -10.0F, 5.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -14.0F, -2.0F));

        PartDefinition leftFrontLegBase = body.addOrReplaceChild("leftFrontLegBase", CubeListBuilder.create().texOffs(77, 0).addBox(-1.5F, -1.0F, -0.5F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 4.0F, -13.0F));

        PartDefinition leftFrontLeg = leftFrontLegBase.addOrReplaceChild("leftFrontLeg", CubeListBuilder.create().texOffs(77, 8).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 3.0F));

        PartDefinition leftFrontLegDown = leftFrontLeg.addOrReplaceChild("leftFrontLegDown", CubeListBuilder.create().texOffs(77, 16).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 5.0F, -3.0F));

        PartDefinition leftFrontHoove = leftFrontLegDown.addOrReplaceChild("leftFrontHoove", CubeListBuilder.create().texOffs(77, 25).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 1.5F));

        PartDefinition rightFrontLegBase = body.addOrReplaceChild("rightFrontLegBase", CubeListBuilder.create().texOffs(77, 0).mirror().addBox(-1.5F, -1.0F, -0.5F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.5F, 4.0F, -13.0F));

        PartDefinition rightFrontLeg = rightFrontLegBase.addOrReplaceChild("rightFrontLeg", CubeListBuilder.create().texOffs(77, 8).mirror().addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 3.0F, 3.0F));

        PartDefinition rightFrontLegDown = rightFrontLeg.addOrReplaceChild("rightFrontLegDown", CubeListBuilder.create().texOffs(77, 16).mirror().addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 5.0F, -3.0F));

        PartDefinition rightFrontHoove = rightFrontLegDown.addOrReplaceChild("rightFrontHoove", CubeListBuilder.create().texOffs(77, 25).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 5.0F, 1.5F));

        PartDefinition leftBackLegBase = body.addOrReplaceChild("leftBackLegBase", CubeListBuilder.create().texOffs(95, 0).addBox(-2.5F, -4.0F, -3.5F, 5.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 4.0F, 9.6F));

        PartDefinition leftBackLeg = leftBackLegBase.addOrReplaceChild("leftBackLeg", CubeListBuilder.create().texOffs(95, 14).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -1.5F, 0.3927F, 0.0F, 0.0F));

        PartDefinition leftBackLegDown = leftBackLeg.addOrReplaceChild("leftBackLegDown", CubeListBuilder.create().texOffs(95, 24).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 4.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition leftBackHoove = leftBackLegDown.addOrReplaceChild("leftBackHoove", CubeListBuilder.create().texOffs(95, 34).addBox(-1.5F, -0.3007F, -1.9537F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.3007F, -1.5463F));

        PartDefinition rightBackLegBase = body.addOrReplaceChild("rightBackLegBase", CubeListBuilder.create().texOffs(95, 0).mirror().addBox(-2.5F, -4.0F, -3.5F, 5.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.5F, 4.0F, 9.6F));

        PartDefinition rightBackLeg = rightBackLegBase.addOrReplaceChild("rightBackLeg", CubeListBuilder.create().texOffs(95, 14).mirror().addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 3.0F, -1.5F, 0.3927F, 0.0F, 0.0F));

        PartDefinition rightBackLegDown = rightBackLeg.addOrReplaceChild("rightBackLegDown", CubeListBuilder.create().texOffs(95, 24).mirror().addBox(-1.5F, 0.0F, -3.0F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 6.0F, 4.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition rightBackHoove = rightBackLegDown.addOrReplaceChild("rightBackHoove", CubeListBuilder.create().texOffs(95, 34).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 6.0F, -1.5F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(47, 64).addBox(-2.5F, 0.0F, -1.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.5F, 12.0F, 0.6545F, 0.0F, 0.0F));

        PartDefinition tailTip = tail.addOrReplaceChild("tailTip", CubeListBuilder.create().texOffs(63, 64).addBox(-1.5F, 0.0F, -3.5F, 2.0F, 13.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 3.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition wingBase = body.addOrReplaceChild("wingBase", CubeListBuilder.create().texOffs(0, 81).addBox(-6.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 91).addBox(-6.0F, -0.5F, 1.0F, 6.0F, 0.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 91).addBox(-6.0F, 0.5F, 1.0F, 6.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -4.0F, 0.0F, 0.0F, 0.0F, 0.3054F));

        PartDefinition bone = wingBase.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(16, 81).addBox(-12.0F, -2.0F, 0.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 98).addBox(-12.0F, -1.5F, 2.0F, 12.0F, 0.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(0, 98).addBox(-12.0F, -0.5F, 2.0F, 12.0F, 0.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 1.0F, -1.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition bone2 = bone.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(44, 81).addBox(-15.0F, 0.0F, 0.0F, 15.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(50, 91).addBox(-15.0F, 0.5F, 2.0F, 15.0F, 0.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(50, 91).addBox(-15.0F, 1.5F, 2.0F, 15.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.0F, -2.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition bone3 = bone2.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(0, 86).addBox(-30.0F, 0.0F, 0.0F, 30.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(28, 108).addBox(-30.0F, 0.5F, 2.0F, 30.0F, 0.0F, 20.0F, new CubeDeformation(0.0F))
                .texOffs(28, 108).addBox(-30.0F, 1.5F, 2.0F, 30.0F, 0.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.9599F));

        PartDefinition wingBase2 = body.addOrReplaceChild("wingBase2", CubeListBuilder.create().texOffs(0, 81).mirror().addBox(0.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 91).mirror().addBox(0.0F, -0.5F, 1.0F, 6.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 91).mirror().addBox(0.0F, 0.5F, 1.0F, 6.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.0F, -4.0F, 0.0F, 0.0F, 0.0F, -0.3054F));

        PartDefinition bone4 = wingBase2.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(16, 81).mirror().addBox(0.0F, -2.0F, 0.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 98).mirror().addBox(0.0F, -1.5F, 2.0F, 12.0F, 0.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 98).mirror().addBox(0.0F, -0.5F, 2.0F, 12.0F, 0.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.0F, 1.0F, -1.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition bone5 = bone4.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(44, 81).mirror().addBox(0.0F, 0.0F, 0.0F, 15.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(50, 91).mirror().addBox(0.0F, 0.5F, 2.0F, 15.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(50, 91).mirror().addBox(0.0F, 1.5F, 2.0F, 15.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(12.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition bone6 = bone5.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(0, 86).mirror().addBox(0.0F, 0.0F, 0.0F, 30.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(28, 108).mirror().addBox(0.0F, 0.5F, 2.0F, 30.0F, 0.0F, 20.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(28, 108).mirror().addBox(0.0F, 1.5F, 2.0F, 30.0F, 0.0F, 20.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(15.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.9599F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public ModelPartHandler getHandler() {
        return this.model;
    }

    @Override
    public void setupAnim(Pegasus entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.getMainPart().render(poseStack, buffer, packedLight, packedLight, red, green, blue, alpha);
    }
}
