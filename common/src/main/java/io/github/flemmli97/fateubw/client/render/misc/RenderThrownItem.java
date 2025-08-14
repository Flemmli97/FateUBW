package io.github.flemmli97.fateubw.client.render.misc;

import io.github.flemmli97.fateubw.common.entity.misc.ThrownItemEntity;
import io.github.flemmli97.tenshilib.client.render.ItemProjectileRenderer;
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
    public Type getRenderType(ThrownItemEntity entity) {
        return Type.WEAPON;
    }
}