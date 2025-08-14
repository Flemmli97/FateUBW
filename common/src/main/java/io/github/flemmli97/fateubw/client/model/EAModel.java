package io.github.flemmli97.fateubw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.tenshilib.client.data.GeoAnimationManager;
import io.github.flemmli97.tenshilib.client.data.GeoModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.BedrockAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;

public class EAModel extends Model implements ExtendedModel {

    protected final ReloadableCache<ModelPartsContainer> model;
    protected final ReloadableCache<BedrockAnimations> animation;

    public EAModel() {
        super(RenderType::entitySolid);
        this.model = GeoModelManager.getInstance().getModel(Fate.modRes("enuma_elish"));
        this.animation = GeoAnimationManager.getInstance().getAnimation(Fate.modRes("enuma_elish"));
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.model.get().getRoot().render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    public void spinBlade(int tick, float partialTicks) {
        this.animation.get().doAnimation(this, "spin", tick, partialTicks);
    }

    @Override
    public ModelPartsContainer getModel() {
        return this.model.get();
    }
}
