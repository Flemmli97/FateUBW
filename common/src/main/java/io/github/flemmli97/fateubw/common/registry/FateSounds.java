package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.TenshiLibCrossPlat;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FateSounds {

    public static final LoaderRegister<SoundEvent> SOUND_EVENTS = LoaderRegistryAccess.INSTANCE.of(Registries.SOUND_EVENT, Fate.MODID);
    public static final Map<ResourceLocation, SoundHolder> SOUND_DATA = new HashMap<>();

    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ALTAR_SUMMON = register("block.altar.summon",
            b -> b.defaultTranslation("Altar summoning").sound(ResourceLocation.withDefaultNamespace("portal/travel")));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> CHALK_PLACE = register("block.chalk.place",
            b -> b.defaultTranslation("Block placed").sound(ResourceLocation.withDefaultNamespace("dig/cloth"), 4));

    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> WEAPON_FIRE = register("entity.weapon.projectile.shoot",
            b -> b.defaultTranslation("Weapon firing").sound(SoundEvents.PLAYER_ATTACK_SWEEP.getLocation(), 7));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> BABYLON_SPAWN = register("entity.weapon.projectile.spawn",
            b -> b.defaultTranslation("Weapon spawns").sound(SoundEvents.BEACON_ACTIVATE.getLocation()));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> CALAD_BOLG_IMPACT = register("entity.caladbolg.impact",
            b -> b.defaultTranslation("Calad Bolg impact").sound(ResourceLocation.withDefaultNamespace("random/explode"), 4));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> CHAIN_MOVE = register("entity.chain",
            b -> b.defaultTranslation("Chains rattles").sound(ResourceLocation.withDefaultNamespace("block/chain/break"), 4));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> DAGGER_THROW = register("entity.dagger.throw", "Dagger thrown");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> EA_SHOOT = register("entity.ea.shoot",
            b -> b.defaultTranslation("EA used").sound(ResourceLocation.withDefaultNamespace("random/explode"), 4));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> EXCALIBUR_SHOOT = register("entity.excalibur.shoot",
            b -> b.defaultTranslation("Excalibur used").sound(ResourceLocation.withDefaultNamespace("random/explode"), 4));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> MONSTER_SPIT = register("entity.monster.spits",
            b -> b.defaultTranslation("Monster spits").sound(ResourceLocation.withDefaultNamespace("block/honeyblock/break"), 5));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SERVANT_DEATH = register("entity.servant.death",
            b -> b.defaultTranslation("Servant died").sound(ResourceLocation.withDefaultNamespace("mob/wither/spawn")));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> TENTACLE_SLAM = register("entity.tentacle.slams",
            b -> b.defaultTranslation("Tentacle slams").sound(ResourceLocation.withDefaultNamespace("random/explode"), 4));

    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> AESTUS_DOMUS_GROUND_STAB = register("entity.nero.aestus_domus_ground_stab",
            b -> b.defaultTranslation("Aestus Domus Aurea prepare").sound(ResourceLocation.withDefaultNamespace("random/anvil_land"), 1).pitch(0.4f));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> AESTUS_DOMUS_IMPACT = register("entity.nero.aestus_domus_impact",
            b -> b.defaultTranslation("Aestus Domus Aurea impact").sound(ResourceLocation.withDefaultNamespace("random/explode"), 4));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> AESTUS_DOMUS_ROSES = register("entity.nero.aestus_domus_roses",
            b -> b.defaultTranslation("Rose-Petals scatters"));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> DIARMUID_SEAL = register("entity.diarmuid.seal",
            b -> b.defaultTranslation("Seal noble phantasm").sound(SoundEvents.BEACON_ACTIVATE.getLocation()));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> DIARMUID_TELEPORT = register("entity.diarmuid.teleport",
            b -> b.defaultTranslation("Shift").sound(SoundEvents.PLAYER_ATTACK_SWEEP.getLocation(), 7));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> DIARMUID_UNSEAL = register("entity.diarmuid.unseal",
            b -> b.defaultTranslation("Unseal noble phantasm").sound(ResourceLocation.withDefaultNamespace("block/enchantment_table/enchant"), 3));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> GILGAMESH_BLOCK = register("entity.gilgamesh.block",
            b -> b.defaultTranslation("Attack blocked").sound(ResourceLocation.withDefaultNamespace("random/explode"), 4));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> GORGONS_EYES = register("entity.medusa.gorgons_eyes",
            b -> b.defaultTranslation("Gorgons Eyes used").sound(ResourceLocation.withDefaultNamespace("item/totem/use_totem")));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> HERACLES_HIT = register("entity.heracles.hit",
            b -> b.defaultTranslation("Strong hit").sound(ResourceLocation.withDefaultNamespace("random/explode"), 4));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> HERACLES_ROAR = register("entity.heracles.roar", "Heracles roars");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> LANCELOT_REFLECT = register("entity.lancelot.reflect",
            b -> b.defaultTranslation("Projectile reflected").sound(ResourceLocation.withDefaultNamespace("random/anvil_land")));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> LANCELOT_SLAM = register("entity.lancelot.slam",
            b -> b.defaultTranslation("Slam").sound(ResourceLocation.withDefaultNamespace("random/explode"), 4));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> MEDEA_CIRCLE = register("entity.medea.cast",
            b -> b.defaultTranslation("Magic casted").sound(ResourceLocation.withDefaultNamespace("block/beacon/power"), 3));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> RULE_BREAKER = register("entity.medea.rule_breaker",
            b -> b.defaultTranslation("Magic being severed").sound(ResourceLocation.withDefaultNamespace("item/totem/use_totem")));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> GORDIUS_STEP = register("entity.gordius_wheel.step",
            b -> b.defaultTranslation("Chariot moves").sound(ResourceLocation.withDefaultNamespace("mob/cow/step"), 4).pitch(0.4f).volume(0.5f));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> GORDIUS_AMBIENT = register("entity.gordius_wheel.ambient",
            b -> b.defaultTranslation("Bulls moos").sound(ResourceLocation.withDefaultNamespace("mob/cow/say"), 4).pitch(0.3f));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> GORDIUS_HURT = register("entity.gordius_wheel.hurt",
            b -> b.defaultTranslation("Bulls hurt").sound(ResourceLocation.withDefaultNamespace("mob/cow/hurt"), 3).pitch(0.3f));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> GORDIUS_DEATH = register("entity.gordius_wheel.death",
            b -> b.defaultTranslation("Chariot defeated").sound(ResourceLocation.withDefaultNamespace("mob/cow/hurt"), 3).pitch(0.3f));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> GORDIUS_CHARGE = register("entity.gordius_wheel.charge", "Angry Bulls");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> HOPLITE_REPAIR = register("entity.hoplite.repair",
            b -> b.defaultTranslation("Gear Repaired").sound(ResourceLocation.withDefaultNamespace("random/anvil_use")));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> HOPLITE_SPEAR = register("entity.hoplite.spear_throw",
            b -> b.defaultTranslation("Spear Thrown").sound(ResourceLocation.withDefaultNamespace("item/trident/throw"), 2));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> HOPLITE_SHIELD_BREAK = register("entity.hoplite.shield_break",
            b -> b.defaultTranslation("Shield Break").sound(ResourceLocation.withDefaultNamespace("mob/zombie/wood"), 4));

    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> MAGIC_SPAWN = register("misc.magic.cast",
            b -> b.defaultTranslation("Magic casted").sound(ResourceLocation.withDefaultNamespace("block/enchantment_table/enchant"), 3));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> PETRIFICATION_CRACK = register("misc.petrification.crack",
            b -> b.defaultTranslation("Petrification cracks").sound(ResourceLocation.withDefaultNamespace("dig/stone"), 4));

    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SLASH = register("generic.slash", "Weapon slashing");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SLASH_IMPACT = register("generic.slash_impact", "Slash impact");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SWOOSH_1 = register("generic.swoosh_1", "Swoosh");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SWOOSH_2 = register("generic.swoosh_2", "Swoosh");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> TELEPORT = register("generic.teleport",
            b -> b.defaultTranslation("Entity teleports").sound(ResourceLocation.withDefaultNamespace("mob/endermen/portal")).sound(ResourceLocation.withDefaultNamespace("mob/endermen/portal2")));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ZAP = register("generic.zap", "Zap");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> REALITY_MARBLE = register("generic.reality_marble_cast", "Reality Marble used");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> BLOCK = register("generic.block",
            b -> b.defaultTranslation("Attack blocked").sound(ResourceLocation.withDefaultNamespace("random/anvil_land")));

    private static RegistryEntrySupplier<SoundEvent, SoundEvent> register(String name, String translation) {
        return register(name, b -> b.defaultTranslation(translation));
    }

    private static RegistryEntrySupplier<SoundEvent, SoundEvent> register(String name, Consumer<SoundHolder.Builder> data) {
        RegistryEntrySupplier<SoundEvent, SoundEvent> res = SOUND_EVENTS.register(name, SoundEvent::createVariableRangeEvent);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            SoundHolder.Builder builder = new SoundHolder.Builder();
            if (data != null) {
                data.accept(builder);
            }
            SOUND_DATA.put(res.getID(), builder.build(res.getID()));
        }
        return res;
    }

    public record SoundHolder(List<ResourceLocation> locations, float pitch, float volume,
                              @Nullable String defaultTranslation) {

        private static class Builder {

            private final List<ResourceLocation> locations = new ArrayList<>();
            private float pitch = 1, volume = 1;
            private String defaultTranslation;

            public Builder sound(ResourceLocation location) {
                return this.sound(location, 1);
            }

            public Builder sound(ResourceLocation location, int variants) {
                if (variants <= 1) {
                    this.locations.add(location);
                } else {
                    for (int i = 0; i < variants; i++) {
                        this.locations.add(ResourceLocation.fromNamespaceAndPath(location.getNamespace(), location.getPath() + (i + 1)));
                    }
                }
                return this;
            }

            public Builder pitch(float pitch) {
                this.pitch = pitch;
                return this;
            }

            public Builder volume(float volume) {
                this.volume = volume;
                return this;
            }

            public Builder defaultTranslation(String defaultTranslation) {
                this.defaultTranslation = defaultTranslation;
                return this;
            }

            public SoundHolder build(ResourceLocation sound) {
                if (this.locations.isEmpty())
                    this.locations.add(sound);
                return new SoundHolder(List.copyOf(this.locations), this.pitch, this.volume, this.defaultTranslation);
            }
        }
    }
}
