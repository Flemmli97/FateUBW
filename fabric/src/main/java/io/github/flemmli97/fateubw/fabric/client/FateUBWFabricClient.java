package io.github.flemmli97.fateubw.fabric.client;

import io.github.flemmli97.fateubw.client.ClientCalls;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class FateUBWFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FabricClientRegister.clientSetup();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!client.isPaused())
                ClientHandler.clientTick++;
            ClientCalls.keyEvent();
        });
        WorldRenderEvents.END.register(ctx -> ClientCalls.worldRender(ctx.matrixStack()));
        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> ClientHandler.getManaBar().renderBar(matrixStack));
        CoreShaderRegistrationCallback.EVENT.register(reg -> FateRenders.registerShader(reg::register));
    }
}
