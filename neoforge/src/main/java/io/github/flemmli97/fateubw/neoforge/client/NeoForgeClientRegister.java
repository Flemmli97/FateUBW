package io.github.flemmli97.fateubw.neoforge.client;

import io.github.flemmli97.fateubw.client.ClientRegister;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import io.github.flemmli97.fateubw.client.render.RenderAltar;
import io.github.flemmli97.fateubw.client.render.item.RenderEAItem;
import io.github.flemmli97.fateubw.client.render.item.RenderExcaliburItem;
import io.github.flemmli97.fateubw.common.registry.FateBlocks;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import java.util.function.Function;

public class NeoForgeClientRegister {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ClientRegister.setupRenderLayers(ItemBlockRenderTypes::setRenderLayer);

        ClientRegister.registerKeyBinding(ClientRegistry::registerKeyBinding);

        BlockEntityRenderers.register(FateBlocks.ALTAR_BLOCK_ENTITY.get(), RenderAltar::new);
        event.enqueueWork(() -> ClientRegister.registerItemProps(ItemProperties::register));
    }

    @SubscribeEvent
    public static void initClientItemProps(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            RenderEAItem instance = new RenderEAItem(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.instance;
            }
        }, FateItems.ENUMAELISH.get());
        event.registerItem(new IClientItemExtensions() {
            RenderExcaliburItem instance = new RenderExcaliburItem(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.instance;
            }
        }, FateItems.EXCALIBUR.get());
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        ClientRegister.registerRenderers(event::registerEntityRenderer);
    }

    @SubscribeEvent
    public static void layerRegister(EntityRenderersEvent.RegisterLayerDefinitions event) {
        ClientRegister.layerRegister(event::registerLayerDefinition);
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        ClientRegister.registerParticles(new ClientRegister.PartileRegister() {
            @Override
            public <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> provider) {
                event.registerSpriteSet(type, provider::apply);
            }
        });
    }

    @SubscribeEvent
    public static void registerShader(RegisterShadersEvent event) {
        FateRenders.registerShader(((id, vertexFormat, onLoad) ->
                event.registerShader(new ShaderInstance(event.getResourceProvider(), id, vertexFormat), onLoad)));
    }
}
