package io.github.flemmli97.fateubw.common.network;

import com.mojang.authlib.GameProfile;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.Set;

public class S2CWarData implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "s2c_war_data");

    private final Set<GameProfile> grailWarPlayers;

    private S2CWarData(Set<GameProfile> war) {
        this.grailWarPlayers = war;
    }

    public S2CWarData(ServerLevel world) {
        this.grailWarPlayers = new HashSet<>();
        GrailWarHandler.get(world.getServer()).players().forEach(uuid -> world.getServer().getProfileCache().get(uuid).ifPresent(this.grailWarPlayers::add));
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.grailWarPlayers.size());
        this.grailWarPlayers.forEach(prof -> {
            buf.writeUUID(prof.getId());
            buf.writeUtf(prof.getName());
        });
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public static S2CWarData read(FriendlyByteBuf buf) {
        Set<GameProfile> grailWarPlayers = new HashSet<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++)
            grailWarPlayers.add(new GameProfile(buf.readUUID(), buf.readUtf()));
        return new S2CWarData(grailWarPlayers);
    }

    public static void handle(S2CWarData pkt) {
        Player player = ClientHandler.clientPlayer();
        if (player == null)
            return;
        if (pkt.grailWarPlayers.stream().map(GameProfile::getId).anyMatch(uuid -> uuid.equals(player.getUUID())))
            ClientHandler.grailData(pkt.grailWarPlayers);
    }
}