package io.github.flemmli97.fateubw.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface Packet {

    void write(FriendlyByteBuf buf);

    ResourceLocation getID();
}
