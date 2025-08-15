package io.github.flemmli97.fateubw.client.gui;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.network.C2SGrailReward;
import io.github.flemmli97.tenshilib.client.gui.widget.TexturedButton;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

public class GuiHolyGrail extends Screen {

    private static final ResourceLocation TEX = Fate.modRes("textures/gui/grail_reward.png");
    private static final WidgetSprites ENTRY_WIDGET = new WidgetSprites(Fate.modRes("widget/grail_reward_entry"), Fate.modRes("widget/grail_reward_entry_highlighted"));
    private static final WidgetSprites NEXT_WIDGET = new WidgetSprites(Fate.modRes("widget/button_right"), Fate.modRes("widget/button_right_highlighted"));
    private static final WidgetSprites PREVIOUS_WIDGET = new WidgetSprites(Fate.modRes("widget/button_left"), Fate.modRes("widget/button_left_highlighted"));

    private final Map<ResourceLocation, Component> rewards;
    private int page;

    private static final int X_SIZE = 255, Y_SIZE = 186;

    public GuiHolyGrail(Map<ResourceLocation, Component> rewards) {
        super(Component.translatable("fateubw.gui.holy_grail"));
        this.rewards = rewards;
    }

    @Override
    protected void init() {
        super.init();
        List<Map.Entry<ResourceLocation, Component>> list = this.rewards.entrySet().stream().toList();
        for (int i = 0; i < 7; i++) {
            int index = this.page * 7 + i;
            if (index < list.size()) {
                Map.Entry<ResourceLocation, Component> val = list.get(this.page * 7 + i);
                this.addRenderableWidget(new TexturedButton(this.width / 2 - X_SIZE / 2 + 6, this.height / 2 - Y_SIZE / 2 + (i * 23) + 6, 243, 20, val.getValue(),
                        button -> {
                            LoaderNetwork.INSTANCE.sendToServer(new C2SGrailReward(val.getKey()));
                            this.minecraft.player.closeContainer();
                        }).withSprite(ENTRY_WIDGET));
            }
        }
        if (this.page > 0) {
            this.addRenderableWidget(new TexturedButton(this.width / 2 - X_SIZE / 2 + 220, this.height / 2 - Y_SIZE / 2 + 167, 13, 13, Component.empty(), b -> {
                this.page--;
                this.clearWidgets();
                this.init();
            }).withSprite(PREVIOUS_WIDGET));
        }
        if ((this.page + 1) * 7 + 1 <= list.size()) {
            this.addRenderableWidget(new TexturedButton(this.width / 2 - X_SIZE / 2 + 235, this.height / 2 - Y_SIZE / 2 + 167, 13, 13, Component.empty(), b -> {
                this.page++;
                this.clearWidgets();
                this.init();
            }).withSprite(NEXT_WIDGET));
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blit(TEX, this.width / 2 - X_SIZE / 2, this.height / 2 - Y_SIZE / 2, 0, 0, X_SIZE, Y_SIZE);
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
}