package io.github.flemmli97.fateubw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.utils.MoveType;
import io.github.flemmli97.fateubw.common.entity.utils.ServantModelLike;
import io.github.flemmli97.tenshilib.client.data.GeoAnimationManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.BedrockAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedEntityModel;
import io.github.flemmli97.tenshilib.client.model.ItemHolderModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.client.model.PoseExtended;
import io.github.flemmli97.tenshilib.client.model.animation.Animation;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.utils.math.parser.VariableMap;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

public class ServantModel<T extends LivingEntity & AnimatedEntity & ServantModelLike> extends ExtendedEntityModel<T> implements ItemHolderModel, HeadedModel, IPreRenderUpdate<T> {

    public static final ResourceLocation DEFAULT_ANIMATION = Fate.modRes("servant/generic");

    protected final ReloadableCache<BedrockAnimations> defaultAnimations;

    public ModelPartsContainer.ModelPartExtended head;
    public ModelPartsContainer.ModelPartExtended body;

    public ItemPart rightItem;
    public ItemPart leftItem;

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

    protected final ModelPart dummyHead = new ModelPart(List.of(), Map.of());

    public int heldItemMain, heldItemOff;

    private float alpha = -1;

    private float limbSwing, limbSwingAmount;

    public ServantModel(ResourceLocation location) {
        this(location, location);
    }

    public ServantModel(ResourceLocation modelLocation, ResourceLocation animationLocation) {
        super(RenderType::entityTranslucent, modelLocation, animationLocation);
        this.defaultAnimations = GeoAnimationManager.getInstance().getAnimation(DEFAULT_ANIMATION);
    }

    @Override
    protected void onModelReload(ModelPartsContainer model) {
        this.head = model.getPart("Head");
        this.body = model.getPart("Body");
        this.rightItem = new ItemPart(model, "RightItem");
        this.leftItem = new ItemPart(model, "LeftItem");

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
    }

    @Override
    public void update(T obj) {
        this.heldItemMain = obj.getRenderHandStack(InteractionHand.MAIN_HAND).isEmpty() ? 0 : 1;
        this.heldItemOff = obj.getRenderHandStack(InteractionHand.OFF_HAND).isEmpty() ? 0 : 1;
    }

    @Override
    public ModelPartsContainer getModel() {
        return this.model.get();
    }

    @Override
    public void transform(HumanoidArm hand, PoseStack stack) {
        if (hand == HumanoidArm.LEFT) {
            this.leftItem.getPart().translateAndRotateWithParents(stack);
        } else {
            this.rightItem.getPart().translateAndRotateWithParents(stack);
        }
    }

    @Override
    public ModelPart getHead() {
        this.dummyHead.x = this.head.x;
        this.dummyHead.y = this.head.y;
        this.dummyHead.z = this.head.z;
        this.dummyHead.xRot = this.head.xRot;
        this.dummyHead.yRot = this.head.yRot;
        this.dummyHead.zRot = this.head.zRot;
        this.dummyHead.xScale = this.head.xScale;
        this.dummyHead.yScale = this.head.yScale;
        this.dummyHead.zScale = this.head.zScale;
        return this.dummyHead;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getModel().resetPoses();
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        float partialTick = this.getPartialTick();
        this.preAnimSetup(entity, partialTick);
        this.animation.get().doAnimation(this, entity.getAnimationHandler(), partialTick, entity.flipAnimation());

        // Move the body to match the (detached) legs
        // Legacy. Remove once all animations and models are updated
        if (entity.isPassenger() && entity.getVehicle() != null && this.bodyVehicleOffset != null) {
            this.body.x = this.body.getDefaultPose().x;
            this.body.y = this.body.getDefaultPose().y;
            this.body.z = this.body.getDefaultPose().z;
            PoseStack stack = new PoseStack();
            this.body.translateAndRotate(stack);
            if (this.bodyVehicleOffset != null) {
                Vector3f v = new Vector3f(this.bodyVehicleOffset);
                v.mulTranspose(stack.last().normal());
                this.body.x += v.x() - this.bodyVehicleOffset.x;
                this.body.y += v.y() - this.bodyVehicleOffset.y;
                this.body.z += v.z() - this.bodyVehicleOffset.z;
            }
        }
        this.alpha = entity.isDeadOrDying() && entity.getDeathAnimation() == null ?
                Math.max(0.15f, 1 - (entity.deathTime / (float) entity.maxDeathTick())) : -1;

        // Leg animations (and any other defined bones) should override anything
        if (this.bodyVehicleOffset == null) {
            BedrockAnimations animation = this.animation.get();
            if (entity.isPassenger() && entity.getVehicle() != null) {
                if (this.riding) {
                    animation.doAnimation(this, "riding", entity.tickCount, partialTick, 1);
                } else {
                    animation.doAnimation(this, "riding_standing", entity.tickCount, partialTick, 1);
                }
            }
        }
    }

    public void preAnimSetup(T entity, float partialTick) {
        BedrockAnimations animation = this.animation.get();
        BedrockAnimations defaulted = this.defaultAnimations.get();

        if (!animation.has("idle")) {
            defaulted.doAnimation(this, "idle", entity.tickCount, partialTick, 1);
        } else {
            animation.doAnimation(this, "idle", entity.tickCount, partialTick, 1);
        }
        animation.doAnimation(this, "look", entity.tickCount, partialTick, 1);
        if (!animation.has("walk")) {
            defaulted.doAnimation(this, "walk", entity.tickCount, partialTick, 1, false, true);
        } else {
            animation.doAnimation(this, "walk", entity.tickCount, partialTick, entity.interpolatedMoveTick(partialTick));
        }
        animation.doAnimation(this, "run", entity.tickCount, partialTick, entity.interpolatedMoveTickOf(MoveType.RUN, partialTick));
        // Legacy
        if (this.bodyVehicleOffset != null) {
            if (entity.isPassenger() && entity.getVehicle() != null) {
                if (this.riding) {
                    if (animation.has("riding")) {
                        animation.doAnimation(this, "riding", entity.tickCount, partialTick, 1, false, true);
                    } else {
                        defaulted.doAnimation(this, "riding", entity.tickCount, partialTick, 1, false, true);
                    }
                } else {
                    if (animation.has("riding_standing")) {
                        animation.doAnimation(this, "riding_standing", entity.tickCount, partialTick, 1, false, true);
                    } else {
                        defaulted.doAnimation(this, "riding_standing", entity.tickCount, partialTick, 1, false, true);
                    }
                }
            }
        }
    }

    @Override
    public void onPlayAnimation(AnimationState state, Animation animation, float tick, VariableMap variables) {
        super.onPlayAnimation(state, animation, tick, variables);
        variables.setVariable("left_held", this.heldItemOff);
        variables.setVariable("left_arm_x_rot", this.leftArm != null ? this.leftArm.xRot * Mth.RAD_TO_DEG : 0);
        variables.setVariable("right_held", this.heldItemMain);
        variables.setVariable("right_arm_x_rot", this.rightArm != null ? this.rightArm.xRot * Mth.RAD_TO_DEG : 0);
        variables.setVariable("limb_swing", this.limbSwing * Mth.RAD_TO_DEG);
        variables.setVariable("limb_swing_amount", this.limbSwingAmount * Mth.RAD_TO_DEG);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        if (this.alpha != -1)
            color = FastColor.ARGB32.color((int) (this.alpha * 255), color);
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, color);
    }

    @Override
    public void copyPropertiesTo(EntityModel<T> model) {
        super.copyPropertiesTo(model);
        if (model instanceof ServantModel<?> other) {
            other.heldItemMain = this.heldItemMain;
            other.heldItemOff = this.heldItemOff;
        }
    }

    public record ItemPart(ModelPartsContainer.ModelPartExtended base,
                           @Nullable ModelPartsContainer.ModelPartExtended swapped,
                           @Nullable ModelPartsContainer.ModelPartExtended detached) {

        public ItemPart(ModelPartsContainer model, String name) {
            this(model.getPart(name), model.getOptionalPart(name + "Swapped").orElse(null),
                    model.getOptionalPart(name + "Standalone").orElse(null));
            this.hidePart(this.swapped());
            this.hidePart(this.detached());
        }

        private void hidePart(ModelPartsContainer.ModelPartExtended part) {
            if (part != null) {
                part.updateDefaultPose(part.getDefaultPose().withScale(0, 0, 0));
            }
        }

        public ModelPartsContainer.ModelPartExtended getPart() {
            if (this.isVisible(this.swapped())) {
                return this.swapped();
            }
            if (this.isVisible(this.detached())) {
                return this.detached();
            }
            return this.base;
        }

        private boolean isVisible(ModelPartsContainer.ModelPartExtended part) {
            return part != null && part.xScale != 0 && part.yScale != 0
                    && part.zScale != 0 && part.visible;
        }
    }
}
