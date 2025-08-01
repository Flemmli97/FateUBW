package io.github.flemmli97.fateubw.common.entity.misc;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModParticles;
import io.github.flemmli97.fateubw.common.registry.ModSounds;
import io.github.flemmli97.fateubw.common.utils.CustomDamageSource;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.tenshilib.common.utils.ItemUtils;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BabylonWeapon extends BaseProjectile {

    protected static final EntityDataAccessor<ItemStack> WEAPON_TYPE = SynchedEntityData.defineId(BabylonWeapon.class, EntityDataSerializers.ITEM_STACK);
    protected static final EntityDataAccessor<Integer> SHOOT_TIME = SynchedEntityData.defineId(BabylonWeapon.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> PRE_SHOOT_TICK = SynchedEntityData.defineId(BabylonWeapon.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> DESPAWN = SynchedEntityData.defineId(BabylonWeapon.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<BlockPos> GROUND = SynchedEntityData.defineId(BabylonWeapon.class, EntityDataSerializers.BLOCK_POS);

    public boolean idle = true;
    private LivingEntity target;
    private double dmg;

    public final int renderRand = this.random.nextInt(1000);

    private final BlockState particleState = Blocks.GOLD_BLOCK.defaultBlockState();
    private int despawnTimer;

    public BabylonWeapon(EntityType<? extends BabylonWeapon> type, Level level) {
        super(type, level);
    }

    public BabylonWeapon(Level level, LivingEntity shootingEntity) {
        super(ModEntities.BABYLON.get(), level, shootingEntity);
    }

    public BabylonWeapon(Level level, LivingEntity shootingEntity, LivingEntity target) {
        this(level, shootingEntity);
        this.target = target;
    }

    @Override
    public int livingTickMax() {
        return this.inGround ? Integer.MAX_VALUE : 250;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(WEAPON_TYPE, ItemStack.EMPTY);
        this.entityData.define(SHOOT_TIME, this.random.nextInt(25) + 20);
        this.entityData.define(PRE_SHOOT_TICK, 0);
        this.entityData.define(DESPAWN, false);
        this.entityData.define(GROUND, BlockPos.ZERO);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == GROUND) {
            this.setInGround(this.entityData.get(GROUND));
        }
    }

    @Override
    public void tick() {
        Entity thrower = this.getOwner();
        if (this.getPreShootTick() <= this.entityData.get(SHOOT_TIME)) {
            this.livingTicks++;
            this.updatePreShootTick();
        }
        if (this.getPreShootTick() == this.entityData.get(SHOOT_TIME)) {
            if (!this.level.isClientSide) {
                if (thrower instanceof Player) {
                    HitResult hit = RayTraceUtils.entityRayTrace(thrower, 64, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, false, false, null);
                    this.shootAtPosition(hit.getLocation().x, hit.getLocation().y, hit.getLocation().z, 1.f, 15);
                } else if (this.target != null) {
                    this.shootAtEntity(this.target, 1.f, 15);
                }
                this.playSound(ModSounds.ENTITY_BABYLON_SHOOT.get(), 0.8f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.5f);
            }
        } else if (this.getPreShootTick() > this.entityData.get(SHOOT_TIME)) {
            this.idle = false;
            if (!this.level.isClientSide) {
                if (thrower == null || !thrower.isAlive()) {
                    this.discard();
                    return;
                }
            }
            if (this.level.isClientSide && !this.inGround)
                this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, this.particleState), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            super.tick();
        }
        if (this.despawning()) {
            ++this.despawnTimer;
            if (!this.level.isClientSide) {
                if (this.despawnTimer >= 40)
                    this.discard();
            } else if (this.random.nextBoolean()) {
                this.level.addParticle(new ColoredParticleData(ModParticles.LIGHT.get(), 1.0f, 0.85f, 0.3f, 0.5f, 0.15f), this.getX(this.random.nextGaussian()), this.getY(this.random.nextGaussian()), this.getZ(this.random.nextGaussian()),
                        this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01);
            }
        }
    }

    @Override
    protected void tickInGround() {
        super.tickInGround();
        if (!this.level.isClientSide() && this.ticksInGround > 40) {
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
        return Math.min(1, (this.getPreShootTick() + partialTicks) / this.entityData.get(SHOOT_TIME));
    }

    private int getPreShootTick() {
        return this.entityData.get(PRE_SHOOT_TICK);
    }

    private void updatePreShootTick() {
        this.entityData.set(PRE_SHOOT_TICK, this.getPreShootTick() + 1);
        if (this.level.isClientSide) {
            this.level.addParticle(new ColoredParticleData(ModParticles.LIGHT.get(), 235 / 255F, 235 / 255F, 0 / 255F, 1, 0.15f), this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01);
        } else {
            if (this.tickCount == 1)
                this.playSound(ModSounds.ENTITY_BABYLON_SPAWN.get(), 0.7f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.9f);
        }
    }

    @Override
    public int livingTicks() {
        return Math.max(this.getPreShootTick(), this.livingTicks);
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
        boolean res = Utils.runWithInvulTimer(null, result.getEntity(),
                e -> e.hurt(CustomDamageSource.babylon(this, this.getOwner()), (float) this.dmg * 1.5F), 2);
        if (res && result.getEntity() instanceof LivingEntity entity) {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30));
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
        if (!this.level.isClientSide) {
            this.entityData.set(GROUND, this.inGround ? pos : BlockPos.ZERO);
        }
    }

    public ItemStack getWeapon() {
        return this.entityData.get(WEAPON_TYPE);
    }

    public void setWeapon(ItemStack stack) {
        if (!stack.isEmpty()) {
            this.entityData.set(WEAPON_TYPE, stack);
            this.dmg = ItemUtils.damage(stack) * CommonConfig.babylonScale;
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("Weapon", this.getWeapon().save(new CompoundTag()));
        compound.putInt("PreShoot", this.getPreShootTick());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setWeapon(ItemStack.of(compound.getCompound("Weapon")));
        this.entityData.set(PRE_SHOOT_TICK, compound.getInt("PreShoot"));
    }

    public static void spawnWeapons(LivingEntity thrower, LivingEntity target, int amount, int range) {
        Vec3 pos = thrower.position();
        Vec3 look = thrower.getLookAngle();
        Vec3 vert = new Vec3(0, 1, 0);
        if (-20 < thrower.getXRot() && thrower.getXRot() > 20)
            vert.xRot(thrower.getXRot());
        if (-20 > thrower.getXRot())
            vert.xRot(-20);
        if (20 < thrower.getXRot())
            vert.xRot(20);
        Vec3 hor = look.cross(vert);
        vert.normalize();
        hor.normalize();
        float rangeSq = (range - 1f) / 2 * (range - 1f) / 2;
        Set<Pair<Integer, Integer>> offsets = new HashSet<>();
        for (int i = 0; i < amount; i++) {
            Pair<Integer, Integer> offset = Pair.of(thrower.getRandom().nextInt(range) - (range - 1) / 2, thrower.getRandom().nextInt((range + 1) / 2));
            double distance = (offset.getFirst() * offset.getFirst() + offset.getSecond() * offset.getSecond());
            int retry = 0;
            while (distance > rangeSq || offsets.contains(offset) || (offset.getFirst() == 0 && offset.getSecond() == 0)) {
                offset = Pair.of(thrower.getRandom().nextInt(range) - (range - 1) / 2, thrower.getRandom().nextInt((range + 1) / 2));
                distance = (offset.getFirst() * offset.getFirst() + offset.getSecond() * offset.getSecond());
                if (++retry > 10)
                    break;
            }
            offsets.add(offset);
        }
        for (Vec3 offset : Utils.randomSidedPositions(thrower, amount, range)) {
            BabylonWeapon weapon = new BabylonWeapon(thrower.level, thrower, target);
            weapon.shoot(thrower, 0, 180 + thrower.getYRot(), 0, 0.5F, 10);
            weapon.setPos(offset.x, offset.y, offset.z);
            weapon.setWeapon(CommonConfig.babylonWeapons.getRandomWeapon(weapon.random));
            weapon.level.addFreshEntity(weapon);
        }
    }

    public static void spawnWeaponsAround(LivingEntity thrower, LivingEntity target, int amount, int range) {
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
                if (greatDist * range > 3) {
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
            BabylonWeapon weapon = new BabylonWeapon(thrower.level, thrower, target);
            weapon.shoot(thrower, offset.getSecond(), offset.getFirst(), 0, 0.5F, 10);
            Vec3 area = pos.add(Vec3.directionFromRotation(-offset.getSecond(), offset.getFirst()).scale(range));
            weapon.setPos(area.x, area.y, area.z);
            weapon.setWeapon(CommonConfig.babylonWeapons.getRandomWeapon(weapon.random));
            weapon.level.addFreshEntity(weapon);
        }
    }

    private static float greatCircDist(float yRot1, float xRot1, float yRot2, float xRot2) {
        return (float) Math.acos(Mth.sin(xRot1) * Mth.sin(xRot2) + Mth.cos(xRot1) * Mth.cos(xRot2) * Mth.cos(Mth.abs(yRot1 - yRot2)));
    }
}
