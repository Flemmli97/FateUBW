package io.github.flemmli97.fateubw.api.datapack.provider;

import io.github.flemmli97.fateubw.api.datapack.ServantProperties;
import io.github.flemmli97.fateubw.common.datapack.EntityPropsManager;
import io.github.flemmli97.tenshilib.common.data.provider.CodecBasedProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public abstract class ServantPropertiesProvider extends CodecBasedProvider<ServantProperties> {

    public ServantPropertiesProvider(PackOutput output, String modid, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, PackOutput.Target.DATA_PACK, modid, EntityPropsManager.DIRECTORY, ServantProperties.CODEC, provider);
    }

    public void addProperties(EntityType<?> type, ServantProperties properties) {
        this.contents.put(BuiltInRegistries.ENTITY_TYPE.getKey(type), properties);
    }
}