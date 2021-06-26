package io.github.flemmli97.fate.client;

import net.minecraft.item.IItemPropertyGetter;

public class ItemModelProps {

    public static final IItemPropertyGetter activeItemProp = (stack, world, entity) -> entity != null && entity.getActiveItemStack().getItem() == stack.getItem() ? 1 : 0;

}
