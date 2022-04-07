package io.github.flemmli97.fateubw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.servant.EntityMedea;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

public class ModelMedea<T extends EntityMedea & IAnimated> extends ModelServant<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Fate.MODID, "medea"), "main");

    private ModelPartHandler.ModelPartExtended body;
    private ModelPartHandler.ModelPartExtended head;
    private ModelPartHandler.ModelPartExtended leftArm;
    private ModelPartHandler.ModelPartExtended rightArm;

    public ModelMedea(ModelPart root) {
        super(root, "medea");
        this.body = this.model.getPart("body");
        this.head = this.model.getPart("head");
        this.leftArm = this.model.getPart("leftArm");
        this.rightArm = this.model.getPart("rightArm");
        this.servantBody.visible = false;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = ModelServant.mesh(new CubeDeformation(0));
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition hat = body.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition leftLeg = body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.9F, 12.0F, 0.0F));

        PartDefinition rightLeg = body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cloakBody = body.addOrReplaceChild("cloakBody", CubeListBuilder.create().texOffs(0, 54).addBox(0.0F, 0.0F, 0.0F, 17.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.5F, -0.2F, -2.5F));

        PartDefinition cloakLegs = body.addOrReplaceChild("cloakLegs", CubeListBuilder.create().texOffs(34, 36).addBox(0.0F, 0.0F, 0.0F, 9.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.5F, 12.8F, -2.5F));

        return LayerDefinition.create(meshdefinition, 64, 72);
    }

    @Override
    public ModelPartHandler.ModelPartExtended getHand(InteractionHand side) {
        return side == InteractionHand.MAIN_HAND ? this.rightArm : this.leftArm;
    }

    @Override
    public ModelPartHandler getHandler() {
        return this.model;
    }

    @Override
    public void transform(HumanoidArm humanoidArm, PoseStack poseStack) {
        if (humanoidArm == HumanoidArm.LEFT) {
            this.rotate(poseStack, this.body, this.leftArm);
        } else {
            this.rotate(poseStack, this.body, this.rightArm);
        }
    }

    @Override
    public void postTransform(boolean leftSide, PoseStack stack) {
        stack.translate(0, 4 / 16d, -8 / 16d);
    }

    @Override
    public void preAnimSetup(T servant, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
    }

    @Override
    public ModelPart getHead() {
        this.dummyHead.loadPose(this.head.storePose());
        return this.dummyHead;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.body.render(poseStack, buffer, packedLight, packedOverlay);
    }
}
