package io.github.flemmli97.fateubw.neoforge.data.tags;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.lib.FateTags;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.tenshilib.TenshiLib;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class EntityTagGen extends IntrinsicHolderTagsProvider<EntityType<?>> {

    public EntityTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super(output, Registries.ENTITY_TYPE, lookupProvider, type -> BuiltInRegistries.ENTITY_TYPE.getResourceKey(type).orElseThrow(), Fate.MODID, fileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (RegistryEntrySupplier<EntityType<?>, EntityType<?>> type : FateEntities.SERVANTS) {
            this.tag(FateTags.EntityTypes.SERVANT).add(type.get());
        }
        this.tag(FateTags.EntityTypes.STRONG_MOB)
                .addTag(FateTags.EntityTypes.SERVANT)
                .add(FateEntities.LESSER_MONSTER.get())
                .add(FateEntities.GORDIUS_WHEEL.get())
                .add(FateEntities.PEGASUS.get())
                .add(FateEntities.HASSAN_COPY.get());

        this.tag(TenshiLib.MULTIPART_ENTITY)
                .add(FateEntities.MULTIPART.get())
                .add(FateEntities.GORDIUS_CHARIOT.get());
    }
}
