package io.github.flemmli97.fateubw.neoforge.event;

import io.github.flemmli97.fateubw.client.ClientCalls;
import io.github.flemmli97.fateubw.common.commands.CommandHandler;
import io.github.flemmli97.fateubw.common.event.EventCalls;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

public class EventHandler {

    @SubscribeEvent
    public static void command(RegisterCommandsEvent event) {
        CommandHandler.reg(event.getDispatcher());
    }

    @SubscribeEvent
    public static void joinWorld(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer)
            EventCalls.joinWorld(serverPlayer);
    }

    @SubscribeEvent
    public static void updateGrailWar(LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel serverLevel && serverLevel.dimension() == Level.OVERWORLD) {
            GrailWarHandler.get(serverLevel.getServer()).tick(serverLevel);
        }
    }

    @SubscribeEvent
    public static void clone(PlayerEvent.Clone event) {
        EventCalls.clone(event.getOriginal(), event.getEntity());
    }

    @SubscribeEvent
    public static void updateLivingTick(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity living) {
            EventCalls.tick(living);
            if (living.level().isClientSide)
                ClientCalls.tick(living);
        }
    }

    @SubscribeEvent
    public static void healingEvent(LivingHealEvent event) {
        if (!EventCalls.canHeal(event.getEntity()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void livingAttack(LivingIncomingDamageEvent event) {
        event.setCanceled(EventCalls.onHurt(event.getEntity(), event.getSource(), event.getAmount()));
    }

    @SubscribeEvent
    public static void damageCalculation(LivingDamageEvent.Pre event) {
        event.setNewDamage(EventCalls.damageCalculation(event.getEntity(), event.getSource(), event.getNewDamage()));
    }

    @SubscribeEvent
    public static void damagePost(LivingDamageEvent.Post event) {
        EventCalls.damagePost(event.getEntity(), event.getSource(), event.getNewDamage());
    }
}
