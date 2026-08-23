package io.github.flemmli97.fateubw.client.render.layer;

import io.github.flemmli97.fateubw.common.entity.utils.ServantModelLike;
import io.github.flemmli97.tenshilib.client.model.ItemHolderModel;
import io.github.flemmli97.tenshilib.client.render.layer.ItemLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public class ServantItemRender<T extends Mob & ServantModelLike, M extends EntityModel<T> & ItemHolderModel> extends ItemLayer<T, M> {

    public ServantItemRender(RenderLayerParent<T, M> renderer, ItemInHandRenderer itemInHandRenderer) {
        super(renderer, itemInHandRenderer);
    }

    @Override
    protected ItemStack heldItemLeft(T entity, boolean rightHanded) {
        return rightHanded ? entity.getRenderHandStack(InteractionHand.OFF_HAND) : entity.getRenderHandStack(InteractionHand.MAIN_HAND);
    }

    @Override
    protected ItemStack heldItemRight(T entity, boolean rightHanded) {
        return rightHanded ? entity.getRenderHandStack(InteractionHand.MAIN_HAND) : entity.getRenderHandStack(InteractionHand.OFF_HAND);
    }
}
