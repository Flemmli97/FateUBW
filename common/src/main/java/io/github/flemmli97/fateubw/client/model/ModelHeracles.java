package io.github.flemmli97.fateubw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.servant.EntityHeracles;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.client.AnimationManager;
import io.github.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import io.github.flemmli97.tenshilib.client.model.ModelPartHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

import java.util.ArrayList;
import java.util.HashMap;

public class ModelHeracles<T extends EntityHeracles & IAnimated> extends BaseServantModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Fate.MODID, "heracles"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public final ModelPartHandler.ModelPartExtended head;
    public final ModelPartHandler.ModelPartExtended body;
    public final ModelPartHandler.ModelPartExtended bodyUpper;
    public final ModelPartHandler.ModelPartExtended leftArm;
    public final ModelPartHandler.ModelPartExtended leftArmDown;
    public final ModelPartHandler.ModelPartExtended leftItem;
    public final ModelPartHandler.ModelPartExtended rightArm;
    public final ModelPartHandler.ModelPartExtended rightArmDown;
    public final ModelPartHandler.ModelPartExtended rightItem;

    protected final ModelPart dummyHead = new ModelPart(new ArrayList<>(), new HashMap<>());

    public int heldItemMain, heldItemOff;

    public ModelHeracles(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.model = new ModelPartHandler(root);
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(Fate.MODID, "heracles"));
        this.head = this.model.getPart("head");
        this.body = this.model.getPart("body");
        this.bodyUpper = this.model.getPart("bodyUpper");
        this.leftArm = this.model.getPart("leftArm");
        this.leftArmDown = this.model.getPart("leftArmDown");
        this.leftItem = this.model.getPart("LeftItem");
        this.rightArm = this.model.getPart("rightArm");
        this.rightArmDown = this.model.getPart("rightArmDown");
        this.rightItem = this.model.getPart("RightItem");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(32, 29).addBox(-5.0F, -3.4F, -2.5F, 10.0F, 10.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-6.0F, 2.6F, -3.0F, 12.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.4F, 0.0F));

        PartDefinition bodyUpper = body.addOrReplaceChild("bodyUpper", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -9.0F, -3.0F, 12.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.4F, -0.5F));

        PartDefinition head = bodyUpper.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 29).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 0.0F));

        PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(44, 22).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.2F, -3.5F, -0.4294F, 0.0F, 0.0F));

        PartDefinition hair1 = head.addOrReplaceChild("hair1", CubeListBuilder.create().texOffs(38, 0).addBox(0.0F, -5.5F, -5.5F, 0.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.5F, 2.0F));

        PartDefinition hair2 = head.addOrReplaceChild("hair2", CubeListBuilder.create().texOffs(32, 44).addBox(0.0F, -5.5F, -5.5F, 0.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.2F, -5.5F, 2.0F));

        PartDefinition hair3 = head.addOrReplaceChild("hair3", CubeListBuilder.create().texOffs(0, 45).addBox(0.0F, -5.5F, -5.5F, 0.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(1.2F, -5.5F, 2.0F));

        PartDefinition hair4 = head.addOrReplaceChild("hair4", CubeListBuilder.create().texOffs(54, 44).addBox(0.0F, -5.5F, -5.5F, 0.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.4F, -5.5F, 2.0F));

        PartDefinition hair5 = head.addOrReplaceChild("hair5", CubeListBuilder.create().texOffs(60, 0).addBox(0.0F, -5.5F, -5.5F, 0.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(2.4F, -5.5F, 2.0F));

        PartDefinition hair6 = head.addOrReplaceChild("hair6", CubeListBuilder.create().texOffs(62, 22).addBox(0.0F, -5.5F, -5.5F, 0.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -5.5F, 2.0F, 0.0F, 0.2276F, 0.0F));

        PartDefinition hair7 = head.addOrReplaceChild("hair7", CubeListBuilder.create().texOffs(22, 66).addBox(0.0F, -5.5F, -5.5F, 0.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -5.5F, 2.0F, 0.0F, -0.2276F, 0.0F));

        PartDefinition leftArm = bodyUpper.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(44, 66).addBox(-0.5F, -2.5F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 67).addBox(0.0F, 3.5F, -3.0F, 5.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, -5.5F, 0.5F));

        PartDefinition leftArmDown = leftArm.addOrReplaceChild("leftArmDown", CubeListBuilder.create().texOffs(44, 78).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, 7.5F, 0.0F));

        PartDefinition leftArmArmor = leftArmDown.addOrReplaceChild("leftArmArmor", CubeListBuilder.create().texOffs(22, 45).addBox(-1.0F, -7.0F, -1.0F, 1.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

        PartDefinition LeftItem = leftArmDown.addOrReplaceChild("LeftItem", CubeListBuilder.create(), PartPose.offset(-2.0F, 6.5F, 0.0F));

        PartDefinition rightArm = bodyUpper.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(68, 66).addBox(-5.5F, -2.5F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(76, 44).addBox(-5.0F, 3.5F, -3.0F, 5.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -5.5F, 0.5F));

        PartDefinition rightArmDown = rightArm.addOrReplaceChild("rightArmDown", CubeListBuilder.create().texOffs(60, 78).addBox(0.0F, -1.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.5F, 7.5F, 0.0F));

        PartDefinition rightArmArmor = rightArmDown.addOrReplaceChild("rightArmArmor", CubeListBuilder.create().texOffs(22, 54).addBox(0.0F, -7.0F, -1.0F, 1.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition RightItem = rightArmDown.addOrReplaceChild("RightItem", CubeListBuilder.create(), PartPose.offset(2.0F, 6.5F, 0.0F));

        PartDefinition bodyLower = body.addOrReplaceChild("bodyLower", CubeListBuilder.create(), PartPose.offset(0.0F, 2.6F, 0.0F));

        PartDefinition leftLeg = body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(76, 54).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(28, 45).addBox(-0.5F, 0.0F, -2.9F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 6.6F, 0.0F));

        PartDefinition leftLegDown = leftLeg.addOrReplaceChild("leftLegDown", CubeListBuilder.create().texOffs(82, 8).addBox(-2.0F, 1.0F, 0.5F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, -2.5F));

        PartDefinition leftShinLower = leftLegDown.addOrReplaceChild("leftShinLower", CubeListBuilder.create().texOffs(36, 22).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -0.4F, 0.4363F, 0.0F, 0.0F));

        PartDefinition leftFoot = leftLegDown.addOrReplaceChild("leftFoot", CubeListBuilder.create().texOffs(76, 78).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 6.0F, 2.5F));

        PartDefinition rightLeg = body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 77).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(28, 53).addBox(-0.5F, 0.0F, -2.9F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 6.6F, 0.0F));

        PartDefinition rightLegDown = rightLeg.addOrReplaceChild("rightLegDown", CubeListBuilder.create().texOffs(84, 18).addBox(-2.0F, 1.0F, 0.5F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, -2.5F));

        PartDefinition rightShinLower = rightLegDown.addOrReplaceChild("rightShinLower", CubeListBuilder.create().texOffs(40, 22).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -0.4F, 0.4363F, 0.0F, 0.0F));

        PartDefinition rightFoot = rightLegDown.addOrReplaceChild("rightFoot", CubeListBuilder.create().texOffs(82, 0).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 6.0F, 2.5F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public ModelPartHandler.ModelPartExtended getHand(InteractionHand side) {
        return side == InteractionHand.MAIN_HAND ? this.rightArm : this.leftArm;
    }

    @Override
    public void update(T obj) {
        this.heldItemMain = obj.getMainHandItem().isEmpty() ? 0 : 1;
        this.heldItemOff = obj.getOffhandItem().isEmpty() ? 0 : 1;
    }

    @Override
    public ModelPartHandler getHandler() {
        return this.model;
    }

    @Override
    public void transform(HumanoidArm humanoidArm, PoseStack poseStack) {
        if (humanoidArm == HumanoidArm.LEFT) {
            this.rotate(poseStack, this.body, this.bodyUpper, this.leftArm, this.leftArmDown, this.leftItem);
        } else {
            this.rotate(poseStack, this.body, this.bodyUpper, this.rightArm, this.rightArmDown, this.rightItem);
        }
    }

    @Override
    public void postTransform(boolean leftSide, PoseStack stack) {
    }

    protected void rotate(PoseStack stack, ModelPartHandler.ModelPartExtended... models) {
        for (ModelPartHandler.ModelPartExtended render : models)
            render.translateAndRotate(stack);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        float partialTicks = Minecraft.getInstance().getFrameTime();
        this.anim.doAnimation(this, "idle", entity.tickCount, partialTicks, 1);
        if (limbSwing > 0)
            this.anim.doAnimation(this, "walk", entity.tickCount, partialTicks, 1);
        if (entity.isStaying()) {
            this.anim.doAnimation(this, "stay", entity.tickCount, partialTicks);
        } else {
            this.anim.doAnimation(this, entity.getAnimationHandler(), partialTicks, entity.flipAnimation());
        }
    }

    @Override
    public ModelPart getHead() {
        this.dummyHead.loadPose(this.head.storePose());
        return this.dummyHead;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(poseStack, buffer, packedLight, packedOverlay);
    }
}
