package io.github.flemmli97.fateubw.fabric.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.flemmli97.fateubw.client.screen.RealityMarbleTransitionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.client.multiplayer.LevelLoadStatusManager;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin extends ClientCommonPacketListenerImpl {

    @Shadow
    private LevelLoadStatusManager levelLoadStatusManager;
    @Unique
    private ResourceKey<Level> fate$sourceLevel, fate$targetLevel;

    private ClientPacketListenerMixin(Minecraft minecraft, Connection connection, CommonListenerCookie commonListenerCookie) {
        super(minecraft, connection, commonListenerCookie);
    }

    @Inject(method = "handleRespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;startWaitingForNewLevel(Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/client/gui/screens/ReceivingLevelScreen$Reason;)V"))
    private void onRespawn(ClientboundRespawnPacket packet, CallbackInfo ci, @Local(ordinal = 0) ResourceKey<Level> resourcekey, @Local(ordinal = 1) ResourceKey<Level> resourcekey1) {
        this.fate$sourceLevel = this.minecraft.player.isDeadOrDying() ? null : resourcekey;
        this.fate$targetLevel = this.minecraft.player.isDeadOrDying() ? null : resourcekey1;
    }

    @Inject(method = "startWaitingForNewLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"), cancellable = true)
    private void onStartWaiting(LocalPlayer player, ClientLevel level, ReceivingLevelScreen.Reason reason, CallbackInfo ci) {
        if (this.fate$sourceLevel != null && RealityMarbleTransitionScreen.DIMENSIONS.contains(this.fate$sourceLevel)) {
            ci.cancel();
            this.minecraft.setScreen(new RealityMarbleTransitionScreen(this.levelLoadStatusManager::levelReady, reason, this.fate$sourceLevel));
        }
        if (this.fate$targetLevel != null && RealityMarbleTransitionScreen.DIMENSIONS.contains(this.fate$targetLevel)) {
            ci.cancel();
            this.minecraft.setScreen(new RealityMarbleTransitionScreen(this.levelLoadStatusManager::levelReady, reason, this.fate$targetLevel));
        }
    }
}
