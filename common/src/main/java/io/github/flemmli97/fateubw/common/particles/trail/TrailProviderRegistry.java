package io.github.flemmli97.fateubw.common.particles.trail;

import com.mojang.serialization.Codec;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.particles.trail.provider.MotionTrailProvider;
import io.github.flemmli97.fateubw.common.particles.trail.provider.TrailData;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityTrailProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class TrailProviderRegistry {

    private static final Map<ResourceLocation, TrailEntry<?>> DECODERS = new HashMap<>();

    public static final Codec<TrailData> CODEC = ResourceLocation.CODEC.dispatch(TrailData::id, r -> DECODERS.get(r).codec);

    public static final ResourceLocation ENTITY_TRAIL = register(new ResourceLocation(Fate.MODID, "entity_trail"), EntityTrailProvider.EntityTrailData::new, EntityTrailProvider.EntityTrailData.CODEC);
    public static final ResourceLocation MOTION_TRAIL = register(new ResourceLocation(Fate.MODID, "motion_trail"), MotionTrailProvider.MotionTrailData::new, MotionTrailProvider.MotionTrailData.CODEC);

    public static synchronized <T extends TrailData> ResourceLocation register(ResourceLocation res, Function<FriendlyByteBuf, T> decoder, Codec<T> codec) {
        if (DECODERS.containsKey(res))
            throw new IllegalStateException("Entry with key " + res + " is already registered");
        DECODERS.put(res, new TrailEntry<>(decoder, codec));
        return res;
    }

    public static TrailData fromBuffer(FriendlyByteBuf buf) {
        ResourceLocation id = buf.readResourceLocation();
        TrailEntry<?> entry = DECODERS.get(id);
        if (entry == null)
            throw new IllegalStateException("No such provider " + id);
        return entry.decoder.apply(buf);
    }

    public static void toBuffer(TrailData data, FriendlyByteBuf buf) {
        buf.writeResourceLocation(data.id());
        data.write(buf);
    }

    public record TrailEntry<T extends TrailData>(Function<FriendlyByteBuf, T> decoder, Codec<T> codec) {
    }
}
