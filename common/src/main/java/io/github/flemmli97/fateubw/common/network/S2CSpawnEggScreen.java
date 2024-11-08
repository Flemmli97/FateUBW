package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class S2CSpawnEggScreen implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "s2c_spawn_egg_screen");

    private final InteractionHand hand;

    public S2CSpawnEggScreen(InteractionHand hand) {
        this.hand = hand;
    }

    public static S2CSpawnEggScreen read(FriendlyByteBuf buf) {
        return new S2CSpawnEggScreen(buf.readEnum(InteractionHand.class));
    }

    public static void handle(S2CSpawnEggScreen pkt) {
        Player player = ClientHandler.clientPlayer();
        if (player == null)
            return;
        ClientHandler.openSpawneggGui(pkt.hand);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(this.hand);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}