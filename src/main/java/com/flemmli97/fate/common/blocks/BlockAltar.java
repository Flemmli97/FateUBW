package com.flemmli97.fate.common.blocks;

import com.flemmli97.fate.common.blocks.tile.TileAltar;
import com.flemmli97.fate.common.capability.IPlayer;
import com.flemmli97.fate.common.capability.PlayerCapProvider;
import com.flemmli97.fate.common.world.GrailWarHandler;
import com.flemmli97.fate.common.items.ItemServantCharm;
import com.flemmli97.fate.common.registry.AdvancementRegister;
import com.flemmli97.fate.common.registry.ModItems;
import com.flemmli97.fate.common.utils.SummonUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

public class BlockAltar extends ContainerBlock {

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    private static final double pixel = 0.0625;

    private static final VoxelShape tableShape = Stream.of(
            Block.makeCuboidShape(13.5, 0, 13.5, 15.5, 11, 15.5),
            Block.makeCuboidShape(0.5, 0, 13.5, 2.5, 11, 15.5),
            Block.makeCuboidShape(13.5, 0, 0.5, 15.5, 11, 2.5),
            Block.makeCuboidShape(0.5, 0, 0.5, 2.5, 11, 2.5),
            Block.makeCuboidShape(0, 3, 0, 16, 12, 16)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).orElse(VoxelShapes.empty());
    private static final VoxelShape south = VoxelShapes.combineAndSimplify(tableShape, Stream.of(
            Block.makeCuboidShape(7, 12, 1, 9, 15, 3),
            Block.makeCuboidShape(6.5, 11.5, 2.5, 7.5, 12.5, 3.5),
            Block.makeCuboidShape(13.5, 11.5, 2.5, 14.5, 12.5, 3.5),
            Block.makeCuboidShape(11.5, 11.5, 3.5, 12.5, 12.5, 4.5),
            Block.makeCuboidShape(12, 10.5, 2, 14, 13.5, 4),
            Block.makeCuboidShape(2.5, 11.5, 3.5, 3.5, 12.5, 4.5),
            Block.makeCuboidShape(2, 11.5, 2, 4, 14.5, 4)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).orElse(VoxelShapes.empty()), IBooleanFunction.OR);
    private static final VoxelShape east = VoxelShapes.combineAndSimplify(tableShape, Stream.of(
            Block.makeCuboidShape(1, 12, 7, 3, 15, 9),
            Block.makeCuboidShape(2.5, 11.5, 8.5, 3.5, 12.5, 9.5),
            Block.makeCuboidShape(2.5, 11.5, 1.5, 3.5, 12.5, 2.5),
            Block.makeCuboidShape(3.5, 11.5, 3.5, 4.5, 12.5, 4.5),
            Block.makeCuboidShape(2, 10.5, 2, 4, 13.5, 4),
            Block.makeCuboidShape(3.5, 11.5, 12.5, 4.5, 12.5, 13.5),
            Block.makeCuboidShape(2, 11.5, 12, 4, 14.5, 14)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).orElse(VoxelShapes.empty()), IBooleanFunction.OR);
    private static final VoxelShape west = VoxelShapes.combineAndSimplify(tableShape, Stream.of(
            Block.makeCuboidShape(13, 12, 7, 15, 15, 9),
            Block.makeCuboidShape(12.5, 11.5, 6.5, 13.5, 12.5, 7.5),
            Block.makeCuboidShape(12.5, 11.5, 13.5, 13.5, 12.5, 14.5),
            Block.makeCuboidShape(11.5, 11.5, 11.5, 12.5, 12.5, 12.5),
            Block.makeCuboidShape(12, 10.5, 12, 14, 13.5, 14),
            Block.makeCuboidShape(11.5, 11.5, 2.5, 12.5, 12.5, 3.5),
            Block.makeCuboidShape(12, 11.5, 2, 14, 14.5, 4)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).orElse(VoxelShapes.empty()), IBooleanFunction.OR);
    private static final VoxelShape north = VoxelShapes.combineAndSimplify(tableShape, Stream.of(
            Block.makeCuboidShape(7, 12, 13, 9, 15, 15),
            Block.makeCuboidShape(8.5, 11.5, 12.5, 9.5, 12.5, 13.5),
            Block.makeCuboidShape(1.5, 11.5, 12.5, 2.5, 12.5, 13.5),
            Block.makeCuboidShape(3.5, 11.5, 11.5, 4.5, 12.5, 12.5),
            Block.makeCuboidShape(2, 10.5, 12, 4, 13.5, 14),
            Block.makeCuboidShape(12.5, 11.5, 11.5, 13.5, 12.5, 12.5),
            Block.makeCuboidShape(12, 11.5, 12, 14, 14.5, 14)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).orElse(VoxelShapes.empty()), IBooleanFunction.OR);

    public BlockAltar(Properties props) {
        super(props);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
    }

    @Override
    public BlockRenderType getRenderType(BlockState p_149645_1_) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        switch (state.get(FACING)) {
            case WEST:
                return west;
            case EAST:
                return east;
            case SOUTH:
                return south;
            default:
                return north;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState oldState, boolean p_196243_5_) {
        if (!state.isIn(oldState.getBlock())) {
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity instanceof TileAltar) {
                TileAltar te = (TileAltar) tileentity;
                ItemStack stack = te.getCharm();
                if (!stack.isEmpty()) {
                    ItemEntity item = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
                    world.addEntity(item);
                }
                NonNullList<ItemStack> list = te.getCatalyst();
                for (ItemStack cat : list) {
                    if (!cat.isEmpty()) {
                        ItemEntity item = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), cat);
                        world.addEntity(item);
                    }
                }
                world.updateComparatorOutputLevel(pos, this);
            }
            super.onReplaced(state, world, pos, oldState, p_196243_5_);
        }
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
        for (int l = 0; l < 5; ++l) {
            double d0 = pos.getX() + random.nextFloat();
            double d1 = pos.getY() + random.nextFloat();
            double d2 = pos.getZ() + random.nextFloat();
            double d3 = (random.nextFloat() - 0.5D) * 1.000000001490116D;
            world.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d3, d3);
        }
        switch (state.get(FACING)) {
            case NORTH:
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 3 * pixel, pos.getY() + 16 * pixel, pos.getZ() + 12.5 * pixel, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 8 * pixel, pos.getY() + 17.5 * pixel, pos.getZ() + 13.5 * pixel, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 13 * pixel, pos.getY() + 17 * pixel, pos.getZ() + 12.5 * pixel, 0, 0, 0);
                break;
            case SOUTH:
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 13 * pixel, pos.getY() + 16 * pixel, pos.getZ() + 2.5 * pixel, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 8 * pixel, pos.getY() + 17.5 * pixel, pos.getZ() + 1.5 * pixel, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 3 * pixel, pos.getY() + 17 * pixel, pos.getZ() + 2.5 * pixel, 0, 0, 0);
                break;
            case EAST:
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 2.5 * pixel, pos.getY() + 16 * pixel, pos.getZ() + 3 * pixel, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 1.5 * pixel, pos.getY() + 17.5 * pixel, pos.getZ() + 8 * pixel, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 2.5 * pixel, pos.getY() + 17 * pixel, pos.getZ() + 13 * pixel, 0, 0, 0);
                break;
            case WEST:
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 12.5 * pixel, pos.getY() + 16 * pixel, pos.getZ() + 13 * pixel, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 13.5 * pixel, pos.getY() + 17.5 * pixel, pos.getZ() + 8 * pixel, 0, 0, 0);
                world.addParticle(ParticleTypes.FLAME, pos.getX() + 12.5 * pixel, pos.getY() + 17 * pixel, pos.getZ() + 3 * pixel, 0, 0, 0);
                break;
            default:
                break;
        }
    }

    @Override
    public ActionResultType onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult res) {
        TileAltar altar = (TileAltar) world.getTileEntity(pos);
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote) {
            if (player.isSneaking() || stack.getItem() == ModItems.chalk.get() || stack.getItem() == ModItems.crystalCluster.get() || stack.getItem() instanceof ItemServantCharm)
                return ActionResultType.SUCCESS;
            return ActionResultType.PASS;
        }
        if (!SummonUtils.checkStructure(world, pos, player.getHorizontalFacing())) {
            altar.setComplete(false);
        }
        if (player.isSneaking()) {
            if (altar.removeItem(player))
                return ActionResultType.SUCCESS;
        } else if (stack.getItem() == ModItems.chalk.get() && !altar.isComplete()) {
            SummonUtils.placeSummoningStructure((ServerWorld) world, pos, altar, state.get(FACING).getOpposite());
            return ActionResultType.SUCCESS;
        } else if (!altar.addItem(player, stack) && stack.getItem() == ModItems.crystalCluster.get()) {
            Optional<IPlayer> opt = player.getCapability(PlayerCapProvider.PlayerCap).resolve();
            if (!opt.isPresent())
                return ActionResultType.FAIL;
            GrailWarHandler tracker = GrailWarHandler.get((ServerWorld) world);
            IPlayer cap = opt.get();
            if (cap.getServant(player) == null) {
                if (altar.isComplete()) {
                    if (!altar.isSummoning()) {
                        if (tracker.canJoin((ServerPlayerEntity) player) && tracker.canSpawnMoreServants()) {
                            if (!player.isCreative())
                                stack.shrink(1);
                            altar.setSummoning(player);
                            cap.setCommandSeals(player, 3);
                            AdvancementRegister.grailWarTrigger.trigger((ServerPlayerEntity) player, true);
                        } else {
                            player.sendMessage(new TranslationTextComponent("chat.altar.fail").formatted(TextFormatting.DARK_RED), Util.NIL_UUID);
                        }
                    }
                } else {
                    player.sendMessage(new TranslationTextComponent("chat.altar.incomplete").formatted(TextFormatting.DARK_RED), Util.NIL_UUID);
                }
            } else {
                player.sendMessage(new TranslationTextComponent("chat.altar.existing").formatted(TextFormatting.DARK_RED), Util.NIL_UUID);
            }
        }
        return ActionResultType.FAIL;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new TileAltar();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
}