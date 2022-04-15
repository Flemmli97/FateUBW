package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class S2CServantSync implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "s2c_servant_update");

    private final boolean none;
    private int entityID;

    private S2CServantSync(boolean none, int id) {
        this.entityID = id;
        this.none = none;
    }

    public S2CServantSync(BaseServant servant) {
        this.none = servant == null;
        if (servant != null)
            this.entityID = servant.getId();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.none);
        buf.writeInt(this.entityID);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public static S2CServantSync read(FriendlyByteBuf buf) {
        return new S2CServantSync(buf.readBoolean(), buf.readInt());
    }

    public static void handle(S2CServantSync pkt) {
        Player player = ClientHandler.clientPlayer();
        if (player != null) {
            Entity fromId = pkt.none ? null : player.level.getEntity(pkt.entityID);
            if (fromId instanceof BaseServant)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.setServant((BaseServant) fromId));
            else
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.setServant(null));
        }
    }
}