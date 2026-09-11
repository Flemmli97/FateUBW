package io.github.flemmli97.fateubw.mixin.compat;

import io.github.flemmli97.fateubw.common.registry.FateAttachments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

/**
 * Lithium overwrites collision logic completely
 */
@Mixin(targets = "net.caffeinemc.mods.lithium.common.entity.LithiumEntityCollisions")
public class LithiumEntityCollisionMixin {

    @Inject(method = "appendWorldBorderCollision", at = @At("RETURN"))
    private static void addBorders(ArrayList<VoxelShape> worldBorderCollisions, Entity entity, AABB box, CallbackInfo info) {
        if (entity != null) {
            VoxelShape shape = FateAttachments.REALITY_MARBLE_CONSTRAINT.get().get(entity).getShape(box);
            if (shape != null) {
                worldBorderCollisions.add(shape);
            }
        }
    }
}
