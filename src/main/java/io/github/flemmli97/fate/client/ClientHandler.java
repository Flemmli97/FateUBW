package io.github.flemmli97.fate.client;

import com.flemmli97.tenshilib.client.particles.ColoredParticle;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.client.gui.CommandGui;
import io.github.flemmli97.fate.client.gui.GuiHolyGrail;
import io.github.flemmli97.fate.client.gui.ManaBar;
import io.github.flemmli97.fate.client.render.RenderAltar;
import io.github.flemmli97.fate.client.render.RenderEmpty;
import io.github.flemmli97.fate.client.render.misc.RenderArcherArrow;
import io.github.flemmli97.fate.client.render.misc.RenderBabylon;
import io.github.flemmli97.fate.client.render.misc.RenderCaladbolg;
import io.github.flemmli97.fate.client.render.misc.RenderEA;
import io.github.flemmli97.fate.client.render.misc.RenderExcalibur;
import io.github.flemmli97.fate.client.render.misc.RenderGaeBolg;
import io.github.flemmli97.fate.client.render.misc.RenderGem;
import io.github.flemmli97.fate.client.render.misc.RenderGordius;
import io.github.flemmli97.fate.client.render.misc.RenderHassanCopy;
import io.github.flemmli97.fate.client.render.misc.RenderMagicBeam;
import io.github.flemmli97.fate.client.render.misc.RenderMedusaDagger;
import io.github.flemmli97.fate.client.render.misc.RenderPegasus;
import io.github.flemmli97.fate.client.render.misc.RenderStarfish;
import io.github.flemmli97.fate.client.render.servant.RenderArthur;
import io.github.flemmli97.fate.client.render.servant.RenderCuchulainn;
import io.github.flemmli97.fate.client.render.servant.RenderDiarmuid;
import io.github.flemmli97.fate.client.render.servant.RenderEmiya;
import io.github.flemmli97.fate.client.render.servant.RenderGilgamesh;
import io.github.flemmli97.fate.client.render.servant.RenderGilles;
import io.github.flemmli97.fate.client.render.servant.RenderHassan;
import io.github.flemmli97.fate.client.render.servant.RenderHeracles;
import io.github.flemmli97.fate.client.render.servant.RenderIskander;
import io.github.flemmli97.fate.client.render.servant.RenderLancelot;
import io.github.flemmli97.fate.client.render.servant.RenderMedea;
import io.github.flemmli97.fate.client.render.servant.RenderMedusa;
import io.github.flemmli97.fate.client.render.servant.RenderSasaki;
import io.github.flemmli97.fate.common.blocks.BlockChalkLine;
import io.github.flemmli97.fate.common.registry.ModBlocks;
import io.github.flemmli97.fate.common.registry.ModEntities;
import io.github.flemmli97.fate.common.registry.ModItems;
import io.github.flemmli97.fate.common.registry.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClientHandler {

    public static ManaBar manaBar;
    public static KeyBinding gui;
    public static KeyBinding special;
    public static KeyBinding boost;
    public static KeyBinding target;
    public static List<GameProfile> grailPlayers = ImmutableList.of();
    public static Set<GameProfile> pending = ImmutableSet.of();
    public static Set<GameProfile> requests = ImmutableSet.of();
    public static Set<GameProfile> truce = ImmutableSet.of();
    public static int clientTick;

    private static final Comparator<GameProfile> sortName = Comparator.comparing(GameProfile::getName);

    public static void registerRenderer(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.arthur.get(), RenderArthur::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.cuchulainn.get(), RenderCuchulainn::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.diarmuid.get(), RenderDiarmuid::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.emiya.get(), RenderEmiya::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.gilgamesh.get(), RenderGilgamesh::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.medea.get(), RenderMedea::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.gilles.get(), RenderGilles::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.heracles.get(), RenderHeracles::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.lancelot.get(), RenderLancelot::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.iskander.get(), RenderIskander::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.medusa.get(), RenderMedusa::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.hassan.get(), RenderHassan::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.sasaki.get(), RenderSasaki::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.excalibur.get(), RenderExcalibur::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.gaebolg.get(), RenderGaeBolg::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.archerArrow.get(), RenderArcherArrow::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.caladbolg.get(), RenderCaladbolg::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.babylon.get(), RenderBabylon::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ea.get(), RenderEA::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.magicBeam.get(), RenderMagicBeam::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.medeaCircle.get(), RenderEmpty::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.gordiusWheel.get(), RenderGordius::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.lesserMonster.get(), RenderStarfish::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.hassanCopy.get(), RenderHassanCopy::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.pegasus.get(), RenderPegasus::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.daggerHook.get(), RenderMedusaDagger::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.gem.get(), RenderGem::new);

        ClientRegistry.bindTileEntityRenderer(ModBlocks.tileAltar.get(), RenderAltar::new);

        RenderTypeLookup.setRenderLayer(ModBlocks.altar.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.crystalOre.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.charmOre.get(), RenderType.getCutout());
        for (RegistryObject<BlockChalkLine> e : ModBlocks.chalks.values())
            RenderTypeLookup.setRenderLayer(e.get(), RenderType.getCutout());
        manaBar = new ManaBar(Minecraft.getInstance());

        ClientRegistry.registerKeyBinding(gui = new KeyBinding("fate.key.gui", GLFW.GLFW_KEY_H, "fate.keycategory"));
        ClientRegistry.registerKeyBinding(special = new KeyBinding("fate.key.np", GLFW.GLFW_KEY_J, "fate.keycategory"));
        ClientRegistry.registerKeyBinding(boost = new KeyBinding("fate.key.boost", GLFW.GLFW_KEY_N, "fate.keycategory"));
        ClientRegistry.registerKeyBinding(target = new KeyBinding("fate.key.target", GLFW.GLFW_KEY_B, "fate.keycategory"));

        event.enqueueWork(() -> {
            ItemModelsProperties.registerProperty(ModItems.excalibur.get(), new ResourceLocation(Fate.MODID, "active"), ItemModelProps.activeItemProp);
        });
    }

    public static void registerParticles(ParticleFactoryRegisterEvent event) {
        ParticleManager manager = Minecraft.getInstance().particles;
        manager.registerFactory(ModParticles.light.get(), ColoredParticle.NoGravityParticleFactory::new);
    }

    public static PlayerEntity clientPlayer() {
        return Minecraft.getInstance().player;
    }

    public static float getPartialTicks() {
        return Minecraft.getInstance().isGamePaused() ? 0 : Minecraft.getInstance().getRenderPartialTicks();
    }

    public static void displayCommandGui() {
        Minecraft.getInstance().displayGuiScreen(new CommandGui());
    }

    public static void openGrailGui(Map<ResourceLocation, String> rewards) {
        Minecraft.getInstance().displayGuiScreen(new GuiHolyGrail(rewards));
    }

    public static void grailData(Set<GameProfile> set) {
        grailPlayers = ImmutableList.copyOf(set.stream().sorted(sortName).filter(prof -> prof.getId().equals(Minecraft.getInstance().player.getUniqueID())).iterator());
    }

    public static void truceData(Set<GameProfile> t, Set<GameProfile> p, Set<GameProfile> r) {
        truce = ImmutableSet.copyOf(t.stream().sorted(sortName).iterator());
        pending = ImmutableSet.copyOf(p.stream().sorted(sortName).iterator());
        requests = ImmutableSet.copyOf(r.stream().sorted(sortName).iterator());
    }
}
