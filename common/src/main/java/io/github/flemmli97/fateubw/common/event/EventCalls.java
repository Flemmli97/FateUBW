package io.github.flemmli97.fateubw.common.event;

import io.github.flemmli97.fateubw.common.network.S2CPlayerCap;
import io.github.flemmli97.fateubw.common.registry.ModAttributes;
import io.github.flemmli97.fateubw.common.registry.ModEffects;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.fateubw.common.world.GrailTeam;
import io.github.flemmli97.fateubw.common.world.TeamHandler;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

import java.util.List;

public class EventCalls {

    public static void joinWorld(ServerPlayer player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> NetworkCalls.INSTANCE.sendToClient(new S2CPlayerCap(data), player));
        TeamHandler teamHandler = TeamHandler.get(player.getServer());
        List<GrailTeam.ShortTeamInfo> invites = teamHandler.fetchInvitesFor(player);
        if (!invites.isEmpty()) {
            player.sendMessage(new TranslatableComponent("fateubw.chat.team.invite.pending",
                    String.join(",", invites.stream().map(GrailTeam.ShortTeamInfo::name).toList())).withStyle(ChatFormatting.GOLD), Util.NIL_UUID);
        }
        List<GrailTeam.ShortTeamInfo> requests = teamHandler.fetchRequestsFor(player, teamHandler.getTeamFor(player));
        if (!requests.isEmpty()) {
            player.sendMessage(new TranslatableComponent("fateubw.chat.team.alliance.pending",
                    String.join(",", requests.stream().map(GrailTeam.ShortTeamInfo::name).toList())).withStyle(ChatFormatting.GOLD), Util.NIL_UUID);
        }
    }

    public static void tick(LivingEntity entity) {
        if (entity instanceof ServerPlayer player)
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.tick(player));
        if (!entity.level.isClientSide) {
            if (entity.tickCount % 20 == 0) {
                boolean target = entity instanceof Mob mob && mob.getTarget() != null;
                AttributeInstance att = entity.getAttribute(target || entity.getCombatTracker().isInCombat() ?
                        ModAttributes.COMBAT_REGEN.get() : ModAttributes.PASSIVE_REGEN.get());
                if (att != null) {
                    entity.heal((float) att.getValue());
                }
            }
        }
    }

    public static boolean canHeal(LivingEntity entity) {
        return !entity.hasEffect(ModEffects.GAE_BUIDHE.get());
    }

    public static boolean onHurt(LivingEntity entity, DamageSource damageSource, float damage) {
        if (damageSource.isProjectile() && !damageSource.isBypassArmor()) {
            AttributeInstance att = entity.getAttribute(ModAttributes.PROJECTILE_BLOCK_CHANCE.get());
            if (att != null && entity.getRandom().nextFloat() < att.getValue()) {
                entity.getAttribute(ModAttributes.PROJECTILE_BLOCK_CHANCE.get());
                entity.level.playSound(null, entity.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.NEUTRAL, 1, 1);
                if (damageSource.getDirectEntity() != null)
                    damageSource.getDirectEntity().remove(Entity.RemovalReason.KILLED);
                return true;
            }
        }
        return false;
    }

    public static float damageCalculation(LivingEntity livingEntity, DamageSource damageSrc, float damageAmount) {
        if (damageSrc.isProjectile())
            damageAmount = Utils.projectileReduce(livingEntity, damageAmount);
        if (damageSrc.isMagic())
            damageAmount = Utils.getDamageAfterMagicAbsorb(livingEntity, damageAmount);
        return damageAmount;
    }
}