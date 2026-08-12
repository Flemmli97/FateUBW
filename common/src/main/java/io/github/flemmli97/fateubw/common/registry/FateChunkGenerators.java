package io.github.flemmli97.fateubw.common.registry;

import com.mojang.serialization.MapCodec;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.world.chunk.UnlimitedBladeworksChunkGenerator;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;

public class FateChunkGenerators {


    public static final LoaderRegister<MapCodec<? extends ChunkGenerator>> GENERATORS = LoaderRegistryAccess.INSTANCE.of(Registries.CHUNK_GENERATOR, Fate.MODID);

    public static final RegistryEntrySupplier<MapCodec<? extends ChunkGenerator>, MapCodec<UnlimitedBladeworksChunkGenerator>> UBW_CHUNK_GENERATOR = GENERATORS.register("ubw_chunk_generator", () -> UnlimitedBladeworksChunkGenerator.CODEC);
}
