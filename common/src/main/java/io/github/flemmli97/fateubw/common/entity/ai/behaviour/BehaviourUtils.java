package io.github.flemmli97.fateubw.common.entity.ai.behaviour;

import io.github.flemmli97.tenshilib.common.entity.AOEAttackEntity;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToAttackTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.PlayAnimation;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.data.AnimationPlayHolder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.function.Predicate;
import java.util.function.ToIntBiFunction;

public class BehaviourUtils {

    public static <E extends Mob & AnimatedEntity> PlayAnimation<E> cooldownedPlay(ToIntBiFunction<String, E> cooldownFunc) {
        return new PlayAnimation<E>().withCallback(cooldownHandler(cooldownFunc))
                .withCallback(cooldownHandlerCont());
    }

    public static <E extends Mob & AnimatedEntity> PlayAnimation.OnStart<E> cooldownHandler(ToIntBiFunction<String, E> cooldownFunc) {
        return (animation, entity) -> {
            double calc = cooldownFunc.applyAsInt(animation, entity);
            calc += entity.getAnimationHandler().get(animation).length();
            int cooldown = Mth.ceil(calc);
            BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, cooldown);
        };
    }

    public static <E extends Mob & AnimatedEntity> PlayAnimation.OnContinue<E> cooldownHandlerCont() {
        return (animation, chains, entity) -> {
            // Extend cooldown by chained attacks
            double calc = BrainUtils.getTimeUntilMemoryExpires(entity, MemoryModuleType.ATTACK_COOLING_DOWN);
            if (chains != null) {
                for (AnimationPlayHolder.AnimationHolder chain : chains) {
                    calc += entity.getAnimationHandler().get(chain.animation()).length();
                }
                int cooldown = Mth.ceil(calc);
                BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, cooldown);
            }
        };
    }

    public static <E extends PathfinderMob & AOEAttackEntity & AnimatedEntity> MoveToAttackTarget<E> moveAttack() {
        Predicate<E> reached = entity -> {
            WalkTarget target = BrainUtils.getMemory(entity, MemoryModuleType.WALK_TARGET);
            if (target != null && target.getTarget() instanceof EntityTracker entityTracker) {
                double close = target.getCloseEnoughDist()
                        + entity.getBbWidth() * 0.5
                        + entityTracker.getEntity().getBbWidth() * 0.5;
                return entity.distanceToSqr(entityTracker.getEntity()) <= close * close;
            }
            return false;
        };
        MoveToAttackTarget<E> behaviour = new MoveToAttackTarget<>();
        behaviour.startCondition(entity -> !reached.test(entity));
        behaviour.stopIf(reached);
        return behaviour;
    }

    public static <E extends PathfinderMob> MoveToWalkTarget<E> moveTo() {
        Predicate<E> reached = entity -> {
            WalkTarget target = BrainUtils.getMemory(entity, MemoryModuleType.WALK_TARGET);
            if (target != null && target.getTarget() instanceof EntityTracker entityTracker) {
                Entity targetEntity = entityTracker.getEntity();
                if (entity.getBoundingBox().inflate(0.5).intersects(targetEntity.getBoundingBox()))
                    return true;
                double close = target.getCloseEnoughDist()
                        + entity.getBbWidth() * 0.5
                        + targetEntity.getBbWidth() * 0.5;
                return entity.distanceToSqr(targetEntity) <= close * close;
            }
            return false;
        };
        MoveToWalkTarget<E> behaviour = new MoveToWalkTarget<>();
        behaviour.startCondition(entity -> !reached.test(entity));
        behaviour.stopIf(reached);
        return behaviour;
    }
}
