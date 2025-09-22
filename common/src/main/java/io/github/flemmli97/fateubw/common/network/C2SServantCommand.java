package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.api.entity.CommandType;
import io.github.flemmli97.fateubw.api.entity.ServantLike;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import io.github.flemmli97.fateubw.platform.Platform;
import io.github.flemmli97.tenshilib.common.utils.HitResultUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;

public record C2SServantCommand(ActionType command, int entityId) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<C2SServantCommand> TYPE = new CustomPacketPayload.Type<>(Fate.modRes("c2s_servant_command"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SServantCommand> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public C2SServantCommand decode(RegistryFriendlyByteBuf buf) {
            return new C2SServantCommand(buf.readEnum(ActionType.class), buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, C2SServantCommand pkt) {
            buf.writeEnum(pkt.command);
            buf.writeInt(pkt.entityId);
        }
    };

    public static void handle(C2SServantCommand pkt, ServerPlayer sender) {
        if (sender == null)
            return;
        PlayerData data = Platform.INSTANCE.getPlayerData(sender);
        ServantLike<?> servant = getServant(sender, pkt.entityId);
        if (servant == null)
            return;
        switch (pkt.command) {
            case NORMAL -> {
                servant.onPlayerCommand(sender, pkt.command.as());
                sender.sendSystemMessage(Component.translatable("fateubw.chat.command.attackservant").withStyle(ChatFormatting.RED));
            }
            case AGGRESSIVE -> {
                servant.onPlayerCommand(sender, pkt.command.as());
                sender.sendSystemMessage(Component.translatable("fateubw.chat.command.attackall").withStyle(ChatFormatting.RED));
            }
            case DEFENSIVE -> {
                servant.onPlayerCommand(sender, pkt.command.as());
                sender.sendSystemMessage(Component.translatable("fateubw.chat.command.defensive").withStyle(ChatFormatting.RED));
            }
            case FOLLOW -> {
                servant.onPlayerCommand(sender, pkt.command.as());
                sender.sendSystemMessage(Component.translatable("fateubw.chat.command.follow").withStyle(ChatFormatting.RED));
            }
            case STAY -> {
                servant.onPlayerCommand(sender, pkt.command.as());
                sender.sendSystemMessage(Component.translatable("fateubw.chat.command.stay").withStyle(ChatFormatting.RED));
            }
            case GUARD -> {
                servant.onPlayerCommand(sender, pkt.command.as());
                sender.sendSystemMessage(Component.translatable("fateubw.chat.command.patrol").withStyle(ChatFormatting.RED));
            }
            case NP -> servant.onPlayerCommand(sender, pkt.command.as());
            case KILL -> {
                servant.onPlayerCommand(sender, pkt.command.as());
                sender.sendSystemMessage(Component.translatable("fateubw.chat.command.kill").withStyle(ChatFormatting.RED));
            }
            case TELEPORT -> {
                servant.get().randomTeleport(sender.getX(), sender.getY(), sender.getZ(), false);
                servant.get().setTarget(null);
                if (CommonConfig.punishTeleport) {
                    for (BaseServant others : sender.level().getEntitiesOfClass(BaseServant.class, sender.getBoundingBox().inflate(32)))
                        if (others != servant && !Utils.alliedTo(sender, others)) {
                            others.setTarget(sender);
                            others.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 1));
                            others.addEffect(new MobEffectInstance(MobEffects.HEAL, 2, 3));
                        }
                    sender.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1200, 1));
                    sender.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 600, 1));
                }
            }
            case BOOST -> {
                if (data.useCommandSeal()) {
                    for (MobEffectInstance effect : CommonConfig.npBoostEffect.potions())
                        servant.get().addEffect(effect);
                    sender.sendSystemMessage(Component.translatable("fateubw.chat.command.spell.success").withStyle(ChatFormatting.RED));
                } else
                    sender.sendSystemMessage(Component.translatable("fateubw.chat.command.spell.fail").withStyle(ChatFormatting.RED));
            }
            case TARGET -> {
                EntityHitResult res = HitResultUtils.calculateEntityFromLook(sender, 16);
                if (res != null && res.getEntity() instanceof LivingEntity target) {
                    if (!Utils.alliedTo(sender, target)) {
                        servant.get().setTarget(target);
                        for (BaseServant others : sender.level().getEntitiesOfClass(BaseServant.class, sender.getBoundingBox().inflate(32), s -> sender.getUUID().equals(s.getOwnerUUID()))) {
                            others.setTarget(target);
                        }
                    }
                }
            }
            case CLOSE -> servant.shouldScheduleEntityDataSync(false);
        }
    }

    public static ServantLike<?> getServant(ServerPlayer sender, int entityId) {
        if (entityId == -1) {
            ServantLike<?> servantLike = GrailWarHandler.get(sender.getServer())
                    .getServant(sender).orElse(null);
            if (servantLike != null && sender.getUUID().equals(servantLike.getOwnerUUID()))
                return servantLike;
            return null;
        }
        Entity entity = sender.level().getEntity(entityId);
        if (!(entity instanceof ServantLike<?> servant) || !sender.getUUID().equals(servant.getOwnerUUID()))
            return null;
        return servant;
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum ActionType {

        NORMAL,
        AGGRESSIVE,
        DEFENSIVE,
        FOLLOW,
        STAY,
        GUARD,
        NP,
        KILL,
        BOOST,
        TELEPORT,
        TARGET,
        CLOSE;

        public CommandType as() {
            return switch (this) {
                case NORMAL -> CommandType.NORMAL;
                case AGGRESSIVE -> CommandType.AGGRESSIVE;
                case DEFENSIVE -> CommandType.DEFENSIVE;
                case FOLLOW -> CommandType.FOLLOW;
                case STAY -> CommandType.STAY;
                case GUARD -> CommandType.GUARD;
                case NP -> CommandType.NP;
                case KILL -> CommandType.KILL;
                default -> null;
            };
        }
    }

}