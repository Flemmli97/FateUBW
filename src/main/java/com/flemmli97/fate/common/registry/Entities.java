package com.flemmli97.fate.common.registry;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.entity.servant.EntityArthur;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

@Mod.EventBusSubscriber(modid = Fate.MODID)
public class Entities {

    private static final Set<EntityType<? extends Entity>> servants = Sets.newHashSet();
    public static EntityType<EntityArthur> arthur;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<EntityType<?>> event){
        event.getRegistry().registerAll(
                arthur = EntityType.Builder.create(EntityArthur::new, EntityClassification.MISC).build("arthur").setRegistryName(new ResourceLocation(Fate.MODID, "arthur"))
        );
    }
}
