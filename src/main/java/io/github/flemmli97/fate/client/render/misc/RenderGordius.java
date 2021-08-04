package io.github.flemmli97.fate.client.render.misc;

import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.client.model.ModelGordiusWheel;
import io.github.flemmli97.fate.common.entity.minions.EntityGordius;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderGordius extends MobRenderer<EntityGordius, ModelGordiusWheel> {

    public final ResourceLocation tex = new ResourceLocation(Fate.MODID, "textures/entity/gordius_wheel.png");

    public RenderGordius(EntityRendererManager manager) {
        super(manager, new ModelGordiusWheel(), 0.5f);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityGordius gordiusWheel) {
        return this.tex;
    }
}
