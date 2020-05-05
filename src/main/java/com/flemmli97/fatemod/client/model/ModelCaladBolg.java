package com.flemmli97.fatemod.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

/**
 * CaladBolg - Undefined
 * Created using Tabula 6.0.0
 */
public class ModelCaladBolg extends ModelBase {
    public ModelRenderer point;
    public ModelRenderer blade1;
    public ModelRenderer blade2;
    public ModelRenderer guard;
    public ModelRenderer handle;

    public ModelCaladBolg() {
        this.textureWidth = 35;
        this.textureHeight = 34;
        this.point = new ModelRenderer(this, 0, 0);
        this.point.setRotationPoint(0.0F, 0F, 0.0F);
        this.point.addBox(0.0F, 0.0F, 0.0F, 1, 1, 5, 0.0F);
        this.blade1 = new ModelRenderer(this, 0, 6);
        this.blade1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.blade1.addBox(-1.0F, -1.0F, 5.0F, 3, 3, 7, 0.0F);
        this.blade2 = new ModelRenderer(this, 0, 16);
        this.blade2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.blade2.addBox(-2.0F, -2.0F, 12.0F, 5, 5, 13, 0.0F);
        this.guard = new ModelRenderer(this, 19, 0);
        this.guard.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.guard.addBox(-3.0F, -3.0F, 25.0F, 7, 7, 1, 0.0F);
        this.handle = new ModelRenderer(this, 19, 8);
        this.handle.setRotationPoint(0F, 0F, 0F);
        this.handle.addBox(-0.5F, -0.5F, 26.0F, 2, 2, 3, 0.0F);
        this.point.addChild(this.blade1);
        this.point.addChild(this.blade2);
        this.point.addChild(this.guard);
        this.point.addChild(this.handle);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float scale) {
    		GlStateManager.pushMatrix();
    		double speed = Math.sqrt(entity.motionX*entity.motionX + entity.motionY*entity.motionY + entity.motionZ*entity.motionZ);
    		if(speed <1.0)
    			speed=1.0;
    		GlStateManager.scale(0.4, 0.4, speed*1.5);
        this.point.render(scale);
        GlStateManager.popMatrix();
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
