package io.github.flemmli97.fateubw.client.model;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.common.entity.summons.LesserMonster;
import io.github.flemmli97.tenshilib.client.data.GeoAnimationManager;
import io.github.flemmli97.tenshilib.client.data.GeoModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.BedrockAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;

public class StarfishModel<T extends LesserMonster> extends EntityModel<T> implements ExtendedModel {

    public static final ResourceLocation LOCATION = Fate.modRes("starfish");

    protected final ReloadableCache<ModelPartsContainer> model;
    protected final ReloadableCache<BedrockAnimations> anim;

    public StarfishModel() {
        super();
        this.model = GeoModelManager.getInstance().getModel(LOCATION);
        this.anim = GeoAnimationManager.getInstance().getAnimation(LOCATION);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.getModel().getRoot().render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getModel().resetPoses();
        float partialTicks = ClientHandler.getPartialTicks();
        if (entity.deathTime <= 0) {
            this.anim.get().doAnimation(this, "idle", entity.tickCount, partialTicks);
            float moveTick = entity.interpolatedMoveTick(partialTicks);
            if (moveTick > 0)
                this.anim.get().doAnimation(this, "walk", entity.tickCount, partialTicks, moveTick);
        }
        this.anim.get().doAnimation(this, entity.getAnimationHandler(), partialTicks);
    }

    @Override
    public ModelPartsContainer getModel() {
        return this.model.get();
    }
}