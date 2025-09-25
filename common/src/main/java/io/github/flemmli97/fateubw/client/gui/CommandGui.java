package io.github.flemmli97.fateubw.client.gui;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.entity.ServantLike;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.common.network.C2SMessageGui;
import io.github.flemmli97.fateubw.common.network.C2SServantCommand;
import io.github.flemmli97.fateubw.common.network.C2SServantSpecial;
import io.github.flemmli97.fateubw.common.network.S2CServantGui;
import io.github.flemmli97.fateubw.platform.Platform;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

public class CommandGui extends Screen {

    private final static ResourceLocation GUI_BACK_GROUND = Fate.modRes("textures/gui/command_gui.png");
    private static final ResourceLocation SEAL_1 = Fate.modRes("icon/command_seal_1");
    private static final ResourceLocation SEAL_2 = Fate.modRes("icon/command_seal_2");
    private static final ResourceLocation SEAL_3 = Fate.modRes("icon/command_seal_3");
    private static final ResourceLocation SEAL_4 = Fate.modRes("icon/command_seal_4");

    private final Map<String, Component> translationCache = new HashMap<>();

    private final int sizeX = 222, sizeY = 200;
    private int leftPos, topPos;

    private Pages currentPage = Pages.MENU;
    private final Random rand = new Random();

    private final int command1;
    private final int command2;
    private final int command3;

    private final ServantLike<?> servant;
    private S2CServantGui.ServantMetaData data;

    public CommandGui(S2CServantGui.ServantMetaData data) {
        super(Component.translatable("fateubw.gui.command"));
        this.servant = this.createFrom(data);
        this.rand.setSeed(Minecraft.getInstance().player.getUUID().getLeastSignificantBits());
        this.command1 = this.rand.nextInt(3);
        this.command2 = this.rand.nextInt(3);
        this.command3 = this.rand.nextInt(3);
        this.update(data);
    }

    private Component getComponent(String key, Consumer<MutableComponent> otherwise) {
        Component cache = this.translationCache.get(key);
        if (cache == null) {
            MutableComponent comp = Component.translatable(key).withStyle();
            otherwise.accept(comp);
            cache = comp;
            this.translationCache.put(key, comp);
        }
        return cache;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();
        this.leftPos = this.width / 2 - (this.sizeX / 2);
        this.topPos = this.height / 2 - (this.sizeY / 2);
        int buttonPos = this.leftPos + this.sizeX - 80 - 16;
        int buttonY = this.topPos + 16;
        if (this.currentPage == Pages.MENU) {
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.command.attack"), b -> {
                this.currentPage = Pages.ATTACK;
                this.init();
            }).bounds(buttonPos, buttonY, 80, 20).build());
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.command.movement"), b -> {
                this.currentPage = Pages.MOVEMENT;
                this.init();
            }).bounds(buttonPos, buttonY += 30, 80, 20).build());
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.team"), b -> {
                LoaderNetwork.INSTANCE.sendToServer(new C2SMessageGui(C2SMessageGui.Type.TEAM));
                this.onClose();
            }).bounds(buttonPos, buttonY += 30, 80, 20).build());
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.command.kill"), b -> {
                LoaderNetwork.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.ActionType.KILL, this.entityId()));
                LoaderNetwork.INSTANCE.sendToServer(new C2SMessageGui(C2SMessageGui.Type.SERVANT));
            }).bounds(buttonPos, buttonY += 30, 80, 20).build());
            if (this.servant != null) {
                if (this.servant.specialCommands() != null)
                    this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.command.special"), b -> {
                        this.currentPage = Pages.SPECIAL;
                        this.init();
                    }).bounds(buttonPos, buttonY + 30, 80, 20).build());
            }
        } else if (this.currentPage == Pages.ATTACK) {
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.back"), this::backButton)
                    .bounds(buttonPos, buttonY, 80, 20).build());
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.command.aggressive"),
                            b -> LoaderNetwork.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.ActionType.AGGRESSIVE, this.entityId())))
                    .bounds(buttonPos, buttonY += 30, 80, 20).build());
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.command.normal"),
                            b -> LoaderNetwork.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.ActionType.NORMAL, this.entityId())))
                    .bounds(buttonPos, buttonY += 30, 80, 20).build());
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.command.defensive"),
                            b -> LoaderNetwork.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.ActionType.DEFENSIVE, this.entityId())))
                    .bounds(buttonPos, buttonY + 30, 80, 20).build());
        } else if (this.currentPage == Pages.MOVEMENT) {
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.back"), this::backButton)
                    .bounds(buttonPos, buttonY, 80, 20).build());
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.command.follow"),
                            b -> LoaderNetwork.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.ActionType.FOLLOW, this.entityId())))
                    .bounds(buttonPos, buttonY += 30, 80, 20).build());
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.command.stay"),
                            b -> LoaderNetwork.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.ActionType.STAY, this.entityId())))
                    .bounds(buttonPos, buttonY += 30, 80, 20).build());
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.command.protect"),
                            b -> LoaderNetwork.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.ActionType.GUARD, this.entityId())))
                    .bounds(buttonPos, buttonY += 30, 80, 20).build());
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.command.call"),
                            b -> LoaderNetwork.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.ActionType.TELEPORT, this.entityId())))
                    .bounds(buttonPos, buttonY + 30, 80, 20).build());
        } else if (this.currentPage == Pages.SPECIAL) {
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.back"), this::backButton)
                    .bounds(buttonPos, buttonY, 80, 20).build());
            if (this.servant != null)
                for (int i = 0; i < this.servant.specialCommands().length; i++) {
                    String id = this.servant.specialCommands()[i];
                    this.addRenderableWidget(Button.builder(
                                    Component.translatable(id),
                                    b -> LoaderNetwork.INSTANCE.sendToServer(new C2SServantSpecial(id, this.entityId())))
                            .bounds(buttonPos, buttonY += 30, 80, 20).build());
                }
        }
    }

    private void backButton(Button button) {
        this.currentPage = Pages.MENU;
        this.init();
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        Player player = this.minecraft.player;
        PlayerData data = Platform.INSTANCE.getPlayerData(player);
        if (data == null)
            return;
        graphics.blit(GUI_BACK_GROUND, this.leftPos, this.topPos, 0, 0, this.sizeX, this.sizeY);
        this.drawCommand(graphics, data.getCommandSeals());
        int textX = this.leftPos + 11;
        int textMiddle = this.leftPos + 3 + 54;
        int textY = this.topPos + 116;
        if (this.servant != null) {
            List<FormattedCharSequence> name = this.font.split(this.servant.get().getName(), 101);
            for (FormattedCharSequence seq : name) {
                this.drawCenteredString(graphics, this.font, seq, textMiddle, textY, 1);
                textY += 12;
            }
            graphics.drawString(this.font, this.getComponent("fateubw.gui.nobel_phantasm", c -> c.withStyle(ChatFormatting.DARK_RED)), textX, textY += 5, 1, false);
            graphics.drawString(this.font, this.servant.nobelPhantasm(), textX, textY += 12, 1, false);
            graphics.drawString(this.font, this.getComponent("fateubw.gui.nobel_phantasm_cost", c -> c.withStyle(ChatFormatting.DARK_RED)), textX, textY += 12, 1, false);
            graphics.drawString(this.font, "" + this.data.npCost(), textX, textY + 12, 1, false);
            RenderUtils.renderScaledEntityGui(graphics, this.leftPos + 26, this.topPos + 20, 61,
                    87, 29, 0.0625f, mouseX, mouseY, this.servant.get());
        } else {
            this.drawCenteredString(graphics, this.font, this.getComponent("fateubw.gui.no_servant", c -> c.withStyle(ChatFormatting.DARK_RED)),
                    textMiddle, textY, 1);
        }
    }

    private void drawCenteredString(GuiGraphics graphics, Font font, Component text, int x, int y, int color) {
        this.drawCenteredString(graphics, font, text.getVisualOrderText(), x, y, 1);
    }

    private void drawCenteredString(GuiGraphics graphics, Font font, FormattedCharSequence text, int x, int y, int color) {
        graphics.drawString(font, text, x - font.width(text) / 2, y, color, false);
    }

    private void drawCommand(GuiGraphics graphics, int amount) {
        int[] command = {this.command1, this.command2, this.command3};
        int posX = this.leftPos + this.sizeX + 2;
        int posY = this.topPos + 2;
        for (int i = 0; i < amount; i++) {
            switch (command[i]) {
                case 0:
                    graphics.blitSprite(SEAL_1, posX, posY + 35 * i, 32, 32);
                    break;
                case 1:
                    graphics.blitSprite(SEAL_2, posX, posY + 35 * i, 32, 32);
                    break;
                case 2:
                    graphics.blitSprite(SEAL_3, posX, posY + 35 * i, 32, 32);
                    break;
                case 3:
                    graphics.blitSprite(SEAL_4, posX, posY + 35 * i, 32, 32);
                    break;
            }
        }
    }

    @Override
    public void removed() {
        super.removed();
        LoaderNetwork.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.ActionType.CLOSE, this.entityId()));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public void update(S2CServantGui.ServantMetaData data) {
        this.data = data;
        if (this.servant != null && data != null) {
            data.equipment().ifPresent(list ->
                    list.forEach(p -> this.servant.get().setItemSlot(p.getFirst(), p.getSecond())));
            if (data.syncedData() != null)
                this.servant.get().getEntityData().assignValues(data.syncedData());
        }
    }

    private ServantLike<?> createFrom(S2CServantGui.ServantMetaData data) {
        if (data == null)
            return null;
        Entity fromId = Minecraft.getInstance().level.getEntity(data.entityId());
        if (fromId instanceof ServantLike<?> s) {
            return s;
        }
        Entity created = data.type().create(Minecraft.getInstance().level);
        if (created instanceof ServantLike<?> s) {
            return s;
        }
        return null;
    }

    private int entityId() {
        return this.data != null ? this.data.entityId() : 0;
    }

    private enum Pages {
        MENU,
        MOVEMENT,
        ATTACK,
        SPECIAL
    }
}