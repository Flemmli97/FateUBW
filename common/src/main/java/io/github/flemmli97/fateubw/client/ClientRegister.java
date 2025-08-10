package io.github.flemmli97.fateubw.client;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.model.BaseServantModel;
import io.github.flemmli97.fateubw.client.model.ModelEA;
import io.github.flemmli97.fateubw.client.model.ModelGordiusWheel;
import io.github.flemmli97.fateubw.client.model.ModelHassanClone;
import io.github.flemmli97.fateubw.client.model.ModelHeracles;
import io.github.flemmli97.fateubw.client.model.ModelMedea;
import io.github.flemmli97.fateubw.client.model.ModelPegasus;
import io.github.flemmli97.fateubw.client.model.ModelServant;
import io.github.flemmli97.fateubw.client.model.ModelStarfishDemon;
import io.github.flemmli97.fateubw.client.particles.RingParticle;
import io.github.flemmli97.fateubw.client.particles.TrailParticle;
import io.github.flemmli97.fateubw.client.render.RenderEmpty;
import io.github.flemmli97.fateubw.client.render.ServantRenderer;
import io.github.flemmli97.fateubw.client.render.misc.EmptyRender;
import io.github.flemmli97.fateubw.client.render.misc.RenderArcherArrow;
import io.github.flemmli97.fateubw.client.render.misc.RenderBabylon;
import io.github.flemmli97.fateubw.client.render.misc.RenderCaladbolg;
import io.github.flemmli97.fateubw.client.render.misc.RenderChainDagger;
import io.github.flemmli97.fateubw.client.render.misc.RenderEA;
import io.github.flemmli97.fateubw.client.render.misc.RenderExcalibur;
import io.github.flemmli97.fateubw.client.render.misc.RenderGaeBolg;
import io.github.flemmli97.fateubw.client.render.misc.RenderGem;
import io.github.flemmli97.fateubw.client.render.misc.RenderGordius;
import io.github.flemmli97.fateubw.client.render.misc.RenderHassanCopy;
import io.github.flemmli97.fateubw.client.render.misc.RenderMagicBeam;
import io.github.flemmli97.fateubw.client.render.misc.RenderPegasus;
import io.github.flemmli97.fateubw.client.render.misc.RenderStarfish;
import io.github.flemmli97.fateubw.client.render.misc.RenderThrownItem;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.registry.FateBlocks;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.tenshilib.client.particles.ColoredParticle;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.lwjgl.glfw.GLFW;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ClientRegister {

    public static void registerKeyBinding(Consumer<KeyMapping> consumer) {
        consumer.accept(ClientHandler.gui = new KeyMapping("fateubw.key.gui", GLFW.GLFW_KEY_H, "fateubw.keycategory"));
        consumer.accept(ClientHandler.special = new KeyMapping("fateubw.key.np", GLFW.GLFW_KEY_J, "fateubw.keycategory"));
        consumer.accept(ClientHandler.boost = new KeyMapping("fateubw.key.boost", GLFW.GLFW_KEY_N, "fateubw.keycategory"));
        consumer.accept(ClientHandler.target = new KeyMapping("fateubw.key.target", GLFW.GLFW_KEY_B, "fateubw.keycategory"));
    }

    public static void registerItemProps(ItemModelPropsRegister register) {
        register.register(FateItems.EXCALIBUR.get(), ItemModelProps.ACTIVE_ID, ItemModelProps.ACTIVE_ITEM_PROP);
        register.register(FateItems.MEDUSA_DAGGER.get(), ItemModelProps.HELD_ID, ItemModelProps.HELD_MAIN_PROP);
        register.register(FateItems.MEDUSA_DAGGER.get(), ItemModelProps.THROWN_DAGGER_ID, ItemModelProps.THROWN_DAGGER_PROP);
        register.register(FateItems.ARCHBOW.get(), ItemModelProps.BOW_PULL_ID, ItemModelProps.BOW_PULL_PROP);
        register.register(FateItems.ARCHBOW.get(), ItemModelProps.CALADBOLG_ID, ItemModelProps.CALADBOLG_CHARGE);
    }

    public static void setupRenderLayers(BiConsumer<Block, RenderType> consumer) {
        consumer.accept(FateBlocks.ALTAR.get(), RenderType.cutout());
        consumer.accept(FateBlocks.GEM_ORE.get(), RenderType.cutout());
        consumer.accept(FateBlocks.ARTIFACT_ORE.get(), RenderType.cutout());
        consumer.accept(FateBlocks.DEEP_SLATE_GEM_ORE.get(), RenderType.cutout());
        consumer.accept(FateBlocks.DEEP_SLATE_ARTIFACT_ORE.get(), RenderType.cutout());
        consumer.accept(FateBlocks.CHALK.get(), RenderType.cutout());
    }

    public static <T extends Entity> void registerRenderers(EntityRendererRegister consumer) {
        registerServant(consumer, FateEntities.ARTHUR);
        registerServant(consumer, FateEntities.CUCHULAINN);
        registerServant(consumer, FateEntities.DIARMUID);
        registerServant(consumer, FateEntities.EMIYA);
        registerServant(consumer, FateEntities.GILGAMESH);
        registerServant(consumer, FateEntities.MEDEA);
        consumer.register(FateEntities.MEDEA.get(), getServantRenderer(ModelMedea::new, ModelMedea.LAYER_LOCATION, servantTexture(FateEntities.MEDEA), 0.5f));
        registerServant(consumer, FateEntities.GILLES);
        consumer.register(FateEntities.HERACLES.get(), getServantRenderer(ModelHeracles::new, ModelHeracles.LAYER_LOCATION, servantTexture(FateEntities.HERACLES), 1f));
        registerServant(consumer, FateEntities.LANCELOT);
        registerServant(consumer, FateEntities.ISKANDER);
        registerServant(consumer, FateEntities.MEDUSA, true);
        registerServant(consumer, FateEntities.HASSAN);
        registerServant(consumer, FateEntities.SASAKI);

        consumer.register(FateEntities.EXCALIBUR.get(), RenderExcalibur::new);
        consumer.register(FateEntities.GAEBOLG.get(), RenderGaeBolg::new);
        consumer.register(FateEntities.ARCHER_ARROW.get(), RenderArcherArrow::new);
        consumer.register(FateEntities.CALADBOLG.get(), RenderCaladbolg::new);
        consumer.register(FateEntities.BABYLON.get(), RenderBabylon::new);
        consumer.register(FateEntities.EA.get(), RenderEA::new);
        consumer.register(FateEntities.MAGIC_BEAM.get(), RenderMagicBeam::new);
        consumer.register(FateEntities.MEDEA_CIRCLE.get(), RenderEmpty::new);
        consumer.register(FateEntities.GORDIUS_WHEEL.get(), RenderGordius::new);
        consumer.register(FateEntities.THROWN_ITEM.get(), RenderThrownItem::new);
        consumer.register(FateEntities.GEM.get(), RenderGem::new);
        consumer.register(FateEntities.MAGIC_SHOT.get(), EmptyRender::new);

        consumer.register(FateEntities.LESSER_MONSTER.get(), RenderStarfish::new);
        consumer.register(FateEntities.HASSAN_COPY.get(), RenderHassanCopy::new);
        consumer.register(FateEntities.PEGASUS.get(), RenderPegasus::new);
        consumer.register(FateEntities.DAGGER_HOOK.get(), RenderChainDagger::new);

        consumer.register(FateEntities.MULTIPART.get(), EmptyRender::new);
    }

    private static <T extends BaseServant, M extends BaseServantModel<T>> EntityRendererProvider<? super T> getServantRenderer(Function<ModelPart, M> model, ModelLayerLocation layerLocation, ResourceLocation texture, float shadow) {
        return manager -> new ServantRenderer<>(manager, model.apply(manager.bakeLayer(layerLocation)), texture, shadow);
    }

    private static <T extends BaseServant> void registerServant(EntityRendererRegister consumer, RegistryEntrySupplier<EntityType<T>> reg) {
        registerServant(consumer, reg, false);
    }

    private static <T extends BaseServant> void registerServant(EntityRendererRegister consumer, RegistryEntrySupplier<EntityType<T>> reg, boolean slim) {
        consumer.register(reg.get(), getServantRenderer(root -> new ModelServant<>(root, reg.getID().getPath()), slim ? ModelServant.LAYER_LOCATION_SLIM : ModelServant.LAYER_LOCATION, servantTexture(reg), 0.5f));
    }

    public static <T extends Entity> ResourceLocation servantTexture(RegistryEntrySupplier<EntityType<T>> reg) {
        return new ResourceLocation(Fate.MODID, "textures/entity/servant/" + reg.getID().getPath() + ".png");
    }

    public static void layerRegister(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> cons) {
        cons.accept(ModelServant.LAYER_LOCATION, () -> ModelServant.createBodyLayer(new CubeDeformation(0), false));
        cons.accept(ModelServant.LAYER_LOCATION_SLIM, () -> ModelServant.createBodyLayer(new CubeDeformation(0), true));
        cons.accept(ModelHeracles.LAYER_LOCATION, ModelHeracles::createBodyLayer);
        cons.accept(ModelMedea.LAYER_LOCATION, ModelMedea::createBodyLayer);
        cons.accept(ModelGordiusWheel.LAYER_LOCATION, ModelGordiusWheel::createBodyLayer);
        cons.accept(ModelHassanClone.LAYER_LOCATION, ModelHassanClone::createBodyLayer);
        cons.accept(ModelStarfishDemon.LAYER_LOCATION, ModelStarfishDemon::createBodyLayer);
        cons.accept(ModelPegasus.LAYER_LOCATION, ModelPegasus::createBodyLayer);

        cons.accept(ModelEA.LAYER_LOCATION, ModelEA::createBodyLayer);
    }

    public static <T extends ParticleOptions> void registerParticles(PartileRegister consumer) {
        consumer.register(FateParticles.LIGHT.get(), ColoredParticle.NoGravityParticleFactory::new);
        consumer.register(FateParticles.TRAIL.get(), TrailParticle.Factory::new);
        consumer.register(FateParticles.RING.get(), RingParticle.Factory::new);
    }

    public interface EntityRendererRegister {
        <T extends Entity> void register(EntityType<? extends T> type, EntityRendererProvider<T> provider);
    }

    public interface PartileRegister {
        <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> provider);
    }

    public interface ItemModelPropsRegister {
        void register(Item item, ResourceLocation res, ClampedItemPropertyFunction function);
    }
}
