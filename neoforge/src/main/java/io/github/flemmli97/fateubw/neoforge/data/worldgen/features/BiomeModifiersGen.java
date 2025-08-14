package io.github.flemmli97.fateubw.neoforge.data.worldgen.features;

import io.github.flemmli97.tenshilib.common.data.provider.CodecBasedProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.concurrent.CompletableFuture;

public class BiomeModifiersGen extends CodecBasedProvider<BiomeModifier> {

    public BiomeModifiersGen(PackOutput output, String modid, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, PackOutput.Target.DATA_PACK, "Biome Modifiers", modid,
                directory(),
                BiomeModifier.DIRECT_CODEC, provider);
    }

    private static String directory() {
        ResourceLocation id = NeoForgeRegistries.Keys.BIOME_MODIFIERS.location();
        return String.format("%s/%s", id.getNamespace(), id.getPath());
    }

    @Override
    protected void add(HolderLookup.Provider provider) {
    }

    public void add(ResourceLocation id, BiomeModifier modifier) {
        this.contents.put(id, modifier);
    }
}
