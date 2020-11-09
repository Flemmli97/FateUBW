package com.flemmli97.fate.client.model.servant;

import com.flemmli97.fate.client.model.ModelServant;
import com.flemmli97.fate.common.entity.servant.EntityCuchulainn;
import com.flemmli97.fate.common.entity.servant.EntityGilles;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.client.Minecraft;

public class ModelGilles extends ModelServant<EntityGilles> {

    public ModelGilles() {
        super();
    }

    @Override
    public void setAngles(EntityGilles gilles, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.setAnglesPre(gilles, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        if (gilles.isStaying()) {

        } else {
            float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
            AnimatedAction anim = gilles.getAnimation();
            if (anim != null) {

            }
        }
        this.syncOverlay();
    }
}
