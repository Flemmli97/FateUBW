package com.flemmli97.fatemod.common.init;

import com.flemmli97.fatemod.common.blocks.BlockAltar;
import com.flemmli97.fatemod.common.blocks.BlockChalkLine;
import com.flemmli97.fatemod.common.blocks.BlockMagicOre;
import com.flemmli97.fatemod.common.blocks.BlockServantCharm;
import com.flemmli97.fatemod.common.blocks.tile.TileAltar;
import com.flemmli97.fatemod.common.blocks.tile.TileChalkLine;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ModBlocks {

	public static Block grailWarKey;
	public static Block magicOre;
	public static Block servantCharmBlock;
	public static Block chalkLine;
	
    public static final void init() {
    		grailWarKey = new BlockAltar();
  		magicOre = new BlockMagicOre();
  		servantCharmBlock = new BlockServantCharm();
  		chalkLine = new BlockChalkLine();
  		GameRegistry.registerTileEntity(TileAltar.class, "tile_grail");
  		GameRegistry.registerTileEntity(TileChalkLine.class, "tile_chalk");
    }
    
   @ SideOnly(Side.CLIENT)
    public static final void initModels()
    {
	   ((BlockAltar) grailWarKey).initModel();
	   ((BlockMagicOre) magicOre).initModel();
    		((BlockServantCharm) servantCharmBlock).initModel();
    		((BlockChalkLine) chalkLine).initModel();
    }
}
