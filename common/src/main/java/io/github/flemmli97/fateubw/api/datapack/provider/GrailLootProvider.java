package io.github.flemmli97.fateubw.api.datapack.provider;

import io.github.flemmli97.fateubw.api.datapack.GrailLootBuilder;
import io.github.flemmli97.fateubw.common.datapack.GrailLootManager;
import io.github.flemmli97.fateubw.common.loot.GrailLootTable;
import io.github.flemmli97.tenshilib.common.data.provider.CodecBasedProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public abstract class GrailLootProvider extends CodecBasedProvider<GrailLootTable> {

    public GrailLootProvider(PackOutput output, String modid, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, PackOutput.Target.DATA_PACK, modid, GrailLootManager.DIRECTORY, GrailLootTable.CODEC, provider);
    }

    public void addLootTable(ResourceLocation res, GrailLootBuilder builder) {
        this.contents.put(res, builder.build());
    }
}