package io.github.flemmli97.fateubw.client.screen;

import io.github.flemmli97.fateubw.common.registry.FateDimensions;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.Level;

import java.util.Set;
import java.util.function.BooleanSupplier;

public class RealityMarbleTransitionScreen extends ReceivingLevelScreen {

    public static final Set<ResourceKey<Level>> DIMENSIONS = Set.of(FateDimensions.SAND_DUNES.dimension(),
            FateDimensions.UNLIMITED_BLADEWORKS.dimension());

    private static final int MAX_DURATION = 20;

    private int tick;

    public RealityMarbleTransitionScreen(BooleanSupplier sup, Reason reason, ResourceKey<Level> dimension) {
        super(sup, reason);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        float progress = 1 - (Math.min(20, (this.tick + partialTick)) / 20);
        int color = FastColor.ARGB32.color((int) (progress * 200), 0xffffff);
        guiGraphics.fill(0, 0, this.width, this.height, color);
    }

    @Override
    public void tick() {
        this.tick++;
        if (this.tick > MAX_DURATION) {
            super.tick();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
