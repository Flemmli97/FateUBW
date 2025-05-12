package io.github.flemmli97.fateubw.common.particles.trail.provider;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface TrailData {

    ResourceLocation id();

    void write(FriendlyByteBuf buf);

    @Nullable
    TrailProvider createProvider(Level level);
}
