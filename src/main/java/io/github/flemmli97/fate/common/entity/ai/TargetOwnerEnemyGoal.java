package io.github.flemmli97.fate.common.entity.ai;


import com.flemmli97.tenshilib.api.entity.IOwnable;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.EnumSet;

public class TargetOwnerEnemyGoal<T extends CreatureEntity & IOwnable<? extends LivingEntity>> extends TargetGoal {

    private final T tameable;
    private LivingEntity attackTarget;
    protected EntityPredicate targetEntitySelector;

    public TargetOwnerEnemyGoal(T entity) {
        super(entity, false);
        this.tameable = entity;
        this.setMutexFlags(EnumSet.of(Goal.Flag.TARGET));
        this.targetEntitySelector = new EntityPredicate().setDistance(this.getTargetDistance())
                .setCustomPredicate(e -> e instanceof MobEntity && ((MobEntity) e).getAttackTarget() == this.tameable.getOwner());
    }

    @Override
    public boolean shouldExecute() {
        LivingEntity livingentity = this.tameable.getOwner();
        LivingEntity currentTarget = this.goalOwner.getAttackTarget();
        if (livingentity == null || currentTarget != null) {
            return false;
        }
        this.attackTarget = livingentity instanceof MobEntity ? ((MobEntity) livingentity).getAttackTarget() : livingentity.getLastAttackedEntity();
        if (this.attackTarget != null)
            return this.isSuitableTarget(this.attackTarget, EntityPredicate.DEFAULT);
        this.attackTarget = this.goalOwner.world.getClosestEntity(LivingEntity.class, this.targetEntitySelector, this.goalOwner, this.goalOwner.getPosX(), this.goalOwner.getPosYEye(), this.goalOwner.getPosZ(), this.getTargetableArea(this.getTargetDistance()));
        return this.isSuitableTarget(this.attackTarget, EntityPredicate.DEFAULT);
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.goalOwner.getBoundingBox().grow(targetDistance, 4.0D, targetDistance);
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.attackTarget = null;
    }

    @Override
    public void startExecuting() {
        this.goalOwner.setAttackTarget(this.attackTarget);
        super.startExecuting();
    }
}

