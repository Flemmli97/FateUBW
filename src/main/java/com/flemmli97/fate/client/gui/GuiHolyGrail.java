package com.flemmli97.fate.client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Set;

public class GuiHolyGrail extends Screen {

    private final Set<String> rewards;

    public GuiHolyGrail(Set<String> rewards) {
        super(new TranslationTextComponent("fate.gui.holy_grail"));
        this.rewards = rewards;
    }

    @Override
    protected void init() {
        super.init();
        int i = 0;
        for (String s : this.rewards) {
            this.addButton(new ButtonValue<String>(this.width / 2 - 150 + (i / 8 * 200), this.height / 2 - 80 + (i * 30), 100, 20, s,
                    button -> {
                        //PacketHandler.sendToServer(new C2SG(s));
                        GuiHolyGrail.this.client.player.closeScreen();
                    }).setVal(s));
            i++;
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}