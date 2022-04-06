package io.github.flemmli97.fateubw.fabric;

import io.github.flemmli97.fateubw.client.ClientCalls;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.fabric.client.FabricClientRegister;
import io.github.flemmli97.fateubw.fabric.common.config.ConfigLoader;
import io.github.flemmli97.fateubw.fabric.common.config.ConfigSpecs;
import io.github.flemmli97.fateubw.fabric.common.network.ClientPacketHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class FateUBWFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FabricClientRegister.clientSetup();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!client.isPaused())
                ClientHandler.clientTick++;
            ClientCalls.keyEvent();
        });
        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> ClientHandler.getManaBar().renderBar(matrixStack));
        ClientPacketHandler.registerClientPackets();
        ConfigSpecs.initClientConfig();
        ConfigLoader.loadClient();
    }
}
