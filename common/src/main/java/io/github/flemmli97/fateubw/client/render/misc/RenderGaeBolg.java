package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.fateubw.client.particles.TrailRenderer;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import io.github.flemmli97.fateubw.common.entity.misc.GaeBolg;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.provider.ParticlePositionProvider;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.tenshilib.client.render.ItemProjectileRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemStack;

public class RenderGaeBolg extends ItemProjectileRenderer<GaeBolg> {

    private final ItemStack stack = new ItemStack(FateItems.GAEBOLG.get());
    private final TrailInfo info = TrailInfo.builder(new ParticlePositionProvider.ParticlePositionData(0))
            .setColor(121 / 255f, 15 / 255f, 15 / 255f, 0.5f)
            .setColor2(121 / 255f, 15 / 255f, 15 / 255f, 0.3f)
            .setWidth(0.07f)
            .setWidth2(0.005f)
            .setInterpolation(1)
            .build();

    public RenderGaeBolg(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(GaeBolg entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.translate(0, 0.05, 0);
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
        stack.popPose();
        TrailRenderer.render(entity, this.info, entity.trailPositions(), buffer.getBuffer(FateRenders.TRAIL_TRANSLUCENT), partialTicks);
    }

    @Override
    public ItemStack getRenderItemStack(GaeBolg entity) {
        return this.stack;
    }

    @Override
    public Type getRenderType(GaeBolg entity) {
        return Type.WEAPON;
    }
}
