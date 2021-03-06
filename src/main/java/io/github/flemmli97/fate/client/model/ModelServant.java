package io.github.flemmli97.fate.client.model;

import com.flemmli97.tenshilib.api.entity.IAnimated;
import com.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import com.flemmli97.tenshilib.client.model.IResetModel;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.client.render.ServantRenderer;
import io.github.flemmli97.fate.common.entity.servant.EntityServant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class ModelServant<T extends EntityServant & IAnimated> extends EntityModel<T> implements IResetModel, IArmModel, IHasHead, IPreRenderUpdate<T> {

    public ModelRendererPlus servantHead;
    public ModelRendererPlus servantHeadOverlay;
    public ModelRendererPlus servantBody;
    public ModelRendererPlus servantBodyOverlay;

    public ModelRendererPlus servantRightArmUp;
    public ModelRendererPlus servantRightArmUpOverlay;
    public ModelRendererPlus servantRightArmJoint;
    public ModelRendererPlus servantRightArmOverlayJoint;
    public ModelRendererPlus servantRightArmDown;
    public ModelRendererPlus servantRightArmDownOverlay;

    public ModelRendererPlus servantLeftArmUp;
    public ModelRendererPlus servantLeftArmUpOverlay;
    public ModelRendererPlus servantLeftArmJoint;
    public ModelRendererPlus servantLeftArmOverlayJoint;
    public ModelRendererPlus servantLeftArmDown;
    public ModelRendererPlus servantLeftArmDownOverlay;

    public ModelRendererPlus servantRightLegUp;
    public ModelRendererPlus servantRightLegUpOverlay;
    public ModelRendererPlus servantRightLegDown;
    public ModelRendererPlus servantRightLegDownOverlay;

    public ModelRendererPlus servantLeftLegUp;
    public ModelRendererPlus servantLeftLegUpOverlay;
    public ModelRendererPlus servantLeftLegDown;
    public ModelRendererPlus servantLeftLegDownOverlay;

    public int heldItemMain, heldItemOff;
    protected boolean show;

    public final BlockBenchAnimations anim;

    public ModelServant(String animFileName) {
        this(animFileName, 0);
    }

    public ModelServant(String animFileName, float scale) {
        super(RenderType::getEntityTranslucent);
        this.textureWidth = 64;
        this.textureHeight = 64;

        this.servantHead = new ModelRendererPlus(this, 0, 0);
        this.servantHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, scale);
        this.servantHead.setDefaultRotPoint(0, 0, 0);
        this.servantHeadOverlay = new ModelRendererPlus(this, 32, 0);
        this.servantHeadOverlay.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, scale + 0.5F);
        this.servantHeadOverlay.setDefaultRotPoint(0, 0, 0);

        this.servantBody = new ModelRendererPlus(this, 16, 16);
        this.servantBody.addBox(-4.0F, 0, -2.0F, 8, 12, 4, scale);
        this.servantBody.setDefaultRotPoint(0, 0, 0);
        this.servantBodyOverlay = new ModelRendererPlus(this, 16, 32);
        this.servantBodyOverlay.addBox(-4.0F, 0, -2.0F, 8, 12, 4, scale + 0.25F);
        this.servantBodyOverlay.setDefaultRotPoint(0, 0, 0);

        this.servantLeftArmUp = new ModelRendererPlus(this, 40, 16);
        this.servantLeftArmUp.mirror = true;
        this.servantLeftArmUp.addBox(-1.0F, -2.0F, -2.0F, 4, 6, 4, scale);
        this.servantLeftArmUp.setDefaultRotPoint(5.0F, 2.0F, 0);
        this.servantLeftArmUpOverlay = new ModelRendererPlus(this, 40, 32);
        this.servantLeftArmUpOverlay.mirror = true;
        this.servantLeftArmUpOverlay.addBox(-1.0F, -2.0F, -2.0F, 4, 6, 4, scale + 0.25F);
        this.servantLeftArmUpOverlay.setDefaultRotPoint(5.0F, 2.0F, 0);

        this.servantLeftArmJoint = new ModelRendererPlus(this, 0, 0);
        this.servantLeftArmJoint.setDefaultRotPoint(3.0F, 4.0F, 0);
        this.servantLeftArmUp.addChild(this.servantLeftArmJoint);
        this.servantLeftArmOverlayJoint = new ModelRendererPlus(this, 0, 0);
        this.servantLeftArmOverlayJoint.setDefaultRotPoint(3.0F, 4.0F, 0);
        this.servantLeftArmUpOverlay.addChild(this.servantLeftArmOverlayJoint);

        this.servantLeftArmDown = new ModelRendererPlus(this, 32, 54);
        this.servantLeftArmDown.mirror = true;
        this.servantLeftArmDown.addBox(-4.0F, 0, -2.0F, 4, 6, 4, scale);
        this.servantLeftArmDown.setDefaultRotPoint(0, 0, 0);
        this.servantLeftArmJoint.addChild(this.servantLeftArmDown);
        this.servantLeftArmDownOverlay = new ModelRendererPlus(this, 48, 54);
        this.servantLeftArmDownOverlay.mirror = true;
        this.servantLeftArmDownOverlay.addBox(-4.0F, 0, -2.0F, 4, 6, 4, scale + 0.25F);
        this.servantLeftArmDownOverlay.setDefaultRotPoint(0, 0, 0);
        this.servantLeftArmOverlayJoint.addChild(this.servantLeftArmDownOverlay);

        this.servantRightArmUp = new ModelRendererPlus(this, 40, 16);
        this.servantRightArmUp.addBox(-3.0F, -2.0F, -2.0F, 4, 6, 4, scale);
        this.servantRightArmUp.setDefaultRotPoint(-5.0F, 2.0F, 0);
        this.servantRightArmUpOverlay = new ModelRendererPlus(this, 40, 32);
        this.servantRightArmUpOverlay.addBox(-3.0F, -2.0F, -2.0F, 4, 6, 4, scale + 0.25F);
        this.servantRightArmUpOverlay.setDefaultRotPoint(-5.0F, 2.0F, 0);

        this.servantRightArmJoint = new ModelRendererPlus(this, 0, 0);
        this.servantRightArmJoint.setDefaultRotPoint(-3.0F, 4.0F, 0);
        this.servantRightArmUp.addChild(this.servantRightArmJoint);
        this.servantRightArmOverlayJoint = new ModelRendererPlus(this, 0, 0);
        this.servantRightArmOverlayJoint.setDefaultRotPoint(-3.0F, 4.0F, 0);
        this.servantRightArmUpOverlay.addChild(this.servantRightArmOverlayJoint);

        this.servantRightArmDown = new ModelRendererPlus(this, 32, 54);
        this.servantRightArmDown.addBox(0, 0, -2.0F, 4, 6, 4, scale);
        this.servantRightArmDown.setDefaultRotPoint(0, 0, 0);
        this.servantRightArmJoint.addChild(this.servantRightArmDown);
        this.servantRightArmDownOverlay = new ModelRendererPlus(this, 48, 54);
        this.servantRightArmDownOverlay.addBox(0, 0, -2.0F, 4, 6, 4, scale + 0.25F);
        this.servantRightArmDownOverlay.setDefaultRotPoint(0, 0, 0);
        this.servantRightArmOverlayJoint.addChild(this.servantRightArmDownOverlay);

        this.servantLeftLegUp = new ModelRendererPlus(this, 0, 16);
        this.servantLeftLegUp.mirror = true;
        this.servantLeftLegUp.addBox(-2.0F, 0, -2.0F, 4, 6, 4, scale);
        this.servantLeftLegUp.setDefaultRotPoint(1.9F, 12.0F, 0);
        this.servantLeftLegUpOverlay = new ModelRendererPlus(this, 0, 32);
        this.servantLeftLegUpOverlay.mirror = true;
        this.servantLeftLegUpOverlay.addBox(-2.0F, 0, -2.0F, 4, 6, 4, scale + 0.25F);
        this.servantLeftLegUpOverlay.setDefaultRotPoint(1.9F, 12.0F, 0);

        this.servantLeftLegDown = new ModelRendererPlus(this, 16, 54);
        this.servantLeftLegDown.mirror = true;
        this.servantLeftLegDown.addBox(-2.0F, 0, 0, 4, 6, 4, scale);
        this.servantLeftLegDown.setDefaultRotPoint(0, 6.0F, -2.0F);
        this.servantLeftLegUp.addChild(this.servantLeftLegDown);
        this.servantLeftLegDownOverlay = new ModelRendererPlus(this, 0, 54);
        this.servantLeftLegDownOverlay.mirror = true;
        this.servantLeftLegDownOverlay.addBox(-2.0F, 0, 0, 4, 6, 4, scale + 0.25F);
        this.servantLeftLegDownOverlay.setDefaultRotPoint(0, 6.0F, -2.0F);
        this.servantLeftLegUpOverlay.addChild(this.servantLeftLegDownOverlay);

        this.servantRightLegUp = new ModelRendererPlus(this, 0, 16);
        this.servantRightLegUp.addBox(-2.0F, 0, -2.0F, 4, 6, 4, scale);
        this.servantRightLegUp.setDefaultRotPoint(-1.9F, 12.0F, 0);
        this.servantRightLegUpOverlay = new ModelRendererPlus(this, 0, 32);
        this.servantRightLegUpOverlay.addBox(-2.0F, 0, -2.0F, 4, 6, 4, scale + 0.25F);
        this.servantRightLegUpOverlay.setRotationPoint(-1.9F, 12.0F, 0);

        this.servantRightLegDown = new ModelRendererPlus(this, 16, 54);
        this.servantRightLegDown.addBox(-2.0F, 0, 0, 4, 6, 4, scale);
        this.servantRightLegDown.setDefaultRotPoint(0, 6.0F, -2.0F);
        this.servantRightLegUp.addChild(this.servantRightLegDown);
        this.servantRightLegDownOverlay = new ModelRendererPlus(this, 0, 54);
        this.servantRightLegDownOverlay.addBox(-2.0F, 0, 0, 4, 6, 4, scale + 0.25F);
        this.servantRightLegDownOverlay.setDefaultRotPoint(0, 6.0F, -2.0F);
        this.servantRightLegUpOverlay.addChild(this.servantRightLegDownOverlay);

        this.servantBody.addChild(this.servantHead);
        this.servantBody.addChild(this.servantRightArmUp);
        this.servantBody.addChild(this.servantLeftArmUp);
        this.servantBody.addChild(this.servantRightLegUp);
        this.servantBody.addChild(this.servantLeftLegUp);

        this.servantBodyOverlay.addChild(this.servantHeadOverlay);
        this.servantBodyOverlay.addChild(this.servantRightArmUpOverlay);
        this.servantBodyOverlay.addChild(this.servantLeftArmUpOverlay);
        this.servantBodyOverlay.addChild(this.servantRightLegUpOverlay);
        this.servantBodyOverlay.addChild(this.servantLeftLegUpOverlay);

        this.anim = new BlockBenchAnimations(this, new ResourceLocation(Fate.MODID, "models/entity/animation/" + animFileName + ".json"));
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.servantBody.render(matrixStack, buffer, packedLight, packedOverlay);
        this.servantBodyOverlay.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setRotationAngles(T servant, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float servantHeadPitch) {
        this.setAnglesPre(servant, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, servantHeadPitch);
        if (this.show) {
            float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
            if (servant.isStaying()) {
                this.anim.doAnimation("stay", servant.ticksExisted, partialTicks);
            } else {
                servant.getAnimationHandler().getAnimation().ifPresent(anim -> this.anim.doAnimation(anim.getID(), anim.getTick(), partialTicks));
            }
        }
        this.syncOverlay();
    }

    public void setAnglesPre(T servant, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float servantHeadPitch) {
        this.resetModel();
        this.servantHead.rotateAngleY = netHeadYaw / (180F / (float) Math.PI);
        this.servantHead.rotateAngleX = servantHeadPitch / (180F / (float) Math.PI);
        this.servantHeadOverlay.rotateAngleY = this.servantHead.rotateAngleY;
        this.servantHeadOverlay.rotateAngleX = this.servantHead.rotateAngleX;

        this.servantRightArmUp.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        this.servantLeftArmUp.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        this.servantRightArmUp.rotateAngleZ = 0;
        this.servantLeftArmUp.rotateAngleZ = 0;
        this.servantRightLegUp.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.servantLeftLegUp.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.servantRightLegUp.rotateAngleY = 0;
        this.servantLeftLegUp.rotateAngleY = 0;

        if (this.isSitting) {
            this.servantRightArmUp.rotateAngleX += -((float) Math.PI / 5F);
            this.servantLeftArmUp.rotateAngleX += -((float) Math.PI / 5F);
            this.servantRightLegUp.rotateAngleX = -((float) Math.PI * 2F / 5F);
            this.servantLeftLegUp.rotateAngleX = -((float) Math.PI * 2F / 5F);
            this.servantRightLegUp.rotateAngleY = ((float) Math.PI / 10F);
            this.servantLeftLegUp.rotateAngleY = -((float) Math.PI / 10F);
        }

        if (this.heldItemOff == 1)
            this.servantLeftArmUp.rotateAngleX = this.servantLeftArmUp.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
        if (this.heldItemMain == 1)
            this.servantRightArmUp.rotateAngleX = this.servantRightArmUp.rotateAngleX * 0.5F - ((float) Math.PI / 10F);

        this.servantRightArmUp.rotateAngleY = 0;
        this.servantLeftArmUp.rotateAngleY = 0;
        if (this.swingProgress > -9990) {
            float swingProgress = this.swingProgress;
            this.servantBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI * 2.0F) * 0.2F;
            this.servantRightArmUp.rotateAngleY += this.servantBody.rotateAngleY;
            this.servantLeftArmUp.rotateAngleY += this.servantBody.rotateAngleY;
            this.servantLeftArmUp.rotateAngleX += this.servantBody.rotateAngleY;
            swingProgress = 1.0F - this.swingProgress;
            swingProgress *= swingProgress;
            swingProgress *= swingProgress;
            swingProgress = 1.0F - swingProgress;
            float var9 = MathHelper.sin(swingProgress * (float) Math.PI);
            float var10 = MathHelper.sin(this.swingProgress * (float) Math.PI) * -(this.servantHead.rotateAngleX - 0.7F) * 0.75F;
            this.servantRightArmUp.rotateAngleX = (float) ((double) this.servantRightArmUp.rotateAngleX - ((double) var9 * 1.2D + (double) var10));
            this.servantRightArmUp.rotateAngleY += this.servantBody.rotateAngleY * 2.0F;
            this.servantRightArmUp.rotateAngleZ = MathHelper.sin(this.swingProgress * (float) Math.PI) * -0.4F;
        }

        this.servantBody.rotateAngleX = 0;

        this.servantRightArmUp.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.servantLeftArmUp.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.servantRightArmUp.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        this.servantLeftArmUp.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
    }

    public void syncOverlay() {
        this.servantLeftLegUpOverlay.copyModelAngles(this.servantLeftLegUp);
        this.servantRightLegUpOverlay.copyModelAngles(this.servantRightLegUp);

        this.servantLeftArmUpOverlay.copyModelAngles(this.servantLeftArmUp);
        this.servantRightArmUpOverlay.copyModelAngles(this.servantRightArmUp);

        this.servantRightArmOverlayJoint.copyModelAngles(this.servantRightArmJoint);
        this.servantLeftArmOverlayJoint.copyModelAngles(this.servantLeftArmJoint);

        this.servantRightArmDownOverlay.copyModelAngles(this.servantRightArmDown);
        this.servantLeftArmDownOverlay.copyModelAngles(this.servantLeftArmDown);

        this.servantLeftLegDownOverlay.copyModelAngles(this.servantLeftLegDown);
        this.servantRightLegDownOverlay.copyModelAngles(this.servantRightLegDown);

        this.servantHeadOverlay.copyModelAngles(this.servantHead);

        this.servantBodyOverlay.copyModelAngles(this.servantBody);
    }

    @Override
    public void resetModel() {
        this.servantBody.reset();
        this.resetChild(this.servantBody);
        this.syncOverlay();
    }

    @Override
    public void transform(HandSide side, MatrixStack stack) {
        if (side == HandSide.LEFT) {
            this.rotate(stack, this.servantBody, this.servantLeftArmUp, this.servantLeftArmJoint, this.servantLeftArmDown);
        } else {
            this.rotate(stack, this.servantBody, this.servantRightArmUp, this.servantRightArmJoint, this.servantRightArmDown);
        }
    }

    @Override
    public void postTransform(boolean leftSide, MatrixStack stack) {
        stack.translate(leftSide ? 0.125 : -0.125, 0.125, -6 / 16d);
    }

    @Override
    public ModelRenderer getModelHead() {
        return this.servantHead;
    }

    @Override
    public void update(T obj) {
        this.show = ServantRenderer.showIdentity(obj);
        this.heldItemMain = this.show || obj.getHeldItemMainhand().isEmpty() ? 0 : 1;
        this.heldItemOff = this.show || obj.getHeldItemOffhand().isEmpty() ? 0 : 1;
    }

    protected void rotate(MatrixStack stack, ModelRenderer... models) {
        for (ModelRenderer render : models)
            render.translateRotate(stack);
    }

    @Override
    public void copyModelAttributesTo(EntityModel<T> model) {
        super.copyModelAttributesTo(model);
        if (model instanceof ModelServant<?>) {
            ModelServant<?> other = (ModelServant<?>) model;
            this.show = other.show;
            this.heldItemMain = other.heldItemMain;
            this.heldItemOff = other.heldItemOff;
        }
    }

    @Override
    public ModelRenderer getHand(Hand side) {
        if (side == Hand.MAIN_HAND)
            return this.servantRightArmDown;
        return this.servantLeftArmDown;
    }
}