package io.github.flemmli97.fateubw.client.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.gui.widget.CustomButton;
import io.github.flemmli97.fateubw.common.network.C2STeamMessage;
import io.github.flemmli97.fateubw.common.network.C2STeamUuidMessage;
import io.github.flemmli97.fateubw.common.world.GrailTeam;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import io.github.flemmli97.tenshilib.client.gui.widget.list.SelectableEntry;
import io.github.flemmli97.tenshilib.client.gui.widget.list.SelectableListWidget;
import io.github.flemmli97.tenshilib.client.gui.widget.list.SelectableText;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.UUID;

public class TeamGui extends Screen {

    private static final ResourceLocation WIDGETS = new ResourceLocation(Fate.MODID, "textures/gui/widgets.png");

    private int leftPos, topPos;

    private GrailTeam.ClientTeamInfo info;
    private boolean admin;

    private Pages page = Pages.MAIN;
    private int sizeX = Pages.MAIN.sizeX;
    private int sizeY = Pages.MAIN.sizeY;

    @Nullable
    private EditBox box;
    private Button leaveButton;

    public TeamGui(GrailTeam.ClientTeamInfo info) {
        super(new TranslatableComponent("fateubw.gui.team"));
        this.update(info, false);
    }

    private void changePage(Pages page) {
        this.page = page;
        this.clearWidgets();
        this.init();
    }

    @Override
    protected void init() {
        super.init();
        this.sizeX = this.page.sizeX;
        this.sizeY = this.page.sizeY;
        this.leftPos = this.width / 2 - (this.sizeX / 2);
        this.topPos = this.height / 2 - (this.sizeY / 2);
        int padding = 12;
        int y = padding;
        switch (this.page) {
            case MAIN -> {
                int x = this.leftPos + this.sizeX / 2 - 40;
                if (this.info.team().isPresent()) {
                    GrailTeam.ShortTeamInfo team = this.info.team().get();
                    y = this.topPos + 12 + 24;
                    Button invite = this.addRenderableWidget(new Button(x, y, 80, 20,
                            new TranslatableComponent("fateubw.gui.team.invites"), b -> this.changePage(Pages.INVITES)));
                    invite.active = team.admin();
                    y += 24;
                    this.addRenderableWidget(new Button(x, y, 80, 20,
                            new TranslatableComponent("fateubw.gui.team.allies"), b -> this.changePage(Pages.ALLIES)));
                    y += 24;
                    this.addRenderableWidget(new Button(x, y, 80, 20,
                            new TranslatableComponent("fateubw.gui.team.members"), b -> this.changePage(Pages.MEMBERS)));

                    if (this.admin) {
                        this.box = this.addRenderableWidget(new EditBox(this.font, x, this.topPos + 10, 80, 16,
                                new TextComponent("")));
                        this.box.visible = false;
                    }

                    this.leaveButton = this.addRenderableWidget(new Button(this.leftPos + this.sizeX - padding - 80, this.topPos + this.sizeY - padding - 20, 80, 20,
                            new TranslatableComponent(team.creator().equals(this.minecraft.player.getUUID()) ?
                                    "fateubw.gui.team.disband" : "fateubw.gui.team.leave").withStyle(ChatFormatting.RED),
                            b -> NetworkCalls.INSTANCE.sendToServer(new C2STeamMessage(C2STeamMessage.Type.LEAVE, ""))));
                    this.leaveButton.active = false;
                } else {
                    this.addRenderableWidget(new Button(this.leftPos + this.sizeX - padding - 80, this.topPos + y, 80, 20,
                            new TranslatableComponent("fateubw.gui.team.invites"), b -> this.changePage(Pages.INVITES)));
                    y = this.topPos + this.sizeY - padding - 20 - 24;
                    this.box = this.addRenderableWidget(new EditBox(this.font, this.leftPos + this.sizeX / 2 - 60, y, 120, 20,
                            new TextComponent("")));
                    y += 24;
                    Button create = this.addRenderableWidget(new Button(this.leftPos + this.sizeX / 2 - 60, y, 120, 20,
                            new TranslatableComponent("fateubw.gui.team.create"), b -> {
                        NetworkCalls.INSTANCE.sendToServer(new C2STeamMessage(C2STeamMessage.Type.CREATE, this.box.getValue()));
                        b.active = false;
                    }));
                    create.active = false;
                    this.box.setResponder(s -> create.active = !s.isEmpty());
                }
            }
            case INVITES -> {
                if (this.info.team().isPresent()) {
                    List<Pair<GameProfile, GrailTeam.TeamPosition>> invites = this.info.players().stream().filter(p -> !p.getSecond().isInTeam()).toList();
                    if (!invites.isEmpty()) {
                        this.addRenderableWidget(new SelectableListWidget(this.leftPos + 25, this.topPos + 37, 170, 128, this.font,
                                invites.stream().<SelectableEntry>map(t -> {
                                    if (t.getSecond() == GrailTeam.TeamPosition.INVITED) {
                                        return new SelectableText("⏳ " + t.getFirst().getName())
                                                .with(this.getButton(C2STeamUuidMessage.Type.RETRACT_INVITE, t.getFirst().getId()));
                                    } else {
                                        return new SelectableText(t.getFirst().getName(), ChatFormatting.AQUA)
                                                .with(this.getButton(C2STeamUuidMessage.Type.INVITE, t.getFirst().getId()));
                                    }
                                }).toList()).selectMultiple());
                    }
                } else {
                    this.addRenderableWidget(new SelectableListWidget(this.leftPos + 25, this.topPos + 37, 170, 128, this.font,
                            this.info.invites().stream().<SelectableEntry>map(t -> new SelectableText(t.name())
                                    .with(this.getButton(C2STeamUuidMessage.Type.ACCEPT_INVITE, t.id()),
                                            this.getButton(C2STeamUuidMessage.Type.DENY_INVITE, t.id()))).toList()));
                }
                this.addRenderableWidget(new CustomButton(this.leftPos + 8, this.topPos + 8, 20, 20,
                        new TextComponent(""), b -> this.changePage(Pages.MAIN)).setTexture(WIDGETS, 0, 0));
            }
            case ALLIES -> {
                this.addRenderableWidget(new SelectableListWidget(this.leftPos + 25, this.topPos + 37, 170, 128, this.font,
                        this.info.others().stream().<SelectableEntry>map(t -> {
                            if (t.getSecond() == GrailTeam.TeamStatus.INCOMING_REQUEST) {
                                return new SelectableText("✉ " + t.getFirst().name(), ChatFormatting.GREEN)
                                        .with(this.getButton(C2STeamUuidMessage.Type.ACCEPT_ALLY, t.getFirst().id()),
                                                this.getButton(C2STeamUuidMessage.Type.DENY_ALLY, t.getFirst().id()));
                            } else if (t.getSecond() == GrailTeam.TeamStatus.OUTGOING_REQUEST) {
                                return new SelectableText("⏳ " + t.getFirst().name())
                                        .with(this.getButton(C2STeamUuidMessage.Type.RETRACT_REQUEST, t.getFirst().id()));
                            } else if (t.getSecond() == GrailTeam.TeamStatus.ALLY) {
                                return new SelectableText("\uD83D\uDEE1 " + t.getFirst().name(), ChatFormatting.AQUA)
                                        .with(this.getButton(C2STeamUuidMessage.Type.DISSOLVE_ALLY, t.getFirst().id()));
                            } else {
                                return new SelectableText(t.getFirst().name())
                                        .with(this.getButton(C2STeamUuidMessage.Type.REQUEST_ALLY, t.getFirst().id()));
                            }
                        }).toList()));
                this.addRenderableWidget(new CustomButton(this.leftPos + 8, this.topPos + 8, 20, 20,
                        new TextComponent(""), b -> this.changePage(Pages.MAIN)).setTexture(WIDGETS, 0, 0));
            }
            case MEMBERS -> {
                List<Pair<GameProfile, GrailTeam.TeamPosition>> members = this.info.players().stream().filter(p -> p.getSecond().isInTeam()).toList();
                if (!members.isEmpty()) {
                    this.addRenderableWidget(new SelectableListWidget(this.leftPos + 25, this.topPos + 37, 170, 128, this.font,
                            members.stream().<SelectableEntry>map(t -> {
                                if (t.getSecond() == GrailTeam.TeamPosition.ADMIN) {
                                    return new SelectableText("◇ " + t.getFirst().getName(), ChatFormatting.AQUA)
                                            .with(this.getButton(C2STeamUuidMessage.Type.DEMOTE, t.getFirst().getId()));
                                } else if (t.getSecond() == GrailTeam.TeamPosition.CREATOR) {
                                    return new SelectableText("☆ " + t.getFirst().getName(), ChatFormatting.GOLD)
                                            .noSelect();
                                } else {
                                    return new SelectableText(t.getFirst().getName())
                                            .with(this.getButton(C2STeamUuidMessage.Type.PROMOTE, t.getFirst().getId()),
                                                    this.getButton(C2STeamUuidMessage.Type.KICK, t.getFirst().getId()));
                                }
                            }).toList()));
                }
                this.addRenderableWidget(new CustomButton(this.leftPos + 8, this.topPos + 8, 20, 20,
                        new TextComponent(""), b -> this.changePage(Pages.MAIN)).setTexture(WIDGETS, 0, 0));
            }
        }
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderTexture(0, this.page.texture);
        this.blit(stack, this.leftPos, this.topPos, 0, 0, this.sizeX, this.sizeY);
        if (this.leaveButton != null) {
            this.leaveButton.active = hasShiftDown();
        }
        if (this.page == Pages.MAIN) {
            if (this.info.team().isPresent()) {
                Component txt = new TranslatableComponent("fateubw.gui.team.name", this.info.team().get().name());
                int width = this.font.width(txt);
                this.font.draw(stack, txt,
                        this.leftPos + this.sizeX / 2 - width / 2, this.topPos + 15, 0);
                if (this.box != null && !this.box.canConsumeInput()) {
                    boolean vis = this.box.visible;
                    this.box.visible = true;
                    if (this.box.isMouseOver(mouseX, mouseY)) {
                        this.renderTooltip(stack, new TranslatableComponent("fateubw.gui.team.rename"), mouseX, mouseY);
                    }
                    this.box.visible = vis;
                }
            } else {
                int y = 0;
                for (FormattedCharSequence lines : this.font.split(new TranslatableComponent("fateubw.gui.team.none"), this.sizeX / 2 - 18)) {
                    this.font.draw(stack, lines,
                            this.leftPos + 18, this.topPos + 18 + y * 11, 0);
                    y += 1;
                }
            }
        } else {
            Component txt = this.page.title;
            int width = this.font.width(txt);
            this.font.draw(stack, txt, this.leftPos + this.sizeX / 2 - width / 2, this.topPos + 12, 0);
        }
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    private SelectableText.SelectButton getButton(C2STeamUuidMessage.Type type, UUID uuid) {
        return switch (type) {
            case INVITE, REQUEST_ALLY -> new SelectableText.SelectButton(WIDGETS, 21, 0,
                    () -> NetworkCalls.INSTANCE.sendToServer(new C2STeamUuidMessage(type, uuid)), () -> this.admin);
            case RETRACT_INVITE, RETRACT_REQUEST -> new SelectableText.SelectButton(WIDGETS, 33, 0,
                    () -> NetworkCalls.INSTANCE.sendToServer(new C2STeamUuidMessage(type, uuid)), () -> this.admin);
            case ACCEPT_INVITE -> new SelectableText.SelectButton(WIDGETS, 45, 0,
                    () -> NetworkCalls.INSTANCE.sendToServer(new C2STeamUuidMessage(type, uuid)));
            case DENY_INVITE -> new SelectableText.SelectButton(WIDGETS, 57, 0,
                    () -> NetworkCalls.INSTANCE.sendToServer(new C2STeamUuidMessage(type, uuid)));
            case ACCEPT_ALLY -> new SelectableText.SelectButton(WIDGETS, 45, 0,
                    () -> NetworkCalls.INSTANCE.sendToServer(new C2STeamUuidMessage(type, uuid)), () -> this.admin);
            case DENY_ALLY -> new SelectableText.SelectButton(WIDGETS, 57, 0,
                    () -> NetworkCalls.INSTANCE.sendToServer(new C2STeamUuidMessage(type, uuid)), () -> this.admin);
            case KICK, DISSOLVE_ALLY -> new SelectableText.SelectButton(WIDGETS, 57, 0,
                    () -> NetworkCalls.INSTANCE.sendToServer(new C2STeamUuidMessage(type, uuid)), () -> this.admin && Screen.hasShiftDown());
            case PROMOTE -> new SelectableText.SelectButton(WIDGETS, 69, 0,
                    () -> NetworkCalls.INSTANCE.sendToServer(new C2STeamUuidMessage(type, uuid)), () -> this.admin);
            case DEMOTE -> new SelectableText.SelectButton(WIDGETS, 81, 0,
                    () -> NetworkCalls.INSTANCE.sendToServer(new C2STeamUuidMessage(type, uuid)), () -> this.admin && Screen.hasShiftDown());
        };
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.box != null && this.info.team().isPresent()) {
            this.box.visible = true;
            if (this.box.isMouseOver(mouseX, mouseY)) {
                this.box.setValue(this.info.team().get().name());
                this.box.setFocus(true);
            } else {
                this.box.visible = false;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.box != null && this.info.team().isPresent() && keyCode == GLFW.GLFW_KEY_ENTER) {
            this.box.visible = false;
            this.box.setFocus(false);
            if (!this.box.getValue().isEmpty())
                NetworkCalls.INSTANCE.sendToServer(new C2STeamMessage(C2STeamMessage.Type.RENAME, this.box.getValue()));
            return true;
        }
        if ((this.box == null || !this.box.canConsumeInput()) && this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        super.onClose();
        NetworkCalls.INSTANCE.sendToServer(new C2STeamMessage(C2STeamMessage.Type.CLOSE, ""));
    }

    public void update(GrailTeam.ClientTeamInfo info, boolean reInit) {
        this.info = info;
        this.admin = this.info.team().map(GrailTeam.ShortTeamInfo::admin).orElse(false);
        if (this.info.team().isEmpty() && this.page != Pages.MAIN && this.page != Pages.INVITES) {
            this.page = Pages.MAIN;
        }
        if (reInit) {
            this.changePage(this.page);
        }
    }

    private enum Pages {

        MAIN(new ResourceLocation(Fate.MODID, "textures/gui/team_gui_1.png"), null, 220, 160),
        INVITES(new ResourceLocation(Fate.MODID, "textures/gui/team_gui_2.png"), new TranslatableComponent("fateubw.gui.team.invites"), 220, 206),
        ALLIES(new ResourceLocation(Fate.MODID, "textures/gui/team_gui_2.png"), new TranslatableComponent("fateubw.gui.team.allies"), 220, 206),
        MEMBERS(new ResourceLocation(Fate.MODID, "textures/gui/team_gui_2.png"), new TranslatableComponent("fateubw.gui.team.members"), 220, 206);

        public final ResourceLocation texture;
        public final Component title;
        public final int sizeX, sizeY;

        Pages(ResourceLocation texture, Component title, int sizeX, int sizeY) {
            this.texture = texture;
            this.title = title;
            this.sizeX = sizeX;
            this.sizeY = sizeY;
        }
    }
}