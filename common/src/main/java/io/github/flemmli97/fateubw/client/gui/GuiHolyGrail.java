package io.github.flemmli97.fateubw.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.network.C2SGrailReward;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class GuiHolyGrail extends Screen {

    private static final ResourceLocation tex = new ResourceLocation(Fate.MODID, "textures/gui/grail_reward.png");

    private final Map<ResourceLocation, BaseComponent> rewards;
    private int page;

    private static final int xSize = 255, ySize = 186;

    public GuiHolyGrail(Map<ResourceLocation, String> rewards) {
        super(new TranslatableComponent("fate.gui.holy_grail"));
        this.rewards = rewards.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> {
            String key = "grail.loot." + e.getKey().toString();
            TranslatableComponent translationTextComponent = new TranslatableComponent(key);
            StringBuilder b = new StringBuilder();
            translationTextComponent.visitSelf(s -> {
                b.append(s);
                return Optional.empty();
            });
            if (b.toString().equals(key))
                return new TextComponent(e.getValue());
            return translationTextComponent;
        }));
    }

    @Override
    protected void init() {
        super.init();
        List<Map.Entry<ResourceLocation, BaseComponent>> list = this.rewards.entrySet().stream().toList();
        for (int i = 0; i < 7; i++) {
            int index = this.page * 7 + i;
            if (index < list.size()) {
                Map.Entry<ResourceLocation, BaseComponent> val = list.get(this.page * 7 + i);
                this.addRenderableWidget(new ButtonValue<String>(this.width / 2 - xSize / 2 + 6, this.height / 2 - ySize / 2 + (i * 23) + 6, 243, 20, val.getValue(),
                        button -> {
                            NetworkCalls.INSTANCE.sendToServer(new C2SGrailReward(val.getKey()));
                            GuiHolyGrail.this.minecraft.player.closeContainer();
                        }).setTexture(tex, 0, 216 - 20).setVal(val.getKey().toString()));
            }
        }
        if (this.page > 0) {
            this.addRenderableWidget(new PageButton(this.width / 2 - xSize / 2 + 220, this.height / 2 - ySize / 2 + 167, false, b -> {
                this.page--;
                this.clearWidgets();
                this.init();
            }));
        }
        if ((this.page + 1) * 7 + 1 <= list.size()) {
            this.addRenderableWidget(new PageButton(this.width / 2 - xSize / 2 + 235, this.height / 2 - ySize / 2 + 167, true, b -> {
                this.page++;
                this.clearWidgets();
                this.init();
            }));
        }
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        RenderSystem.setShaderTexture(0, tex);
        this.blit(stack, this.width / 2 - xSize / 2, this.height / 2 - ySize / 2, 0, 0, xSize, ySize);
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    static class PageButton extends Button {

        private final boolean next;

        public PageButton(int i, int j, boolean next, OnPress onPress) {
            super(i, j, 13, 13, new TextComponent(""), onPress);
            this.next = next;
        }

        @Override
        public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
            Minecraft minecraft = Minecraft.getInstance();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, GuiHolyGrail.tex);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
            int i = this.isHoveredOrFocused() ? 1 : 0;
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            this.blit(poseStack, this.x, this.y, (this.next ? 15 : 0), 189 + i * this.height, this.width, this.height);
            this.renderBg(poseStack, minecraft, mouseX, mouseY);
        }
    }
}