package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.misc.MagicBeam;
import io.github.flemmli97.tenshilib.client.render.RenderBeam;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderMagicBeam extends RenderBeam<MagicBeam> {

    private static final ResourceLocation tex = new ResourceLocation(Fate.MODID, "textures/entity/magic_beam.png");
    private static final ResourceLocation texCircle = new ResourceLocation(Fate.MODID, "textures/entity/medea_beam_circle.png");

    private final RenderUtils.TextureBuilder textureBuilder = new RenderUtils.TextureBuilder();

    public RenderMagicBeam(EntityRendererProvider.Context ctx) {
        super(ctx, 0.3f, 0.25f, 4);
    }

    @Override
    public void render(MagicBeam projectile, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        if (projectile.iddle) {
            stack.pushPose();
            RenderUtils.applyYawPitch(stack, Mth.lerp(partialTicks, projectile.yRotO, projectile.getYRot()),
                    Mth.lerp(partialTicks, projectile.xRotO, projectile.getXRot()));
            this.textureBuilder.setLight(0xf000f0);
            RenderUtils.renderTexture(stack, buffer.getBuffer(RenderType.entityCutoutNoCull(texCircle)), 1, 1, this.textureBuilder);
            stack.popPose();
        } else
            super.render(projectile, rotation, partialTicks, stack, buffer, packedLight);
    }

    @Override
    public ResourcePair startTexture(MagicBeam entityMagicBeam) {
        return null;
    }

    @Override
    public ResourcePair endTexture(MagicBeam entityMagicBeam) {
        return null;
    }

    @Override
    public ResourceLocation getTextureLocation(MagicBeam entityMagicBeam) {
        return tex;
    }
}
