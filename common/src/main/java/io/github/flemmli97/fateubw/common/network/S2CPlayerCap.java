package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class S2CPlayerCap implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "s2c_player_data");

    public final int manaValue, commandSeals;

    private S2CPlayerCap(int mana, int commandSeals) {
        this.manaValue = mana;
        this.commandSeals = commandSeals;
    }

    public S2CPlayerCap(PlayerData cap) {
        this.manaValue = cap.getMana();
        this.commandSeals = cap.getCommandSeals();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.manaValue);
        buf.writeInt(this.commandSeals);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public static S2CPlayerCap read(FriendlyByteBuf buf) {
        return new S2CPlayerCap(buf.readInt(), buf.readInt());
    }

    public static void handle(S2CPlayerCap pkt) {
        Player player = ClientHandler.clientPlayer();
        if (player != null)
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.handleClientUpdatePacket(pkt));
    }
}
