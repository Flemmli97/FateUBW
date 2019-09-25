package com.flemmli97.fatemod.client.gui;

import java.io.IOException;
import java.util.Set;

import com.flemmli97.fatemod.network.MessageHolyGrail;
import com.flemmli97.fatemod.network.PacketHandler;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiHolyGrail extends GuiScreen{

	private final Set<String> rewards;
	
	public GuiHolyGrail(Set<String> rewards)
	{
		this.rewards=rewards;
	}
	
	@Override
	public void initGui() {
		this.buttonList.clear();
		this.labelList.clear();
		int i = 0;
		for(String s : this.rewards)
		{
			this.buttonList.add(new GuiStringButton(0, this.width/2-150 + (i/8*200), this.height/2-80+(i*30), 100, 20, s).setSavedString(s));
			i++;
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if(button instanceof GuiStringButton)
		{
			GuiStringButton sButton = (GuiStringButton) button;
			PacketHandler.sendToServer(new MessageHolyGrail(sButton.getSavedString()));
			this.mc.player.closeScreen();
		}
	}
	
	@Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode))
        {
            this.mc.player.closeScreen();
        }
    }
	
	@Override
	public boolean doesGuiPauseGame() {
	    return false;
	}
}
