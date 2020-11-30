package com.flemmli97.fate.client;

import com.flemmli97.fate.client.gui.CommandGui;
import com.flemmli97.fate.client.gui.GuiHolyGrail;
import com.flemmli97.fate.client.gui.ManaBar;
import com.flemmli97.fate.client.render.RenderAltar;
import com.flemmli97.fate.client.render.RenderArcherArrow;
import com.flemmli97.fate.client.render.RenderBabylon;
import com.flemmli97.fate.client.render.RenderCaladbolg;
import com.flemmli97.fate.client.render.RenderEA;
import com.flemmli97.fate.client.render.RenderEmpty;
import com.flemmli97.fate.client.render.RenderExcalibur;
import com.flemmli97.fate.client.render.RenderGaeBolg;
import com.flemmli97.fate.client.render.RenderGem;
import com.flemmli97.fate.client.render.RenderHassanCopy;
import com.flemmli97.fate.client.render.RenderMagicBeam;
import com.flemmli97.fate.client.render.RenderStarfish;
import com.flemmli97.fate.client.render.servant.RenderArthur;
import com.flemmli97.fate.client.render.servant.RenderCuchulainn;
import com.flemmli97.fate.client.render.servant.RenderDiarmuid;
import com.flemmli97.fate.client.render.servant.RenderEmiya;
import com.flemmli97.fate.client.render.servant.RenderGilgamesh;
import com.flemmli97.fate.client.render.servant.RenderGilles;
import com.flemmli97.fate.client.render.servant.RenderHassan;
import com.flemmli97.fate.client.render.servant.RenderHeracles;
import com.flemmli97.fate.client.render.servant.RenderIskander;
import com.flemmli97.fate.client.render.servant.RenderLancelot;
import com.flemmli97.fate.client.render.servant.RenderMedea;
import com.flemmli97.fate.client.render.servant.RenderMedusa;
import com.flemmli97.fate.client.render.servant.RenderSasaki;
import com.flemmli97.fate.common.blocks.BlockChalkLine;
import com.flemmli97.fate.common.registry.ModBlocks;
import com.flemmli97.fate.common.registry.ModEntities;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;
import java.util.List;
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
    private static final Comparator<GameProfile> sortName = Comparator.comparing(GameProfile::getName);

    public static void registerRenderer() {
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

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.lesserMonster.get(), RenderStarfish::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.hassanCopy.get(), RenderHassanCopy::new);

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
    }

    public static PlayerEntity clientPlayer() {
        return Minecraft.getInstance().player;
    }

    public static void displayCommandGui() {
        Minecraft.getInstance().displayGuiScreen(new CommandGui());
    }

    public static void openGrailGui(Set<String> rewards) {
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
