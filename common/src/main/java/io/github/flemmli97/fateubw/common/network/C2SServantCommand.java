package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import io.github.flemmli97.fateubw.platform.Platform;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;

public record C2SServantCommand(Type command, int entityId) implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "c2s_servant_command");

    public static C2SServantCommand read(FriendlyByteBuf buf) {
        return new C2SServantCommand(buf.readEnum(Type.class), buf.readInt());
    }

    public static void handle(C2SServantCommand pkt, ServerPlayer sender) {
        if (sender == null)
            return;
        PlayerData data = Platform.INSTANCE.getPlayerData(sender).orElse(null);
        if (data == null)
            return;
        BaseServant servant = getServant(sender, pkt.entityId);
        if (servant == null)
            return;
        switch (pkt.command) {
            case NORMAL:
                servant.updateAI(pkt.command);
                sender.sendMessage(new TranslatableComponent("fateubw.chat.command.attackservant").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                break;
            case AGGRESSIVE:
                servant.updateAI(pkt.command);
                sender.sendMessage(new TranslatableComponent("fateubw.chat.command.attackall").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                break;
            case DEFENSIVE:
                servant.updateAI(pkt.command);
                sender.sendMessage(new TranslatableComponent("fateubw.chat.command.defensive").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                break;
            case FOLLOW:
                servant.updateAI(pkt.command);
                sender.sendMessage(new TranslatableComponent("fateubw.chat.command.follow").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                break;
            case STAY:
                servant.updateAI(pkt.command);
                sender.sendMessage(new TranslatableComponent("fateubw.chat.command.stay").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                break;
            case GUARD:
                servant.updateAI(pkt.command);
                sender.sendMessage(new TranslatableComponent("fateubw.chat.command.patrol").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                break;
            case NP:
                if (!servant.forcedNP) {
                    if (!sender.isCreative()) {
                        if (data.useMana(sender, servant.props().hogouMana()) && data.useCommandSeal(sender)) {
                            sender.sendMessage(new TranslatableComponent("fateubw.chat.command.npsuccess").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                            servant.forcedNP = true;
                        } else {
                            sender.sendMessage(new TranslatableComponent("fateubw.chat.command.npfail").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                        }
                    } else {
                        sender.sendMessage(new TranslatableComponent("fateubw.chat.command.npsuccess").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                        servant.forcedNP = true;
                    }
                } else {
                    sender.sendMessage(new TranslatableComponent("fateubw.chat.command.npprep").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                }
                break;
            case KILL:
                servant.onKillOrder(sender, data.useCommandSeal(sender));
                break;
            case TELEPORT:
                servant.randomTeleport(sender.getX(), sender.getY(), sender.getZ(), false);
                servant.setTarget(null);
                if (CommonConfig.punishTeleport) {
                    for (BaseServant others : sender.level.getEntitiesOfClass(BaseServant.class, sender.getBoundingBox().inflate(32)))
                        if (others != servant && !Utils.alliedTo(sender, others)) {
                            others.setTarget(sender);
                            others.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 1));
                            others.addEffect(new MobEffectInstance(MobEffects.HEAL, 2, 3));
                        }
                    sender.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1200, 1));
                    sender.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 600, 1));
                }
                break;
            case BOOST:
                if (Platform.INSTANCE.getPlayerData(sender).map(d -> d.useCommandSeal(sender)).orElse(false)) {
                    for (MobEffectInstance effect : CommonConfig.npBoostEffect.potions())
                        servant.addEffect(effect);
                    sender.sendMessage(new TranslatableComponent("fateubw.chat.command.spell.success").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                } else
                    sender.sendMessage(new TranslatableComponent("fateubw.chat.command.spell.fail").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                break;
            case TARGET:
                EntityHitResult res = RayTraceUtils.calculateEntityFromLook(sender, 16);
                if (res != null && res.getEntity() instanceof LivingEntity target && !Utils.alliedTo(sender, target)) {
                    servant.setTarget(target);
                }
                break;
        }
    }

    public static BaseServant getServant(ServerPlayer sender, int entityId) {
        Entity entity = entityId == -1 ? GrailWarHandler.get(sender.getServer()).getServant(sender) : sender.level.getEntity(entityId);
        if (!(entity instanceof BaseServant servant) || !sender.getUUID().equals(servant.getOwnerUUID()))
            return null;
        return servant;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(this.command);
        buf.writeInt(this.entityId);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public enum Type {

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
        TARGET

    }
}