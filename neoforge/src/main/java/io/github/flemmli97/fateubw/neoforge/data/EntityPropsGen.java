package io.github.flemmli97.fateubw.neoforge.data;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.datapack.AttributeHolderProperties;
import io.github.flemmli97.fateubw.api.datapack.provider.ServantPropertiesProvider;
import io.github.flemmli97.fateubw.common.datapack.EntityPropsManager;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class EntityPropsGen extends ServantPropertiesProvider {

    public EntityPropsGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, Fate.MODID, provider);
    }

    @Override
    protected void add(HolderLookup.Provider provider) {
        FateEntities.DEFAULT_SERVANT_PROPERTIES.forEach((id, builder) -> {
            this.contents.put(id, builder.build());
        });
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return CompletableFuture.allOf(super.run(cache), this.provider.thenCompose(provider -> {
            DynamicOps<JsonElement> ops = provider.createSerializationContext(JsonOps.INSTANCE);
            ImmutableList.Builder<CompletableFuture<?>> futures = new ImmutableList.Builder<>();
            FateEntities.DEFAULT_ENTITY_PROPERTIES.forEach((res, prop) -> {
                Path path = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(res.getNamespace() + "/" + EntityPropsManager.DIRECTORY + "/" + res.getPath() + ".json");
                JsonElement obj = AttributeHolderProperties.CODEC.encodeStart(ops, prop.build()).getOrThrow();
                futures.add(DataProvider.saveStable(cache, obj, path));
            });
            return CompletableFuture.allOf(futures.build().toArray(CompletableFuture[]::new));
        }));
    }
}