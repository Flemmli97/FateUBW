package io.github.flemmli97.fateubw.common.network;

import com.mojang.authlib.GameProfile;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.common.world.TruceHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;

public class S2CTruceData implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "s2c_truce_data");

    private final Set<GameProfile> truces;
    private final Set<GameProfile> pending;
    private final Set<GameProfile> requests;

    private S2CTruceData(Set<GameProfile> truce, Set<GameProfile> pending, Set<GameProfile> requests) {
        this.truces = truce;
        this.pending = pending;
        this.requests = requests;
    }

    public S2CTruceData(ServerLevel world, ServerPlayer player) {
        this.truces = new HashSet<>();
        this.pending = new HashSet<>();
        this.requests = new HashSet<>();
        TruceHandler.get(world.getServer()).get(player.getUUID()).forEach(uuid -> world.getServer().getProfileCache().get(uuid).ifPresent(this.truces::add));
        TruceHandler.get(world.getServer()).pending(player).forEach(uuid -> world.getServer().getProfileCache().get(uuid).ifPresent(this.pending::add));
        TruceHandler.get(world.getServer()).outgoingRequests(player).forEach(uuid -> world.getServer().getProfileCache().get(uuid).ifPresent(this.requests::add));
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.truces.size());
        this.truces.forEach(prof -> {
            buf.writeUUID(prof.getId());
            buf.writeUtf(prof.getName());
        });
        buf.writeInt(this.pending.size());
        this.pending.forEach(prof -> {
            buf.writeUUID(prof.getId());
            buf.writeUtf(prof.getName());
        });
        buf.writeInt(this.requests.size());
        this.requests.forEach(prof -> {
            buf.writeUUID(prof.getId());
            buf.writeUtf(prof.getName());
        });
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public static S2CTruceData read(FriendlyByteBuf buf) {
        Set<GameProfile> truces = new HashSet<>();
        Set<GameProfile> pending = new HashSet<>();
        Set<GameProfile> requests = new HashSet<>();
        for (int i = 0; i < buf.readInt(); i++)
            truces.add(new GameProfile(buf.readUUID(), buf.readUtf()));
        for (int i = 0; i < buf.readInt(); i++)
            pending.add(new GameProfile(buf.readUUID(), buf.readUtf()));
        for (int i = 0; i < buf.readInt(); i++)
            requests.add(new GameProfile(buf.readUUID(), buf.readUtf()));
        return new S2CTruceData(truces, pending, requests);
    }

    public static class Handler {
        public static void handle(S2CTruceData pkt) {
            ClientHandler.truceData(pkt.truces, pkt.pending, pkt.requests);
        }
    }
}