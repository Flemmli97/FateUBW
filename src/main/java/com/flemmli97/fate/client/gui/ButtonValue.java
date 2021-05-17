package com.flemmli97.fate.client.gui;

import com.flemmli97.fate.Fate;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class ButtonValue<T> extends Button {

    public boolean selected;
    private T val;
    protected static final ResourceLocation guiStuff = new ResourceLocation(Fate.MODID + "textures/gui/buttons.png");
    private final Pressable<T> pressable;

    @SuppressWarnings("unchecked")
    public ButtonValue(int x, int y, int widthIn, int heightIn, String buttonText, Pressable<T> press) {
        super(x, y, widthIn, heightIn, new TranslationTextComponent(buttonText), (button) -> {
        });
        this.pressable = press;
    }

    public ButtonValue<T> setVal(T val) {
        this.val = val;
        return this;
    }

    public T getVal() {
        return this.val;
    }

    @Override
    public void onPress() {
        this.pressable.press(this);
    }

    public static interface Pressable<T> {

        void press(ButtonValue<T> button);
    }
}
