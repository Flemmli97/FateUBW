package com.flemmli97.fate.common.registry;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.config.Config;
import com.flemmli97.fate.common.config.ServantProperties;
import com.flemmli97.fate.common.entity.EntityArcherArrow;
import com.flemmli97.fate.common.entity.EntityBabylonWeapon;
import com.flemmli97.fate.common.entity.EntityCaladBolg;
import com.flemmli97.fate.common.entity.EntityCasterCircle;
import com.flemmli97.fate.common.entity.EntityEnumaElish;
import com.flemmli97.fate.common.entity.EntityExcalibur;
import com.flemmli97.fate.common.entity.EntityGaeBolg;
import com.flemmli97.fate.common.entity.EntityGem;
import com.flemmli97.fate.common.entity.EntityGordiusWheel;
import com.flemmli97.fate.common.entity.EntityHassanCopy;
import com.flemmli97.fate.common.entity.EntityLesserMonster;
import com.flemmli97.fate.common.entity.EntityMagicBeam;
import com.flemmli97.fate.common.entity.servant.EntityArthur;
import com.flemmli97.fate.common.entity.servant.EntityCuchulainn;
import com.flemmli97.fate.common.entity.servant.EntityDiarmuid;
import com.flemmli97.fate.common.entity.servant.EntityEmiya;
import com.flemmli97.fate.common.entity.servant.EntityGilgamesh;
import com.flemmli97.fate.common.entity.servant.EntityGilles;
import com.flemmli97.fate.common.entity.servant.EntityHassan;
import com.flemmli97.fate.common.entity.servant.EntityIskander;
import com.flemmli97.fate.common.entity.servant.EntityLancelot;
import com.flemmli97.fate.common.entity.servant.EntityMedea;
import com.flemmli97.fate.common.entity.servant.EntityServant;
import com.flemmli97.fate.common.items.FateEgg;
import com.flemmli97.fate.common.lib.LibEntities;
import com.flemmli97.fate.common.utils.EnumServantType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ModEntities {

    //Trying out deferred register with configs
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Fate.MODID);
    private static final Map<ResourceLocation, EnumServantType> servantTypeMap = Maps.newHashMap();

    //This is generic hell
    private static final EnumMap<EnumServantType, List<RegistryObject<?>>> typeServantsMap = new EnumMap<>(EnumServantType.class);

    public static RegistryObject<EntityType<EntityArthur>> arthur = regServant(EnumServantType.SABER, EntityType.Builder.create(EntityArthur::new, EntityClassification.MISC),
            LibEntities.arthur, 0x048dd0, 0xecee37, new ServantProperties(300, 10, 17, 0.4f, 12, 10, 0.3, 100));
    public static RegistryObject<EntityType<EntityCuchulainn>> cuchulainn = regServant(EnumServantType.LANCER, EntityType.Builder.create(EntityCuchulainn::new, EntityClassification.MISC),
            LibEntities.cuchulainn, 0x0038ff, 0xb6c0c1, new ServantProperties(275, 7.5, 10, 0, 14, 6, 0.35, 75));
    public static RegistryObject<EntityType<EntityDiarmuid>> diarmuid = regServant(EnumServantType.LANCER, EntityType.Builder.create(EntityDiarmuid::new, EntityClassification.MISC),
            LibEntities.diarmuid, 0x000000, 0x2a079a, new ServantProperties(310, 8.5, 12, 0, 13, 7, 0.35, 80));
    public static RegistryObject<EntityType<EntityEmiya>> emiya = regServant(EnumServantType.ARCHER, EntityType.Builder.create(EntityEmiya::new, EntityClassification.MISC),
            LibEntities.emiya, 0x9f0707, 0x000000, new ServantProperties(250, 7.5, 8, 0, 15.5, 7, 0.33, 66));
    public static RegistryObject<EntityType<EntityGilgamesh>> gilgamesh = regServant(EnumServantType.ARCHER, EntityType.Builder.create(EntityGilgamesh::new, EntityClassification.MISC),
            LibEntities.gilgamesh, 0xfff400, 0xffdb00, new ServantProperties(250, 10, 9, 0, 12.5, 5, 0.3, 100));
    public static RegistryObject<EntityType<EntityGilles>> gilles = regServant(EnumServantType.CASTER, EntityType.Builder.create(EntityGilles::new, EntityClassification.MISC),
            LibEntities.gilles, 0x100460, 0x600453, new ServantProperties(350, 5.5, 7, 0, 5, 14, 0.3, 80));
    public static RegistryObject<EntityType<EntityLancelot>> lancelot = regServant(EnumServantType.BERSERKER, EntityType.Builder.create(EntityLancelot::new, EntityClassification.MISC),
            LibEntities.lancelot, 0x071a33, 0x1d4f94, new ServantProperties(450, 9, 14, 0.4f, 19, 4, 0.2, 0));
    public static RegistryObject<EntityType<EntityHassan>> hassan = regServant(EnumServantType.ASSASSIN, EntityType.Builder.create(EntityHassan::new, EntityClassification.MISC),
            LibEntities.hassan, 0x000000, 0x3a393a, new ServantProperties(200, 6, 8.5, 0, 17, 4, 0.34, 15));
    public static RegistryObject<EntityType<EntityIskander>> iskander = regServant(EnumServantType.RIDER, EntityType.Builder.create(EntityIskander::new, EntityClassification.MISC),
            LibEntities.alexander, 0xd40000, 0x8d0101, new ServantProperties(400, 5.5, 10, 0, 9, 9.5, 0.3, 100));
    public static RegistryObject<EntityType<EntityMedea>> medea = regServant(EnumServantType.CASTER, EntityType.Builder.create(EntityMedea::new, EntityClassification.MISC),
            LibEntities.medea, 0x6f086b, 0x4a8be5, new ServantProperties(350, 9.5, 5, 0, 4, 17.5, 0.2, 100));

    public static final RegistryObject<EntityType<EntityExcalibur>> excalibur = reg(EntityType.Builder.<EntityExcalibur>create(EntityExcalibur::new, EntityClassification.MISC).size(0.25F, 0.25F), LibEntities.excalibur);
    public static final RegistryObject<EntityType<EntityGaeBolg>> gaebolg = reg(EntityType.Builder.<EntityGaeBolg>create(EntityGaeBolg::new, EntityClassification.MISC).size(0.25F, 0.25F), LibEntities.gae_bolg);
    public static final RegistryObject<EntityType<EntityArcherArrow>> archerArrow = reg(EntityType.Builder.<EntityArcherArrow>create(EntityArcherArrow::new, EntityClassification.MISC).size(0.25F, 0.25F), LibEntities.archer_arrow);
    public static final RegistryObject<EntityType<EntityCaladBolg>> caladbolg = reg(EntityType.Builder.<EntityCaladBolg>create(EntityCaladBolg::new, EntityClassification.MISC).size(0.25F, 0.25F), LibEntities.caladbolg);
    public static final RegistryObject<EntityType<EntityBabylonWeapon>> babylon = reg(EntityType.Builder.<EntityBabylonWeapon>create(EntityBabylonWeapon::new, EntityClassification.MISC).size(0.25F, 0.25F), LibEntities.babylon);
    public static final RegistryObject<EntityType<EntityEnumaElish>> ea = reg(EntityType.Builder.<EntityEnumaElish>create(EntityEnumaElish::new, EntityClassification.MISC).size(0.25F, 0.25F), LibEntities.ea);
    public static final RegistryObject<EntityType<EntityLesserMonster>> lesserMonster = reg(EntityType.Builder.<EntityLesserMonster>create(EntityLesserMonster::new, EntityClassification.MONSTER).maxTrackingRange(8), LibEntities.monster_small);
    public static final RegistryObject<EntityType<EntityGem>> gem = reg(EntityType.Builder.<EntityGem>create(EntityGem::new, EntityClassification.MISC).size(0.25F, 0.25F), LibEntities.entity_gem);
    public static final RegistryObject<EntityType<EntityHassanCopy>> hassanCopy = reg(EntityType.Builder.create(EntityHassanCopy::new, EntityClassification.MISC), LibEntities.hassan_copy);
    public static final RegistryObject<EntityType<EntityGordiusWheel>> gordiusWheel = reg(EntityType.Builder.create(EntityGordiusWheel::new, EntityClassification.MISC), LibEntities.gordius);
    public static final RegistryObject<EntityType<EntityCasterCircle>> medeaCircle = reg(EntityType.Builder.create(EntityCasterCircle::new, EntityClassification.MISC), LibEntities.medea_circle);
    public static final RegistryObject<EntityType<EntityMagicBeam>> magicBeam = reg(EntityType.Builder.create(EntityMagicBeam::new, EntityClassification.MISC), LibEntities.magic_beam);

    public static <V extends EntityServant> RegistryObject<EntityType<V>> regServant(EnumServantType type, EntityType.Builder<V> entity, ResourceLocation name, int primary, int secondary, ServantProperties defaultVals) {
        RegistryObject<EntityType<V>> reg = reg(entity.maxTrackingRange(10), name);
        servantTypeMap.put(name, type);
        typeServantsMap.merge(type, Lists.newArrayList(reg), (old, val) -> {
            old.add(reg);
            return old;
        });
        ModItems.ITEMS.register(name.getPath() + "_spawn_egg", () -> new FateEgg(reg, primary, secondary, new Item.Properties().group(Fate.TAB)));
        Config.Common.attributes.put(name.toString(), defaultVals);
        return reg;
    }

    private static <V extends Entity> RegistryObject<EntityType<V>> reg(EntityType.Builder<V> v, ResourceLocation name) {
        return ENTITIES.register(name.getPath(), () -> v.build(name.getPath()));
    }

    public static EnumServantType get(ResourceLocation type) {
        return servantTypeMap.getOrDefault(type, EnumServantType.NOTASSIGNED);
    }

    public static Collection<ResourceLocation> registeredServants() {
        return servantTypeMap.keySet();
    }

    @SuppressWarnings("unchecked")
    public static <V extends EntityServant> List<RegistryObject<EntityType<V>>> getFromType(EnumServantType type) {
        List<RegistryObject<EntityType<V>>> list = Lists.newArrayList();
        typeServantsMap.getOrDefault(type, Lists.newArrayList()).forEach(r -> list.add((RegistryObject<EntityType<V>>) r));
        return list;
    }

    public static void registerAttributes() {
        GlobalEntityTypeAttributes.put(arthur.get(), EntityServant.createMobAttributes().build());
        GlobalEntityTypeAttributes.put(cuchulainn.get(), EntityServant.createMobAttributes().build());
        GlobalEntityTypeAttributes.put(diarmuid.get(), EntityServant.createMobAttributes().build());
        GlobalEntityTypeAttributes.put(emiya.get(), EntityServant.createMobAttributes().build());
        GlobalEntityTypeAttributes.put(gilgamesh.get(), EntityServant.createMobAttributes().build());
        GlobalEntityTypeAttributes.put(gilles.get(), EntityServant.createMobAttributes().build());
        GlobalEntityTypeAttributes.put(lancelot.get(), EntityServant.createMobAttributes().build());
        GlobalEntityTypeAttributes.put(hassan.get(), EntityServant.createMobAttributes().build());

        GlobalEntityTypeAttributes.put(lesserMonster.get(), MonsterEntity.createHostileAttributes().build());
        GlobalEntityTypeAttributes.put(hassanCopy.get(), EntityServant.createMobAttributes().build());
    }
}
