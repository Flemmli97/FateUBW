package io.github.flemmli97.fateubw.client;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.model.ModelCaladBolg;
import io.github.flemmli97.fateubw.client.model.ModelEA;
import io.github.flemmli97.fateubw.client.model.ModelGordiusWheel;
import io.github.flemmli97.fateubw.client.model.ModelHassanClone;
import io.github.flemmli97.fateubw.client.model.ModelHeracles;
import io.github.flemmli97.fateubw.client.model.ModelMedea;
import io.github.flemmli97.fateubw.client.model.ModelPegasus;
import io.github.flemmli97.fateubw.client.model.ModelServant;
import io.github.flemmli97.fateubw.client.model.ModelStarfishDemon;
import io.github.flemmli97.fateubw.client.render.RenderEmpty;
import io.github.flemmli97.fateubw.client.render.RenderMultiPartEntity;
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
import io.github.flemmli97.fateubw.client.render.servant.RenderArthur;
import io.github.flemmli97.fateubw.client.render.servant.RenderCuchulainn;
import io.github.flemmli97.fateubw.client.render.servant.RenderDiarmuid;
import io.github.flemmli97.fateubw.client.render.servant.RenderEmiya;
import io.github.flemmli97.fateubw.client.render.servant.RenderGilgamesh;
import io.github.flemmli97.fateubw.client.render.servant.RenderGilles;
import io.github.flemmli97.fateubw.client.render.servant.RenderHassan;
import io.github.flemmli97.fateubw.client.render.servant.RenderHeracles;
import io.github.flemmli97.fateubw.client.render.servant.RenderIskander;
import io.github.flemmli97.fateubw.client.render.servant.RenderLancelot;
import io.github.flemmli97.fateubw.client.render.servant.RenderMedea;
import io.github.flemmli97.fateubw.client.render.servant.RenderMedusa;
import io.github.flemmli97.fateubw.client.render.servant.RenderSasaki;
import io.github.flemmli97.fateubw.common.registry.ModBlocks;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.registry.ModParticles;
import io.github.flemmli97.tenshilib.client.particles.ColoredParticle;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.model.geom.ModelLayerLocation;
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
        register.register(ModItems.EXCALIBUR.get(), new ResourceLocation(Fate.MODID, "active"), ItemModelProps.ACTIVE_ITEM_PROP);
        register.register(ModItems.MEDUSA_DAGGER.get(), new ResourceLocation(Fate.MODID, "thrown"), ItemModelProps.THROWN_DAGGER_PROP);
        register.register(ModItems.ARCHBOW.get(), new ResourceLocation(Fate.MODID, "pull"), ItemModelProps.BOW_PULL_PROP);
        register.register(ModItems.ARCHBOW.get(), new ResourceLocation(Fate.MODID, "caladbolg"), ItemModelProps.CALADBOLG_CHARGE);
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
        consumer.register(ModEntities.ARTHUR.get(), RenderArthur::new);
        consumer.register(ModEntities.ARTHUR.get(), RenderArthur::new);
        consumer.register(ModEntities.CUCHULAINN.get(), RenderCuchulainn::new);
        consumer.register(ModEntities.DIARMUID.get(), RenderDiarmuid::new);
        consumer.register(ModEntities.EMIYA.get(), RenderEmiya::new);
        consumer.register(ModEntities.GILGAMESH.get(), RenderGilgamesh::new);
        consumer.register(ModEntities.MEDEA.get(), RenderMedea::new);
        consumer.register(ModEntities.GILLES.get(), RenderGilles::new);
        consumer.register(ModEntities.HERACLES.get(), RenderHeracles::new);
        consumer.register(ModEntities.LANCELOT.get(), RenderLancelot::new);
        consumer.register(ModEntities.ISKANDER.get(), RenderIskander::new);
        consumer.register(ModEntities.MEDUSA.get(), RenderMedusa::new);
        consumer.register(ModEntities.HASSAN.get(), RenderHassan::new);
        consumer.register(ModEntities.SASAKI.get(), RenderSasaki::new);

        consumer.register(ModEntities.EXCALIBUR.get(), RenderExcalibur::new);
        consumer.register(ModEntities.GAEBOLG.get(), RenderGaeBolg::new);
        consumer.register(ModEntities.ARCHER_ARROW.get(), RenderArcherArrow::new);
        consumer.register(ModEntities.CALADBOLG.get(), RenderCaladbolg::new);
        consumer.register(ModEntities.BABYLON.get(), RenderBabylon::new);
        consumer.register(ModEntities.EA.get(), RenderEA::new);
        consumer.register(ModEntities.MAGIC_BEAM.get(), RenderMagicBeam::new);
        consumer.register(ModEntities.MEDEA_CIRCLE.get(), RenderEmpty::new);
        consumer.register(ModEntities.GORDIUS_WHEEL.get(), RenderGordius::new);

        consumer.register(ModEntities.LESSER_MONSTER.get(), RenderStarfish::new);
        consumer.register(ModEntities.HASSAN_COPY.get(), RenderHassanCopy::new);
        consumer.register(ModEntities.PEGASUS.get(), RenderPegasus::new);
        consumer.register(ModEntities.DAGGER_HOOK.get(), RenderChainDagger::new);

        consumer.register(ModEntities.GEM.get(), RenderGem::new);

        consumer.register(ModEntities.MULTIPART.get(), RenderMultiPartEntity::new);
    }

    public static void layerRegister(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> cons) {
        cons.accept(ModelServant.LAYER_LOCATION, () -> ModelServant.createBodyLayer(new CubeDeformation(0)));
        cons.accept(ModelCaladBolg.LAYER_LOCATION, ModelCaladBolg::createBodyLayer);
        cons.accept(ModelGordiusWheel.LAYER_LOCATION, ModelGordiusWheel::createBodyLayer);
        cons.accept(ModelHassanClone.LAYER_LOCATION, ModelHassanClone::createBodyLayer);
        cons.accept(ModelHeracles.LAYER_LOCATION, ModelHeracles::createBodyLayer);
        cons.accept(ModelMedea.LAYER_LOCATION, ModelMedea::createBodyLayer);
        cons.accept(ModelStarfishDemon.LAYER_LOCATION, ModelStarfishDemon::createBodyLayer);
        cons.accept(ModelPegasus.LAYER_LOCATION, ModelPegasus::createBodyLayer);

        cons.accept(ModelEA.LAYER_LOCATION, ModelEA::createBodyLayer);
    }

    public static <T extends ParticleOptions> void registerParticles(PartileRegister consumer) {
        consumer.register(ModParticles.LIGHT.get(), ColoredParticle.NoGravityParticleFactory::new);
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
