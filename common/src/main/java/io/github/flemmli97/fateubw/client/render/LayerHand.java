package io.github.flemmli97.fateubw.client.render;

import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.tenshilib.client.model.IItemArmModel;
import io.github.flemmli97.tenshilib.client.render.ItemLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class LayerHand<T extends BaseServant, M extends EntityModel<T> & IItemArmModel> extends ItemLayer<T, M> {

    private static final ItemStack genericWeapon = new ItemStack(Items.IRON_SWORD);

    public LayerHand(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Override
    protected ItemStack heldItemLeft(T entity, boolean rightHanded) {
        return ServantRenderer.showIdentity(entity) ? super.heldItemLeft(entity, rightHanded) : ItemStack.EMPTY;
    }

    @Override
    protected ItemStack heldItemRight(T entity, boolean rightHanded) {
        return ServantRenderer.showIdentity(entity) ? super.heldItemRight(entity, rightHanded) : genericWeapon;
    }
}
