package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.model.TentacleModel;
import io.github.flemmli97.fateubw.common.entity.summons.Tentacle;
import io.github.flemmli97.tenshilib.client.render.SimpleModelRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderTentacle extends SimpleModelRenderer<Tentacle> {

    public static final ResourceLocation TEX = Fate.modRes("textures/entity/tentacle.png");

    public RenderTentacle(EntityRendererProvider.Context ctx) {
        super(ctx, new TentacleModel<>());
    }

    @Override
    public void translate(Tentacle entity, PoseStack stack, float pitch, float yaw, float partialTicks) {
        stack.scale(Tentacle.SCALE, Tentacle.SCALE, Tentacle.SCALE);
        super.translate(entity, stack, pitch, yaw, partialTicks);
    }

    @Override
    public ResourceLocation getTextureLocation(Tentacle entity) {
        return TEX;
    }
}
