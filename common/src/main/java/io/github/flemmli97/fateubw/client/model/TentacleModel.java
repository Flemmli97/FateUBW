package io.github.flemmli97.fateubw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.summons.Tentacle;
import io.github.flemmli97.tenshilib.client.data.GeoAnimationManager;
import io.github.flemmli97.tenshilib.client.data.GeoModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.BedrockAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedEntityModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class TentacleModel<T extends Tentacle> extends ExtendedEntityModel<T> {

    public static final ResourceLocation LOCATION = Fate.modRes("tentacle");

    protected final ReloadableCache<ModelPartsContainer> model;
    protected final ReloadableCache<BedrockAnimations> anim;

    private float progress;
    private ModelPartsContainer.ModelPartExtended portal;
    private ModelPartsContainer.ModelPartExtended base;

    public TentacleModel() {
        super(RenderType::entityTranslucent);
        this.model = GeoModelManager.getInstance().getModel(LOCATION, model -> {
            this.portal = model.getPart("portal");
            this.base = model.getPart("tentacle1");
        });
        this.anim = GeoAnimationManager.getInstance().getAnimation(LOCATION);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        if (this.progress != -1) {
            color = FastColor.ARGB32.color((int) ((1 - this.progress) * 255), color);
        }
        this.getModel().getRoot().render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getModel().resetPoses();
        float partialTick = this.getPartialTick();
        this.anim.get().doAnimation(this, "idle", entity.tickCount, partialTick);
        this.anim.get().doAnimation(this, entity.getAnimationHandler(), partialTick);
        this.progress = entity.getDespawnProgress(partialTick);
    }

    @Override
    public ModelPartsContainer getModel() {
        return this.model.get();
    }
}