package com.flemmli97.fatemod.client.gui;

import org.lwjgl.opengl.GL11;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ManaBar extends Gui {
	private Minecraft mc;
	public static int width;

	private static final ResourceLocation texturepath = new ResourceLocation(Fate.MODID + ":textures/gui/mana_bar.png");

	public ManaBar(Minecraft mc)
	{
		this.mc = mc;
		this.renderBar();
	}

	private void renderBar() {
		IPlayer cap = this.mc.thePlayer.getCapability(PlayerCapProvider.PlayerCap, null);
		if (cap == null)
		{
			return;
		}
		int xPos = 2;
		int yPos = 2;

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		GL11.glDisable(GL11.GL_LIGHTING);
		
		this.mc.getTextureManager().bindTexture(texturepath);

		this.drawTexturedModalRect(xPos, yPos, 0, 0, 102, 5);

		int manabarwidth = (int)((cap.getMana() / 100) * 102);

		this.drawTexturedModalRect(xPos, yPos, 0, 5, manabarwidth, 5);
	}
}
