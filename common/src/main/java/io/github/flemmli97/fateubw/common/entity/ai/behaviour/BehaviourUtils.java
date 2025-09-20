package io.github.flemmli97.fateubw.common.entity.ai.behaviour;

import io.github.flemmli97.tenshilib.common.entity.AOEAttackEntity;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToAttackTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToWalkTargetWithSight;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.PlayAnimation;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetAnimationToPlay;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.data.AnimationPlayHolder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import io.github.flemmli97.tenshilib.common.registry.TenshilibMemoryModules;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.function.Predicate;
import java.util.function.ToIntBiFunction;

public class BehaviourUtils {

    public static <E extends Mob & AnimatedEntity> PlayAnimation<E> cooldownedPlay(boolean requireInRange, int min, int max) {
        return cooldownedPlay(requireInRange, (s, entity) -> min + entity.getRandom().nextInt(max - min));
    }

    public static <E extends Mob & AnimatedEntity> PlayAnimation<E> cooldownedPlay(Predicate<E> condition, int min, int max) {
        return cooldownedPlay(condition, (s, entity) -> min + entity.getRandom().nextInt(max - min));
    }

    public static <E extends Mob & AnimatedEntity> PlayAnimation<E> cooldownedPlay(boolean requireInRange, ToIntBiFunction<String, E> cooldownFunc) {
        return cooldownedPlay(requireInRange ? entity -> {
            AnimationPlayHolder<?> anim = BrainUtils.getMemory(entity, TenshilibMemoryModules.ANIMATION_TO_PLAY.get());
            Entity target = BrainUtils.getTargetOfEntity(entity);
            if (target == null)
                return false;
            if (entity instanceof AOEAttackEntity aoeEntity)
                return aoeEntity.prepareAttackBox(anim.animation(), target, -0.15, true)
                        .intersects(target.getBoundingBox());
            return target instanceof LivingEntity living && entity.isWithinMeleeAttackRange(living);
        } : null, cooldownFunc);
    }

    public static <E extends Mob & AnimatedEntity> PlayAnimation<E> cooldownedPlay(Predicate<E> condition, ToIntBiFunction<String, E> cooldownFunc) {
        PlayAnimation<E> behaviour = new PlayAnimation<E>().withCallback(cooldownHandler(cooldownFunc))
                .withCallback(cooldownHandlerCont());
        if (condition != null) {
            behaviour.startCondition(condition);
        }
        return behaviour;
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

    @SafeVarargs
    public static <E extends Mob & AnimatedEntity> SetAnimationToPlay<E> of(AnimationPlayHolder<E>... animations) {
        return new SetAnimationToPlay<>(animations);
    }

    public static <E extends LivingEntity> ToIntBiFunction<E, LivingEntity> closeEnough(int dist) {
        return (entity, target) -> dist;
    }

    public static <E extends LivingEntity> Predicate<E> ifCloserThan(double dist) {
        return entity -> {
            double distance = dist + entity.getBbWidth() * 0.5;
            LivingEntity target = BrainUtils.hasMemory(entity, MemoryModuleType.ATTACK_TARGET) ? BrainUtils.getTargetOfEntity(entity) : null;
            if (target == null && entity instanceof Mob mob) {
                target = mob.getTarget();
            }
            if (target == null)
                return false;
            distance += target.getBbWidth() * 0.5;
            return entity.distanceToSqr(target) <= distance * distance;
        };
    }

    public static <E extends LivingEntity> Predicate<E> ifFurtherThan(double dist) {
        return entity -> {
            double distance = dist + entity.getBbWidth() * 0.5;
            LivingEntity target = BrainUtils.hasMemory(entity, MemoryModuleType.ATTACK_TARGET) ? BrainUtils.getTargetOfEntity(entity) : null;
            if (target == null && entity instanceof Mob mob) {
                target = mob.getTarget();
            }
            if (target == null)
                return false;
            distance += target.getBbWidth() * 0.5;
            return entity.distanceToSqr(target) >= distance * distance;
        };
    }

    public static <E extends LivingEntity> Predicate<E> ifFurtherThan(double dist, double verticalDist) {
        return entity -> {
            double distance = dist + entity.getBbWidth() * 0.5;
            LivingEntity target = BrainUtils.hasMemory(entity, MemoryModuleType.ATTACK_TARGET) ? BrainUtils.getTargetOfEntity(entity) : null;
            if (target == null && entity instanceof Mob mob) {
                target = mob.getTarget();
            }
            if (target == null)
                return false;
            distance += target.getBbWidth() * 0.5;
            return entity.distanceToSqr(target.getX(), entity.getY(), target.getZ()) >= distance * distance
                    && Math.abs(entity.getY() - target.getY()) <= verticalDist;
        };
    }

    public static <E extends PathfinderMob & AOEAttackEntity & AnimatedEntity> MoveToAttackTarget<E> timedMoveAttack() {
        return timedMoveAttack(30, 40);
    }

    public static <E extends PathfinderMob & AOEAttackEntity & AnimatedEntity> MoveToAttackTarget<E> timedMoveAttack(int min, int max) {
        MoveToAttackTarget<E> behaviour = moveAttack();
        behaviour.runFor(e -> min + e.getRandom().nextInt(max - min));
        return behaviour;
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

    public static <E extends PathfinderMob> MoveToWalkTargetWithSight<E> moveTo() {
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
        MoveToWalkTargetWithSight<E> behaviour = new MoveToWalkTargetWithSight<>();
        behaviour.startCondition(entity -> !reached.test(entity));
        behaviour.stopIf(reached);
        return behaviour;
    }
}
