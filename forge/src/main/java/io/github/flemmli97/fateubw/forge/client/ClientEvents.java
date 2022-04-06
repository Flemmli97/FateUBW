package io.github.flemmli97.fateubw.forge.client;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientCalls;
import io.github.flemmli97.fateubw.client.ClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(modid = Fate.MODID, value = Dist.CLIENT)
public class ClientEvents {

    public static void register() {
        MinecraftForge.EVENT_BUS.register(ClientEvents.class);
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(ForgeClientRegister::clientSetup);
        modBus.addListener(ForgeClientRegister::registerRenderers);
        modBus.addListener(ForgeClientRegister::registerParticles);
        modBus.addListener(ForgeClientRegister::layerRegister);
    }

    @SubscribeEvent(receiveCanceled = true)
    public static void keyEvent(InputEvent.KeyInputEvent event) {
        ClientCalls.keyEvent();
    }

    @SubscribeEvent
    public static void renderManaBar(RenderGameOverlayEvent.Post event) {
        if (event.isCancelable() || event.getType() != RenderGameOverlayEvent.ElementType.LAYER)
            return;
        ClientHandler.getManaBar().renderBar(event.getMatrixStack());
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (!Minecraft.getInstance().isPaused())
                ClientHandler.clientTick++;
        }
    }
}