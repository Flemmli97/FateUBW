package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.fateubw.common.entity.misc.ThrownItemEntity;
import io.github.flemmli97.tenshilib.client.render.ItemProjectileRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemStack;

public class RenderThrownItem extends ItemProjectileRenderer<ThrownItemEntity> {

    public RenderThrownItem(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ItemStack getRenderItemStack(ThrownItemEntity entity) {
        return entity.getWeapon();
    }

    @Override
    public void render(ThrownItemEntity entity, float rotation, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        this.scaleX = 2;
        this.scaleY = 2;
        this.scaleZ = 2;
        super.render(entity, rotation, partialTick, stack, buffer, packedLight);
    }

    @Override
    public Type getRenderType(ThrownItemEntity entity) {
        return Type.WEAPON;
    }
}