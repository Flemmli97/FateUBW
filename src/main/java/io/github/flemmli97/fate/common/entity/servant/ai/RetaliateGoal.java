package io.github.flemmli97.fate.common.entity.servant.ai;

import io.github.flemmli97.fate.common.entity.IServantMinion;
import io.github.flemmli97.fate.common.entity.servant.EntityServant;
import io.github.flemmli97.fate.common.utils.Utils;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

public class RetaliateGoal extends TargetGoal {

    private static final EntityPredicate pred = (new EntityPredicate()).setIgnoresLineOfSight().setUseInvisibilityCheck();

    protected LivingEntity target;

    public RetaliateGoal(EntityServant servant) {
        super(servant, false);
    }

    @Override
    public boolean shouldExecute() {
        LivingEntity target = this.goalOwner.getAttackTarget();
        LivingEntity revenge = this.goalOwner.getRevengeTarget();
        if (target == null || !target.isAlive() || revenge instanceof IServantMinion) {
            if (this.checkTarget(revenge) && target != revenge) {
                this.target = revenge;
                return true;
            }
        }
        return false;
    }

    @Override
    public void startExecuting() {
        this.goalOwner.setAttackTarget(this.target);
        super.startExecuting();
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.target = null;
    }

    protected boolean checkTarget(LivingEntity livingBase) {
        EntityServant servant = (EntityServant) this.goalOwner;
        if (servant.getOwner() != null) {
            if (servant.getOwner() == livingBase) {
                return false;
            } else if (livingBase instanceof PlayerEntity && Utils.inSameTeam((ServerPlayerEntity) servant.getOwner(), (PlayerEntity) livingBase)) {
                return false;
            } else if (livingBase instanceof EntityServant && ((EntityServant) livingBase).getOwner() != null && Utils.inSameTeam((ServerPlayerEntity) servant.getOwner(), ((EntityServant) livingBase).getOwner())) {
                return false;
            }
        }
        return super.isSuitableTarget(livingBase, pred);
    }
}