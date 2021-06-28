package io.github.flemmli97.fate.client.gui;

import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.network.C2SServantSpecial;
import io.github.flemmli97.fate.network.PacketHandler;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class ButtonSpecial extends AbstractButton {

    private String id;
    protected static final ResourceLocation guiStuff = new ResourceLocation(Fate.MODID + "textures/gui/buttons.png");

    public ButtonSpecial(int x, int y, int widthIn, int heightIn, String specialID) {
        super(x, y, widthIn, heightIn, new TranslationTextComponent(specialID));
        this.id = specialID;
    }

    public String getID() {
        return this.id;
    }

    @Override
    public void onPress() {
        PacketHandler.sendToServer(new C2SServantSpecial(this.id));
    }
}
