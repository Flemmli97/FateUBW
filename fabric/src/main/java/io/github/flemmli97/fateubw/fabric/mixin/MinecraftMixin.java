package io.github.flemmli97.fateubw.fabric.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.flemmli97.fateubw.client.screen.RealityMarbleTransitionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow
    protected abstract void updateScreenAndTick(Screen screen);

    @Shadow
    public ClientLevel level;

    @WrapOperation(method = "setLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;updateScreenAndTick(Lnet/minecraft/client/gui/screens/Screen;)V"))
    private void modifyLevelSetScreen(Minecraft instance, Screen screen, Operation<Void> original, ClientLevel level, ReceivingLevelScreen.Reason reason) {
        if (this.level != null && RealityMarbleTransitionScreen.DIMENSIONS.contains(this.level.dimension())) {
            this.updateScreenAndTick(new RealityMarbleTransitionScreen(() -> false, reason, this.level.dimension()));
            return;
        }
        if (RealityMarbleTransitionScreen.DIMENSIONS.contains(level.dimension())) {
            this.updateScreenAndTick(new RealityMarbleTransitionScreen(() -> false, reason, level.dimension()));
            return;
        }
        original.call(instance, screen);
    }

}
