package com.flemmli97.fate.client.render;

import com.flemmli97.fate.common.entity.EntityGem;
import com.flemmli97.fate.common.registry.ModItems;
import com.flemmli97.tenshilib.client.render.RenderProjectileItem;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;

public class RenderGem extends RenderProjectileItem<EntityGem> {

    private final ItemStack gem = new ItemStack(ModItems.crystalCluster.get());

    public RenderGem(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public ItemStack getRenderItemStack(EntityGem entityGem) {
        return gem;
    }

    @Override
    public Type getRenderType(EntityGem entityGem) {
        return Type.NORMAL;
    }
}
