package com.flemmli97.fate.client.render;

import com.flemmli97.fate.common.entity.EntityGaeBolg;
import com.flemmli97.fate.common.registry.ModItems;
import com.flemmli97.tenshilib.client.render.RenderProjectileItem;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;

public class RenderGaeBolg extends RenderProjectileItem<EntityGaeBolg> {

    private final ItemStack stack = new ItemStack(ModItems.gaebolg.get());

    public RenderGaeBolg(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ItemStack getRenderItemStack(EntityGaeBolg entity) {
        return this.stack;
    }

    @Override
    public Type getRenderType(EntityGaeBolg entity) {
        return Type.WEAPON;
    }

}
