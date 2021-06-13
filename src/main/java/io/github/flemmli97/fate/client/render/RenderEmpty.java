package io.github.flemmli97.fate.client.render;

import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderEmpty<T extends Entity> extends EntityRenderer<T> {

    public RenderEmpty(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public boolean shouldRender(T entity, ClippingHelper helper, double camX, double camY, double camZ) {
        return false;
    }

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return null;
    }
}
