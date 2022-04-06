package io.github.flemmli97.fateubw.client;

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
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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

    public static void setupRenderLayers(BiConsumer<Block, RenderType> consumer) {
        consumer.accept(ModBlocks.altar.get(), RenderType.cutout());
        consumer.accept(ModBlocks.gemOre.get(), RenderType.cutout());
        consumer.accept(ModBlocks.artifactOre.get(), RenderType.cutout());
        consumer.accept(ModBlocks.deepSlateGemOre.get(), RenderType.cutout());
        consumer.accept(ModBlocks.deepSlateArtifactOre.get(), RenderType.cutout());
        consumer.accept(ModBlocks.chalk.get(), RenderType.cutout());
    }

    public static <T extends Entity> void registerRenderers(EntityRendererRegister consumer) {
        consumer.register(ModEntities.arthur.get(), RenderArthur::new);
        consumer.register(ModEntities.arthur.get(), RenderArthur::new);
        consumer.register(ModEntities.cuchulainn.get(), RenderCuchulainn::new);
        consumer.register(ModEntities.diarmuid.get(), RenderDiarmuid::new);
        consumer.register(ModEntities.emiya.get(), RenderEmiya::new);
        consumer.register(ModEntities.gilgamesh.get(), RenderGilgamesh::new);
        consumer.register(ModEntities.medea.get(), RenderMedea::new);
        consumer.register(ModEntities.gilles.get(), RenderGilles::new);
        consumer.register(ModEntities.heracles.get(), RenderHeracles::new);
        consumer.register(ModEntities.lancelot.get(), RenderLancelot::new);
        consumer.register(ModEntities.iskander.get(), RenderIskander::new);
        consumer.register(ModEntities.medusa.get(), RenderMedusa::new);
        consumer.register(ModEntities.hassan.get(), RenderHassan::new);
        consumer.register(ModEntities.sasaki.get(), RenderSasaki::new);

        consumer.register(ModEntities.excalibur.get(), RenderExcalibur::new);
        consumer.register(ModEntities.gaebolg.get(), RenderGaeBolg::new);
        consumer.register(ModEntities.archerArrow.get(), RenderArcherArrow::new);
        consumer.register(ModEntities.caladbolg.get(), RenderCaladbolg::new);
        consumer.register(ModEntities.babylon.get(), RenderBabylon::new);
        consumer.register(ModEntities.ea.get(), RenderEA::new);
        consumer.register(ModEntities.magicBeam.get(), RenderMagicBeam::new);
        consumer.register(ModEntities.medeaCircle.get(), RenderEmpty::new);
        consumer.register(ModEntities.gordiusWheel.get(), RenderGordius::new);

        consumer.register(ModEntities.lesserMonster.get(), RenderStarfish::new);
        consumer.register(ModEntities.hassanCopy.get(), RenderHassanCopy::new);
        consumer.register(ModEntities.pegasus.get(), RenderPegasus::new);
        consumer.register(ModEntities.daggerHook.get(), RenderChainDagger::new);

        consumer.register(ModEntities.gem.get(), RenderGem::new);

        consumer.register(ModEntities.multipart.get(), RenderMultiPartEntity::new);
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
        consumer.register(ModParticles.light.get(), ColoredParticle.NoGravityParticleFactory::new);
    }

    public interface EntityRendererRegister {
        <T extends Entity> void register(EntityType<? extends T> type, EntityRendererProvider<T> provider);
    }

    public interface PartileRegister {
        <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> provider);
    }
}
