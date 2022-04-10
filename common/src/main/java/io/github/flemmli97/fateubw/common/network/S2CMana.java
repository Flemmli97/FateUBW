package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class S2CMana implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "s2c_mana");

    private final int manaValue;

    private S2CMana(int mana) {
        this.manaValue = mana;
    }

    public S2CMana(PlayerData playerMana) {
        this.manaValue = playerMana.getMana();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.manaValue);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public static S2CMana read(FriendlyByteBuf buf) {
        return new S2CMana(buf.readInt());
    }

    public static void handle(S2CMana pkt) {
        Player player = ClientHandler.clientPlayer();
        if (player != null)
            Platform.INSTANCE.getPlayerData(player).ifPresent(cap -> cap.setMana(player, pkt.manaValue));
    }
}
