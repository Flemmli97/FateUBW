package com.flemmli97.fatemod.common.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.blocks.tile.TileAltar;
import com.flemmli97.fatemod.common.blocks.tile.TileChalkLine;
import com.flemmli97.fatemod.common.entity.servant.EntityArthur;
import com.flemmli97.fatemod.common.entity.servant.EntityCuchulainn;
import com.flemmli97.fatemod.common.entity.servant.EntityDiarmuid;
import com.flemmli97.fatemod.common.entity.servant.EntityEmiya;
import com.flemmli97.fatemod.common.entity.servant.EntityGilgamesh;
import com.flemmli97.fatemod.common.entity.servant.EntityGilles;
import com.flemmli97.fatemod.common.entity.servant.EntityHassan;
import com.flemmli97.fatemod.common.entity.servant.EntityHeracles;
import com.flemmli97.fatemod.common.entity.servant.EntityIskander;
import com.flemmli97.fatemod.common.entity.servant.EntityLancelot;
import com.flemmli97.fatemod.common.entity.servant.EntityMedea;
import com.flemmli97.fatemod.common.entity.servant.EntityMedusa;
import com.flemmli97.fatemod.common.entity.servant.EntitySasaki;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.entity.servant.EnumServantType;
import com.flemmli97.fatemod.common.handler.GrailWarPlayerTracker;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;
import com.flemmli97.fatemod.common.init.ModBlocks;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//somehow this class got real messy
public class BlockAltar extends BlockContainer {
		
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

	public BlockAltar() {
		super(Material.ROCK);
		this.setCreativeTab(Fate.customTab);
        this.setUnlocalizedName("grail_core");
        this.blockSoundType = SoundType.STONE;
        this.setResistance(30.0F);
        this.setHardness(5.0F);
        GameRegistry.register(this, new ResourceLocation(Fate.MODID, "grail_core"));
        GameRegistry.register(new ItemBlock(this), this.getRegistryName());
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);
	}

	private final double pixel = 0.0625;
	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random) 
	{
		World par1World = world;
        for (int l = 0; l < 5; ++l)
        {
            double d0 = (double)((float)pos.getX() + random.nextFloat());
            double d1 = (double)((float)pos.getY() + random.nextFloat());
            double d2 = (double)((float)pos.getZ() + random.nextFloat());
            double d3 = 0.0D;
            double d4 = 0.0D;
            double d5 = 0.0D;

            d3 = ((double)random.nextFloat() - 0.5D) * 1.000000001490116D;
            d4 = ((double)random.nextFloat() - 0.5D) * 1.000000001490116D;
            d5 = ((double)random.nextFloat() - 0.5D) * 1.000000001490116D;
            par1World.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }
        switch(state.getValue(FACING))
        {
			case NORTH:
		        par1World.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+3*pixel, pos.getY()+16*pixel, pos.getZ()+12.5*pixel, 0, 0, 0);
		        par1World.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+8*pixel, pos.getY()+17.5*pixel, pos.getZ()+13.5*pixel, 0, 0, 0);
		        par1World.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+13*pixel, pos.getY()+17*pixel, pos.getZ()+12.5*pixel, 0, 0, 0);
				break;
			case SOUTH:
		        par1World.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+13*pixel, pos.getY()+16*pixel, pos.getZ()+2.5*pixel, 0, 0, 0);
		        par1World.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+8*pixel, pos.getY()+17.5*pixel, pos.getZ()+1.5*pixel, 0, 0, 0);
		        par1World.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+3*pixel, pos.getY()+17*pixel, pos.getZ()+2.5*pixel, 0, 0, 0);
				break;
			case EAST:
				par1World.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+2.5*pixel, pos.getY()+16*pixel, pos.getZ()+3*pixel, 0, 0, 0);
		        par1World.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+1.5*pixel, pos.getY()+17.5*pixel, pos.getZ()+8*pixel, 0, 0, 0);
		        par1World.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+2.5*pixel, pos.getY()+17*pixel, pos.getZ()+13*pixel, 0, 0, 0);
				break;
			case WEST:
				par1World.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+12.5*pixel, pos.getY()+16*pixel, pos.getZ()+13*pixel, 0, 0, 0);
		        par1World.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+13.5*pixel, pos.getY()+17.5*pixel, pos.getZ()+8*pixel, 0, 0, 0);
		        par1World.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+12.5*pixel, pos.getY()+17*pixel, pos.getZ()+3*pixel, 0, 0, 0);
				break;
			default:
				break;
        }
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING});
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing)state.getValue(FACING)).getIndex();
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileAltar grail = (TileAltar) world.getTileEntity(pos);
		ItemStack stack = player.getHeldItemMainhand();
		IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
		GrailWarPlayerTracker tracker = GrailWarPlayerTracker.get(world);
		if(!this.testStructure(world, pos, player.getHorizontalFacing()))
		{
			grail.setComplete(false);
		}
		if (player.isSneaking())
		{
			grail.removeItem(player);
		}
		else
		{
			if(stack!=null)
			{
				if(stack.getItem() == ModItems.chalk && !grail.isComplete())
					this.placeStructure(world, pos, grail, state.getValue(FACING).getOpposite());
				else if (!grail.addItem(player, stack))
				{
					if(stack.getItem()==ModItems.crystalCluster)
					{
						if (!world.isRemote)
						{
							if(cap.getServant() == null)
							{
								if(grail.isComplete())
								{
									if(!grail.isSummoning())
										if(tracker.addPlayer(player))
										{
											if(!player.capabilities.isCreativeMode)
											{
												--stack.stackSize;
											}
											grail.setSummoning(player);
											cap.setCommandSeals(player, 3);
											//boolean multiyPlayer = ConfigHandler.multiPlayer;
											//if(multiyPlayer)
										}
										else
										{
											player.addChatMessage(new TextComponentString(TextFormatting.DARK_RED + "Summon failed either because you can't join the current war or joined already"));
										}
								}
								else
								{
									grail.setComplete(false);
									player.addChatMessage(new TextComponentString(TextFormatting.DARK_RED + "Incomplete Structure"));
								}
							}
							else
							{
								player.addChatMessage(new TextComponentString(TextFormatting.DARK_RED + "How dare you to summon another servant"));
		
							}
						}
					}
				}
			}
		}
		return true;
		
	}
	
	public static void summonRandomServant(ItemStack stack, EntityPlayer player, BlockPos pos, World world, IPlayer cap)
	{
		EnumServantType type = EnumServantType.values()[Block.RANDOM.nextInt(EnumServantType.values().length-1)];
		if(stack!=null && Block.RANDOM.nextFloat()<=0.6)
			type = EnumServantType.values()[stack.getMetadata()-1];

		int servant = Block.RANDOM.nextInt(2);
		switch(type)
		{
			case SABER:
				switch (servant) 
				{
					case 0:
						summonServant(new EntityArthur(world), player, world, cap, pos);
						break;
					case 1:
						summonServant(new EntityArthur(world), player, world, cap, pos);
						break;
					case 2:
						//spawn a saber class
						break;
					case 3:
					//spawn a saber class
						break;
				}
				break;
			case LANCER:
				switch (servant)
				{
					case 0:
						summonServant(new EntityCuchulainn(world), player, world, cap, pos);
						break;
					case 1:
						summonServant(new EntityDiarmuid(world), player, world, cap, pos);
						break;
					case 2:
						//spawn a lancer class
						break;
					case 3:
						//spawn a lancer class
						break;
				}
				break;
			case ARCHER:
				switch (servant)
				{
					case 0:
						summonServant(new EntityEmiya(world), player, world, cap, pos);
						break;
					case 1:
						summonServant(new EntityGilgamesh(world), player, world, cap, pos);
						break;
					case 2:
						//spawn an archer class
						break;
					case 3:
						//spawn an archer class
						break;
				}
				break;
			case RIDER:
				switch (servant)
				{
					case 0:		
						summonServant(new EntityMedusa(world), player, world, cap, pos);
						break;	
					case 1:
						summonServant(new EntityIskander(world), player, world, cap, pos);
						break;
					case 2:
						//spawn a rider class
						break;
					case 3:
						//spawn a rider class
						break;
				}
				break;
			case CASTER:
				switch (servant)
				{
					case 0:	
						summonServant(new EntityMedea(world), player, world, cap, pos);

						break;	
					case 1:
						summonServant(new EntityGilles(world), player, world, cap, pos);
						break;
					case 2:
						//spawn a caster class
						break;
					case 3:
						//spawn a caster class
						break;
				}
				break;
			case ASSASSIN:
				switch (servant)
				{
					case 0:			
						summonServant(new EntitySasaki(world), player, world, cap, pos);
						break;	
					case 1:
						summonServant(new EntityHassan(world), player, world, cap, pos);
						break;
					case 2:
						//spawn an assassin class
						break;
					case 3:
						//spawn an assassin class
						break;
				}
				break;
			case BERSERKER:
				switch (servant)
				{
					case 0:				
						summonServant(new EntityHeracles(world), player, world, cap, pos);
						break;	
					case 1:
						summonServant(new EntityLancelot(world), player, world, cap, pos);
						break;
					case 2:
						//spawn a berseker class
						break;
					case 3:
						//spawn a berseker class
						break;
				}
				break;
			case NOTASSIGNED:
				break;	
		}	
	}
	
	public static void summonServant(EntityServant servant, EntityPlayer player, World world, IPlayer cap, BlockPos pos)
	{
		servant.setOwner(player);
		servant.setLocationAndAngles(pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
		servant.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(servant)), (IEntityLivingData)null);
		world.spawnEntityInWorld(servant);
		cap.setServant(servant);
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
	    TileAltar te = (TileAltar) world.getTileEntity(pos);
	    if(te!=null)
	    {
		    ItemStack stack = te.getCharm();
		    if(stack!=null)
		    {
			    EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
			    world.spawnEntityInWorld(item);
		    }
		    ItemStack[] cat = te.getCatalyst();
		    for(int x=0;x<cat.length;x++)
		    {
		    		if(cat[x]!=null)
		    		{
		    			EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), cat[x]);
		    			world.spawnEntityInWorld(item);
		    		}
		    }
	    }
	    super.breakBlock(world, pos, blockstate);
	}
	
	private boolean testStructure(World world, BlockPos pos, EnumFacing facing)
	{
		int rightBlock = 0;
		for(int x=-2;x<=2;x++)
			for(int z=-2; z<=2;z++)
				for(int y=0;y<2;y++)
				{
					if(x!=0 || z!=0 || y !=0)
					{
						BlockPos newPos = new BlockPos(pos.getX()+x, pos.getY()+y, pos.getZ()+z);
						Block state = world.getBlockState(newPos).getBlock();
						if(y==0)
						{
							IBlockState chalkState = world.getBlockState(newPos).getActualState(world, newPos);
							if(chalkState.getBlock() == ModBlocks.chalkLine && chalkState.getValue(BlockChalkLine.POSITIONMULTI) == this.getChalkPos(x, z, chalkState.getValue(BlockChalkLine.FACING)))
								rightBlock++;
						}
						else if(y==1 && state == Blocks.AIR)
						{
							rightBlock++;
						}
						if(rightBlock==49)
						{				
							return true;
						}
					}
				}
		return false;
	}
	
	private void placeStructure(World world, BlockPos pos, TileAltar tile, EnumFacing facing)
	{
		int testBlocks = 0;
		for(int x=-2;x<=2;x++)
			for(int z=-2;z<=2;z++)
			{
				if(x!=0 || z!=0)
				{
					Block blockTest = world.getBlockState(pos.add(x, 0, z)).getBlock();
					if(blockTest == ModBlocks.chalkLine)
					{
						testBlocks++;
					}
				}
			}
		if(testBlocks==24)
		{
			for(int x=-2;x<=2;x++)
				for(int z=-2;z<=2;z++)
				{
					if(x!=0 || z!=0)
					{						
						BlockPos newPos = new BlockPos(pos.getX()+x, pos.getY(), pos.getZ()+z);
						TileChalkLine tileChalk = this.getTileEntity(world, newPos);
						tileChalk.setPropertiesFromState(facing, this.getChalkPos(x, z, facing));
						world.notifyBlockUpdate(newPos, world.getBlockState(newPos), world.getBlockState(newPos), 3);
						world.spawnParticle(EnumParticleTypes.CLOUD, newPos.getX()+0.5, newPos.getY(), newPos.getZ()+0.5, 0, 0.2, 0);
					}
				}
			tile.setComplete(true);
		}
	}
	
	private EnumPositionChalk getChalkPos(int x, int z, EnumFacing facing)
	{
		//default to north
		int column = x+2;
		int row = z+2;
		if(facing == EnumFacing.NORTH)
		{
			column = x+2;
			row = z+2;
		}
		else if(facing == EnumFacing.WEST)
		{
			column = -z+2;
			row = x+2;
		}
		else if(facing == EnumFacing.SOUTH)
		{
			column = -x+2;
			row = -z+2;
		}
		else if(facing == EnumFacing.EAST)
		{
			column = z+2;
			row = -x+2;
		}
		return EnumPositionChalk.fromMeta(column + row*5);
	}
	
	@Nullable
    private TileChalkLine getTileEntity(World worldIn, BlockPos pos)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity instanceof TileChalkLine ? (TileChalkLine)tileentity : null;
    }
	
	public static void removeStructure(World world, BlockPos pos)
	{
		for(int x=-3;x<=3;x++)
			for(int z=-3;z<=3;z++)
			{
				if(x!=0 || z!=0)
				{
					BlockPos posNew = pos.add(x, 0, z);
					world.setBlockToAir(posNew);
				}
			}
		world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT, 0.4F, 1F);
		world.removeTileEntity(pos);
		world.setBlockToAir(pos);
	}

	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileAltar();
	}
}


