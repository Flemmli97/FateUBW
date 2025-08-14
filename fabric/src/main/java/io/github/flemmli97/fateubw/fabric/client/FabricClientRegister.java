package io.github.flemmli97.fateubw.fabric.client;

import io.github.flemmli97.fateubw.client.ClientRegister;
import io.github.flemmli97.fateubw.client.model.EAModel;
import io.github.flemmli97.fateubw.client.render.RenderAltar;
import io.github.flemmli97.fateubw.client.render.item.RenderEAItem;
import io.github.flemmli97.fateubw.client.render.item.RenderExcaliburItem;
import io.github.flemmli97.fateubw.common.registry.FateBlocks;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Function;

public class FabricClientRegister {

    public static void clientSetup() {
        ClientRegister.setupRenderLayers(BlockRenderLayerMap.INSTANCE::putBlock);

        ClientRegister.registerKeyBinding(KeyBindingHelper::registerKeyBinding);

        BlockEntityRenderers.register(FateBlocks.ALTAR_BLOCK_ENTITY.get(), RenderAltar::new);
        ClientRegister.registerItemProps(ItemProperties::register);
        ClientRegister.registerRenderers(EntityRendererRegistry::register);
        ClientRegister.registerParticles(new ClientRegister.PartileRegister() {
            @Override
            public <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> provider) {
                ParticleFactoryRegistry.getInstance().register(type, provider::apply);
            }
        });
        registerBEWLR();
    }

    private static final RenderUtils.BeamBuilder EXCALIBUR_BEAM = RenderExcaliburItem.createBeam();
    private static final RenderUtils.BeamBuilder EA_BEAM = RenderEAItem.createBeam();

    private static EAModel EA_MODEL;

    public static void registerBEWLR() {
        BuiltinItemRendererRegistry.INSTANCE.register(FateItems.EXCALIBUR.get(), ((stack, mode, matrices, vertexConsumers, light, overlay) -> RenderExcaliburItem.render(stack, mode, matrices, vertexConsumers, light, overlay, EXCALIBUR_BEAM)));
        BuiltinItemRendererRegistry.INSTANCE.register(FateItems.ENUMAELISH.get(), ((stack, mode, matrices, vertexConsumers, light, overlay) -> {
            if (EA_MODEL == null)
                EA_MODEL = new EAModel();
            RenderEAItem.render(stack, mode, matrices, vertexConsumers, light, overlay, EA_BEAM, EA_MODEL);
        }));
    }
}
