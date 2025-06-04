package io.github.flemmli97.fateubw.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.network.C2SMessageGui;
import io.github.flemmli97.fateubw.common.network.C2SServantCommand;
import io.github.flemmli97.fateubw.common.network.C2SServantSpecial;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

public class CommandGui extends Screen {

    private final static ResourceLocation GUI_BACK_GROUND = new ResourceLocation(Fate.MODID, "textures/gui/command_gui_1.png");

    private final Map<String, Component> translationCache = new HashMap<>();

    private Pages currentPage = Pages.MENU;
    private Random rand = new Random();
    private int command1 = this.rand.nextInt(3);
    private int command2 = this.rand.nextInt(3);
    private int command3 = this.rand.nextInt(3);
    private final BaseServant servant;

    public CommandGui(BaseServant servant) {
        super(new TranslatableComponent("fateubw.gui.command"));
        this.servant = servant;
    }

    private Component getComponent(String key, Consumer<MutableComponent> otherwise) {
        Component cache = this.translationCache.get(key);
        if (cache == null) {
            MutableComponent comp = new TranslatableComponent(key).withStyle();
            otherwise.accept(comp);
            cache = comp;
            this.translationCache.put(key, comp);
        }
        return cache;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        Player player = this.minecraft.player;
        PlayerData capSync = Platform.INSTANCE.getPlayerData(player).orElse(null);
        if (capSync == null)
            return;
        RenderSystem.setShaderTexture(0, GUI_BACK_GROUND);
        this.blit(stack, this.width / 2 - 100, this.height / 2 - 100, 0, 0, 201, 210);
        this.drawCommand(stack, capSync.getCommandSeals());

        this.minecraft.font.draw(stack, this.getComponent("fateubw.gui.name", c -> c.withStyle(ChatFormatting.DARK_RED)), this.width / 2 - 90, this.height / 2 - 5, 1);
        this.minecraft.font.draw(stack, this.getComponent("fateubw.gui.damage", c -> c.withStyle(ChatFormatting.DARK_RED)), this.width / 2 - 90, this.height / 2 + 15, 1);
        this.minecraft.font.draw(stack, this.getComponent("fateubw.gui.armor", c -> c.withStyle(ChatFormatting.DARK_RED)), this.width / 2 - 90, this.height / 2 + 35, 1);
        this.minecraft.font.draw(stack, this.getComponent("fateubw.gui.nobel_phantasm", c -> c.withStyle(ChatFormatting.DARK_RED)), this.width / 2 - 90, this.height / 2 + 55, 1);
        if (this.servant != null) {
            this.minecraft.font.draw(stack, this.servant.getRealName(), this.width / 2 - 90, this.height / 2 + 5, 1);
            this.minecraft.font.draw(stack, String.valueOf((int) this.servant.getAttributeValue(Attributes.ATTACK_DAMAGE)), this.width / 2 - 90, this.height / 2 + 25, 1);
            this.minecraft.font.draw(stack, String.valueOf((int) this.servant.getAttributeValue(Attributes.ARMOR)), this.width / 2 - 90, this.height / 2 + 45, 1);
            this.minecraft.font.draw(stack, this.servant.nobelPhantasm(), this.width / 2 - 90, this.height / 2 + 65, 1);
            float mouseXNew = (float) ((this.width - 200) / 2 + 51) - mouseX;
            float mouseYNew = (float) ((this.height - 180) / 2 + 75 - 50) - mouseY;
            InventoryScreen.renderEntityInInventory(this.width / 2 - 50, this.height / 2 - 20, 29, mouseXNew, mouseYNew, this.servant);
        }
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    private void drawCommand(PoseStack stack, int amount) {
        int[] command = {this.command1, this.command2, this.command3};
        for (int i = 0; i < amount; i++) {
            switch (command[i]) {
                case 0:
                    this.blit(stack, this.width / 2 + 105, this.height / 2 - 100 + 35 * i, 226, 0, 30, 30);
                    break;
                case 1:
                    this.blit(stack, this.width / 2 + 105, this.height / 2 - 100 + 35 * i, 226, 32, 30, 30);
                    break;
                case 2:
                    this.blit(stack, this.width / 2 + 105, this.height / 2 - 100 + 35 * i, 226, 63, 30, 30);
                    break;
                case 3:
                    this.blit(stack, this.width / 2 + 105, this.height / 2 - 100 + 35 * i, 226, 94, 30, 30);
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
            this.addRenderableWidget(new Button(this.width / 2 + 10, this.height / 2 - 82, 80, 20
                    , new TranslatableComponent("fateubw.gui.command.attack"), b -> {
                this.currentPage = Pages.ATTACK;
                this.init(this.minecraft, this.width, this.height);
            }));
            this.addRenderableWidget(new Button(this.width / 2 + 10, this.height / 2 - 52, 80, 20
                    , new TranslatableComponent("fateubw.gui.command.movement"), b -> {
                this.currentPage = Pages.MOVEMENT;
                this.init(this.minecraft, this.width, this.height);
            }));
            this.addRenderableWidget(new Button(this.width / 2 + 10, this.height / 2 - 22, 80, 20
                    , new TranslatableComponent("fateubw.gui.team"), b -> {
                NetworkCalls.INSTANCE.sendToServer(new C2SMessageGui(C2SMessageGui.Type.TEAM));
                this.onClose();
            }));
            this.addRenderableWidget(new Button(this.width / 2 + 10, this.height / 2 + 8, 80, 20
                    , new TranslatableComponent("fateubw.gui.command.kill"), b -> {
                NetworkCalls.INSTANCE.sendToServer(new C2SServantCommand(EnumServantUpdate.KILL));
                NetworkCalls.INSTANCE.sendToServer(new C2SMessageGui(C2SMessageGui.Type.SERVANT));
            }));
            if (this.servant != null) {
                if (this.servant.specialCommands() != null)
                    this.addRenderableWidget(new Button(this.width / 2 + 10, this.height / 2 + 38, 80, 20
                            , new TranslatableComponent("fateubw.gui.command.special"), b -> {
                        this.currentPage = Pages.SPECIAL;
                        this.init(this.minecraft, this.width, this.height);
                    }));
            }
        } else if (this.currentPage == Pages.ATTACK) {
            this.addRenderableWidget(new Button(this.width / 2 + 10, this.height / 2 - 82, 80, 20
                    , new TranslatableComponent("fateubw.gui.back"), this::backButton));
            this.addRenderableWidget(new Button(this.width / 2 + 10, this.height / 2 - 52, 80, 20
                    , new TranslatableComponent("fateubw.gui.command.aggressive"), b -> NetworkCalls.INSTANCE.sendToServer(new C2SServantCommand(EnumServantUpdate.AGGRESSIVE))));
            this.addRenderableWidget(new Button(this.width / 2 + 10, this.height / 2 - 22, 80, 20
                    , new TranslatableComponent("fateubw.gui.command.normal"), b -> NetworkCalls.INSTANCE.sendToServer(new C2SServantCommand(EnumServantUpdate.NORMAL))));
            this.addRenderableWidget(new Button(this.width / 2 + 10, this.height / 2 + 8, 80, 20
                    , new TranslatableComponent("fateubw.gui.command.defensive"), b -> NetworkCalls.INSTANCE.sendToServer(new C2SServantCommand(EnumServantUpdate.DEFENSIVE))));
        } else if (this.currentPage == Pages.MOVEMENT) {
            this.addRenderableWidget(new Button(this.width / 2 + 10, this.height / 2 - 82, 80, 20
                    , new TranslatableComponent("fateubw.gui.command.back"), this::backButton));
            this.addRenderableWidget(new Button(this.width / 2 + 10, this.height / 2 - 52, 80, 20
                    , new TranslatableComponent("fateubw.gui.command.follow"), b -> NetworkCalls.INSTANCE.sendToServer(new C2SServantCommand(EnumServantUpdate.FOLLOW))));
            this.addRenderableWidget(new Button(this.width / 2 + 10, this.height / 2 - 22, 80, 20
                    , new TranslatableComponent("fateubw.gui.command.stay"), b -> NetworkCalls.INSTANCE.sendToServer(new C2SServantCommand(EnumServantUpdate.STAY))));
            this.addRenderableWidget(new Button(this.width / 2 + 10, this.height / 2 + 8, 80, 20
                    , new TranslatableComponent("fateubw.gui.command.protect"), b -> NetworkCalls.INSTANCE.sendToServer(new C2SServantCommand(EnumServantUpdate.GUARD))));
            this.addRenderableWidget(new Button(this.width / 2 + 10, this.height / 2 + 38, 80, 20
                    , new TranslatableComponent("fateubw.gui.command.call"), b -> NetworkCalls.INSTANCE.sendToServer(new C2SServantCommand(EnumServantUpdate.TELEPORT))));
        } else if (this.currentPage == Pages.SPECIAL) {
            this.addRenderableWidget(new Button(this.width / 2 + 10, this.height / 2 - 82, 80, 20
                    , new TranslatableComponent("fateubw.gui.command.back"), this::backButton));
            if (this.servant != null)
                for (int i = 0; i < this.servant.specialCommands().length; i++) {
                    String id = this.servant.specialCommands()[i];
                    this.addRenderableWidget(new Button(this.width / 2 + 10, this.height / 2 - 52, 80, 20,
                            new TranslatableComponent(id),
                            b -> NetworkCalls.INSTANCE.sendToServer(new C2SServantSpecial(id))));
                }
        }
    }

    private void backButton(Button button) {
        this.currentPage = Pages.MENU;
        this.init(this.minecraft, this.width, this.height);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private enum Pages {
        MENU,
        MOVEMENT,
        ATTACK,
        SPECIAL
    }
}