package io.github.flemmli97.fateubw.fabric.mixin;

import io.github.flemmli97.fateubw.common.event.EventCalls;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @Inject(method = "restoreFrom", at = @At("RETURN"))
    private void copyOld(ServerPlayer oldPlayer, boolean alive, CallbackInfo info) {
        EventCalls.clone(oldPlayer, (ServerPlayer) (Object) this);
    }
}
