package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.common.entity.DaggerHitNotifiable;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.platform.Platform;
import io.github.flemmli97.tenshilib.common.utils.HitResultUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ChainDagger extends BaseProjectile {

    private static final EntityDataAccessor<Boolean> RETRACTING = SynchedEntityData.defineId(ChainDagger.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> MAINHAND = SynchedEntityData.defineId(ChainDagger.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> HOOKED_ENTITY = SynchedEntityData.defineId(ChainDagger.class, EntityDataSerializers.INT);

    private Entity hookedEntity;

    public ChainDagger(EntityType<? extends ChainDagger> type, Level level) {
        super(type, level);
    }

    public ChainDagger(Level level, LivingEntity shooter, boolean mainHand) {
        super(FateEntities.DAGGER_HOOK.get(), level, shooter);
        this.entityData.set(MAINHAND, mainHand);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HOOKED_ENTITY, 0);
        builder.define(RETRACTING, false);
        builder.define(MAINHAND, true);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (HOOKED_ENTITY.equals(key)) {
            int i = this.getEntityData().get(HOOKED_ENTITY);
            this.hookedEntity = i > 0 ? this.level().getEntity(i) : null;
            if (this.hookedEntity != null)
                this.setDeltaMovement(Vec3.ZERO);
        }
        super.onSyncedDataUpdated(key);
    }

    public boolean retracting() {
        return this.getEntityData().get(RETRACTING);
    }

    public boolean fromMainHand() {
        return this.getEntityData().get(MAINHAND);
    }

    @Override
    public boolean canHitShooter() {
        return this.retracting();
    }

    @Override
    public void tick() {
        if (this.hookedEntity != null) {
            if (!this.hookedEntity.isAlive()) {
                this.hookedEntity = null;
                this.retractHook();
            } else {
                this.setDeltaMovement(Vec3.ZERO);
                this.setPos(this.hookedEntity.getX(), this.hookedEntity.getY(0.5D), this.hookedEntity.getZ());
            }
        } else if (this.level().isClientSide) {
            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.CHAIN_PLACE, SoundSource.NEUTRAL, 1, 1, false);
            if (this.isAlive() && this.getOwner() instanceof Player player) {
                PlayerData data = Platform.INSTANCE.getPlayerData(player);
                if (data.getThrownDagger() != this)
                    data.setThrownDagger(this);
            }
        }
        super.tick();
        if (!this.level().isClientSide) {
            if (this.retracting()) {
                Entity entity = this.getOwner();
                if (entity != null) {
                    Vec3 vector3d = new Vec3(entity.getX() - this.getX(), entity.getY() - this.getY() + entity.getBbHeight() * 0.5f, entity.getZ() - this.getZ()).normalize().scale(0.8D);
                    this.setDeltaMovement(vector3d);
                }
            }
            if (this.getOwner() == null)
                this.discard();
            else if (this.distanceToSqr(this.getOwner()) > 900)
                this.retractHook();
        }
    }

    @Override
    protected float motionReduction(boolean inWater) {
        return 1;
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        if (!this.retracting()) {
            if (this.getOwner() instanceof LivingEntity entity)
                result.getEntity().hurt(this.damageSources().mobProjectile(this, entity), (float) entity.getAttributeValue(Attributes.ATTACK_DAMAGE));
            this.hookedEntity = result.getEntity();
            this.entityData.set(HOOKED_ENTITY, result.getEntity().getId());
            this.setDeltaMovement(Vec3.ZERO);
            if (this.getOwner() instanceof DaggerHitNotifiable notif)
                notif.onDaggerHit(this);
            return true;
        } else if (result.getEntity() == this.getOwner()) {
            this.discard();
            return true;
        }
        return false;
    }

    @Override
    protected EntityHitResult getEntityHit(Vec3 from, Vec3 to) {
        if (this.retracting())
            return HitResultUtils.rayTraceEntities(this, from, to, e -> e == this.getOwner());
        return super.getEntityHit(from, to);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockRayTraceResult) {
        if (this.hookedEntity == null)
            this.retractHook();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.remove("Shooter"); //Don't save owner
    }

    public void retractHook() {
        this.getEntityData().set(RETRACTING, true);
        this.noPhysics = true;
        Entity entity = this.getOwner();
        if (entity != null) {
            if (this.hookedEntity != null) {
                Vec3 vector3d = entity.position().subtract(this.hookedEntity.position()).scale(0.18);
                vector3d = vector3d.add(0, 0.5, 0);
                if (entity instanceof LivingEntity living)
                    this.hookedEntity.hurt(this.damageSources().mobProjectile(this, living), (float) living.getAttributeValue(Attributes.ATTACK_DAMAGE));
                this.hookedEntity.setDeltaMovement(vector3d);
                //this.hookedEntity.push(vector3d.x(), vector3d.y(), vector3d.z());
                this.hookedEntity.hurtMarked = true;
            }
            this.hookedEntity = null;
            this.getEntityData().set(HOOKED_ENTITY, -1);
        } else {
            this.discard();
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        if (this.getOwner() instanceof Player player)
            Platform.INSTANCE.getPlayerData(player).setThrownDagger(null);
    }
}
