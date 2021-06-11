package com.flemmli97.fate.client.gui;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.capability.IPlayer;
import com.flemmli97.fate.common.capability.PlayerCapProvider;
import com.flemmli97.fate.common.config.Config;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ManaBar extends AbstractGui {
    private Minecraft mc;
    public static int width;

    private static final ResourceLocation texturepath = new ResourceLocation(Fate.MODID + ":textures/gui/mana_bar.png");

    public ManaBar(Minecraft mc) {
        super();
        this.mc = mc;
    }

    public void renderBar(MatrixStack stack) {
        int mana = this.mc.player.getCapability(PlayerCapProvider.PlayerCap).map(IPlayer::getMana).orElse(0);
        int xPos = Config.Client.manaX;
        int yPos = Config.Client.manaY;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glDisable(GL11.GL_LIGHTING);

        this.mc.getTextureManager().bindTexture(texturepath);

        this.blit(stack, xPos, yPos, 0, 0, 102, 5);

        int manabarwidth = (int) ((mana / 100F) * 102);

        this.blit(stack, xPos, yPos, 0, 5, manabarwidth, 5);
    }
}
