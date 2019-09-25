package com.flemmli97.fatemod.client.gui;

import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;


public class GuiStringButton extends GuiButton{

	public boolean selected;
	private String string="";
	protected static final ResourceLocation guiStuff = new ResourceLocation(LibReference.MODID + "textures/gui/buttons.png");

	public GuiStringButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
	}
	
	public GuiStringButton setSavedString(String uuid)
	{
		this.string=uuid;
		return this;
	}
	
	public String getSavedString()
	{
		return this.string;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTick) {
		super.drawButton(mc, mouseX, mouseY, partialTick);
	}
}
