package com.flemmli97.fate.common.entity.ai;


import com.flemmli97.tenshilib.api.entity.IOwnable;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;

import java.util.EnumSet;

public class TargetOwnerEnemyGoal<T extends CreatureEntity & IOwnable> extends TargetGoal {
    private final T tameable;
    private LivingEntity attacker;
    private int timestamp;

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
            this.attacker = livingentity.getLastAttackedEntity();
            int i = livingentity.getLastAttackedEntityTime();
            return i != this.timestamp && this.isSuitableTarget(this.attacker, EntityPredicate.DEFAULT);
        }
    }

    @Override
    public void startExecuting() {
        this.goalOwner.setAttackTarget(this.attacker);
        LivingEntity livingentity = this.tameable.getOwner();
        if (livingentity != null) {
            this.timestamp = livingentity.getLastAttackedEntityTime();
        }

        super.startExecuting();
    }
}

