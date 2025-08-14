//package io.github.flemmli97.fateubw.common.entity.ai;
//
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.PathfinderMob;
//import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
//import net.minecraft.world.entity.ai.targeting.TargetingConditions;
//
//import java.util.function.Predicate;
//
//public class HurtByTargetPredicateGoal extends HurtByTargetGoal {
//
//    private final Predicate<LivingEntity> pred;
//    private int timestamp;
//
//    public HurtByTargetPredicateGoal(PathfinderMob mob, Predicate<LivingEntity> pred) {
//        super(mob);
//        this.pred = pred;
//    }
//
//    @Override
//    protected boolean canAttack(LivingEntity target, TargetingConditions pred) {
//        return (this.mob.getTarget() == null || this.mob.getTarget() != target) &&
//                (this.pred == null || this.pred.test(target)) && super.canAttack(target, pred);
//    }
//
//    // Makes it so mob is not constantly focused on the last target
//    @Override
//    public boolean canContinueToUse() {
//        int i = this.mob.getLastHurtByMobTimestamp();
//        LivingEntity lastHurtBy = this.mob.getLastHurtByMob();
//        LivingEntity target = this.mob.getTarget();
//        if (lastHurtBy != null && target != lastHurtBy) {
//            float resetChance = Math.min(0.7f, (i - this.timestamp - 40) / 100.0f);
//            if (this.mob.getRandom().nextFloat() < resetChance) {
//                return false;
//            }
//        }
//        return super.canContinueToUse();
//    }
//
//    @Override
//    public void start() {
//        this.timestamp = this.mob.getLastHurtByMobTimestamp();
//        super.start();
//    }
//}
