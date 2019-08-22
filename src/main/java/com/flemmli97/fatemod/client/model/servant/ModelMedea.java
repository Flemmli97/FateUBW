package com.flemmli97.fatemod.client.model.servant;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Medea_Normal - Black_Saturn
 * Created using Tabula 4.1.1
 */
public class ModelMedea extends ModelBiped {
    public ModelRenderer Hat;
    public ModelRenderer LeftArm;
    public ModelRenderer Body;
    public ModelRenderer RightArm;
    public ModelRenderer LeftLeg;
    public ModelRenderer RightLeg;
    public ModelRenderer Head;
    public ModelRenderer CloakBody;
    public ModelRenderer CloakLegs;


    public ModelMedea() {
    	super();
        this.textureWidth = 64;
        this.textureHeight = 72;
        this.RightLeg = new ModelRenderer(this, 0, 16);
        this.RightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.RightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.Head = new ModelRenderer(this, 0, 0);
        this.Head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.Body = new ModelRenderer(this, 16, 16);
        this.Body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
        this.LeftLeg = new ModelRenderer(this, 0, 16);
        this.LeftLeg.mirror = true;
        this.LeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.LeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.RightArm = new ModelRenderer(this, 40, 16);
        this.RightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.RightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.CloakBody = new ModelRenderer(this, 0, 54);
        this.CloakBody.setRotationPoint(-8.5F, -0.2F, -2.5F);
        this.CloakBody.addBox(0.0F, 0.0F, 0.0F, 17, 13, 5, 0.0F);
        this.Hat = new ModelRenderer(this, 32, 0);
        this.Hat.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Hat.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
        this.LeftArm = new ModelRenderer(this, 40, 16);
        this.LeftArm.mirror = true;
        this.LeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.LeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.CloakLegs = new ModelRenderer(this, 34, 36);
        this.CloakLegs.setRotationPoint(-4.5F, 12.8F, -2.5F);
        this.CloakLegs.addBox(0.0F, 0.0F, 0.0F, 9, 11, 5, 0.0F);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    	this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        this.RightLeg.render(scale);
        this.Head.render(scale);
        this.Body.render(scale);
        this.LeftLeg.render(scale);
        this.RightArm.render(scale);
        this.CloakBody.render(scale);
        this.Hat.render(scale);
        this.LeftArm.render(scale);
        this.CloakLegs.render(scale);
    }
    
    @Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity)
    {
    	this.Head.rotateAngleY = netHeadYaw / (180F / (float)Math.PI);
        this.Head.rotateAngleX = headPitch / (180F / (float)Math.PI);
        this.Hat.rotateAngleY = netHeadYaw / (180F / (float)Math.PI);
        this.Hat.rotateAngleX = headPitch / (180F / (float)Math.PI);
    }

}
