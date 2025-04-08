package io.github.flemmli97.fateubw.common.particles.trail.provider;

import io.github.flemmli97.fateubw.common.particles.trail.TrailPositions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface TrailProvider {

    void write(FriendlyByteBuf buf);

    @Nullable
    TrailPositions positions(Level level);

    @Nullable
    Vec3 particleTick(Level level);

    ResourceLocation id();

    boolean removed(Level level);

    record TickResult(Vec3 position, Function<Float, AABB> bounds) {
    }
}
