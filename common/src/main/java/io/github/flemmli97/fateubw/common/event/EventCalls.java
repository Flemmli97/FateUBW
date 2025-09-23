package io.github.flemmli97.fateubw.common.event;

import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.common.effects.PetrificationEffect;
import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.lib.FateTags;
import io.github.flemmli97.fateubw.common.network.S2CPlayerCap;
import io.github.flemmli97.fateubw.common.registry.FateAttributes;
import io.github.flemmli97.fateubw.common.registry.FateMobEffects;
import io.github.flemmli97.fateubw.common.utils.ExtendedCombatRules;
import io.github.flemmli97.fateubw.common.world.GrailTeam;
import io.github.flemmli97.fateubw.common.world.TeamHandler;
import io.github.flemmli97.fateubw.mixin.CombatTrackerAccessor;
import io.github.flemmli97.fateubw.platform.Platform;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class EventCalls {

    public static void joinWorld(ServerPlayer player) {
        LoaderNetwork.INSTANCE.sendToPlayer(new S2CPlayerCap(Platform.INSTANCE.getPlayerData(player)), player);
        TeamHandler teamHandler = TeamHandler.get(player.getServer());
        List<GrailTeam.ShortTeamInfo> invites = teamHandler.fetchInvitesFor(player);
        if (!invites.isEmpty()) {
            player.sendSystemMessage(Component.translatable("fateubw.chat.team.invite.pending",
                    String.join(",", invites.stream().map(GrailTeam.ShortTeamInfo::name).toList())).withStyle(ChatFormatting.GOLD));
        }
        List<GrailTeam.ShortTeamInfo> requests = teamHandler.fetchRequestsFor(player, teamHandler.getTeamFor(player));
        if (!requests.isEmpty()) {
            player.sendSystemMessage(Component.translatable("fateubw.chat.team.alliance.pending",
                    String.join(",", requests.stream().map(GrailTeam.ShortTeamInfo::name).toList())).withStyle(ChatFormatting.GOLD));
        }
    }

    public static void tick(LivingEntity entity) {
        if (entity instanceof ServerPlayer player)
            Platform.INSTANCE.getPlayerData(player).tick();
        if (!entity.level().isClientSide) {
            if (entity.isAlive() && entity.tickCount % 20 == 0) {
                boolean target = entity instanceof Mob mob && mob.getTarget() != null;
                AttributeInstance att = entity.getAttribute(target || ((CombatTrackerAccessor) entity.getCombatTracker()).getInCombat() ?
                        FateAttributes.COMBAT_REGEN.asHolder() : FateAttributes.PASSIVE_REGEN.asHolder());
                if (att != null) {
                    entity.heal((float) att.getValue());
                }
            }
        }
    }

    public static void clone(Player origin, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            PlayerData data = Platform.INSTANCE.getPlayerData(origin);
            Platform.INSTANCE.getPlayerData(player).load(data.save(new CompoundTag()));
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CPlayerCap(Platform.INSTANCE.getPlayerData(serverPlayer)), serverPlayer);
        }
    }

    public static boolean canHeal(LivingEntity entity) {
        return !entity.hasEffect(FateMobEffects.GAE_BUIDHE.asHolder());
    }

    public static boolean onHurt(LivingEntity entity, DamageSource damageSource, float damage) {
        if (damageSource.getEntity() instanceof LivingEntity) {
            MobEffectInstance eff = entity.getEffect(FateMobEffects.PETRIFICATION.asHolder());
            if (eff != null && eff.getAmplifier() >= PetrificationEffect.MAX_PROGRESS) {
                entity.removeEffect(eff.getEffect());
                entity.hurt(damageSource, damage * 2);
                return true;
            }
        }
        if (damageSource.is(DamageTypeTags.IS_PROJECTILE) && !damageSource.is(DamageTypeTags.BYPASSES_ARMOR)) {
            AttributeInstance att = entity.getAttribute(FateAttributes.PROJECTILE_BLOCK_CHANCE.asHolder());
            if (att != null && entity.getRandom().nextFloat() < att.getValue()) {
                entity.level().playSound(null, entity.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.NEUTRAL, 1, 1);
                if (damageSource.getDirectEntity() != null)
                    damageSource.getDirectEntity().remove(Entity.RemovalReason.KILLED);
                return true;
            }
        }
        return false;
    }

    public static float damageCalculation(LivingEntity livingEntity, DamageSource damageSrc, float damageAmount) {
        if (damageSrc.is(DamageTypeTags.IS_PROJECTILE))
            damageAmount = ExtendedCombatRules.projectileReduce(livingEntity, damageAmount);
        if (damageSrc.is(FateTags.DamageTypes.IS_MAGIC))
            damageAmount = ExtendedCombatRules.getDamageAfterMagicProtection(livingEntity, damageAmount);
        return damageAmount;
    }

    public static void damagePost(LivingEntity livingEntity, DamageSource damageSrc, float damageAmount) {
        if (damageAmount > 0) {
            if (damageSrc.getEntity() instanceof BaseServant servant && damageSrc.getDirectEntity() != null) {
                if (damageSrc.getEntity() == damageSrc.getDirectEntity() || damageSrc.getDirectEntity().getType().is(FateTags.EntityTypes.MANA_LEECHING_PROJECTILE)) {
                    servant.regenMana(damageSrc.getDirectEntity());
                }
            }
            if (damageSrc.getEntity() != null && damageSrc.getEntity() instanceof OwnableEntity ownable
                    && damageSrc.getEntity().getType().is(FateTags.EntityTypes.MANA_LEECHING_SUMMONS)) {
                if (ownable.getOwner() instanceof BaseServant servant) {
                    servant.regenMana(damageSrc.getDirectEntity());
                }
            }
        }
    }
}