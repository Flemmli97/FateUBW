package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.blocks.AltarBlock;
import io.github.flemmli97.fateubw.common.blocks.ChalkBlock;
import io.github.flemmli97.fateubw.common.blocks.entity.AltarBlockEntity;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class FateBlocks {

    public static final LoaderRegister<Block> BLOCKS = LoaderRegistryAccess.INSTANCE.of(Registries.BLOCK, Fate.MODID);
    public static final LoaderRegister<BlockEntityType<?>> BLOCK_ENTITIES = LoaderRegistryAccess.INSTANCE.of(Registries.BLOCK_ENTITY_TYPE, Fate.MODID);

    public static final RegistryEntrySupplier<Block, AltarBlock> ALTAR = BLOCKS.register("summoning_altar", () -> new AltarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).lightLevel(s -> 3).requiresCorrectToolForDrops().strength(5, 30)));
    public static final RegistryEntrySupplier<Block, DropExperienceBlock> GEM_ORE = BLOCKS.register("gem_ore", () -> new DropExperienceBlock(UniformInt.of(10, 15), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3, 6)));
    public static final RegistryEntrySupplier<Block, DropExperienceBlock> ARTIFACT_ORE = BLOCKS.register("artifact_ore", () -> new DropExperienceBlock(UniformInt.of(20, 30), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5, 7)));
    public static final RegistryEntrySupplier<Block, DropExperienceBlock> DEEP_SLATE_GEM_ORE = BLOCKS.register("deepslate_gem_ore", () -> new DropExperienceBlock(UniformInt.of(10, 15), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5, 7)));
    public static final RegistryEntrySupplier<Block, DropExperienceBlock> DEEP_SLATE_ARTIFACT_ORE = BLOCKS.register("deepslate_artifact_ore", () -> new DropExperienceBlock(UniformInt.of(20, 30), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(6, 8)));
    public static final RegistryEntrySupplier<Block, ChalkBlock> CHALK = BLOCKS.register("chalk_line", () -> new ChalkBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOL).sound(SoundType.WOOL).noOcclusion().strength(0.1f, 10)));

    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<AltarBlockEntity>> ALTAR_BLOCK_ENTITY = BLOCK_ENTITIES.register("altar_block_entity", () -> BlockEntityType.Builder.<AltarBlockEntity>of(AltarBlockEntity::new, FateBlocks.ALTAR.get()).build(null));
}
