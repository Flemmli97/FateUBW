package com.flemmli97.fate.network;

import com.flemmli97.fate.client.ClientHandler;
import com.flemmli97.fate.common.world.TruceHandler;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Set;
import java.util.function.Supplier;

public class S2CTruceData {

    private final Set<GameProfile> truces;
    private final Set<GameProfile> pending;
    private final Set<GameProfile> requests;

    private S2CTruceData(Set<GameProfile> truce, Set<GameProfile> pending, Set<GameProfile> requests) {
        this.truces = truce;
        this.pending = pending;
        this.requests = requests;
    }

    public S2CTruceData(ServerWorld world, ServerPlayerEntity player) {
        this.truces = Sets.newHashSet();
        this.pending = Sets.newHashSet();
        this.requests = Sets.newHashSet();
        TruceHandler.get(world).get(player.getUniqueID()).forEach(uuid -> this.truces.add(world.getServer().getPlayerProfileCache().getProfileByUUID(uuid)));
        TruceHandler.get(world).pending(player).forEach(uuid -> this.pending.add(world.getServer().getPlayerProfileCache().getProfileByUUID(uuid)));
        TruceHandler.get(world).outgoingRequests(player).forEach(uuid -> this.requests.add(world.getServer().getPlayerProfileCache().getProfileByUUID(uuid)));
    }

    public static S2CTruceData read(PacketBuffer buf) {
        Set<GameProfile> truces = Sets.newHashSet();
        Set<GameProfile> pending = Sets.newHashSet();
        Set<GameProfile> requests = Sets.newHashSet();
        for (int i = 0; i < buf.readInt(); i++)
            truces.add(new GameProfile(buf.readUniqueId(), buf.readString()));
        for (int i = 0; i < buf.readInt(); i++)
            pending.add(new GameProfile(buf.readUniqueId(), buf.readString()));
        for (int i = 0; i < buf.readInt(); i++)
            requests.add(new GameProfile(buf.readUniqueId(), buf.readString()));
        return new S2CTruceData(truces, pending, requests);
    }

    public static void write(S2CTruceData pkt, PacketBuffer buf) {
        buf.writeInt(pkt.truces.size());
        pkt.truces.forEach(prof -> {
            buf.writeUniqueId(prof.getId());
            buf.writeString(prof.getName());
        });
        buf.writeInt(pkt.pending.size());
        pkt.pending.forEach(prof -> {
            buf.writeUniqueId(prof.getId());
            buf.writeString(prof.getName());
        });
        buf.writeInt(pkt.requests.size());
        pkt.requests.forEach(prof -> {
            buf.writeUniqueId(prof.getId());
            buf.writeString(prof.getName());
        });
    }

    public static void handle(S2CTruceData pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandler::clientPlayer);
            if (player == null)
                return;
            ClientHandler.truceData(pkt.truces, pkt.pending, pkt.requests);
        });
        ctx.get().setPacketHandled(true);
    }
}