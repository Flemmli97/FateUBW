package io.github.flemmli97.fate.client.render.misc;

import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.client.model.ModelGordiusBulls;
import io.github.flemmli97.fate.common.entity.EntityGordiusBulls;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderGordiusBulls extends MobRenderer<EntityGordiusBulls, ModelGordiusBulls> {

    public final ResourceLocation tex = new ResourceLocation(Fate.MODID, "textures/entity/gordius_wheel.png");

    public RenderGordiusBulls(EntityRendererManager manager) {
        super(manager, new ModelGordiusBulls(), 0.5f);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityGordiusBulls gordiusWheel) {
        return this.tex;
    }
}
