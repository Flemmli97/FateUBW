package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.fateubw.common.entity.misc.GaeBolg;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.tenshilib.client.render.ItemProjectileRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemStack;

public class RenderGaeBolg extends ItemProjectileRenderer<GaeBolg> {

    private final ItemStack stack = new ItemStack(FateItems.GAEBOLG.get());

    public RenderGaeBolg(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(GaeBolg entity, float rotation, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.translate(0, 0.05, 0);
        super.render(entity, rotation, partialTick, stack, buffer, packedLight);
        stack.popPose();
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
