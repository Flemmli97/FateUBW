package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.AttackBBRender;
import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class S2CAttackDebug implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CAttackDebug> TYPE = new CustomPacketPayload.Type<>(Fate.modRes("s2c_attack_debug"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CAttackDebug> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CAttackDebug decode(RegistryFriendlyByteBuf buf) {
            return new S2CAttackDebug(OrientedBoundingBox.fromBuffer(buf), buf.readInt(), buf.readEnum(EnumAABBType.class));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CAttackDebug pkt) {
            pkt.obb.toBuffer(buf);
            buf.writeInt(pkt.duration);
            buf.writeEnum(pkt.aabbType);
        }
    };

    private final OrientedBoundingBox obb;
    private final int duration;
    private final EnumAABBType aabbType;

    public S2CAttackDebug(OrientedBoundingBox aabb) {
        this(aabb, 200, EnumAABBType.ATTACK);
    }

    public S2CAttackDebug(OrientedBoundingBox aabb, EnumAABBType type) {
        this(aabb, 200, type);
    }

    public S2CAttackDebug(OrientedBoundingBox aabb, int duration, EnumAABBType type) {
        this.obb = aabb;
        this.duration = duration;
        this.aabbType = type;
    }

    public static void sendDebugPacket(OrientedBoundingBox aabb, EnumAABBType type, Entity entity) {
        if (CommonConfig.debugAttack)
            LoaderNetwork.INSTANCE.sendToTracking(new S2CAttackDebug(aabb, type), entity);
    }

    public static void handle(S2CAttackDebug pkt, Player player) {
        if (!player.level().isClientSide || !CommonConfig.debugAttack)
            return;
        AttackBBRender.INST.addNewAABB(pkt.obb, pkt.duration, pkt.aabbType);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum EnumAABBType {

        ATTEMPT,
        ATTACK
    }
}
