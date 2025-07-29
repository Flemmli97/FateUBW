package io.github.flemmli97.fateubw.neoforge.client;

import io.github.flemmli97.fateubw.client.ClientCalls;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.client.ShakeHandler;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;

public class ClientEvents {

    public static void register(IEventBus modBus) {
        NeoForge.EVENT_BUS.register(ClientEvents.class);
        modBus.register(NeoForgeClientRegister.class);
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

    @SubscribeEvent
    public static void shaking(EntityViewRenderEvent.CameraSetup event) {
        ShakeHandler.renderShaking(event.getCamera(), event.getYaw(), event.getPitch(), event.getRoll(), (float) event.getPartialTicks(), event::setYaw, event::setPitch, event::setRoll);
    }

    @SubscribeEvent
    public static void worldRender(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES)
            ClientCalls.worldRender(event.getPoseStack());
    }
}