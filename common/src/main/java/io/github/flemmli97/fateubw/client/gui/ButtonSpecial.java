package io.github.flemmli97.fateubw.client.gui;

import io.github.flemmli97.fateubw.common.network.C2SServantSpecial;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.TranslatableComponent;

public class ButtonSpecial extends AbstractButton {

    private String id;

    public ButtonSpecial(int x, int y, int widthIn, int heightIn, String specialID) {
        super(x, y, widthIn, heightIn, new TranslatableComponent(specialID));
        this.id = specialID;
    }

    public String getID() {
        return this.id;
    }

    @Override
    public void onPress() {
        NetworkCalls.INSTANCE.sendToServer(new C2SServantSpecial(this.id));
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {

    }
}
