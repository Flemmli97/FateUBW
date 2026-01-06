package io.github.flemmli97.fateubw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.summons.Pegasus;
import io.github.flemmli97.fateubw.common.entity.utils.MoveType;
import io.github.flemmli97.tenshilib.client.data.GeoAnimationManager;
import io.github.flemmli97.tenshilib.client.data.GeoModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.BedrockAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedEntityModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.client.model.RideableModel;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class PegasusModel extends ExtendedEntityModel<Pegasus> implements RideableModel<Pegasus> {

    public static final ResourceLocation LOCATION = Fate.modRes("pegasus");

    protected final ReloadableCache<ModelPartsContainer> model;
    protected final ReloadableCache<BedrockAnimations> anim;

    public ModelPartsContainer.ModelPartExtended head;
    public ModelPartsContainer.ModelPartExtended ridingPosition;

    public PegasusModel() {
        super();
        this.model = GeoModelManager.getInstance().getModel(LOCATION, model -> {
            this.head = model.getPart("head");
            this.ridingPosition = model.getPart("mountPos");
        });
        this.anim = GeoAnimationManager.getInstance().getAnimation(LOCATION);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.getModel().getRoot().render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    @Override
    public void setupAnim(Pegasus entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getModel().resetPoses();
        this.head.yRot += netHeadYaw * Mth.DEG_TO_RAD * 0.3f;
        this.head.xRot += headPitch * Mth.DEG_TO_RAD * 0.1f;
        float partialTick = this.getPartialTick();
        this.anim.get().setVariable("x_rotation", entity::getXRot);
        if (entity.deathTime <= 0) {
            this.anim.get().doAnimation(this, "idle", entity.tickCount, partialTick);
            this.anim.get().doAnimation(this, "walk", entity.tickCount, partialTick, entity.interpolatedMoveTick(partialTick));
            this.anim.get().doAnimation(this, "run", entity.tickCount, partialTick, entity.interpolatedMoveTickOf(MoveType.RUN, partialTick));
            this.anim.get().doAnimation(this, "fly", entity.tickCount, partialTick, entity.interpolatedMoveTickOf(MoveType.FLY, partialTick));
        }
        this.anim.get().doAnimation(this, entity.getAnimationHandler(), partialTick);
    }

    @Override
    public ModelPartsContainer getModel() {
        return this.model.get();
    }

    @Override
    public boolean transform(Pegasus entity, EntityRenderer<Pegasus> entityRenderer, Entity rider, EntityRenderer<?> ridingEntityRenderer, PoseStack stack, int riderNum) {
        this.ridingPosition.translateAndRotateWithParents(stack);
        AnimationState animation = entity.getAnimationHandler().getAnimation();
        if (animation == null || !animation.is(Pegasus.SUMMON) || animation.isPast("seated"))
            translateRider(stack, entity, rider);
        return true;
    }

    public static void translateRider(PoseStack poseStack, LivingEntity entity, Entity rider) {
        Vec3 attach = rider.getVehicleAttachmentPoint(entity);
        float scale = entity.getScale();
        poseStack.scale(1 / scale, 1 / scale, 1 / scale);
        poseStack.translate(attach.x(), attach.y(), attach.z());
    }
}