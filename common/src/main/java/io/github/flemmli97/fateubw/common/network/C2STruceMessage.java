package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.world.TruceHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class C2STruceMessage implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "c2s_truce_response");

    public Type type;
    public UUID uuid;

    public C2STruceMessage(Type type, UUID uuid) {
        this.type = type;
        this.uuid = uuid;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(this.type);
        buf.writeUUID(this.uuid);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public static C2STruceMessage read(FriendlyByteBuf buf) {
        return new C2STruceMessage(buf.readEnum(Type.class), buf.readUUID());
    }

    public static void handle(C2STruceMessage pkt, ServerPlayer sender) {
        if (sender == null)
            return;
        switch (pkt.type) {
            case ACCEPT -> TruceHandler.get(sender.getServer()).accept(sender, pkt.uuid);
            case SEND -> TruceHandler.get(sender.getServer()).sendRequest(sender, pkt.uuid);
            case DENY -> TruceHandler.get(sender.getServer()).disband(sender, pkt.uuid);
        }
    }

    public enum Type {
        ACCEPT,
        SEND,
        DENY
    }
}