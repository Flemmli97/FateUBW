package io.github.flemmli97.fateubw.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.network.C2SGuiOpenRequest;
import io.github.flemmli97.fateubw.common.network.C2SServantCommand;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

public class ClientCalls {

    public static void keyEvent() {
        if (ClientHandler.gui.consumeClick()) {
            LoaderNetwork.INSTANCE.sendToServer(C2SGuiOpenRequest.INSTANCE);
        }
        if (ClientHandler.special.consumeClick()) {
            LoaderNetwork.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.ActionType.NP, -1));
        }
        if (ClientHandler.boost.consumeClick()) {
            LoaderNetwork.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.ActionType.BOOST, -1));
        }
        if (ClientHandler.target.consumeClick()) {
            LoaderNetwork.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.ActionType.TARGET, -1));
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