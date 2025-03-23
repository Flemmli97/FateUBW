package io.github.flemmli97.fateubw.common.entity.minions;

import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.entity.ai.TargetOwnerEnemyGoal;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

public class LesserMonster extends PathfinderMob implements IAnimated, OwnableEntity {

    public static final AnimatedAction ATTACK = AnimatedAction.builder(0.76, "attack").marker("attack", 0.52).build();
    private static final AnimatedAction[] ANIMS = {ATTACK};

    public static final List<WeightedEntry.Wrapper<GoalAttackAction<LesserMonster>>> ATTACKS = List.of(
            WeightedEntry.wrap(new GoalAttackAction<LesserMonster>(LesserMonster.ATTACK)
                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1, 0.5))), 1)
    );
    public static final List<WeightedEntry.Wrapper<IdleAction<LesserMonster>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 0.5)), 1)
    );

    private UUID ownerUUID;
    private LivingEntity owner;
    private int livingTicks;

    private final AnimationHandler<LesserMonster> animationHandler = new AnimationHandler<>(this, ANIMS);

    private int moveTick;

    public static final int MOVE_TICK_MAX = 3;

    public LesserMonster(EntityType<? extends LesserMonster> type, Level world) {
        super(type, world);
        if (!world.isClientSide) {
            this.goals();
            this.setAttributes();
        }
    }

    public LesserMonster(Level world, LivingEntity owner) {
        this(ModEntities.LESSER_MONSTER.get(), world);
        this.owner = owner;
        this.ownerUUID = owner.getUUID();
    }

    protected void setAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Config.Common.smallMonsterHealth);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Config.Common.smallMonsterDamage);
    }

    protected void goals() {
        this.goalSelector.addGoal(2, new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS));
        this.goalSelector.addGoal(3, new FloatGoal(this));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(0, new TargetOwnerEnemyGoal<>(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, true,
                LesserMonster.this::canAttackTarget));

    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            this.livingTicks++;
            if (this.livingTicks > Config.Common.gillesMinionDuration)
                this.remove(RemovalReason.KILLED);
            AnimatedAction anim = this.getAnimationHandler().getAnimation();
            if (anim != null && anim.is(ATTACK) && anim.isAt("attack")) {
                LivingEntity target = this.getTarget();
                if (target != null && this.distanceToSqr(target) <= this.getMeleeAttackRangeSqr(target)) {
                    this.doHurtTarget(target);
                }
            }
        }
        if (this.isMoving()) {
            this.moveTick = Math.min(MOVE_TICK_MAX, ++this.moveTick);
        } else {
            this.moveTick = Math.max(0, --this.moveTick);
        }
        this.getAnimationHandler().tick();
    }

    protected boolean isMoving() {
        return this.getDeltaMovement().x != 0 || this.getDeltaMovement().z != 0;
    }

    public float interpolatedMoveTick(float partialTicks) {
        return Mth.clamp((this.moveTick + (this.isMoving() ? partialTicks : -partialTicks)) / (float) MOVE_TICK_MAX, 0, 1);
    }

    @Override
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null) {
            this.owner = EntityUtil.findFromUUID(LivingEntity.class, this.level, this.ownerUUID);
        }
        return this.owner;
    }

    @Override
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    protected boolean canAttackTarget(LivingEntity e) {
        if (e.getUUID().equals(this.ownerUUID))
            return false;
        return this.getOwnerUUID() == null || !(e instanceof OwnableEntity ownableEntity) || !this.getOwnerUUID().equals(ownableEntity.getOwnerUUID());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Owner"))
            this.ownerUUID = tag.getUUID("Owner");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.ownerUUID != null)
            tag.putUUID("Owner", this.ownerUUID);
    }

    @Override
    public AnimationHandler<LesserMonster> getAnimationHandler() {
        return this.animationHandler;
    }
}