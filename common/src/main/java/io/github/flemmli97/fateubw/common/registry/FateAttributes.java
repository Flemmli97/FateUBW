package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class FateAttributes {

    public static final LoaderRegister<Attribute> ATTRIBUTES = LoaderRegistryAccess.INSTANCE.of(Registries.ATTRIBUTE, Fate.MODID);

    public static final RegistryEntrySupplier<Attribute, Attribute> MAGIC_ATTACK = ATTRIBUTES.register("magic_attack", () -> new RangedAttribute("attribute.fateubw.magic_attack", 1, 0.0, Double.MAX_VALUE));
    public static final RegistryEntrySupplier<Attribute, Attribute> MAGIC_RESISTANCE = ATTRIBUTES.register("magic_resistance", () -> new RangedAttribute("attribute.fateubw.magic_resistance", 0, 0, 100));
    public static final RegistryEntrySupplier<Attribute, Attribute> PROJECTILE_RESISTANCE = ATTRIBUTES.register("projectile_resistance", () -> new RangedAttribute("attribute.fateubw.projectile_resistance", 0, 0, 100));
    public static final RegistryEntrySupplier<Attribute, Attribute> PROJECTILE_BLOCK_CHANCE = ATTRIBUTES.register("projectile_block_chance", () -> new RangedAttribute("attribute.fateubw.projectile_block_chance", 0, 0, 1));
    public static final RegistryEntrySupplier<Attribute, Attribute> COMBAT_REGEN = ATTRIBUTES.register("combat_regen", () -> new RangedAttribute("attribute.fateubw.combat_regen", 0, 0, Double.MAX_VALUE));
    public static final RegistryEntrySupplier<Attribute, Attribute> PASSIVE_REGEN = ATTRIBUTES.register("passive_regen", () -> new RangedAttribute("attribute.fateubw.passive_regen", 0, 0, Double.MAX_VALUE));
    public static final RegistryEntrySupplier<Attribute, Attribute> MANA_REGEN = ATTRIBUTES.register("mana_regen", () -> new RangedAttribute("attribute.fateubw.mana_regen", 0, 0, Double.MAX_VALUE));
    public static final RegistryEntrySupplier<Attribute, Attribute> MANA_LEECH = ATTRIBUTES.register("mana_leech", () -> new RangedAttribute("attribute.fateubw.mana_leech", 0, 0, Double.MAX_VALUE));
}
