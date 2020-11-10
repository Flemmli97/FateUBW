package com.flemmli97.fate.common.registry;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.items.weapons.ClassSpear;
import com.flemmli97.fate.common.items.weapons.ItemArcherBow;
import com.flemmli97.fate.common.items.weapons.ItemEA;
import com.flemmli97.fate.common.items.weapons.ItemExcalibur;
import com.flemmli97.fate.common.items.weapons.ItemGaeBolg;
import com.flemmli97.fate.common.items.weapons.ItemGrimoire;
import com.flemmli97.fate.common.lib.ItemTiers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;
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

    public static Item icon0;
    public static Item icon1;
    public static Item icon2;
    public static Item icon3;

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

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Item> event) {
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

                icon0 = new Item(new Item.Properties()).setRegistryName(Fate.MODID, "icon_0"),
                icon1 = new Item(new Item.Properties()).setRegistryName(Fate.MODID, "icon_1"),
                icon2 = new Item(new Item.Properties()).setRegistryName(Fate.MODID, "icon_2"),
                icon3 = new Item(new Item.Properties()).setRegistryName(Fate.MODID, "icon_3")
        );
    }
}
