package com.flemmli97.fate.common.entity.servant.ai;

import com.flemmli97.fate.common.entity.servant.EntityServant;
import net.minecraft.entity.ai.goal.Goal;

public class RetaliateGoal extends Goal {

    public RetaliateGoal(EntityServant servant) {
    }

    @Override
    public boolean shouldExecute() {
        return false;
    }
}
