package io.github.flemmli97.fateubw.common.registry;

import com.google.common.collect.Lists;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.config.ServantProperties;
import io.github.flemmli97.fateubw.common.entity.MultiPartEntity;
import io.github.flemmli97.fateubw.common.entity.minions.Gordius;
import io.github.flemmli97.fateubw.common.entity.minions.HassanClone;
import io.github.flemmli97.fateubw.common.entity.minions.LesserMonster;
import io.github.flemmli97.fateubw.common.entity.minions.Pegasus;
import io.github.flemmli97.fateubw.common.entity.misc.ArcherArrow;
import io.github.flemmli97.fateubw.common.entity.misc.BabylonWeapon;
import io.github.flemmli97.fateubw.common.entity.misc.CaladBolg;
import io.github.flemmli97.fateubw.common.entity.misc.ChainDagger;
import io.github.flemmli97.fateubw.common.entity.misc.EnumaElish;
import io.github.flemmli97.fateubw.common.entity.misc.Excalibur;
import io.github.flemmli97.fateubw.common.entity.misc.GaeBolg;
import io.github.flemmli97.fateubw.common.entity.misc.MagicBeam;
import io.github.flemmli97.fateubw.common.entity.misc.MagicBufCircle;
import io.github.flemmli97.fateubw.common.entity.misc.ThrownGem;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.entity.servant.EntityArthur;
import io.github.flemmli97.fateubw.common.entity.servant.EntityCuchulainn;
import io.github.flemmli97.fateubw.common.entity.servant.EntityDiarmuid;
import io.github.flemmli97.fateubw.common.entity.servant.EntityEmiya;
import io.github.flemmli97.fateubw.common.entity.servant.EntityGilgamesh;
import io.github.flemmli97.fateubw.common.entity.servant.EntityGilles;
import io.github.flemmli97.fateubw.common.entity.servant.EntityHassan;
import io.github.flemmli97.fateubw.common.entity.servant.EntityHeracles;
import io.github.flemmli97.fateubw.common.entity.servant.EntityIskander;
import io.github.flemmli97.fateubw.common.entity.servant.EntityLancelot;
import io.github.flemmli97.fateubw.common.entity.servant.EntityMedea;
import io.github.flemmli97.fateubw.common.entity.servant.EntityMedusa;
import io.github.flemmli97.fateubw.common.entity.servant.EntitySasaki;
import io.github.flemmli97.fateubw.common.items.FateEgg;
import io.github.flemmli97.fateubw.common.lib.LibEntities;
import io.github.flemmli97.fateubw.common.utils.EnumServantType;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModEntities {

    public static final PlatformRegistry<EntityType<?>> ENTITIES = PlatformUtils.INSTANCE.of(Registry.ENTITY_TYPE_REGISTRY, Fate.MODID);
    private static final Map<ResourceLocation, EnumServantType> servantTypeMap = new HashMap<>();

    //This is generic hell
    private static final EnumMap<EnumServantType, List<RegistryEntrySupplier<?>>> typeServantsMap = new EnumMap<>(EnumServantType.class);

    public static final RegistryEntrySupplier<EntityType<EntityArthur>> arthur = regServant(EnumServantType.SABER, EntityType.Builder.of(EntityArthur::new, MobCategory.MISC),
            LibEntities.arthur, 0x048dd0, 0xecee37, new ServantProperties(300, 10, 17, 0.4f, 12, 10, 0.3, 100));

    public static final RegistryEntrySupplier<EntityType<EntityCuchulainn>> cuchulainn = regServant(EnumServantType.LANCER, EntityType.Builder.of(EntityCuchulainn::new, MobCategory.MISC),
            LibEntities.cuchulainn, 0x0038ff, 0xb6c0c1, new ServantProperties(275, 7.5, 10, 0, 14, 6, 0.35, 75));
    public static final RegistryEntrySupplier<EntityType<EntityDiarmuid>> diarmuid = regServant(EnumServantType.LANCER, EntityType.Builder.of(EntityDiarmuid::new, MobCategory.MISC),
            LibEntities.diarmuid, 0x000000, 0x2a079a, new ServantProperties(310, 8.5, 12, 0, 13, 7, 0.35, 80));

    public static final RegistryEntrySupplier<EntityType<EntityEmiya>> emiya = regServant(EnumServantType.ARCHER, EntityType.Builder.of(EntityEmiya::new, MobCategory.MISC),
            LibEntities.emiya, 0x9f0707, 0x000000, new ServantProperties(250, 7.5, 8, 0, 15.5, 7, 0.33, 66));
    public static final RegistryEntrySupplier<EntityType<EntityGilgamesh>> gilgamesh = regServant(EnumServantType.ARCHER, EntityType.Builder.of(EntityGilgamesh::new, MobCategory.MISC),
            LibEntities.gilgamesh, 0xfff400, 0xffdb00, new ServantProperties(250, 10, 9, 0, 12.5, 5, 0.3, 100));

    public static final RegistryEntrySupplier<EntityType<EntityMedea>> medea = regServant(EnumServantType.CASTER, EntityType.Builder.of(EntityMedea::new, MobCategory.MISC),
            LibEntities.medea, 0x6f086b, 0x4a8be5, new ServantProperties(350, 9.5, 5, 0, 4, 17.5, 0.2, 100));
    public static final RegistryEntrySupplier<EntityType<EntityGilles>> gilles = regServant(EnumServantType.CASTER, EntityType.Builder.of(EntityGilles::new, MobCategory.MISC),
            LibEntities.gilles, 0x100460, 0x600453, new ServantProperties(350, 5.5, 7, 0, 5, 14, 0.3, 80));

    public static final RegistryEntrySupplier<EntityType<EntityHeracles>> heracles = regServant(EnumServantType.BERSERKER, EntityType.Builder.of(EntityHeracles::new, MobCategory.MISC),
            LibEntities.heracles, 0x3c1d06, 0x5e3c22, new ServantProperties(75, 7.5, 10, 0, 17, 9.5, 0.2, 0));
    public static final RegistryEntrySupplier<EntityType<EntityLancelot>> lancelot = regServant(EnumServantType.BERSERKER, EntityType.Builder.of(EntityLancelot::new, MobCategory.MISC),
            LibEntities.lancelot, 0x071a33, 0x1d4f94, new ServantProperties(450, 9, 14, 0.4f, 19, 4, 0.2, 0));

    public static final RegistryEntrySupplier<EntityType<EntityIskander>> iskander = regServant(EnumServantType.RIDER, EntityType.Builder.of(EntityIskander::new, MobCategory.MISC),
            LibEntities.alexander, 0xd40000, 0x8d0101, new ServantProperties(400, 5.5, 10, 0, 9, 9.5, 0.3, 100));
    public static final RegistryEntrySupplier<EntityType<EntityMedusa>> medusa = regServant(EnumServantType.RIDER, EntityType.Builder.of(EntityMedusa::new, MobCategory.MISC),
            LibEntities.medusa, 0x000000, 0xf234ea, new ServantProperties(250, 4.5, 11, 0, 7, 10, 0.3, 80));

    public static final RegistryEntrySupplier<EntityType<EntityHassan>> hassan = regServant(EnumServantType.ASSASSIN, EntityType.Builder.of(EntityHassan::new, MobCategory.MISC),
            LibEntities.hassan, 0x000000, 0x3a393a, new ServantProperties(200, 6, 8.5, 0, 17, 4, 0.34, 15));
    public static final RegistryEntrySupplier<EntityType<EntitySasaki>> sasaki = regServant(EnumServantType.ASSASSIN, EntityType.Builder.of(EntitySasaki::new, MobCategory.MISC),
            LibEntities.sasaki, 0x4e04c3, 0xa77cec, new ServantProperties(350, 9.5, 9, 0, 8, 8.5, 0.3, 50));

    public static final RegistryEntrySupplier<EntityType<Excalibur>> excalibur = reg(EntityType.Builder.<Excalibur>of(Excalibur::new, MobCategory.MISC).sized(0.25F, 0.25F), LibEntities.excalibur);
    public static final RegistryEntrySupplier<EntityType<GaeBolg>> gaebolg = reg(EntityType.Builder.<GaeBolg>of(GaeBolg::new, MobCategory.MISC).sized(0.25F, 0.25F), LibEntities.gae_bolg);
    public static final RegistryEntrySupplier<EntityType<ArcherArrow>> archerArrow = reg(EntityType.Builder.<ArcherArrow>of(ArcherArrow::new, MobCategory.MISC).sized(0.25F, 0.25F), LibEntities.archer_arrow);
    public static final RegistryEntrySupplier<EntityType<CaladBolg>> caladbolg = reg(EntityType.Builder.<CaladBolg>of(CaladBolg::new, MobCategory.MISC).sized(0.25F, 0.25F), LibEntities.caladbolg);
    public static final RegistryEntrySupplier<EntityType<BabylonWeapon>> babylon = reg(EntityType.Builder.<BabylonWeapon>of(BabylonWeapon::new, MobCategory.MISC).sized(0.25F, 0.25F), LibEntities.babylon);
    public static final RegistryEntrySupplier<EntityType<EnumaElish>> ea = reg(EntityType.Builder.<EnumaElish>of(EnumaElish::new, MobCategory.MISC).sized(0.25F, 0.25F), LibEntities.ea);
    public static final RegistryEntrySupplier<EntityType<MagicBeam>> magicBeam = reg(EntityType.Builder.of(MagicBeam::new, MobCategory.MISC), LibEntities.magic_beam);
    public static final RegistryEntrySupplier<EntityType<MagicBufCircle>> medeaCircle = reg(EntityType.Builder.of(MagicBufCircle::new, MobCategory.MISC), LibEntities.medea_circle);
    public static final RegistryEntrySupplier<EntityType<LesserMonster>> lesserMonster = reg(EntityType.Builder.<LesserMonster>of(LesserMonster::new, MobCategory.MONSTER).clientTrackingRange(8), LibEntities.monster_small);
    public static final RegistryEntrySupplier<EntityType<Gordius>> gordiusWheel = reg(EntityType.Builder.of(Gordius::new, MobCategory.CREATURE).sized(2, 1.5f), LibEntities.gordiusWheel);

    public static final RegistryEntrySupplier<EntityType<HassanClone>> hassanCopy = reg(EntityType.Builder.of(HassanClone::new, MobCategory.MISC), LibEntities.hassan_copy);
    public static final RegistryEntrySupplier<EntityType<Pegasus>> pegasus = reg(EntityType.Builder.of(Pegasus::new, MobCategory.MONSTER).sized(1.2f, 1.6f), LibEntities.pegasus);
    public static final RegistryEntrySupplier<EntityType<ChainDagger>> daggerHook = reg(EntityType.Builder.<ChainDagger>of(ChainDagger::new, MobCategory.MISC).updateInterval(5).sized(0.25F, 0.25F), LibEntities.daggerHook);

    public static final RegistryEntrySupplier<EntityType<ThrownGem>> gem = reg(EntityType.Builder.<ThrownGem>of(ThrownGem::new, MobCategory.MISC).sized(0.25F, 0.25F), LibEntities.entity_gem);

    public static final RegistryEntrySupplier<EntityType<MultiPartEntity>> multipart = reg(EntityType.Builder.<MultiPartEntity>of(MultiPartEntity::new, MobCategory.MISC).sized(0.25F, 0.25F), LibEntities.multipart);

    public static <V extends BaseServant> RegistryEntrySupplier<EntityType<V>> regServant(EnumServantType type, EntityType.Builder<V> entity, ResourceLocation name, int primary, int secondary, ServantProperties defaultVals) {
        RegistryEntrySupplier<EntityType<V>> reg = reg(entity.clientTrackingRange(10), name);
        servantTypeMap.put(name, type);
        typeServantsMap.merge(type, Lists.newArrayList(reg), (old, val) -> {
            old.add(reg);
            return old;
        });
        ModItems.ITEMS.register(name.getPath() + "_spawn_egg", () -> new FateEgg(reg, primary, secondary, new Item.Properties().tab(Fate.TAB)));
        Config.Common.attributes.put(name.toString(), defaultVals);
        return reg;
    }

    private static <V extends Entity> RegistryEntrySupplier<EntityType<V>> reg(EntityType.Builder<V> v, ResourceLocation name) {
        return ENTITIES.register(name.getPath(), () -> v.build(name.getPath()));
    }

    public static EnumServantType get(ResourceLocation type) {
        return servantTypeMap.getOrDefault(type, EnumServantType.NOTASSIGNED);
    }

    public static Collection<ResourceLocation> registeredServants() {
        return servantTypeMap.keySet();
    }

    @SuppressWarnings("unchecked")
    public static <V extends BaseServant> List<RegistryEntrySupplier<EntityType<V>>> getFromType(EnumServantType type) {
        List<RegistryEntrySupplier<EntityType<V>>> list = new ArrayList<>();
        typeServantsMap.getOrDefault(type, new ArrayList<>()).forEach(r -> list.add((RegistryEntrySupplier<EntityType<V>>) r));
        return list;
    }

    public static Map<EntityType<? extends LivingEntity>, AttributeSupplier.Builder> registeredAttributes() {
        Map<EntityType<? extends LivingEntity>, AttributeSupplier.Builder> map = new HashMap<>();
        map.put(arthur.get(), BaseServant.createMobAttributes());

        map.put(cuchulainn.get(), BaseServant.createMobAttributes());
        map.put(diarmuid.get(), BaseServant.createMobAttributes());

        map.put(emiya.get(), BaseServant.createMobAttributes());
        map.put(gilgamesh.get(), BaseServant.createMobAttributes());

        map.put(gilles.get(), BaseServant.createMobAttributes());
        map.put(medea.get(), BaseServant.createMobAttributes());

        map.put(heracles.get(), BaseServant.createMobAttributes());
        map.put(lancelot.get(), BaseServant.createMobAttributes());

        map.put(medusa.get(), BaseServant.createMobAttributes());
        map.put(iskander.get(), BaseServant.createMobAttributes());

        map.put(hassan.get(), BaseServant.createMobAttributes());
        map.put(sasaki.get(), BaseServant.createMobAttributes());

        map.put(lesserMonster.get(), BaseServant.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.25));
        map.put(gordiusWheel.get(), Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1));
        map.put(pegasus.get(), BaseServant.createMobAttributes());
        map.put(hassanCopy.get(), BaseServant.createMobAttributes());
        return map;
    }
}
