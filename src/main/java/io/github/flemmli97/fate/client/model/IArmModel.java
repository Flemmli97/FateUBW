package io.github.flemmli97.fate.client.model;

import com.flemmli97.tenshilib.client.model.IItemArmModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.Hand;

public interface IArmModel extends IItemArmModel {

    ModelRenderer getHand(Hand side);
}
