package io.github.flemmli97.fateubw.fabric.client;

import io.github.flemmli97.fateubw.client.ClientCalls;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.client.ShakeHandler;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import io.github.flemmli97.tenshilib.fabric.client.ClientSetupModInitializer;
import io.github.flemmli97.tenshilib.fabric.client.events.CameraViewEvent;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class FateUBWFabricClient implements ClientSetupModInitializer {

    @Override
    public void clientSetup() {
        FabricClientRegister.clientSetup();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!client.isPaused())
                ClientHandler.clientTick++;
            ClientCalls.keyEvent();
        });
        WorldRenderEvents.END.register(ctx -> ClientCalls.worldRender(ctx.matrixStack()));
        HudRenderCallback.EVENT.register(ClientHandler.getManaBar()::renderBar);
        FateRenders.registerShader();
        CameraViewEvent.EVENT.register(event -> ShakeHandler.renderShaking(event.getYaw(), event.getPitch(), event.getRoll(),
                event.getPartialTicks(), event::setYaw, event::setPitch, event::setRoll));
    }
}
