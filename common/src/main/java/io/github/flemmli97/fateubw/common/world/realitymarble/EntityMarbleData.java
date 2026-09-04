package io.github.flemmli97.fateubw.common.world.realitymarble;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.UUID;

public record EntityMarbleData(UUID group, ResourceKey<Level> sourceLevel, BlockPos sourcePosition,
                               BlockPos targetPosition) {

    public static final Codec<EntityMarbleData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(UUIDUtil.CODEC.fieldOf("group").forGetter(EntityMarbleData::group),
                            ResourceKey.codec(Registries.DIMENSION).fieldOf("source_level").forGetter(EntityMarbleData::sourceLevel),
                            BlockPos.CODEC.fieldOf("source_position").forGetter(EntityMarbleData::sourcePosition),
                            BlockPos.CODEC.fieldOf("target_position").forGetter(EntityMarbleData::targetPosition))
                    .apply(instance, EntityMarbleData::new));

    public EntityMarbleData(RealityMarbleGroup group) {
        this(group.id(), group.sourceLevel(), group.sourcePosition(), group.targetPosition());
    }
}
