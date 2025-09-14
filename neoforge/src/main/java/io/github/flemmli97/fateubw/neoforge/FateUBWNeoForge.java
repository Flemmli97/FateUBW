package io.github.flemmli97.fateubw.neoforge;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.config.specs.ConfigLoader;
import io.github.flemmli97.fateubw.common.config.specs.ConfigSpecs;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.entity.servant.lancelot.LancelotAttackAI;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateRegistration;
import io.github.flemmli97.fateubw.neoforge.client.ClientEvents;
import io.github.flemmli97.fateubw.neoforge.event.EventHandler;
import io.github.flemmli97.fateubw.neoforge.network.PacketHandler;
import io.github.flemmli97.fateubw.neoforge.registry.FateAttachments;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@Mod(value = Fate.MODID)
public class FateUBWNeoForge {

    public FateUBWNeoForge(IEventBus modBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, ConfigSpecs.CLIENT_SPEC, Fate.MODID + "/client.toml");
        container.registerConfig(ModConfig.Type.COMMON, ConfigSpecs.COMMON_SPEC, Fate.MODID + "/common.toml");
        FateRegistration.registerContent();
        FateAttachments.ATTACHMENT_TYPES.register(modBus);
        modBus.addListener(this::configLoading);
        modBus.addListener(this::configReloading);
        modBus.addListener(this::attributes);
        modBus.addListener(PacketHandler::register);

        if (FMLLoader.getDist() == Dist.CLIENT)
            ClientEvents.register(modBus);
        IEventBus eventBus = NeoForge.EVENT_BUS;
        eventBus.register(EventHandler.class);
        eventBus.addListener(this::reloadListener);
        LancelotAttackAI.init();
    }

    public void configLoading(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == ConfigSpecs.COMMON_SPEC)
            ConfigLoader.loadCommon();
        if (event.getConfig().getSpec() == ConfigSpecs.CLIENT_SPEC)
            ConfigLoader.loadClient();
    }

    public void configReloading(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == ConfigSpecs.COMMON_SPEC)
            ConfigLoader.loadCommon();
        if (event.getConfig().getSpec() == ConfigSpecs.CLIENT_SPEC)
            ConfigLoader.loadClient();
    }

    public void reloadListener(AddReloadListenerEvent event) {
        DatapackHandler.addListeners(ext -> {
            ext.insertRegistryAccess(event.getServerResources().getRegistryLookup());
            event.addListener(ext);
        });
    }

    public void attributes(EntityAttributeCreationEvent event) {
        FateEntities.registeredAttributes()
                .forEach((type, builder) -> event.put(type, builder.build()));
    }
}
