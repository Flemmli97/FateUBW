package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class ModAttributes {

    public static final PlatformRegistry<Attribute> ATTRIBUTES = PlatformUtils.INSTANCE.of(Registry.ATTRIBUTE_REGISTRY, Fate.MODID);

    public static final RegistryEntrySupplier<Attribute> MAGIC_RESISTANCE = ATTRIBUTES.register("attr_magic_res", () -> new RangedAttribute("generic.magicResistance", 0, 0, 1));
    public static final RegistryEntrySupplier<Attribute> PROJECTILE_RESISTANCE = ATTRIBUTES.register("attr_proj_res", () -> new RangedAttribute("generic.magicResistance", 0, 0, 30));
    public static final RegistryEntrySupplier<Attribute> PROJECTILE_BLOCKCHANCE = ATTRIBUTES.register("attr_proj_block", () -> new RangedAttribute("generic.magicResistance", 0, 0, 1));

}
