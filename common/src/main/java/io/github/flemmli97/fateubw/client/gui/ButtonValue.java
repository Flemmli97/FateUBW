package io.github.flemmli97.fateubw.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.fateubw.Fate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ButtonValue<T> extends Button {

    public boolean selected;
    private T val;
    public static final ResourceLocation guiStuff = new ResourceLocation(Fate.MODID + "textures/gui/buttons.png");
    private final Pressable<T> pressable;

    private int u, v = 46, uMax = 200;

    private ResourceLocation texture = AbstractWidget.WIDGETS_LOCATION;

    public ButtonValue(int x, int y, int widthIn, int heightIn, String buttonText, Pressable<T> press) {
        super(x, y, widthIn, heightIn, new TranslatableComponent(buttonText), (button) -> {
        });
        this.pressable = press;
    }

    public ButtonValue(int x, int y, int widthIn, int heightIn, BaseComponent buttonText, Pressable<T> press) {
        super(x, y, widthIn, heightIn, buttonText, (button) -> {
        });
        this.pressable = press;
    }

    public ButtonValue<T> setVal(T val) {
        this.val = val;
        return this;
    }

    public T getVal() {
        return this.val;
    }

    public ButtonValue<T> setTexture(ResourceLocation res, int u, int v) {
        this.texture = res;
        this.u = u;
        this.v = v;
        this.uMax = this.u + this.width;
        return this;
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.texture);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHoveredOrFocused());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(poseStack, this.x, this.y, this.u, this.v + i * this.height, this.width / 2, this.height);
        this.blit(poseStack, this.x + this.width / 2, this.y, this.uMax - this.width / 2, this.v + i * this.height, this.width / 2, this.height);
        this.renderBg(poseStack, minecraft, mouseX, mouseY);
        int j = this.active ? 16777215 : 10526880;
        drawCenteredString(poseStack, font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24);
        if (this.isHoveredOrFocused()) {
            this.renderToolTip(poseStack, mouseX, mouseY);
        }
    }

    @Override
    public void onPress() {
        this.pressable.press(this);
    }

    public interface Pressable<T> {

        void press(ButtonValue<T> button);
    }
}
