package com.flemmli97.fate.client.model.servant;

import com.flemmli97.fate.client.model.ModelServant;
import com.flemmli97.fate.common.entity.servant.EntityEmiya;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.client.Minecraft;

public class ModelEmiya extends ModelServant<EntityEmiya> {

    public ModelEmiya() {
        super();
    }

    @Override
    public void setAngles(EntityEmiya emiya, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.setAnglesPre(emiya, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        if (emiya.isStaying()) {

        } else {
            float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
            AnimatedAction anim = emiya.getAnimation();
            if (anim != null) {

            }
        }
        this.syncOverlay();
    }
}
