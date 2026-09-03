package io.github.flemmli97.fateubw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.misc.SpearProjectile;
import io.github.flemmli97.tenshilib.client.data.GeoModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class SpearModel extends EntityModel<SpearProjectile> {

    public static final ResourceLocation MODEL = Fate.modRes("spear_projectile");

    protected final ReloadableCache<ModelPartsContainer> model;

    public SpearModel() {
        super(RenderType::entityCutoutNoCull);
        this.model = GeoModelManager.getInstance().getModel(MODEL);
    }

    @Override
    public void setupAnim(SpearProjectile entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        this.model.get().getRoot().render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
