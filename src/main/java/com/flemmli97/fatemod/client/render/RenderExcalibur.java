package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.entity.EntityExcalibur;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderExcalibur<T extends EntityExcalibur> extends RenderProjectileBase<T>{
	
    private static final ResourceLocation tex = new ResourceLocation(Fate.MODID + ":textures/entity/excalibur.png");
    public float radiusPoint = 1.2F;
    private final double texWidght = 64;
    private final double texHeight = 32;
    public RenderExcalibur(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }  

	@Override
	public boolean shouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ) {
		return true;
	}

	@Override
	public void doRender(T projectile, double x, double y, double z, float entityYaw, float partialTick) {
		/*double[] f = projectile.getHitPos();
		Vec3d length = new Vec3d(f[0],f[1],f[2]).subtract(projectile.getPositionVector());
        float[] rot = projectile.getRotation(); 
        float yaw = rot[0];
        float pitch = rot[1];
		this.bindEntityTexture(projectile);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        this.drawStartOrEnd(x,y,z,-(yaw+90), -(pitch+90), radiusPoint/2F); //end
		this.drawStartOrEnd(x+length.xCoord, y+length.yCoord, z+length.zCoord,-(yaw+90), -(pitch+90), radiusPoint);	//start
		GlStateManager.translate(x, y, z);
		GlStateManager.rotate(-yaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);

		this.drawBeam(length.lengthVector());
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();*/
	}
	
	public void drawStartOrEnd(double x, double y, double z, float yaw, float pitch, float radius)
	{
		GlStateManager.translate(x, y, z);
		GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(pitch, 0.0F, 0.0F, 1.0F);
		Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buf = tessellator.getBuffer();
        buf.begin(7, DefaultVertexFormats.POSITION_TEX);
        buf.pos(-radius, 0, -radius).tex(0/texWidght, 0/texHeight).endVertex();
        buf.pos(-radius, 0, radius).tex(0/texWidght, 32/texHeight).endVertex();
        buf.pos(radius, 0, radius).tex(32/texWidght, 32/texHeight).endVertex();
        buf.pos(radius, 0, -radius).tex(32/texWidght, 0/texHeight).endVertex();
        tessellator.draw();
        GlStateManager.rotate(pitch, 0.0F, 0.0F, -1.0F);
        GlStateManager.rotate(yaw, 0.0F, -1.0F, 0.0F);
		GlStateManager.translate(-x, -y, -z);
	}
	
	public void drawBeam(double length)
	{
		for(int x = 0; x < 4; x ++)
		{
            GlStateManager.rotate(45.0F, 0.0F, 0.0F, 1.0F);
			Tessellator tessellator = Tessellator.getInstance();
	        VertexBuffer buf = tessellator.getBuffer();
	        buf.begin(7, DefaultVertexFormats.POSITION_TEX);
	        buf.pos(-radiusPoint/2, 0, 0).tex(64/texWidght, 32/texHeight).endVertex();
	        buf.pos(-radiusPoint, 0, length).tex(64/texWidght, 32/texHeight).endVertex();
	        buf.pos(radiusPoint, 0, length).tex(32/texWidght, 0/texHeight).endVertex();
	        buf.pos(radiusPoint/2, 0, 0).tex(32/texWidght, 0/texHeight).endVertex();
	
	        tessellator.draw();
		}
	}

	@Override
	public ItemStack renderItemStackFromEntity(T entity) {
		return null;
	}

	@Override
	public int getItemType() {
		return 0;
	}
	
	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return tex;
	}

}
