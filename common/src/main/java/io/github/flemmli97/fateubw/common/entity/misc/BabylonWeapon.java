package io.github.flemmli97.fateubw.common.entity.misc;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.entity.utils.EntityTrailHandler;
import io.github.flemmli97.fateubw.common.particles.trail.TrailPositions;
import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateParticles;
import io.github.flemmli97.fateubw.common.registry.FateSounds;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.EntityUtils;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.ColorData;
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import io.github.flemmli97.tenshilib.common.utils.HitResultUtils;
import io.github.flemmli97.tenshilib.common.utils.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class BabylonWeapon extends BaseProjectile {

    protected static final EntityDataAccessor<ItemStack> WEAPON_TYPE = SynchedEntityData.defineId(BabylonWeapon.class, EntityDataSerializers.ITEM_STACK);
    protected static final EntityDataAccessor<Integer> SHOOT_TIME = SynchedEntityData.defineId(BabylonWeapon.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> PREPARING = SynchedEntityData.defineId(BabylonWeapon.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> DESPAWN = SynchedEntityData.defineId(BabylonWeapon.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<BlockPos> GROUND = SynchedEntityData.defineId(BabylonWeapon.class, EntityDataSerializers.BLOCK_POS);

    private LivingEntity target;

    public final int renderRand = this.random.nextInt(1000);

    private final BlockState particleState = Blocks.GOLD_BLOCK.defaultBlockState();
    private int preparationTick, despawnTimer;

    private final EntityTrailHandler trailHandler = new EntityTrailHandler(this, 12);

    public BabylonWeapon(EntityType<? extends BabylonWeapon> type, Level level) {
        super(type, level);
    }

    public BabylonWeapon(Level level, LivingEntity shootingEntity) {
        super(FateEntities.BABYLON.get(), level, shootingEntity);
        this.entityData.set(SHOOT_TIME, this.random.nextInt(15) + 15);
    }

    public BabylonWeapon(Level level, LivingEntity shootingEntity, LivingEntity target) {
        this(level, shootingEntity);
        this.target = target;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(WEAPON_TYPE, ItemStack.EMPTY);
        builder.define(PREPARING, true);
        builder.define(SHOOT_TIME, 20);
        builder.define(DESPAWN, false);
        builder.define(GROUND, BlockPos.ZERO);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == GROUND) {
            this.setInGround(this.entityData.get(GROUND));
        }
    }

    public boolean preparing() {
        return this.entityData.get(PREPARING);
    }

    @Override
    public int livingTickMax() {
        return this.inGround ? Integer.MAX_VALUE : 250;
    }

    @Override
    public void tick() {
        if (this.preparing()) {
            this.updatePreparation();
        } else {
            if (!this.level().isClientSide) {
                Entity thrower = this.getOwner();
                if (thrower == null || !thrower.isAlive()) {
                    this.discard();
                    return;
                }
            }
            if (this.firstTick) {
                this.trailHandler.tick();
            }
            if (this.level().isClientSide && !this.inGround)
                this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, this.particleState), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            super.tick();
            this.trailHandler.tick();
        }
        if (this.despawning()) {
            ++this.despawnTimer;
            if (!this.level().isClientSide) {
                if (this.despawnTimer >= 40)
                    this.discard();
            } else if (this.random.nextBoolean()) {
                AdvancedParticleContainer.make(FateParticles.LIGHT.get())
                        .addData(new ColorData(1.0f, 0.85f, 0.3f, 0.5f))
                        .addData(new ScaleData(0.15f))
                        .addData(new MotionData(this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01))
                        .addData(new ParticleMetaData(20, false, 0))
                        .add(this.level(), this.getX(this.random.nextGaussian()), this.getY(this.random.nextGaussian()), this.getZ(this.random.nextGaussian()));
            }
        }
    }

    @Override
    protected void tickInGround() {
        super.tickInGround();
        if (!this.level().isClientSide() && this.ticksInGround > 40) {
            if (!this.despawning())
                this.entityData.set(DESPAWN, true);
        }
    }

    @Override
    protected void resetInGround() {
        this.entityData.set(DESPAWN, true);
    }

    @Override
    public void moveEntity() {
        if (this.inGround)
            return;
        super.moveEntity();
    }

    public boolean despawning() {
        return this.entityData.get(DESPAWN);
    }

    public float despawnProgress() {
        return Math.min(1, this.despawnTimer / 40f);
    }

    public float preparationState(float partialTicks) {
        if (!this.preparing())
            return 1;
        return Mth.clamp((this.preparationTick + partialTicks) / this.entityData.get(SHOOT_TIME), 0, 1);
    }

    private void updatePreparation() {
        this.preparationTick++;
        Vec3 motion = this.getDeltaMovement();
        double f = Math.sqrt(horizontalMag(motion));
        this.setYRot(this.updateRotation(this.yRotO, (float) (Mth.atan2(motion.x, motion.z) * Mth.RAD_TO_DEG)));
        this.setXRot(this.updateRotation(this.xRotO, (float) (Mth.atan2(motion.y, f) * Mth.RAD_TO_DEG)));

        if (this.level().isClientSide) {
            AdvancedParticleContainer.make(FateParticles.LIGHT.get())
                    .addData(new ColorData(235 / 255F, 235 / 255F, 0 / 255F, 1))
                    .addData(new ScaleData(0.15f))
                    .addData(new MotionData(this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01))
                    .addData(new ParticleMetaData(20, false, 0))
                    .add(this.level(), this.getX(), this.getY(), this.getZ());
        } else {
            if (this.tickCount == 1)
                this.playSound(FateSounds.ENTITY_BABYLON_SPAWN.get(), 0.7f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.9f);
            if (this.preparing() && this.preparationTick >= this.entityData.get(SHOOT_TIME)) {
                this.entityData.set(PREPARING, false);
                Entity thrower = this.getOwner();
                if (thrower instanceof Player) {
                    HitResult hit = HitResultUtils.entityRayTrace(thrower, 64, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, false, false, null);
                    this.shootAtPosition(hit.getLocation().x, hit.getLocation().y, hit.getLocation().z, 1.f, 6);
                } else if (this.target != null) {
                    this.shootAtEntity(this.target, 1.f, 6);
                } else {
                    this.discard();
                }
                this.playSound(FateSounds.ENTITY_BABYLON_SHOOT.get(), 0.8f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.5f);
            }
        }
    }

    @Override
    public void shootAtEntity(Entity target, float velocity, float inaccuracy, boolean ignoreGravity) {
        Vec3 targetPos = EntityUtils.getStraightProjectileTarget(this.position(), target).add(target.getDeltaMovement().scale(2));
        this.shootAtPosition(targetPos.x(), targetPos.y(), targetPos.z(), velocity, inaccuracy, ignoreGravity);
    }

    private float updateRotation(float prev, float current) {
        while (current - prev < -180.0F)
            prev -= 360.0F;
        while (current - prev >= 180.0F)
            prev += 360.0F;
        return Mth.lerp(0.2F, prev, current);
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
        DamageSource source = FateDamageTypes.indirect(FateDamageTypes.BABYLON, this, this.getOwner());
        float damage = (float) ItemUtils.damage(this.level(), null, result.getEntity(), source, this.getWeapon());
        boolean res = Utils.runWithInvulTimer(this.getOwner(), result.getEntity(),
                e -> e.hurt(source, damage * CommonConfig.babylonScale), 2);
        if (res) {
            if (result.getEntity() instanceof LivingEntity entity) {
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40));
            }
            if (this.level() instanceof ServerLevel serverLevel) {
                EnchantmentHelper.doPostAttackEffects(serverLevel, result.getEntity(), source);
            }
        }
        this.discard();
        return true;
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        Vec3 vec3 = result.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vec3);
        Vec3 vec32 = vec3.normalize().scale(0.2);
        this.setPosRaw(result.getLocation().x() - vec32.x, result.getLocation().y() - vec32.y, result.getLocation().z() - vec32.z);
        this.setInGround(result.getBlockPos());
    }

    @Override
    public void setInGround(BlockPos pos) {
        super.setInGround(pos);
        this.inGround = true;
        if (!this.level().isClientSide) {
            this.entityData.set(GROUND, this.inGround ? pos : BlockPos.ZERO);
        }
    }

    public ItemStack getWeapon() {
        return this.entityData.get(WEAPON_TYPE);
    }

    public void setWeapon(ItemStack stack) {
        if (!stack.isEmpty()) {
            this.entityData.set(WEAPON_TYPE, stack);
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("Weapon", ItemStack.CODEC.encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.getWeapon()).getOrThrow());
        compound.putBoolean("Preparing", this.preparing());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setWeapon(ItemStack.CODEC.parse(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), compound.get("Weapon")).getOrThrow());
        this.entityData.set(PREPARING, compound.getBoolean("Preparing"));
    }

    public TrailPositions trailPositions() {
        return this.trailHandler.getPositions();
    }

    public static void spawnWeapons(LivingEntity thrower, LivingEntity target, int amount, int range) {
        for (Vec3 offset : Utils.randomSidedPositions(thrower, amount, range)) {
            BabylonWeapon weapon = new BabylonWeapon(thrower.level(), thrower, target);
            // Initial rotation is based of the delta. don't want to dig into where its exactly handled so this will do
            weapon.setPos(offset.x, offset.y + thrower.getBbHeight() * 0.5, offset.z);
            Vec3 dir = Vec3.directionFromRotation(0, thrower.getYRot());
            weapon.shoot(dir.x(), dir.y(), dir.z(), 0.02F, 0);
            weapon.setWeapon(CommonConfig.babylonWeapons.getRandomWeapon(weapon.random));
            weapon.level().addFreshEntity(weapon);
        }
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
            BabylonWeapon weapon = new BabylonWeapon(thrower.level(), thrower, target);
            // Initial rotation is based of the delta. don't want to dig into where its exactly handled so this will do
            Vec3 dir = Vec3.directionFromRotation(-offset.getSecond(), offset.getFirst());
            Vec3 area = pos.add(dir.scale(range));
            weapon.setPos(area.x, area.y, area.z);
            weapon.shoot(-dir.x(), -dir.y(), -dir.z(), 0.02F, 0);
            weapon.setWeapon(CommonConfig.babylonWeapons.getRandomWeapon(weapon.random));
            weapon.level().addFreshEntity(weapon);
        }
    }

    private static float greatCircDist(float yRot1, float xRot1, float yRot2, float xRot2) {
        return (float) Math.acos(Mth.sin(xRot1) * Mth.sin(xRot2) + Mth.cos(xRot1) * Mth.cos(xRot2) * Mth.cos(Mth.abs(yRot1 - yRot2)));
    }
}
