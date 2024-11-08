package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.AttackBBRender;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import io.github.flemmli97.tenshilib.common.utils.OrientedBoundingBox;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class S2CAttackDebug implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "s2c_attack_debug");

    public static void sendDebugPacket(OrientedBoundingBox aabb, EnumAABBType type, Entity entity) {
        if (Config.Common.debugAttack)
            NetworkCalls.INSTANCE.sendToTracking(new S2CAttackDebug(aabb, type), entity);
    }

    private final OrientedBoundingBox obb;
    private final int duration;
    private final EnumAABBType type;

    public S2CAttackDebug(OrientedBoundingBox aabb) {
        this(aabb, 300, EnumAABBType.ATTACK);
    }

    public S2CAttackDebug(OrientedBoundingBox aabb, EnumAABBType type) {
        this(aabb, 300, type);
    }

    public S2CAttackDebug(OrientedBoundingBox aabb, int duration, EnumAABBType type) {
        this.obb = aabb;
        this.duration = duration;
        this.type = type;
    }

    public static S2CAttackDebug read(FriendlyByteBuf buf) {
        return new S2CAttackDebug(OrientedBoundingBox.fromBuffer(buf), buf.readInt(), buf.readEnum(EnumAABBType.class));
    }

    public static void handle(S2CAttackDebug pkt) {
        Player player = ClientHandler.clientPlayer();
        if (player == null || !Config.Common.debugAttack)
            return;
        AttackBBRender.INST.addNewAABB(pkt.obb, pkt.duration, pkt.type);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        this.obb.toBuffer(buf);
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
