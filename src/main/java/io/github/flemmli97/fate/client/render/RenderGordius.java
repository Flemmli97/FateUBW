package io.github.flemmli97.fate.client.render;

import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.client.model.ModelGordiusWheel;
import io.github.flemmli97.fate.common.entity.EntityGordiusWheel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderGordius extends MobRenderer<EntityGordiusWheel, ModelGordiusWheel> {

    public final ResourceLocation tex = new ResourceLocation(Fate.MODID, "textures/entity/gordius_wheel.png");

    public RenderGordius(EntityRendererManager manager) {
        super(manager, new ModelGordiusWheel(), 0.5f);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityGordiusWheel gordiusWheel) {
        return this.tex;
    }
}
