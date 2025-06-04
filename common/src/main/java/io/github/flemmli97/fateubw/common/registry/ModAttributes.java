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

    public static final RegistryEntrySupplier<Attribute> MAGIC_ATTACK = ATTRIBUTES.register("magic_attack", () -> new RangedAttribute("attribute.fateubw.magic_attack", 1, 0.0, Double.MAX_VALUE));
    public static final RegistryEntrySupplier<Attribute> MAGIC_RESISTANCE = ATTRIBUTES.register("magic_resistance", () -> new RangedAttribute("attribute.fateubw.magic_resistance", 0, 0, 1));
    public static final RegistryEntrySupplier<Attribute> PROJECTILE_RESISTANCE = ATTRIBUTES.register("projectile_resistance", () -> new RangedAttribute("attribute.fateubw.projectile_resistance", 0, 0, 20));
    public static final RegistryEntrySupplier<Attribute> PROJECTILE_BLOCK_CHANCE = ATTRIBUTES.register("projectile_block_chance", () -> new RangedAttribute("attribute.fateubw.projectile_block_chance", 0, 0, 1));
    public static final RegistryEntrySupplier<Attribute> COMBAT_REGEN = ATTRIBUTES.register("combat_regen", () -> new RangedAttribute("attribute.fateubw.combat_regen", 0, 0, Double.MAX_VALUE));
    public static final RegistryEntrySupplier<Attribute> PASSIVE_REGEN = ATTRIBUTES.register("passive_regen", () -> new RangedAttribute("attribute.fateubw.passive_regen", 0, 0, Double.MAX_VALUE));

}
