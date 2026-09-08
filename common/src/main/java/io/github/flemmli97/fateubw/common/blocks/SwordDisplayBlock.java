package io.github.flemmli97.fateubw.common.blocks;

import com.mojang.serialization.MapCodec;
import io.github.flemmli97.fateubw.common.blocks.entity.SwordDisplayEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SwordDisplayBlock extends BaseEntityBlock {

    public static final MapCodec<SwordDisplayBlock> CODEC = simpleCodec(SwordDisplayBlock::new);

    public static final int MAX = RotationSegment.getMaxSegmentIndex();
    private static final int ROTATIONS = MAX + 1;
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
    public static final BooleanProperty FILLED = BooleanProperty.create("filled");
    private static final VoxelShape SHAPE = Block.box(2, 0.0, 2, 14, 2, 14);
    private static final VoxelShape SHAPE_FILLED = Block.box(1, 0.0, 1, 14, 10, 15);

    public SwordDisplayBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(ROTATION, 0).setValue(FILLED, false));
    }

    @Override
    public MapCodec<? extends SwordDisplayBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(FILLED)) {
            return Shapes.empty();
        }
        return super.getCollisionShape(state, level, pos, context);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(FILLED)) {
            return SHAPE_FILLED;
        }
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(ROTATION, RotationSegment.convertToSegment(context.getRotation()));
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(ROTATION, rotation.rotate(state.getValue(ROTATION), ROTATIONS));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(ROTATION, mirror.mirror(state.getValue(ROTATION), ROTATIONS));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!state.is(oldState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof SwordDisplayEntity entity) {
                ItemStack stack = entity.item();
                if (!stack.isEmpty()) {
                    ItemEntity item = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                    level.addFreshEntity(item);
                }
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, oldState, isMoving);
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof SwordDisplayEntity displayEntity))
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (!(player instanceof ServerPlayer)) {
            return ItemInteractionResult.SUCCESS;
        }
        if (player.isShiftKeyDown()) {
            if (displayEntity.removeItem(player))
                return ItemInteractionResult.CONSUME;
            return ItemInteractionResult.FAIL;
        } else if (hand == InteractionHand.MAIN_HAND && stack.isEmpty()) {
            displayEntity.flipRotation();
            return ItemInteractionResult.CONSUME;
        } else if (displayEntity.setItem(player, stack)) {
            return ItemInteractionResult.CONSUME;
        }
        return ItemInteractionResult.FAIL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ROTATION).add(FILLED);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SwordDisplayEntity(pos, state);
    }
}
