package com.flemmli97.fatemod.client.gui;

import net.minecraft.client.gui.GuiButton;

public class ButtonTrucePage extends GuiButton{

	public ButtonTrucePage(int buttonId, int x, int y, boolean next) {
		super(buttonId, x, y, 44, 20, next?">":"<");
	}
	
}
