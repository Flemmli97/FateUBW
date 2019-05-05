package com.flemmli97.fatemod.client.render.layer;

import java.lang.reflect.Method;

import org.lwjgl.opengl.GL11;

import com.flemmli97.fatemod.client.model.servant.ModelServant;
import com.flemmli97.fatemod.client.render.servant.RenderServant;
import com.flemmli97.fatemod.common.entity.servant.EntityLancelot;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.common.javahelper.ReflectionUtils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerHand extends LayerHeldItem
{
    private static final ItemStack genericWeapon = new ItemStack(Items.IRON_SWORD);

    public LayerHand(RenderLivingBase<?> renderLiving)
    {
    	super(renderLiving);
    }

    @Override
    public void doRenderLayer(EntityLivingBase servant, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	if(servant instanceof EntityServant)
    	{
	    	if(RenderServant.showIdentity((EntityServant) servant))
	    	{
	    		boolean flag = servant.getPrimaryHand() == EnumHandSide.RIGHT;
	            ItemStack itemstack = flag ? servant.getHeldItemOffhand() : servant.getHeldItemMainhand();
	            ItemStack itemstack1 = flag ? servant.getHeldItemMainhand() : servant.getHeldItemOffhand();

	            if (!itemstack.isEmpty() || !itemstack1.isEmpty())
	            {
	                GlStateManager.pushMatrix();

	                if (this.livingEntityRenderer.getMainModel().isChild)
	                {
	                    GlStateManager.translate(0.0F, 0.75F, 0.0F);
	                    GlStateManager.scale(0.5F, 0.5F, 0.5F);
	                }

	                this.renderHeldItem(servant, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);	           
	                this.renderHeldItem(servant, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT);
	                if(servant instanceof EntityLancelot)
	                {
	                	renderCorruptedEffect(servant, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, false);
	                	renderCorruptedEffect(servant, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, true);
	                }
	                GlStateManager.popMatrix();
	            }
	    	}
	    	else
	    	{
	    		EnumHandSide side = servant.getPrimaryHand();
	            boolean leftHanded = side == EnumHandSide.LEFT;
	    		ItemCameraTransforms.TransformType trans = leftHanded?TransformType.THIRD_PERSON_LEFT_HAND:TransformType.THIRD_PERSON_RIGHT_HAND;	
	    		this.renderHeldItem(servant, genericWeapon, trans, side);
	    	}
    	}
    }
    
    private void renderHeldItem(EntityLivingBase entity, ItemStack stack, ItemCameraTransforms.TransformType camera, EnumHandSide handSide)
    {
        if (!stack.isEmpty())
        {
            GlStateManager.pushMatrix();

            if (entity.isSneaking())
            {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }
            // Forge: moved this call down, fixes incorrect offset while sneaking.
            this.translateToHand(handSide);
            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            boolean flag = handSide == EnumHandSide.LEFT;
            GlStateManager.translate((float)(flag ? -1 : 1) / 16.0F, 0.125F, -0.625F);
            Minecraft.getMinecraft().getItemRenderer().renderItemSide(entity, stack, camera, flag);
            GlStateManager.popMatrix();
        }
    }
    
    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    private static final Method renderModel = ObfuscationReflectionHelper.findMethod(RenderItem.class, "func_191965_a", Void.TYPE, IBakedModel.class, Integer.TYPE);
    
    private void renderCorruptedEffect(EntityLivingBase entitylivingbaseIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform, boolean leftHanded)
    {
    	if(heldStack.isEmpty() || heldStack.getItem()==ModItems.arondight)
    		return;
    	Item item = heldStack.getItem();
        Block block = Block.getBlockFromItem(item);
        
        GlStateManager.pushMatrix();
        if (entitylivingbaseIn.isSneaking())
        {
            GlStateManager.translate(0.0F, 0.2F, 0.0F);
        }
        // Forge: moved this call down, fixes incorrect offset while sneaking.
        this.translateToHand(leftHanded?EnumHandSide.LEFT:EnumHandSide.RIGHT);
        GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate((float)(leftHanded ? -1 : 1) / 16.0F, 0.125F, -0.625F);     
        //ItemRenderer.renderItemSide
        boolean flag = Minecraft.getMinecraft().getRenderItem().shouldRenderItemIn3D(heldStack) && block.getBlockLayer() == BlockRenderLayer.TRANSLUCENT;

        if (flag)
        {
            GlStateManager.depthMask(false);
        }
        //ItemRenderer.renderItem
        IBakedModel ibakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(heldStack, entitylivingbaseIn.world, entitylivingbaseIn);
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.pushMatrix();
        ibakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(ibakedmodel, transform, leftHanded);
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);

        if (ibakedmodel.isBuiltInRenderer())
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableRescaleNormal();
            heldStack.getItem().getTileEntityItemStackRenderer().renderByItem(heldStack);
        }
        else
        {
            renderEffect(ibakedmodel);
        }

        GlStateManager.popMatrix();
        GlStateManager.cullFace(GlStateManager.CullFace.BACK);
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();

        if (flag)
        {
            GlStateManager.depthMask(true);
        }

        GlStateManager.popMatrix();
    }
    
    private static void renderEffect(IBakedModel model)
    {
        GlStateManager.depthMask(false);
        GlStateManager.depthFunc(GL11.GL_EQUAL);
        GlStateManager.disableLighting();
        GlStateManager.color(0.99f, 0.99f, 0.99f);
        //GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR);
        Minecraft.getMinecraft().getTextureManager().bindTexture(RES_ITEM_GLINT);
        GlStateManager.matrixMode(GL11.GL_TEXTURE);
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
        GlStateManager.translate(f, 0.0F, 0.0F);
        GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
        ReflectionUtils.invokeMethod(renderModel, Minecraft.getMinecraft().getRenderItem(), model, -8372020);        
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f1 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
        GlStateManager.translate(-f1, 0.0F, 0.0F);
        GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
        ReflectionUtils.invokeMethod(renderModel, Minecraft.getMinecraft().getRenderItem(), model, -8372020);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableLighting();
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        GlStateManager.depthMask(true);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    }
    
    @Override
	protected void translateToHand(EnumHandSide handSide)
    {
        ((ModelServant) this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F, handSide);
    }

}