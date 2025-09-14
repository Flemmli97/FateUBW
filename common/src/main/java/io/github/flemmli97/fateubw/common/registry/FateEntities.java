package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.datapack.AttributeHolderProperties;
import io.github.flemmli97.fateubw.api.datapack.ServantExtraData;
import io.github.flemmli97.fateubw.api.datapack.ServantProperties;
import io.github.flemmli97.fateubw.common.entity.BaseServant;
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
import io.github.flemmli97.fateubw.common.entity.misc.StarfishShot;
import io.github.flemmli97.fateubw.common.entity.misc.ThrownGem;
import io.github.flemmli97.fateubw.common.entity.misc.ThrownItemEntity;
import io.github.flemmli97.fateubw.common.entity.servant.Arthur;
import io.github.flemmli97.fateubw.common.entity.servant.Cuchulainn;
import io.github.flemmli97.fateubw.common.entity.servant.Diarmuid;
import io.github.flemmli97.fateubw.common.entity.servant.Emiya;
import io.github.flemmli97.fateubw.common.entity.servant.Gilgamesh;
import io.github.flemmli97.fateubw.common.entity.servant.Gilles;
import io.github.flemmli97.fateubw.common.entity.servant.Hassan;
import io.github.flemmli97.fateubw.common.entity.servant.Heracles;
import io.github.flemmli97.fateubw.common.entity.servant.Iskander;
import io.github.flemmli97.fateubw.common.entity.servant.Medea;
import io.github.flemmli97.fateubw.common.entity.servant.Medusa;
import io.github.flemmli97.fateubw.common.entity.servant.Sasaki;
import io.github.flemmli97.fateubw.common.entity.servant.lancelot.Lancelot;
import io.github.flemmli97.fateubw.common.entity.summons.GordiusChariot;
import io.github.flemmli97.fateubw.common.entity.summons.GordiusWheel;
import io.github.flemmli97.fateubw.common.entity.summons.HassanClone;
import io.github.flemmli97.fateubw.common.entity.summons.LesserMonster;
import io.github.flemmli97.fateubw.common.entity.summons.Pegasus;
import io.github.flemmli97.fateubw.common.entity.summons.Tentacle;
import io.github.flemmli97.fateubw.common.items.FateEgg;
import io.github.flemmli97.fateubw.common.lib.BuiltinServantClasses;
import io.github.flemmli97.tenshilib.common.entity.MultiPartEntity;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.TenshiLibCrossPlat;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FateEntities {

    public static final LoaderRegister<EntityType<?>> ENTITIES = LoaderRegistryAccess.INSTANCE.of(Registries.ENTITY_TYPE, Fate.MODID);

    // Datagen stuff only
    public static final Map<ResourceLocation, ServantProperties.Builder> DEFAULT_SERVANT_PROPERTIES = new HashMap<>();
    public static final Map<ResourceLocation, String> SERVANT_NOBEL_PHANTASM = new HashMap<>();
    public static final Map<ResourceLocation, AttributeHolderProperties.Builder> DEFAULT_ENTITY_PROPERTIES = new HashMap<>();
    public static final List<RegistryEntrySupplier<EntityType<?>, EntityType<?>>> SERVANTS = new ArrayList<>();

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Arthur>> ARTHUR = regServant("artoria_pendragon_saber", "Excalibur", EntityType.Builder.of(Arthur::new, MobCategory.MISC)
                    .vehicleAttachment(new Vec3(0, 12 / 16d, 0)),
            0x048dd0, 0xecee37,
            new ServantProperties.Builder(BuiltinServantClasses.SABER)
                    .putAttributes(Attributes.MAX_HEALTH, 400).putAttributes(Attributes.ATTACK_DAMAGE, 15)
                    .putAttributes(Attributes.ARMOR, 19).putAttributes(Attributes.ARMOR_TOUGHNESS, 8)
                    .putAttributes(FateAttributes.PROJECTILE_BLOCK_CHANCE.asHolder(), 0.15f).putAttributes(FateAttributes.PROJECTILE_RESISTANCE.asHolder(), 16)
                    .putAttributes(FateAttributes.MAGIC_ATTACK.asHolder(), 12).putAttributes(FateAttributes.MAGIC_RESISTANCE.asHolder(), 14)
                    .putAttributes(Attributes.MOVEMENT_SPEED, 0.34)
                    .putAttributes(FateAttributes.COMBAT_REGEN.asHolder(), 1.5).putAttributes(FateAttributes.PASSIVE_REGEN.asHolder(), 10)
                    .npCost(80));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Cuchulainn>> CUCHULAINN = regServant("cu_chulainn_lancer", "Gae Bolg", EntityType.Builder.of(Cuchulainn::new, MobCategory.MISC)
                    .vehicleAttachment(new Vec3(0, 12 / 16d, 0)),
            0x0038ff, 0xb6c0c1,
            new ServantProperties.Builder(BuiltinServantClasses.LANCER)
                    .putAttributes(Attributes.MAX_HEALTH, 370).putAttributes(Attributes.ATTACK_DAMAGE, 13)
                    .putAttributes(Attributes.ARMOR, 17).putAttributes(Attributes.ARMOR_TOUGHNESS, 6)
                    .putAttributes(FateAttributes.PROJECTILE_BLOCK_CHANCE.asHolder(), 0.1f).putAttributes(FateAttributes.PROJECTILE_RESISTANCE.asHolder(), 12)
                    .putAttributes(FateAttributes.MAGIC_ATTACK.asHolder(), 17).putAttributes(FateAttributes.MAGIC_RESISTANCE.asHolder(), 10)
                    .putAttributes(Attributes.MOVEMENT_SPEED, 0.37)
                    .putAttributes(FateAttributes.COMBAT_REGEN.asHolder(), 1).putAttributes(FateAttributes.PASSIVE_REGEN.asHolder(), 10)
                    .npCost(50));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Diarmuid>> DIARMUID = regServant("diarmuid_ua_duibhne_lancer", "Gae Dearg/Buidhe", EntityType.Builder.of(Diarmuid::new, MobCategory.MISC)
                    .vehicleAttachment(new Vec3(0, 12 / 16d, 0)),
            0x2d5554, 0x302f34,
            new ServantProperties.Builder(BuiltinServantClasses.LANCER)
                    .putAttributes(Attributes.MAX_HEALTH, 380).putAttributes(Attributes.ATTACK_DAMAGE, 10)
                    .putAttributes(Attributes.ARMOR, 17.5).putAttributes(Attributes.ARMOR_TOUGHNESS, 5)
                    .putAttributes(FateAttributes.PROJECTILE_BLOCK_CHANCE.asHolder(), 0.1f).putAttributes(FateAttributes.PROJECTILE_RESISTANCE.asHolder(), 11)
                    .putAttributes(FateAttributes.MAGIC_RESISTANCE.asHolder(), 9)
                    .putAttributes(Attributes.MOVEMENT_SPEED, 0.37)
                    .putAttributes(FateAttributes.COMBAT_REGEN.asHolder(), 1).putAttributes(FateAttributes.PASSIVE_REGEN.asHolder(), 10)
                    .npCost(60));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Emiya>> EMIYA = regServant("emiya_archer", "Caladbolg", EntityType.Builder.of(Emiya::new, MobCategory.MISC)
                    .vehicleAttachment(new Vec3(0, 12 / 16d, 0)),
            0x9f0707, 0x000000,
            new ServantProperties.Builder(BuiltinServantClasses.ARCHER)
                    .putAttributes(Attributes.MAX_HEALTH, 350).putAttributes(Attributes.ATTACK_DAMAGE, 13)
                    .putAttributes(Attributes.ARMOR, 16).putAttributes(Attributes.ARMOR_TOUGHNESS, 6)
                    .putAttributes(FateAttributes.PROJECTILE_RESISTANCE.asHolder(), 17)
                    .putAttributes(FateAttributes.MAGIC_ATTACK.asHolder(), 14).putAttributes(FateAttributes.MAGIC_RESISTANCE.asHolder(), 12)
                    .putAttributes(Attributes.MOVEMENT_SPEED, 0.35)
                    .putAttributes(FateAttributes.COMBAT_REGEN.asHolder(), 1).putAttributes(FateAttributes.PASSIVE_REGEN.asHolder(), 10)
                    .npCost(50));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Gilgamesh>> GILGAMESH = regServant("gilgamesh_archer", "Gate of Babylon/EA", EntityType.Builder.of(Gilgamesh::new, MobCategory.MISC)
                    .vehicleAttachment(new Vec3(0, 12 / 16d, 0)),
            0xfff400, 0xffdb00,
            new ServantProperties.Builder(BuiltinServantClasses.ARCHER)
                    .putAttributes(Attributes.MAX_HEALTH, 430).putAttributes(Attributes.ATTACK_DAMAGE, 12)
                    .putAttributes(Attributes.ARMOR, 18).putAttributes(Attributes.ARMOR_TOUGHNESS, 12)
                    .putAttributes(FateAttributes.PROJECTILE_RESISTANCE.asHolder(), 11)
                    .putAttributes(FateAttributes.MAGIC_ATTACK.asHolder(), 10).putAttributes(FateAttributes.MAGIC_RESISTANCE.asHolder(), 16)
                    .putAttributes(Attributes.MOVEMENT_SPEED, 0.33)
                    .putAttributes(FateAttributes.COMBAT_REGEN.asHolder(), 1).putAttributes(FateAttributes.PASSIVE_REGEN.asHolder(), 10)
                    .npCost(80));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Medea>> MEDEA = regServant("medea_caster", "Rule Breaker", EntityType.Builder.of(Medea::new, MobCategory.MISC)
                    .vehicleAttachment(new Vec3(0, 12 / 16d, 0)),
            0x6f086b, 0x4a8be5,
            new ServantProperties.Builder(BuiltinServantClasses.CASTER)
                    .putAttributes(Attributes.MAX_HEALTH, 350).putAttributes(Attributes.ATTACK_DAMAGE, 9)
                    .putAttributes(Attributes.ARMOR, 14).putAttributes(Attributes.ARMOR_TOUGHNESS, 4)
                    .putAttributes(FateAttributes.PROJECTILE_RESISTANCE.asHolder(), 10)
                    .putAttributes(FateAttributes.MAGIC_ATTACK.asHolder(), 19).putAttributes(FateAttributes.MAGIC_RESISTANCE.asHolder(), 18)
                    .putAttributes(Attributes.MOVEMENT_SPEED, 0.31)
                    .putAttributes(FateAttributes.COMBAT_REGEN.asHolder(), 1.25).putAttributes(FateAttributes.PASSIVE_REGEN.asHolder(), 10)
                    .npCost(70)
                    .withConfigData(ServantExtraData.MEDEA_CIRCLE_DURATION)
                    .withConfigData(ServantExtraData.MEDEA_CIRCLE_RANGE));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Gilles>> GILLES = regServant("gilles_de_rais_caster", "Prelati's Spellbook", EntityType.Builder.of(Gilles::new, MobCategory.MISC)
                    .vehicleAttachment(new Vec3(0, 12 / 16d, 0)),
            0x100460, 0x600453,
            new ServantProperties.Builder(BuiltinServantClasses.CASTER)
                    .putAttributes(Attributes.MAX_HEALTH, 370).putAttributes(Attributes.ATTACK_DAMAGE, 5)
                    .putAttributes(Attributes.ARMOR, 16).putAttributes(Attributes.ARMOR_TOUGHNESS, 6)
                    .putAttributes(FateAttributes.PROJECTILE_RESISTANCE.asHolder(), 12)
                    .putAttributes(FateAttributes.MAGIC_ATTACK.asHolder(), 15).putAttributes(FateAttributes.MAGIC_RESISTANCE.asHolder(), 12)
                    .putAttributes(Attributes.MOVEMENT_SPEED, 0.32)
                    .putAttributes(FateAttributes.COMBAT_REGEN.asHolder(), 1.25).putAttributes(FateAttributes.PASSIVE_REGEN.asHolder(), 10)
                    .npCost(80)
                    .withConfigData(ServantExtraData.GILLES_MONSTER_DURATION)
                    .withConfigData(ServantExtraData.GILLES_MONSTER_MAX)
                    .withConfigData(ServantExtraData.GILLES_TENTACLE_DAMAGE));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Heracles>> HERACLES = regServant("heracles_berserker", "God Hand", EntityType.Builder.of(Heracles::new, MobCategory.MISC)
                    .sized(1.4f, 2.6f),
            0x3c1d06, 0x5e3c22,
            new ServantProperties.Builder(BuiltinServantClasses.BERSERKER)
                    .putAttributes(Attributes.MAX_HEALTH, 250).putAttributes(Attributes.ATTACK_DAMAGE, 10)
                    .putAttributes(Attributes.ARMOR, 18)
                    .putAttributes(FateAttributes.PROJECTILE_RESISTANCE.asHolder(), 8)
                    .putAttributes(Attributes.MOVEMENT_SPEED, 0.3)
                    .putAttributes(FateAttributes.PASSIVE_REGEN.asHolder(), 10)
                    .npCost(0)
                    .withConfigData(ServantExtraData.HERACLES_DEATH_MAX));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Lancelot>> LANCELOT = regServant("lancelot_berserker", "Knight of Owner", EntityType.Builder.of(Lancelot::new, MobCategory.MISC)
                    .vehicleAttachment(new Vec3(0, 12 / 16d, 0)),
            0x071a33, 0x1d4f94,
            new ServantProperties.Builder(BuiltinServantClasses.BERSERKER)
                    .putAttributes(Attributes.MAX_HEALTH, 450).putAttributes(Attributes.ATTACK_DAMAGE, 15)
                    .putAttributes(Attributes.ARMOR, 15).putAttributes(FateAttributes.PROJECTILE_BLOCK_CHANCE.asHolder(), 0.1f)
                    .putAttributes(FateAttributes.PROJECTILE_RESISTANCE.asHolder(), 10)
                    .putAttributes(FateAttributes.MAGIC_RESISTANCE.asHolder(), 0.1)
                    .putAttributes(Attributes.MOVEMENT_SPEED, 0.3)
                    .putAttributes(FateAttributes.COMBAT_REGEN.asHolder(), 1).putAttributes(FateAttributes.PASSIVE_REGEN.asHolder(), 10)
                    .npCost(0)
                    .withConfigData(ServantExtraData.LANCELOT_REFLECT_CHANCE));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Iskander>> ISKANDER = regServant("iskander_rider", "Gordius Bulls", EntityType.Builder.of(Iskander::new, MobCategory.MISC)
                    .vehicleAttachment(new Vec3(0, 12 / 16d, 0)),
            0xd40000, 0x8d0101,
            new ServantProperties.Builder(BuiltinServantClasses.RIDER)
                    .putAttributes(Attributes.MAX_HEALTH, 450).putAttributes(Attributes.ATTACK_DAMAGE, 12)
                    .putAttributes(Attributes.ARMOR, 14)
                    .putAttributes(FateAttributes.PROJECTILE_RESISTANCE.asHolder(), 4).putAttributes(Attributes.MOVEMENT_SPEED, 0.35)
                    .putAttributes(FateAttributes.COMBAT_REGEN.asHolder(), 1.5).putAttributes(FateAttributes.PASSIVE_REGEN.asHolder(), 10)
                    .npCost(70));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Medusa>> MEDUSA = regServant("medusa_rider", "Bellerophon", EntityType.Builder.of(Medusa::new, MobCategory.MISC)
                    .vehicleAttachment(new Vec3(0, 12 / 16d, 0)),
            0x000000, 0xf234ea,
            new ServantProperties.Builder(BuiltinServantClasses.RIDER)
                    .putAttributes(Attributes.MAX_HEALTH, 350).putAttributes(Attributes.ATTACK_DAMAGE, 11)
                    .putAttributes(Attributes.ARMOR, 12)
                    .putAttributes(FateAttributes.PROJECTILE_RESISTANCE.asHolder(), 6).putAttributes(Attributes.MOVEMENT_SPEED, 0.36)
                    .putAttributes(FateAttributes.COMBAT_REGEN.asHolder(), 1.5).putAttributes(FateAttributes.PASSIVE_REGEN.asHolder(), 10)
                    .npCost(70));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Hassan>> HASSAN = regServant("hassan-i-sabbah_assassin", "Delusional Illusion", EntityType.Builder.of(Hassan::new, MobCategory.MISC)
                    .vehicleAttachment(new Vec3(0, 12 / 16d, 0)),
            0x000000, 0x3a393a,
            new ServantProperties.Builder(BuiltinServantClasses.ASSASSIN)
                    .putAttributes(Attributes.MAX_HEALTH, 320).putAttributes(Attributes.ATTACK_DAMAGE, 11)
                    .putAttributes(Attributes.ARMOR, 12).putAttributes(Attributes.ARMOR_TOUGHNESS, 2)
                    .putAttributes(FateAttributes.PROJECTILE_RESISTANCE.asHolder(), 14).putAttributes(FateAttributes.MAGIC_RESISTANCE.asHolder(), 8)
                    .putAttributes(Attributes.MOVEMENT_SPEED, 0.36)
                    .putAttributes(FateAttributes.COMBAT_REGEN.asHolder(), 1).putAttributes(FateAttributes.PASSIVE_REGEN.asHolder(), 10)
                    .npCost(40)
                    .withConfigData(ServantExtraData.HASSAN_COPIES));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Sasaki>> SASAKI = regServant("sasaki_kojiro_assassin", "Tsubame Gaeshi", EntityType.Builder.of(Sasaki::new, MobCategory.MISC)
                    .vehicleAttachment(new Vec3(0, 12 / 16d, 0)),
            0x4e04c3, 0xa77cec,
            new ServantProperties.Builder(BuiltinServantClasses.ASSASSIN)
                    .putAttributes(Attributes.MAX_HEALTH, 300).putAttributes(Attributes.ATTACK_DAMAGE, 13)
                    .putAttributes(Attributes.ARMOR, 15).putAttributes(Attributes.ARMOR_TOUGHNESS, 4)
                    .putAttributes(FateAttributes.PROJECTILE_RESISTANCE.asHolder(), 8).putAttributes(FateAttributes.MAGIC_RESISTANCE.asHolder(), 10)
                    .putAttributes(Attributes.MOVEMENT_SPEED, 0.33)
                    .putAttributes(FateAttributes.COMBAT_REGEN.asHolder(), 1).putAttributes(FateAttributes.PASSIVE_REGEN.asHolder(), 10)
                    .npCost(30));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Excalibur>> EXCALIBUR = reg("excalibur", EntityType.Builder.<Excalibur>of(Excalibur::new, MobCategory.MISC).sized(0.05F, 0.05F));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<GaeBolg>> GAEBOLG = reg("gae_bolg", EntityType.Builder.<GaeBolg>of(GaeBolg::new, MobCategory.MISC).sized(0.25F, 0.25F));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<ArcherArrow>> ARCHER_ARROW = reg("archer_arrow", EntityType.Builder.<ArcherArrow>of(ArcherArrow::new, MobCategory.MISC).sized(0.25F, 0.25F));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<CaladBolg>> CALADBOLG = reg("caladbolg", EntityType.Builder.<CaladBolg>of(CaladBolg::new, MobCategory.MISC).sized(0.25F, 0.25F));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<BabylonWeapon>> BABYLON = reg("babylon", EntityType.Builder.<BabylonWeapon>of(BabylonWeapon::new, MobCategory.MISC).sized(0.25F, 0.25F));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EnumaElish>> EA = reg("ea", EntityType.Builder.<EnumaElish>of(EnumaElish::new, MobCategory.MISC).sized(0.05F, 0.05F));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<MagicBeam>> MAGIC_BEAM = reg("magic_beam", EntityType.Builder.<MagicBeam>of(MagicBeam::new, MobCategory.MISC).sized(0.05F, 0.05F));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<MagicBufCircle>> MEDEA_CIRCLE = reg("medeas_circle", EntityType.Builder.of(MagicBufCircle::new, MobCategory.MISC));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<ThrownItemEntity>> THROWN_ITEM = reg("thrown_item", EntityType.Builder.<ThrownItemEntity>of(ThrownItemEntity::new, MobCategory.MISC).sized(0.25F, 0.25F));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<ChainDagger>> DAGGER_HOOK = reg("medusas_dagger", EntityType.Builder.<ChainDagger>of(ChainDagger::new, MobCategory.MISC).updateInterval(5).sized(0.25F, 0.25F));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<ThrownGem>> GEM = reg("thrown_gem", EntityType.Builder.<ThrownGem>of(ThrownGem::new, MobCategory.MISC).sized(0.25F, 0.25F));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<MagicShot>> MAGIC_SHOT = reg("magic_shot", EntityType.Builder.<MagicShot>of(MagicShot::new, MobCategory.MISC).sized(0.25F, 0.25F));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Tentacle>> TENTACLE = reg("tentacle", EntityType.Builder.of(Tentacle::new, MobCategory.MISC).sized(0.05F, 0.05F));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<StarfishShot>> STARFISH_SHOT = reg("starfish_shot", EntityType.Builder.<StarfishShot>of(StarfishShot::new, MobCategory.MISC).sized(0.25F, 0.25F));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<LesserMonster>> LESSER_MONSTER = regWithEgg("starfish_monster", EntityType.Builder.<LesserMonster>of(LesserMonster::new, MobCategory.MONSTER).clientTrackingRange(8),
            0x171c3f, 0x00ff00,
            new AttributeHolderProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 28).putAttributes(Attributes.ATTACK_DAMAGE, 11)
                    .putAttributes(Attributes.ARMOR, 6).putAttributes(Attributes.MOVEMENT_SPEED, 0.28)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<GordiusWheel>> GORDIUS_WHEEL = regWithEgg("gordius_wheel", EntityType.Builder.of(GordiusWheel::new, MobCategory.CREATURE).sized(2, 1.5f),
            0x87595c, 0x981a24,
            new AttributeHolderProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 120).putAttributes(Attributes.ATTACK_DAMAGE, 15)
                    .putAttributes(Attributes.ARMOR, 6)
                    .putAttributes(FateAttributes.PROJECTILE_RESISTANCE.asHolder(), 6).putAttributes(FateAttributes.MAGIC_RESISTANCE.asHolder(), 4)
                    .putAttributes(Attributes.MOVEMENT_SPEED, 0.34)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<HassanClone>> HASSAN_COPY = hassanClone(new AttributeHolderProperties.Builder()
            .putAttributes(Attributes.MAX_HEALTH, 50).putAttributes(Attributes.ATTACK_DAMAGE, 8)
            .putAttributes(Attributes.ARMOR, 10).putAttributes(Attributes.ARMOR_TOUGHNESS, 2)
            .putAttributes(FateAttributes.PROJECTILE_RESISTANCE.asHolder(), 10).putAttributes(FateAttributes.MAGIC_RESISTANCE.asHolder(), 6)
            .putAttributes(Attributes.MOVEMENT_SPEED, 0.35));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Pegasus>> PEGASUS = regWithEgg("pegasus", EntityType.Builder.of(Pegasus::new, MobCategory.MONSTER).sized(1.35f, 1.65f),
            0xffffff, 0xdde0e1,
            new AttributeHolderProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 80).putAttributes(Attributes.ATTACK_DAMAGE, 17)
                    .putAttributes(Attributes.ARMOR, 7).putAttributes(FateAttributes.PROJECTILE_BLOCK_CHANCE.asHolder(), 0.2)
                    .putAttributes(FateAttributes.PROJECTILE_RESISTANCE.asHolder(), 6).putAttributes(FateAttributes.MAGIC_RESISTANCE.asHolder(), 14)
                    .putAttributes(Attributes.MOVEMENT_SPEED, 0.34)
                    .putAttributes(Attributes.FLYING_SPEED, 0.85));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<MultiPartEntity>> MULTIPART = reg("multi_part", EntityType.Builder.<MultiPartEntity>of(MultiPartEntity::new, MobCategory.MISC)
            .noSave().noSummon().sized(0.25F, 0.25F));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<GordiusChariot>> GORDIUS_CHARIOT = reg("gordius_chariot", EntityType.Builder.<GordiusChariot>of(GordiusChariot::new, MobCategory.MISC)
            .noSave().noSummon().sized(0.25F, 0.25F));

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static <V extends BaseServant> RegistryEntrySupplier<EntityType<?>, EntityType<V>> regServant(String name, String hogou, EntityType.Builder<V> entity, int primary, int secondary, ServantProperties.Builder props) {
        RegistryEntrySupplier<EntityType<?>, EntityType<V>> reg = reg(name, entity.clientTrackingRange(10));
        FateCreativeTab.addToTab(FateItems.ITEMS.register(name + "_spawn_egg", () -> new FateEgg(reg, primary, secondary, new Item.Properties())));
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            DEFAULT_SERVANT_PROPERTIES.put(reg.getID(), props);
            SERVANTS.add((RegistryEntrySupplier) reg);
            SERVANT_NOBEL_PHANTASM.put(reg.getID(), hogou);
        }
        return reg;
    }

    private static <V extends Mob> RegistryEntrySupplier<EntityType<?>, EntityType<V>> regWithEgg(String name, EntityType.Builder<V> entity, int primary, int secondary, AttributeHolderProperties.Builder props) {
        RegistryEntrySupplier<EntityType<?>, EntityType<V>> reg = reg(name, entity.clientTrackingRange(10));
        FateCreativeTab.addToTab(FateItems.ITEMS.register(name + "_spawn_egg", () -> new SpawnEgg(reg, primary, secondary, new Item.Properties())));
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            DEFAULT_ENTITY_PROPERTIES.put(reg.getID(), props);
        }
        return reg;
    }

    private static RegistryEntrySupplier<EntityType<?>, EntityType<HassanClone>> hassanClone(AttributeHolderProperties.Builder props) {
        RegistryEntrySupplier<EntityType<?>, EntityType<HassanClone>> reg = reg("hassan_copy", EntityType.Builder.of(HassanClone::new, MobCategory.MISC));
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            DEFAULT_ENTITY_PROPERTIES.put(reg.getID(), props);
        }
        return reg;
    }

    private static <V extends Entity> RegistryEntrySupplier<EntityType<?>, EntityType<V>> reg(String name, EntityType.Builder<V> v) {
        return ENTITIES.register(name, () -> v.build(name));
    }

    public static Map<EntityType<? extends LivingEntity>, AttributeSupplier.Builder> registeredAttributes() {
        Map<EntityType<? extends LivingEntity>, AttributeSupplier.Builder> map = new HashMap<>();
        map.put(FateEntities.ARTHUR.get(), BaseServant.createAttributes());

        map.put(FateEntities.CUCHULAINN.get(), BaseServant.createAttributes());
        map.put(FateEntities.DIARMUID.get(), BaseServant.createAttributes());

        map.put(FateEntities.EMIYA.get(), BaseServant.createAttributes());
        map.put(FateEntities.GILGAMESH.get(), BaseServant.createAttributes());

        map.put(FateEntities.GILLES.get(), BaseServant.createAttributes());
        map.put(FateEntities.MEDEA.get(), BaseServant.createAttributes());

        map.put(FateEntities.HERACLES.get(), BaseServant.createAttributes());
        map.put(FateEntities.LANCELOT.get(), BaseServant.createAttributes());

        map.put(FateEntities.MEDUSA.get(), BaseServant.createAttributes());
        map.put(FateEntities.ISKANDER.get(), BaseServant.createAttributes());

        map.put(FateEntities.HASSAN.get(), BaseServant.createAttributes());
        map.put(FateEntities.SASAKI.get(), BaseServant.createAttributes());

        map.put(FateEntities.LESSER_MONSTER.get(), BaseServant.createAttributes());
        map.put(FateEntities.GORDIUS_WHEEL.get(), GordiusWheel.createAttributes());
        map.put(FateEntities.PEGASUS.get(), BaseServant.createAttributes().add(Attributes.FLYING_SPEED, 0.85));
        map.put(FateEntities.HASSAN_COPY.get(), BaseServant.createAttributes());
        return map;
    }
}
