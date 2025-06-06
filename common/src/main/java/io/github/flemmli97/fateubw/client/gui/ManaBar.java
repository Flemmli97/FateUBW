package io.github.flemmli97.fateubw.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.common.config.ClientConfig;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;

public class ManaBar extends GuiComponent {

    private final Minecraft mc;

    private static final ResourceLocation TEXTUREPATH = new ResourceLocation(Fate.MODID + ":textures/gui/mana_bar.png");

    public ManaBar(Minecraft mc) {
        super();
        this.mc = mc;
    }

    public void renderBar(PoseStack stack) {
        int mana = Platform.INSTANCE.getPlayerData(this.mc.player).map(PlayerData::getMana).orElse(0);
        int guiWidth = this.mc.getWindow().getGuiScaledWidth();
        int guiHeight = this.mc.getWindow().getGuiScaledHeight();
        int width = 101;
        int height = 7;
        int xPos = ClientConfig.manaBarPosition.positionX(guiWidth, width, ClientConfig.manaX);
        int yPos = ClientConfig.manaBarPosition.positionY(guiHeight, height, ClientConfig.manaY);
        RenderSystem.setShaderTexture(0, TEXTUREPATH);
        this.blit(stack, xPos, yPos, 0, 0, width, height);
        double perc = (mana / 100.);
        int manabarwidth = (int) (perc * (width - 2));
        this.blit(stack, xPos + 1, yPos + 1, 1, 8, manabarwidth, height - 2);
    }
}
