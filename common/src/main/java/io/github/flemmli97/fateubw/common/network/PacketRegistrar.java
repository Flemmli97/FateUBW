package io.github.flemmli97.fateubw.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class PacketRegistrar {

    public static int registerServerPackets(ServerPacketRegister register, int id) {
        register.registerMessage(id++, C2SGrailReward.ID, C2SGrailReward.class, C2SGrailReward::write, C2SGrailReward::read, C2SGrailReward::handle);
        register.registerMessage(id++, C2SMessageGui.ID, C2SMessageGui.class, C2SMessageGui::write, C2SMessageGui::read, C2SMessageGui::handle);
        register.registerMessage(id++, C2SServantCommand.ID, C2SServantCommand.class, C2SServantCommand::write, C2SServantCommand::read, C2SServantCommand::handle);
        register.registerMessage(id++, C2SServantSpecial.ID, C2SServantSpecial.class, C2SServantSpecial::write, C2SServantSpecial::read, C2SServantSpecial::handle);
        register.registerMessage(id++, C2STruceMessage.ID, C2STruceMessage.class, C2STruceMessage::write, C2STruceMessage::read, C2STruceMessage::handle);
        return id;
    }

    public static int registerClientPackets(ClientPacketRegister register, int id) {
        register.registerMessage(id++, S2CAltarUpdate.ID, S2CAltarUpdate.class, S2CAltarUpdate::write, S2CAltarUpdate::read, S2CAltarUpdate::handle);
        register.registerMessage(id++, S2CCommandSeals.ID, S2CCommandSeals.class, S2CCommandSeals::write, S2CCommandSeals::read, S2CCommandSeals::handle);
        register.registerMessage(id++, S2CGrailGui.ID, S2CGrailGui.class, S2CGrailGui::write, S2CGrailGui::read, S2CGrailGui::handle);
        register.registerMessage(id++, S2CItemInUse.ID, S2CItemInUse.class, S2CItemInUse::write, S2CItemInUse::read, S2CItemInUse::handle);
        register.registerMessage(id++, S2CMana.ID, S2CMana.class, S2CMana::write, S2CMana::read, S2CMana::handle);
        register.registerMessage(id++, S2CPlayerCap.ID, S2CPlayerCap.class, S2CPlayerCap::write, S2CPlayerCap::read, S2CPlayerCap::handle);
        register.registerMessage(id++, S2CServantSync.ID, S2CServantSync.class, S2CServantSync::write, S2CServantSync::read, S2CServantSync::handle);
        register.registerMessage(id++, S2CTruceData.ID, S2CTruceData.class, S2CTruceData::write, S2CTruceData::read, S2CTruceData::handle);
        register.registerMessage(id++, S2CWarData.ID, S2CWarData.class, S2CWarData::write, S2CWarData::read, S2CWarData::handle);
        return id;
    }

    public interface ServerPacketRegister {
        <P> void registerMessage(int index, ResourceLocation id, Class<P> clss, BiConsumer<P, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, P> decoder, BiConsumer<P, ServerPlayer> handler);
    }

    public interface ClientPacketRegister {
        <P> void registerMessage(int index, ResourceLocation id, Class<P> clss, BiConsumer<P, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, P> decoder, Consumer<P> handler);
    }
}
