package com.flemmli97.fate.client.gui;

import com.flemmli97.fate.Fate;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class ButtonValue<T> extends Button {

    public boolean selected;
    private T val;
    protected static final ResourceLocation guiStuff = new ResourceLocation(Fate.MODID + "textures/gui/buttons.png");

    public ButtonValue(int x, int y, int widthIn, int heightIn, String buttonText, Button.IPressable press) {
        super(x, y, widthIn, heightIn, new TranslationTextComponent(buttonText), press);
    }

    public ButtonValue<T> setVal(T val) {
        this.val = val;
        return this;
    }

    public T getVal() {
        return this.val;
    }
}
