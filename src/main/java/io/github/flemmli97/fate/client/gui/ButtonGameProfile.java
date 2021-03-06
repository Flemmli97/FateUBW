package io.github.flemmli97.fate.client.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.client.ClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import java.util.UUID;

public class ButtonGameProfile extends Button {

    public boolean selected;
    private GameProfile prof;
    protected static final ResourceLocation guiStuff = new ResourceLocation(Fate.MODID, "textures/gui/player_button.png");
    private State state;

    public ButtonGameProfile(int x, int y, GameProfile prof, Button.IPressable press) {
        super(x, y, 89, 20, new StringTextComponent(prof.getName()), press);
        this.prof = prof;
        this.state = State.getState(this.prof);
    }

    @Override
    public void renderWidget(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            Minecraft mc = Minecraft.getInstance();
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(guiStuff);
            //GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.active ? 2 : this.getYImage(this.isHovered());
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
            GlStateManager.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            this.blit(stack, this.x, this.y, 0, state * 40 + i * 20 - 20, this.width, this.height);
            //this.drawTexturedModalRect(this.x + this.width / 2, this.y, 256 - this.width / 2, state*20+i * 20, this.width / 2, this.height);
            this.renderBg(stack, mc, mouseX, mouseY);
            int j = 14737632;
            if (this.packedFGColor != UNSET_FG_COLOR) {
                j = this.packedFGColor;
            } else if (!this.active) {
                j = 10526880;
            } else if (this.isHovered) {
                j = 16777120;
            }
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