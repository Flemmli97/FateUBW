package com.flemmli97.fate.common.registry;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.items.weapons.ItemExcalibur;
import com.flemmli97.fate.common.items.weapons.ItemGaeBolg;
import com.flemmli97.fate.common.lib.ItemTiers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Fate.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    public static Item invisexcalibur;
    public static Item excalibur;
    public static Item gaebolg;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                invisexcalibur = new SwordItem(ItemTiers.invis_excalibur, 0, -2.4f, new Item.Properties().group(Fate.TAB)){
                    @Override
                    public boolean hasEffect(ItemStack stack) {
                        return true;
                    }
                }.setRegistryName(Fate.MODID, "invis_excalibur"),
                excalibur = new ItemExcalibur(new Item.Properties().group(Fate.TAB)).setRegistryName(Fate.MODID, "excalibur"),
                gaebolg = new ItemGaeBolg(new Item.Properties().group(Fate.TAB)).setRegistryName(Fate.MODID, "gae_bolg")
        );
    }
}
