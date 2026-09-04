package io.github.flemmli97.fateubw.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.flemmli97.fateubw.common.registry.FateAttachments;
import io.github.flemmli97.fateubw.common.world.realitymarble.RealityMarbleHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

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

    @Inject(method = "collectColliders", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList$Builder;build()Lcom/google/common/collect/ImmutableList;"))
    private static void onCollectColliders(Entity entity, Level level, List<VoxelShape> collisions, AABB boundingBox, CallbackInfoReturnable<List<VoxelShape>> info,
                                           @Local ImmutableList.Builder<VoxelShape> builder) {
        if (entity != null) {
            VoxelShape shape = FateAttachments.REALITY_MARBLE_CONSTRAINT.get().get(entity).getShape(boundingBox);
            if (shape != null) {
                builder.add(shape);
            }
        }
    }
}
