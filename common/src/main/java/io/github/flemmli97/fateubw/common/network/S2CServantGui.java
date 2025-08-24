package io.github.flemmli97.fateubw.common.network;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.entity.ServantLike;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.mixin.ClientboundSetEntityDataPacketAccessor;
import io.github.flemmli97.fateubw.mixinhelper.SynchedEntityDataExtension;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class S2CServantGui implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CServantGui> TYPE = new CustomPacketPayload.Type<>(Fate.modRes("s2c_servant_gui"));
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CServantGui> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CServantGui decode(RegistryFriendlyByteBuf buf) {
            return new S2CServantGui(buf.readBoolean(), buf.readBoolean() ? ServantMetaData.STREAM_CODEC.decode(buf) : null);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CServantGui pkt) {
            buf.writeBoolean(pkt.open);
            buf.writeBoolean(pkt.data != null);
            if (pkt.data != null)
                ServantMetaData.STREAM_CODEC.encode(buf, pkt.data);
        }
    };

    private final boolean open;
    private final ServantMetaData data;

    private S2CServantGui(boolean open, ServantMetaData data) {
        this.open = open;
        this.data = data;
    }

    private S2CServantGui(ServerPlayer player, ServantLike<?> servant, boolean open) {
        this.open = open;
        if (servant != null && player.getUUID().equals(servant.getOwnerUUID())) {
            this.data = ServantMetaData.of(servant, open);
            if (open)
                servant.shouldScheduleEntityDataSync(true);
        } else {
            this.data = null;
        }
    }

    public static void sendServantGui(ServerPlayer player, ServantLike<?> servant) {
        sendServantGui(player, servant, true);
    }

    public static void sendServantGui(ServerPlayer player, ServantLike<?> servant, boolean open) {
        LoaderNetwork.INSTANCE.sendToPlayer(new S2CServantGui(player, servant, open), player);
    }

    public static void handle(S2CServantGui pkt) {
        ClientHandler.displayCommandGui(pkt.data, pkt.open);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public record ServantMetaData(int entityId, EntityType<?> type,
                                  Optional<List<Pair<EquipmentSlot, ItemStack>>> equipment,
                                  List<SynchedEntityData.DataValue<?>> syncedData, int npCost) {

        public static final StreamCodec<RegistryFriendlyByteBuf, ServantMetaData> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public ServantMetaData decode(RegistryFriendlyByteBuf buf) {
                return new ServantMetaData(buf.readInt(), BuiltInRegistries.ENTITY_TYPE.get(buf.readResourceLocation()),
                        buf.readBoolean() ? Optional.of(buf.readList(b ->
                                Pair.of(b.readEnum(EquipmentSlot.class), ItemStack.OPTIONAL_STREAM_CODEC.decode(buf))
                        )) : Optional.empty(), ClientboundSetEntityDataPacketAccessor.doUnpack(buf), buf.readInt());
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, ServantMetaData data) {
                buf.writeInt(data.entityId);
                buf.writeResourceLocation(BuiltInRegistries.ENTITY_TYPE.getKey(data.type()));
                buf.writeBoolean(data.equipment.isPresent());
                data.equipment.ifPresent(list -> buf.writeCollection(list, (b, p) -> {
                    b.writeEnum(p.getFirst());
                    ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, p.getSecond());
                }));
                ClientboundSetEntityDataPacketAccessor.doPack(data.syncedData, buf);
                buf.writeInt(data.npCost);
            }
        };

        public static ServantMetaData of(ServantLike<?> servantLike, boolean full) {
            Mob servant = servantLike.get();
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
                    full ? ((SynchedEntityDataExtension) servant.getEntityData()).fate$getAll() : servant.getEntityData().packDirty(), servantLike.props().manaCost());
        }
    }
}