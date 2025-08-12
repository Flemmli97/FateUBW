package io.github.flemmli97.fateubw.mixin;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(ClientboundSetEntityDataPacket.class)
public interface ClientboundSetEntityDataPacketAccessor {

    @Invoker("pack")
    static void doPack(List<SynchedEntityData.DataValue<?>> dataValues, RegistryFriendlyByteBuf buffer) {
        throw new IllegalStateException();
    }

    @Invoker("unpack")
    static List<SynchedEntityData.DataValue<?>> doUnpack(RegistryFriendlyByteBuf buffer) {
        throw new IllegalStateException();
    }
}
