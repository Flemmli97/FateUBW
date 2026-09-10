package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.fateubw.client.render.vertex.AlphaVertexConsumerWrapper;
import io.github.flemmli97.fateubw.common.entity.misc.ProjectileBlockingItemEntity;
import io.github.flemmli97.tenshilib.client.render.ItemProjectileRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemStack;

public class RenderProjectileBlockingEntity extends ItemProjectileRenderer<ProjectileBlockingItemEntity> {

    public RenderProjectileBlockingEntity(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(ProjectileBlockingItemEntity entity, float rotation, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.scale(1.5f, 1.5f, 1.5f);
        if (!entity.isTargeting() && entity.getOwner() != null) {
            entity.setYRot(-entity.getOwner().getViewYRot(1));
            entity.yRotO = -entity.getOwner().getViewYRot(0);
            entity.setXRot(-entity.getOwner().getViewXRot(1));
            entity.xRotO = -entity.getOwner().getViewXRot(0);
        }
        stack.translate(0, 0.05, 0);
        float alpha = Math.clamp((entity.tickCount + partialTick) / 8, 0, 1);
        MultiBufferSource source = buffer;
        if (alpha != 1) {
            source = type -> new AlphaVertexConsumerWrapper(buffer.getBuffer(type), alpha);
        }
        super.render(entity, rotation, partialTick, stack, source, packedLight);
        stack.popPose();
    }

    @Override
    public ItemStack getRenderItemStack(ProjectileBlockingItemEntity entity) {
        return entity.getItem();
    }

    @Override
    public Type getRenderType(ProjectileBlockingItemEntity entity) {
        return Type.WEAPON;
    }
}
