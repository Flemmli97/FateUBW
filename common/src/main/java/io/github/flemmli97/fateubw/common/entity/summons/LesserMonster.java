package io.github.flemmli97.fateubw.common.entity.summons;

import io.github.flemmli97.fateubw.api.datapack.AttributeHolderProperties;
import io.github.flemmli97.fateubw.api.datapack.ServantExtraData;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.tenshilib.common.entity.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class LesserMonster extends PathfinderMob implements AnimatedEntity, OwnableEntity {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String ATTACK = BUILDER.add("attack", AnimationsBuilder.definition(0.76).marker("attack", 0.52));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

//    public static final List<WeightedEntry.Wrapper<GoalAttackAction<LesserMonster>>> ATTACKS = List.of(
//            WeightedEntry.wrap(new GoalAttackAction<LesserMonster>(LesserMonster.ATTACK)
//                    .cooldown(e -> e.getRandom().nextInt(15) + 8)
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1, 0.5))), 1)
//    );
//    public static final List<WeightedEntry.Wrapper<IdleAction<LesserMonster>>> IDLE_ACTIONS = List.of(
//            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 0.5)), 1)
//    );

    private UUID ownerUUID;
    private LivingEntity owner;
    private int livingTicks;

    private final AnimationHandler<LesserMonster> animationHandler = new AnimationHandler<>(this, ANIMS);

    private int moveTick;

    public static final int MOVE_TICK_MAX = 3;

    private final int maxLivingTicks;

    public LesserMonster(EntityType<? extends LesserMonster> type, Level level) {
        super(type, level);
        if (!level.isClientSide) {
            this.updateAttributes();
        }
        this.maxLivingTicks = DatapackHandler.SERVANT_PROPS.get(FateEntities.GILLES.get())
                .getConfig(ServantExtraData.GILLES_MONSTER_DURATION);
    }

    public LesserMonster(Level level, LivingEntity owner) {
        this(FateEntities.LESSER_MONSTER.get(), level);
        this.owner = owner;
        this.ownerUUID = owner.getUUID();
    }

    protected void updateAttributes() {
        AttributeHolderProperties props = DatapackHandler.SERVANT_PROPS.getGeneric(this.getType());
        props.attributes().forEach((att, val) -> {
            AttributeInstance inst = this.getAttribute(att);
            if (inst != null) {
                inst.setBaseValue(val);
                if (att == Attributes.MAX_HEALTH)
                    this.setHealth(this.getMaxHealth());
            }
        });
    }

    protected void goals() {
//        this.goalSelector.addGoal(2, new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS));
//        this.goalSelector.addGoal(3, new FloatGoal(this));
//        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
//        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
//        this.targetSelector.addGoal(0, new TargetOwnerEnemyGoal<>(this));
//        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, true,
//                LesserMonster.this::canAttackTarget));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            this.livingTicks++;
            if (this.livingTicks > this.maxLivingTicks)
                this.remove(RemovalReason.KILLED);
            AnimationState anim = this.getAnimationHandler().getAnimation();
            if (anim != null && anim.is(ATTACK) && anim.isAt("attack")) {
                LivingEntity target = this.getTarget();
                if (target != null && this.getAttackBoundingBox().intersects(target.getBoundingBox())) {
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
            this.owner = EntityUtils.findFromUUID(LivingEntity.class, this.level(), this.ownerUUID);
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