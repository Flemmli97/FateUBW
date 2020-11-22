package com.flemmli97.fate.client.render;

import com.flemmli97.fate.client.model.IPreRenderUpdate;
import com.flemmli97.fate.common.entity.servant.EntityServant;
import com.flemmli97.tenshilib.client.model.IItemArmModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
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

public abstract class ServantRenderer<T extends EntityServant, M extends EntityModel<T> & IItemArmModel & IHasHead & IPreRenderUpdate<T>> extends LivingRenderer<T, M> {

    private static final ResourceLocation DEFAULT_RES_LOC = new ResourceLocation("textures/entity/steve.png");

    public ServantRenderer(EntityRendererManager manager, M model) {
        super(manager, model, 0.5f);
        this.addLayer(new LayerHand<>(this));
        this.addLayer(new HeadLayer<>(this));
    }

    @Override
    public void render(T entity, float yaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light) {
        this.entityModel.update(entity);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre<T, M>(entity, this, partialTicks, matrixStack, buffer, light)))
            return;
        matrixStack.push();
        this.entityModel.swingProgress = this.getSwingProgress(entity, partialTicks);

        boolean shouldSit = entity.isPassenger() && (entity.getRidingEntity() != null && entity.getRidingEntity().shouldRiderSit());
        this.entityModel.isSitting = shouldSit;
        this.entityModel.isChild = entity.isChild();
        float yawOffset = MathHelper.func_219805_h(partialTicks, entity.prevRenderYawOffset, entity.renderYawOffset);
        float yawHead = MathHelper.func_219805_h(partialTicks, entity.prevRotationYawHead, entity.rotationYawHead);
        float yawHeadAct = yawHead - yawOffset;
        if (shouldSit && entity.getRidingEntity() instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity) entity.getRidingEntity();
            yawOffset = MathHelper.func_219805_h(partialTicks, livingentity.prevRenderYawOffset, livingentity.renderYawOffset);
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
        this.setupTransforms(entity, matrixStack, f7, yawOffset, partialTicks);
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        this.scale(entity, matrixStack, partialTicks);
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
        this.entityModel.setAngles(entity, maxLimbSwing, limgSwingAmount, f7, yawHeadAct, pitch);
        Minecraft minecraft = Minecraft.getInstance();
        boolean visible = this.isVisible(entity);
        boolean transparent = (!visible || entity.isDead()) && !entity.isInvisibleToPlayer(minecraft.player);
        boolean outline = minecraft.hasOutline(entity);
        RenderType rendertype = this.getRenderLayer(entity, visible, transparent, outline);
        if (rendertype != null) {
            IVertexBuilder ivertexbuilder = buffer.getBuffer(rendertype);
            int i = getOverlay(entity, this.getAnimationCounter(entity, partialTicks));
            float alpha = entity.isDead() ? Math.max(0.1f, 1 - (entity.getDeathTick() / (float) entity.maxDeathTick())) : transparent ? 0.15f : 1;
            this.entityModel.render(matrixStack, ivertexbuilder, light, i, 1.0F, 1.0F, 1.0F, alpha);
        }

        if (!entity.isSpectator()) {
            for (LayerRenderer<T, M> layerrenderer : this.layerRenderers) {
                layerrenderer.render(matrixStack, buffer, light, entity, maxLimbSwing, limgSwingAmount, partialTicks, f7, yawHeadAct, pitch);
            }
        }

        matrixStack.pop();
        this.nameTag(entity, yaw, partialTicks, matrixStack, buffer, light);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post<T, M>(entity, this, partialTicks, matrixStack, buffer, light));
    }

    private void nameTag(T entity, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
        net.minecraftforge.client.event.RenderNameplateEvent renderNameplateEvent = new net.minecraftforge.client.event.RenderNameplateEvent(entity, entity.getDisplayName(), this, stack, buffer, light, partialTicks);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(renderNameplateEvent);
        if (renderNameplateEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameplateEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW || this.canRenderName(entity))) {
            this.renderLabelIfPresent(entity, renderNameplateEvent.getContent(), stack, buffer, light);
        }
    }

    @Override
    protected boolean canRenderName(T entity) {
        return super.canRenderName(entity) && (entity.getAlwaysRenderNameTagForRender() || entity.hasCustomName() && entity == this.renderManager.pointedEntity);
    }

    @Override
    protected void setupTransforms(T p_225621_1_, MatrixStack p_225621_2_, float p_225621_3_, float p_225621_4_, float p_225621_5_) {
        super.setupTransforms(p_225621_1_, p_225621_2_, p_225621_3_, p_225621_4_, p_225621_5_);
    }

    @Override
    public ResourceLocation getEntityTexture(T servant) {
        return showIdentity(servant) ? this.servantTexture(servant) : DEFAULT_RES_LOC;
    }

    public static boolean showIdentity(EntityServant servant) {
        return servant.isDead() || servant.showServant() || Minecraft.getInstance().player.equals(servant.getOwner());
    }

    public abstract ResourceLocation servantTexture(T servant);
}
