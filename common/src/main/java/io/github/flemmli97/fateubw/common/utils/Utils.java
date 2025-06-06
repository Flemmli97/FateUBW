package io.github.flemmli97.fateubw.common.utils;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.registry.ModAttributes;
import io.github.flemmli97.fateubw.common.world.TeamHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class Utils {

    public static <T extends BaseServant> GoalAttackAction.Condition<T> npCheck() {
        return (goal, target, prev) -> (goal.attacker.canUseNP() && goal.attacker.getOwner() == null && goal.attacker.getMana() >= goal.attacker.props().hogouMana()) || goal.attacker.forcedNP;
    }

    public static Vec3 fromRelativeVector(Entity entity, Vec3 relative) {
        return fromRelativeVector(entity.getYRot(), relative);
    }

    public static Vec3 fromRelativeVector(float yRot, Vec3 relative) {
        Vec3 vec3 = relative.normalize();
        float f = Mth.sin(yRot * Mth.DEG_TO_RAD);
        float g = Mth.cos(yRot * Mth.DEG_TO_RAD);
        return new Vec3(vec3.x * g - vec3.z * f, vec3.y, vec3.z * g + vec3.x * f);
    }

    public static float getDamageAfterMagicAbsorb(LivingEntity entity, float damage) {
        if (entity.getAttribute(ModAttributes.MAGIC_RESISTANCE.get()) == null)
            return damage;
        return (float) (damage * entity.getAttribute(ModAttributes.MAGIC_RESISTANCE.get()).getValue());
    }

    public static float projectileReduce(LivingEntity entity, float damage) {
        if (entity.getAttribute(ModAttributes.PROJECTILE_RESISTANCE.get()) == null)
            return damage;
        float reduceAmount = (float) Mth.clamp(1 - entity.getAttribute(ModAttributes.PROJECTILE_RESISTANCE.get()).getValue() * 0.04, 0.1, 1);
        return damage * reduceAmount;
    }

    public static float magicDamage(@Nullable Entity entity) {
        if (!(entity instanceof LivingEntity living) || living.getAttribute(ModAttributes.MAGIC_ATTACK.get()) == null)
            return 0;
        return (float) living.getAttributeValue(ModAttributes.MAGIC_ATTACK.get());
    }

    public static boolean runWithInvulTimer(@Nullable Entity source, Entity target, Predicate<Entity> attack, int invulnerability) {
        int invul = target.invulnerableTime;
        boolean modified = false;
        boolean sourceCheck = true;
        if (target instanceof LivingEntity living && source != null) {
            if (living.getLastHurtByMob() != source) {
                sourceCheck = false;
            }
        }
        if (target.invulnerableTime + invulnerability <= 20 && sourceCheck) {
            target.invulnerableTime = Math.min(target.invulnerableTime, 10);
            modified = true;
        }
        boolean success = attack.test(target);
        if (!success && modified) {
            target.invulnerableTime = invul;
        }
        return success;
    }

    public static boolean alliedTo(@Nullable Entity entity, @Nullable Entity other) {
        if (entity == null || other == null)
            return false;
        if (entity == other)
            return false;
        if (entity.getServer() == null)
            return false;
        if (entity instanceof OwnableEntity ownable && other.getUUID().equals(ownable.getOwnerUUID()))
            return true;
        if (other instanceof OwnableEntity ownable && entity.getUUID().equals(ownable.getOwnerUUID()))
            return true;
        return TeamHandler.get(entity.getServer()).areAllies(entity, other);
    }

    public static Predicate<LivingEntity> servantTargetPredicate(Mob entity) {
        return target -> {
            if (target == entity || !entity.canAttack(target) || !target.canBeSeenAsEnemy() || Utils.alliedTo(entity, target))
                return false;
            if (target == entity.getTarget())
                return true;
            if (entity.hasPassenger(target) || entity.getVehicle() == target)
                return false;
            if (entity instanceof BaseServant servant) {
                if (target instanceof BaseServant || (target instanceof ServerPlayer)) //TODO: only when player is active grailwar participant
                    return true;
            }
            return target instanceof Enemy;
        };
    }

    public static List<Vec3> randomSidedPositions(LivingEntity thrower, int amount, int range) {
        Vec3 pos = thrower.position();
        Vec3 look = thrower.getLookAngle();
        Vec3 vert = new Vec3(0, 1, 0);
        if (-20 < thrower.getXRot() && thrower.getXRot() > 20)
            vert.xRot(thrower.getXRot());
        if (-20 > thrower.getXRot())
            vert.xRot(-20);
        if (20 < thrower.getXRot())
            vert.xRot(20);
        Vec3 hor = look.cross(vert);
        vert.normalize();
        hor.normalize();
        float rangeSq = (range - 1f) / 2 * (range - 1f) / 2;
        Set<Pair<Integer, Integer>> offsets = new HashSet<>();
        for (int i = 0; i < amount; i++) {
            Pair<Integer, Integer> offset = Pair.of(thrower.getRandom().nextInt(range) - (range - 1) / 2, thrower.getRandom().nextInt((range + 1) / 2));
            double distance = (offset.getFirst() * offset.getFirst() + offset.getSecond() * offset.getSecond());
            int retry = 0;
            while (distance > rangeSq || offsets.contains(offset) || (offset.getFirst() == 0 && offset.getSecond() == 0)) {
                offset = Pair.of(thrower.getRandom().nextInt(range) - (range - 1) / 2, thrower.getRandom().nextInt((range + 1) / 2));
                distance = (offset.getFirst() * offset.getFirst() + offset.getSecond() * offset.getSecond());
                if (++retry > 10)
                    break;
            }
            offsets.add(offset);
        }
        return offsets.stream().map(p -> pos.add(hor.scale(p.getFirst() * 2)).add(vert.scale(p.getSecond() * 2 + 1)))
                .toList();
    }
}
