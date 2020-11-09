package com.flemmli97.fate.client.render;

import com.flemmli97.fate.common.entity.EntityGaeBolg;
import com.flemmli97.fate.common.registry.ModItems;
import com.flemmli97.tenshilib.client.render.RenderProjectileItem;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class RenderGaeBolg extends RenderProjectileItem<EntityGaeBolg> {

    private final ItemStack stack = new ItemStack(ModItems.gaebolg);

    public RenderGaeBolg(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ItemStack getRenderItemStack(EntityGaeBolg entity) {
        return this.stack;
    }

    @Override
    public RenderType getRenderType(EntityGaeBolg entity) {
        return RenderType.WEAPON;
    }

}
