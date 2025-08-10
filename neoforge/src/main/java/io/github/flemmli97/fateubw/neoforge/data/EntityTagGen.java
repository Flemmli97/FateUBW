package io.github.flemmli97.fateubw.neoforge.data;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.lib.FateTags;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.tenshilib.TenshiLib;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class EntityTagGen extends TagsProvider<EntityType<?>> {

    @SuppressWarnings("deprecation")
    public EntityTagGen(DataGenerator arg, @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, Registry.ENTITY_TYPE, Fate.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        for (RegistryEntrySupplier<EntityType<?>, EntityType<?>> type : FateEntities.getServants()) {
            this.tag(FateTags.SERVANT).add(type.get());
        }
        this.tag(FateTags.STRONG_MOB)
                .addTag(FateTags.SERVANT)
                .add(FateEntities.LESSER_MONSTER.get())
                .add(FateEntities.GORDIUS_WHEEL.get())
                .add(FateEntities.PEGASUS.get())
                .add(FateEntities.HASSAN_COPY.get());

        this.tag(TenshiLib.MULTIPART_ENTITY)
                .add(FateEntities.MULTIPART.get());
    }

    @Override
    public String getName() {
        return "Entity Tags";
    }
}
