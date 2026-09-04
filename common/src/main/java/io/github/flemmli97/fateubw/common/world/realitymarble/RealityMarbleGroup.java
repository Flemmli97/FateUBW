package io.github.flemmli97.fateubw.common.world.realitymarble;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.common.config.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record RealityMarbleGroup(UUID id, UUID creator, ResourceKey<Level> sourceLevel, BlockPos sourcePosition,
                                 ResourceKey<Level> targetLevel, BlockPos targetPosition, int positionIndex,
                                 Set<UUID> entities) {

    public static final Codec<RealityMarbleGroup> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(UUIDUtil.CODEC.fieldOf("id").forGetter(RealityMarbleGroup::id),
                            UUIDUtil.CODEC.fieldOf("creator").forGetter(RealityMarbleGroup::creator),
                            ResourceKey.codec(Registries.DIMENSION).fieldOf("source_level").forGetter(RealityMarbleGroup::sourceLevel),
                            BlockPos.CODEC.fieldOf("source_position").forGetter(RealityMarbleGroup::sourcePosition),
                            ResourceKey.codec(Registries.DIMENSION).fieldOf("target_level").forGetter(RealityMarbleGroup::targetLevel),
                            BlockPos.CODEC.fieldOf("target_position").forGetter(RealityMarbleGroup::targetPosition),
                            Codec.INT.fieldOf("position_index").forGetter(RealityMarbleGroup::positionIndex),
                            UUIDUtil.CODEC.listOf().fieldOf("entities").forGetter(d -> List.copyOf(d.entities())))
                    .apply(instance, RealityMarbleGroup::new));

    public RealityMarbleGroup(UUID id, UUID creator, ResourceKey<Level> sourceLevel, BlockPos sourcePosition,
                              ResourceKey<Level> targetLevel, BlockPos targetPosition, int positionIndex, Collection<UUID> entities) {
        this(id, creator, sourceLevel, sourcePosition, targetLevel, targetPosition, positionIndex, new HashSet<>(entities));
    }

    public void loadChunks(Level level) {
        int chunkRange = SectionPos.blockToSectionCoord(Mth.ceil(CommonConfig.realityMarbleSize));
        for (int x = -chunkRange; x <= chunkRange; ++x) {
            for (int z = -chunkRange; z <= chunkRange; ++z) {
                level.getChunk(x, z);
            }
        }
    }
}
