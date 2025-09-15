package io.github.flemmli97.fateubw.common.particles.trail.provider;

import io.github.flemmli97.fateubw.common.particles.trail.TrailProviderRegistry;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

public interface TrailData {

    TrailProviderRegistry.TrailType<?> type();

    @Nullable
    TrailProvider createProvider(Level level, Supplier<Vec3> position, IntSupplier lifetime);
}
