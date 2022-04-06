package io.github.flemmli97.fateubw.common.network;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;

public class C2SServantCommand implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(Fate.MODID, "c2s_servant_command");

    private final EnumServantUpdate command;

    public C2SServantCommand(EnumServantUpdate command) {
        this.command = command;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(this.command);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public static C2SServantCommand read(FriendlyByteBuf buf) {
        return new C2SServantCommand(buf.readEnum(EnumServantUpdate.class));
    }

    public static void handle(C2SServantCommand pkt, ServerPlayer sender) {
        PlayerData cap;
        BaseServant servant;
        if (sender == null || (cap = Platform.INSTANCE.getPlayerData(sender).orElse(null)) == null || (servant = cap.getServant(sender)) == null)
            return;
        switch (pkt.command) {
            case NORMAL:
                servant.updateAI(pkt.command);
                sender.sendMessage(new TranslatableComponent("chat.command.attackservant").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                break;
            case AGGRESSIVE:
                servant.updateAI(pkt.command);
                sender.sendMessage(new TranslatableComponent("chat.command.attackall").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                break;
            case DEFENSIVE:
                servant.updateAI(pkt.command);
                sender.sendMessage(new TranslatableComponent("chat.command.defensive").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                break;
            case FOLLOW:
                servant.updateAI(pkt.command);
                sender.sendMessage(new TranslatableComponent("chat.command.follow").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                break;
            case STAY:
                servant.updateAI(pkt.command);
                sender.sendMessage(new TranslatableComponent("chat.command.stay").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                break;
            case GUARD:
                servant.updateAI(pkt.command);
                sender.sendMessage(new TranslatableComponent("chat.command.patrol").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                break;
            case NP:
                if (!servant.forcedNP) {
                    if (!sender.isCreative()) {
                        if (cap.useMana(sender, servant.props().hogouMana()) && cap.useCommandSeal(sender)) {
                            sender.sendMessage(new TranslatableComponent("chat.command.npsuccess").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                            servant.forcedNP = true;
                        } else {
                            sender.sendMessage(new TranslatableComponent("chat.command.npfail").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                        }
                    } else {
                        sender.sendMessage(new TranslatableComponent("chat.command.npsuccess").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                        servant.forcedNP = true;
                    }
                } else {
                    sender.sendMessage(new TranslatableComponent("chat.command.npprep").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                }
                break;
            case KILL:
                servant.onKillOrder(sender, cap.useCommandSeal(sender));
                break;
            case FORFEIT: //Unused
                GrailWarHandler track = GrailWarHandler.get(sender.getServer());
                if (track.hasPlayer(sender)) {
                    track.removePlayer(sender);
                    servant.onForfeit(sender);
                }
                break;
            case TELEPORT:
                servant.randomTeleport(sender.getX(), sender.getY(), sender.getZ(), false);
                servant.setTarget(null);
                if (Config.Common.punishTeleport) {
                    for (BaseServant others : sender.level.getEntitiesOfClass(BaseServant.class, sender.getBoundingBox().inflate(32)))
                        if (others != servant && !Utils.inSameTeam(sender, others)) {
                            others.setTarget(sender);
                            others.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 1));
                            others.addEffect(new MobEffectInstance(MobEffects.HEAL, 2, 3));
                        }
                    sender.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1200, 1));
                    sender.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 600, 1));
                }
                break;
            case BOOST:
                if (cap.useCommandSeal(sender)) {
                    for (MobEffectInstance effect : Config.Common.npBoostEffect.potions())
                        servant.addEffect(effect);
                    sender.sendMessage(new TranslatableComponent("chat.command.spell.success").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                } else
                    sender.sendMessage(new TranslatableComponent("chat.command.spell.fail").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                break;
            case TARGET:
                EntityHitResult res = RayTraceUtils.calculateEntityFromLook(sender, 16);
                if (res != null && res.getEntity() instanceof LivingEntity e) {
                    if (e instanceof BaseServant) {
                        if (!Utils.inSameTeam(sender, (BaseServant) e))
                            servant.setTarget(e);
                    } else if (e instanceof Player) {
                        if (!Utils.inSameTeam(sender, e.getUUID()))
                            servant.setTarget(e);
                    } else
                        servant.setTarget(e);
                }
                break;
        }
    }
}