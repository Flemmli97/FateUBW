package io.github.flemmli97.fateubw.fabric.client;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientRegister;
import io.github.flemmli97.fateubw.client.ItemModelProps;
import io.github.flemmli97.fateubw.client.model.ModelEA;
import io.github.flemmli97.fateubw.client.render.RenderAltar;
import io.github.flemmli97.fateubw.client.render.item.RenderEAItem;
import io.github.flemmli97.fateubw.client.render.item.RenderExcaliburItem;
import io.github.flemmli97.fateubw.common.registry.ModBlocks;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class FabricClientRegister {

    public static void clientSetup() {
        ClientRegister.setupRenderLayers(BlockRenderLayerMap.INSTANCE::putBlock);

        ClientRegister.registerKeyBinding(KeyBindingHelper::registerKeyBinding);

        BlockEntityRendererRegistry.register(ModBlocks.tileAltar.get(), RenderAltar::new);
        FabricModelPredicateProviderRegistry.register(ModItems.excalibur.get(), new ResourceLocation(Fate.MODID, "active"), ItemModelProps.activeItemProp);
        FabricModelPredicateProviderRegistry.register(ModItems.medusaDagger.get(), new ResourceLocation(Fate.MODID, "thrown"), ItemModelProps.thrownDaggerProp);

        ClientRegister.registerRenderers(EntityRendererRegistry::register);
        ClientRegister.layerRegister((loc, sup) -> EntityModelLayerRegistry.registerModelLayer(loc, sup::get));

        ClientRegister.registerParticles(new ClientRegister.PartileRegister() {
            @Override
            public <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> provider) {
                ParticleFactoryRegistry.getInstance().register(type, provider::apply);
            }
        });
        registerBEWLR();
    }


    private static final RenderUtils.BeamBuilder excaliburBeam = RenderExcaliburItem.createBeam();
    private static final RenderUtils.BeamBuilder eaBeam = RenderEAItem.createBeam();

    private static ModelEA eaModel;

    public static void registerBEWLR() {
        BuiltinItemRendererRegistry.INSTANCE.register(ModItems.excalibur.get(), ((stack, mode, matrices, vertexConsumers, light, overlay) -> RenderExcaliburItem.render(stack, mode, matrices, vertexConsumers, light, overlay, excaliburBeam)));
        BuiltinItemRendererRegistry.INSTANCE.register(ModItems.enumaelish.get(), ((stack, mode, matrices, vertexConsumers, light, overlay) -> {
            if (eaModel == null)
                eaModel = new ModelEA(Minecraft.getInstance().getEntityModels().bakeLayer(ModelEA.LAYER_LOCATION));
            RenderEAItem.render(stack, mode, matrices, vertexConsumers, light, overlay, eaBeam, eaModel);
        }));
    }
}
