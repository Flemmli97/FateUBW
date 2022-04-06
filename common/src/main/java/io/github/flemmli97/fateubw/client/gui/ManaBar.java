package io.github.flemmli97.fateubw.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;

public class ManaBar extends GuiComponent {
    private Minecraft mc;
    public static int width;

    private static final ResourceLocation texturepath = new ResourceLocation(Fate.MODID + ":textures/gui/mana_bar.png");

    public ManaBar(Minecraft mc) {
        super();
        this.mc = mc;
    }

    public void renderBar(PoseStack stack) {
        int mana = Platform.INSTANCE.getPlayerData(this.mc.player).map(PlayerData::getMana).orElse(0);
        int xPos = Config.Client.manaX;
        int yPos = Config.Client.manaY;
        RenderSystem.setShaderTexture(0, texturepath);
        this.blit(stack, xPos, yPos, 0, 0, 102, 5);

        int manabarwidth = (int) ((mana / 100F) * 102);

        this.blit(stack, xPos, yPos, 0, 5, manabarwidth, 5);
    }
}
