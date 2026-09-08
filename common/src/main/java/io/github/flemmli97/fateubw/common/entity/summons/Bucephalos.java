package io.github.flemmli97.fateubw.common.entity.summons;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.ai.behaviour.BehaviourUtils;
import io.github.flemmli97.fateubw.common.entity.utils.MoveType;
import io.github.flemmli97.fateubw.common.network.S2CAttackDebug;
import io.github.flemmli97.fateubw.common.network.S2CScreenShake;
import io.github.flemmli97.fateubw.common.utils.MathsHelper;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.entity.data.SyncedDataContainer;
import io.github.flemmli97.tenshilib.common.entity.data.SyncedMobDataHandler;
import io.github.flemmli97.tenshilib.common.registry.TenshilibSyncableEntityDatas;
import io.github.flemmli97.tenshilib.common.utils.TypedResource;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;

import java.util.List;
import java.util.function.Consumer;

public class Bucephalos extends SummonedEntity implements SyncedMobDataHandler {

    private static final float ATTACK_MOVE_SPEED = 1.5f;

    public static final TypedResource<Vec3> CHARGE_MOTION = new TypedResource<>(Fate.modRes("charge_motion"));

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String STOMP = BUILDER.add("stomp", AnimationsBuilder.definition(0.76).marker("attack", 0.52));
    public static final String HEADBUTT = BUILDER.add("headbutt", AnimationsBuilder.definition(0.84).marker("attack", 0.44));
    public static final String CHARGE = BUILDER.add("charge", AnimationsBuilder.definition(1.44)
            .marker("charge_start", 0.64).marker("charge_end", 1.16));

    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<Bucephalos> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                if (!this.level().isClientSide && anim != null && anim.is(CHARGE)) {
                    this.getDataContainer().set(CHARGE_MOTION, null);
                }
                return false;
            });

    private final SyncedDataContainer<Bucephalos> syncedDataContainer = SyncedDataContainer.builder(this)
            .define(CHARGE_MOTION, TenshilibSyncableEntityDatas.VEC_3.get(), null).build();

    private int gallopSoundCounter;

    public Bucephalos(EntityType<? extends Bucephalos> type, Level level) {
        super(type, level);
    }

    @Override
    public SyncedDataContainer<?> getDataContainer() {
        return this.syncedDataContainer;
    }

    @Override
    public ExtendedBehaviour<? extends Bucephalos> getCombatAI() {
        return AttackBehaviourBuilder.<Bucephalos>create()
                .start(STOMP).play(BehaviourUtils.cooldownedPlay(true, 20, 45))
                .prepare(new SetWalkTargetToAttackTarget<Bucephalos>().speedMod((owner, target) -> ATTACK_MOVE_SPEED)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(6)
                .start(HEADBUTT).play(BehaviourUtils.cooldownedPlay(true, 20, 45))
                .prepare(new SetWalkTargetToAttackTarget<Bucephalos>().speedMod((owner, target) -> ATTACK_MOVE_SPEED)).prepareOptional(BehaviourUtils.timedMoveAttack())
                .end(6)
                .start(CHARGE).play(BehaviourUtils.cooldownedPlay(false, 20, 50))
                .condition(BehaviourUtils.ifFurtherThan(5))
                .prepare(new SetWalkTargetToAttackTarget<Bucephalos>().speedMod((owner, target) -> ATTACK_MOVE_SPEED).closeEnoughDist(BehaviourUtils.closeEnough(12)))
                .prepareOptional(BehaviourUtils.moveAttack())
                .end(9)
                .start(CHARGE).play(BehaviourUtils.cooldownedPlay(false, 20, 50))
                .prepare(new SetWalkTargetToAttackTarget<Bucephalos>().speedMod((owner, target) -> ATTACK_MOVE_SPEED).closeEnoughDist(BehaviourUtils.closeEnough(12)))
                .prepareOptional(BehaviourUtils.moveAttack())
                .end(4)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends Bucephalos> getCooldownAI() {
        return SelectableBehaviourBuilder.<Bucephalos>builder()
                .add(7, new SetWalkTargetToAttackTarget<Bucephalos>().speedMod((owner, target) -> ATTACK_MOVE_SPEED), BehaviourUtils.moveTo())
                .add(5, BehaviourUtils.withCondition(BehaviourUtils.ifCloserThan(7)),
                        new SetRandomWalkTarget<Bucephalos>().speedModifier(ATTACK_MOVE_SPEED).setRadius(12, 5), BehaviourUtils.moveTo())
                .build();
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.animationHandler.isCurrent(CHARGE)) {
            Vec3 charge = this.syncedDataContainer.get(CHARGE_MOTION);
            if (charge != null) {
                float[] yxRot = MathsHelper.YXRotFrom(charge);
                this.setYRot(yxRot[0]);
                this.setXRot(yxRot[1]);
                this.setYBodyRot(this.getYRot());
                this.setYHeadRot(this.getYRot());
            }
        }
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(CHARGE)) {
            if (!anim.isPast("charge_start")) {
                if (this.getTarget() != null) {
                    this.lookAt(this.getTarget(), 60, 30);
                }
            }
            if (anim.isAt("charge_start")) {
                LivingEntity target = this.getTarget();
                Vec3 dir = target != null ? target.position().subtract(this.position()) : this.getViewVector(1);
                dir = new Vec3(dir.x(), 0, dir.z()).normalize().scale(3).add(0, 0.2, 0);
                this.setDeltaMovement(dir);
                this.syncedDataContainer.set(CHARGE_MOTION, dir);
            }
            if (anim.isPast("charge_start") && !anim.isPast("charge_end")) {
                OrientedBoundingBox obb = this.prepareAttackBox(anim.getAnimation(), null, 0.2, false);
                List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, obb.getEncompassingBox(),
                        entity -> this.targetPred.test(entity) && obb.intersects(entity.getBoundingBox()));
                boolean hit = false;
                for (LivingEntity entity : list) {
                    if (entity.hurt(this.damageSources().mobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE))) {
                        this.setDeltaMovement(Vec3.ZERO);
                        hit = true;
                    }
                }
                S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, this);
                if (hit) {
                    S2CScreenShake.sendAround(this, 14, 8, 2);
                    this.animationHandler.setAnimation(HEADBUTT);
                }
            }
        } else if (anim.is(STOMP, HEADBUTT)) {
            this.getNavigation().stop();
            if (this.getTarget() != null) {
                this.lookAt(this.getTarget(), 60, 30);
            }
            if (anim.isAt("attack")) {
                Consumer<LivingEntity> consumer = this::doHurtTarget;
                if (anim.is(HEADBUTT)) {
                    consumer = entity -> {
                        if (this.doHurtTarget(entity)) {
                            entity.setDeltaMovement(entity.getDeltaMovement().add(0, 0.3, 0));
                            entity.hurtMarked = true;
                        }
                    };
                }
                this.mobAttack(anim, this.getTarget(), consumer);
                if (anim.is(STOMP)) {
                    S2CScreenShake.sendAround(this, 6, 8, 2);
                }
            }
        }
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, double grow) {
        if (anim.is(CHARGE)) {
            AABB aabb = OrientedBoundingBox.originAABB(this).inflate(grow).expandTowards(this.getDeltaMovement());
            return new OrientedBoundingBox(aabb, this.getYRot(), 0, this.position());
        }
        AABB aabb;
        if (anim.is(STOMP)) {
            double width = this.getBbWidth() * 1.5;
            aabb = new AABB(-width, -0.02, -width, width, this.getBbHeight() * 0.5, width)
                    .inflate(grow);
        } else {
            double width = this.getBbWidth() * 1.5;
            double length = this.getBbWidth() * 2.2;
            aabb = new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() * 0.5, length)
                    .inflate(grow);
        }
        return new OrientedBoundingBox(aabb, this.getYHeadRot(), 0, this.position());
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return Utils.runWithInvulTimer(this, entity, super::doHurtTarget, 0);
    }

    @Override
    public AnimationHandler<Bucephalos> getAnimationHandler() {
        return this.animationHandler;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void playStepSound(BlockPos pos, BlockState block) {
        if (!block.liquid()) {
            BlockState blockstate = this.level().getBlockState(pos.above());
            SoundType soundType = block.getSoundType();
            if (blockstate.is(Blocks.SNOW)) {
                soundType = blockstate.getSoundType();
            }

            if (this.interpolatedMoveTickOf(MoveType.RUN, 1) == 1) {
                this.gallopSoundCounter++;
                if (this.gallopSoundCounter > 5 && this.gallopSoundCounter % 3 == 0) {
                    this.playSound(SoundEvents.HORSE_GALLOP, soundType.getVolume() * 0.15F, soundType.getPitch());
                } else if (this.gallopSoundCounter <= 5) {
                    this.playSound(SoundEvents.HORSE_STEP_WOOD, soundType.getVolume() * 0.15F, soundType.getPitch());
                }
            } else if (this.isWoodSoundType(soundType)) {
                this.playSound(SoundEvents.HORSE_STEP_WOOD, soundType.getVolume() * 0.15F, soundType.getPitch());
            } else {
                this.playSound(SoundEvents.HORSE_STEP, soundType.getVolume() * 0.15F, soundType.getPitch());
            }
        }
    }

    private boolean isWoodSoundType(SoundType soundType) {
        return soundType == SoundType.WOOD
                || soundType == SoundType.NETHER_WOOD
                || soundType == SoundType.STEM
                || soundType == SoundType.CHERRY_WOOD
                || soundType == SoundType.BAMBOO_WOOD;
    }
}
