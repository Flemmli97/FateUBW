package io.github.flemmli97.fateubw.common.utils;

import io.github.flemmli97.fateubw.common.network.S2CAttackDebug;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.utils.HitResultUtils;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CombatUtils {

    public static void spinAttack(LivingEntity entity, AnimationState anim, double startSec, double endSec, float startRot, float endRot, float range,
                                  XRotMap xRot, Predicate<LivingEntity> predicate, Consumer<LivingEntity> cons) {
        if (!entity.level().isClientSide() && anim.isBetween(startSec, endSec)) {
            float start = (float) (startSec * 20);
            float end = (float) (endSec * 20);
            float progress = (float) anim.progress(start, end, 1, 0);
            float progressNext = (float) anim.progress(start, end, 1, 1);
            float add = endRot - startRot;
            XRotMap xRotFunc = xRot == null ? null : (yRot, partial) -> xRot.xRot(yRot, progress + partial);
            circlingAttack(entity, startRot + progress * add, startRot + progressNext * add, range, xRotFunc, predicate, cons);
        }
    }

    private static void circlingAttack(LivingEntity entity, float startRot, float endRot, float reach,
                                       XRotMap xRot,
                                       Predicate<LivingEntity> predicate, Consumer<LivingEntity> cons) {
        double incHalf = Math.asin(0.5 / reach) * Mth.RAD_TO_DEG;
        float minYRot = Math.min(startRot, endRot);
        float maxYRot = Math.max(startRot, endRot);
        AABB aabb = new AABB(-0.5, -0.02, 0, 0.5, entity.getBbHeight() + 0.02, reach);
        int rotationSteps = (int) ((maxYRot - minYRot) / (incHalf * 2)) + 2;
        float inc = (maxYRot - minYRot) / rotationSteps;
        Set<LivingEntity> entities = new HashSet<>();
        for (int steps = 0; steps <= rotationSteps; steps++) {
            float yRot = minYRot + inc * steps;
            OrientedBoundingBox obb = new OrientedBoundingBox(aabb, yRot, xRot == null ? 0 : xRot.xRot(yRot, (float) steps / rotationSteps), entity.position());
            entities.addAll(HitResultUtils.getEntities(entity, obb, false, EntityTypeTest.forClass(LivingEntity.class), predicate));
            S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, entity);
        }
        entities.forEach(cons);
    }

    public interface XRotMap {

        float xRot(float yRot, float progress);
    }
}
