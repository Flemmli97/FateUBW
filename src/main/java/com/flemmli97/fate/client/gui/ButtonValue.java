package com.flemmli97.fate.client.gui;

import com.flemmli97.fate.Fate;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.function.Consumer;

public class ButtonValue<T> extends Button {

	public boolean selected;
	private T val;
	protected static final ResourceLocation guiStuff = new ResourceLocation(Fate.MODID + "textures/gui/buttons.png");
	private Consumer<ButtonValue> onPress;

	public ButtonValue(int x, int y, int widthIn, int heightIn, String buttonText, Button.IPressable press) {
		super(x, y, widthIn, heightIn, new TranslationTextComponent(buttonText), press);
	}
	
	public ButtonValue setVal(T val)
	{
		this.val=val;
		return this;
	}

	public ButtonValue setAction(Consumer<ButtonValue> cons){
		this.onPress = cons;
		return this;
	}
	
	public T getVal()
	{
		return this.val;
	}
}
