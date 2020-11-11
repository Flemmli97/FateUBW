package com.flemmli97.fate.common.registry;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.items.ItemChalk;
import com.flemmli97.fate.common.items.ItemCrystal;
import com.flemmli97.fate.common.items.ItemServantCharm;
import com.flemmli97.fate.common.items.weapons.ClassSpear;
import com.flemmli97.fate.common.items.weapons.ItemArcherBow;
import com.flemmli97.fate.common.items.weapons.ItemEA;
import com.flemmli97.fate.common.items.weapons.ItemExcalibur;
import com.flemmli97.fate.common.items.weapons.ItemGaeBolg;
import com.flemmli97.fate.common.items.weapons.ItemGrimoire;
import com.flemmli97.fate.common.lib.ItemTiers;
import com.flemmli97.fate.common.utils.EnumServantType;
import com.google.common.collect.Lists;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Random;
import java.util.ServiceConfigurationError;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Fate.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    public static Item invisexcalibur;
    public static Item excalibur;
    public static Item gaebolg;
    public static Item gaedearg;
    public static Item gaebuidhe;
    public static Item kanshou;
    public static Item bakuya;
    public static Item archbow;
    public static Item enumaelish;
    public static Item grimoire;
    public static Item arondight;

    public static Item charmNone;
    public static Item charmSaber;
    public static Item charmArcher;
    public static Item charmLancer;
    public static Item charmRider;
    public static Item charmBerserker;
    public static Item charmCaster;
    public static Item charmAssassin;
    public static Item crystalFire;
    public static Item crystalWind;
    public static Item crystalEarth;
    public static Item crystalWater;
    public static Item crystalVoid;
    public static Item crystalCluster;
    public static Item chalk;

    public static Item icon0;
    public static Item icon1;
    public static Item icon2;
    public static Item icon3;

    public static Item altar;

    public static final Supplier<Item> randomIcon = () -> {
        int i = new Random().nextInt(4);
        switch (i) {
            case 0:
                return icon0;
            case 1:
                return icon1;
            case 2:
                return icon2;
        }
        return icon3;
    };

    public static final List<Item> charms = Lists.newArrayList();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Item> event) {
        charms.clear();
        event.getRegistry().registerAll(
                invisexcalibur = new SwordItem(ItemTiers.invis_excalibur, 0, -2.4f, new Item.Properties()) {
                    @Override
                    public boolean hasEffect(ItemStack stack) {
                        return true;
                    }
                }.setRegistryName(Fate.MODID, "invis_excalibur"),
                excalibur = new ItemExcalibur(new Item.Properties().group(Fate.TAB)).setRegistryName(Fate.MODID, "excalibur"),
                gaebolg = new ItemGaeBolg(new Item.Properties().group(Fate.TAB)).setRegistryName(Fate.MODID, "gae_bolg"),
                gaedearg = new ClassSpear(ItemTiers.gae_dearg, new Item.Properties().group(Fate.TAB), -1.5f, 3.5f).setRegistryName(Fate.MODID, "gae_dearg"),
                gaebuidhe = new ClassSpear(ItemTiers.gae_buidhe, new Item.Properties().group(Fate.TAB), -1.5f, 3.5f).setRegistryName(Fate.MODID, "gae_buidhe"),
                kanshou = new SwordItem(ItemTiers.kanshou, 0, -2f, new Item.Properties().group(Fate.TAB)).setRegistryName(Fate.MODID, "kanshou"),
                bakuya = new SwordItem(ItemTiers.bakuya, 0, -2f, new Item.Properties().group(Fate.TAB)).setRegistryName(Fate.MODID, "bakuya"),
                archbow = new ItemArcherBow(new Item.Properties().group(Fate.TAB)).setRegistryName(Fate.MODID, "archer_bow"),
                enumaelish = new ItemEA(new Item.Properties().group(Fate.TAB)).setRegistryName(Fate.MODID, "enuma_elish"),
                grimoire = new ItemGrimoire(new Item.Properties().group(Fate.TAB)).setRegistryName(Fate.MODID, "prelati"),
                arondight = new SwordItem(ItemTiers.arondight, 0, -2.4f, new Item.Properties().group(Fate.TAB)).setRegistryName(Fate.MODID, "arondight"),

                charmNone = registercharm(new ItemServantCharm(EnumServantType.NOTASSIGNED, new Item.Properties().group(Fate.TAB))),
                charmSaber = registercharm(new ItemServantCharm(EnumServantType.SABER, new Item.Properties().group(Fate.TAB))),
                charmArcher = registercharm(new ItemServantCharm(EnumServantType.ARCHER, new Item.Properties().group(Fate.TAB))),
                charmLancer = registercharm(new ItemServantCharm(EnumServantType.LANCER, new Item.Properties().group(Fate.TAB))),
                charmRider = registercharm(new ItemServantCharm(EnumServantType.RIDER, new Item.Properties().group(Fate.TAB))),
                charmBerserker = registercharm(new ItemServantCharm(EnumServantType.BERSERKER, new Item.Properties().group(Fate.TAB))),
                charmCaster = registercharm(new ItemServantCharm(EnumServantType.CASTER, new Item.Properties().group(Fate.TAB))),
                charmAssassin = registercharm(new ItemServantCharm(EnumServantType.ASSASSIN, new Item.Properties().group(Fate.TAB))),

                crystalFire = new Item(new Item.Properties().group(Fate.TAB)).setRegistryName(Fate.MODID, "gem_shard_fire"),
                crystalWater = new Item(new Item.Properties().group(Fate.TAB)).setRegistryName(Fate.MODID, "gem_shard_water"),
                crystalWind = new Item(new Item.Properties().group(Fate.TAB)).setRegistryName(Fate.MODID, "gem_shard_wind"),
                crystalEarth = new Item(new Item.Properties().group(Fate.TAB)).setRegistryName(Fate.MODID, "gem_shard_earth"),
                crystalVoid = new Item(new Item.Properties().group(Fate.TAB)).setRegistryName(Fate.MODID, "gem_shard_void"),
                crystalCluster = new ItemCrystal(new Item.Properties().group(Fate.TAB)).setRegistryName(Fate.MODID, "gem_cluster"),
                chalk = new ItemChalk(new Item.Properties().group(Fate.TAB)).setRegistryName(Fate.MODID, "chalk"),

                icon0 = new Item(new Item.Properties()).setRegistryName(Fate.MODID, "icon_0"),
                icon1 = new Item(new Item.Properties()).setRegistryName(Fate.MODID, "icon_1"),
                icon2 = new Item(new Item.Properties()).setRegistryName(Fate.MODID, "icon_2"),
                icon3 = new Item(new Item.Properties()).setRegistryName(Fate.MODID, "icon_3"),

                altar = new BlockItem(ModBlocks.altar.get(), new Item.Properties().group(Fate.TAB)).setRegistryName(Fate.MODID, "altar")
        );
    }

    private static Item registercharm(ItemServantCharm item){
        item.setRegistryName(Fate.MODID, "servant_medallion_"+item.type.getLowercase());
        charms.add(item);
        return item;
    }
}
