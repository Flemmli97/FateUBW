package io.github.flemmli97.fate.client.render.misc;

import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.client.model.ModelGordiusWheel;
import io.github.flemmli97.fate.client.model.ModelPegasus;
import io.github.flemmli97.fate.common.entity.EntityGordiusWheel;
import io.github.flemmli97.fate.common.entity.EntityPegasus;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderPegasus extends MobRenderer<EntityPegasus, ModelPegasus> {

    public final ResourceLocation tex = new ResourceLocation(Fate.MODID, "textures/entity/pegasus.png");

    public RenderPegasus(EntityRendererManager manager) {
        super(manager, new ModelPegasus(), 0.5f);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityPegasus gordiusWheel) {
        return this.tex;
    }
}
