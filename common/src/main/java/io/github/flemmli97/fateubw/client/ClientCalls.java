package io.github.flemmli97.fateubw.client;

import io.github.flemmli97.fateubw.common.network.C2SServantCommand;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

public class ClientCalls {

    public static void keyEvent() {
        if (ClientHandler.gui.consumeClick()) {
            ClientHandler.displayCommandGui();
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
}