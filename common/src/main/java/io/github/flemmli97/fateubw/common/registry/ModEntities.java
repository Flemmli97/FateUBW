package io.github.flemmli97.fateubw.common.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.datapack.ServantProperties;
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
import io.github.flemmli97.fateubw.common.lib.BuiltinServantClasses;
import io.github.flemmli97.fateubw.platform.Platform;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModEntities {

    public static final PlatformRegistry<EntityType<?>> ENTITIES = PlatformUtils.INSTANCE.of(Registry.ENTITY_TYPE_REGISTRY, Fate.MODID);

    private static final Map<ResourceLocation, ServantProperties> DEFAULT_PROPERTIES = new HashMap<>();

    private static final List<RegistryEntrySupplier<EntityType<?>>> SERVANTS = new ArrayList<>();

    public static final RegistryEntrySupplier<EntityType<EntityArthur>> ARTHUR = regServant(EntityType.Builder.of(EntityArthur::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "arthur"), 0x048dd0, 0xecee37,
            new ServantProperties(300, 10, 17, 0.15f, 12, 10, 0.3, 100, BuiltinServantClasses.SABER));

    public static final RegistryEntrySupplier<EntityType<EntityCuchulainn>> CUCHULAINN = regServant(EntityType.Builder.of(EntityCuchulainn::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "cuchulainn"), 0x0038ff, 0xb6c0c1,
            new ServantProperties(275, 7.5, 10, 0, 14, 6, 0.35, 75, BuiltinServantClasses.LANCER));
    public static final RegistryEntrySupplier<EntityType<EntityDiarmuid>> DIARMUID = regServant(EntityType.Builder.of(EntityDiarmuid::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "diarmuid"), 0x2d5554, 0x302f34,
            new ServantProperties(310, 8.5, 12, 0, 13, 7, 0.35, 80, BuiltinServantClasses.LANCER));

    public static final RegistryEntrySupplier<EntityType<EntityEmiya>> EMIYA = regServant(EntityType.Builder.of(EntityEmiya::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "emiya"), 0x9f0707, 0x000000,
            new ServantProperties(250, 7.5, 8, 0, 15.5, 7, 0.33, 66, BuiltinServantClasses.ARCHER));
    public static final RegistryEntrySupplier<EntityType<EntityGilgamesh>> GILGAMESH = regServant(EntityType.Builder.of(EntityGilgamesh::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "gilgamesh"), 0xfff400, 0xffdb00,
            new ServantProperties(250, 10, 9, 0, 12.5, 5, 0.3, 100, BuiltinServantClasses.ARCHER));

    public static final RegistryEntrySupplier<EntityType<EntityMedea>> MEDEA = regServant(EntityType.Builder.of(EntityMedea::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "medea"), 0x6f086b, 0x4a8be5,
            new ServantProperties(350, 9.5, 5, 0, 4, 17.5, 0.27, 100, BuiltinServantClasses.CASTER));
    public static final RegistryEntrySupplier<EntityType<EntityGilles>> GILLES = regServant(EntityType.Builder.of(EntityGilles::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "gilles"), 0x100460, 0x600453,
            new ServantProperties(350, 5.5, 7, 0, 5, 14, 0.29, 80, BuiltinServantClasses.CASTER));

    public static final RegistryEntrySupplier<EntityType<EntityHeracles>> HERACLES = regServant(EntityType.Builder.of(EntityHeracles::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "heracles"), 0x3c1d06, 0x5e3c22,
            new ServantProperties(100, 7.5, 10, 0, 17, 9.5, 0.25, 0, BuiltinServantClasses.BERSERKER));
    public static final RegistryEntrySupplier<EntityType<EntityLancelot>> LANCELOT = regServant(EntityType.Builder.of(EntityLancelot::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "lancelot"), 0x071a33, 0x1d4f94,
            new ServantProperties(450, 9, 14, 0.4f, 19, 4, 0.26, 0, BuiltinServantClasses.BERSERKER));

    public static final RegistryEntrySupplier<EntityType<EntityIskander>> ISKANDER = regServant(EntityType.Builder.of(EntityIskander::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "iskander"), 0xd40000, 0x8d0101,
            new ServantProperties(400, 5.5, 10, 0, 9, 9.5, 0.3, 100, BuiltinServantClasses.RIDER));
    public static final RegistryEntrySupplier<EntityType<EntityMedusa>> MEDUSA = regServant(EntityType.Builder.of(EntityMedusa::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "medusa"), 0x000000, 0xf234ea,
            new ServantProperties(250, 4.5, 11, 0, 7, 10, 0.3, 80, BuiltinServantClasses.RIDER));

    public static final RegistryEntrySupplier<EntityType<EntityHassan>> HASSAN = regServant(EntityType.Builder.of(EntityHassan::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "hassan"), 0x000000, 0x3a393a,
            new ServantProperties(200, 6, 8.5, 0, 17, 4, 0.34, 15, BuiltinServantClasses.ASSASSIN));
    public static final RegistryEntrySupplier<EntityType<EntitySasaki>> SASAKI = regServant(EntityType.Builder.of(EntitySasaki::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "sasaki"), 0x4e04c3, 0xa77cec,
            new ServantProperties(350, 9.5, 9, 0, 8, 8.5, 0.3, 50, BuiltinServantClasses.ASSASSIN));

    public static final RegistryEntrySupplier<EntityType<Excalibur>> EXCALIBUR = reg(EntityType.Builder.<Excalibur>of(Excalibur::new, MobCategory.MISC).sized(0.05F, 0.05F), new ResourceLocation(Fate.MODID, "excalibur"));
    public static final RegistryEntrySupplier<EntityType<GaeBolg>> GAEBOLG = reg(EntityType.Builder.<GaeBolg>of(GaeBolg::new, MobCategory.MISC).sized(0.25F, 0.25F), new ResourceLocation(Fate.MODID, "gae_bolg"));
    public static final RegistryEntrySupplier<EntityType<ArcherArrow>> ARCHER_ARROW = reg(EntityType.Builder.<ArcherArrow>of(ArcherArrow::new, MobCategory.MISC).sized(0.25F, 0.25F), new ResourceLocation(Fate.MODID, "archer_arrow"));
    public static final RegistryEntrySupplier<EntityType<CaladBolg>> CALADBOLG = reg(EntityType.Builder.<CaladBolg>of(CaladBolg::new, MobCategory.MISC).sized(0.25F, 0.25F), new ResourceLocation(Fate.MODID, "caladbolg"));
    public static final RegistryEntrySupplier<EntityType<BabylonWeapon>> BABYLON = reg(EntityType.Builder.<BabylonWeapon>of(BabylonWeapon::new, MobCategory.MISC).sized(0.25F, 0.25F), new ResourceLocation(Fate.MODID, "babylon"));
    public static final RegistryEntrySupplier<EntityType<EnumaElish>> EA = reg(EntityType.Builder.<EnumaElish>of(EnumaElish::new, MobCategory.MISC).sized(0.05F, 0.05F), new ResourceLocation(Fate.MODID, "ea"));
    public static final RegistryEntrySupplier<EntityType<MagicBeam>> MAGIC_BEAM = reg(EntityType.Builder.of(MagicBeam::new, MobCategory.MISC), new ResourceLocation(Fate.MODID, "magic_beam"));
    public static final RegistryEntrySupplier<EntityType<MagicBufCircle>> MEDEA_CIRCLE = reg(EntityType.Builder.of(MagicBufCircle::new, MobCategory.MISC), new ResourceLocation(Fate.MODID, "medea_circle"));
    public static final RegistryEntrySupplier<EntityType<LesserMonster>> LESSER_MONSTER = regWithEgg(EntityType.Builder.<LesserMonster>of(LesserMonster::new, MobCategory.MONSTER).clientTrackingRange(8), new ResourceLocation(Fate.MODID, "starfish_monster"), 0x171c3f, 0x00ff00);
    public static final RegistryEntrySupplier<EntityType<Gordius>> GORDIUS_WHEEL = regWithEgg(EntityType.Builder.of(Gordius::new, MobCategory.CREATURE).sized(2, 1.5f), new ResourceLocation(Fate.MODID, "gordius_wheel"), 0x87595c, 0x981a24);

    public static final RegistryEntrySupplier<EntityType<HassanClone>> HASSAN_COPY = reg(EntityType.Builder.of(HassanClone::new, MobCategory.MISC), new ResourceLocation(Fate.MODID, "hassan_copy"));
    public static final RegistryEntrySupplier<EntityType<Pegasus>> PEGASUS = regWithEgg(EntityType.Builder.of(Pegasus::new, MobCategory.MONSTER).sized(1.35f, 1.65f), new ResourceLocation(Fate.MODID, "pegasus"), 0xffffff, 0xdde0e1);
    public static final RegistryEntrySupplier<EntityType<ChainDagger>> DAGGER_HOOK = reg(EntityType.Builder.<ChainDagger>of(ChainDagger::new, MobCategory.MISC).updateInterval(5).sized(0.25F, 0.25F), new ResourceLocation(Fate.MODID, "medusa_dagger"));

    public static final RegistryEntrySupplier<EntityType<ThrownGem>> GEM = reg(EntityType.Builder.<ThrownGem>of(ThrownGem::new, MobCategory.MISC).sized(0.25F, 0.25F), new ResourceLocation(Fate.MODID, "entity_gem"));

    public static final RegistryEntrySupplier<EntityType<MultiPartEntity>> MULTIPART = reg(EntityType.Builder.<MultiPartEntity>of(MultiPartEntity::new, MobCategory.MISC).sized(0.25F, 0.25F), new ResourceLocation(Fate.MODID, "multi_part"));

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <V extends BaseServant> RegistryEntrySupplier<EntityType<V>> regServant(EntityType.Builder<V> entity, ResourceLocation name, int primary, int secondary, ServantProperties props) {
        RegistryEntrySupplier<EntityType<V>> reg = reg(entity.clientTrackingRange(10), name);
        ModItems.ITEMS.register(name.getPath() + "_spawn_egg", () -> new FateEgg(reg, primary, secondary, new Item.Properties().tab(Fate.TAB)));
        if (Platform.INSTANCE.isDatagen()) {
            DEFAULT_PROPERTIES.put(name, props);
            SERVANTS.add((RegistryEntrySupplier) reg);
        }
        return reg;
    }

    public static <V extends Mob> RegistryEntrySupplier<EntityType<V>> regWithEgg(EntityType.Builder<V> entity, ResourceLocation name, int primary, int secondary) {
        RegistryEntrySupplier<EntityType<V>> reg = reg(entity.clientTrackingRange(10), name);
        ModItems.ITEMS.register(name.getPath() + "_spawn_egg", () -> new SpawnEgg(reg, primary, secondary, new Item.Properties().tab(Fate.TAB)));
        return reg;
    }

    private static <V extends Entity> RegistryEntrySupplier<EntityType<V>> reg(EntityType.Builder<V> v, ResourceLocation name) {
        return ENTITIES.register(name.getPath(), () -> v.build(name.getPath()));
    }

    public static Map<ResourceLocation, ServantProperties> getDefaultMobProperties() {
        return ImmutableMap.copyOf(DEFAULT_PROPERTIES);
    }

    public static List<RegistryEntrySupplier<EntityType<?>>> getServants() {
        return ImmutableList.copyOf(SERVANTS);
    }

    public static Map<EntityType<? extends LivingEntity>, AttributeSupplier.Builder> registeredAttributes() {
        Map<EntityType<? extends LivingEntity>, AttributeSupplier.Builder> map = new HashMap<>();
        map.put(ModEntities.ARTHUR.get(), BaseServant.createMobAttributes());

        map.put(ModEntities.CUCHULAINN.get(), BaseServant.createMobAttributes());
        map.put(ModEntities.DIARMUID.get(), BaseServant.createMobAttributes());

        map.put(ModEntities.EMIYA.get(), BaseServant.createMobAttributes());
        map.put(ModEntities.GILGAMESH.get(), BaseServant.createMobAttributes());

        map.put(ModEntities.GILLES.get(), BaseServant.createMobAttributes());
        map.put(ModEntities.MEDEA.get(), BaseServant.createMobAttributes());

        map.put(ModEntities.HERACLES.get(), BaseServant.createMobAttributes());
        map.put(ModEntities.LANCELOT.get(), BaseServant.createMobAttributes());

        map.put(ModEntities.MEDUSA.get(), BaseServant.createMobAttributes());
        map.put(ModEntities.ISKANDER.get(), BaseServant.createMobAttributes());

        map.put(ModEntities.HASSAN.get(), BaseServant.createMobAttributes());
        map.put(ModEntities.SASAKI.get(), BaseServant.createMobAttributes());

        map.put(ModEntities.LESSER_MONSTER.get(), BaseServant.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.25));
        map.put(ModEntities.GORDIUS_WHEEL.get(), Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1));
        map.put(ModEntities.PEGASUS.get(), BaseServant.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.3));
        map.put(ModEntities.HASSAN_COPY.get(), BaseServant.createMobAttributes());
        return map;
    }
}
