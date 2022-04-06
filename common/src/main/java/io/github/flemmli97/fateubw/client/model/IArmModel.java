package io.github.flemmli97.fateubw.client.model;

import io.github.flemmli97.tenshilib.client.model.IItemArmModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartHandler;
import net.minecraft.world.InteractionHand;

public interface IArmModel extends IItemArmModel {

    ModelPartHandler.ModelPartExtended getHand(InteractionHand side);
}
