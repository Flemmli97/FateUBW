package io.github.flemmli97.fateubw.common.entity.servant.ai;

import io.github.flemmli97.fateubw.common.entity.IServantMinion;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.utils.Utils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

public class RetaliateGoal extends TargetGoal {

    private static final TargetingConditions pred = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();

    protected LivingEntity target;

    public RetaliateGoal(BaseServant servant) {
        super(servant, false);
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.mob.getTarget();
        LivingEntity revenge = this.mob.getLastHurtByMob();
        if (target == null || !target.isAlive() || revenge instanceof IServantMinion) {
            if (this.checkTarget(revenge) && target != revenge) {
                this.target = revenge;
                return true;
            }
        }
        return false;
    }

    @Override
    public void start() {
        this.mob.setTarget(this.target);
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        this.target = null;
    }

    protected boolean checkTarget(LivingEntity livingBase) {
        BaseServant servant = (BaseServant) this.mob;
        if (servant.getOwner() != null) {
            if (servant.getOwner() == livingBase) {
                return false;
            } else if (livingBase instanceof Player player && Utils.inSameTeam((ServerPlayer) servant.getOwner(), player.getUUID())) {
                return false;
            } else if (livingBase instanceof BaseServant && ((BaseServant) livingBase).getOwner() != null && Utils.inSameTeam((ServerPlayer) servant.getOwner(), ((BaseServant) livingBase).getOwnerUUID())) {
                return false;
            }
        }
        return super.canAttack(livingBase, pred);
    }
}