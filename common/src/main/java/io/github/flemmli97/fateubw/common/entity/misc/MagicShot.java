package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.ColorData;
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
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
import org.joml.Vector3f;

public class MagicShot extends BaseProjectile {

    private static final EntityDataAccessor<Integer> TYPE_DATA = SynchedEntityData.defineId(MagicShot.class, EntityDataSerializers.INT);

    private ColorType colorType = ColorType.PURPLE;

    public MagicShot(EntityType<? extends MagicShot> type, Level level) {
        super(type, level);
    }

    public MagicShot(Level level, LivingEntity shootingEntity) {
        super(FateEntities.MAGIC_SHOT.get(), level, shootingEntity);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TYPE_DATA, 0);
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
        if (this.level().isClientSide) {
            Vector3f color = this.colorType.particleColor;
            Vec3 delta = this.getDeltaMovement().scale(0.5);
            for (int i = 0; i < 8; i++) {
                AdvancedParticleContainer.make(FateParticles.LIGHT.get())
                        .addData(new ColorData(color.x(), color.y(), color.z(), 0.5f))
                        .addData(new ScaleData(0.5f))
                        .addData(new MotionData(this.random.nextGaussian() * 0.01, Math.abs(this.random.nextGaussian() * 0.03), this.random.nextGaussian() * 0.01))
                        .addData(new ParticleMetaData(20, false, 0))
                        .build().add(this.level(), this.getX() + this.random.nextGaussian() * delta.x(), this.getY() + 0.35 + this.random.nextGaussian() * delta.y(), this.getZ() + this.random.nextGaussian() * delta.z());
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
        return result.getEntity().hurt(FateDamageTypes.indirect(FateDamageTypes.MAGIC_SHOT, this, this.getOwner()), Utils.magicDamage(this.getOwner()));
    }

    @Override
    protected void onBlockHit(BlockHitResult blockRayTraceResult) {
        this.discard();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Color", this.colorType.ordinal());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
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
