package io.github.flemmli97.fateubw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.summons.GordiusWheel;
import io.github.flemmli97.fateubw.common.entity.utils.MoveType;
import io.github.flemmli97.tenshilib.client.data.GeoAnimationManager;
import io.github.flemmli97.tenshilib.client.data.GeoModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.BedrockAnimations;
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

    protected final ReloadableCache<ModelPartsContainer> model;
    protected final ReloadableCache<BedrockAnimations> anim;

    public ModelPartsContainer.ModelPartExtended bull1;
    public ModelPartsContainer.ModelPartExtended bull2;
    public ModelPartsContainer.ModelPartExtended centerBeam;
    public ModelPartsContainer.ModelPartExtended backBeam;
    public ModelPartsContainer.ModelPartExtended ridingPosition;

    public GordiusWheelModel() {
        super();
        this.model = GeoModelManager.getInstance().getModel(LOCATION, model -> {
            this.bull1 = model.getPart("bull1");
            this.bull2 = model.getPart("bull2");
            this.centerBeam = model.getPart("centerBeam");
            this.backBeam = model.getPart("backBeam");
            this.ridingPosition = model.getPart("mountPos");
        });
        this.anim = GeoAnimationManager.getInstance().getAnimation(LOCATION);
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
            this.anim.get().doAnimation(this, "idle", entity.tickCount, partialTick);
            this.anim.get().doAnimation(this, "move", entity.tickCount, partialTick, entity.interpolatedMoveTick(partialTick));
            this.anim.get().doAnimation(this, "run", entity.tickCount, partialTick, entity.interpolatedMoveTickOf(MoveType.RUN, partialTick));
            if (entity.getMoveType() != MoveType.NONE)
                entity.wheelPartial = partialTick;
            this.anim.get().doAnimation(this, "wheel_move", entity.wheelMoveTick, entity.wheelPartial, 1);
        }
        this.anim.get().doAnimation(this, entity.getAnimationHandler(), partialTick);

        if (entity.getWheelEntity() != null) {
            float yRot = lerpClamped(partialTick, entity.getWheelEntity().yRotO, entity.getWheelEntity().getYRot());
            float pYRot = lerpClamped(partialTick, entity.yRotO, entity.getYRot());
            this.backBeam.yRot -= pYRot * Mth.DEG_TO_RAD;
            this.backBeam.yRot += yRot * Mth.DEG_TO_RAD;
            float xRot = Mth.lerp(partialTick, entity.getWheelEntity().xRotO, entity.getWheelEntity().getXRot());
            float chariotX = Mth.clamp(xRot, -15, 15);
            this.centerBeam.xRot += chariotX * Mth.DEG_TO_RAD;
            chariotX = Mth.clamp(xRot - chariotX, -40, 40);
            this.backBeam.xRot += chariotX * Mth.DEG_TO_RAD;
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