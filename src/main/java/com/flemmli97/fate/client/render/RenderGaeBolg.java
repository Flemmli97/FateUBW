package com.flemmli97.fate.client.render;

import com.flemmli97.fate.common.entity.EntityGaeBolg;
import com.flemmli97.fate.common.registry.ModItems;
import com.flemmli97.tenshilib.client.render.RenderProjectileItem;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderGaeBolg extends RenderProjectileItem<EntityGaeBolg> {

    public RenderGaeBolg(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ItemStack getRenderItemStack(EntityGaeBolg entity) {
        return new ItemStack(ModItems.gaebolg);
    }

    @Override
    public RenderType getRenderType(EntityGaeBolg entity) {
        return RenderType.WEAPON;
    }

    @Override
    public ResourceLocation getEntityTexture(EntityGaeBolg entity) {
        return null;
    }
}
