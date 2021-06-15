package io.github.flemmli97.fate.client.render;

import com.flemmli97.tenshilib.client.model.IItemArmModel;
import com.flemmli97.tenshilib.client.render.ItemLayer;
import io.github.flemmli97.fate.common.entity.servant.EntityServant;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class LayerHand<T extends EntityServant, M extends EntityModel<T> & IItemArmModel> extends ItemLayer<T, M> {

    private static final ItemStack genericWeapon = new ItemStack(Items.IRON_SWORD);

    public LayerHand(IEntityRenderer<T, M> renderer) {
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
