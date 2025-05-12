package io.github.flemmli97.fateubw.common.particles.trail;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import io.github.flemmli97.fateubw.common.particles.trail.provider.MotionTrailProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public class TrailParticleData implements ParticleOptions {

    @SuppressWarnings("deprecation")
    public static final Deserializer<TrailParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public TrailParticleData fromCommand(ParticleType<TrailParticleData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float r = reader.readFloat();
            reader.expect(' ');
            float g = reader.readFloat();
            reader.expect(' ');
            float b = reader.readFloat();
            reader.expect(' ');
            float a = reader.readFloat();
            reader.expect(' ');
            float scale = reader.readFloat();
            reader.expect(' ');
            float r2 = reader.readFloat();
            reader.expect(' ');
            float g2 = reader.readFloat();
            reader.expect(' ');
            float b2 = reader.readFloat();
            reader.expect(' ');
            float a2 = reader.readFloat();
            reader.expect(' ');
            float scale2 = reader.readFloat();
            reader.expect(' ');
            TrailInfo.Visual visual;
            int txtIdx;
            try {
                visual = TrailInfo.Visual.valueOf(reader.readString());
                reader.expect(' ');
                txtIdx = reader.readInt();
            } catch (CommandSyntaxException | IllegalArgumentException e) {
                visual = TrailInfo.Visual.SOLID;
                txtIdx = 0;
            }
            reader.expect(' ');

            float x = reader.readFloat();
            reader.expect(' ');
            float y = reader.readFloat();
            reader.expect(' ');
            float z = reader.readFloat();
            reader.expect(' ');
            int frames = reader.readInt();
            reader.expect(' ');
            int duration = reader.readInt();
            return new TrailParticleData(type, new TrailInfo(r, g, b, a, scale, r2, g2, b2, a2, scale2, visual, txtIdx,
                    new MotionTrailProvider.MotionTrailData(x, y, z, frames, duration)));
        }

        @Override
        public TrailParticleData fromNetwork(ParticleType<TrailParticleData> type, FriendlyByteBuf buffer) {
            return new TrailParticleData(type, new TrailInfo(buffer));
        }
    };
    private final ParticleType<TrailParticleData> type;
    private final TrailInfo trailInfo;

    public TrailParticleData(ParticleType<TrailParticleData> type, TrailInfo trailInfo) {
        this.type = type;
        this.trailInfo = trailInfo;
    }

    public static Codec<TrailParticleData> codec(ParticleType<TrailParticleData> type) {
        return TrailInfo.CODEC.xmap(i -> new TrailParticleData(type, i), TrailParticleData::getTrailInfo);
    }

    @Override
    public ParticleType<?> getType() {
        return this.type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        this.trailInfo.toBuffer(buffer);
    }

    @Override
    public String writeToString() {
        return Registry.PARTICLE_TYPE.getKey(this.getType()).toString();
    }

    public TrailInfo getTrailInfo() {
        return this.trailInfo;
    }
}