package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.api.datapack.ServantExtraData;
import io.github.flemmli97.fateubw.common.entity.misc.MagicShot;
import io.github.flemmli97.fateubw.common.entity.summons.LesserMonster;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntityGilles extends BaseServant {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String CAST_1 = BUILDER.add("cast", AnimationsBuilder.definition(1.6).marker("attack", 0.95));
    public static final String CAST_2 = BUILDER.add("cast_2", AnimationsBuilder.definition(1.2).marker("attack", 0.8));

    public static final String NP_ATTACK = BUILDER.add("np", AnimationsBuilder.definition(1));
    public static final String SUMMON = BUILDER.add("summon", AnimationsBuilder.definition(2.));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

//    public static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityGilles>>> ATTACKS = List.of(
//            WeightedEntry.wrap(new GoalAttackAction<EntityGilles>(EntityGilles.CAST_1)
//                    .cooldown(e -> e.getRandom().nextInt(70) + 30)
//                    .withCondition(((goal, target, previous) -> goal.attacker.canSummonMore()))
//                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(7, 10, 1.1))), 13),
//            WeightedEntry.wrap(new GoalAttackAction<EntityGilles>(EntityGilles.CAST_1)
//                    .cooldown(e -> e.getRandom().nextInt(70) + 30)
//                    .withCondition(((goal, target, previous) -> goal.attacker.canSummonMore()))
//                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 10),
//            WeightedEntry.wrap(new GoalAttackAction<EntityGilles>(EntityGilles.CAST_2)
//                    .cooldown(e -> e.getRandom().nextInt(70) + 30)
//                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(7, 10, 1.1))), 11),
//            WeightedEntry.wrap(new GoalAttackAction<EntityGilles>(EntityGilles.CAST_2)
//                    .cooldown(e -> e.getRandom().nextInt(70) + 30)
//                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 8),
//            WeightedEntry.wrap(new GoalAttackAction<EntityGilles>(EntityGilles.CAST_1)
//                    .cooldown(e -> e.getRandom().nextInt(70) + 30)
//                    .withCondition(((goal, target, previous) -> goal.attacker.canSummonMore()))
//                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(7, 10, 1.1))), 9),
//            WeightedEntry.wrap(new GoalAttackAction<EntityGilles>(EntityGilles.CAST_2)
//                    .cooldown(e -> e.getRandom().nextInt(70) + 30)
//                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(7, 10, 1.1))), 9)
//    );
//    public static final List<WeightedEntry.Wrapper<IdleAction<EntityGilles>>> IDLE_ACTIONS = List.of(
//            WeightedEntry.wrap(new IdleAction<>(() -> new StrafingRunner<>(12, 7, 1, 0.3f)), 6),
//            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<>(1, 1, 6)), 4)
//    );
//
//    public final AnimatedAttackGoal<EntityGilles> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    private final AnimationHandler<EntityGilles> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityGilles(EntityType<? extends EntityGilles> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.GRIMOIRE.get()));
    }

    @Override
    public AnimationHandler<EntityGilles> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(CAST_1)) {
            LivingEntity target = this.getTarget();
            if (target != null) {
                this.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }
            if (anim.isAt("attack")) {
                this.attackWithRangedAttack();
            }
        }
        if (anim.is(CAST_2)) {
            LivingEntity target = this.getTarget();
            if (target != null) {
                this.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }
            if (anim.isAt("attack")) {
                this.shoot();
            }
        }
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDeadOrDying() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
        }
    }

    public void cthulhu() {
        if (!this.level().isClientSide) {
            //EntityMonster minion = new EntityMonster(this.world, this);
            //this.world.spawnEntity(minion);
            //minion.setAttackTarget(this.getAttackTarget());
        }
    }

    protected boolean canSummonMore() {
        return this.level().getEntitiesOfClass(LesserMonster.class, this.getBoundingBox().inflate(16),
                monster -> this.getUUID().equals(monster.getOwnerUUID())).size() < this.props().getConfig(ServantExtraData.GILLES_MONSTER_MAX);
    }

    public void attackWithRangedAttack() {
        if (!this.level().isClientSide) {
            if (this.canSummonMore()) {
                int amount = 1;
                if (this.getHealth() < 0.5 * this.getMaxHealth())
                    amount = 1 + this.getRandom().nextInt(3);
                for (int i = 0; i < amount; i++) {
                    LesserMonster minion = new LesserMonster(this.level(), this);
                    BlockPos pos = RandomPos.generateRandomDirection(this.getRandom(), 9, 4);
                    if (pos != null) {
                        minion.moveTo(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, Mth.wrapDegrees(this.level().random.nextFloat() * 360.0F), 0.0F);
                        this.level().addFreshEntity(minion);
                        minion.setTarget(this.getTarget());
                        this.revealServant();
                    }
                }
            }
        }
    }

    public void shoot() {
        MagicShot proj = new MagicShot(this.level(), this);
        if (this.getTarget() != null) {
            proj.shootAtEntity(this.getTarget(), 1, 0);
        } else {
            proj.shoot(this, this.getXRot(), this.getYRot(), 0, 1, 0);
        }
        this.level().addFreshEntity(proj);
    }

    @Override
    protected String getSummonAnimation() {
        return SUMMON;
    }
}
