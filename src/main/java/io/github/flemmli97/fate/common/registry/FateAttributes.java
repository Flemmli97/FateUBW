package io.github.flemmli97.fate.common.registry;

import io.github.flemmli97.fate.Fate;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Fate.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FateAttributes {

    public static Attribute MAGIC_RESISTANCE;
    public static Attribute PROJECTILE_RESISTANCE;
    public static Attribute PROJECTILE_BLOCKCHANCE;

    @SubscribeEvent
    public static void reg(RegistryEvent.Register<Attribute> event) {
        event.getRegistry().registerAll(
                MAGIC_RESISTANCE = new RangedAttribute("generic.magicResistance", 0, 0, 1).setRegistryName(Fate.MODID, "attr_magic_res"),
                PROJECTILE_RESISTANCE = new RangedAttribute("generic.projectileResistance", 0, 0, 30).setRegistryName(Fate.MODID, "attr_proj_res"),
                PROJECTILE_BLOCKCHANCE = new RangedAttribute("generic.projectileBlockChance", 0, 0, 1).setRegistryName(Fate.MODID, "attr_proj_block")
        );
    }
}
