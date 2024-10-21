package io.github.flemmli97.fateubw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.client.AnimationManager;
import io.github.flemmli97.tenshilib.client.model.BlockBenchAnimations;
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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

import java.util.ArrayList;
import java.util.HashMap;

public class ModelServant<T extends BaseServant & IAnimated> extends BaseServantModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Fate.MODID, "servant"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

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

    protected final ModelPart dummyHead = new ModelPart(new ArrayList<>(), new HashMap<>());

    public int heldItemMain, heldItemOff;

    public ModelServant(ModelPart root, String animFileName) {
        super(RenderType::entityTranslucent);
        this.model = new ModelPartHandler(root);
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(Fate.MODID, animFileName));
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
    }

    public static MeshDefinition mesh(CubeDeformation deform) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(16, 16).mirror().addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(16, 32).mirror().addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(32, 0).mirror().addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition LeftArm = Body.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(40, 32).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition LeftArmDown = LeftArm.addOrReplaceChild("LeftArmDown", CubeListBuilder.create().texOffs(32, 54).mirror().addBox(-4.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(48, 54).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(3.0F, 4.0F, 0.0F));

        PartDefinition LeftItem = LeftArmDown.addOrReplaceChild("LeftItem", CubeListBuilder.create(), PartPose.offset(-2.0F, 3.0F, 0.0F));

        PartDefinition RightArm = Body.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-3.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(40, 32).mirror().addBox(-3.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition RightArmDown = RightArm.addOrReplaceChild("RightArmDown", CubeListBuilder.create().texOffs(32, 54).mirror().addBox(0.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(48, 54).mirror().addBox(0.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(-3.0F, 4.0F, 0.0F));

        PartDefinition RightItem = RightArmDown.addOrReplaceChild("RightItem", CubeListBuilder.create(), PartPose.offset(2.0F, 3.0F, 0.0F));

        PartDefinition LeftLeg = Body.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(2.0F, 12.0F, 0.0F));

        PartDefinition LeftLegDown = LeftLeg.addOrReplaceChild("LeftLegDown", CubeListBuilder.create().texOffs(16, 54).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 54).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 6.0F, -2.0F));

        PartDefinition RightLeg = Body.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 32).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(-2.0F, 12.0F, 0.0F));

        PartDefinition RightLegDown = RightLeg.addOrReplaceChild("RightLegDown", CubeListBuilder.create().texOffs(16, 54).mirror().addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 54).mirror().addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(0.0F, 6.0F, -2.0F));

        return meshdefinition;
    }

    public static LayerDefinition createBodyLayer(CubeDeformation deform) {
        return LayerDefinition.create(mesh(deform), 64, 64);
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
            this.rotate(poseStack, this.body, this.leftArm, this.leftArmDown, this.leftItem);
        } else {
            this.rotate(poseStack, this.body, this.rightArm, this.rightArmDown, this.rightItem);
        }
    }

    @Override
    public void postTransform(boolean leftSide, PoseStack stack) {
        stack.translate(0, 0.125, -3 / 16d);
    }

    protected void rotate(PoseStack stack, ModelPartHandler.ModelPartExtended... models) {
        for (ModelPartHandler.ModelPartExtended render : models)
            render.translateAndRotate(stack);
    }

    @Override
    public void setupAnim(T servant, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.preAnimSetup(servant, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float partialTicks = Minecraft.getInstance().getFrameTime();
        if (servant.isStaying()) {
            this.anim.doAnimation(this, "stay", servant.tickCount, partialTicks);
        } else {
            this.anim.doAnimation(this, servant.getAnimationHandler(), partialTicks);
        }
    }

    public void preAnimSetup(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
        this.head.xRot = headPitch / (180F / (float) Math.PI);

        this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        this.rightArm.zRot = 0;
        this.leftArm.zRot = 0;
        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.rightLeg.yRot = 0;
        this.leftLeg.yRot = 0;

        if (this.riding) {
            this.rightArm.xRot -= ((float) Math.PI / 5F);
            this.leftArm.xRot -= ((float) Math.PI / 5F);
            this.rightLeg.xRot = -((float) Math.PI * 2F / 5F);
            this.leftLeg.xRot = -((float) Math.PI * 2F / 5F);
            this.rightLeg.yRot = ((float) Math.PI / 10F);
            this.leftLeg.yRot = -((float) Math.PI / 10F);
        }

        if (this.heldItemOff == 1)
            this.leftArm.xRot = this.leftArm.xRot * 0.5F - ((float) Math.PI / 10F);
        if (this.heldItemMain == 1)
            this.rightArm.xRot = this.rightArm.xRot * 0.5F - ((float) Math.PI / 10F);

        this.rightArm.yRot = 0;
        this.leftArm.yRot = 0;
        if (this.attackTime > -9990) {
            float swingProgress = this.attackTime;
            this.body.yRot = Mth.sin(Mth.sqrt(swingProgress) * (float) Math.PI * 2.0F) * 0.2F;
            this.rightArm.yRot += this.body.yRot;
            this.leftArm.yRot += this.body.yRot;
            this.leftArm.xRot += this.body.yRot;
            swingProgress = 1.0F - this.attackTime;
            swingProgress *= swingProgress;
            swingProgress *= swingProgress;
            swingProgress = 1.0F - swingProgress;
            float var9 = Mth.sin(swingProgress * (float) Math.PI);
            float var10 = Mth.sin(this.attackTime * (float) Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
            this.rightArm.xRot = (float) ((double) this.rightArm.xRot - ((double) var9 * 1.2D + (double) var10));
            this.rightArm.yRot += this.body.yRot * 2.0F;
            this.rightArm.zRot = Mth.sin(this.attackTime * (float) Math.PI) * -0.4F;
        }

        this.body.xRot = 0;

        this.rightArm.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.leftArm.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.rightArm.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
        this.leftArm.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;
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

    @Override
    public void copyPropertiesTo(EntityModel<T> model) {
        super.copyPropertiesTo(model);
        if (model instanceof ModelServant<?> other) {
            this.heldItemMain = other.heldItemMain;
            this.heldItemOff = other.heldItemOff;
        }
    }
}
