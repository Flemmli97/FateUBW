package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.common.world.GrailTeam;
import io.github.flemmli97.fateubw.common.world.TeamHandler;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class S2CTeamGuiData implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "s2c_team_gui");

    private final boolean open;
    private final GrailTeam.ClientTeamInfo info;

    private S2CTeamGuiData(boolean open, GrailTeam.ClientTeamInfo info) {
        this.open = open;
        this.info = info;
    }

    private S2CTeamGuiData(ServerPlayer player, boolean open) {
        this.open = open;
        TeamHandler handler = TeamHandler.get(player.getServer());
        handler.listenChanges(player);
        this.info = GrailTeam.ClientTeamInfo.create(player, handler);
    }

    public static void sendTeamData(ServerPlayer player, boolean open) {
        NetworkCalls.INSTANCE.sendToClient(new S2CTeamGuiData(player, open), player);
    }

    public static S2CTeamGuiData read(FriendlyByteBuf buf) {
        return new S2CTeamGuiData(buf.readBoolean(), GrailTeam.ClientTeamInfo.read(buf));
    }

    public static void handle(S2CTeamGuiData pkt) {
        ClientHandler.openTeamGui(pkt.open, pkt.info);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.open);
        this.info.write(buf);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}