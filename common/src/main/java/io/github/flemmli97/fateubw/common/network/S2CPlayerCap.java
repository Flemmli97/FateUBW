package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class S2CPlayerCap implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "s2c_player_data");

    public final int manaValue, commandSeals;
    public final UUID servantUUID;

    private S2CPlayerCap(int mana, int commandSeals, UUID servantUUID) {
        this.manaValue = mana;
        this.commandSeals = commandSeals;
        this.servantUUID = servantUUID;
    }

    public S2CPlayerCap(PlayerData cap) {
        this.manaValue = cap.getMana();
        this.commandSeals = cap.getCommandSeals();
        this.servantUUID = cap.getServantUUID();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.manaValue);
        buf.writeInt(this.commandSeals);
        boolean nonnull = this.servantUUID != null;
        buf.writeBoolean(nonnull);
        if (nonnull) {
            buf.writeUUID(this.servantUUID);
        }
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public static S2CPlayerCap read(FriendlyByteBuf buf) {
        return new S2CPlayerCap(buf.readInt(), buf.readInt(), buf.readBoolean() ? buf.readUUID() : null);
    }

    public static void handle(S2CPlayerCap pkt) {
        Player player = ClientHandler.clientPlayer();
        if (player != null)
            Platform.INSTANCE.getPlayerData(player).ifPresent(cap -> cap.handleClientUpdatePacket(pkt));
    }
}
