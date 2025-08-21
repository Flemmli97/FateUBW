package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.api.datapack.ServantExtraData;
import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.misc.MagicShot;
import io.github.flemmli97.fateubw.common.entity.summons.LesserMonster;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinition;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.StrafeTarget;
import net.tslat.smartbrainlib.util.BrainUtils;

public class Gilles extends BaseServant {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String CAST_1 = BUILDER.add("cast", AnimationsBuilder.definition(1.6).marker("attack", 0.95));
    public static final String CAST_2 = BUILDER.add("cast_2", AnimationsBuilder.definition(1.2).marker("attack", 0.8));

    public static final String NP_ATTACK = BUILDER.add("np", AnimationsBuilder.definition(1));
    public static final String SUMMON = BUILDER.add("summon", AnimationsBuilder.definition(2.));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<Gilles> animationHandler = new AnimationHandler<>(this, ANIMS);

    public Gilles(EntityType<? extends Gilles> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.GRIMOIRE.get()));
    }

    @Override
    public boolean hasOwnWeapon() {
        return this.getMainHandItem().is(FateItems.GRIMOIRE.get());
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCombatAI() {
        return AttackBehaviourBuilder.<Gilles>create()
                .start(CAST_1).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(16), 50, 80))
                .condition(Gilles::canSummonMore)
                .prepare(new SetWalkTargetWithinDist<Gilles>()
                        .min(7).max(12).speedMod((m, e) -> 1.1f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(13)
                .start(CAST_1).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(16), 50, 80))
                .condition(Gilles::canSummonMore)
                .end(10)
                .start(CAST_2).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(16), 40, 90))
                .prepare(new SetWalkTargetWithinDist<Gilles>()
                        .min(7).max(12).speedMod((m, e) -> 1.1f)).prepareOptional(BehaviourUtils.moveAttack())
                .end(11)
                .start(CAST_2).play(BehaviourUtils.cooldownedPlay(BehaviourUtils.ifCloserThan(16), 40, 90))
                .end(8)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseServant> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseServant>builder()
                .add(7, new StrafeTarget<BaseServant>()
                        .strafeDistance(11))
                .add(2, BehaviourUtils.ifCloserThan(7),
                        new SetWalkTargetAwayFromTarget<BaseServant>()
                                .radius(7), BehaviourUtils.moveTo()).build();
    }

    @Override
    public void setupAttack(AnimationDefinition anim) {
        BrainUtils.clearMemory(this, MemoryModuleType.LOOK_TARGET);
        this.getNavigation().stop();
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
    public AnimationHandler<Gilles> getAnimationHandler() {
        return this.animationHandler;
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
        return this.level().getEntitiesOfClass(LesserMonster.class, this.getBoundingBox().inflate(32),
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
                    for (int j = 0; j < 10; j++) {
                        double x = this.getX() + this.random.nextInt(18) - 9;
                        double y = this.getY() + this.random.nextInt(4) - 2.0;
                        double z = this.getZ() + this.random.nextInt(18) - 9;
                        minion.absMoveTo(x, y, z, Mth.wrapDegrees(this.level().random.nextFloat() * 360.0F), 0.0F);
                        if (this.level().noCollision(minion)) {
                            this.level().addFreshEntity(minion);
                            minion.setTarget(this.getTarget());
                            this.revealServant();
                            break;
                        }
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
