package io.github.flemmli97.fateubw.common.blocks;

import io.github.flemmli97.fateubw.common.blocks.tile.AltarBlockEntity;
import io.github.flemmli97.fateubw.common.items.ItemServantCharm;
import io.github.flemmli97.fateubw.common.registry.AdvancementRegister;
import io.github.flemmli97.fateubw.common.registry.ModBlocks;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.SummonUtils;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.stream.Stream;

public class AltarBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final double pixel = 0.0625;

    private static final VoxelShape tableShape = Stream.of(
            Block.box(13.5, 0, 0.5, 15.5, 11, 2.5),
            Block.box(0.5, 0, 0.5, 2.5, 11, 2.5),
            Block.box(0.5, 9, 0.5, 15.5, 12, 15.5),
            Block.box(13.5, 0, 13.5, 15.5, 11, 15.5),
            Block.box(0.5, 0, 13.5, 2.5, 11, 15.5),
            Block.box(0, 11.9, 0, 16, 12, 16),
            Block.box(15.9, 5, 4, 16, 6, 5),
            Block.box(15.9, 4, 3, 16, 6, 4),
            Block.box(15.9, 3, 0, 16, 6, 3),
            Block.box(15.9, 3, 13, 16, 6, 16),
            Block.box(15.9, 4, 12, 16, 6, 13),
            Block.box(15.9, 5, 11, 16, 6, 12),
            Block.box(0, 3, 0, 0.1, 6, 3),
            Block.box(0, 4, 3, 0.1, 6, 4),
            Block.box(0, 5, 4, 0.1, 6, 5),
            Block.box(0, 5, 11, 0.1, 6, 12),
            Block.box(0, 4, 12, 0.1, 6, 13),
            Block.box(0, 3, 13, 0.1, 6, 16),
            Block.box(0, 3, 15.9, 3, 6, 16),
            Block.box(3, 4, 15.9, 4, 6, 16),
            Block.box(4, 5, 15.9, 5, 6, 16),
            Block.box(13, 3, 15.9, 16, 6, 16),
            Block.box(12, 4, 15.9, 13, 6, 16),
            Block.box(11, 5, 15.9, 12, 6, 16),
            Block.box(13, 3, 0, 16, 6, 0.1),
            Block.box(12, 4, 0, 13, 6, 0.1),
            Block.box(11, 5, 0, 12, 6, 0.1),
            Block.box(4, 5, 0, 5, 6, 0.1),
            Block.box(0, 3, 0, 3, 6, 0.1),
            Block.box(3, 4, 0, 4, 6, 0.1),
            Block.box(0, 6, 0, 0.1, 12, 16),
            Block.box(15.9, 6, 0, 16, 12, 16),
            Block.box(0, 6, 0, 16, 12, 0.1),
            Block.box(0, 6, 15.9, 16, 12, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).orElse(Shapes.empty());
    private static final VoxelShape south = Shapes.join(tableShape, Stream.of(
            Block.box(7, 12, 1, 9, 15, 3),
            Block.box(6.5, 11.5, 2.5, 7.5, 12.5, 3.5),
            Block.box(13.5, 11.5, 2.5, 14.5, 12.5, 3.5),
            Block.box(11.5, 11.5, 3.5, 12.5, 12.5, 4.5),
            Block.box(12, 10.5, 2, 14, 13.5, 4),
            Block.box(2.5, 11.5, 3.5, 3.5, 12.5, 4.5),
            Block.box(2, 11.5, 2, 4, 14.5, 4)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).orElse(Shapes.empty()), BooleanOp.OR);
    private static final VoxelShape east = Shapes.join(tableShape, Stream.of(
            Block.box(1, 12, 7, 3, 15, 9),
            Block.box(2.5, 11.5, 8.5, 3.5, 12.5, 9.5),
            Block.box(2.5, 11.5, 1.5, 3.5, 12.5, 2.5),
            Block.box(3.5, 11.5, 3.5, 4.5, 12.5, 4.5),
            Block.box(2, 10.5, 2, 4, 13.5, 4),
            Block.box(3.5, 11.5, 12.5, 4.5, 12.5, 13.5),
            Block.box(2, 11.5, 12, 4, 14.5, 14)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).orElse(Shapes.empty()), BooleanOp.OR);
    private static final VoxelShape west = Shapes.join(tableShape, Stream.of(
            Block.box(13, 12, 7, 15, 15, 9),
            Block.box(12.5, 11.5, 6.5, 13.5, 12.5, 7.5),
            Block.box(12.5, 11.5, 13.5, 13.5, 12.5, 14.5),
            Block.box(11.5, 11.5, 11.5, 12.5, 12.5, 12.5),
            Block.box(12, 10.5, 12, 14, 13.5, 14),
            Block.box(11.5, 11.5, 2.5, 12.5, 12.5, 3.5),
            Block.box(12, 11.5, 2, 14, 14.5, 4)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).orElse(Shapes.empty()), BooleanOp.OR);
    private static final VoxelShape north = Shapes.join(tableShape, Stream.of(
            Block.box(7, 12, 13, 9, 15, 15),
            Block.box(8.5, 11.5, 12.5, 9.5, 12.5, 13.5),
            Block.box(1.5, 11.5, 12.5, 2.5, 12.5, 13.5),
            Block.box(3.5, 11.5, 11.5, 4.5, 12.5, 12.5),
            Block.box(2, 10.5, 12, 4, 13.5, 14),
            Block.box(12.5, 11.5, 11.5, 13.5, 12.5, 12.5),
            Block.box(12, 11.5, 12, 14, 14.5, 14)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).orElse(Shapes.empty()), BooleanOp.OR);

    public AltarBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case WEST -> west;
            case EAST -> east;
            case SOUTH -> south;
            default -> north;
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!state.is(oldState.getBlock())) {
            BlockEntity tileentity = world.getBlockEntity(pos);
            if (tileentity instanceof AltarBlockEntity altar) {
                ItemStack stack = altar.getCharm();
                if (!stack.isEmpty()) {
                    ItemEntity item = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
                    world.addFreshEntity(item);
                }
                NonNullList<ItemStack> list = altar.getCatalyst();
                for (ItemStack cat : list) {
                    if (!cat.isEmpty()) {
                        ItemEntity item = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), cat);
                        world.addFreshEntity(item);
                    }
                }
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, world, pos, oldState, isMoving);
        }
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
        for (int l = 0; l < 5; ++l) {
            double d0 = pos.getX() + random.nextFloat();
            double d1 = pos.getY() + random.nextFloat();
            double d2 = pos.getZ() + random.nextFloat();
            double d3 = (random.nextFloat() - 0.5D) * 1.000000001490116D;
            world.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d3, d3);
        }
        switch (state.getValue(FACING)) {
            case NORTH -> {
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 3 * pixel, pos.getY() + 16 * pixel, pos.getZ() + 12.5 * pixel, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 8 * pixel, pos.getY() + 17.5 * pixel, pos.getZ() + 13.5 * pixel, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 13 * pixel, pos.getY() + 17 * pixel, pos.getZ() + 12.5 * pixel, 0, 0, 0);
            }
            case SOUTH -> {
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 13 * pixel, pos.getY() + 16 * pixel, pos.getZ() + 2.5 * pixel, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 8 * pixel, pos.getY() + 17.5 * pixel, pos.getZ() + 1.5 * pixel, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 3 * pixel, pos.getY() + 17 * pixel, pos.getZ() + 2.5 * pixel, 0, 0, 0);
            }
            case EAST -> {
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 2.5 * pixel, pos.getY() + 16 * pixel, pos.getZ() + 3 * pixel, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 1.5 * pixel, pos.getY() + 17.5 * pixel, pos.getZ() + 8 * pixel, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 2.5 * pixel, pos.getY() + 17 * pixel, pos.getZ() + 13 * pixel, 0, 0, 0);
            }
            case WEST -> {
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 12.5 * pixel, pos.getY() + 16 * pixel, pos.getZ() + 13 * pixel, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 13.5 * pixel, pos.getY() + 17.5 * pixel, pos.getZ() + 8 * pixel, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 12.5 * pixel, pos.getY() + 17 * pixel, pos.getZ() + 3 * pixel, 0, 0, 0);
            }
            default -> {
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult res) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        ItemStack stack = player.getItemInHand(hand);
        if (!(blockEntity instanceof AltarBlockEntity altar))
            return InteractionResult.PASS;
        if (world.isClientSide) {
            if (player.isShiftKeyDown() || stack.getItem() == ModItems.chalk.get() || stack.getItem() == ModItems.crystalCluster.get() || stack.getItem() instanceof ItemServantCharm)
                return InteractionResult.SUCCESS;
            return InteractionResult.PASS;
        }
        if (!SummonUtils.checkStructure(world, pos, player.getDirection())) {
            altar.setComplete(false);
        }
        if (player.isShiftKeyDown()) {
            if (altar.removeItem(player))
                return InteractionResult.SUCCESS;
        } else if (stack.getItem() == ModItems.chalk.get() && !altar.isComplete()) {
            SummonUtils.placeSummoningStructure((ServerLevel) world, pos, altar, state.getValue(FACING).getOpposite());
            return InteractionResult.SUCCESS;
        } else if (!altar.addItem(player, stack) && stack.getItem() == ModItems.crystalCluster.get()) {
            return Platform.INSTANCE.getPlayerData(player).map(cap -> {
                GrailWarHandler tracker = GrailWarHandler.get(world.getServer());
                if (cap.getServant(player) == null) {
                    if (altar.isComplete()) {
                        if (!altar.isSummoning()) {
                            if (tracker.canJoin((ServerPlayer) player) && tracker.canSpawnMoreServants()) {
                                if (!player.isCreative())
                                    stack.shrink(1);
                                altar.setSummoning(player);
                                cap.setCommandSeals(player, 3);
                                AdvancementRegister.grailWarTrigger.trigger((ServerPlayer) player, true);
                                return InteractionResult.CONSUME;
                            } else {
                                player.sendMessage(new TranslatableComponent("chat.altar.fail").withStyle(ChatFormatting.DARK_RED), Util.NIL_UUID);
                            }
                        }
                    } else {
                        player.sendMessage(new TranslatableComponent("chat.altar.incomplete").withStyle(ChatFormatting.DARK_RED), Util.NIL_UUID);
                    }
                } else {
                    player.sendMessage(new TranslatableComponent("chat.altar.existing").withStyle(ChatFormatting.DARK_RED), Util.NIL_UUID);
                }
                return InteractionResult.FAIL;
            }).orElse(InteractionResult.FAIL);
        }
        return InteractionResult.FAIL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlocks.tileAltar.get(), AltarBlockEntity::ticker);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AltarBlockEntity(pos, state);
    }
}