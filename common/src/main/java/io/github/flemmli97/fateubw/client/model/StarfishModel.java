package io.github.flemmli97.fateubw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.summons.LesserMonster;
import io.github.flemmli97.tenshilib.client.model.ExtendedEntityModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import net.minecraft.resources.ResourceLocation;

public class StarfishModel<T extends LesserMonster> extends ExtendedEntityModel<T> {

    public static final ResourceLocation LOCATION = Fate.modRes("starfish");

    public StarfishModel() {
        super(LOCATION, LOCATION);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.getModel().getRoot().render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getModel().resetPoses();
        float partialTick = this.getPartialTick();
        if (entity.deathTime <= 0) {
            this.animation.get().doAnimation(this, "idle", entity.tickCount, partialTick);
            float moveTick = entity.interpolatedMoveTick(partialTick);
            if (moveTick > 0)
                this.animation.get().doAnimation(this, "walk", entity.tickCount, partialTick, moveTick);
        }
        this.animation.get().doAnimation(this, entity.getAnimationHandler(), partialTick);
    }

    @Override
    public ModelPartsContainer getModel() {
        return this.model.get();
    }
}