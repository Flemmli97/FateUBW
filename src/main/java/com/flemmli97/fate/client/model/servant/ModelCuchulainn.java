package com.flemmli97.fate.client.model.servant;

import com.flemmli97.fate.client.model.ModelServant;
import com.flemmli97.fate.common.entity.servant.EntityCuchulainn;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.client.Minecraft;

public class ModelCuchulainn extends ModelServant<EntityCuchulainn> {

    public ModelCuchulainn() {
        super();
    }

    @Override
    public void setAngles(EntityCuchulainn cuchulainn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.setAnglesPre(cuchulainn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        if (cuchulainn.isStaying()) {

        } else {
            float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
            AnimatedAction anim = cuchulainn.getAnimation();
            if (anim != null) {
                //if(anim.getID().equals("gae_bolg"))
                //	this.gae_bolg.animate(anim.getTick(), partialTicks);
            }
        }
        this.syncOverlay();
    }
}
