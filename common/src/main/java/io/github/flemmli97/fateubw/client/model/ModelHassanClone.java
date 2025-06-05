package io.github.flemmli97.fateubw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.common.entity.summons.HassanClone;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.client.AnimationManager;
import io.github.flemmli97.tenshilib.client.model.BlockBenchAnimations;
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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

import java.util.ArrayList;
import java.util.HashMap;

public class ModelHassanClone<T extends HassanClone & IAnimated> extends BaseServantModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Fate.MODID, "hassan_clone"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;
    protected final BlockBenchAnimations servantAnim;

    public ModelPartHandler.ModelPartExtended head;
    public ModelPartHandler.ModelPartExtended body;

    public ModelPartHandler.ModelPartExtended rightArm;
    public ModelPartHandler.ModelPartExtended rightArmDown;
    public ModelPartHandler.ModelPartExtended rightItem;

    public ModelPartHandler.ModelPartExtended leftArm;
    public ModelPartHandler.ModelPartExtended leftArmDown;
    public ModelPartHandler.ModelPartExtended leftItem;

    public ModelPartHandler.ModelPartExtended rightLeg;
    public ModelPartHandler.ModelPartExtended rightLegDown;

    public ModelPartHandler.ModelPartExtended leftLeg;
    public ModelPartHandler.ModelPartExtended leftLegDown;

    // All parts are normally children of the body. Sometimes animating that is not ideal though
    public ModelPartHandler.ModelPartExtended ridingLegs;
    public ModelPartHandler.ModelPartExtended leftItemDetached;
    public ModelPartHandler.ModelPartExtended rightItemDetached;

    protected final ModelPart dummyHead = new ModelPart(new ArrayList<>(), new HashMap<>());

    public int heldItemMain, heldItemOff;

    public ModelHassanClone(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.model = new ModelPartHandler(root);
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(Fate.MODID, "hassan"));
        this.servantAnim = AnimationManager.getInstance().getAnimation(new ResourceLocation(Fate.MODID, "humanoid_servant"));
        this.head = this.model.getPart("Head");
        this.body = this.model.getPart("Body");
        this.rightArm = this.model.getPart("RightArm");
        this.rightArmDown = this.model.getPart("RightArmDown");
        this.leftArm = this.model.getPart("LeftArm");
        this.leftArmDown = this.model.getPart("LeftArmDown");
        this.rightLeg = this.model.getPart("RightLeg");
        this.rightLegDown = this.model.getPart("RightLegDown");
        this.leftLeg = this.model.getPart("LeftLeg");
        this.leftLegDown = this.model.getPart("LeftLegDown");

        this.leftItem = this.model.getPart("LeftItem");
        this.rightItem = this.model.getPart("RightItem");

        this.ridingLegs = this.model.getPart("RidingLegs");
        this.ridingLegs.updateDefaultPose(this.ridingLegs.getDefaultPose().withScale(0, 0, 0));
        this.leftItemDetached = this.model.getPart("LeftItemDetached");
        this.leftItemDetached.updateDefaultPose(this.leftItemDetached.getDefaultPose().withScale(0, 0, 0));
        this.rightItemDetached = this.model.getPart("RightItemDetached");
        this.rightItemDetached.updateDefaultPose(this.rightItemDetached.getDefaultPose().withScale(0, 0, 0));
    }

    public static LayerDefinition createBodyLayer() {
        CubeDeformation deform = new CubeDeformation(0);
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, deform)
                .texOffs(32, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, deform.extend(0.5f)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deform)
                .texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deform.extend(0.5f)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition LeftArm = Body.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(32, 16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform)
                .texOffs(24, 32).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform.extend(0.5f)), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition LeftArmDown = LeftArm.addOrReplaceChild("LeftArmDown", CubeListBuilder.create().texOffs(40, 26).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform)
                .texOffs(40, 36).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform.extend(0.5f)), PartPose.offset(3.0F, 4.0F, 0.0F));

        PartDefinition LeftItem = LeftArmDown.addOrReplaceChild("LeftItem", CubeListBuilder.create(), PartPose.offset(-2.0F, 3.0F, 0.0F));

        PartDefinition RightArm = Body.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(24, 42).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform)
                .texOffs(40, 46).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform.extend(0.5f)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition RightArmDown = RightArm.addOrReplaceChild("RightArmDown", CubeListBuilder.create().texOffs(0, 48).addBox(0.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform)
                .texOffs(48, 16).addBox(0.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform.extend(0.5f)), PartPose.offset(-3.0F, 4.0F, 0.0F));

        PartDefinition RightItem = RightArmDown.addOrReplaceChild("RightItem", CubeListBuilder.create(), PartPose.offset(2.0F, 3.0F, 0.0F));

        PartDefinition LeftLeg = Body.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(16, 52).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform)
                .texOffs(56, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform.extend(0.5f)), PartPose.offset(2.0F, 12.0F, 0.0F));

        PartDefinition LeftLegDown = LeftLeg.addOrReplaceChild("LeftLegDown", CubeListBuilder.create().texOffs(56, 26).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, deform)
                .texOffs(32, 56).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, deform.extend(0.5f)), PartPose.offset(0.0F, 6.0F, -2.0F));

        PartDefinition RightLeg = Body.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(56, 36).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform)
                .texOffs(56, 46).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform.extend(0.5f)), PartPose.offset(-2.0F, 12.0F, 0.0F));

        PartDefinition RightLegDown = RightLeg.addOrReplaceChild("RightLegDown", CubeListBuilder.create().texOffs(48, 56).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, deform)
                .texOffs(0, 58).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, deform.extend(0.5f)), PartPose.offset(0.0F, 6.0F, -2.0F));

        PartDefinition RidingLegs = partdefinition.addOrReplaceChild("RidingLegs", CubeListBuilder.create(), PartPose.offset(2.0F, 12.0F, 0.0F));

        PartDefinition LeftLeg2 = RidingLegs.addOrReplaceChild("LeftLeg2", CubeListBuilder.create().texOffs(16, 52).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform)
                .texOffs(56, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform.extend(0.5f)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition LeftLegDown2 = LeftLeg2.addOrReplaceChild("LeftLegDown2", CubeListBuilder.create().texOffs(56, 26).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, deform)
                .texOffs(32, 56).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, deform.extend(0.5f)), PartPose.offset(0.0F, 6.0F, -2.0F));

        PartDefinition RightLeg2 = RidingLegs.addOrReplaceChild("RightLeg2", CubeListBuilder.create().texOffs(56, 36).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform)
                .texOffs(56, 46).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform.extend(0.5f)), PartPose.offset(-4.0F, 0.0F, 0.0F));

        PartDefinition RightLegDown2 = RightLeg2.addOrReplaceChild("RightLegDown2", CubeListBuilder.create().texOffs(48, 56).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, deform)
                .texOffs(0, 58).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, deform.extend(0.5f)), PartPose.offset(0.0F, 6.0F, -2.0F));

        PartDefinition LeftItemDetached = partdefinition.addOrReplaceChild("LeftItemDetached", CubeListBuilder.create(), PartPose.offset(6.0F, 9.0F, 0.0F));

        PartDefinition RightItemDetached = partdefinition.addOrReplaceChild("RightItemDetached", CubeListBuilder.create(), PartPose.offset(-6.0F, 9.0F, -2.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
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
            this.rotate(poseStack, this.body, this.leftArm, this.leftArmDown, this.leftItem);
        } else {
            this.rotate(poseStack, this.body, this.rightArm, this.rightArmDown, this.rightItem);
        }
    }

    @Override
    public void postTransform(boolean leftSide, PoseStack stack) {
        stack.translate(0, 2 / 16d, -3 / 16d);
    }

    protected void rotate(PoseStack stack, ModelPartHandler.ModelPartExtended... models) {
        for (ModelPartHandler.ModelPartExtended render : models)
            render.translateAndRotate(stack);
    }

    @Override
    public void setupAnim(T servant, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float partialTicks = ClientHandler.getPartialTicks();
        this.preAnimSetup(servant, limbSwing, limbSwingAmount, netHeadYaw, headPitch, partialTicks);
        this.anim.doAnimation(this, servant.getAnimationHandler(), partialTicks, false);
        // Move the body to match the (detached) legs
        if (servant.isPassenger() && servant.getVehicle() != null) {
            this.body.x = this.body.getDefaultPose().x;
            this.body.y = this.body.getDefaultPose().y;
            this.body.z = this.body.getDefaultPose().z;
            PoseStack stack = new PoseStack();
            this.body.translateAndRotate(stack);
            float bodyLength = -12;
            Vector3f v = new Vector3f(0, bodyLength, 0);
            v.transform(stack.last().normal());
            this.body.x += v.x();
            this.body.y += v.y() - bodyLength;
            this.body.z += v.z();
        }
    }

    public void preAnimSetup(T entity, float limbSwing, float limbSwingAmount, float netHeadYaw, float headPitch, float partialTicks) {
        this.model.resetPoses();

        this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        this.rightArm.zRot = 0;
        this.leftArm.zRot = 0;
        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.rightLeg.yRot = 0;
        this.leftLeg.yRot = 0;

        this.servantAnim.setVariable("query.head_x_rotation", () -> headPitch);
        this.servantAnim.setVariable("query.head_y_rotation", () -> netHeadYaw);
        this.servantAnim.setVariable("left_held", () -> this.heldItemOff);
        this.servantAnim.setVariable("left_arm_x_rot", () -> this.leftArm.xRot * Mth.RAD_TO_DEG);
        this.servantAnim.setVariable("right_held", () -> this.heldItemMain);
        this.servantAnim.setVariable("right_arm_x_rot", () -> this.rightArm.xRot * Mth.RAD_TO_DEG);

        this.servantAnim.doAnimation(this, "idle", entity.tickCount, partialTicks, 1, false, true);
        this.servantAnim.doAnimation(this, "head_look", entity.tickCount, partialTicks, 1, false, true);
        this.servantAnim.doAnimation(this, "item_holding", entity.tickCount, partialTicks, 1, false, true);
//        this.servantAnim.doAnimation(this, "walk", entity.tickCount, partialTicks, 1, false, true);
//        this.servantAnim.doAnimation(this, "run", entity.tickCount, partialTicks, 1, false, true);
        if (entity.isPassenger() && entity.getVehicle() != null) {
            if (this.riding) {
                this.servantAnim.doAnimation(this, "riding", entity.tickCount, partialTicks, 1, false, true);
            } else {
                this.servantAnim.doAnimation(this, "riding_standing", entity.tickCount, partialTicks, 1, false, true);
            }
        }
    }

    @Override
    public ModelPart getHead() {
        this.dummyHead.loadPose(this.head.storePose());
        return this.dummyHead;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.ridingLegs.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.leftItemDetached.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.rightItemDetached.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void copyPropertiesTo(EntityModel<T> model) {
        super.copyPropertiesTo(model);
        if (model instanceof ModelHassanClone<?> other) {
            this.heldItemMain = other.heldItemMain;
            this.heldItemOff = other.heldItemOff;
        }
    }
}
