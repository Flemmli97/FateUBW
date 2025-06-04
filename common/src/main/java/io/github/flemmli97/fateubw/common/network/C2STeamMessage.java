package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.world.TeamHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class C2STeamMessage implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "c2s_team_message");

    private final C2STeamMessage.Type type;
    private final String name;

    public C2STeamMessage(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    public static C2STeamMessage read(FriendlyByteBuf buf) {
        return new C2STeamMessage(buf.readEnum(Type.class), buf.readUtf());
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
    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(this.type);
        buf.writeUtf(this.name);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public enum Type {
        CREATE,
        LEAVE,
        RENAME,
        CLOSE
    }
}