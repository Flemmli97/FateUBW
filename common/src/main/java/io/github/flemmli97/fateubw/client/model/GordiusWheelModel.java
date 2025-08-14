package io.github.flemmli97.fateubw.client.model;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.common.entity.summons.GordiusWheel;
import io.github.flemmli97.fateubw.common.utils.MathsHelper;
import io.github.flemmli97.tenshilib.client.data.GeoAnimationManager;
import io.github.flemmli97.tenshilib.client.data.GeoModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.BedrockAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.client.model.RideableModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class GordiusWheelModel extends EntityModel<GordiusWheel> implements ExtendedModel, RideableModel<GordiusWheel> {

    public static final ResourceLocation LOCATION = Fate.modRes("gordius");

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
        float partialTicks = ClientHandler.getPartialTicks();
        if (entity.deathTime <= 0) {
            this.anim.get().doAnimation(this, "idle", entity.tickCount, partialTicks);
            this.anim.get().doAnimation(this, "move", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks), false, true);
            this.anim.get().doAnimation(this, "move", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks), false, true);
        }
        this.anim.get().doAnimation(this, entity.getAnimationHandler(), partialTicks);
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
        poseStack.translate(attach.x(), attach.y(), attach.z());
    }
}