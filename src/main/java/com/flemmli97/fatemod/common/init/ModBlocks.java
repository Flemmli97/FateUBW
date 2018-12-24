package com.flemmli97.fatemod.common.init;

import com.flemmli97.fatemod.common.blocks.BlockAltar;
import com.flemmli97.fatemod.common.blocks.BlockChalkLine;
import com.flemmli97.fatemod.common.blocks.BlockMagicOre;
import com.flemmli97.fatemod.common.blocks.BlockServantCharm;
import com.flemmli97.fatemod.common.blocks.tile.TileAltar;
import com.flemmli97.fatemod.common.blocks.tile.TileChalkLine;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = LibReference.MODID)
public final class ModBlocks {

	public static final Block grailWarKey = new BlockAltar();
	public static final Block magicOre = new BlockMagicOre();
	public static final Block servantCharmBlock = new BlockServantCharm();
	public static final Block chalkLine = new BlockChalkLine();
	
	@SubscribeEvent
	public static final void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(grailWarKey);
		event.getRegistry().register(magicOre);
		event.getRegistry().register(servantCharmBlock);
		event.getRegistry().register(chalkLine);
  		GameRegistry.registerTileEntity(TileAltar.class, new ResourceLocation(LibReference.MODID,"tile_grail"));
  		GameRegistry.registerTileEntity(TileChalkLine.class, new ResourceLocation(LibReference.MODID,"tile_chalk"));
    }
	
	@SubscribeEvent
    public static final void registerItemBlocks(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(fromBlock(grailWarKey));
		event.getRegistry().register(fromBlock(magicOre));
		event.getRegistry().register(fromBlock(servantCharmBlock));
		event.getRegistry().register(fromBlock(chalkLine));
	}
	
	private static final ItemBlock fromBlock(Block block)
	{
		ItemBlock item = new ItemBlock(block);
		item.setRegistryName(block.getRegistryName());
		return item;
	}
    
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static final void initModel(ModelRegistryEvent event)
    {
		registerDefaultModel(grailWarKey);
		registerDefaultModel(magicOre);
		registerDefaultModel(servantCharmBlock);
		registerDefaultModel(chalkLine);
    }
	
	@SideOnly(Side.CLIENT)
    public static void registerDefaultModel(Block block) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }
}
