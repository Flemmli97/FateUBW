package com.flemmli97.fate.client.model.servant;

import com.flemmli97.fate.client.model.ModelServant;
import com.flemmli97.fate.common.entity.servant.EntityDiarmuid;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.client.Minecraft;

public class ModelDiarmuid extends ModelServant<EntityDiarmuid> {

    public ModelDiarmuid() {
        super();
    }

    @Override
    public void setAngles(EntityDiarmuid diarmuid, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.setAnglesPre(diarmuid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        if (diarmuid.isStaying()) {

        } else {
            float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
            AnimatedAction anim = diarmuid.getAnimation();
            if (anim != null) {

            }
        }
        this.syncOverlay();
    }
}
