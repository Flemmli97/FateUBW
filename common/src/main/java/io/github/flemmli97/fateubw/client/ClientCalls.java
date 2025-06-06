package io.github.flemmli97.fateubw.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.network.C2SGuiOpenRequest;
import io.github.flemmli97.fateubw.common.network.C2SServantCommand;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

public class ClientCalls {

    public static void keyEvent() {
        if (ClientHandler.gui.consumeClick()) {
            NetworkCalls.INSTANCE.sendToServer(new C2SGuiOpenRequest());
        }
        if (ClientHandler.special.consumeClick()) {
            NetworkCalls.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.Type.NP, -1));
        }
        if (ClientHandler.boost.consumeClick()) {
            NetworkCalls.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.Type.BOOST, -1));
        }
        if (ClientHandler.target.consumeClick()) {
            NetworkCalls.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.Type.TARGET, -1));
        }
    }

    public static void tick(Entity entity) {
        if (entity == Minecraft.getInstance().cameraEntity) {
            ShakeHandler.shakeTick--;
        }
    }

    public static void worldRender(PoseStack stack) {
        if (CommonConfig.debugAttack) {
            AttackBBRender.INST.render(stack, Minecraft.getInstance().renderBuffers().crumblingBufferSource());
        }
    }
}