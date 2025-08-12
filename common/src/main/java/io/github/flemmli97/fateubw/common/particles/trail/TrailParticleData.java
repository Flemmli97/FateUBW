package io.github.flemmli97.fateubw.common.particles.trail;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class TrailParticleData implements ParticleOptions {

    private final ParticleType<TrailParticleData> type;
    private final TrailInfo trailInfo;

    public TrailParticleData(ParticleType<TrailParticleData> type, TrailInfo trailInfo) {
        this.type = type;
        this.trailInfo = trailInfo;
    }

    public static MapCodec<TrailParticleData> codec(ParticleType<TrailParticleData> type) {
        return TrailInfo.CODEC.fieldOf("trail").xmap(info -> new TrailParticleData(type, info), TrailParticleData::getTrailInfo);
    }

    public static StreamCodec<RegistryFriendlyByteBuf, TrailParticleData> streamCodec(ParticleType<TrailParticleData> type) {
        return TrailInfo.STREAM_CODEC.map(info -> new TrailParticleData(type, info), TrailParticleData::getTrailInfo);
    }

    @Override
    public ParticleType<?> getType() {
        return this.type;
    }

    public TrailInfo getTrailInfo() {
        return this.trailInfo;
    }
}