package io.github.flemmli97.fateubw.common.entity.ai;

import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.ActionRun;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class TeleportRunner<T extends PathfinderMob & IAnimated> implements ActionRun<T> {

    private final double minDistSqr, maxDistSqr;
    private final int teleportMin, teleportMax;

    private boolean teleported;

    public TeleportRunner(double minDist, double maxDist, int teleportMin, int teleportMax) {
        this.minDistSqr = minDist * minDist;
        this.maxDistSqr = maxDist * maxDist;
        this.teleportMin = teleportMin;
        this.teleportMax = Math.max(this.teleportMin + 1, teleportMax);
    }

    @Override
    public boolean run(AnimatedAttackGoal<T> goal, LivingEntity target, AnimatedAction anim) {
        if (!this.teleported) {
            for (int i = 0; i < 32; ++i) {
                Vec3 posAway;
                if (!goal.attacker.isWithinRestriction() && goal.attacker.getRandom().nextFloat() < 0.5) {
                    posAway = DefaultRandomPos.getPosTowards(goal.attacker, goal.attacker.getRandom().nextInt(this.teleportMax - this.teleportMin) + this.teleportMin,
                            8, Vec3.atCenterOf(goal.attacker.getRestrictCenter()), 90 * Mth.DEG_TO_RAD);
                } else if (goal.distanceToTargetSq > this.maxDistSqr) {
                    posAway = DefaultRandomPos.getPosTowards(goal.attacker, goal.attacker.getRandom().nextInt(this.teleportMax - this.teleportMin) + this.teleportMin, 8, target.position(), 90 * Mth.DEG_TO_RAD);
                } else {
                    posAway = DefaultRandomPos.getPosAway(goal.attacker, goal.attacker.getRandom().nextInt(this.teleportMax - this.teleportMin) + this.teleportMin, 8, target.position());
                }
                if (posAway != null && target.distanceToSqr(posAway) >= this.minDistSqr) {
                    if (goal.attacker.isWithinRestriction(new BlockPos(posAway))) {
                        Utils.teleportTo(goal.attacker, posAway.x(), posAway.y(), posAway.z(),
                                SoundEvents.ENDERMAN_TELEPORT, ParticleTypes.WITCH);
                        this.teleported = true;
                    }
                    return true;
                }
            }
        }
        return true;
    }
}
