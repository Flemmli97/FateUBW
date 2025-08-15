package io.github.flemmli97.fateubw.client.gui;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.config.ClientConfig;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ManaBar {

    private final Minecraft mc;

    private static final ResourceLocation MANA_BAR_BACKGROUND = Fate.modRes("hud/mana_bar_background");
    private static final ResourceLocation MANA_BAR = Fate.modRes("hud/mana_bar");

    public ManaBar(Minecraft mc) {
        this.mc = mc;
    }

    public void renderBar(GuiGraphics graphics, DeltaTracker tracker) {
        if (this.mc.player.isCreative() || this.mc.player.isSpectator() || this.mc.options.hideGui)
            return;
        int mana = Platform.INSTANCE.getPlayerData(this.mc.player).getMana();
        int guiWidth = this.mc.getWindow().getGuiScaledWidth();
        int guiHeight = this.mc.getWindow().getGuiScaledHeight();
        int width = 101;
        int height = 7;
        int xPos = ClientConfig.manaBarPosition.positionX(guiWidth, width, ClientConfig.manaX);
        int yPos = ClientConfig.manaBarPosition.positionY(guiHeight, height, ClientConfig.manaY);
        graphics.blitSprite(MANA_BAR_BACKGROUND, xPos, yPos, width, height);
        double perc = (mana / 100.);
        int manabarwidth = (int) (perc * (width - 2));
        graphics.blitSprite(MANA_BAR, xPos, yPos, manabarwidth, height);
    }
}
