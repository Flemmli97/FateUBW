package io.github.flemmli97.fateubw.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.network.C2SGuiOpenRequest;
import io.github.flemmli97.fateubw.common.network.C2SServantCommand;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

public class ClientCalls {

    public static void keyEvent() {
        if (ClientHandler.gui.consumeClick()) {
            NetworkCalls.INSTANCE.sendToServer(new C2SGuiOpenRequest());
        }
        if (ClientHandler.special.consumeClick()) {
            NetworkCalls.INSTANCE.sendToServer(new C2SServantCommand(EnumServantUpdate.NP));
        }
        if (ClientHandler.boost.consumeClick()) {
            NetworkCalls.INSTANCE.sendToServer(new C2SServantCommand(EnumServantUpdate.BOOST));
        }
        if (ClientHandler.target.consumeClick()) {
            NetworkCalls.INSTANCE.sendToServer(new C2SServantCommand(EnumServantUpdate.TARGET));
        }
    }

    public static void tick(Entity entity) {
        if (entity == Minecraft.getInstance().player) {
            ShakeHandler.shakeTick--;
        }
    }

    public static void worldRender(PoseStack stack) {
        if (Config.Common.debugAttack) {
            AttackAABBRender.INST.render(stack, Minecraft.getInstance().renderBuffers().crumblingBufferSource());
        }
    }
}