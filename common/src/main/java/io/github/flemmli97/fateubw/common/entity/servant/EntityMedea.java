package io.github.flemmli97.fateubw.common.entity.servant;

import com.mojang.math.Vector4f;
import io.github.flemmli97.fateubw.api.datapack.ServantExtraData;
import io.github.flemmli97.fateubw.common.entity.SwitchableWeapon;
import io.github.flemmli97.fateubw.common.entity.ai.TeleportRunner;
import io.github.flemmli97.fateubw.common.entity.misc.MagicBeam;
import io.github.flemmli97.fateubw.common.entity.misc.MagicBufCircle;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.fateubw.common.utils.TeleportUtils;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.DoNothingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.JumpEvadeAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.KeepDistanceRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityMedea extends BaseServant {

    public static final AnimatedAction CAST_1 = AnimatedAction.builder(0.86, "cast_1").marker("attack", 0.28).build();
    public static final AnimatedAction CAST_2 = AnimatedAction.builder(1.06, "cast_2").marker("attack", 0.6).build();
    public static final AnimatedAction CAST_3 = AnimatedAction.builder(1.58, "cast_3").marker("attack", 1.24).build();
    public static final AnimatedAction CAST_4 = AnimatedAction.builder(2.88, "cast_4")
            .marker("attack_start", 0.6).marker("attack_end", 2.12)
            .marker("teleport_start", 0.2).marker("teleport_end", 2.4).build();
    public static final AnimatedAction MAGIC_CIRCLE = AnimatedAction.builder(2.2, "magic_circle")
            .marker("attack", 1.04).marker("push", 0.52).build();

    public static final AnimatedAction RULE_BREAKER = AnimatedAction.builder(2.76, "rule_breaker")
            .marker("attack", 1.88).marker("teleport_start", 0.8).marker("teleport_end", 2.44).build();
    public static final AnimatedAction SUMMON = new AnimatedAction(2., "summon");

    private static final AnimatedAction[] ANIMS = {CAST_1, CAST_2, CAST_3, CAST_4, MAGIC_CIRCLE, RULE_BREAKER, SUMMON};

    public static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityMedea>>> ATTACKS = List.of(
            WeightedEntry.wrap(new GoalAttackAction<EntityMedea>(EntityMedea.CAST_1)
                    .cooldown(e -> e.getRandom().nextInt(25) + 20)
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(6, 15, 1.1))), 20),
            WeightedEntry.wrap(new GoalAttackAction<EntityMedea>(EntityMedea.CAST_2)
                    .cooldown(e -> e.getRandom().nextInt(25) + 20)
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(6, 15, 1.1))), 20),
            WeightedEntry.wrap(new GoalAttackAction<EntityMedea>(EntityMedea.CAST_3)
                    .cooldown(e -> e.getRandom().nextInt(25) + 20)
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(6, 15, 1.1))), 12),
            WeightedEntry.wrap(new GoalAttackAction<EntityMedea>(EntityMedea.CAST_4)
                    .cooldown(e -> e.getRandom().nextInt(25) + 20)
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 6),
            WeightedEntry.wrap(new GoalAttackAction<EntityMedea>(EntityMedea.CAST_1)
                    .cooldown(e -> e.getRandom().nextInt(25) + 20)
                    .withCondition((goal, target, prev) -> goal.distanceToTargetSq < 9)
                    .prepare(() -> new WrappedRunner<>(new JumpEvadeAction<>(3, 2, 1, 1, 0, new DoNothingRunner<>(true)))), 23),
            WeightedEntry.wrap(new GoalAttackAction<EntityMedea>(EntityMedea.CAST_3)
                    .cooldown(e -> e.getRandom().nextInt(25) + 20)
                    .withCondition((goal, target, prev) -> goal.distanceToTargetSq < 9)
                    .prepare(() -> new WrappedRunner<>(new JumpEvadeAction<>(3, 2, 1, 1, 0, new DoNothingRunner<>(true)))), 23),
            WeightedEntry.wrap(new GoalAttackAction<EntityMedea>(EntityMedea.MAGIC_CIRCLE)
                    .cooldown(e -> e.getRandom().nextInt(40) + 20)
                    .withCondition(((goal, target, previous) -> goal.attacker.aiCircledelay < 0))
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 4),
            WeightedEntry.wrap(new GoalAttackAction<EntityMedea>(EntityMedea.RULE_BREAKER)
                    .cooldown(e -> e.getRandom().nextInt(25) + 20)
                    .withCondition(Utils.npCheck())
                    .prepare(() -> new WrappedRunner<>(new KeepDistanceRunner<>(7, 12, 1.2))), 50)
    );
    public static final List<WeightedEntry.Wrapper<IdleAction<EntityMedea>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new TeleportRunner<>(5, 12, 6, 12)), 1),
            WeightedEntry.wrap(new IdleAction<>(() -> new TeleportRunner<EntityMedea>(5, 12, 6, 12))
                    .withCondition(((goal, target) -> goal.distanceToTargetSq < 25)), 7),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<>(1, 1.2, 6)), 20)
    );

    public final AnimatedAttackGoal<EntityMedea> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    private final AnimationHandler<EntityMedea> animationHandler = new AnimationHandler<>(this, ANIMS).withChangeListener(anim -> {
        if (!this.level().isClientSide()) {
            if (this.getAnimationHandler().isCurrent(RULE_BREAKER)) {
                this.switchableWeapon.switchItems(true);
            } else if (anim != null && anim.is(RULE_BREAKER)) {
                this.switchableWeapon.switchItems(false);
            }
            if (this.teleportPre != null) {
                this.teleportPre = null;
                this.teleportPos = null;
                this.setNoGravity(this.gravityPre);
            }
        }
        return false;
    });

    public final SwitchableWeapon<EntityMedea> switchableWeapon = new SwitchableWeapon<>(this, new ItemStack(FateItems.RULE_BREAKER.get()), ItemStack.EMPTY);

    private boolean gravityPre;
    private Vec3 teleportPre, teleportPos;

    private int circleDelay, aiCircledelay;
    private Vec3 circlePos;

    private final Vector4f summonColor = new Vector4f(59 / 255f, 14 / 255f, 76 / 255f, 0.7f);

    public EntityMedea(EntityType<? extends BaseServant> entityType, Level level) {
        super(entityType, level);
        if (!level.isClientSide)
            this.goalSelector.addGoal(0, this.attack);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FateItems.STAFF.get()));
    }

    @Override
    public Goal getAttackAI() {
        return this.attack;
    }

    @Override
    public AnimationHandler<EntityMedea> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDeadOrDying() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
        }
    }

    @Override
    public void tick() {
        super.tick();
        --this.circleDelay;
        --this.aiCircledelay;
        if (this.level().isClientSide) {
            if (this.getAnimationHandler().isCurrent(MAGIC_CIRCLE) && this.getAnimationHandler().getAnimation().isAt("attack")) {
                this.sphereParticles();
            }
        } else {
            if (this.tickCount % 10 == 0 && this.circlePos != null) {
                if (this.level().getEntities(EntityTypeTest.forClass(MagicBufCircle.class), new AABB(this.circlePos.add(-2, -2, -2),
                        this.circlePos.add(2, 2, 2)), e -> e.getOwner() == this).isEmpty()) {
                    this.circlePos = null;
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return !this.transit(false) && super.hurt(source, amount);
    }

    private boolean transit(boolean teleportOnly) {
        AnimatedAction anim = this.getAnimationHandler().getAnimation();
        if (CAST_4.is(anim)) {
            if (!teleportOnly)
                return anim.isPast("teleport_start", 0.56);
            return anim.isPast("teleport_start", 0.56) || anim.isPast("teleport_end");
        }
        return RULE_BREAKER.is(anim) && (anim.isPast("teleport_start", 1.32) || anim.isPast("teleport_end", 2.6));
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(CAST_1, CAST_2, CAST_3)) {
            LivingEntity target = this.getTarget();
            if (target != null) {
                this.lookAtNow(target, 60, 30);
            }
            if (anim.isAt("attack")) {
                this.singleShot(target);
            }
        } else if (anim.is(CAST_4)) {
            LivingEntity target = this.getTarget();
            if (target != null) {
                this.lookAtNow(target, 60, 30);
            }
            if (anim.isAt("teleport_start")) {
                this.teleportPre = this.position();
                this.gravityPre = this.isNoGravity();
                this.teleportTo(this.getX(), this.getY() + 8, this.getZ());
                this.teleportPos = this.position();
            }
            if (this.teleportPos != null) {
                this.setPos(this.teleportPos);
            }
            if (anim.isAt("teleport_end")) {
                this.fallDistance = 0;
                this.teleportTo(this.teleportPre.x(), this.teleportPre.y(), this.teleportPre.z());
                this.teleportPre = null;
                this.teleportPos = null;
                this.setNoGravity(this.gravityPre);
            }
            if (anim.isPast("attack_start") && !anim.isPast("attack_end") && this.tickCount % 3 == 0) {
                this.attackWithRangedAttack(target);
            }
        } else if (anim.is(MAGIC_CIRCLE)) {
            if (anim.isAt("attack")) {
                this.makeCircle();
            }
            if (anim.isAt("push")) {
                this.level().getEntities(EntityTypeTest.forClass(LivingEntity.class),
                                this.getBoundingBox().inflate(12, 8, 12),
                                this.targetPred)
                        .forEach(e -> {
                            Vec3 dir = e.position().subtract(this.position());
                            boolean none = dir.x() == 0 && dir.z() == 0;
                            dir = new Vec3(none ? 1 : dir.x(), 0, dir.z()).normalize().scale(2.5);
                            e.setDeltaMovement(e.getDeltaMovement().add(dir));
                            e.hurtMarked = true;
                        });
            }
        } else if (anim.is(RULE_BREAKER)) {
            LivingEntity target = this.getTarget();
            if (target != null) {
                this.lookAtNow(target, 60, 30);
            }
            if (anim.isAt("teleport_start")) {
                Vec3 dir;
                if (this.getTarget() != null) {
                    dir = this.getTarget().position().subtract(this.position());
                } else {
                    Vec3 look = Vec3.directionFromRotation(0, this.getYHeadRot()).scale(11);
                    HitResult res = this.level().clip(new ClipContext(this.getEyePosition(), this.getEyePosition().add(look), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                    dir = res.getLocation().subtract(this.getEyePosition());
                }
                Vec3 off = dir.normalize();
                dir = dir.subtract(off);
                this.teleportPre = this.position();
                this.gravityPre = this.isNoGravity();
                TeleportUtils.teleportTo(this, this.getX() + dir.x(), this.getY() + dir.y(), this.getZ() + dir.z(),
                        SoundEvents.ENDERMAN_TELEPORT, ParticleTypes.WITCH);
                this.teleportPos = this.position();
            }
            if (anim.isAt("teleport_end") && this.teleportPre != null) {
                this.teleportTo(this.teleportPre.x(), this.teleportPre.y(), this.teleportPre.z());
                this.teleportPre = null;
                this.teleportPos = null;
                this.setNoGravity(this.gravityPre);
            }
            if (anim.isAt("attack")) {
                this.ruleBreaker();
            }
        }
    }

    @Override
    public AABB attackBB(AnimatedAction anim) {
        double width = this.getBbWidth() + 0.3;
        double length = 1;
        if (anim.is(RULE_BREAKER)) {
            width += 0.1;
            length += 0.6;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public boolean isInvisible() {
        if (this.transit(true)) {
            return true;
        }
        return super.isInvisible();
    }

    public void ruleBreaker() {
        if (!this.forcedNP)
            this.useMana(this.props().hogouMana());
        this.forcedNP = false;
        this.mobAttack(RULE_BREAKER, this.getTarget(), this::doHurtTarget);
        this.revealServant();
    }

    public void singleShot(LivingEntity target) {
        int strength = 0;
        MobEffectInstance eff = this.getEffect(MobEffects.DAMAGE_BOOST);
        if (eff != null)
            strength = eff.getAmplifier();
        MagicBeam beam = new MagicBeam(this.level, this, target);
        Vec3 look = this.getLookAngle();
        beam.setPos(this.getEyePosition().add(look.x(), 2, look.z()));
        beam.setDamageMultiplier(1 + strength * 0.15f);
        if (target != null)
            beam.setRotationTo(target, 0);
        else {
            Vec3 dir = this.getLookAngle();
            beam.setRotationToDir(dir.x(), dir.y(), dir.z(), 0);
        }
        this.level().addFreshEntity(beam);
        this.revealServant();
    }

    public void attackWithRangedAttack(LivingEntity target) {
        int strength = 0;
        MobEffectInstance eff = this.getEffect(MobEffects.DAMAGE_BOOST);
        if (eff != null)
            strength = eff.getAmplifier();
        int amount = this.getRandom().nextInt(2) + 1;
        for (Vec3 offset : Utils.randomSidedPositions(this, amount, 6)) {
            MagicBeam beam = new MagicBeam(this.level, this, target);
            beam.setDamageMultiplier(1 + strength * 0.1f);
            beam.setPos(offset.x, offset.y, offset.z);
            if (target != null)
                beam.setRotationTo(target, 0);
            else {
                Vec3 dir = this.getLookAngle();
                beam.setRotationToDir(dir.x(), dir.y(), dir.z(), 0);
            }
            this.level().addFreshEntity(beam);
        }
        this.revealServant();
    }

    public void makeCircle() {
        if (!this.level().isClientSide) {
            MagicBufCircle circle = new MagicBufCircle(this.level, this, this.props().getConfig(ServantExtraData.MEDEA_CIRCLE_RANGE));
            this.level().addFreshEntity(circle);
            int duration = this.props().getConfig(ServantExtraData.MEDEA_CIRCLE_DURATION);
            this.circleDelay = duration + this.random.nextInt(100);
            this.aiCircledelay = (int) (this.random.nextInt(400) + duration * 0.5);
            this.circlePos = circle.position();
            if (this.getOwner() != null)
                this.getOwner().sendMessage(Component.translatable("fateubw.chat.medea.circle.spawn"));
            this.playSound(SoundEvents.BEACON_POWER_SELECT, 1, 1);
        }
    }

    private void sphereParticles() {
        double goldenAngle = Math.PI * (Math.sqrt(5) - 1);
        for (int i = 0; i < 40; i++) {
            double phi = Math.acos(1 - 2. * i / 50);
            double theta = goldenAngle * i;
            double x = Math.cos(theta) * Math.sin(phi);
            double y = Math.sin(theta) * Math.sin(phi);
            double z = Math.cos(phi);
            this.level().addParticle(ParticleTypes.WITCH, this.getX() + x, this.getY(0.5) + y, this.getZ() + z, x * 0.15, y * 0.15, z * 0.15);
        }
    }

    @Override
    public boolean hasRestriction() {
        return super.hasRestriction() || this.circlePos != null;
    }

    @Override
    public BlockPos getRestrictCenter() {
        return this.circlePos != null ? new BlockPos(this.circlePos) : super.getRestrictCenter();
    }

    @Override
    public boolean isWithinRestriction(BlockPos pos) {
        if (this.circlePos != null) {
            float range = this.props().getConfig(ServantExtraData.MEDEA_CIRCLE_RANGE);
            if (this.circlePos.distanceToSqr(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5) > range * range)
                return false;
        }
        return super.isWithinRestriction(pos);
    }

    @Override
    public String[] specialCommands() {
        return new String[]{FateEntities.MEDEA.getID() + ".circle"};
    }

    @Override
    public void doSpecialCommand(String s) {
        if (s.equals(FateEntities.MEDEA.getID() + ".circle") && this.circleDelay <= 0)
            this.getAnimationHandler().setAnimation(MAGIC_CIRCLE);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("CircleDelay", this.circleDelay);
        tag.putInt("CircleDelayAI", this.aiCircledelay);
        if (this.circlePos != null) {
            tag.putDouble("CircleX", this.circlePos.x());
            tag.putDouble("CircleY", this.circlePos.y());
            tag.putDouble("CircleZ", this.circlePos.z());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.circleDelay = tag.getInt("CircleDelay");
        this.aiCircledelay = tag.getInt("CircleDelayAI");
        if (tag.contains("CircleX")) {
            this.circlePos = new Vec3(tag.getDouble("CircleX"), tag.getDouble("CircleY"), tag.getDouble("CircleZ"));
        }
    }

    @Override
    protected AnimatedAction getSummonAnimation() {
        return SUMMON;
    }

    @Override
    public Vector4f summonColor() {
        return this.summonColor;
    }
}
