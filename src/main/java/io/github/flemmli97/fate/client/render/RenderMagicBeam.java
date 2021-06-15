package io.github.flemmli97.fate.client.render;

import com.flemmli97.tenshilib.client.render.RenderBeam;
import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.common.entity.EntityMagicBeam;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderMagicBeam extends RenderBeam<EntityMagicBeam> {

    private static final ResourceLocation tex = new ResourceLocation(Fate.MODID, "textures/entity/magic_beam.png");

    public RenderMagicBeam(EntityRendererManager manager) {
        super(manager, 0.3f);
    }

    @Override
    public void render(EntityMagicBeam projectile, float rotation, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int packedLight) {
        if (projectile.iddle) {

        } else
            super.render(projectile, rotation, partialTicks, stack, buffer, packedLight);
    }

    @Override
    public ResourcePair startTexture(EntityMagicBeam entityMagicBeam) {
        return null;
    }

    @Override
    public ResourcePair endTexture(EntityMagicBeam entityMagicBeam) {
        return null;
    }

    @Override
    public ResourceLocation getEntityTexture(EntityMagicBeam entityMagicBeam) {
        return tex;
    }
}
