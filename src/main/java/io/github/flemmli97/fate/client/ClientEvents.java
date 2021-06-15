package io.github.flemmli97.fate.client;

import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.common.utils.EnumServantUpdate;
import io.github.flemmli97.fate.network.C2SServantCommand;
import io.github.flemmli97.fate.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Fate.MODID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent(receiveCanceled = true)
    public static void keyEvent(InputEvent.KeyInputEvent event) {
        if (ClientHandler.gui.isPressed()) {
            ClientHandler.displayCommandGui();
        }
        if (ClientHandler.special.isPressed()) {
            PacketHandler.sendToServer(new C2SServantCommand(EnumServantUpdate.NP));
        }
        if (ClientHandler.boost.isPressed()) {
            PacketHandler.sendToServer(new C2SServantCommand(EnumServantUpdate.BOOST));
        }
        if (ClientHandler.target.isPressed()) {
            PacketHandler.sendToServer(new C2SServantCommand(EnumServantUpdate.TARGET));
        }
    }

    @SubscribeEvent
    public static void renderManaBar(RenderGameOverlayEvent.Post event) {
        if (event.isCancelable() || event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE || ClientHandler.manaBar == null)
            return;
        ClientHandler.manaBar.renderBar(event.getMatrixStack());
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (!Minecraft.getInstance().isGamePaused())
                ClientHandler.clientTick++;
        }
    }
}