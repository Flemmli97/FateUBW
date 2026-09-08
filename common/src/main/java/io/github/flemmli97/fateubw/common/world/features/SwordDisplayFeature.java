package io.github.flemmli97.fateubw.common.world.features;

import com.mojang.serialization.Codec;
import io.github.flemmli97.fateubw.common.blocks.SwordDisplayBlock;
import io.github.flemmli97.fateubw.common.blocks.entity.SwordDisplayEntity;
import io.github.flemmli97.fateubw.common.registry.FateBlocks;
import io.github.flemmli97.fateubw.common.world.features.config.SwordDisplayFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.Optional;

public class SwordDisplayFeature extends Feature<SwordDisplayFeatureConfig> {

    public SwordDisplayFeature(Codec<SwordDisplayFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SwordDisplayFeatureConfig> ctx) {
        BlockState state = FateBlocks.SWORD_DISPLAY.get().defaultBlockState();
        BlockPos pos = ctx.origin();
        if (ctx.level().getBlockState(pos).is(BlockTags.REPLACEABLE) && state.canSurvive(ctx.level(), pos)) {
            state = state.setValue(SwordDisplayBlock.ROTATION, RotationSegment.convertToSegment(360 * ctx.random().nextFloat()));
            ctx.level().setBlock(pos, state, Block.UPDATE_ALL);
            ChunkAccess chunk = ctx.level().getChunk(pos);
            SwordDisplayEntity displayEntity = null;
            boolean tagged = false;
            if (chunk.getBlockEntity(pos) instanceof SwordDisplayEntity entity) {
                displayEntity = entity;
            } else {
                CompoundTag tag = chunk.getBlockEntityNbt(pos);
                if (tag != null) {
                    BlockEntity blockentity = ((EntityBlock) state.getBlock()).newBlockEntity(pos, state);
                    if (blockentity instanceof SwordDisplayEntity entity) {
                        displayEntity = entity;
                        tagged = true;
                    }
                }
            }
            if (displayEntity != null) {
                int flips = ctx.random().nextInt(3);
                for (int i = 0; i < flips; i++) {
                    displayEntity.flipRotation();
                }
                Optional<WeightedEntry.Wrapper<ItemStack>> stack = WeightedRandom.getRandomItem(ctx.random(), ctx.config().weapons());
                if (stack.isPresent()) {
                    ItemStack displayStack = stack.get().data().copy();
                    displayStack.set(DataComponents.MAX_DAMAGE, 1);
                    displayStack.set(DataComponents.DAMAGE, 1);
                    displayEntity.setItemDirect(displayStack);
                }
                if (tagged) {
                    chunk.setBlockEntityNbt(displayEntity.saveWithFullMetadata(ctx.level().registryAccess()));
                }
            }
            return true;
        }
        return true;
    }
}