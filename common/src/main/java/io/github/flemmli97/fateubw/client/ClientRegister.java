package io.github.flemmli97.fateubw.client;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.model.BaseServantModel;
import io.github.flemmli97.fateubw.client.model.ModelCaladBolg;
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
import io.github.flemmli97.fateubw.client.render.RenderMultiPartEntity;
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
import io.github.flemmli97.fateubw.common.registry.ModBlocks;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.registry.ModParticles;
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
        consumer.accept(ClientHandler.gui = new KeyMapping("fate.key.gui", GLFW.GLFW_KEY_H, "fate.keycategory"));
        consumer.accept(ClientHandler.special = new KeyMapping("fate.key.np", GLFW.GLFW_KEY_J, "fate.keycategory"));
        consumer.accept(ClientHandler.boost = new KeyMapping("fate.key.boost", GLFW.GLFW_KEY_N, "fate.keycategory"));
        consumer.accept(ClientHandler.target = new KeyMapping("fate.key.target", GLFW.GLFW_KEY_B, "fate.keycategory"));
    }

    public static void registerItemProps(ItemModelPropsRegister register) {
        register.register(ModItems.EXCALIBUR.get(), ItemModelProps.ACTIVE_ID, ItemModelProps.ACTIVE_ITEM_PROP);
        register.register(ModItems.MEDUSA_DAGGER.get(), ItemModelProps.HELD_ID, ItemModelProps.HELD_MAIN_PROP);
        register.register(ModItems.MEDUSA_DAGGER.get(), ItemModelProps.THROWN_DAGGER_ID, ItemModelProps.THROWN_DAGGER_PROP);
        register.register(ModItems.ARCHBOW.get(), ItemModelProps.BOW_PULL_ID, ItemModelProps.BOW_PULL_PROP);
        register.register(ModItems.ARCHBOW.get(), ItemModelProps.CALADBOLG_ID, ItemModelProps.CALADBOLG_CHARGE);
    }

    public static void setupRenderLayers(BiConsumer<Block, RenderType> consumer) {
        consumer.accept(ModBlocks.ALTAR.get(), RenderType.cutout());
        consumer.accept(ModBlocks.GEM_ORE.get(), RenderType.cutout());
        consumer.accept(ModBlocks.ARTIFACT_ORE.get(), RenderType.cutout());
        consumer.accept(ModBlocks.DEEP_SLATE_GEM_ORE.get(), RenderType.cutout());
        consumer.accept(ModBlocks.DEEP_SLATE_ARTIFACT_ORE.get(), RenderType.cutout());
        consumer.accept(ModBlocks.CHALK.get(), RenderType.cutout());
    }

    public static <T extends Entity> void registerRenderers(EntityRendererRegister consumer) {
        registerServant(consumer, ModEntities.ARTHUR);
        registerServant(consumer, ModEntities.CUCHULAINN);
        registerServant(consumer, ModEntities.DIARMUID);
        registerServant(consumer, ModEntities.EMIYA);
        registerServant(consumer, ModEntities.GILGAMESH);
        registerServant(consumer, ModEntities.MEDEA);
        consumer.register(ModEntities.MEDEA.get(), getServantRenderer(ModelMedea::new, ModelMedea.LAYER_LOCATION, servantTexture(ModEntities.MEDEA), 0.5f));
        registerServant(consumer, ModEntities.GILLES);
        consumer.register(ModEntities.HERACLES.get(), getServantRenderer(ModelHeracles::new, ModelHeracles.LAYER_LOCATION, servantTexture(ModEntities.HERACLES), 1f));
        registerServant(consumer, ModEntities.LANCELOT);
        registerServant(consumer, ModEntities.ISKANDER);
        registerServant(consumer, ModEntities.MEDUSA, true);
        registerServant(consumer, ModEntities.HASSAN);
        registerServant(consumer, ModEntities.SASAKI);

        consumer.register(ModEntities.EXCALIBUR.get(), RenderExcalibur::new);
        consumer.register(ModEntities.GAEBOLG.get(), RenderGaeBolg::new);
        consumer.register(ModEntities.ARCHER_ARROW.get(), RenderArcherArrow::new);
        consumer.register(ModEntities.CALADBOLG.get(), RenderCaladbolg::new);
        consumer.register(ModEntities.BABYLON.get(), RenderBabylon::new);
        consumer.register(ModEntities.EA.get(), RenderEA::new);
        consumer.register(ModEntities.MAGIC_BEAM.get(), RenderMagicBeam::new);
        consumer.register(ModEntities.MEDEA_CIRCLE.get(), RenderEmpty::new);
        consumer.register(ModEntities.GORDIUS_WHEEL.get(), RenderGordius::new);
        consumer.register(ModEntities.THROWN_ITEM.get(), RenderThrownItem::new);
        consumer.register(ModEntities.GEM.get(), RenderGem::new);
        consumer.register(ModEntities.MAGIC_SHOT.get(), EmptyRender::new);

        consumer.register(ModEntities.LESSER_MONSTER.get(), RenderStarfish::new);
        consumer.register(ModEntities.HASSAN_COPY.get(), RenderHassanCopy::new);
        consumer.register(ModEntities.PEGASUS.get(), RenderPegasus::new);
        consumer.register(ModEntities.DAGGER_HOOK.get(), RenderChainDagger::new);

        consumer.register(ModEntities.MULTIPART.get(), RenderMultiPartEntity::new);
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
        cons.accept(ModelCaladBolg.LAYER_LOCATION, ModelCaladBolg::createBodyLayer);
        cons.accept(ModelGordiusWheel.LAYER_LOCATION, ModelGordiusWheel::createBodyLayer);
        cons.accept(ModelHassanClone.LAYER_LOCATION, ModelHassanClone::createBodyLayer);
        cons.accept(ModelStarfishDemon.LAYER_LOCATION, ModelStarfishDemon::createBodyLayer);
        cons.accept(ModelPegasus.LAYER_LOCATION, ModelPegasus::createBodyLayer);

        cons.accept(ModelEA.LAYER_LOCATION, ModelEA::createBodyLayer);
    }

    public static <T extends ParticleOptions> void registerParticles(PartileRegister consumer) {
        consumer.register(ModParticles.LIGHT.get(), ColoredParticle.NoGravityParticleFactory::new);
        consumer.register(ModParticles.TRAIL.get(), TrailParticle.Factory::new);
        consumer.register(ModParticles.RING.get(), RingParticle.Factory::new);
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
