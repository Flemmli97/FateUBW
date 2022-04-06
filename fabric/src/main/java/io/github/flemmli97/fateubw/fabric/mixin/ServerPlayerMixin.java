package io.github.flemmli97.fateubw.fabric.mixin;

import io.github.flemmli97.fateubw.common.network.S2CPlayerCap;
import io.github.flemmli97.fateubw.fabric.common.data.PlayerDataGet;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Inject(method = "restoreFrom", at = @At("RETURN"))
    private void copyOld(ServerPlayer oldPlayer, boolean alive, CallbackInfo info) {
        ((PlayerDataGet) this).getData().from(((PlayerDataGet) oldPlayer).getData());
        NetworkCalls.INSTANCE.sendToClient(new S2CPlayerCap(((PlayerDataGet) this).getData()), (ServerPlayer) (Object) this);
    }
}
