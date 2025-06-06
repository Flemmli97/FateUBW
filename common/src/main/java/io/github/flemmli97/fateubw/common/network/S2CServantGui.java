package io.github.flemmli97.fateubw.common.network;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class S2CServantGui implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "s2c_servant_gui");

    private final boolean open;
    private final ServantMetaData data;

    private S2CServantGui(boolean open, ServantMetaData data) {
        this.open = open;
        this.data = data;
    }

    private S2CServantGui(ServerPlayer player, BaseServant servant, boolean open) {
        this.open = open;
        if (servant != null && player.getUUID().equals(servant.getOwnerUUID())) {
            this.data = ServantMetaData.of(servant, open);
            if (open)
                servant.setSentOwnerData(true);
        } else {
            this.data = null;
        }
    }

    public static void sendServantGui(ServerPlayer player, BaseServant servant) {
        sendServantGui(player, servant, true);
    }

    public static void sendServantGui(ServerPlayer player, BaseServant servant, boolean open) {
        NetworkCalls.INSTANCE.sendToClient(new S2CServantGui(player, servant, open), player);
    }

    public static S2CServantGui read(FriendlyByteBuf buf) {
        return new S2CServantGui(buf.readBoolean(), buf.readBoolean() ? new ServantMetaData(buf) : null);
    }

    public static void handle(S2CServantGui pkt) {
        Player player = ClientHandler.clientPlayer();
        if (player != null) {
            ClientHandler.displayCommandGui(pkt.data, pkt.open);
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.open);
        buf.writeBoolean(this.data != null);
        if (this.data != null)
            this.data.write(buf);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public record ServantMetaData(int entityId, EntityType<?> type,
                                  Optional<List<Pair<EquipmentSlot, ItemStack>>> equipment,
                                  List<SynchedEntityData.DataItem<?>> syncedData, int npCost) {

        public ServantMetaData(FriendlyByteBuf buf) {
            this(buf.readInt(), Registry.ENTITY_TYPE.get(buf.readResourceLocation()),
                    buf.readBoolean() ? Optional.of(buf.readList(b ->
                            Pair.of(b.readEnum(EquipmentSlot.class), b.readItem())
                    )) : Optional.empty(), SynchedEntityData.unpack(buf), buf.readInt());
        }

        public static ServantMetaData of(BaseServant servant, boolean full) {
            Optional<List<Pair<EquipmentSlot, ItemStack>>> equip;
            if (full) {
                ArrayList<Pair<EquipmentSlot, ItemStack>> list = Lists.newArrayList();
                for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                    ItemStack itemStack = servant.getItemBySlot(equipmentSlot);
                    if (itemStack.isEmpty()) continue;
                    list.add(Pair.of(equipmentSlot, itemStack.copy()));
                }
                equip = Optional.of(list);
            } else {
                equip = Optional.empty();
            }
            return new ServantMetaData(servant.getId(), servant.getType(), equip,
                    full ? servant.getEntityData().getAll() : servant.getEntityData().packDirty(), servant.props().hogouMana());
        }

        public void write(FriendlyByteBuf buf) {
            buf.writeInt(this.entityId);
            buf.writeResourceLocation(Registry.ENTITY_TYPE.getKey(this.type()));
            buf.writeBoolean(this.equipment.isPresent());
            this.equipment.ifPresent(list -> buf.writeCollection(list, (b, p) -> {
                b.writeEnum(p.getFirst());
                b.writeItem(p.getSecond());
            }));
            SynchedEntityData.pack(this.syncedData, buf);
            buf.writeInt(this.npCost);
        }
    }
}