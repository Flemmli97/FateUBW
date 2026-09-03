package io.github.flemmli97.fateubw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.summons.GordiusWheel;
import io.github.flemmli97.fateubw.common.entity.utils.MoveType;
import io.github.flemmli97.tenshilib.client.model.ExtendedEntityModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.client.model.RideableModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class GordiusWheelModel extends ExtendedEntityModel<GordiusWheel> implements RideableModel<GordiusWheel> {

    public static final ResourceLocation LOCATION = Fate.modRes("gordius_wheel");

    public ModelPartsContainer.ModelPartExtended bull1;
    public ModelPartsContainer.ModelPartExtended bull2;
    public ModelPartsContainer.ModelPartExtended couplerBase;
    public ModelPartsContainer.ModelPartExtended chariotBase;
    public ModelPartsContainer.ModelPartExtended ridingPosition;

    public GordiusWheelModel() {
        super(LOCATION, LOCATION);
    }

    @Override
    protected void onModelReload(ModelPartsContainer model) {
        this.bull1 = model.getPart("bull1");
        this.bull2 = model.getPart("bull2");
        this.couplerBase = model.getPart("couplerBase");
        this.chariotBase = model.getPart("chariotBase");
        this.ridingPosition = model.getPart("mountPos");
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.getModel().getRoot().render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    @Override
    public void setupAnim(GordiusWheel entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getModel().resetPoses();
        float partialTick = this.getPartialTick();
        if (entity.deathTime <= 0) {
            this.animation.get().doAnimation(this, "idle", entity.tickCount, partialTick);
            this.animation.get().doAnimation(this, "walk", entity.tickCount, partialTick, entity.interpolatedMoveTick(partialTick));
            this.animation.get().doAnimation(this, "run", entity.tickCount, partialTick, entity.interpolatedMoveTickOf(MoveType.RUN, partialTick));
            if (entity.getMoveType() != MoveType.NONE)
                entity.wheelPartial = partialTick;
            this.animation.get().doAnimation(this, "wheel_move", entity.wheelMoveTick + entity.wheelPartial
                    * Mth.clamp(entity.walkAnimation.speed(partialTick) / 0.25f, 0, 1), 1, false, false);
        }
        this.animation.get().doAnimation(this, entity.getAnimationHandler(), partialTick);

        if (entity.getWheelEntity() != null) {
            float yRot = lerpClamped(partialTick, entity.getWheelEntity().yRotO, entity.getWheelEntity().getYRot());
            float pYRot = lerpClamped(partialTick, entity.yRotO, entity.getYRot());
            this.couplerBase.yRot -= pYRot * Mth.DEG_TO_RAD;
            this.couplerBase.yRot += yRot * Mth.DEG_TO_RAD;
            float xRot = Mth.lerp(partialTick, entity.getWheelEntity().xRotO, entity.getWheelEntity().getXRot());
            float chariotX = Mth.clamp(xRot, -60, 60);
            this.chariotBase.xRot += chariotX * Mth.DEG_TO_RAD;
            this.ridingPosition.xRot -= (chariotX * 0.75f) * Mth.DEG_TO_RAD;
        }
    }

    private static float lerpClamped(float partialTick, float start, float end) {
        while (start < 0) {
            start += 360;
        }
        while (end < 0) {
            end += 360;
        }
        start = start % 360;
        end = end % 360;
        float diff1 = end - start;
        float diff2 = (Math.min(start, end) + 360) - Math.max(start, end);
        if (Math.abs(diff2) > Math.abs(diff1)) {
            return start + partialTick * diff1;
        }
        return start + partialTick * diff2;
    }

    @Override
    public ModelPartsContainer getModel() {
        return this.model.get();
    }

    @Override
    public boolean transform(GordiusWheel entity, EntityRenderer<GordiusWheel> entityRenderer, Entity rider, EntityRenderer<?> ridingEntityRenderer, PoseStack stack, int riderNum) {
        this.ridingPosition.translateAndRotateWithParents(stack);
        translateRider(stack, entity, rider);
        return true;
    }

    public static void translateRider(PoseStack poseStack, LivingEntity entity, Entity rider) {
        Vec3 attach = rider.getVehicleAttachmentPoint(entity);
        float scale = entity.getScale();
        poseStack.scale(1 / scale, 1 / scale, 1 / scale);
        poseStack.translate(attach.x(), 0, attach.z());
    }
}