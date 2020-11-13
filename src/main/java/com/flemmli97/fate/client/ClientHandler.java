package com.flemmli97.fate.client;

import com.flemmli97.fate.client.render.RenderAltar;
import com.flemmli97.fate.client.render.RenderArcherArrow;
import com.flemmli97.fate.client.render.RenderBabylon;
import com.flemmli97.fate.client.render.RenderCaladbolg;
import com.flemmli97.fate.client.render.RenderEA;
import com.flemmli97.fate.client.render.RenderExcalibur;
import com.flemmli97.fate.client.render.RenderGaeBolg;
import com.flemmli97.fate.client.render.RenderGem;
import com.flemmli97.fate.client.render.RenderStarfish;
import com.flemmli97.fate.client.render.servant.RenderArthur;
import com.flemmli97.fate.client.render.servant.RenderCuchulainn;
import com.flemmli97.fate.client.render.servant.RenderDiarmuid;
import com.flemmli97.fate.client.render.servant.RenderEmiya;
import com.flemmli97.fate.client.render.servant.RenderGilgamesh;
import com.flemmli97.fate.client.render.servant.RenderGilles;
import com.flemmli97.fate.client.render.servant.RenderLancelot;
import com.flemmli97.fate.common.blocks.BlockChalkLine;
import com.flemmli97.fate.common.registry.ModBlocks;
import com.flemmli97.fate.common.registry.ModEntities;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.MultiLayerModel;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.pipeline.ForgeBlockModelRenderer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientHandler {

    public static void registerRenderer() {
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.arthur.get(), RenderArthur::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.cuchulainn.get(), RenderCuchulainn::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.diarmuid.get(), RenderDiarmuid::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.emiya.get(), RenderEmiya::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.gilgamesh.get(), RenderGilgamesh::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.gilles.get(), RenderGilles::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.lancelot.get(), RenderLancelot::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.excalibur.get(), RenderExcalibur::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.gaebolg.get(), RenderGaeBolg::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.archerArrow.get(), RenderArcherArrow::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.caladbolg.get(), RenderCaladbolg::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.babylon.get(), RenderBabylon::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ea.get(), RenderEA::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.lesserMonster.get(), RenderStarfish::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.gem.get(), RenderGem::new);

        ClientRegistry.bindTileEntityRenderer(ModBlocks.tileAltar.get(), RenderAltar::new);

        RenderTypeLookup.setRenderLayer(ModBlocks.crystalOre.get(), type->true);
        RenderTypeLookup.setRenderLayer(ModBlocks.charmOre.get(), type->true);
        for(RegistryObject<BlockChalkLine> e : ModBlocks.chalks.values())
            RenderTypeLookup.setRenderLayer(e.get(), RenderType.getCutout());
    }

    public static PlayerEntity clientPlayer() {
        return Minecraft.getInstance().player;
    }
}
