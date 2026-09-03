package io.github.flemmli97.fateubw.common.entity.summons;

import io.github.flemmli97.fateubw.api.datapack.ServantExtraData;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.misc.StarfishShot;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateSounds;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.fateubw.mixin.CombatTrackerAccessor;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.CombatEntry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;

import java.util.List;
import java.util.function.Predicate;

public class LesserMonster extends SummonedEntity {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String ATTACK = BUILDER.add("attack", AnimationsBuilder.definition(0.76).marker("attack", 0.52));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private int livingTicks;

    private final AnimationHandler<LesserMonster> animationHandler = new AnimationHandler<>(this, ANIMS);

    private final int maxLivingTicks;

    private boolean ranged;

    public LesserMonster(EntityType<? extends LesserMonster> type, Level level) {
        super(type, level);
        this.maxLivingTicks = DatapackHandler.SERVANT_PROPS.get(FateEntities.GILLES.get())
                .getConfig(ServantExtraData.SUMMONED_MONSTER_DURATION);
    }

    @Override
    protected Predicate<LivingEntity> createTargetPredicate() {
        return target -> !Utils.alliedTo(this, target);
    }

    public void setRanged(boolean ranged) {
        this.ranged = ranged;
    }

    @Override
    public ExtendedBehaviour<? extends LesserMonster> getCombatAI() {
        return AttackBehaviourBuilder.<LesserMonster>create()
                .start(ATTACK).play(BehaviourUtils.cooldownedPlay(false, 10, 25))
                .condition(entity -> !entity.ranged)
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(BehaviourUtils.moveTo())
                .end(1)
                .start(ATTACK).play(BehaviourUtils.cooldownedPlay(false, 10, 25))
                .condition(entity -> entity.ranged)
                .prepare(new SetWalkTargetWithinDist<LesserMonster>()
                        .min(4).max(8)).prepareOptional(BehaviourUtils.moveTo())
                .end(1)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends LesserMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<LesserMonster>builder()
                .add(1, entity -> !entity.ranged, new SetWalkTargetToAttackTarget<>(), BehaviourUtils.moveTo())
                .add(1, entity -> entity.ranged, new SetWalkTargetWithinDist<LesserMonster>().min(4).max(8), BehaviourUtils.moveTo())
                .build();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            this.livingTicks++;
            if (this.livingTicks > this.maxLivingTicks)
                this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    public boolean removeWhenFarAway(double dist) {
        return true;
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(ATTACK) && anim.isAt("attack")) {
            LivingEntity target = this.getTarget();
            if (target != null) {
                if (this.ranged) {
                    this.shoot();
                } else if (this.getAttackBoundingBox().intersects(target.getBoundingBox())) {
                    this.doHurtTarget(target);
                }
            }
        }
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, double grow) {
        return new OrientedBoundingBox(this.getBoundingBox());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.ranged = tag.getBoolean("Ranged");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Ranged", this.ranged);
    }

    @Override
    public AnimationHandler<LesserMonster> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean res = super.doHurtTarget(target);
        if (res && target instanceof LivingEntity living) {
            List<CombatEntry> entries = ((CombatTrackerAccessor) living.getCombatTracker())
                    .getEntries();
            if (!entries.isEmpty() && entries.getLast().source().getEntity() == this) {
                float damage = Math.max(0, entries.getLast().damage());
                if (damage > 0 && this.getOwner() != null) {
                    LivingEntity owner = this.getOwner();
                    owner.heal(damage * 0.33f);
                    if (this.level() instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.HEART, owner.getX(), owner.getY() + owner.getBbHeight() + 0.5, owner.getZ(), 0, 0, 0.1, 0, 0);
                    }
                }
            }
        }
        return res;
    }

    public void shoot() {
        StarfishShot proj = new StarfishShot(this.level(), this);
        if (this.getTarget() != null) {
            Vec3 pos = this.getTarget().position();
            proj.shootAtPosition(pos.x(), this.getTarget().getY(0.5), pos.z(), 0.6f, 0);
        } else {
            proj.shoot(this, this.getXRot() - 15, this.getYRot(), 0, 0.6f, 0);
        }
        this.level().addFreshEntity(proj);
        this.playSound(FateSounds.MONSTER_SPIT.get(), 1, 1);
    }
}