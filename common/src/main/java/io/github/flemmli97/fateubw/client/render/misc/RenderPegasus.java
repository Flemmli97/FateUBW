package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.model.ModelPegasus;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import io.github.flemmli97.fateubw.client.render.layer.PortalLayerRenderer;
import io.github.flemmli97.fateubw.common.entity.summons.Pegasus;
import io.github.flemmli97.tenshilib.client.render.RiderLayerRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class RenderPegasus extends MobRenderer<Pegasus, ModelPegasus> {

    public static final ResourceLocation TEX = new ResourceLocation(Fate.MODID, "textures/entity/pegasus.png");

    private float partialTicks;
    private final float shadowDefault;

    public RenderPegasus(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelPegasus(ctx.bakeLayer(ModelPegasus.LAYER_LOCATION)), 0.5f);
        this.shadowDefault = this.shadowRadius;
        this.layers.add(new PortalLayerRenderer<>(this, e -> e.getAnimationHandler().isCurrent(Pegasus.SUMMON),
                stack -> stack.translate(0, 0, this.portalOffset()), new ResourceLocation(Fate.MODID, "textures/misc/magic_circle.png"),
                Pegasus.PORTAL_SIZE));
        this.layers.add(new RiderLayerRenderer<>(this));
    }

    @Override
    public void render(Pegasus entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        this.partialTicks = partialTicks;
        if (entity.getAnimationHandler().isCurrent(Pegasus.SUMMON))
            this.shadowRadius = 0;
        else
            this.shadowRadius = this.shadowDefault;
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(Pegasus livingEntity, boolean bodyVisible, boolean translucent, boolean glowing) {
        RenderType origin = super.getRenderType(livingEntity, bodyVisible, translucent, glowing);
        if (origin == null || !livingEntity.getAnimationHandler().isCurrent(Pegasus.SUMMON))
            return origin;
        Vector3f normal = new Vector3f(0, 0, 1);
        normal.transform(Vector3f.YP.rotationDegrees(-Mth.rotLerp(this.partialTicks, livingEntity.yBodyRotO, livingEntity.yBodyRot)));
        return FateRenders.getClippedRendertype(origin,
                FateRenders.createClippingPlane(normal, livingEntity, this.portalOffset()));
    }

    private float portalOffset() {
        return Pegasus.PORTAL_OFFSET;
    }

    @Override
    public ResourceLocation getTextureLocation(Pegasus pegasus) {
        return TEX;
    }
}
