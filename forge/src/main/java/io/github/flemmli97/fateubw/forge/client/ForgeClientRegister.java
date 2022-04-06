package io.github.flemmli97.fateubw.forge.client;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientRegister;
import io.github.flemmli97.fateubw.client.ItemModelProps;
import io.github.flemmli97.fateubw.client.render.RenderAltar;
import io.github.flemmli97.fateubw.common.registry.ModBlocks;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Function;

public class ForgeClientRegister {

    public static void clientSetup(FMLClientSetupEvent event) {
        ClientRegister.setupRenderLayers(ItemBlockRenderTypes::setRenderLayer);

        ClientRegister.registerKeyBinding(ClientRegistry::registerKeyBinding);

        BlockEntityRenderers.register(ModBlocks.tileAltar.get(), RenderAltar::new);
        event.enqueueWork(() -> {
            ItemProperties.register(ModItems.excalibur.get(), new ResourceLocation(Fate.MODID, "active"), ItemModelProps.activeItemProp);
            ItemProperties.register(ModItems.medusaDagger.get(), new ResourceLocation(Fate.MODID, "thrown"), ItemModelProps.thrownDaggerProp);
        });
    }

    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        ClientRegister.registerRenderers(event::registerEntityRenderer);
    }

    public static void layerRegister(EntityRenderersEvent.RegisterLayerDefinitions event) {
        ClientRegister.layerRegister(event::registerLayerDefinition);
    }

    public static void registerParticles(ParticleFactoryRegisterEvent event) {
        ParticleEngine manager = Minecraft.getInstance().particleEngine;
        ClientRegister.registerParticles(new ClientRegister.PartileRegister() {
            @Override
            public <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> provider) {
                manager.register(type, provider::apply);
            }
        });
    }
}
