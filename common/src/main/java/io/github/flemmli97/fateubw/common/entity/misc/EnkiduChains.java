package io.github.flemmli97.fateubw.common.entity.misc;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.utils.OnProjectileHit;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.fateubw.common.registry.FateSounds;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.data.SyncedDataContainer;
import io.github.flemmli97.tenshilib.common.entity.data.SyncedMobDataHandler;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.ColorData;
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import io.github.flemmli97.tenshilib.common.registry.TenshilibSyncableEntityDatas;
import io.github.flemmli97.tenshilib.common.utils.HitResultUtils;
import io.github.flemmli97.tenshilib.common.utils.TypedResource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class EnkiduChains extends BaseProjectile implements SyncedMobDataHandler {

    protected static final int CHAIN_DURATION = 100;

    protected static final EntityDataAccessor<Integer> SHOOT_TIME = SynchedEntityData.defineId(EnkiduChains.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> PREPARING = SynchedEntityData.defineId(EnkiduChains.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Integer> HOOKED_ENTITY = SynchedEntityData.defineId(EnkiduChains.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Float> START_YROT = SynchedEntityData.defineId(EnkiduChains.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> START_XROT = SynchedEntityData.defineId(EnkiduChains.class, EntityDataSerializers.FLOAT);

    public static final TypedResource<Vec3> START_POSITION = new TypedResource<>(Fate.modRes("start_position"));

    private final SyncedDataContainer<EnkiduChains> syncedDataContainer = SyncedDataContainer.builder(this)
            .define(START_POSITION, TenshilibSyncableEntityDatas.VEC_3.get(), null).build();

    private LivingEntity target;
    private Entity hookedEntity;
    private Vec3 hookedEntityPosition;
    private int preparationTick, hitTimer;

    public final int renderRand = this.random.nextInt(1000);

    public EnkiduChains(EntityType<? extends EnkiduChains> type, Level level) {
        super(type, level);
    }

    public EnkiduChains(Level level, LivingEntity shootingEntity) {
        super(FateEntities.ENKIDU_CHAINS.get(), level, shootingEntity);
        this.entityData.set(SHOOT_TIME, this.random.nextInt(15) + 15);
    }

    public EnkiduChains(Level level, LivingEntity shooter, LivingEntity target) {
        this(level, shooter);
        this.target = target;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PREPARING, true);
        builder.define(SHOOT_TIME, 20);
        builder.define(HOOKED_ENTITY, 0);
        builder.define(START_YROT, 0f);
        builder.define(START_XROT, 0f);
    }

    @Override
    public SyncedDataContainer<?> getDataContainer() {
        return this.syncedDataContainer;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (HOOKED_ENTITY.equals(key)) {
            int i = this.getEntityData().get(HOOKED_ENTITY);
            this.hookedEntity = i > 0 ? this.level().getEntity(i) : null;
        }
        super.onSyncedDataUpdated(key);
    }

    public boolean preparing() {
        return this.entityData.get(PREPARING);
    }

    @Override
    public int livingTickMax() {
        return 80 + (this.hookedEntity != null ? CHAIN_DURATION : 0);
    }

    @Override
    public void tick() {
        if (this.level().isClientSide) {
            AdvancedParticleContainer.make(FateParticles.LIGHT.get())
                    .addData(new ColorData(235 / 255F, 235 / 255F, 0 / 255F, 1))
                    .addData(new ScaleData(0.3f))
                    .addData(new MotionData(this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01))
                    .addData(new ParticleMetaData(20, false, 0))
                    .add(this.level(), this.getStartPosition().x(), this.getStartPosition().y() + this.getBbHeight() * 0.5, this.getStartPosition().z());
        }
        if (this.preparing()) {
            this.updatePreparation();
            return;
        }
        if (!this.level().isClientSide) {
            Entity thrower = this.getOwner();
            if (thrower == null || !thrower.isAlive()) {
                this.discard();
                return;
            }
            if (this.hookedEntity != null) {
                this.hitTimer++;
                if (!this.hookedEntity.isAlive() || this.hitTimer > CHAIN_DURATION) {
                    this.discard();
                } else {
                    if (this.hookedEntity instanceof Player player) {
                        if (!player.getAbilities().invulnerable) {
                            player.setDeltaMovement(Vec3.ZERO);
                            player.teleportTo(this.hookedEntityPosition.x(), this.hookedEntityPosition.y(), this.hookedEntityPosition.z());
                            player.hurtMarked = true;
                        }
                    } else {
                        this.hookedEntity.setPos(this.hookedEntityPosition);
                        this.hookedEntity.setDeltaMovement(Vec3.ZERO);
                    }
                }
            }
            if (this.tickCount < 4) {
                this.playSound(SoundEvents.CHAIN_PLACE, 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.8f);
            }
        }
        super.tick();
    }

    @Override
    public void moveEntity() {
        if (this.hookedEntity != null)
            return;
        super.moveEntity();
    }

    private void updatePreparation() {
        this.preparationTick++;
        Vec3 motion = this.getDeltaMovement();
        double f = Math.sqrt(horizontalMag(motion));
        this.setYRot(this.updateRotation(this.yRotO, (float) (Mth.atan2(motion.x, motion.z) * Mth.RAD_TO_DEG)));
        this.setXRot(this.updateRotation(this.xRotO, (float) (Mth.atan2(motion.y, f) * Mth.RAD_TO_DEG)));
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();

        if (!this.level().isClientSide) {
            this.entityData.set(START_YROT, this.getYRot());
            this.entityData.set(START_XROT, this.getXRot());
            this.syncedDataContainer.set(START_POSITION, this.position());
            if (this.tickCount == 1)
                this.playSound(FateSounds.ENTITY_BABYLON_SPAWN.get(), 0.7f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.9f);
            if (this.preparing() && this.preparationTick >= this.entityData.get(SHOOT_TIME)) {
                this.entityData.set(PREPARING, false);
                Entity thrower = this.getOwner();
                if (thrower instanceof Player) {
                    HitResult hit = HitResultUtils.entityRayTrace(thrower, 64, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, false, false, null);
                    this.shootAtPosition(hit.getLocation().x, hit.getLocation().y, hit.getLocation().z, 1, 6);
                } else if (this.target != null) {
                    this.shootAtEntity(this.target, 1, 6);
                } else {
                    this.discard();
                }
                this.playSound(SoundEvents.CHAIN_PLACE, 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.8f);
            }
        }
    }

    private float updateRotation(float prev, float current) {
        while (current - prev < -180.0F)
            prev -= 360.0F;
        while (current - prev >= 180.0F)
            prev += 360.0F;
        return Mth.lerp(0.2F, prev, current);
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
        if (result.getEntity() == this.getOwner())
            return false;
        if (this.hookedEntity == null) {
            boolean hurt = false;
            if (this.getOwner() instanceof LivingEntity entity)
                hurt = Utils.runWithInvulTimer(entity, result.getEntity(),
                        target -> target.hurt(this.damageSources().mobProjectile(this, entity), (float) entity.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.4f), 2);
            if (!hurt)
                return false;
            this.moveEntity();
            this.hookedEntity = result.getEntity();
            this.hookedEntityPosition = this.hookedEntity.position();
            this.entityData.set(HOOKED_ENTITY, result.getEntity().getId());
            if (this.getOwner() instanceof OnProjectileHit notif)
                notif.onProjectileHit(this);
        }
        return true;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockRayTraceResult) {
        this.discard();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Preparing", this.preparing());
        Vec3 start = this.getStartPosition();
        compound.put("StartPosition", Vec3.CODEC.encodeStart(NbtOps.INSTANCE, start).getOrThrow());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(PREPARING, compound.getBoolean("Preparing"));
        this.syncedDataContainer.set(START_POSITION, Vec3.CODEC.parse(NbtOps.INSTANCE, compound.get("StartPosition"))
                .result().orElse(this.position()));
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return super.getBoundingBoxForCulling().expandTowards(this.getStartPosition().subtract(this.position()));
    }

    public boolean hasHooked() {
        return this.entityData.get(HOOKED_ENTITY) > 0;
    }

    public Vec3 getStartPosition() {
        Vec3 start = this.syncedDataContainer.get(START_POSITION);
        return start == null ? this.position() : start;
    }

    public float getStartY() {
        return this.entityData.get(START_YROT);
    }

    public float getStartX() {
        return this.entityData.get(START_XROT);
    }

    public static void spawnWeaponsAround(LivingEntity thrower, LivingEntity target, int amount, int range) {
        int targetSize = Math.max(Mth.ceil(target.getBbHeight()), Mth.ceil(target.getBbWidth()));
        range = Math.max(targetSize + 3, range);
        List<Pair<Float, Float>> angles = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            float yRot = thrower.getRandom().nextFloat() * 360;
            float xRot = thrower.getRandom().nextFloat() * 70 + 20;
            int retry = 0;
            while (true) {
                float greatDist = Float.MAX_VALUE;
                for (Pair<Float, Float> p : angles) {
                    if (p != null) {
                        float d = greatCircDist(yRot, xRot, p.getFirst(), p.getSecond());
                        if (d < greatDist) {
                            greatDist = d;
                        }
                    }
                }
                if (greatDist * range > 2) {
                    angles.add(Pair.of(yRot, xRot));
                    break;
                }
                if (++retry > 10)
                    break;
            }
        }
        Vec3 pos = target.position();
        for (Pair<Float, Float> offset : angles) {
            if (offset == null)
                continue;
            EnkiduChains chains = new EnkiduChains(thrower.level(), thrower, target);
            // Initial rotation is based of the delta. don't want to dig into where its exactly handled so this will do
            chains.shoot(thrower, offset.getSecond(), offset.getFirst(), 0, 0.02F, 0);
            Vec3 area = pos.add(Vec3.directionFromRotation(-offset.getSecond(), offset.getFirst()).scale(range));
            chains.setPos(area.x, area.y, area.z);
            chains.level().addFreshEntity(chains);
        }
    }

    private static float greatCircDist(float yRot1, float xRot1, float yRot2, float xRot2) {
        return (float) Math.acos(Mth.sin(xRot1) * Mth.sin(xRot2) + Mth.cos(xRot1) * Mth.cos(xRot2) * Mth.cos(Mth.abs(yRot1 - yRot2)));
    }
}
