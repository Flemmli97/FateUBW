package io.github.flemmli97.fateubw.common.particles.trail;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.particles.trail.provider.ParticlePositionProvider;
import io.github.flemmli97.fateubw.common.particles.trail.provider.TrailData;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityWeaponTrailProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class TrailProviderRegistry {

    private static final Map<ResourceLocation, TrailType<?>> DECODERS = new HashMap<>();

    public static final Codec<TrailData> CODEC = ResourceLocation.CODEC.dispatch(t -> t.type().id(), r -> DECODERS.get(r).codec);

    public static final TrailType<EntityWeaponTrailProvider.EntityTrailData> ENTITY_TRAIL = register(Fate.modRes("entity_trail"), EntityWeaponTrailProvider.EntityTrailData.CODEC, EntityWeaponTrailProvider.EntityTrailData.STREAM_CODEC);
    public static final TrailType<ParticlePositionProvider.ParticlePositionData> MOTION_TRAIL = register(Fate.modRes("motion_trail"), ParticlePositionProvider.ParticlePositionData.CODEC, ParticlePositionProvider.ParticlePositionData.STREAM_CODEC);

    public static synchronized <T extends TrailData> TrailType<T> register(ResourceLocation res, MapCodec<T> codec, StreamCodec<? super FriendlyByteBuf, T> streamCodec) {
        if (DECODERS.containsKey(res))
            throw new IllegalStateException("Entry with key " + res + " is already registered");
        TrailType<T> entry = new TrailType<>(res, codec, streamCodec);
        DECODERS.put(res, entry);
        return entry;
    }

    public static TrailData fromBuffer(FriendlyByteBuf buf) {
        ResourceLocation id = buf.readResourceLocation();
        TrailType<?> entry = DECODERS.get(id);
        if (entry == null)
            throw new IllegalStateException("No such provider " + id);
        return entry.streamCodec.decode(buf);
    }

    @SuppressWarnings("unchecked")
    public static <T extends TrailData> void toBuffer(T data, FriendlyByteBuf buf) {
        buf.writeResourceLocation(data.type().id());
        TrailType<T> type = (TrailType<T>) data.type();
        type.streamCodec().encode(buf, data);
    }

    public record TrailType<T extends TrailData>(ResourceLocation id, MapCodec<T> codec,
                                                 StreamCodec<? super FriendlyByteBuf, T> streamCodec) {

    }
}
