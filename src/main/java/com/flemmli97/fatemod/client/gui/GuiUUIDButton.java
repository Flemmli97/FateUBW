package com.flemmli97.fatemod.client.gui;

import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;


public class GuiUUIDButton extends GuiButton{

	public boolean selected;
	private String uuid="";
	protected static final ResourceLocation guiStuff = new ResourceLocation(LibReference.MODID + "textures/gui/buttons.png");

	public GuiUUIDButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
	}
	
	public void setUUID(String uuid)
	{
		this.uuid=uuid;
	}
	
	public String getUUID()
	{
		return this.uuid;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTick) {
		super.drawButton(mc, mouseX, mouseY, partialTick);
	}
}
