package io.github.flemmli97.fateubw.mixinhelper;

import io.github.flemmli97.fateubw.client.ItemModelProps;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;

public class ClientMixinUtils {

    public static boolean renderCorruptedItem;

    public static void adjustForHeldModel(ItemStack itemStack, ItemTransforms.TransformType transformType) {
        ItemModelProps.HELD_TYPE = switch (transformType) {
            case FIRST_PERSON_LEFT_HAND, THIRD_PERSON_LEFT_HAND, FIRST_PERSON_RIGHT_HAND, THIRD_PERSON_RIGHT_HAND -> 1;
            default -> 0;
        };
    }

    public static void resetHeldModel() {
        ItemModelProps.HELD_TYPE = 0;
    }
}
