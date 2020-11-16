package com.flemmli97.fate.client.gui;

import com.flemmli97.fate.network.PacketHandler;
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
			this.addButton(new GuiStringButton(this.width / 2 - 150 + (i / 8 * 200), this.height / 2 - 80 + (i * 30), 100, 20, s).setSavedString(s)
					.setAction(button -> {
						//PacketHandler.sendToServer(new C2SG(s));
						GuiHolyGrail.this.client.player.closeScreen();
					}));
			i++;
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
