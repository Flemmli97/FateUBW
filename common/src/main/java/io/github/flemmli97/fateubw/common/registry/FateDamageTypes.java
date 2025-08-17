package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class FateDamageTypes {

    /**
     * This is purely for datagen
     */
    public static final Map<ResourceKey<DamageType>, Translation> TRANSLATIONS = new HashMap<>();

    public static final ResourceKey<DamageType> GRAIL = create("grail",
            "%1$s died by the power of the holy grail",
            "%1$s tried to run from %2$s but the grail decided otherwise",
            null);

    public static final ResourceKey<DamageType> EXCALIBUR = create("excalibur",
            "%1$s was vaporized by %2$s with excalibur",
            null,
            "%1$s was vaporized by %2$s using %3$s");
    public static final ResourceKey<DamageType> ENUMA_ELISH = create("enuma_elish",
            "%1$s was erased by the swirling void of Ea",
            null,
            "%1$s was erased by the swirling void from %2$s using %3$s");
    public static final ResourceKey<DamageType> BABYLON = create("babylon",
            "%1$s was impaled by %2$s with the gate of babylon",
            null,
            "%1$s was impaled by %2$s with the gate of babylon");
    public static final ResourceKey<DamageType> THROWN_ITEM = create("thrown_item",
            "%1$s was killed by %2$s",
            null,
            "%1$s was killed by %2$s using %3$s");
    public static final ResourceKey<DamageType> GAE_BOLG = create("gae_bolg",
            "%1$s's heart was pierced with gae bolg",
            null,
            "%1$s's heart was pierced with gae bolg");
    public static final ResourceKey<DamageType> CALADBOLG = create("caladbolg",
            "%1$s was pierced by Caladbolg (v. II)",
            null,
            "%1$s was pierced by Caladbolg (v. II)");
    public static final ResourceKey<DamageType> ARCHER_NORMAL = create("arrow",
            "%1$s was shot by %2$s",
            null,
            "%1$s was shot by %2$s using %3$s");
    public static final ResourceKey<DamageType> MAGIC_BEAM = create("magic_beam",
            "%1$s was killed by magic from %2$s",
            null,
            "%1$s was killed by magic from %2$s");
    public static final ResourceKey<DamageType> MAGIC_SHOT = create("magic_shot",
            "%1$s was killed by %2$s using magic",
            null,
            "%1$s was killed by %2$s using magic");
    public static final ResourceKey<DamageType> TSUBAME = create("tsubame",
            "%1$s fell victim to the mighty swallow",
            null,
            "%1$s fell victim to the mighty swallow");
    public static final ResourceKey<DamageType> GAE_DEARG = create("gae_dearg",
            "The curse of gae dearg proved to be too much for %1$s",
            null,
            "The curse of gae dearg proved to be too much for %1$s");
    public static final ResourceKey<DamageType> GORDIUS_TRAMPLE = create("gordius",
            "%1$s was trampled by the divine bulls",
            null,
            "%1$s was trampled by the divine bulls");
    public static final ResourceKey<DamageType> PEGASUS_CHARGE = create("pegasus",
            "%1$s thought they could take on pegasus charge",
            null,
            "%1$s thought they could take on pegasus charge");

    private static ResourceKey<DamageType> create(String path, String generic, String player, String item) {
        ResourceKey<DamageType> key = ResourceKey.create(Registries.DAMAGE_TYPE, Fate.modRes(path));
        TRANSLATIONS.put(key, new Translation(key, generic, player, item));
        return key;
    }

    public static DamageSource grail(RegistryAccess registry) {
        return create(GRAIL, registry.lookupOrThrow(Registries.DAMAGE_TYPE), null, null);
    }

    public static DamageSource direct(ResourceKey<DamageType> key, Entity direct) {
        return create(key, direct.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE), direct, direct);
    }

    public static DamageSource indirect(ResourceKey<DamageType> key, Entity direct, Entity causing) {
        return create(key, direct.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE), direct, causing);
    }

    public static DamageSource create(ResourceKey<DamageType> key, HolderLookup<DamageType> registry, @Nullable Entity direct, @Nullable Entity causing) {
        return new DamageSource(registry.getOrThrow(key), direct, causing);
    }

    public record Translation(ResourceKey<DamageType> key, String generic, String player, String item) {

        public void add(BiConsumer<String, String> cons) {
            if (this.generic != null) {
                cons.accept("death.attack." + this.key.location().toLanguageKey(), this.generic);
            }
            if (this.player != null) {
                cons.accept("death.attack." + this.key.location().toLanguageKey() + ".player", this.player);
            }
            if (this.item != null) {
                cons.accept("death.attack." + this.key.location().toLanguageKey() + ".item", this.item);
            }
        }
    }
}
