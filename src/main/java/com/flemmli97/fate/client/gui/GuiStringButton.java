package com.flemmli97.fate.client.gui;

import com.flemmli97.fate.Fate;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.function.Consumer;

public class GuiStringButton extends AbstractButton {

	public boolean selected;
	private String string="";
	protected static final ResourceLocation guiStuff = new ResourceLocation(Fate.MODID + "textures/gui/buttons.png");
	private Consumer<GuiStringButton> onPress;

	public GuiStringButton(int x, int y, int widthIn, int heightIn, String buttonText) {
		super(x, y, widthIn, heightIn, new TranslationTextComponent(buttonText));
	}
	
	public GuiStringButton setSavedString(String uuid)
	{
		this.string=uuid;
		return this;
	}

	public GuiStringButton setAction(Consumer<GuiStringButton> cons){
		this.onPress = cons;
		return this;
	}
	
	public String getSavedString()
	{
		return this.string;
	}

	@Override
	public void onPress() {
		if(this.onPress!=null)
			this.onPress.accept(this);
	}
}
