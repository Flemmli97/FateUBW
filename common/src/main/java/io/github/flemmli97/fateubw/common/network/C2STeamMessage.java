package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.world.TeamHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public class C2STeamMessage implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<C2STeamMessage> TYPE = new CustomPacketPayload.Type<>(Fate.modRes("c2s_team_message"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2STeamMessage> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public C2STeamMessage decode(RegistryFriendlyByteBuf buf) {
            return new C2STeamMessage(buf.readEnum(Type.class), buf.readUtf());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, C2STeamMessage pkt) {
            buf.writeEnum(pkt.type);
            buf.writeUtf(pkt.name);
        }
    };

    private final C2STeamMessage.Type type;
    private final String name;

    public C2STeamMessage(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    public static void handle(C2STeamMessage pkt, ServerPlayer sender) {
        if (sender == null)
            return;
        TeamHandler handler = TeamHandler.get(sender.getServer());
        switch (pkt.type) {
            case CREATE -> handler.createTeam(sender, pkt.name);
            case LEAVE -> handler.removeFromTeam(sender, sender.getUUID());
            case RENAME -> handler.rename(sender, pkt.name);
            case CLOSE -> handler.removeListener(sender);
        }
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum Type {
        CREATE,
        LEAVE,
        RENAME,
        CLOSE
    }
}