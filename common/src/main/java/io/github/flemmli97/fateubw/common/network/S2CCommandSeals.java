package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class S2CCommandSeals implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "s2c_command_seal");

    private final int commandSeals;

    private S2CCommandSeals(int commandSeals) {
        this.commandSeals = commandSeals;
    }

    public S2CCommandSeals(PlayerData playerCap) {
        this.commandSeals = playerCap.getCommandSeals();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.commandSeals);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public static S2CCommandSeals read(FriendlyByteBuf buf) {
        return new S2CCommandSeals(buf.readInt());
    }

    public static void handle(S2CCommandSeals pkt) {
        Player player = ClientHandler.clientPlayer();
        if (player != null)
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.setCommandSeals(player, pkt.commandSeals));
    }
}
