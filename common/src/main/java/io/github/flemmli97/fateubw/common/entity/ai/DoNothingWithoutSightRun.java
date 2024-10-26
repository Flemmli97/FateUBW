package io.github.flemmli97.fateubw.common.entity.ai;

import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.ActionRun;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;

public class DoNothingWithoutSightRun<T extends PathfinderMob & IAnimated> implements ActionRun<T> {

    @Override
    public boolean run(AnimatedAttackGoal<T> goal, LivingEntity target, AnimatedAction anim) {
        goal.attacker.getNavigation().stop();
        goal.attacker.lookAt(target, 30.0F, 30.0F);
        return goal.canSee;
    }
}
