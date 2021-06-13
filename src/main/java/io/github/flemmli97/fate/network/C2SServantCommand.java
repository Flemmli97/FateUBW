package io.github.flemmli97.fate.network;

import io.github.flemmli97.fate.common.capability.IPlayer;
import io.github.flemmli97.fate.common.capability.PlayerCapProvider;
import io.github.flemmli97.fate.common.config.Config;
import io.github.flemmli97.fate.common.entity.servant.EntityServant;
import io.github.flemmli97.fate.common.utils.EnumServantUpdate;
import io.github.flemmli97.fate.common.utils.Utils;
import io.github.flemmli97.fate.common.world.GrailWarHandler;
import com.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Util;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SServantCommand {

    private final EnumServantUpdate command;

    public C2SServantCommand(EnumServantUpdate command) {
        this.command = command;
    }

    public static C2SServantCommand read(PacketBuffer buf) {
        return new C2SServantCommand(EnumServantUpdate.values()[buf.readInt()]);
    }

    public static void write(C2SServantCommand pkt, PacketBuffer buf) {
        buf.writeInt(pkt.command.ordinal());
    }

    public static void handle(C2SServantCommand pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            IPlayer cap;
            EntityServant servant;
            if (player == null || (cap = player.getCapability(PlayerCapProvider.PlayerCap).orElse(null)) == null || (servant = cap.getServant(player)) == null)
                return;
            switch (pkt.command) {
                case NORMAL:
                    servant.updateAI(pkt.command);
                    player.sendMessage(new TranslationTextComponent("chat.command.attackservant").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
                    break;
                case AGGRESSIVE:
                    servant.updateAI(pkt.command);
                    player.sendMessage(new TranslationTextComponent("chat.command.attackall").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
                    break;
                case DEFENSIVE:
                    servant.updateAI(pkt.command);
                    player.sendMessage(new TranslationTextComponent("chat.command.defensive").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
                    break;
                case FOLLOW:
                    servant.updateAI(pkt.command);
                    player.sendMessage(new TranslationTextComponent("chat.command.follow").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
                    break;
                case STAY:
                    servant.updateAI(pkt.command);
                    player.sendMessage(new TranslationTextComponent("chat.command.stay").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
                    break;
                case GUARD:
                    servant.updateAI(pkt.command);
                    player.sendMessage(new TranslationTextComponent("chat.command.patrol").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
                    break;
                case NP:
                    if (!servant.forcedNP) {
                        if (!player.isCreative()) {
                            if (cap.useMana(player, servant.props().hogouMana()) && cap.useCommandSeal(player)) {
                                player.sendMessage(new TranslationTextComponent("chat.command.npsuccess").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
                                servant.forcedNP = true;
                            } else {
                                player.sendMessage(new TranslationTextComponent("chat.command.npfail").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
                            }
                        } else {
                            player.sendMessage(new TranslationTextComponent("chat.command.npsuccess").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
                            servant.forcedNP = true;
                        }
                    } else {
                        player.sendMessage(new TranslationTextComponent("chat.command.npprep").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
                    }
                    break;
                case KILL:
                    servant.onKillOrder(player, cap.useCommandSeal(player));
                    break;
                case FORFEIT:
                    GrailWarHandler track = GrailWarHandler.get(player.getServerWorld());
                    if (track.hasPlayer(player)) {
                        track.removePlayer(player);
                        servant.onForfeit(player);
                    }
                    break;
                case TELEPORT:
                    servant.attemptTeleport(player.getPosX(), player.getPosY(), player.getPosZ(), false);
                    servant.setAttackTarget(null);
                    if (Config.Common.punishTeleport)
                        for (EntityServant others : player.world.getEntitiesWithinAABB(EntityServant.class, player.getBoundingBox().grow(32)))
                            if (others != servant && !Utils.inSameTeam(player, others)) {
                                others.setAttackTarget(player);
                                others.addPotionEffect(new EffectInstance(Effects.SPEED, 600, 1));
                            }
                    break;
                case BOOST:
                    if (cap.useCommandSeal(player)) {
                        for (EffectInstance effect : Config.Common.npBoostEffect.potions())
                            servant.addPotionEffect(effect);
                        player.sendMessage(new TranslationTextComponent("chat.command.spell.success").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
                    } else
                        player.sendMessage(new TranslationTextComponent("chat.command.spell.fail").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
                    break;
                case TARGET:
                    EntityRayTraceResult res = RayTraceUtils.calculateEntityFromLook(player, 16);
                    if (res != null && res.getEntity() instanceof LivingEntity) {
                        LivingEntity e = (LivingEntity) res.getEntity();
                        if (e instanceof EntityServant) {
                            if (!Utils.inSameTeam(player, (EntityServant) e)) ;
                            servant.setAttackTarget(e);
                        } else if (e instanceof PlayerEntity) {
                            if (!Utils.inSameTeam(player, (PlayerEntity) e)) ;
                            servant.setAttackTarget(e);
                        } else
                            servant.setAttackTarget(e);
                    }
                    break;
            }
        });
        ctx.get().setPacketHandled(true);
    }
}