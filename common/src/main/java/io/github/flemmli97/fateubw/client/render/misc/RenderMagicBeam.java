package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
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

    public static final ResourceLocation TEX = new ResourceLocation(Fate.MODID, "textures/entity/magic_beam.png");
    public static final ResourceLocation TEX_CIRCLE = new ResourceLocation(Fate.MODID, "textures/entity/medea_beam_circle.png");

    private final RenderUtils.TextureBuilder textureBuilder = new RenderUtils.TextureBuilder();

    private final float widthMod;

    public RenderMagicBeam(EntityRendererProvider.Context ctx) {
        super(ctx, 1, 0.8f, 4);
        this.widthMod = Mth.sqrt(0.8f * 0.8f / 2) * 2;
    }

    @Override
    public void render(MagicBeam projectile, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        if (projectile.idle) {
            stack.pushPose();
            stack.scale(1.6f, 1.6f, 1.6f);
            stack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, projectile.yRotO, projectile.getYRot()) + 90));
            stack.mulPose(Vector3f.XP.rotationDegrees(Mth.lerp(partialTicks, projectile.xRotO, projectile.getXRot())));
            this.textureBuilder.setLight(0xf000f0);
            RenderUtils.renderTexture(stack, buffer.getBuffer(RenderType.entityCutoutNoCull(TEX_CIRCLE)), 1, 1, this.textureBuilder);
            stack.popPose();
        } else
            super.render(projectile, rotation, partialTicks, stack, buffer, packedLight);
    }

    @Override
    public float widthFunc(MagicBeam entity) {
        float width = (float) (entity.radius() * 2.0F * Math.sin((double) entity.livingTicks() / entity.livingTickMax() * Math.PI));
        return width / this.widthMod;
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
        return TEX;
    }
}
