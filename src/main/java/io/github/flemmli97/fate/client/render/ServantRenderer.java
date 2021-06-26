package io.github.flemmli97.fate.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.flemmli97.fate.client.model.IArmModel;
import io.github.flemmli97.fate.client.model.IPreRenderUpdate;
import io.github.flemmli97.fate.common.entity.servant.EntityServant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public abstract class ServantRenderer<T extends EntityServant, M extends EntityModel<T> & IArmModel & IHasHead & IPreRenderUpdate<T>> extends LivingRenderer<T, M> {

    private static final ResourceLocation DEFAULT_RES_LOC = new ResourceLocation("textures/entity/steve.png");

    public ServantRenderer(EntityRendererManager manager, M model) {
        super(manager, model, 0.5f);
        this.addLayer(new LayerHand<>(this));
        this.addLayer(new HeadLayer<>(this));
    }

    @Override
    public void render(T entity, float yaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light) {
        this.entityModel.update(entity);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre<>(entity, this, partialTicks, matrixStack, buffer, light)))
            return;
        matrixStack.push();
        this.entityModel.swingProgress = this.getSwingProgress(entity, partialTicks);

        boolean shouldSit = entity.isPassenger() && (entity.getRidingEntity() != null && entity.getRidingEntity().shouldRiderSit());
        this.entityModel.isSitting = shouldSit;
        this.entityModel.isChild = entity.isChild();
        float yawOffset = MathHelper.interpolateAngle(partialTicks, entity.prevRenderYawOffset, entity.renderYawOffset);
        float yawHead = MathHelper.interpolateAngle(partialTicks, entity.prevRotationYawHead, entity.rotationYawHead);
        float yawHeadAct = yawHead - yawOffset;
        if (shouldSit && entity.getRidingEntity() instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity) entity.getRidingEntity();
            yawOffset = MathHelper.interpolateAngle(partialTicks, livingentity.prevRenderYawOffset, livingentity.renderYawOffset);
            yawHeadAct = yawHead - yawOffset;
            float wrappedYaw = MathHelper.wrapDegrees(yawHeadAct);
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

        float pitch = MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch);
        if (entity.getPose() == Pose.SLEEPING) {
            Direction direction = entity.getBedDirection();
            if (direction != null) {
                float f4 = entity.getEyeHeight(Pose.STANDING) - 0.1F;
                matrixStack.translate(-direction.getXOffset() * f4, 0.0D, -direction.getZOffset() * f4);
            }
        }

        float f7 = this.handleRotationFloat(entity, partialTicks);
        this.applyRotations(entity, matrixStack, f7, yawOffset, partialTicks);
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        this.preRenderCallback(entity, matrixStack, partialTicks);
        matrixStack.translate(0.0D, -1.501F, 0.0D);
        float limgSwingAmount = 0.0F;
        float maxLimbSwing = 0.0F;
        if (!shouldSit && entity.isAlive()) {
            limgSwingAmount = MathHelper.lerp(partialTicks, entity.prevLimbSwingAmount, entity.limbSwingAmount);
            maxLimbSwing = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);
            if (entity.isChild()) {
                maxLimbSwing *= 3.0F;
            }

            if (limgSwingAmount > 1.0F) {
                limgSwingAmount = 1.0F;
            }
        }

        this.entityModel.setLivingAnimations(entity, maxLimbSwing, limgSwingAmount, partialTicks);
        this.entityModel.setRotationAngles(entity, maxLimbSwing, limgSwingAmount, f7, yawHeadAct, pitch);
        Minecraft minecraft = Minecraft.getInstance();
        boolean visible = this.isVisible(entity);
        boolean transparent = (!visible || entity.getShouldBeDead()) && !entity.isInvisibleToPlayer(minecraft.player);
        boolean outline = minecraft.isEntityGlowing(entity);
        RenderType rendertype = this.func_230496_a_(entity, visible, transparent, outline);
        if (rendertype != null) {
            IVertexBuilder ivertexbuilder = buffer.getBuffer(rendertype);
            int i = getPackedOverlay(entity, this.getOverlayProgress(entity, partialTicks));
            float alpha = entity.getShouldBeDead() ? Math.max(0.1f, 1 - (entity.getDeathTick() / (float) entity.maxDeathTick())) : transparent ? 0.15f : 1;
            this.entityModel.render(matrixStack, ivertexbuilder, light, i, 1.0F, 1.0F, 1.0F, alpha);
        }

        if (!entity.isSpectator()) {
            for (LayerRenderer<T, M> layerrenderer : this.layerRenderers) {
                layerrenderer.render(matrixStack, buffer, light, entity, maxLimbSwing, limgSwingAmount, partialTicks, f7, yawHeadAct, pitch);
            }
        }

        matrixStack.pop();
        this.nameTag(entity, yaw, partialTicks, matrixStack, buffer, light);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post<>(entity, this, partialTicks, matrixStack, buffer, light));
    }

    private void nameTag(T entity, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
        net.minecraftforge.client.event.RenderNameplateEvent renderNameplateEvent = new net.minecraftforge.client.event.RenderNameplateEvent(entity, entity.getDisplayName(), this, stack, buffer, light, partialTicks);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(renderNameplateEvent);
        if (renderNameplateEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameplateEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW || this.canRenderName(entity))) {
            this.renderName(entity, renderNameplateEvent.getContent(), stack, buffer, light);
        }
    }

    @Override
    protected boolean canRenderName(T entity) {
        return super.canRenderName(entity) && (entity.getAlwaysRenderNameTagForRender() || entity.hasCustomName() && entity == this.renderManager.pointedEntity);
    }

    @Override
    protected void applyRotations(T entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
    }

    @Override
    public ResourceLocation getEntityTexture(T servant) {
        return showIdentity(servant) ? this.servantTexture(servant) : DEFAULT_RES_LOC;
    }

    public static boolean showIdentity(EntityServant servant) {
        return servant.getShouldBeDead() || servant.showServant() || Minecraft.getInstance().player.equals(servant.getOwner());
    }

    public abstract ResourceLocation servantTexture(T servant);
}
