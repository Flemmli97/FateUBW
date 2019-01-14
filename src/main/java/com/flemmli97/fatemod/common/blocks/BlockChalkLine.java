package com.flemmli97.fatemod.common.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import com.flemmli97.fatemod.common.blocks.tile.TileChalkLine;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockChalkLine extends Block implements ITileEntityProvider{

    public static final PropertyEnum<EnumPositionChalk> POSITIONMULTI = PropertyEnum.<EnumPositionChalk>create("position", EnumPositionChalk.class);
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

	protected static final AxisAlignedBB LayerAABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);

	public BlockChalkLine() {
		super(Material.CIRCUITS);
        this.setUnlocalizedName("chalk_line");
        this.blockSoundType = SoundType.STONE;
        this.setResistance(100.0F);
        this.setHardness(0.1F);
        this.setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.NORTH).withProperty(POSITIONMULTI, EnumPositionChalk.MIDDLE));
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "chalk_line"));
		this.setUnlocalizedName(this.getRegistryName().toString());
	}	
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(FACING, this.getTile(worldIn, pos).getFacing()).withProperty(POSITIONMULTI, this.getTile(worldIn, pos).getPosition());
	}
	
	private TileChalkLine getTile(IBlockAccess world, BlockPos pos)
	{
        TileEntity tile = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
		if(tile instanceof TileChalkLine)
			return (TileChalkLine) tile;
		return null;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
			EnumFacing side) {
		return false;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {POSITIONMULTI, FACING});
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState();
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return LayerAABB;
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block block, BlockPos fromPos) 
	{
		if (!this.canBlockStay(worldIn, pos))
        {
            worldIn.setBlockToAir(pos);
        }
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos);
	}

	private boolean canBlockStay(World worldIn, BlockPos pos) 
	{
		return !worldIn.isAirBlock(pos.down());
	}

	@Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return null;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileChalkLine();
	}
}
