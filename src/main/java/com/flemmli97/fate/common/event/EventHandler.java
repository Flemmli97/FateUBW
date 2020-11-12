package com.flemmli97.fate.common.event;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.capability.PlayerCapProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Resource;

@Mod.EventBusSubscriber(modid = Fate.MODID)
public class EventHandler {

    public static final ResourceLocation cap = new ResourceLocation(Fate.MODID, "fate_cap");

    @SubscribeEvent
    public static void capability(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject() instanceof PlayerEntity)
            event.addCapability(cap, new PlayerCapProvider());
    }
}
