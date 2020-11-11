package com.flemmli97.fate.common.registry;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.blocks.BlockAltar;
import com.flemmli97.fate.common.blocks.tile.TileAltar;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Fate.MODID);
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Fate.MODID);

    public static final RegistryObject<BlockAltar> altar = BLOCKS.register("altar", () -> new BlockAltar(AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(5, 30)));

    public static RegistryObject<TileEntityType<TileAltar>> tileAltar = TILES.register("altar_tile", ()->TileEntityType.Builder.create(TileAltar::new, altar.get()).build(null));

}
