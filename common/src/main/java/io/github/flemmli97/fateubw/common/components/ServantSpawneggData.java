package io.github.flemmli97.fateubw.common.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ServantSpawneggData(boolean withMaster, boolean joinGrailwar) {

    public static final ServantSpawneggData DEFAULT = new ServantSpawneggData(false, false);
    public static final Codec<ServantSpawneggData> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(Codec.BOOL.fieldOf("with_master").forGetter(ServantSpawneggData::withMaster),
                    Codec.BOOL.fieldOf("join_grailwar").forGetter(ServantSpawneggData::joinGrailwar)
            ).apply(instance, ServantSpawneggData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ServantSpawneggData> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL,
            ServantSpawneggData::withMaster, ByteBufCodecs.BOOL, ServantSpawneggData::joinGrailwar, ServantSpawneggData::new);
}
