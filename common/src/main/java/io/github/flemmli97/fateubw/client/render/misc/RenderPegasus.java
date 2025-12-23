package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.model.PegasusModel;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import io.github.flemmli97.fateubw.client.render.layer.PortalLayerRenderer;
import io.github.flemmli97.fateubw.common.entity.summons.Pegasus;
import io.github.flemmli97.tenshilib.client.render.layer.RiderEntityLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class RenderPegasus extends MobRenderer<Pegasus, PegasusModel> {

    public static final ResourceLocation TEX = Fate.modRes("textures/entity/pegasus.png");

    private float partialTicks;
    private final float shadowDefault;

    public RenderPegasus(EntityRendererProvider.Context ctx) {
        super(ctx, new PegasusModel(), 0.5f);
        this.shadowDefault = this.shadowRadius;
        this.layers.add(new PortalLayerRenderer<>(this, e -> e.getAnimationHandler().isCurrent(Pegasus.SUMMON),
                stack -> stack.translate(0, 0, this.portalOffset()), Fate.modRes("textures/misc/magic_circle_1.png"),
                Pegasus.PORTAL_SIZE).color(255, 0, 0, 255));
        this.layers.add(new RiderEntityLayer<>(this));
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
        normal.rotate(Axis.YP.rotationDegrees(-Mth.rotLerp(this.partialTicks, livingEntity.yBodyRotO, livingEntity.yBodyRot)));
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
