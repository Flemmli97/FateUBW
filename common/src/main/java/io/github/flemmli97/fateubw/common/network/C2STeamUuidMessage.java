package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.world.TeamHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class C2STeamUuidMessage implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<C2STeamUuidMessage> TYPE = new CustomPacketPayload.Type<>(Fate.modRes("c2s_team_message_uuid"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2STeamUuidMessage> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public C2STeamUuidMessage decode(RegistryFriendlyByteBuf buf) {
            return new C2STeamUuidMessage(buf.readEnum(Type.class), buf.readUUID());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, C2STeamUuidMessage pkt) {
            buf.writeEnum(pkt.type);
            buf.writeUUID(pkt.value);
        }
    };

    private final Type type;
    private final UUID value;

    public C2STeamUuidMessage(Type type, UUID value) {
        this.type = type;
        this.value = value;
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
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
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