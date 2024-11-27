package io.github.flemmli97.fateubw.common.entity.misc;

import com.mojang.math.Vector3f;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModParticles;
import io.github.flemmli97.fateubw.common.utils.CustomDamageSource;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class MagicShot extends BaseProjectile {

    private static final EntityDataAccessor<Integer> TYPE_DATA = SynchedEntityData.defineId(MagicShot.class, EntityDataSerializers.INT);

    private ColorType colorType = ColorType.PURPLE;

    protected float damage;

    public MagicShot(EntityType<? extends MagicShot> type, Level world) {
        super(type, world);
    }

    public MagicShot(Level world, LivingEntity shootingEntity) {
        super(ModEntities.MAGIC_SHOT.get(), world, shootingEntity);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TYPE_DATA, 0);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == TYPE_DATA) {
            int id = this.entityData.get(TYPE_DATA);
            if (id >= 0 && id < ColorType.values().length)
                this.colorType = ColorType.values()[id];
        }
    }

    public void setType(ColorType type) {
        this.colorType = type;
        this.entityData.set(TYPE_DATA, this.colorType.ordinal());
    }

    @Override
    public int livingTickMax() {
        return 100;
    }

    @Override
    public float radius() {
        return 1;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            Vector3f color = this.colorType.particleColor;
            Vec3 delta = this.getDeltaMovement().scale(0.5);
            for (int i = 0; i < 8; i++) {
                this.level.addParticle(new ColoredParticleData(ModParticles.LIGHT.get(), color.x(), color.y(), color.z(), 0.5f, 0.5f),
                        this.getX() + this.random.nextGaussian() * delta.x(), this.getY() + 0.35 + this.random.nextGaussian() * delta.y(), this.getZ() + this.random.nextGaussian() * delta.z(),
                        this.random.nextGaussian() * 0.01, Math.abs(this.random.nextGaussian() * 0.03), this.random.nextGaussian() * 0.01);
            }
        }
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    protected float motionReduction(boolean inWater) {
        return 1;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        this.discard();
        return result.getEntity().hurt(CustomDamageSource.babylon(this, this.getOwner()), this.damage);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockRayTraceResult) {
        this.discard();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Color", this.colorType.ordinal());
        compound.putFloat("Damage", this.damage);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.damage = compound.getFloat("Damage");
        this.setType(ColorType.values()[compound.getInt("Color")]);
    }

    public enum ColorType {

        PURPLE(new Vector3f(105 / 255f, 49 / 255f, 140 / 255f));

        public final Vector3f particleColor;

        ColorType(Vector3f particleColor) {
            this.particleColor = particleColor;
        }
    }
}
