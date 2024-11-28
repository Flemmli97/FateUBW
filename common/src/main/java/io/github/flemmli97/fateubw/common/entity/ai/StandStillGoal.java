package io.github.flemmli97.fateubw.common.entity.ai;

import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class StandStillGoal extends Goal {

    private final BaseServant servant;

    public StandStillGoal(BaseServant servant) {
        this.servant = servant;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    @Override
    public boolean canContinueToUse() {
        return this.servant.isStaying();
    }

    @Override
    public boolean canUse() {
        if (this.servant.isInWaterOrBubble()) {
            return false;
        }
        if (!this.servant.isOnGround()) {
            return false;
        }
        return this.servant.isStaying();
    }

    @Override
    public void start() {
        this.servant.getNavigation().stop();
    }
}

