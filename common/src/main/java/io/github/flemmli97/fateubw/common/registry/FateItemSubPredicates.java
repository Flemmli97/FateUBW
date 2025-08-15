package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.advancements.DataComponentPresentPredicate;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.core.registries.Registries;

public class FateItemSubPredicates {

    public static final LoaderRegister<ItemSubPredicate.Type<?>> SUB_PREDICATES = LoaderRegistryAccess.INSTANCE.of(Registries.ITEM_SUB_PREDICATE_TYPE, Fate.MODID);

    public static final RegistryEntrySupplier<ItemSubPredicate.Type<?>, ItemSubPredicate.Type<DataComponentPresentPredicate>> COMPONENT_PRESENT = SUB_PREDICATES.register("component_present", () -> DataComponentPresentPredicate.TYPE);

}
