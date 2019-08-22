package com.flemmli97.fatemod.client.gui;

import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.fatemod.network.MessageServantSpecial;
import com.flemmli97.fatemod.network.PacketHandler;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;


public class ButtonSpecial extends GuiButton{

	private String id = "";
	protected static final ResourceLocation guiStuff = new ResourceLocation(LibReference.MODID + "textures/gui/buttons.png");

	public ButtonSpecial(int buttonId, int x, int y, int widthIn, int heightIn, String specialID) {
		super(buttonId, x, y, widthIn, heightIn, I18n.format(specialID));
		this.id=specialID;
	}
	
	public String getID()
	{
		return this.id;
	}
	
	public void onPressed()
	{
		PacketHandler.sendToServer(new MessageServantSpecial(this.id));
	}
}
