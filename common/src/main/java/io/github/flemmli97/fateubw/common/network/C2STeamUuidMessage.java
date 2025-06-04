package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.world.TeamHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class C2STeamUuidMessage implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "c2s_team_message_uuid");

    private final Type type;
    private final UUID value;

    public C2STeamUuidMessage(Type type, UUID value) {
        this.type = type;
        this.value = value;
    }

    public static C2STeamUuidMessage read(FriendlyByteBuf buf) {
        return new C2STeamUuidMessage(buf.readEnum(Type.class), buf.readUUID());
    }

    public static void handle(C2STeamUuidMessage pkt, ServerPlayer sender) {
        if (sender == null)
            return;
        TeamHandler handler = TeamHandler.get(sender.getServer());
        switch (pkt.type) {
            case INVITE -> handler.invite(sender, pkt.value);
            case RETRACT_INVITE -> handler.retractInvite(sender, pkt.value);
            case REQUEST_ALLY -> handler.requestAlliance(sender, pkt.value);
            case RETRACT_REQUEST -> handler.retractRequest(sender, pkt.value);
            case ACCEPT_ALLY -> handler.acceptAlliance(sender, pkt.value);
            case DENY_ALLY -> handler.denyAlliance(sender, pkt.value);
            case DISSOLVE_ALLY -> handler.dissolveAlliance(sender, pkt.value);
            case PROMOTE -> handler.givePerms(sender, pkt.value);
            case DEMOTE -> handler.revokePerms(sender, pkt.value);
            case KICK -> handler.removeFromTeam(sender, pkt.value);
            case ACCEPT_INVITE -> handler.joinTeam(sender, pkt.value);
            case DENY_INVITE -> handler.denyInvite(sender, pkt.value);
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(this.type);
        buf.writeUUID(this.value);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public enum Type {
        INVITE,
        RETRACT_INVITE,
        REQUEST_ALLY,
        RETRACT_REQUEST,
        ACCEPT_ALLY,
        DENY_ALLY,
        DISSOLVE_ALLY,
        PROMOTE,
        DEMOTE,
        KICK,
        ACCEPT_INVITE,
        DENY_INVITE
    }
}