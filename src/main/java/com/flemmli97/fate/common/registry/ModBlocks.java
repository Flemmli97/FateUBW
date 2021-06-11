package com.flemmli97.fate.common.registry;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.blocks.BlockAltar;
import com.flemmli97.fate.common.blocks.BlockChalkLine;
import com.flemmli97.fate.common.blocks.BlockOre;
import com.flemmli97.fate.common.blocks.tile.TileAltar;
import com.flemmli97.fate.common.utils.EnumPositionChalk;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.EnumMap;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Fate.MODID);
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Fate.MODID);

    public static final RegistryObject<BlockAltar> altar = BLOCKS.register("altar", () -> new BlockAltar(AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(5, 30)));
    public static final RegistryObject<BlockOre> crystalOre = BLOCKS.register("crystal_ore", () -> new BlockOre(rand -> rand.nextInt(10) + 2, AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(3, 12).harvestLevel(2)));
    public static final RegistryObject<BlockOre> charmOre = BLOCKS.register("charm_ore", () -> new BlockOre(rand -> 5, AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(5, 15).harvestLevel(2)));
    public static final EnumMap<EnumPositionChalk, RegistryObject<BlockChalkLine>> chalks = registerChalk();

    public static final RegistryObject<TileEntityType<TileAltar>> tileAltar = TILES.register("altar_tile", () -> TileEntityType.Builder.create(TileAltar::new, altar.get()).build(null));

    public static EnumMap<EnumPositionChalk, RegistryObject<BlockChalkLine>> registerChalk() {
        EnumMap<EnumPositionChalk, RegistryObject<BlockChalkLine>> map = new EnumMap<>(EnumPositionChalk.class);
        for (EnumPositionChalk e : EnumPositionChalk.values())
            map.put(e, BLOCKS.register("chalk_line_" + e.getID(), () -> new BlockChalkLine(e, AbstractBlock.Properties.create(Material.MISCELLANEOUS).sound(SoundType.CLOTH).notSolid().hardnessAndResistance(0.1f, 100))));
        return map;
    }
}
