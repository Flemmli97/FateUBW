package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.blocks.AltarBlock;
import io.github.flemmli97.fateubw.common.blocks.ChalkBlock;
import io.github.flemmli97.fateubw.common.blocks.OreBlock;
import io.github.flemmli97.fateubw.common.blocks.tile.AltarBlockEntity;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class ModBlocks {

    public static final PlatformRegistry<Block> BLOCKS = PlatformUtils.INSTANCE.of(Registry.BLOCK_REGISTRY, Fate.MODID);
    public static final PlatformRegistry<BlockEntityType<?>> TILES = PlatformUtils.INSTANCE.of(Registry.BLOCK_ENTITY_TYPE_REGISTRY, Fate.MODID);

    public static final RegistryEntrySupplier<AltarBlock> ALTAR = BLOCKS.register("summoning_altar", () -> new AltarBlock(BlockBehaviour.Properties.of(Material.STONE).strength(5, 30)));
    public static final RegistryEntrySupplier<OreBlock> GEM_ORE = BLOCKS.register("gem_ore", () -> new OreBlock(rand -> rand.nextInt(10) + 2, BlockBehaviour.Properties.of(Material.METAL).strength(3, 6).requiresCorrectToolForDrops()));
    public static final RegistryEntrySupplier<OreBlock> ARTIFACT_ORE = BLOCKS.register("artifact_ore", () -> new OreBlock(rand -> 5, BlockBehaviour.Properties.of(Material.METAL).strength(5, 7).requiresCorrectToolForDrops()));
    public static final RegistryEntrySupplier<OreBlock> DEEP_SLATE_GEM_ORE = BLOCKS.register("deepslate_gem_ore", () -> new OreBlock(rand -> rand.nextInt(10) + 2, BlockBehaviour.Properties.of(Material.METAL).strength(5, 7).requiresCorrectToolForDrops()));
    public static final RegistryEntrySupplier<OreBlock> DEEP_SLATE_ARTIFACT_ORE = BLOCKS.register("deepslate_artifact_ore", () -> new OreBlock(rand -> 5, BlockBehaviour.Properties.of(Material.METAL).strength(6, 8).requiresCorrectToolForDrops()));

    public static final RegistryEntrySupplier<ChalkBlock> CHALK = BLOCKS.register("chalk_line", () -> new ChalkBlock(BlockBehaviour.Properties.of(Material.DECORATION).sound(SoundType.WOOL).noOcclusion().strength(0.1f, 100)));

    public static final RegistryEntrySupplier<BlockEntityType<AltarBlockEntity>> TILE_ALTAR = TILES.register("altar_tile", () -> PlatformUtils.INSTANCE.blockEntityType(AltarBlockEntity::new, ModBlocks.ALTAR.get()));
}
