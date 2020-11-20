package com.flemmli97.fate.client.model;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.entity.EntityHassanCopy;
import com.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ModelHassanCopy extends ModelServant<EntityHassanCopy> {

    public final BlockBenchAnimations anim;

    public ModelHassanCopy() {
        super();
        this.anim = new BlockBenchAnimations(this, new ResourceLocation(Fate.MODID, "models/entity/animation/hassan.json"));
    }

    @Override
    public void setAngles(EntityHassanCopy servant, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.setAnglesPre(servant, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
        AnimatedAction anim = servant.getAnimation();
        if (anim != null)
            this.anim.doAnimation(anim.getID(), anim.getTick(), partialTicks);
        this.syncOverlay();
    }
}
