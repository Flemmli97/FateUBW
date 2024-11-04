package io.github.flemmli97.fateubw.forge.data;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.lib.FateTags;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.tenshilib.TenshiLib;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class EntityTagGen extends TagsProvider<EntityType<?>> {

    @SuppressWarnings("deprecation")
    public EntityTagGen(DataGenerator arg, @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, Registry.ENTITY_TYPE, Fate.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        for (RegistryEntrySupplier<EntityType<?>> type : ModEntities.getServants()) {
            this.tag(FateTags.SERVANT).add(type.get());
        }
        this.tag(TenshiLib.MULTIPART_ENTITY)
                .add(ModEntities.MULTIPART.get());
    }

    @Override
    public String getName() {
        return "Entity Tags";
    }
}
