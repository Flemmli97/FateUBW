package io.github.flemmli97.fate.client.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.client.ClientHandler;
import io.github.flemmli97.fate.common.capability.IPlayer;
import io.github.flemmli97.fate.common.capability.PlayerCapProvider;
import io.github.flemmli97.fate.common.entity.servant.EntityServant;
import io.github.flemmli97.fate.common.utils.EnumServantUpdate;
import io.github.flemmli97.fate.common.utils.Utils;
import io.github.flemmli97.fate.network.C2SMessageGui;
import io.github.flemmli97.fate.network.C2SServantCommand;
import io.github.flemmli97.fate.network.C2STruceMessage;
import io.github.flemmli97.fate.network.PacketHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class CommandGui extends Screen {

    private Pages currentPage = Pages.MENU;
    private int trucePage = 0;
    private Random rand = new Random();
    private int command1 = this.rand.nextInt(3);
    private int command2 = this.rand.nextInt(3);
    private int command3 = this.rand.nextInt(3);
    private ButtonValue<UUID> request, accept, remove;

    private final static ResourceLocation guiBackGround = new ResourceLocation(Fate.MODID, "textures/gui/command_gui_1.png");
    private final static ResourceLocation guiTruce = new ResourceLocation(Fate.MODID, "textures/gui/command_gui_2.png");

    public CommandGui() {
        super(new TranslationTextComponent("fate.gui.command"));
        PacketHandler.sendToServer(new C2SMessageGui(C2SMessageGui.Type.ALL));
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        PlayerEntity player = this.getMinecraft().player;
        IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap).orElse(null);
        EntityServant servant = capSync.getServant(player);
        if (capSync == null)
            return;
        if (this.currentPage != Pages.TRUCE) {
            this.getMinecraft().getTextureManager().bindTexture(guiBackGround);
            this.blit(stack, this.width / 2 - 100, this.height / 2 - 100, 0, 0, 201, 210);
            this.drawCommand(stack, capSync.getCommandSeals());

            this.getMinecraft().fontRenderer.drawString(stack, "§4Name:", this.width / 2 - 90, this.height / 2 - 5, 1);
            this.getMinecraft().fontRenderer.drawString(stack, "§4Damage:", this.width / 2 - 90, this.height / 2 + 15, 1);
            this.getMinecraft().fontRenderer.drawString(stack, "§4Armor:", this.width / 2 - 90, this.height / 2 + 35, 1);
            this.getMinecraft().fontRenderer.drawString(stack, "§4Nobel Phantasm:", this.width / 2 - 90, this.height / 2 + 55, 1);
            if (servant != null) {
                this.getMinecraft().fontRenderer.drawText(stack, servant.getRealName(), this.width / 2 - 90, this.height / 2 + 5, 1);
                this.getMinecraft().fontRenderer.drawString(stack, String.valueOf(servant.props().strength()), this.width / 2 - 90, this.height / 2 + 25, 1);
                this.getMinecraft().fontRenderer.drawString(stack, String.valueOf(servant.props().armor()), this.width / 2 - 90, this.height / 2 + 45, 1);
                this.getMinecraft().fontRenderer.drawString(stack, servant.nobelPhantasm(), this.width / 2 - 90, this.height / 2 + 65, 1);
            }
        } else {
            this.getMinecraft().getTextureManager().bindTexture(guiTruce);
            this.blit(stack, this.width / 2 - 100, this.height / 2 - 100, 0, 0, 201, 210);
            this.drawCommand(stack, capSync.getCommandSeals());
        }
        if (servant != null) {
            float mouseXNew = (float) ((this.width - 200) / 2 + 51) - mouseX;
            float mouseYNew = (float) ((this.height - 180) / 2 + 75 - 50) - mouseY;
            InventoryScreen.drawEntityOnScreen(this.width / 2 - 50, this.height / 2 - 20, 29, mouseXNew, mouseYNew, servant);
        }
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    private void drawCommand(MatrixStack stack, int amount) {
        int[] command = new int[]{this.command1, this.command2, this.command3};
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
            this.addButton(new Button(this.width / 2 + 10, this.height / 2 - 82, 80, 20
                    , new TranslationTextComponent("fate.gui.command.attack"), b -> {
                this.currentPage = Pages.ATTACK;
                this.init(this.minecraft, this.width, this.height);
            }));
            this.addButton(new Button(this.width / 2 + 10, this.height / 2 - 52, 80, 20
                    , new TranslationTextComponent("fate.gui.command.movement"), b -> {
                this.currentPage = Pages.MOVEMENT;
                this.init(this.minecraft, this.width, this.height);
            }));
            this.addButton(new Button(this.width / 2 + 10, this.height / 2 - 22, 80, 20
                    , new TranslationTextComponent("fate.gui.command.truce"), b -> {
                this.currentPage = Pages.TRUCE;
                this.init(this.minecraft, this.width, this.height);
            }));
            this.addButton(new Button(this.width / 2 + 10, this.height / 2 + 8, 80, 20
                    , new TranslationTextComponent("fate.gui.command.kill"), b -> {
                PacketHandler.sendToServer(new C2SServantCommand(EnumServantUpdate.KILL));
                PacketHandler.sendToServer(new C2SMessageGui(C2SMessageGui.Type.SERVANT));
            }));
            EntityServant servant = Utils.getServant(this.getMinecraft().player);
            if (servant != null) {
                if (servant.specialCommands() != null)
                    this.addButton(new Button(this.width / 2 + 10, this.height / 2 + 38, 80, 20
                            , new TranslationTextComponent("fate.gui.command.special"), b -> {
                        this.currentPage = Pages.SPECIAL;
                        this.init(this.minecraft, this.width, this.height);
                    }));
            }
        } else if (this.currentPage == Pages.ATTACK) {
            this.addButton(new Button(this.width / 2 + 10, this.height / 2 - 82, 80, 20
                    , new TranslationTextComponent("fate.gui.command.back"), this::backButton));
            this.addButton(new Button(this.width / 2 + 10, this.height / 2 - 52, 80, 20
                    , new TranslationTextComponent("fate.gui.command.aggressive"), b -> PacketHandler.sendToServer(new C2SServantCommand(EnumServantUpdate.AGGRESSIVE))));
            this.addButton(new Button(this.width / 2 + 10, this.height / 2 - 22, 80, 20
                    , new TranslationTextComponent("fate.gui.command.normal"), b -> PacketHandler.sendToServer(new C2SServantCommand(EnumServantUpdate.NORMAL))));
            this.addButton(new Button(this.width / 2 + 10, this.height / 2 + 8, 80, 20
                    , new TranslationTextComponent("fate.gui.command.defensive"), b -> PacketHandler.sendToServer(new C2SServantCommand(EnumServantUpdate.DEFENSIVE))));
        } else if (this.currentPage == Pages.MOVEMENT) {
            this.addButton(new Button(this.width / 2 + 10, this.height / 2 - 82, 80, 20
                    , new TranslationTextComponent("fate.gui.command.back"), this::backButton));
            this.addButton(new Button(this.width / 2 + 10, this.height / 2 - 52, 80, 20
                    , new TranslationTextComponent("fate.gui.command.follow"), b -> PacketHandler.sendToServer(new C2SServantCommand(EnumServantUpdate.FOLLOW))));
            this.addButton(new Button(this.width / 2 + 10, this.height / 2 - 22, 80, 20
                    , new TranslationTextComponent("fate.gui.command.stay"), b -> PacketHandler.sendToServer(new C2SServantCommand(EnumServantUpdate.STAY))));
            this.addButton(new Button(this.width / 2 + 10, this.height / 2 + 8, 80, 20
                    , new TranslationTextComponent("fate.gui.command.protect"), b -> PacketHandler.sendToServer(new C2SServantCommand(EnumServantUpdate.GUARD))));
            this.addButton(new Button(this.width / 2 + 10, this.height / 2 + 38, 80, 20
                    , new TranslationTextComponent("fate.gui.command.call"), b -> PacketHandler.sendToServer(new C2SServantCommand(EnumServantUpdate.TELEPORT))));
        } else if (this.currentPage == Pages.SPECIAL) {
            this.addButton(new Button(this.width / 2 + 10, this.height / 2 - 82, 80, 20
                    , new TranslationTextComponent("fate.gui.command.back"), this::backButton));
            EntityServant servant = Utils.getServant(this.getMinecraft().player);
            if (servant != null)
                for (int i = 0; i < servant.specialCommands().length; i++)
                    this.addButton(new ButtonSpecial(this.width / 2 + 10, this.height / 2 - 52, 80, 20, servant.specialCommands()[i]));
        } else if (this.currentPage == Pages.TRUCE) {
            List<GameProfile> players = ClientHandler.grailPlayers;
            for (int i = 0; i < 7; i++) {
                int index = this.trucePage * 7 + i;
                if (index < players.size()) {
                    this.addButton(new ButtonGameProfile(this.width / 2 + 4, this.height / 2 - 82 + (index % 7) * 20, players.get(index), button -> {
                        ButtonGameProfile gp = (ButtonGameProfile) button;
                        if (!gp.selected) {
                            this.request.active = gp.getState() == ButtonGameProfile.State.NONE;
                            this.accept.active = gp.getState() == ButtonGameProfile.State.PENDING;
                            this.remove.active = gp.getState() == ButtonGameProfile.State.TRUCE;
                            gp.selected = !(gp.getState() == ButtonGameProfile.State.REQUESTED);
                            switch (gp.getState()) {
                                case NONE:
                                    this.request.setVal(gp.getUUID());
                                    break;
                                case PENDING:
                                    this.accept.setVal(gp.getUUID());
                                    break;
                                case TRUCE:
                                    this.remove.setVal(gp.getUUID());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            this.request.active = false;
                            this.accept.active = false;
                            this.remove.active = false;
                            this.request.setVal(null);
                            this.accept.setVal(null);
                            this.remove.setVal(null);
                            gp.selected = false;
                        }
                    }));
                }
            }
            this.addButton(new Button(this.width / 2 + 48, this.height / 2 + 62, 44, 20, new StringTextComponent(">"), button -> {
                if (this.trucePage < ClientHandler.grailPlayers.size() / 7) {
                    ++this.trucePage;
                    this.init(this.minecraft, this.width, this.height);
                }
            }));
            this.addButton(new Button(this.width / 2 + 4, this.height / 2 + 62, 44, 20, new StringTextComponent("<"), button -> {
                if (this.trucePage > 0) {
                    --this.trucePage;
                    this.init(this.minecraft, this.width, this.height);
                }
            }));

            this.addButton(new Button(this.width / 2 - 90, this.height / 2 - 5, 80, 20
                    , new TranslationTextComponent("fate.gui.command.back"), this::backButton));
            this.addButton(this.request = new ButtonValue<>(this.width / 2 - 90, this.height / 2 + 25, 80, 20, "fate.gui.truce.request", button -> {
                if (button.getVal() != null) {
                    PacketHandler.sendToServer(new C2STruceMessage(C2STruceMessage.Type.SEND, button.getVal()));
                    this.init(this.minecraft, this.width, this.height);
                }
            }));
            this.addButton(this.accept = new ButtonValue<>(this.width / 2 - 90, this.height / 2 + 45, 80, 20, "fate.gui.truce.accept", button -> {
                if (button.getVal() != null) {
                    PacketHandler.sendToServer(new C2STruceMessage(C2STruceMessage.Type.ACCEPT, button.getVal()));
                    this.init(this.minecraft, this.width, this.height);
                }
            }));
            this.addButton(this.remove = new ButtonValue<>(this.width / 2 - 90, this.height / 2 + 65, 80, 20, "fate.gui.truce.remove", button -> {
                if (button.getVal() != null) {
                    PacketHandler.sendToServer(new C2STruceMessage(C2STruceMessage.Type.DENY, button.getVal()));
                    this.init(this.minecraft, this.width, this.height);
                }
            }));
            this.request.active = false;
            this.accept.active = false;
            this.remove.active = false;
        }
    }

    private void backButton(Button button) {
        this.currentPage = Pages.MENU;
        this.trucePage = 0;
        this.init(this.minecraft, this.width, this.height);
    }

    private enum Pages {
        MENU,
        MOVEMENT,
        ATTACK,
        SPECIAL,
        TRUCE
    }
}