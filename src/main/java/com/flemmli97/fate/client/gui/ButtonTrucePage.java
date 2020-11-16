package com.flemmli97.fate.client.gui;

import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.util.text.StringTextComponent;

public class ButtonTrucePage extends AbstractButton {

	public ButtonTrucePage(int x, int y, boolean next) {
		super(x, y, 44, 20, new StringTextComponent(next?">":"<"));
	}

	@Override
	public void onPress() {

	}
}
