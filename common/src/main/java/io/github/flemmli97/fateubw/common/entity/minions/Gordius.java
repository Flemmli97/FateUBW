package io.github.flemmli97.fateubw.common.entity.minions;

import io.github.flemmli97.fateubw.common.entity.ChargingHandler;
import io.github.flemmli97.fateubw.common.entity.IServantMinion;
import io.github.flemmli97.fateubw.common.entity.MultiPartEntity;
import io.github.flemmli97.fateubw.common.entity.NonSitVehicle;
import io.github.flemmli97.fateubw.common.entity.ai.GordiusAttackGoal;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public class Gordius extends PathfinderMob implements IServantMinion, IAnimated, NonSitVehicle {

    public final GordiusAttackGoal attackAI = new GordiusAttackGoal(this);

    public static final AnimatedAction charging = new AnimatedAction(20, 5, "charge");
    private static final AnimatedAction[] anims = {AnimatedAction.vanillaAttack, charging};

    private final AnimationHandler<Gordius> animationHandler = new AnimationHandler<>(this, anims);

    private final MultiPartEntity wheels;

    private static final Function<AnimatedAction, Boolean> chargingAnim = anim -> anim != null && anim.getID().equals(charging.getID());
    private static final EntityDataAccessor<Float> lockedYaw = SynchedEntityData.defineId(Gordius.class, EntityDataSerializers.FLOAT);

    public final ChargingHandler<Gordius> chargingHandler = new ChargingHandler<>(this, lockedYaw, chargingAnim);

    private Vec3 view = Vec3.ZERO;

    public Gordius(EntityType<? extends Gordius> type, Level world) {
        super(type, world);
        this.maxUpStep = 1;
        if (world != null && !world.isClientSide)
            this.goalSelector.addGoal(0, this.attackAI);
        this.wheels = new MultiPartEntity(world, 2.2f, 1.6f);
        this.wheels.maxUpStep = this.maxUpStep;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(lockedYaw, 0f);
    }

    @Override
    public AnimationHandler<Gordius> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    public AnimatedAction getChargingAnim() {
        return charging;
    }

    public boolean isCharging() {
        AnimatedAction anim = this.getAnimationHandler().getAnimation();
        return anim != null && charging.getID().equals(anim.getID()) && anim.getTick() >= anim.getAttackTime();
    }

    @Override
    public void baseTick() {
        this.getAnimationHandler().tick();
        this.chargingHandler.tick();
        if (!this.level.isClientSide) {
            this.view = this.calculateViewVector(0, this.getYRot());
            Vec3 offset = this.view.scale(-2.6);
            if (!this.wheels.isAddedToLevel()) {
                this.wheels.setParent(this);
                this.wheels.setPos(this.getX() + offset.x, this.getY(), this.getZ() + offset.z);
                this.level.addFreshEntity(this.wheels);
            }
            this.wheels.updatePositionTo(this.getX() + offset.x, this.getY(), this.getZ() + offset.z, true);
        }
        super.baseTick();
    }

    @Override
    public void moveTo(double x, double y, double z) {
        super.moveTo(x, y, z);
        Vec3 offset = this.view.scale(-2.6);
        this.wheels.moveTo(this.getX() + offset.x, this.getY(), this.getZ() + offset.z);
    }

    @Override
    public void setYRot(float yRot) {
        super.setYRot(yRot);
    }

    @Override
    public void positionRider(Entity passenger) {
        if (this.hasPassenger(passenger)) {
            Vec3 offSet = this.calculateViewVector(0, this.getYRot()).scale(2.65);
            passenger.setPos(this.getX() - offSet.x, this.getY() + this.getPassengersRidingOffset() - 0.3, this.getZ() - offSet.z);
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.825;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (damageSource != DamageSource.OUT_OF_WORLD && this.isCharging())
            damage *= 0.5f;
        return super.hurt(damageSource, damage);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.COW_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.COW_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COW_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void push(Entity entity) {
    }

    public boolean canAttackTarget(LivingEntity entity) {
        if (this.getFirstPassenger() instanceof OwnableEntity ownable) {
            return entity != ownable.getOwner();
        }
        return true;
    }

    /*@Override
    protected void pushEntities() {
        List<Entity> list = this.level.getEntities(this, this.wheels.getBoundingBox(), EntitySelector.pushableBy(this));
        list.addAll(this.level.getEntities(this, this.getBoundingBox(), EntitySelector.pushableBy(this)));
        if (!list.isEmpty()) {
            int i = this.level.getGameRules().getInt(GameRules.RULE_MAX_ENTITY_CRAMMING);
            if (i > 0 && list.size() > i - 1 && this.random.nextInt(4) == 0) {
                int j = 0;

                for (int k = 0; k < list.size(); ++k) {
                    if (!list.get(k).isPassenger()) {
                        ++j;
                    }
                }

                if (j > i - 1) {
                    this.hurt(DamageSource.CRAMMING, 6.0F);
                }
            }

            for (int l = 0; l < list.size(); ++l) {
                Entity entity = list.get(l);
                this.doPush(entity);
            }
        }
    }

    @Override
    protected void checkInsideBlocks() {
        AABB mainAABB = this.getBoundingBox();
        BlockPos corner1 = new BlockPos(mainAABB.minX + 0.001D, mainAABB.minY + 0.001D, mainAABB.minZ + 0.001D);
        BlockPos corner2 = new BlockPos(mainAABB.maxX - 0.001D, mainAABB.maxY - 0.001D, mainAABB.maxZ - 0.001D);
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        if (this.level.hasChunksAt(corner1, corner2)) {
            this.doBlockCollisionIn(mutable, this.wheels.getBoundingBox());
            this.doBlockCollisionIn(mutable, this.getBoundingBox());
        }
    }

    private void doBlockCollisionIn(BlockPos.MutableBlockPos mutable, AABB aabb) {
        BlockPos corner1 = new BlockPos(aabb.minX + 0.001D, aabb.minY + 0.001D, aabb.minZ + 0.001D);
        BlockPos corner2 = new BlockPos(aabb.maxX - 0.001D, aabb.maxY - 0.001D, aabb.maxZ - 0.001D);
        for (int i = corner1.getX(); i <= corner2.getX(); ++i) {
            for (int j = corner1.getY(); j <= corner2.getY(); ++j) {
                for (int k = corner1.getZ(); k <= corner2.getZ(); ++k) {
                    mutable.set(i, j, k);
                    BlockState blockstate = this.level.getBlockState(mutable);
                    try {
                        blockstate.entityInside(this.level, mutable, this);
                        this.onInsideBlock(blockstate);
                    } catch (Throwable throwable) {
                        CrashReport crashreport = CrashReport.forThrowable(throwable, "Colliding entity with block");
                        CrashReportCategory crashreportcategory = crashreport.addCategory("Block being collided with");
                        CrashReportCategory.populateBlockDetails(crashreportcategory, this.level, mutable, blockstate);
                        throw new ReportedException(crashreport);
                    }
                }
            }
        }
    }*/

    //====== Forge

    public boolean shouldRiderSit() {
        return false;
    }
}
