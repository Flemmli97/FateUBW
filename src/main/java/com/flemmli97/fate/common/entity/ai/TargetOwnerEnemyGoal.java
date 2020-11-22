package com.flemmli97.fate.common.entity.ai;


import com.flemmli97.tenshilib.api.entity.IOwnable;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;

import java.util.EnumSet;

public class TargetOwnerEnemyGoal<T extends CreatureEntity & IOwnable> extends TargetGoal {
    private final T tameable;
    private LivingEntity attackTarget;

    public TargetOwnerEnemyGoal(T entity) {
        super(entity, false);
        this.tameable = entity;
        this.setMutexFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean shouldExecute() {
        LivingEntity livingentity = this.tameable.getOwner();
        if (livingentity == null) {
            return false;
        } else {
            LivingEntity target = livingentity instanceof MobEntity ? ((MobEntity)livingentity).getAttackTarget() : livingentity.getLastAttackedEntity();
            if(target != null && !target.equals(this.attackTarget)) {
                this.attackTarget = target;
            }
            return this.isSuitableTarget(this.attackTarget, EntityPredicate.DEFAULT);
        }
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

