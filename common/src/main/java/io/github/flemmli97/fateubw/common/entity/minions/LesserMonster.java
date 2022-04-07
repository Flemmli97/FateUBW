package io.github.flemmli97.fateubw.common.entity.minions;

import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.entity.IServantMinion;
import io.github.flemmli97.fateubw.common.entity.ai.AnimatedMeleeGoal;
import io.github.flemmli97.fateubw.common.entity.ai.TargetOwnerEnemyGoal;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import net.minecraft.nbt.CompoundTag;
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

import java.util.UUID;

public class LesserMonster extends PathfinderMob implements IServantMinion, IAnimated, OwnableEntity {

    private UUID ownerUUID;
    private LivingEntity owner;
    private int livingTicks;

    public static final AnimatedAction walk = new AnimatedAction(31, 0, "walk");
    public static final AnimatedAction attack = new AnimatedAction(20, 15, "attack");
    private static final AnimatedAction[] anims = {walk, attack};

    private final AnimationHandler<LesserMonster> animationHandler = new AnimationHandler<>(this, anims);

    public LesserMonster(EntityType<? extends LesserMonster> type, Level world) {
        super(type, world);
        if (!world.isClientSide) {
            this.goals();
            this.setAttributes();
        }
    }

    public LesserMonster(Level world, LivingEntity owner) {
        this(ModEntities.lesserMonster.get(), world);
        this.owner = owner;
        this.ownerUUID = owner.getUUID();
    }

    protected void setAttributes() {
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Config.Common.smallMonsterDamage);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(18);
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
    }

    protected void goals() {
        this.goalSelector.addGoal(2, new AnimatedMeleeGoal<>(this, m -> attack));
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
        }
        this.getAnimationHandler().tick();
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