package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.AttackAABBRender;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class S2CAttackDebug implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "s2c_attack_debug");

    public static void sendDebugPacket(AABB aabb, EnumAABBType type, Entity entity) {
        if (Config.Common.debugAttack)
            NetworkCalls.INSTANCE.sendToTracking(new S2CAttackDebug(aabb, type), entity);
    }

    private final AABB aabb;
    private final int duration;
    private final EnumAABBType type;

    public S2CAttackDebug(AABB aabb) {
        this(aabb, 300, EnumAABBType.ATTACK);
    }

    public S2CAttackDebug(AABB aabb, EnumAABBType type) {
        this(aabb, 300, type);
    }

    public S2CAttackDebug(AABB aabb, int duration, EnumAABBType type) {
        this.aabb = aabb;
        this.duration = duration;
        this.type = type;
    }

    public static S2CAttackDebug read(FriendlyByteBuf buf) {
        AABB aabb = new AABB(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble());
        return new S2CAttackDebug(aabb, buf.readInt(), buf.readEnum(EnumAABBType.class));
    }

    public static void handle(S2CAttackDebug pkt) {
        Player player = ClientHandler.clientPlayer();
        if (player == null || !Config.Common.debugAttack)
            return;
        AttackAABBRender.INST.addNewAABB(pkt.aabb, pkt.duration, pkt.type);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeDouble(this.aabb.maxX);
        buf.writeDouble(this.aabb.maxY);
        buf.writeDouble(this.aabb.maxZ);
        buf.writeDouble(this.aabb.minX);
        buf.writeDouble(this.aabb.minY);
        buf.writeDouble(this.aabb.minZ);
        buf.writeInt(this.duration);
        buf.writeEnum(this.type);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public enum EnumAABBType {

        ATTEMPT,
        ATTACK
    }
}
