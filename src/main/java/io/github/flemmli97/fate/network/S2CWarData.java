package io.github.flemmli97.fate.network;

import com.mojang.authlib.GameProfile;
import io.github.flemmli97.fate.client.ClientHandler;
import io.github.flemmli97.fate.common.world.GrailWarHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class S2CWarData {

    private final Set<GameProfile> grailWarPlayers;

    private S2CWarData(Set<GameProfile> war) {
        this.grailWarPlayers = war;
    }

    public S2CWarData(ServerWorld world) {
        this.grailWarPlayers = new HashSet<>();
        GrailWarHandler.get(world).players().forEach(uuid -> this.grailWarPlayers.add(world.getServer().getPlayerProfileCache().getProfileByUUID(uuid)));
    }

    public static S2CWarData read(PacketBuffer buf) {
        Set<GameProfile> grailWarPlayers = new HashSet<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++)
            grailWarPlayers.add(new GameProfile(buf.readUniqueId(), buf.readString()));
        return new S2CWarData(grailWarPlayers);
    }

    public static void write(S2CWarData pkt, PacketBuffer buf) {
        buf.writeInt(pkt.grailWarPlayers.size());
        pkt.grailWarPlayers.forEach(prof -> {
            buf.writeUniqueId(prof.getId());
            buf.writeString(prof.getName());
        });
    }

    public static void handle(S2CWarData pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandler::clientPlayer);
            if (player == null)
                return;
            if (pkt.grailWarPlayers.stream().map(GameProfile::getId).anyMatch(uuid -> uuid.equals(player.getUniqueID())))
                ClientHandler.grailData(pkt.grailWarPlayers);
        });
        ctx.get().setPacketHandled(true);
    }
}