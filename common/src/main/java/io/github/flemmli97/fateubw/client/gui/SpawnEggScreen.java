package io.github.flemmli97.fateubw.client.gui;

import io.github.flemmli97.fateubw.common.components.ServantSpawneggData;
import io.github.flemmli97.fateubw.common.network.C2SSpawnEgg;
import io.github.flemmli97.fateubw.common.registry.FateDataComponents;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SpawnEggScreen extends Screen {

    private final Player player;
    protected LivingEntity entity;
    private final InteractionHand hand;
    private int leftPos, topPos;
    private final int sizeX = 240;
    private final int sizeY = 200;

    private Checkbox withMasterCheckbox, warCheckBox;
    private boolean withMaster, joinWar;

    public SpawnEggScreen(InteractionHand hand) {
        super(Component.empty());
        this.hand = hand;
        this.player = Minecraft.getInstance().player;
    }

    @Override
    protected void init() {
        super.init();
        ItemStack stack = this.player.getItemInHand(this.hand);
        EntityType<?> type;
        if (stack.getItem() instanceof SpawnEgg egg) {
            type = egg.getType(stack);
        } else {
            Minecraft.getInstance().setScreen(null);
            return;
        }
        Entity e = type.create(Minecraft.getInstance().level);
        if (e instanceof LivingEntity living) {
            this.entity = living;
        } else {
            Minecraft.getInstance().setScreen(null);
            return;
        }
        ServantSpawneggData data = stack.getOrDefault(FateDataComponents.SERVANT_EGG_DATA.get(), ServantSpawneggData.DEFAULT);
        this.withMaster = data.withMaster();
        this.joinWar = data.joinGrailwar();
        this.leftPos = this.width / 2 - (this.sizeX / 2);
        this.topPos = this.height / 2 - (this.sizeY / 2);
        this.buttons();
    }

    @Override
    public void tick() {
        super.tick();
        this.entity.tickCount++;
    }

    @Override
    protected void renderBlurredBackground(float partialTick) {
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.fillGradient(this.leftPos, this.topPos, this.leftPos + this.sizeX, this.topPos + this.sizeY, 0xc0101010, 0xc0101010);
        int xPadding = 16;
        int yOff = 16;
        graphics.drawString(this.font, Component.translatable("fateubw.gui.spawn.master"), this.leftPos + xPadding, this.topPos + yOff, 0xffffff, true);
        yOff += 16 + 20 + 8;
        graphics.drawString(this.font, Component.translatable("fateubw.gui.spawn.war"), this.leftPos + xPadding, this.topPos + yOff, 0xffffff, true);
        yOff += 16;
        graphics.drawString(this.font, Component.translatable("fateubw.gui.spawn.war.help"), this.leftPos + xPadding, this.topPos + yOff, 0xffffff, true);
        int posX = 180;
        int posY = 100;
        RenderUtils.renderScaledEntityGui(graphics, this.leftPos + posX, this.topPos + posY, 32 * 3,
                32 * 3, 32, 0, mouseX, mouseY, this.entity);
    }

    protected void buttons() {
        int padding = 16;
        int yOff = padding + 12;
        this.withMasterCheckbox = Checkbox.builder(Component.empty(), this.font)
                .selected(this.withMaster)
                .pos(this.leftPos + padding, this.topPos + yOff)
                .maxWidth(20)
                .onValueChange((b, a) -> this.withMaster = a).build();
        this.addRenderableWidget(this.withMasterCheckbox);
        yOff += 16 + 20 + 8 + 16;
        this.warCheckBox = Checkbox.builder(Component.empty(), this.font)
                .selected(this.joinWar)
                .pos(this.leftPos + padding, this.topPos + yOff)
                .maxWidth(20)
                .onValueChange((b, a) -> this.joinWar = a).build();
        this.addRenderableWidget(this.warCheckBox);
        yOff += 16 + 20 + 44;
        this.addRenderableWidget(Button.builder(Component.translatable("fateubw.gui.save"), b -> {
            LoaderNetwork.INSTANCE.sendToServer(new C2SSpawnEgg(this.hand, this.withMasterCheckbox.selected(), this.warCheckBox.selected()));
            this.minecraft.setScreen(null);
        }).bounds(this.leftPos + this.sizeX / 2 - 50, this.topPos + yOff, 100, 20).build());
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
