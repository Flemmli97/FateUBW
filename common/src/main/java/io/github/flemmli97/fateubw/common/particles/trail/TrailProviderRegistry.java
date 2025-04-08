package io.github.flemmli97.fateubw.common.particles.trail;

import com.mojang.serialization.Codec;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.particles.trail.provider.EntityTrailProvider;
import io.github.flemmli97.fateubw.common.particles.trail.provider.MotionTrailProvider;
import io.github.flemmli97.fateubw.common.particles.trail.provider.TrailProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class TrailProviderRegistry {

    private static final Map<ResourceLocation, TrailEntry<?>> DECODERS = new HashMap<>();

    public static final Codec<TrailProvider> CODEC = ResourceLocation.CODEC.dispatch(TrailProvider::id, r -> DECODERS.get(r).codec);

    public static final ResourceLocation ENTITY_TRAIL = register(new ResourceLocation(Fate.MODID, "entity_trail"), EntityTrailProvider::new, EntityTrailProvider.CODEC);
    public static final ResourceLocation MOTION_TRAIL = register(new ResourceLocation(Fate.MODID, "motion_trail"), MotionTrailProvider::new, MotionTrailProvider.CODEC);

    public static synchronized <T extends TrailProvider> ResourceLocation register(ResourceLocation res, Function<FriendlyByteBuf, T> decoder, Codec<T> codec) {
        if (DECODERS.containsKey(res))
            throw new IllegalStateException("Entry with key " + res + " is already registered");
        DECODERS.put(res, new TrailEntry<>(decoder, codec));
        return res;
    }

    public static TrailProvider fromBuffer(FriendlyByteBuf buf) {
        ResourceLocation id = buf.readResourceLocation();
        TrailEntry<?> decoder = DECODERS.get(id);
        if (decoder == null)
            throw new IllegalStateException("No such provider " + id);
        return decoder.decoder.apply(buf);
    }

    public static void toBuffer(TrailProvider provider, FriendlyByteBuf buf) {
        buf.writeResourceLocation(provider.id());
        provider.write(buf);
    }

    public record TrailEntry<T extends TrailProvider>(Function<FriendlyByteBuf, T> decoder, Codec<T> codec) {
    }
}
