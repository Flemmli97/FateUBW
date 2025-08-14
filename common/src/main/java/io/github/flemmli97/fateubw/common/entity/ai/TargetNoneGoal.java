//package io.github.flemmli97.fateubw.common.entity.ai;
//
//import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
//import net.minecraft.world.entity.ai.goal.Goal;
//
//import java.util.EnumSet;
//
//public class TargetNoneGoal extends Goal {
//
//    private final BaseServant servant;
//
//    public TargetNoneGoal(BaseServant servant) {
//        this.servant = servant;
//        this.setFlags(EnumSet.of(Flag.TARGET));
//    }
//
//    @Override
//    public boolean canContinueToUse() {
//        return this.canUse();
//    }
//
//    @Override
//    public boolean canUse() {
//        return this.servant.isStaying();
//    }
//
//    @Override
//    public void start() {
//        this.servant.setTarget(null);
//        this.servant.getNavigation().stop();
//    }
//}
