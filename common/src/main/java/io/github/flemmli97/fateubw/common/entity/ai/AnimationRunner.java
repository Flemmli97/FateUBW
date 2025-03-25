package io.github.flemmli97.fateubw.common.entity.ai;

import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.ActionRun;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;

public class AnimationRunner<T extends PathfinderMob & IAnimated> implements ActionRun<T> {

    private final AnimatedAction animation;

    private boolean set;

    public AnimationRunner(AnimatedAction animation) {
        this.animation = animation;
    }

    @Override
    public boolean run(AnimatedAttackGoal<T> goal, LivingEntity target, AnimatedAction anim) {
        if (!this.set) {
            goal.attacker.getAnimationHandler().setAnimation(this.animation);
            this.set = true;
        }
        return goal.attacker.getAnimationHandler().getAnimation() == null;
    }
}
