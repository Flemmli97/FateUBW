package io.github.flemmli97.fateubw.client.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class ButtonGameProfile extends Button {

    public boolean selected;
    private GameProfile prof;
    protected static final ResourceLocation guiStuff = new ResourceLocation(Fate.MODID, "textures/gui/player_button.png");
    private State state;

    public ButtonGameProfile(int x, int y, GameProfile prof, Button.OnPress press) {
        super(x, y, 89, 20, new TextComponent(prof.getName()), press);
        this.prof = prof;
        this.state = State.getState(this.prof);
    }

    @Override
    public void renderButton(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            Minecraft mc = Minecraft.getInstance();
            Font fontrenderer = mc.font;
            RenderSystem.setShaderTexture(0, guiStuff);
            //GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.active ? 2 : this.getYImage(this.isHovered);
            int state = 0;
            switch (this.state) {
                case NONE:
                    state = 0;
                    break;
                case PENDING:
                    state = 1;
                    break;
                case REQUESTED:
                    state = 2;
                    break;
                case TRUCE:
                    state = 3;
                    break;
            }
            GlStateManager._enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            this.blit(stack, this.x, this.y, 0, state * 40 + i * 20 - 20, this.width, this.height);
            //this.drawTexturedModalRect(this.x + this.width / 2, this.y, 256 - this.width / 2, state*20+i * 20, this.width / 2, this.height);
            this.renderBg(stack, mc, mouseX, mouseY);
            int j = this.active ? 16777215 : 10526880;
            drawCenteredString(stack, fontrenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
        }
    }

    public UUID getUUID() {
        return this.prof.getId();
    }

    public State getState() {
        return this.state;
    }

    public void refreshState() {
        this.state = State.getState(this.prof);
    }

    public enum State {
        NONE,
        PENDING,
        REQUESTED,
        TRUCE;

        public static State getState(GameProfile prof) {
            if (ClientHandler.pending.contains(prof))
                return PENDING;
            if (ClientHandler.requests.contains(prof))
                return REQUESTED;
            if (ClientHandler.truce.contains(prof))
                return TRUCE;
            return NONE;
        }
    }
}