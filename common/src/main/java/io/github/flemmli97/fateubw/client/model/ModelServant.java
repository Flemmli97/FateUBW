package io.github.flemmli97.fateubw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.render.ServantRenderer;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.client.AnimationManager;
import io.github.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

import java.util.ArrayList;
import java.util.HashMap;

public class ModelServant<T extends BaseServant & IAnimated> extends EntityModel<T> implements IArmModel, HeadedModel, IPreRenderUpdate<T>, ExtendedModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Fate.MODID, "servant"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelPartHandler.ModelPartExtended servantHead;
    public ModelPartHandler.ModelPartExtended servantBody;

    public ModelPartHandler.ModelPartExtended servantRightArmUp;
    public ModelPartHandler.ModelPartExtended servantRightArmJoint;
    public ModelPartHandler.ModelPartExtended servantRightArmDown;

    public ModelPartHandler.ModelPartExtended servantLeftArmUp;
    public ModelPartHandler.ModelPartExtended servantLeftArmJoint;
    public ModelPartHandler.ModelPartExtended servantLeftArmDown;

    public ModelPartHandler.ModelPartExtended servantRightLegUp;
    public ModelPartHandler.ModelPartExtended servantRightLegDown;

    public ModelPartHandler.ModelPartExtended servantLeftLegUp;
    public ModelPartHandler.ModelPartExtended servantLeftLegDown;

    protected final ModelPart dummyHead = new ModelPart(new ArrayList<>(), new HashMap<>());

    public int heldItemMain, heldItemOff;
    protected boolean show;

    public ModelServant(ModelPart root, String animFileName) {
        super(RenderType::entityTranslucent);
        this.model = new ModelPartHandler(root);
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(Fate.MODID, animFileName));
        this.servantHead = this.model.getPart("servantHead");
        this.servantBody = this.model.getPart("servantBody");
        this.servantRightArmUp = this.model.getPart("servantRightArmUp");
        this.servantRightArmJoint = this.model.getPart("servantRightArmJoint");
        this.servantRightArmDown = this.model.getPart("servantRightArmDown");
        this.servantLeftArmUp = this.model.getPart("servantLeftArmUp");
        this.servantLeftArmJoint = this.model.getPart("servantLeftArmJoint");
        this.servantLeftArmDown = this.model.getPart("servantLeftArmDown");
        this.servantRightLegUp = this.model.getPart("servantRightLegUp");
        this.servantRightLegDown = this.model.getPart("servantRightLegDown");
        this.servantLeftLegUp = this.model.getPart("servantLeftLegUp");
        this.servantLeftLegDown = this.model.getPart("servantLeftLegDown");
    }

    public static MeshDefinition mesh(CubeDeformation deform) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition servantBody = partdefinition.addOrReplaceChild("servantBody", CubeListBuilder.create().texOffs(16, 16).mirror().addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, deform).mirror(false)
                .texOffs(16, 32).mirror().addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, deform.extend(0.25f)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition servantHead = servantBody.addOrReplaceChild("servantHead", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deform).mirror(false)
                .texOffs(32, 0).mirror().addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deform.extend(0.25f)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition servantLeftArmUp = servantBody.addOrReplaceChild("servantLeftArmUp", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform)
                .texOffs(40, 32).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform.extend(0.25f)), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition servantLeftArmJoint = servantLeftArmUp.addOrReplaceChild("servantLeftArmJoint", CubeListBuilder.create(), PartPose.offset(3.0F, 4.0F, 0.0F));

        PartDefinition servantLeftArmDown = servantLeftArmJoint.addOrReplaceChild("servantLeftArmDown", CubeListBuilder.create().texOffs(32, 54).mirror().addBox(-4.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform).mirror(false)
                .texOffs(48, 54).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform.extend(0.25f)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition servantRightArmUp = servantBody.addOrReplaceChild("servantRightArmUp", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-3.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform).mirror(false)
                .texOffs(40, 32).mirror().addBox(-3.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform.extend(0.25f)).mirror(false), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition servantRightArmJoint = servantRightArmUp.addOrReplaceChild("servantRightArmJoint", CubeListBuilder.create(), PartPose.offset(-3.0F, 4.0F, 0.0F));

        PartDefinition servantRightArmDown = servantRightArmJoint.addOrReplaceChild("servantRightArmDown", CubeListBuilder.create().texOffs(32, 54).mirror().addBox(0.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform).mirror(false)
                .texOffs(48, 54).mirror().addBox(0.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform.extend(0.25f)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition servantLeftLegUp = servantBody.addOrReplaceChild("servantLeftLegUp", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform)
                .texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform.extend(0.25f)), PartPose.offset(2.0F, 12.0F, 0.0F));

        PartDefinition servantLeftLegDown = servantLeftLegUp.addOrReplaceChild("servantLeftLegDown", CubeListBuilder.create().texOffs(16, 54).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, deform)
                .texOffs(0, 54).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, deform.extend(0.25f)), PartPose.offset(0.0F, 6.0F, -2.0F));

        PartDefinition servantRightLegUp = servantBody.addOrReplaceChild("servantRightLegUp", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform).mirror(false)
                .texOffs(0, 32).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deform.extend(0.25f)).mirror(false), PartPose.offset(-2.0F, 12.0F, 0.0F));

        PartDefinition servantRightLegDown = servantRightLegUp.addOrReplaceChild("servantRightLegDown", CubeListBuilder.create().texOffs(16, 54).mirror().addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, deform).mirror(false)
                .texOffs(0, 54).mirror().addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, deform.extend(0.25f)).mirror(false), PartPose.offset(0.0F, 6.0F, -2.0F));

        return meshdefinition;
    }

    public static LayerDefinition createBodyLayer(CubeDeformation deform) {
        return LayerDefinition.create(mesh(deform), 64, 64);
    }

    @Override
    public ModelPartHandler.ModelPartExtended getHand(InteractionHand side) {
        return side == InteractionHand.MAIN_HAND ? this.servantRightArmUp : this.servantLeftArmUp;
    }

    @Override
    public void update(T obj) {
        this.show = ServantRenderer.showIdentity(obj);
        this.heldItemMain = this.show || obj.getMainHandItem().isEmpty() ? 0 : 1;
        this.heldItemOff = this.show || obj.getOffhandItem().isEmpty() ? 0 : 1;
    }

    @Override
    public ModelPartHandler getHandler() {
        return this.model;
    }

    @Override
    public void transform(HumanoidArm humanoidArm, PoseStack poseStack) {
        if (humanoidArm == HumanoidArm.LEFT) {
            this.rotate(poseStack, this.servantBody, this.servantLeftArmUp, this.servantLeftArmJoint, this.servantLeftArmDown);
        } else {
            this.rotate(poseStack, this.servantBody, this.servantRightArmUp, this.servantRightArmJoint, this.servantRightArmDown);
        }
    }

    @Override
    public void postTransform(boolean leftSide, PoseStack stack) {
        stack.translate(leftSide ? 0.125 : -0.125, 0.125, -6 / 16d);
    }

    protected void rotate(PoseStack stack, ModelPartHandler.ModelPartExtended... models) {
        for (ModelPartHandler.ModelPartExtended render : models)
            render.translateAndRotate(stack);
    }

    @Override
    public void setupAnim(T servant, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.preAnimSetup(servant, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if (this.show) {
            float partialTicks = Minecraft.getInstance().getFrameTime();
            if (servant.isStaying()) {
                this.anim.doAnimation(this, "stay", servant.tickCount, partialTicks);
            } else {
                AnimatedAction anim = servant.getAnimationHandler().getAnimation();
                if (anim != null)
                    this.anim.doAnimation(this, anim.getID(), anim.getTick(), partialTicks);
            }
        }
    }

    public void preAnimSetup(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        this.servantHead.yRot = netHeadYaw / (180F / (float) Math.PI);
        this.servantHead.xRot = headPitch / (180F / (float) Math.PI);

        this.servantRightArmUp.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        this.servantLeftArmUp.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        this.servantRightArmUp.zRot = 0;
        this.servantLeftArmUp.zRot = 0;
        this.servantRightLegUp.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.servantLeftLegUp.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.servantRightLegUp.yRot = 0;
        this.servantLeftLegUp.yRot = 0;

        if (this.riding) {
            this.servantRightArmUp.xRot += -((float) Math.PI / 5F);
            this.servantLeftArmUp.xRot += -((float) Math.PI / 5F);
            this.servantRightLegUp.xRot = -((float) Math.PI * 2F / 5F);
            this.servantLeftLegUp.xRot = -((float) Math.PI * 2F / 5F);
            this.servantRightLegUp.yRot = ((float) Math.PI / 10F);
            this.servantLeftLegUp.yRot = -((float) Math.PI / 10F);
        }

        if (this.heldItemOff == 1)
            this.servantLeftArmUp.xRot = this.servantLeftArmUp.xRot * 0.5F - ((float) Math.PI / 10F);
        if (this.heldItemMain == 1)
            this.servantRightArmUp.xRot = this.servantRightArmUp.xRot * 0.5F - ((float) Math.PI / 10F);

        this.servantRightArmUp.yRot = 0;
        this.servantLeftArmUp.yRot = 0;
        if (this.attackTime > -9990) {
            float swingProgress = this.attackTime;
            this.servantBody.yRot = Mth.sin(Mth.sqrt(swingProgress) * (float) Math.PI * 2.0F) * 0.2F;
            this.servantRightArmUp.yRot += this.servantBody.yRot;
            this.servantLeftArmUp.yRot += this.servantBody.yRot;
            this.servantLeftArmUp.xRot += this.servantBody.yRot;
            swingProgress = 1.0F - this.attackTime;
            swingProgress *= swingProgress;
            swingProgress *= swingProgress;
            swingProgress = 1.0F - swingProgress;
            float var9 = Mth.sin(swingProgress * (float) Math.PI);
            float var10 = Mth.sin(this.attackTime * (float) Math.PI) * -(this.servantHead.xRot - 0.7F) * 0.75F;
            this.servantRightArmUp.xRot = (float) ((double) this.servantRightArmUp.xRot - ((double) var9 * 1.2D + (double) var10));
            this.servantRightArmUp.yRot += this.servantBody.yRot * 2.0F;
            this.servantRightArmUp.zRot = Mth.sin(this.attackTime * (float) Math.PI) * -0.4F;
        }

        this.servantBody.xRot = 0;

        this.servantRightArmUp.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.servantLeftArmUp.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.servantRightArmUp.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
        this.servantLeftArmUp.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;
    }

    @Override
    public ModelPart getHead() {
        this.dummyHead.loadPose(this.servantHead.storePose());
        return this.dummyHead;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.servantBody.render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void copyPropertiesTo(EntityModel<T> model) {
        super.copyPropertiesTo(model);
        if (model instanceof ModelServant<?>) {
            ModelServant<?> other = (ModelServant<?>) model;
            this.show = other.show;
            this.heldItemMain = other.heldItemMain;
            this.heldItemOff = other.heldItemOff;
        }
    }
}
