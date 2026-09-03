package io.github.flemmli97.fateubw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.summons.Hoplite;
import io.github.flemmli97.fateubw.common.entity.utils.MoveType;
import io.github.flemmli97.tenshilib.client.model.ExtendedEntityModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.client.model.animation.Animation;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.utils.math.parser.VariableMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class HopliteModel<T extends Hoplite> extends ExtendedEntityModel<T> {

    public static final ResourceLocation LOCATION = Fate.modRes("hoplite");

    public ModelPartsContainer.ModelPartExtended head;
    public ModelPartsContainer.ModelPartExtended spear;
    public ModelPartsContainer.ModelPartExtended shield;

    public HopliteModel() {
        super(LOCATION, LOCATION);
    }

    @Override
    protected void onModelReload(ModelPartsContainer model) {
        this.head = model.getPart("Head");
        this.spear = model.getPart("Spear");
        this.shield = model.getPart("Shield");
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.getModel().getRoot().render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getModel().resetPoses();
        this.head.yRot += netHeadYaw * Mth.DEG_TO_RAD * 0.3f;
        this.head.xRot += headPitch * Mth.DEG_TO_RAD * 0.1f;
        float partialTick = this.getPartialTick();
        this.shield.visible = entity.hasShield();
        this.spear.visible = entity.hasSpear();
        if (entity.deathTime <= 0) {
            this.animation.get().doAnimation(this, entity.isBlockingShield() ? "block" : "idle", entity.tickCount, partialTick);
            this.animation.get().doAnimation(this, entity.isBlockingShield() ? "block_walk" : "walk", entity.tickCount, partialTick, entity.interpolatedMoveTick(partialTick));
            this.animation.get().doAnimation(this, "run", entity.tickCount, partialTick, entity.interpolatedMoveTickOf(MoveType.RUN, partialTick));
        }
        if (this.riding) {
            this.animation.get().doAnimation(this, "riding_pre", entity.tickCount, partialTick, 1);
        }
        this.animation.get().doAnimation(this, entity.getAnimationHandler(), partialTick);
        if (this.riding) {
            this.animation.get().doAnimation(this, "riding", entity.tickCount, partialTick, 1);
        }
    }

    @Override
    public void onPlayAnimation(AnimationState state, Animation animation, float tick, VariableMap variables) {
        super.onPlayAnimation(state, animation, tick, variables);
        if (animation.variables().contains("query.has_spear")) {
            variables.setVariable("query.has_spear", this.entity.get().hasSpear() ? 1 : 0);
        }
    }

    @Override
    public ModelPartsContainer getModel() {
        return this.model.get();
    }
}