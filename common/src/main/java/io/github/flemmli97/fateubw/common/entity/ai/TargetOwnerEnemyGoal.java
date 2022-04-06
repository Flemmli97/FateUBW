package io.github.flemmli97.fateubw.common.entity.ai;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;

public class TargetOwnerEnemyGoal<T extends PathfinderMob & OwnableEntity> extends TargetGoal {

    private final T tameable;
    private LivingEntity attackTarget;
    protected TargetingConditions targetEntitySelector;

    public TargetOwnerEnemyGoal(T entity) {
        super(entity, false);
        this.tameable = entity;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        this.targetEntitySelector = TargetingConditions.forCombat().range(this.getFollowDistance())
                .selector(e -> e instanceof Mob && ((Mob) e).getTarget() == this.tameable.getOwner());
    }

    @Override
    public boolean canUse() {
        Entity entity = this.tameable.getOwner();
        LivingEntity currentTarget = this.mob.getTarget();
        if (!(entity instanceof LivingEntity owner) || currentTarget != null) {
            return false;
        }
        this.attackTarget = owner instanceof Mob ? ((Mob) owner).getTarget() : owner.getLastHurtMob();
        if (this.attackTarget != null)
            return this.canAttack(this.attackTarget, TargetingConditions.DEFAULT);
        this.attackTarget = this.mob.level.getNearestEntity(LivingEntity.class, this.targetEntitySelector, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ(), this.getTargetableArea(this.getFollowDistance()));
        return this.canAttack(this.attackTarget, TargetingConditions.DEFAULT);
    }

    protected AABB getTargetableArea(double targetDistance) {
        return this.mob.getBoundingBox().inflate(targetDistance, 4.0D, targetDistance);
    }

    @Override
    public void stop() {
        super.stop();
        this.attackTarget = null;
    }

    @Override
    public void start() {
        this.mob.setTarget(this.attackTarget);
        super.start();
    }
}

