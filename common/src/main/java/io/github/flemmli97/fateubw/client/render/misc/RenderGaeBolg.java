package io.github.flemmli97.fateubw.client.render.misc;

import io.github.flemmli97.fateubw.common.entity.misc.GaeBolg;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.tenshilib.client.render.RenderProjectileItem;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemStack;

public class RenderGaeBolg extends RenderProjectileItem<GaeBolg> {

    private final ItemStack stack = new ItemStack(ModItems.gaebolg.get());

    public RenderGaeBolg(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ItemStack getRenderItemStack(GaeBolg entity) {
        return this.stack;
    }

    @Override
    public Type getRenderType(GaeBolg entity) {
        return Type.WEAPON;
    }

}
