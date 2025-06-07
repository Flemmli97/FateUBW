package io.github.flemmli97.fateubw.common.blocks;

import io.github.flemmli97.fateubw.common.blocks.tile.AltarBlockEntity;
import io.github.flemmli97.fateubw.common.items.ItemServantCharm;
import io.github.flemmli97.fateubw.common.registry.AdvancementRegister;
import io.github.flemmli97.fateubw.common.registry.ModBlocks;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import io.github.flemmli97.fateubw.platform.Platform;
import io.github.flemmli97.tenshilib.common.utils.VoxelUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class AltarBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final double PIXEL = 0.0625;

    private static final VoxelShape[] SHAPES = VoxelUtils.joinedOrDirs(
            VoxelUtils.ShapeBuilder.of(13.5, 0, 0.5, 15.5, 11, 2.5),
            VoxelUtils.ShapeBuilder.of(0.5, 0, 0.5, 2.5, 11, 2.5),
            VoxelUtils.ShapeBuilder.of(0.5, 9, 0.5, 15.5, 12, 15.5),
            VoxelUtils.ShapeBuilder.of(13.5, 0, 13.5, 15.5, 11, 15.5),
            VoxelUtils.ShapeBuilder.of(0.5, 0, 13.5, 2.5, 11, 15.5),
            VoxelUtils.ShapeBuilder.of(0, 11.9, 0, 16, 12, 16),
            VoxelUtils.ShapeBuilder.of(15.9, 5, 4, 16, 6, 5),
            VoxelUtils.ShapeBuilder.of(15.9, 4, 3, 16, 6, 4),
            VoxelUtils.ShapeBuilder.of(15.9, 3, 0, 16, 6, 3),
            VoxelUtils.ShapeBuilder.of(15.9, 3, 13, 16, 6, 16),
            VoxelUtils.ShapeBuilder.of(15.9, 4, 12, 16, 6, 13),
            VoxelUtils.ShapeBuilder.of(15.9, 5, 11, 16, 6, 12),
            VoxelUtils.ShapeBuilder.of(0, 3, 0, 0.1, 6, 3),
            VoxelUtils.ShapeBuilder.of(0, 4, 3, 0.1, 6, 4),
            VoxelUtils.ShapeBuilder.of(0, 5, 4, 0.1, 6, 5),
            VoxelUtils.ShapeBuilder.of(0, 5, 11, 0.1, 6, 12),
            VoxelUtils.ShapeBuilder.of(0, 4, 12, 0.1, 6, 13),
            VoxelUtils.ShapeBuilder.of(0, 3, 13, 0.1, 6, 16),
            VoxelUtils.ShapeBuilder.of(0, 3, 15.9, 3, 6, 16),
            VoxelUtils.ShapeBuilder.of(3, 4, 15.9, 4, 6, 16),
            VoxelUtils.ShapeBuilder.of(4, 5, 15.9, 5, 6, 16),
            VoxelUtils.ShapeBuilder.of(13, 3, 15.9, 16, 6, 16),
            VoxelUtils.ShapeBuilder.of(12, 4, 15.9, 13, 6, 16),
            VoxelUtils.ShapeBuilder.of(11, 5, 15.9, 12, 6, 16),
            VoxelUtils.ShapeBuilder.of(13, 3, 0, 16, 6, 0.1),
            VoxelUtils.ShapeBuilder.of(12, 4, 0, 13, 6, 0.1),
            VoxelUtils.ShapeBuilder.of(11, 5, 0, 12, 6, 0.1),
            VoxelUtils.ShapeBuilder.of(4, 5, 0, 5, 6, 0.1),
            VoxelUtils.ShapeBuilder.of(0, 3, 0, 3, 6, 0.1),
            VoxelUtils.ShapeBuilder.of(3, 4, 0, 4, 6, 0.1),
            VoxelUtils.ShapeBuilder.of(0, 6, 0, 0.1, 12, 16),
            VoxelUtils.ShapeBuilder.of(15.9, 6, 0, 16, 12, 16),
            VoxelUtils.ShapeBuilder.of(0, 6, 0, 16, 12, 0.1),
            VoxelUtils.ShapeBuilder.of(0, 6, 15.9, 16, 12, 16),
            VoxelUtils.ShapeBuilder.of(7, 12, 13, 9, 15, 15),
            VoxelUtils.ShapeBuilder.of(8.5, 11.5, 12.5, 9.5, 12.5, 13.5),
            VoxelUtils.ShapeBuilder.of(1.5, 11.5, 12.5, 2.5, 12.5, 13.5),
            VoxelUtils.ShapeBuilder.of(3.5, 11.5, 11.5, 4.5, 12.5, 12.5),
            VoxelUtils.ShapeBuilder.of(2, 10.5, 12, 4, 13.5, 14),
            VoxelUtils.ShapeBuilder.of(12.5, 11.5, 11.5, 13.5, 12.5, 12.5),
            VoxelUtils.ShapeBuilder.of(12, 11.5, 12, 14, 14.5, 14)
    );

    public AltarBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public static void placeSummoningStructure(ServerLevel world, BlockPos pos, AltarBlockEntity altar, Direction facing) {
        for (int x = -2; x <= 2; x++)
            for (int z = -2; z <= 2; z++) {
                if (x != 0 || z != 0) {
                    BlockPos posNew = pos.offset(x, 0, z);
                    if (!(world.getBlockState(posNew).getBlock() instanceof ChalkBlock))
                        return;
                }
            }
        for (int x = -2; x <= 2; x++)
            for (int z = -2; z <= 2; z++) {
                if (x != 0 || z != 0) {
                    BlockPos newPos = new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
                    world.removeBlock(newPos, false);
                    world.sendParticles(ParticleTypes.CLOUD, newPos.getX() + 0.5, newPos.getY(), newPos.getZ() + 0.5, 1, 0, 0.2, 0, 0);
                }
            }
        altar.setComplete(true);
    }

    public static void removeSummoningStructure(Level world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.GENERIC_EXPLODE, SoundSource.AMBIENT, 0.4F, 1F);
        world.removeBlockEntity(pos);
        world.destroyBlock(pos, false);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(FACING).get2DDataValue()];
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!state.is(oldState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof AltarBlockEntity altar) {
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
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof AltarBlockEntity altar) || !altar.isComplete())
            return;
        switch (state.getValue(FACING)) {
            case NORTH -> {
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 3 * PIXEL, pos.getY() + 16 * PIXEL, pos.getZ() + 12.5 * PIXEL, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 8 * PIXEL, pos.getY() + 17.5 * PIXEL, pos.getZ() + 13.5 * PIXEL, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 13 * PIXEL, pos.getY() + 17 * PIXEL, pos.getZ() + 12.5 * PIXEL, 0, 0, 0);
            }
            case SOUTH -> {
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 13 * PIXEL, pos.getY() + 16 * PIXEL, pos.getZ() + 2.5 * PIXEL, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 8 * PIXEL, pos.getY() + 17.5 * PIXEL, pos.getZ() + 1.5 * PIXEL, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 3 * PIXEL, pos.getY() + 17 * PIXEL, pos.getZ() + 2.5 * PIXEL, 0, 0, 0);
            }
            case EAST -> {
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 2.5 * PIXEL, pos.getY() + 16 * PIXEL, pos.getZ() + 3 * PIXEL, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 1.5 * PIXEL, pos.getY() + 17.5 * PIXEL, pos.getZ() + 8 * PIXEL, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 2.5 * PIXEL, pos.getY() + 17 * PIXEL, pos.getZ() + 13 * PIXEL, 0, 0, 0);
            }
            case WEST -> {
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 12.5 * PIXEL, pos.getY() + 16 * PIXEL, pos.getZ() + 13 * PIXEL, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 13.5 * PIXEL, pos.getY() + 17.5 * PIXEL, pos.getZ() + 8 * PIXEL, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 12.5 * PIXEL, pos.getY() + 17 * PIXEL, pos.getZ() + 3 * PIXEL, 0, 0, 0);
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
        if (!(player instanceof ServerPlayer serverPlayer)) {
            if (player.isShiftKeyDown() || stack.getItem() == ModItems.CHALK.get() || stack.getItem() == ModItems.CRYSTAL_CLUSTER.get() || stack.getItem() instanceof ItemServantCharm)
                return InteractionResult.SUCCESS;
            return InteractionResult.PASS;
        }
        if (player.isShiftKeyDown()) {
            if (altar.removeItem(player))
                return InteractionResult.SUCCESS;
        } else if (stack.getItem() == ModItems.CHALK.get() && !altar.isComplete()) {
            placeSummoningStructure((ServerLevel) world, pos, altar, state.getValue(FACING).getOpposite());
            return InteractionResult.SUCCESS;
        } else if (!altar.addItem(player, stack) && stack.getItem() == ModItems.CRYSTAL_CLUSTER.get()) {
            return Platform.INSTANCE.getPlayerData(player).map(data -> {
                GrailWarHandler tracker = GrailWarHandler.get(serverPlayer.getServer());
                if (tracker.getServant(serverPlayer) == null) {
                    if (!altar.isComplete()) {
                        player.sendMessage(new TranslatableComponent("fateubw.chat.altar.incomplete").withStyle(ChatFormatting.DARK_RED), Util.NIL_UUID);
                        return InteractionResult.FAIL;
                    }
                    if (!altar.isSummoning()) {
                        GrailWarHandler.JoinResult joinResult = tracker.checkJoining(player);
                        if (joinResult != GrailWarHandler.JoinResult.SUCCESS) {
                            player.sendMessage(new TranslatableComponent(joinResult.translationKey).withStyle(ChatFormatting.DARK_RED), Util.NIL_UUID);
                            return InteractionResult.FAIL;
                        }
                        if (!player.isCreative())
                            stack.shrink(1);
                        altar.setSummoning(player);
                        AdvancementRegister.GRAIL_WAR_TRIGGER.trigger(serverPlayer, true);
                        return InteractionResult.CONSUME;
                    }
                } else {
                    player.sendMessage(new TranslatableComponent("fateubw.chat.altar.servant.existing").withStyle(ChatFormatting.DARK_RED), Util.NIL_UUID);
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
        return createTickerHelper(blockEntityType, ModBlocks.TILE_ALTAR.get(), AltarBlockEntity::ticker);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AltarBlockEntity(pos, state);
    }
}