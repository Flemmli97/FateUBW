package io.github.flemmli97.fate.common.entity;

import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.api.entity.AnimationHandler;
import com.flemmli97.tenshilib.api.entity.IAnimated;
import com.flemmli97.tenshilib.api.entity.IOwnable;
import com.flemmli97.tenshilib.common.entity.EntityUtil;
import io.github.flemmli97.fate.common.config.Config;
import io.github.flemmli97.fate.common.entity.ai.AnimatedMeleeGoal;
import io.github.flemmli97.fate.common.entity.ai.TargetOwnerEnemyGoal;
import io.github.flemmli97.fate.common.registry.ModEntities;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityLesserMonster extends CreatureEntity implements IServantMinion, IAnimated, IOwnable<LivingEntity> {

    private UUID ownerUUID;
    private LivingEntity owner;
    private int livingTicks;

    public static final AnimatedAction walk = new AnimatedAction(31, 0, "walk");
    public static final AnimatedAction attack = new AnimatedAction(20, 15, "attack");
    private static final AnimatedAction[] anims = {walk, attack};

    private final AnimationHandler<EntityLesserMonster> animationHandler = new AnimationHandler<>(this, anims);

    public EntityLesserMonster(EntityType<? extends EntityLesserMonster> type, World world) {
        super(type, world);
        if (!world.isRemote) {
            this.goals();
            this.setAttributes();
        }
    }

    public EntityLesserMonster(World world, LivingEntity owner) {
        this(ModEntities.lesserMonster.get(), world);
        this.owner = owner;
        this.ownerUUID = owner.getUniqueID();
    }

    protected void setAttributes() {
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Config.Common.smallMonsterDamage);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(18);
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    protected void goals() {
        this.goalSelector.addGoal(2, new AnimatedMeleeGoal<>(this, m -> attack));
        this.goalSelector.addGoal(3, new SwimGoal(this));
        this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.targetSelector.addGoal(0, new TargetOwnerEnemyGoal<>(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, true,
                EntityLesserMonster.this::canAttackTarget));

    }

    @Override
    public void livingTick() {
        super.livingTick();
        if (!this.world.isRemote) {
            this.livingTicks++;
            if (this.livingTicks > Config.Common.gillesMinionDuration)
                this.remove();
        }
        this.getAnimationHandler().tick();
    }

    /*@Override
    public void tickAnimation() {
        if (this.getAnimation() != null && this.getAnimation().tick()) {
            if (this.getAnimation().getID().equals("walk") && (this.getMotion().x != 0 || this.getMotion().z != 0))
                this.getAnimation().reset();
            else
                this.setAnimation(null);
        }
    }*/

    @Override
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null) {
            this.owner = EntityUtil.findFromUUID(LivingEntity.class, this.world, this.ownerUUID);
        }
        return this.owner;
    }

    @Override
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    protected boolean canAttackTarget(LivingEntity e) {
        if (e.getUniqueID().equals(this.ownerUUID))
            return false;
        return this.getOwnerUUID() == null || !(e instanceof IOwnable) || !this.getOwnerUUID().equals(((IOwnable<?>) e).getOwnerUUID());
    }

    @Override
    public void readAdditional(CompoundNBT tag) {
        super.readAdditional(tag);
        if (tag.contains("Owner"))
            this.ownerUUID = tag.getUniqueId("Owner");
    }

    @Override
    public void writeAdditional(CompoundNBT tag) {
        super.writeAdditional(tag);
        if (this.ownerUUID != null)
            tag.putUniqueId("Owner", this.ownerUUID);
    }

    @Override
    public AnimationHandler<EntityLesserMonster> getAnimationHandler() {
        return this.animationHandler;
    }
}