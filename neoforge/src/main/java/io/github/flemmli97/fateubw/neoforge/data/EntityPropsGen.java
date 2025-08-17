package io.github.flemmli97.fateubw.neoforge.data;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.fateubw.api.datapack.AttributeHolderProperties;
import io.github.flemmli97.fateubw.api.datapack.ServantProperties;
import io.github.flemmli97.fateubw.common.datapack.EntityPropsManager;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public record EntityPropsGen(PackOutput output,
                             CompletableFuture<HolderLookup.Provider> provider) implements DataProvider {

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return this.provider.thenCompose(provider -> {
            DynamicOps<JsonElement> ops = provider.createSerializationContext(JsonOps.INSTANCE);
            ImmutableList.Builder<CompletableFuture<?>> futures = new ImmutableList.Builder<>();
            FateEntities.getServantProperties().forEach((res, prop) -> {
                Path path = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(res.getNamespace() + "/" + EntityPropsManager.DIRECTORY + "/" + res.getPath() + ".json");
                JsonElement obj = ServantProperties.CODEC.encodeStart(ops, prop.build()).getOrThrow();
                futures.add(DataProvider.saveStable(cache, obj, path));
            });
            FateEntities.getEntityProps().forEach((res, prop) -> {
                Path path = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(res.getNamespace() + "/" + EntityPropsManager.DIRECTORY + "/" + res.getPath() + ".json");
                JsonElement obj = AttributeHolderProperties.CODEC.encodeStart(ops, prop.build()).getOrThrow();
                futures.add(DataProvider.saveStable(cache, obj, path));
            });
            return CompletableFuture.allOf(futures.build().toArray(CompletableFuture[]::new));
        });
    }

    @Override
    public String getName() {
        return "EntityProperties";
    }
}