package io.github.flemmli97.fateubw.common.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.datapack.AttributeHolderProperties;
import io.github.flemmli97.fateubw.api.datapack.ServantProperties;
import io.github.flemmli97.fateubw.common.entity.MultiPartEntity;
import io.github.flemmli97.fateubw.common.entity.misc.ArcherArrow;
import io.github.flemmli97.fateubw.common.entity.misc.BabylonWeapon;
import io.github.flemmli97.fateubw.common.entity.misc.CaladBolg;
import io.github.flemmli97.fateubw.common.entity.misc.ChainDagger;
import io.github.flemmli97.fateubw.common.entity.misc.EnumaElish;
import io.github.flemmli97.fateubw.common.entity.misc.Excalibur;
import io.github.flemmli97.fateubw.common.entity.misc.GaeBolg;
import io.github.flemmli97.fateubw.common.entity.misc.MagicBeam;
import io.github.flemmli97.fateubw.common.entity.misc.MagicBufCircle;
import io.github.flemmli97.fateubw.common.entity.misc.MagicShot;
import io.github.flemmli97.fateubw.common.entity.misc.ThrownGem;
import io.github.flemmli97.fateubw.common.entity.misc.ThrownItemEntity;
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
import io.github.flemmli97.fateubw.common.entity.summons.GordiusWheel;
import io.github.flemmli97.fateubw.common.entity.summons.HassanClone;
import io.github.flemmli97.fateubw.common.entity.summons.LesserMonster;
import io.github.flemmli97.fateubw.common.entity.summons.Pegasus;
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
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModEntities {

    public static final PlatformRegistry<EntityType<?>> ENTITIES = PlatformUtils.INSTANCE.of(Registry.ENTITY_TYPE_REGISTRY, Fate.MODID);

    private static final Map<ResourceLocation, ServantProperties.Builder> DEFAULT_SERVANT_PROPERTIES = new HashMap<>();
    private static final Map<ResourceLocation, AttributeHolderProperties.Builder> DEFAULT_ENTITY_PROPERTIES = new HashMap<>();

    private static final List<RegistryEntrySupplier<EntityType<?>>> SERVANTS = new ArrayList<>();

    public static final RegistryEntrySupplier<EntityType<EntityArthur>> ARTHUR = regServant(EntityType.Builder.of(EntityArthur::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "arthur"), 0x048dd0, 0xecee37,
            new ServantProperties.Builder(BuiltinServantClasses.SABER)
                    .putAttributes(() -> Attributes.MAX_HEALTH, 400).putAttributes(() -> Attributes.ATTACK_DAMAGE, 15)
                    .putAttributes(() -> Attributes.ARMOR, 16).putAttributes(ModAttributes.PROJECTILE_BLOCK_CHANCE, 0.15f)
                    .putAttributes(ModAttributes.PROJECTILE_RESISTANCE, 3).putAttributes(ModAttributes.MAGIC_ATTACK, 15)
                    .putAttributes(() -> Attributes.MOVEMENT_SPEED, 0.33)
                    .putAttributes(ModAttributes.COMBAT_REGEN, 3).putAttributes(ModAttributes.PASSIVE_REGEN, 15)
                    .npCost(80));

    public static final RegistryEntrySupplier<EntityType<EntityCuchulainn>> CUCHULAINN = regServant(EntityType.Builder.of(EntityCuchulainn::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "cuchulainn"), 0x0038ff, 0xb6c0c1,
            new ServantProperties.Builder(BuiltinServantClasses.LANCER)
                    .putAttributes(() -> Attributes.MAX_HEALTH, 370).putAttributes(() -> Attributes.ATTACK_DAMAGE, 12)
                    .putAttributes(() -> Attributes.ARMOR, 14).putAttributes(ModAttributes.PROJECTILE_BLOCK_CHANCE, 0.1f)
                    .putAttributes(ModAttributes.PROJECTILE_RESISTANCE, 6).putAttributes(() -> Attributes.MOVEMENT_SPEED, 0.37)
                    .putAttributes(ModAttributes.COMBAT_REGEN, 2).putAttributes(ModAttributes.PASSIVE_REGEN, 15)
                    .npCost(50));
    public static final RegistryEntrySupplier<EntityType<EntityDiarmuid>> DIARMUID = regServant(EntityType.Builder.of(EntityDiarmuid::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "diarmuid"), 0x2d5554, 0x302f34,
            new ServantProperties.Builder(BuiltinServantClasses.LANCER)
                    .putAttributes(() -> Attributes.MAX_HEALTH, 380).putAttributes(() -> Attributes.ATTACK_DAMAGE, 10)
                    .putAttributes(() -> Attributes.ARMOR, 14).putAttributes(ModAttributes.PROJECTILE_BLOCK_CHANCE, 0.1f)
                    .putAttributes(ModAttributes.PROJECTILE_RESISTANCE, 5).putAttributes(() -> Attributes.MOVEMENT_SPEED, 0.37)
                    .putAttributes(ModAttributes.COMBAT_REGEN, 2).putAttributes(ModAttributes.PASSIVE_REGEN, 15)
                    .npCost(60));

    public static final RegistryEntrySupplier<EntityType<EntityEmiya>> EMIYA = regServant(EntityType.Builder.of(EntityEmiya::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "emiya"), 0x9f0707, 0x000000,
            new ServantProperties.Builder(BuiltinServantClasses.ARCHER)
                    .putAttributes(() -> Attributes.MAX_HEALTH, 350).putAttributes(() -> Attributes.ATTACK_DAMAGE, 13)
                    .putAttributes(() -> Attributes.ARMOR, 10)
                    .putAttributes(ModAttributes.PROJECTILE_RESISTANCE, 4).putAttributes(() -> Attributes.MOVEMENT_SPEED, 0.35)
                    .putAttributes(ModAttributes.COMBAT_REGEN, 1).putAttributes(ModAttributes.PASSIVE_REGEN, 15)
                    .npCost(50));
    public static final RegistryEntrySupplier<EntityType<EntityGilgamesh>> GILGAMESH = regServant(EntityType.Builder.of(EntityGilgamesh::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "gilgamesh"), 0xfff400, 0xffdb00,
            new ServantProperties.Builder(BuiltinServantClasses.ARCHER)
                    .putAttributes(() -> Attributes.MAX_HEALTH, 450).putAttributes(() -> Attributes.ATTACK_DAMAGE, 10)
                    .putAttributes(() -> Attributes.ARMOR, 12)
                    .putAttributes(ModAttributes.PROJECTILE_RESISTANCE, 5).putAttributes(() -> Attributes.MOVEMENT_SPEED, 0.32)
                    .putAttributes(ModAttributes.COMBAT_REGEN, 2).putAttributes(ModAttributes.PASSIVE_REGEN, 15)
                    .npCost(80));

    public static final RegistryEntrySupplier<EntityType<EntityMedea>> MEDEA = regServant(EntityType.Builder.of(EntityMedea::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "medea"), 0x6f086b, 0x4a8be5,
            new ServantProperties.Builder(BuiltinServantClasses.CASTER)
                    .putAttributes(() -> Attributes.MAX_HEALTH, 350).putAttributes(() -> Attributes.ATTACK_DAMAGE, 9)
                    .putAttributes(() -> Attributes.ARMOR, 8)
                    .putAttributes(ModAttributes.PROJECTILE_RESISTANCE, 2).putAttributes(ModAttributes.MAGIC_ATTACK, 17)
                    .putAttributes(ModAttributes.MAGIC_RESISTANCE, 0.4).putAttributes(() -> Attributes.MOVEMENT_SPEED, 0.31)
                    .putAttributes(ModAttributes.COMBAT_REGEN, 3.5).putAttributes(ModAttributes.PASSIVE_REGEN, 15)
                    .npCost(70));
    public static final RegistryEntrySupplier<EntityType<EntityGilles>> GILLES = regServant(EntityType.Builder.of(EntityGilles::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "gilles"), 0x100460, 0x600453,
            new ServantProperties.Builder(BuiltinServantClasses.CASTER)
                    .putAttributes(() -> Attributes.MAX_HEALTH, 370).putAttributes(() -> Attributes.ATTACK_DAMAGE, 5)
                    .putAttributes(() -> Attributes.ARMOR, 16)
                    .putAttributes(ModAttributes.PROJECTILE_RESISTANCE, 2).putAttributes(ModAttributes.MAGIC_ATTACK, 15)
                    .putAttributes(ModAttributes.MAGIC_RESISTANCE, 0.6).putAttributes(() -> Attributes.MOVEMENT_SPEED, 0.32)
                    .putAttributes(ModAttributes.COMBAT_REGEN, 2.5).putAttributes(ModAttributes.PASSIVE_REGEN, 15)
                    .npCost(80));

    public static final RegistryEntrySupplier<EntityType<EntityHeracles>> HERACLES = regServant(EntityType.Builder.of(EntityHeracles::new, MobCategory.MISC).sized(1.4f, 2.6f),
            new ResourceLocation(Fate.MODID, "heracles"), 0x3c1d06, 0x5e3c22,
            new ServantProperties.Builder(BuiltinServantClasses.BERSERKER)
                    .putAttributes(() -> Attributes.MAX_HEALTH, 200).putAttributes(() -> Attributes.ATTACK_DAMAGE, 10)
                    .putAttributes(() -> Attributes.ARMOR, 18)
                    .putAttributes(ModAttributes.PROJECTILE_RESISTANCE, 8).putAttributes(() -> Attributes.MOVEMENT_SPEED, 0.3)
                    .putAttributes(ModAttributes.PASSIVE_REGEN, 15)
                    .npCost(0));
    public static final RegistryEntrySupplier<EntityType<EntityLancelot>> LANCELOT = regServant(EntityType.Builder.of(EntityLancelot::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "lancelot"), 0x071a33, 0x1d4f94,
            new ServantProperties.Builder(BuiltinServantClasses.BERSERKER)
                    .putAttributes(() -> Attributes.MAX_HEALTH, 450).putAttributes(() -> Attributes.ATTACK_DAMAGE, 15)
                    .putAttributes(() -> Attributes.ARMOR, 15).putAttributes(ModAttributes.PROJECTILE_BLOCK_CHANCE, 0.1f)
                    .putAttributes(ModAttributes.PROJECTILE_RESISTANCE, 10)
                    .putAttributes(ModAttributes.MAGIC_RESISTANCE, 0.1).putAttributes(() -> Attributes.MOVEMENT_SPEED, 0.3)
                    .putAttributes(ModAttributes.COMBAT_REGEN, 2).putAttributes(ModAttributes.PASSIVE_REGEN, 15)
                    .npCost(0));

    public static final RegistryEntrySupplier<EntityType<EntityIskander>> ISKANDER = regServant(EntityType.Builder.of(EntityIskander::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "iskander"), 0xd40000, 0x8d0101,
            new ServantProperties.Builder(BuiltinServantClasses.RIDER)
                    .putAttributes(() -> Attributes.MAX_HEALTH, 450).putAttributes(() -> Attributes.ATTACK_DAMAGE, 12)
                    .putAttributes(() -> Attributes.ARMOR, 14)
                    .putAttributes(ModAttributes.PROJECTILE_RESISTANCE, 4).putAttributes(() -> Attributes.MOVEMENT_SPEED, 0.35)
                    .putAttributes(ModAttributes.COMBAT_REGEN, 1.5).putAttributes(ModAttributes.PASSIVE_REGEN, 15)
                    .npCost(70));
    public static final RegistryEntrySupplier<EntityType<EntityMedusa>> MEDUSA = regServant(EntityType.Builder.of(EntityMedusa::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "medusa"), 0x000000, 0xf234ea,
            new ServantProperties.Builder(BuiltinServantClasses.RIDER)
                    .putAttributes(() -> Attributes.MAX_HEALTH, 350).putAttributes(() -> Attributes.ATTACK_DAMAGE, 11)
                    .putAttributes(() -> Attributes.ARMOR, 12)
                    .putAttributes(ModAttributes.PROJECTILE_RESISTANCE, 6).putAttributes(() -> Attributes.MOVEMENT_SPEED, 0.36)
                    .putAttributes(ModAttributes.COMBAT_REGEN, 1.5).putAttributes(ModAttributes.PASSIVE_REGEN, 15)
                    .npCost(70));

    public static final RegistryEntrySupplier<EntityType<EntityHassan>> HASSAN = regServant(EntityType.Builder.of(EntityHassan::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "hassan"), 0x000000, 0x3a393a,
            new ServantProperties.Builder(BuiltinServantClasses.ASSASSIN)
                    .putAttributes(() -> Attributes.MAX_HEALTH, 320).putAttributes(() -> Attributes.ATTACK_DAMAGE, 10)
                    .putAttributes(() -> Attributes.ARMOR, 10)
                    .putAttributes(ModAttributes.PROJECTILE_RESISTANCE, 12).putAttributes(() -> Attributes.MOVEMENT_SPEED, 0.36)
                    .putAttributes(ModAttributes.COMBAT_REGEN, 2).putAttributes(ModAttributes.PASSIVE_REGEN, 15)
                    .npCost(40));
    public static final RegistryEntrySupplier<EntityType<EntitySasaki>> SASAKI = regServant(EntityType.Builder.of(EntitySasaki::new, MobCategory.MISC),
            new ResourceLocation(Fate.MODID, "sasaki"), 0x4e04c3, 0xa77cec,
            new ServantProperties.Builder(BuiltinServantClasses.ASSASSIN)
                    .putAttributes(() -> Attributes.MAX_HEALTH, 300).putAttributes(() -> Attributes.ATTACK_DAMAGE, 13)
                    .putAttributes(() -> Attributes.ARMOR, 10)
                    .putAttributes(ModAttributes.PROJECTILE_RESISTANCE, 5).putAttributes(() -> Attributes.MOVEMENT_SPEED, 0.33)
                    .putAttributes(ModAttributes.COMBAT_REGEN, 2).putAttributes(ModAttributes.PASSIVE_REGEN, 15)
                    .npCost(30));

    public static final RegistryEntrySupplier<EntityType<Excalibur>> EXCALIBUR = reg(EntityType.Builder.<Excalibur>of(Excalibur::new, MobCategory.MISC).sized(0.05F, 0.05F), new ResourceLocation(Fate.MODID, "excalibur"));
    public static final RegistryEntrySupplier<EntityType<GaeBolg>> GAEBOLG = reg(EntityType.Builder.<GaeBolg>of(GaeBolg::new, MobCategory.MISC).sized(0.25F, 0.25F), new ResourceLocation(Fate.MODID, "gae_bolg"));
    public static final RegistryEntrySupplier<EntityType<ArcherArrow>> ARCHER_ARROW = reg(EntityType.Builder.<ArcherArrow>of(ArcherArrow::new, MobCategory.MISC).sized(0.25F, 0.25F), new ResourceLocation(Fate.MODID, "archer_arrow"));
    public static final RegistryEntrySupplier<EntityType<CaladBolg>> CALADBOLG = reg(EntityType.Builder.<CaladBolg>of(CaladBolg::new, MobCategory.MISC).sized(0.25F, 0.25F), new ResourceLocation(Fate.MODID, "caladbolg"));
    public static final RegistryEntrySupplier<EntityType<BabylonWeapon>> BABYLON = reg(EntityType.Builder.<BabylonWeapon>of(BabylonWeapon::new, MobCategory.MISC).sized(0.25F, 0.25F), new ResourceLocation(Fate.MODID, "babylon"));
    public static final RegistryEntrySupplier<EntityType<EnumaElish>> EA = reg(EntityType.Builder.<EnumaElish>of(EnumaElish::new, MobCategory.MISC).sized(0.05F, 0.05F), new ResourceLocation(Fate.MODID, "ea"));
    public static final RegistryEntrySupplier<EntityType<MagicBeam>> MAGIC_BEAM = reg(EntityType.Builder.<MagicBeam>of(MagicBeam::new, MobCategory.MISC).sized(0.05F, 0.05F), new ResourceLocation(Fate.MODID, "magic_beam"));
    public static final RegistryEntrySupplier<EntityType<MagicBufCircle>> MEDEA_CIRCLE = reg(EntityType.Builder.of(MagicBufCircle::new, MobCategory.MISC), new ResourceLocation(Fate.MODID, "medeas_circle"));
    public static final RegistryEntrySupplier<EntityType<ThrownItemEntity>> THROWN_ITEM = reg(EntityType.Builder.<ThrownItemEntity>of(ThrownItemEntity::new, MobCategory.MISC).sized(0.25F, 0.25F), new ResourceLocation(Fate.MODID, "thrown_item"));
    public static final RegistryEntrySupplier<EntityType<ChainDagger>> DAGGER_HOOK = reg(EntityType.Builder.<ChainDagger>of(ChainDagger::new, MobCategory.MISC).updateInterval(5).sized(0.25F, 0.25F), new ResourceLocation(Fate.MODID, "medusas_dagger"));
    public static final RegistryEntrySupplier<EntityType<ThrownGem>> GEM = reg(EntityType.Builder.<ThrownGem>of(ThrownGem::new, MobCategory.MISC).sized(0.25F, 0.25F), new ResourceLocation(Fate.MODID, "thrown_gem"));
    public static final RegistryEntrySupplier<EntityType<MagicShot>> MAGIC_SHOT = reg(EntityType.Builder.<MagicShot>of(MagicShot::new, MobCategory.MISC).sized(0.25F, 0.25F), new ResourceLocation(Fate.MODID, "magic_shot"));

    public static final RegistryEntrySupplier<EntityType<LesserMonster>> LESSER_MONSTER = regWithEgg(EntityType.Builder.<LesserMonster>of(LesserMonster::new, MobCategory.MONSTER).clientTrackingRange(8),
            new ResourceLocation(Fate.MODID, "starfish_monster"), 0x171c3f, 0x00ff00,
            new AttributeHolderProperties.Builder()
                    .putAttributes(() -> Attributes.MAX_HEALTH, 30).putAttributes(() -> Attributes.ATTACK_DAMAGE, 11)
                    .putAttributes(() -> Attributes.ARMOR, 4).putAttributes(() -> Attributes.MOVEMENT_SPEED, 0.28)
                    .putAttributes(() -> Attributes.KNOCKBACK_RESISTANCE, 1));
    public static final RegistryEntrySupplier<EntityType<GordiusWheel>> GORDIUS_WHEEL = regWithEgg(EntityType.Builder.of(GordiusWheel::new, MobCategory.CREATURE).sized(2, 1.5f),
            new ResourceLocation(Fate.MODID, "gordius_wheel"), 0x87595c, 0x981a24,
            new AttributeHolderProperties.Builder()
                    .putAttributes(() -> Attributes.MAX_HEALTH, 120).putAttributes(() -> Attributes.ATTACK_DAMAGE, 15)
                    .putAttributes(() -> Attributes.ARMOR, 6).putAttributes(() -> Attributes.MOVEMENT_SPEED, 0.37)
                    .putAttributes(() -> Attributes.KNOCKBACK_RESISTANCE, 1));
    public static final RegistryEntrySupplier<EntityType<HassanClone>> HASSAN_COPY = hassanClone(new AttributeHolderProperties.Builder()
            .putAttributes(() -> Attributes.MAX_HEALTH, 50).putAttributes(() -> Attributes.ATTACK_DAMAGE, 7)
            .putAttributes(() -> Attributes.ARMOR, 8).putAttributes(ModAttributes.PROJECTILE_RESISTANCE, 10)
            .putAttributes(() -> Attributes.MOVEMENT_SPEED, 0.35));
    public static final RegistryEntrySupplier<EntityType<Pegasus>> PEGASUS = regWithEgg(EntityType.Builder.of(Pegasus::new, MobCategory.MONSTER).sized(1.35f, 1.65f),
            new ResourceLocation(Fate.MODID, "pegasus"), 0xffffff, 0xdde0e1,
            new AttributeHolderProperties.Builder()
                    .putAttributes(() -> Attributes.MAX_HEALTH, 80).putAttributes(() -> Attributes.ATTACK_DAMAGE, 17)
                    .putAttributes(() -> Attributes.ARMOR, 5).putAttributes(ModAttributes.PROJECTILE_BLOCK_CHANCE, 0.2)
                    .putAttributes(ModAttributes.MAGIC_RESISTANCE, 0.1).putAttributes(() -> Attributes.MOVEMENT_SPEED, 0.34)
                    .putAttributes(() -> Attributes.FLYING_SPEED, 0.85));

    public static final RegistryEntrySupplier<EntityType<MultiPartEntity>> MULTIPART = reg(EntityType.Builder.<MultiPartEntity>of(MultiPartEntity::new, MobCategory.MISC)
            .noSave().noSummon().sized(0.25F, 0.25F), new ResourceLocation(Fate.MODID, "multi_part"));

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <V extends BaseServant> RegistryEntrySupplier<EntityType<V>> regServant(EntityType.Builder<V> entity, ResourceLocation name, int primary, int secondary, ServantProperties.Builder props) {
        RegistryEntrySupplier<EntityType<V>> reg = reg(entity.clientTrackingRange(10), name);
        ModItems.ITEMS.register(name.getPath() + "_spawn_egg", () -> new FateEgg(reg, primary, secondary, new Item.Properties().tab(Fate.TAB)));
        if (Platform.INSTANCE.isDatagen()) {
            DEFAULT_SERVANT_PROPERTIES.put(name, props);
            SERVANTS.add((RegistryEntrySupplier) reg);
        }
        return reg;
    }

    public static <V extends Mob> RegistryEntrySupplier<EntityType<V>> regWithEgg(EntityType.Builder<V> entity, ResourceLocation name, int primary, int secondary, AttributeHolderProperties.Builder props) {
        RegistryEntrySupplier<EntityType<V>> reg = reg(entity.clientTrackingRange(10), name);
        ModItems.ITEMS.register(name.getPath() + "_spawn_egg", () -> new SpawnEgg(reg, primary, secondary, new Item.Properties().tab(Fate.TAB)));
        if (Platform.INSTANCE.isDatagen()) {
            DEFAULT_ENTITY_PROPERTIES.put(name, props);
        }
        return reg;
    }

    public static RegistryEntrySupplier<EntityType<HassanClone>> hassanClone(AttributeHolderProperties.Builder props) {
        RegistryEntrySupplier<EntityType<HassanClone>> reg = reg(EntityType.Builder.of(HassanClone::new, MobCategory.MISC), new ResourceLocation(Fate.MODID, "hassan_copy"));
        if (Platform.INSTANCE.isDatagen()) {
            DEFAULT_ENTITY_PROPERTIES.put(reg.getID(), props);
        }
        return reg;
    }

    private static <V extends Entity> RegistryEntrySupplier<EntityType<V>> reg(EntityType.Builder<V> v, ResourceLocation name) {
        return ENTITIES.register(name.getPath(), () -> v.build(name.getPath()));
    }

    public static Map<ResourceLocation, ServantProperties.Builder> getServantProperties() {
        return ImmutableMap.copyOf(DEFAULT_SERVANT_PROPERTIES);
    }

    public static Map<ResourceLocation, AttributeHolderProperties.Builder> getEntityProps() {
        return ImmutableMap.copyOf(DEFAULT_ENTITY_PROPERTIES);
    }

    public static List<RegistryEntrySupplier<EntityType<?>>> getServants() {
        return ImmutableList.copyOf(SERVANTS);
    }

    public static Map<EntityType<? extends LivingEntity>, AttributeSupplier.Builder> registeredAttributes() {
        Map<EntityType<? extends LivingEntity>, AttributeSupplier.Builder> map = new HashMap<>();
        map.put(ModEntities.ARTHUR.get(), BaseServant.createAttributes());

        map.put(ModEntities.CUCHULAINN.get(), BaseServant.createAttributes());
        map.put(ModEntities.DIARMUID.get(), BaseServant.createAttributes());

        map.put(ModEntities.EMIYA.get(), BaseServant.createAttributes());
        map.put(ModEntities.GILGAMESH.get(), BaseServant.createAttributes());

        map.put(ModEntities.GILLES.get(), BaseServant.createAttributes());
        map.put(ModEntities.MEDEA.get(), BaseServant.createAttributes());

        map.put(ModEntities.HERACLES.get(), BaseServant.createAttributes());
        map.put(ModEntities.LANCELOT.get(), BaseServant.createAttributes());

        map.put(ModEntities.MEDUSA.get(), BaseServant.createAttributes());
        map.put(ModEntities.ISKANDER.get(), BaseServant.createAttributes());

        map.put(ModEntities.HASSAN.get(), BaseServant.createAttributes());
        map.put(ModEntities.SASAKI.get(), BaseServant.createAttributes());

        map.put(ModEntities.LESSER_MONSTER.get(), BaseServant.createAttributes());
        map.put(ModEntities.GORDIUS_WHEEL.get(), BaseServant.createAttributes());
        map.put(ModEntities.PEGASUS.get(), BaseServant.createAttributes().add(Attributes.FLYING_SPEED, 0.85));
        map.put(ModEntities.HASSAN_COPY.get(), BaseServant.createAttributes());
        return map;
    }
}
