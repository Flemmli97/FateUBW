package io.github.flemmli97.fateubw.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.client.model.BaseServantModel;
import io.github.flemmli97.fateubw.client.model.ModelServant;
import io.github.flemmli97.fateubw.common.entity.NonSitVehicle;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.platform.ClientPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;

public abstract class ServantRenderer<T extends BaseServant, M extends BaseServantModel<T>> extends LivingEntityRenderer<T, BaseServantModel<T>> {

    private static final ResourceLocation DEFAULT_RES_LOC = new ResourceLocation("textures/entity/steve.png");

    private final ModelServant<T> defaultModel;
    private final M servantModel;

    public ServantRenderer(EntityRendererProvider.Context ctx, M model) {
        super(ctx, model, 0.5f);
        this.defaultModel = new ModelServant<>(ctx.bakeLayer(ModelServant.LAYER_LOCATION), "default_servant");
        this.servantModel = model;
        this.addLayer(new LayerHand<>(this));
        this.addLayer(new CustomHeadLayer<>(this, ctx.getModelSet()));
    }

    @Override
    public void render(T entity, float yaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light) {
        this.model = ServantRenderer.showIdentity(entity) ? this.servantModel : this.defaultModel;
        this.model.update(entity);
        if (ClientPlatform.INSTANCE.renderLivingEvent(entity, this, partialTicks, matrixStack, buffer, light, true))
            return;
        matrixStack.pushPose();
        this.model.attackTime = this.getAttackAnim(entity, partialTicks);

        boolean shouldSit = entity.isPassenger() && (entity.getVehicle() != null && entity.getVehicle() instanceof NonSitVehicle);
        this.model.riding = shouldSit;
        this.model.young = entity.isBaby();
        float yawOffset = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
        float yawHead = Mth.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRot);
        float yawHeadAct = yawHead - yawOffset;
        if (shouldSit && entity.getVehicle() instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity) entity.getVehicle();
            yawOffset = Mth.rotLerp(partialTicks, livingentity.yBodyRotO, livingentity.yBodyRot);
            yawHeadAct = yawHead - yawOffset;
            float wrappedYaw = Mth.wrapDegrees(yawHeadAct);
            if (wrappedYaw < -85.0F) {
                wrappedYaw = -85.0F;
            }

            if (wrappedYaw >= 85.0F) {
                wrappedYaw = 85.0F;
            }

            yawOffset = yawHead - wrappedYaw;
            if (wrappedYaw * wrappedYaw > 2500.0F) {
                yawOffset += wrappedYaw * 0.2F;
            }

            yawHeadAct = yawHead - yawOffset;
        }

        float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        if (entity.getPose() == Pose.SLEEPING) {
            Direction direction = entity.getBedOrientation();
            if (direction != null) {
                float f4 = entity.getEyeHeight(Pose.STANDING) - 0.1F;
                matrixStack.translate(-direction.getStepX() * f4, 0.0D, -direction.getStepZ() * f4);
            }
        }

        float f7 = this.getBob(entity, partialTicks);
        this.setupRotations(entity, matrixStack, f7, yawOffset, partialTicks);
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        this.scale(entity, matrixStack, partialTicks);
        matrixStack.translate(0.0D, -1.501F, 0.0D);
        float limgSwingAmount = 0.0F;
        float maxLimbSwing = 0.0F;
        if (!shouldSit && entity.isAlive()) {
            limgSwingAmount = Mth.lerp(partialTicks, entity.animationSpeedOld, entity.animationSpeed);
            maxLimbSwing = entity.animationPosition - entity.animationSpeed * (1.0F - partialTicks);
            if (entity.isBaby()) {
                maxLimbSwing *= 3.0F;
            }

            if (limgSwingAmount > 1.0F) {
                limgSwingAmount = 1.0F;
            }
        }

        this.model.prepareMobModel(entity, maxLimbSwing, limgSwingAmount, partialTicks);
        this.model.setupAnim(entity, maxLimbSwing, limgSwingAmount, f7, yawHeadAct, pitch);
        Minecraft minecraft = Minecraft.getInstance();
        boolean visible = this.isBodyVisible(entity);
        boolean transparent = (!visible || entity.isDeadOrDying()) && !entity.isInvisibleTo(minecraft.player) && entity.transparentOnDeath();
        boolean outline = minecraft.shouldEntityAppearGlowing(entity);
        RenderType rendertype = this.getRenderType(entity, visible, transparent, outline);
        if (rendertype != null) {
            VertexConsumer ivertexbuilder = buffer.getBuffer(rendertype);
            int i = getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, partialTicks));
            float alpha = entity.isDeadOrDying() ? Math.max(0.1f, 1 - (entity.getDeathTick() / (float) entity.maxDeathTick())) : transparent ? 0.15f : 1;
            this.model.renderToBuffer(matrixStack, ivertexbuilder, light, i, 1.0F, 1.0F, 1.0F, alpha);
        }

        if (!entity.isSpectator()) {
            for (RenderLayer<T, BaseServantModel<T>> layerrenderer : this.layers) {
                layerrenderer.render(matrixStack, buffer, light, entity, maxLimbSwing, limgSwingAmount, partialTicks, f7, yawHeadAct, pitch);
            }
        }

        matrixStack.popPose();
        this.nameTag(entity, yaw, partialTicks, matrixStack, buffer, light);
        ClientPlatform.INSTANCE.renderLivingEvent(entity, this, partialTicks, matrixStack, buffer, light, false);
    }

    private void nameTag(T entity, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
        Component name = ClientPlatform.INSTANCE.nameTagRenderEvent(entity, entity.getDisplayName(), this, stack, buffer, light, partialTicks, this::shouldShowName);
        if (name != null) {
            this.renderNameTag(entity, name, stack, buffer, light);
        }
    }

    @Override
    protected boolean shouldShowName(T entity) {
        return super.shouldShowName(entity) && (entity.shouldShowName() || entity.hasCustomName() && entity == this.entityRenderDispatcher.crosshairPickEntity);
    }

    @Override
    protected void setupRotations(T entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
    }

    @Override
    public ResourceLocation getTextureLocation(T servant) {
        return showIdentity(servant) ? this.servantTexture(servant) : DEFAULT_RES_LOC;
    }

    public static boolean showIdentity(BaseServant servant) {
        return servant.isDeadOrDying() || servant.showServant() || Minecraft.getInstance().player.equals(servant.getOwner());
    }

    public abstract ResourceLocation servantTexture(T servant);
}
