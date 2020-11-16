package com.flemmli97.fate.client;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.client.gui.ManaBar;
import com.flemmli97.fate.common.utils.EnumServantUpdate;
import com.flemmli97.fate.network.C2SServantCommand;
import com.flemmli97.fate.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
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
}