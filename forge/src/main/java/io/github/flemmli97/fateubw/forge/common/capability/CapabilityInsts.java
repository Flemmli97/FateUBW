package io.github.flemmli97.fateubw.forge.common.capability;

import io.github.flemmli97.fateubw.common.attachment.ItemStackData;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CapabilityInsts {

    public static final Capability<PlayerData> PLAYERCAP = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<ItemStackData> ITEMSTACKCAP = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(PlayerData.class);
        event.register(ItemStackData.class);
    }
}
