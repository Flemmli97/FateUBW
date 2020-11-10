package com.flemmli97.fate.client.model.servant;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.client.model.ModelServant;
import com.flemmli97.fate.common.entity.servant.EntityCuchulainn;
import com.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ModelCuchulainn extends ModelServant<EntityCuchulainn> {

    public final BlockBenchAnimations anim;

    public ModelCuchulainn() {
        super();
        this.anim = new BlockBenchAnimations(this, new ResourceLocation(Fate.MODID, "models/entity/animation/cuchulainn.json"));
    }

    @Override
    public void setAngles(EntityCuchulainn servant, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.setAnglesPre(servant, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        if (servant.isStaying()) {

        } else {
            float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
            AnimatedAction anim = servant.getAnimation();
            if (anim != null)
                this.anim.doAnimation(anim.getID(), anim.getTick(), partialTicks);
        }
        this.syncOverlay();
    }
}
