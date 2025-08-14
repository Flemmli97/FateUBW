package io.github.flemmli97.fateubw.neoforge.client;

import io.github.flemmli97.fateubw.client.ClientCalls;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.client.ShakeHandler;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.common.NeoForge;

public class ClientEvents {

    public static void register(IEventBus modBus) {
        NeoForge.EVENT_BUS.register(ClientEvents.class);
        modBus.register(NeoForgeClientRegister.class);
    }

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Pre event) {
        if (!Minecraft.getInstance().isPaused())
            ClientHandler.clientTick++;
    }

    @SubscribeEvent(receiveCanceled = true)
    public static void keyEvent(ClientTickEvent.Post event) {
        ClientCalls.keyEvent();
    }

    @SubscribeEvent
    public static void shaking(ViewportEvent.ComputeCameraAngles event) {
        ShakeHandler.renderShaking(event.getCamera(), event.getYaw(), event.getPitch(), event.getRoll(), (float) event.getPartialTick(), event::setYaw, event::setPitch, event::setRoll);
    }

    @SubscribeEvent
    public static void worldRender(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES)
            ClientCalls.worldRender(event.getPoseStack());
    }
}