package io.github.flemmli97.fateubw.fabric.client;

import io.github.flemmli97.fateubw.client.ClientCalls;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.client.ShakeHandler;
import io.github.flemmli97.fateubw.client.particles.SphereParticle;
import io.github.flemmli97.fateubw.client.particles.TrailParticle;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import io.github.flemmli97.fateubw.fabric.compat.GeoEvents;
import io.github.flemmli97.tenshilib.fabric.client.ClientSetupModInitializer;
import io.github.flemmli97.tenshilib.fabric.client.events.CameraViewEvent;
import io.github.flemmli97.tenshilib.fabric.client.events.ParticleTypeRegisterEvent;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.loader.api.FabricLoader;

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
        ParticleTypeRegisterEvent.EVENT.register(register -> {
            register.addRenderType(TrailParticle.SOLID_COLOR_PARTICLE);
            register.addRenderType(TrailParticle.COLOR_PARTICLE);
            register.addRenderType(SphereParticle.SPHERE_RENDER_TYPE);
        });

        FateRenders.registerShader();
        CameraViewEvent.EVENT.register(event -> ShakeHandler.renderShaking(event.getYaw(), event.getPitch(), event.getRoll(),
                event.getPartialTicks(), event::setYaw, event::setPitch, event::setRoll));
        if (FabricLoader.getInstance().isModLoaded("geckolib")) {
            GeoEvents.init();
        }
    }
}
