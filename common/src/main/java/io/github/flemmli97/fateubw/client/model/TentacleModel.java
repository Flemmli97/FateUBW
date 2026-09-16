package io.github.flemmli97.fateubw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.summons.Tentacle;
import io.github.flemmli97.tenshilib.client.model.ExtendedEntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class TentacleModel<T extends Tentacle> extends ExtendedEntityModel<T> {

    public static final ResourceLocation LOCATION = Fate.modRes("tentacle");

    private float progress;

    public TentacleModel() {
        super(RenderType::entityTranslucent, LOCATION, LOCATION);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        if (this.progress != -1) {
            color = FastColor.ARGB32.color((int) ((1 - this.progress) * 255), color);
        }
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, color);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getModel().resetPoses();
        float partialTick = this.getPartialTick();
        this.animation.get().doAnimation(this, "idle", entity.tickCount, partialTick);
        this.animation.get().doAnimation(this, entity.getAnimationHandler(), partialTick);
        this.progress = entity.getDespawnProgress(partialTick);
    }
}