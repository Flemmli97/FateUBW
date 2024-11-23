package io.github.flemmli97.fateubw.common.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class TrailParticleData implements ParticleOptions {

    public static final Deserializer<TrailParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public TrailParticleData fromCommand(ParticleType<TrailParticleData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            double x = reader.readDouble();
            reader.expect(' ');
            double y = reader.readDouble();
            reader.expect(' ');
            Vec3 start = new Vec3(x, y, reader.readDouble());
            reader.expect(' ');
            x = reader.readDouble();
            reader.expect(' ');
            y = reader.readDouble();
            reader.expect(' ');
            Vec3 end = new Vec3(x, y, reader.readDouble());
            reader.expect(' ');
            x = reader.readDouble();
            reader.expect(' ');
            y = reader.readDouble();
            reader.expect(' ');
            Vec3 control = new Vec3(x, y, reader.readDouble());
            reader.expect(' ');
            float yRot = reader.readFloat();
            reader.expect(' ');
            float xRot = reader.readFloat();
            reader.expect(' ');
            float zRot = reader.readFloat();
            reader.expect(' ');
            int duration = reader.readInt();
            reader.expect(' ');
            int fate = reader.readInt();
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
            return new TrailParticleData(type, new TrailInfo(start, end, control, yRot, xRot, zRot, duration, fate, r, g, b, a, scale, r2, g2, b2, a2, scale2));
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