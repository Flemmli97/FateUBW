package io.github.flemmli97.fateubw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.entity.utils.MoveType;
import io.github.flemmli97.tenshilib.client.data.GeoAnimationManager;
import io.github.flemmli97.tenshilib.client.data.GeoModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.BedrockAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.ItemHolderModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.client.model.PoseExtended;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;

public class ServantModel<T extends LivingEntity & AnimatedEntity> extends EntityModel<T> implements ItemHolderModel, HeadedModel, IPreRenderUpdate<T>, ExtendedModel {

    protected final ReloadableCache<ModelPartsContainer> model;
    protected final ReloadableCache<BedrockAnimations> animation;

    public ModelPartsContainer.ModelPartExtended head;
    public ModelPartsContainer.ModelPartExtended body;

    public ModelPartsContainer.ModelPartExtended rightItem;
    public ModelPartsContainer.ModelPartExtended leftItem;

    @Nullable
    public ModelPartsContainer.ModelPartExtended rightArm;
    @Nullable
    public ModelPartsContainer.ModelPartExtended leftArm;
    @Nullable
    public ModelPartsContainer.ModelPartExtended rightLeg;
    @Nullable
    public ModelPartsContainer.ModelPartExtended leftLeg;

    @Nullable
    public ModelPartsContainer.ModelPartExtended vehicleAttachment;
    @Nullable
    private Vector3f bodyVehicleOffset;
    @Nullable
    public ModelPartsContainer.ModelPartExtended leftItemDetached;
    @Nullable
    public ModelPartsContainer.ModelPartExtended rightItemDetached;

    protected final ModelPart dummyHead = new ModelPart(new ArrayList<>(), new HashMap<>());

    public int heldItemMain, heldItemOff;

    public ServantModel(ResourceLocation location) {
        this(location, location);
    }

    public ServantModel(ResourceLocation modelLocation, ResourceLocation animationLocation) {
        super(RenderType::entityTranslucent);
        this.model = GeoModelManager.getInstance().getModel(modelLocation, model -> {
            this.head = model.getPart("Head");
            this.body = model.getPart("Body");
            this.rightItem = model.getPart("RightItem");
            this.leftItem = model.getPart("LeftItem");

            this.rightArm = model.getOptionalPart("RightArm").orElse(null);
            this.leftArm = model.getOptionalPart("LeftArm").orElse(null);
            this.rightLeg = model.getOptionalPart("RightLeg").orElse(null);
            this.leftLeg = model.getOptionalPart("LeftLeg").orElse(null);

            this.vehicleAttachment = model.getOptionalPart("VehicleAttachment").orElse(null);
            if (this.vehicleAttachment != null) {
                this.vehicleAttachment.updateDefaultPose(this.vehicleAttachment.getDefaultPose().withScale(0, 0, 0));
                PoseExtended bodyPose = this.body.getDefaultPose();
                PoseExtended attachmentPose = this.vehicleAttachment.getDefaultPose();
                this.bodyVehicleOffset = new Vector3f(attachmentPose.x - bodyPose.x, attachmentPose.y - bodyPose.y, attachmentPose.z - bodyPose.z);
            }
            this.leftItemDetached = model.getOptionalPart("LeftItemStandalone").orElse(null);
            if (this.leftItemDetached != null) {
                this.leftItemDetached.updateDefaultPose(this.leftItemDetached.getDefaultPose().withScale(0, 0, 0));
            }
            this.rightItemDetached = model.getOptionalPart("RightItemStandalone").orElse(null);
            if (this.rightItemDetached != null) {
                this.rightItemDetached.updateDefaultPose(this.rightItemDetached.getDefaultPose().withScale(0, 0, 0));
            }
            this.modelReloadListener(model);
        });
        this.animation = GeoAnimationManager.getInstance().getAnimation(animationLocation);
    }

    protected void modelReloadListener(ModelPartsContainer model) {
    }

    @Override
    public void update(T obj) {
        this.heldItemMain = obj.getMainHandItem().isEmpty() ? 0 : 1;
        this.heldItemOff = obj.getOffhandItem().isEmpty() ? 0 : 1;
    }

    @Override
    public ModelPartsContainer getModel() {
        return this.model.get();
    }

    @Override
    public void transform(HumanoidArm hand, PoseStack stack) {
        if (hand == HumanoidArm.LEFT) {
            boolean detached = this.leftItemDetached != null && this.leftItemDetached.xScale != 0 && this.leftItemDetached.yScale != 0
                    && this.leftItemDetached.zScale != 0 && this.leftItemDetached.visible;
            if (detached) {
                this.leftItemDetached.translateAndRotateWithParents(stack);
            } else if (this.leftArm != null) {
                this.leftArm.translateAndRotateWithParents(stack);
            }
        } else {
            boolean detached = this.rightItemDetached != null && this.rightItemDetached.xScale != 0 && this.rightItemDetached.yScale != 0
                    && this.rightItemDetached.zScale != 0 && this.rightItemDetached.visible;
            if (detached) {
                this.rightItemDetached.translateAndRotateWithParents(stack);
            } else if (this.rightArm != null) {
                this.rightArm.translateAndRotateWithParents(stack);
            }
        }
    }

    @Override
    public ModelPart getHead() {
        PoseExtended pose = this.head.extendedPose();
        this.dummyHead.x = pose.x;
        this.dummyHead.y = pose.y;
        this.dummyHead.z = pose.z;
        this.dummyHead.xRot = pose.xRot;
        this.dummyHead.yRot = pose.yRot;
        this.dummyHead.zRot = pose.zRot;
        this.dummyHead.xScale = pose.xScale;
        this.dummyHead.yScale = pose.yScale;
        this.dummyHead.zScale = pose.zScale;
        return this.dummyHead;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float partialTicks = ClientHandler.getPartialTicks();
        this.preAnimSetup(entity, limbSwing, limbSwingAmount, netHeadYaw, headPitch, partialTicks);
        if (entity.isStaying()) {
            this.animation.get().doAnimation(this, "stay", entity.tickCount, partialTicks);
        } else {
            this.animation.get().doAnimation(this, entity.getAnimationHandler(), partialTicks, entity.flipAnimation());
        }

        // Move the body to match the (detached) legs
        if (entity.isPassenger() && entity.getVehicle() != null) {
            this.body.x = this.body.getDefaultPose().x;
            this.body.y = this.body.getDefaultPose().y;
            this.body.z = this.body.getDefaultPose().z;
            PoseStack stack = new PoseStack();
            this.body.translateAndRotate(stack);
            Vector3f v = this.bodyVehicleOffset != null ? new Vector3f(this.bodyVehicleOffset) : new Vector3f();
            v.mulTranspose(stack.last().normal());
            this.body.x += v.x() - this.bodyVehicleOffset.x;
            this.body.y += v.y() - this.bodyVehicleOffset.y;
            this.body.z += v.z() - this.bodyVehicleOffset.z;
        }
    }

    public void preAnimSetup(T entity, float limbSwing, float limbSwingAmount, float netHeadYaw, float headPitch, float partialTicks) {
        this.model.get().resetPoses();

        BedrockAnimations animation = this.animation.get();
        animation.setVariable("query.head_x_rotation", () -> headPitch);
        animation.setVariable("query.head_y_rotation", () -> netHeadYaw);
        animation.setVariable("left_held", () -> this.heldItemOff);
        animation.setVariable("left_arm_x_rot", () -> this.leftArm != null ? this.leftArm.xRot * Mth.RAD_TO_DEG : 0);
        animation.setVariable("right_held", () -> this.heldItemMain);
        animation.setVariable("right_arm_x_rot", () -> this.rightArm != null ? this.rightArm.xRot * Mth.RAD_TO_DEG : 0);

        animation.doAnimation(this, "idle", entity.tickCount, partialTicks, 1, false, true);
        animation.doAnimation(this, "head_look", entity.tickCount, partialTicks, 1, false, true);
        animation.doAnimation(this, "item_holding", entity.tickCount, partialTicks, 1, false, true);

        animation.doAnimation(this, "idle", entity.tickCount, partialTicks, 1);
        boolean defaultedAnimation = animation.doAnimation(this, "walk", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks), false, true);
        defaultedAnimation = defaultedAnimation || animation.doAnimation(this, "run", entity.tickCount, partialTicks, entity.interpolatedMoveTickOf(MoveType.RUN, partialTicks), false, true);
        if (!defaultedAnimation) {
            if (this.rightArm != null) {
                this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
                this.rightArm.zRot = 0;
            }
            if (this.leftArm != null) {
                this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
                this.leftArm.zRot = 0;
            }
            if (this.rightLeg != null) {
                this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
                this.rightLeg.yRot = 0;
            }
            if (this.leftLeg != null) {
                this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
                this.leftLeg.yRot = 0;
            }
        }
        if (entity.isPassenger() && entity.getVehicle() != null) {
            if (this.riding) {
                animation.doAnimation(this, "riding", entity.tickCount, partialTicks, 1, false, true);
            } else {
                animation.doAnimation(this, "riding_standing", entity.tickCount, partialTicks, 1, false, true);
            }
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.model.get().getRoot().render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    @Override
    public void copyPropertiesTo(EntityModel<T> model) {
        super.copyPropertiesTo(model);
        if (model instanceof ServantModel<?> other) {
            this.heldItemMain = other.heldItemMain;
            this.heldItemOff = other.heldItemOff;
        }
    }
}
