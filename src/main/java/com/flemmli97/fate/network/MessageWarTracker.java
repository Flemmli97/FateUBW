package com.flemmli97.fate.network;


import com.flemmli97.fate.client.ClientHandler;
import com.flemmli97.fate.common.world.GrailWarHandler;
import com.flemmli97.fate.common.world.TruceHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageWarTracker {

    private final CompoundNBT grailWarTracker;
    private final CompoundNBT truceMap;

    private MessageWarTracker(CompoundNBT war, CompoundNBT truce) {
        this.grailWarTracker = war;
        this.truceMap = truce;
    }

    public MessageWarTracker(ServerWorld world) {
        this.grailWarTracker = GrailWarHandler.get(world).write(new CompoundNBT());
        this.truceMap = TruceHandler.get(world).write(new CompoundNBT());
    }

    public static MessageWarTracker read(PacketBuffer buf) {
        return new MessageWarTracker(buf.readCompoundTag(), buf.readCompoundTag());
    }

    public static void write(MessageWarTracker pkt, PacketBuffer buf) {
        buf.writeCompoundTag(pkt.grailWarTracker);
        buf.writeCompoundTag(pkt.truceMap);
    }

    public static void handle(MessageWarTracker pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandler::clientPlayer);
            if (player == null)
                return;
            World world = player.world;
            /*GrailWarHandler tracker = GrailWarHandler.get(world);
            tracker.reset(world);
            tracker.read(pkt.grailWarTracker);
            TruceHandler truce = TruceHandler.get(world);
            truce.reset();
            truce.read(pkt.truceMap);*/
            //Fate.proxy.updateGuiTruce();
        });
        ctx.get().setPacketHandled(true);
    }
}