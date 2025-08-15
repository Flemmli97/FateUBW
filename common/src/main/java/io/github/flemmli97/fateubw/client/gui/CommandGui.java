package io.github.flemmli97.fateubw.client.gui;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.network.C2SMessageGui;
import io.github.flemmli97.fateubw.common.network.C2SServantCommand;
import io.github.flemmli97.fateubw.common.network.C2SServantSpecial;
import io.github.flemmli97.fateubw.common.network.S2CServantGui;
import io.github.flemmli97.fateubw.platform.Platform;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

public class CommandGui extends Screen {

    private final static ResourceLocation GUI_BACK_GROUND = Fate.modRes("textures/gui/command_gui_1.png");
    private static final ResourceLocation SEAL_1 = Fate.modRes("icon/command_seal_1");
    private static final ResourceLocation SEAL_2 = Fate.modRes("icon/command_seal_2");
    private static final ResourceLocation SEAL_3 = Fate.modRes("icon/command_seal_3");
    private static final ResourceLocation SEAL_4 = Fate.modRes("icon/command_seal_4");

    private final Map<String, Component> translationCache = new HashMap<>();

    private Pages currentPage = Pages.MENU;
    private final Random rand = new Random();

    private final int command1 = this.rand.nextInt(3);
    private final int command2 = this.rand.nextInt(3);
    private final int command3 = this.rand.nextInt(3);

    private final BaseServant servant;
    private S2CServantGui.ServantMetaData data;

    public CommandGui(S2CServantGui.ServantMetaData data) {
        super(Component.translatable("fateubw.gui.command"));
        this.servant = this.createFrom(data);
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
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        Player player = this.minecraft.player;
        PlayerData data = Platform.INSTANCE.getPlayerData(player);
        if (data == null)
            return;
        graphics.blit(GUI_BACK_GROUND, this.width / 2 - 100, this.height / 2 - 100, 0, 0, 201, 210);
        this.drawCommand(graphics, data.getCommandSeals());

        graphics.drawString(this.font, this.getComponent("fateubw.gui.name", c -> c.withStyle(ChatFormatting.DARK_RED)), this.width / 2 - 90, this.height / 2 - 5, 1, true);
        graphics.drawString(this.font, this.getComponent("fateubw.gui.nobel_phantasm", c -> c.withStyle(ChatFormatting.DARK_RED)), this.width / 2 - 90, this.height / 2 + 15, 1, true);
        graphics.drawString(this.font, this.getComponent("fateubw.gui.nobel_phantasm_cost", c -> c.withStyle(ChatFormatting.DARK_RED)), this.width / 2 - 90, this.height / 2 + 35, 1, true);
        if (this.servant != null) {
            graphics.drawString(this.font, this.servant.getRealName(), this.width / 2 - 90, this.height / 2 + 5, 1, true);
            graphics.drawString(this.font, this.servant.nobelPhantasm(), this.width / 2 - 90, this.height / 2 + 25, 1, true);
            graphics.drawString(this.font, "" + this.data.npCost(), this.width / 2 - 90, this.height / 2 + 45, 1, true);
            RenderUtils.renderScaledEntityGui(graphics, this.width / 2 - 50, this.height / 2 - 20, 29 * 3,
                    29 * 3, 29, 0, mouseX, mouseY, this.servant);
        }
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    private void drawCommand(GuiGraphics graphics, int amount) {
        int[] command = {this.command1, this.command2, this.command3};
        for (int i = 0; i < amount; i++) {
            switch (command[i]) {
                case 0:
                    graphics.blitSprite(SEAL_1, this.width / 2 + 105, this.height / 2 - 100 + 35 * i, 32, 32);
                    break;
                case 1:
                    graphics.blitSprite(SEAL_2, this.width / 2 + 105, this.height / 2 - 100 + 35 * i, 32, 32);
                    break;
                case 2:
                    graphics.blitSprite(SEAL_3, this.width / 2 + 105, this.height / 2 - 100 + 35 * i, 32, 32);
                    break;
                case 3:
                    graphics.blitSprite(SEAL_4, this.width / 2 + 105, this.height / 2 - 100 + 35 * i, 32, 32);
                    break;
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        super.init();
        if (this.currentPage == Pages.MENU) {
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.command.attack"), b -> {
                this.currentPage = Pages.ATTACK;
                this.init(this.minecraft, this.width, this.height);
            }).bounds(this.width / 2 + 10, this.height / 2 - 82, 80, 20).build());
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.command.movement"), b -> {
                this.currentPage = Pages.MOVEMENT;
                this.init(this.minecraft, this.width, this.height);
            }).bounds(this.width / 2 + 10, this.height / 2 - 52, 80, 20).build());
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.team"), b -> {
                LoaderNetwork.INSTANCE.sendToServer(new C2SMessageGui(C2SMessageGui.Type.TEAM));
                this.onClose();
            }).bounds(this.width / 2 + 10, this.height / 2 - 22, 80, 20).build());
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.command.kill"), b -> {
                LoaderNetwork.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.Type.KILL, this.entityId()));
                LoaderNetwork.INSTANCE.sendToServer(new C2SMessageGui(C2SMessageGui.Type.SERVANT));
            }).bounds(this.width / 2 + 10, this.height / 2 + 8, 80, 20).build());
            if (this.servant != null) {
                if (this.servant.specialCommands() != null)
                    this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.command.special"), b -> {
                        this.currentPage = Pages.SPECIAL;
                        this.init(this.minecraft, this.width, this.height);
                    }).bounds(this.width / 2 + 10, this.height / 2 + 38, 80, 20).build());
            }
        } else if (this.currentPage == Pages.ATTACK) {
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.back"), this::backButton)
                    .bounds(this.width / 2 + 10, this.height / 2 - 82, 80, 20).build());
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.command.aggressive"),
                            b -> LoaderNetwork.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.Type.AGGRESSIVE, this.entityId())))
                    .bounds(this.width / 2 + 10, this.height / 2 - 52, 80, 20).build());
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.command.normal"),
                            b -> LoaderNetwork.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.Type.NORMAL, this.entityId())))
                    .bounds(this.width / 2 + 10, this.height / 2 - 22, 80, 20).build());
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.command.defensive"),
                            b -> LoaderNetwork.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.Type.DEFENSIVE, this.entityId())))
                    .bounds(this.width / 2 + 10, this.height / 2 + 8, 80, 20).build());
        } else if (this.currentPage == Pages.MOVEMENT) {
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.back"), this::backButton)
                    .bounds(this.width / 2 + 10, this.height / 2 - 82, 80, 20).build());
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.command.follow"),
                            b -> LoaderNetwork.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.Type.FOLLOW, this.entityId())))
                    .bounds(this.width / 2 + 10, this.height / 2 - 52, 80, 20).build());
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.command.stay"),
                            b -> LoaderNetwork.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.Type.STAY, this.entityId())))
                    .bounds(this.width / 2 + 10, this.height / 2 - 22, 80, 20).build());
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.command.protect"),
                            b -> LoaderNetwork.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.Type.GUARD, this.entityId())))
                    .bounds(this.width / 2 + 10, this.height / 2 + 8, 80, 20).build());
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.command.call"),
                            b -> LoaderNetwork.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.Type.TELEPORT, this.entityId())))
                    .bounds(this.width / 2 + 10, this.height / 2 + 38, 80, 20).build());
        } else if (this.currentPage == Pages.SPECIAL) {
            this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.back"), this::backButton)
                    .bounds(this.width / 2 + 10, this.height / 2 - 82, 80, 20).build());
            if (this.servant != null)
                for (int i = 0; i < this.servant.specialCommands().length; i++) {
                    String id = this.servant.specialCommands()[i];
                    this.addRenderableWidget(Button.builder(
                                    Component.translatable(id),
                                    b -> LoaderNetwork.INSTANCE.sendToServer(new C2SServantSpecial(id, this.entityId())))
                            .bounds(this.width / 2 + 10, this.height / 2 - 52, 80, 20).build());
                }
        }
    }

    private void backButton(Button button) {
        this.currentPage = Pages.MENU;
        this.init(this.minecraft, this.width, this.height);
    }

    @Override
    public void removed() {
        super.removed();
        LoaderNetwork.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.Type.CLOSE, this.entityId()));
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
                    list.forEach(p -> this.servant.setItemSlot(p.getFirst(), p.getSecond())));
            if (data.syncedData() != null)
                this.servant.getEntityData().assignValues(data.syncedData());
        }
    }

    private BaseServant createFrom(S2CServantGui.ServantMetaData data) {
        if (data == null)
            return null;
        Entity fromId = Minecraft.getInstance().level.getEntity(data.entityId());
        if (fromId instanceof BaseServant s) {
            return s;
        }
        Entity created = data.type().create(Minecraft.getInstance().level);
        if (created instanceof BaseServant s) {
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