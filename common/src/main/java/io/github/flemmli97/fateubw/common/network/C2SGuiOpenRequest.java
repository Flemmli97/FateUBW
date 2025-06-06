package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record C2SGuiOpenRequest() implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "c2s_gui_open_request");

    @Override
    public void write(FriendlyByteBuf buf) {
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public static C2SGuiOpenRequest read(FriendlyByteBuf buf) {
        return new C2SGuiOpenRequest();
    }

    public static void handle(C2SGuiOpenRequest pkt, ServerPlayer sender) {
        if (sender == null)
            return;
        BaseServant servant = GrailWarHandler.get(sender.getServer()).getServant(sender);
        S2CServantGui.sendServantGui(sender, servant);
    }
}