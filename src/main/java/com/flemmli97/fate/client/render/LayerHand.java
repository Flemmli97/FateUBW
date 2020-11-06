package com.flemmli97.fate.client.render;

import com.flemmli97.fate.common.entity.servant.EntityServant;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3f;

public class LayerHand<T extends EntityServant, M extends EntityModel<T> & IHasArm> extends HeldItemLayer<T, M> {

    private static final ItemStack genericWeapon = new ItemStack(Items.IRON_SWORD);

    public LayerHand(IEntityRenderer<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(MatrixStack stack, IRenderTypeBuffer buffer, int light, T entity, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if (ServantRenderer.showIdentity(entity))
            super.render(stack, buffer, light, entity, p_225628_5_, p_225628_6_, p_225628_7_, p_225628_8_, p_225628_9_, p_225628_10_);
        else {
            stack.push();
            this.getEntityModel().setArmAngle(HandSide.RIGHT, stack);
            stack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
            stack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
            stack.translate(1 / 16.0F, 0.125D, -0.625D);
            Minecraft.getInstance().getFirstPersonRenderer().renderItem(entity, genericWeapon, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, false, stack, buffer, light);
            stack.pop();
        }
    }
}
