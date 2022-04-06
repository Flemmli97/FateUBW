package io.github.flemmli97.fateubw.client.render.misc;

import io.github.flemmli97.fateubw.common.entity.misc.ThrownGem;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.tenshilib.client.render.RenderProjectileItem;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemStack;

public class RenderGem extends RenderProjectileItem<ThrownGem> {

    private final ItemStack gem = new ItemStack(ModItems.crystalCluster.get());

    public RenderGem(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ItemStack getRenderItemStack(ThrownGem entityGem) {
        return this.gem;
    }

    @Override
    public Type getRenderType(ThrownGem entityGem) {
        return Type.NORMAL;
    }
}
