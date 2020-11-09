package com.flemmli97.fate.client.model.servant;

import com.flemmli97.fate.client.model.ModelServant;
import com.flemmli97.fate.common.entity.servant.EntityCuchulainn;
import com.flemmli97.fate.common.entity.servant.EntityGilgamesh;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.client.Minecraft;

public class ModelGilgamesh extends ModelServant<EntityGilgamesh> {

    public ModelGilgamesh() {
        super();
    }

    @Override
    public void setAngles(EntityGilgamesh gilgamesh, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.setAnglesPre(gilgamesh, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        if (gilgamesh.isStaying()) {

        } else {
            float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
            AnimatedAction anim = gilgamesh.getAnimation();
            if (anim != null) {

            }
        }
        this.syncOverlay();
    }
}
