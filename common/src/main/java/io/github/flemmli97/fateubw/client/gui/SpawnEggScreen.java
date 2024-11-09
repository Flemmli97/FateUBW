package io.github.flemmli97.fateubw.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.fateubw.common.items.FateEgg;
import io.github.flemmli97.fateubw.common.network.C2SSpawnEgg;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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
        super(new TextComponent(""));
        this.hand = hand;
        this.player = Minecraft.getInstance().player;
    }

    @Override
    protected void init() {
        super.init();
        ItemStack stack = this.player.getItemInHand(this.hand);
        EntityType<?> type;
        if (stack.getItem() instanceof SpawnEgg egg) {
            type = egg.getType(stack.getTag());
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
        this.withMaster = FateEgg.spawnOwned(stack);
        this.joinWar = FateEgg.joinGrailwar(stack);
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
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTick) {
        this.fillGradient(stack, this.leftPos, this.topPos, this.leftPos + this.sizeX, this.topPos + this.sizeY, 0xc0101010, 0xc0101010);
        int xPadding = 16;
        int yOff = 16;
        this.minecraft.font.draw(stack, new TranslatableComponent("fateubw.gui.spawn.master"), this.leftPos + xPadding, this.topPos + yOff, 0xffffff);
        yOff += 16 + 20 + 8;
        this.minecraft.font.draw(stack, new TranslatableComponent("fateubw.gui.spawn.war"), this.leftPos + xPadding, this.topPos + yOff, 0xffffff);
        yOff += 16;
        this.minecraft.font.draw(stack, new TranslatableComponent("fateubw.gui.spawn.war.help"), this.leftPos + xPadding, this.topPos + yOff, 0xffffff);
        int posX = 180;
        int posY = 100;
        InventoryScreen.renderEntityInInventory(this.leftPos + posX, this.topPos + posY, 32, this.leftPos + posX - mouseX, this.topPos + (posY - 35) - mouseY, this.entity);
        super.render(stack, mouseX, mouseY, partialTick);
    }

    protected void buttons() {
        int padding = 16;
        int yOff = padding + 12;
        this.withMasterCheckbox = new Checkbox(this.leftPos + padding, this.topPos + yOff, 20, 20, new TextComponent(""), this.withMaster) {
            @Override
            public boolean charTyped(char codePoint, int modifiers) {
                if (Character.isDigit(codePoint))
                    return super.charTyped(codePoint, modifiers);
                return false;
            }
        };
        this.addRenderableWidget(this.withMasterCheckbox);
        yOff += 16 + 20 + 8 + 16;
        this.warCheckBox = new Checkbox(this.leftPos + padding, this.topPos + yOff, 20, 20, new TextComponent(""), this.joinWar) {
            @Override
            public boolean charTyped(char codePoint, int modifiers) {
                if (Character.isDigit(codePoint))
                    return super.charTyped(codePoint, modifiers);
                return false;
            }
        };
        this.addRenderableWidget(this.warCheckBox);
        yOff += 16 + 20 + 44;
        this.addRenderableWidget(new Button(this.leftPos + this.sizeX / 2 - 50, this.topPos + yOff, 100, 20, new TranslatableComponent("fateubw.gui.save"), b -> {
            NetworkCalls.INSTANCE.sendToServer(new C2SSpawnEgg(this.hand, this.withMasterCheckbox.selected(), this.warCheckBox.selected()));
            this.minecraft.setScreen(null);
        }));
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
