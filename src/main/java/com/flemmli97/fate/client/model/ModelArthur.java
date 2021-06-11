package com.flemmli97.fate.client.model;

import com.flemmli97.fate.common.entity.servant.EntityArthur;
import com.flemmli97.tenshilib.client.model.ModelUtils;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.client.Minecraft;

public class ModelArthur extends ModelServant<EntityArthur> {

    public ModelArthur() {
        super("arthur");
    }

    @Override
    public void setRotationAngles(EntityArthur servant, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.setAnglesPre(servant, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        if (servant.isStaying()) {
            this.servantRightArmUp.rotateAngleX = ModelUtils.degToRad(-28.70F);
            this.servantLeftArmUp.rotateAngleX = ModelUtils.degToRad(-65.0F);

            this.servantRightArmUp.rotateAngleY = ModelUtils.degToRad(-15.0F);
            this.servantLeftArmUp.rotateAngleY = ModelUtils.degToRad(35.0F);

            this.servantRightArmJoint.rotateAngleX = ModelUtils.degToRad(28.7F);
            this.servantLeftArmJoint.rotateAngleX = ModelUtils.degToRad(65.0F);

            this.servantRightArmDown.rotateAngleX = ModelUtils.degToRad(-90.0F);
            this.servantLeftArmDown.rotateAngleX = ModelUtils.degToRad(-90.0F);

            this.servantRightArmDown.rotateAngleY = ModelUtils.degToRad(-18.26F);
            this.servantLeftArmDown.rotateAngleY = ModelUtils.degToRad(26.09F);

            this.servantLeftArmDown.rotateAngleZ = ModelUtils.degToRad(-10.43F);

            this.servantLeftLegUp.rotateAngleX = ModelUtils.degToRad(6.0F);
            this.servantLeftLegUp.rotateAngleY = ModelUtils.degToRad(-5.0F);

            this.servantRightLegUp.rotateAngleX = ModelUtils.degToRad(-14);
            this.servantRightLegDown.rotateAngleX = ModelUtils.degToRad(14);
        } else {
            float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
            AnimatedAction anim = servant.getAnimation();
            if (anim != null)
                this.anim.doAnimation(anim.getID(), anim.getTick(), partialTicks);
        }
        this.syncOverlay();
    }
}
