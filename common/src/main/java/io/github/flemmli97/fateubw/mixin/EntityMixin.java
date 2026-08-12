package io.github.flemmli97.fateubw.mixin;

import io.github.flemmli97.fateubw.common.world.RealityMarbleHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    @Nullable
    public abstract MinecraftServer getServer();

    @Inject(method = "setRemoved", at = @At("RETURN"))
    private void onRemove(Entity.RemovalReason removalReason, CallbackInfo info) {
        if (this.getServer() != null && removalReason.shouldDestroy()) {
            RealityMarbleHandler.get(this.getServer())
                    .removeFromGroup((Entity) (Object) this);
        }
    }
}
