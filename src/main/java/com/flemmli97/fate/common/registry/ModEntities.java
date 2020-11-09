package com.flemmli97.fate.common.registry;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.config.Config;
import com.flemmli97.fate.common.config.ServantProperties;
import com.flemmli97.fate.common.entity.EntityArcherArrow;
import com.flemmli97.fate.common.entity.EntityBabylonWeapon;
import com.flemmli97.fate.common.entity.EntityCaladBolg;
import com.flemmli97.fate.common.entity.EntityEnumaElish;
import com.flemmli97.fate.common.entity.EntityExcalibur;
import com.flemmli97.fate.common.entity.EntityGaeBolg;
import com.flemmli97.fate.common.entity.EntityLesserMonster;
import com.flemmli97.fate.common.entity.servant.EntityArthur;
import com.flemmli97.fate.common.entity.servant.EntityCuchulainn;
import com.flemmli97.fate.common.entity.servant.EntityServant;
import com.flemmli97.fate.common.lib.LibEntities;
import com.flemmli97.fate.common.utils.EnumServantType;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class ModEntities {

    //Trying out deferred register with configs
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Fate.MODID);
    private static final Map<ResourceLocation, EnumServantType> servants = Maps.newHashMap();

    public static RegistryObject<EntityType<EntityArthur>> arthur = regServant(EnumServantType.SABER, EntityType.Builder.create(EntityArthur::new, EntityClassification.MISC),
            LibEntities.arthur, new ServantProperties(300, 10, 17, 0.4f, 12, 10, 0.3f, 100));
    public static RegistryObject<EntityType<EntityCuchulainn>> cuchulainn = regServant(EnumServantType.LANCER, EntityType.Builder.create(EntityCuchulainn::new, EntityClassification.MISC),
            LibEntities.cuchulainn, new ServantProperties(275, 7.5, 10, 0, 14, 6, 0.35f, 75));

    public static final RegistryObject<EntityType<EntityExcalibur>> excalibur = reg(EntityType.Builder.<EntityExcalibur>create(EntityExcalibur::new, EntityClassification.MISC).size(0.25F, 0.25F), LibEntities.excalibur);
    public static final RegistryObject<EntityType<EntityEnumaElish>> ea = reg(EntityType.Builder.<EntityEnumaElish>create(EntityEnumaElish::new, EntityClassification.MISC).size(0.25F, 0.25F), LibEntities.ea);
    public static final RegistryObject<EntityType<EntityGaeBolg>> gaebolg = reg(EntityType.Builder.<EntityGaeBolg>create(EntityGaeBolg::new, EntityClassification.MISC).size(0.25F, 0.25F), LibEntities.gae_bolg);
    public static final RegistryObject<EntityType<EntityArcherArrow>> archerArrow = reg(EntityType.Builder.<EntityArcherArrow>create(EntityArcherArrow::new, EntityClassification.MISC).size(0.25F, 0.25F), LibEntities.archer_arrow);
    public static final RegistryObject<EntityType<EntityBabylonWeapon>> babylon = reg(EntityType.Builder.<EntityBabylonWeapon>create(EntityBabylonWeapon::new, EntityClassification.MISC).size(0.25F, 0.25F), LibEntities.babylon);
    public static final RegistryObject<EntityType<EntityCaladBolg>> caladbolg = reg(EntityType.Builder.<EntityCaladBolg>create(EntityCaladBolg::new, EntityClassification.MISC).size(0.25F, 0.25F), LibEntities.caladbolg);
    public static final RegistryObject<EntityType<EntityLesserMonster>> lesserMonster = reg(EntityType.Builder.<EntityLesserMonster>create(EntityLesserMonster::new, EntityClassification.MONSTER).size(0.25F, 0.25F), LibEntities.monster_small);

    private static <V extends EntityServant> RegistryObject<EntityType<V>> regServant(EnumServantType type, EntityType.Builder<V> entity, ResourceLocation name, ServantProperties defaultVals) {
        servants.put(name, type);
        Config.Common.attributes.put(name.toString(), defaultVals);
        return reg(entity.maxTrackingRange(10), name);
    }

    private static <V extends Entity> RegistryObject<EntityType<V>> reg(EntityType.Builder<V> v, ResourceLocation name) {
        return ENTITIES.register(name.getPath(), () -> v.build(name.getPath()));
    }

    public static EnumServantType get(ResourceLocation type) {
        return servants.getOrDefault(type, EnumServantType.NOTASSIGNED);
    }

    public static void registerAttributes() {
        GlobalEntityTypeAttributes.put(arthur.get(), EntityServant.createMobAttributes().build());
        GlobalEntityTypeAttributes.put(cuchulainn.get(), EntityServant.createMobAttributes().build());
    }
}
