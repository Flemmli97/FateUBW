package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModParticles;
import io.github.flemmli97.fateubw.common.utils.CachedWeaponList;
import io.github.flemmli97.fateubw.common.utils.CustomDamageSource;
import io.github.flemmli97.tenshilib.common.entity.EntityProjectile;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Random;

public class BabylonWeapon extends EntityProjectile {

    protected static final EntityDataAccessor<ItemStack> weaponType = SynchedEntityData.defineId(BabylonWeapon.class, EntityDataSerializers.ITEM_STACK);
    protected static final EntityDataAccessor<Integer> shootTime = SynchedEntityData.defineId(BabylonWeapon.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> preShootTick = SynchedEntityData.defineId(BabylonWeapon.class, EntityDataSerializers.INT);

    public boolean idle = true;
    private LivingEntity target;
    private double dmg;

    public final int renderRand = this.random.nextInt(10);

    private final BlockState particleState = Blocks.GOLD_BLOCK.defaultBlockState();

    public BabylonWeapon(EntityType<? extends BabylonWeapon> type, Level world) {
        super(type, world);
    }

    public BabylonWeapon(Level world, LivingEntity shootingEntity) {
        super(ModEntities.babylon.get(), world, shootingEntity);
    }

    public BabylonWeapon(Level world, LivingEntity shootingEntity, LivingEntity target) {
        this(world, shootingEntity);
        this.target = target;
    }

    @Override
    public int livingTickMax() {
        return 200;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(weaponType, ItemStack.EMPTY);
        this.entityData.define(shootTime, this.random.nextInt(20) + 25);
        this.entityData.define(preShootTick, 0);
    }

    @Override
    public void tick() {
        Entity thrower = this.getOwner();
        if (this.getPreShootTick() <= this.entityData.get(shootTime)) {
            this.livingTicks++;
            this.updatePreShootTick();
        }
        if (this.getPreShootTick() == this.entityData.get(shootTime)) {
            if (!this.level.isClientSide) {
                if (thrower instanceof Player) {
                    HitResult hit = RayTraceUtils.entityRayTrace(thrower, 64, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, false, false, null);
                    this.shootAtPosition(hit.getLocation().x, hit.getLocation().y, hit.getLocation().z, 0.5F, 0.5F);
                } else if (this.target != null) {
                    Vec3 targetMot = this.target.getDeltaMovement();
                    this.shootAtPosition(this.target.getX() + targetMot.x, this.target.getY() + this.target.getBbHeight() / 2 + targetMot.y, this.target.getZ() + targetMot.z, 0.5F, 4);
                }
            }
        } else if (this.getPreShootTick() > this.entityData.get(shootTime)) {
            this.idle = false;
            if (!this.level.isClientSide) {
                if (thrower == null || !thrower.isAlive()) {
                    this.kill();
                    return;
                }
            }
            if (this.level.isClientSide)
                this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, this.particleState), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            super.tick();
        }
    }

    private int getPreShootTick() {
        return this.entityData.get(preShootTick);
    }

    private void updatePreShootTick() {
        this.entityData.set(preShootTick, this.getPreShootTick() + 1);
        if (this.level.isClientSide) {
            this.level.addParticle(new ColoredParticleData(ModParticles.light.get(), 235 / 255F, 235 / 255F, 0 / 255F, 1, 0.15f), this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01);
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
        result.getEntity().hurt(CustomDamageSource.babylon(this, this.getOwner()), (float) this.dmg * 1.5F);
        this.kill();
        return true;
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        this.kill();
    }

    public void setEntityProperties() {
        this.setWeapon(CachedWeaponList.getRandomWeapon(this.random));
        this.setProjectileAreaPosition(7);
    }

    public ItemStack getWeapon() {
        return this.entityData.get(weaponType);
    }

    public void setWeapon(ItemStack stack) {
        if (!stack.isEmpty()) {
            this.entityData.set(weaponType, stack);
            this.dmg = ItemUtils.damage(stack) * Config.Common.babylonScale;
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
        this.entityData.set(preShootTick, compound.getInt("PreShoot"));
    }

    /**
     * for symmetry range should be odd number, minimum is 3
     */
    public void setProjectileAreaPosition(int range) {
        Random rand = new Random();
        Entity thrower = this.getOwner();
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
        int scaleHor = rand.nextInt(range) - (range - 1) / 2;
        int scaleVert = rand.nextInt((range + 1) / 2);
        double distance = (scaleHor * scaleHor + scaleVert * scaleVert);
        float rangeSq = (range - 1) / 2 * (range - 1) / 2;
        boolean playerSpace = (scaleVert == 0 && scaleHor == 0);
        if (distance <= rangeSq && !playerSpace) {
            Vec3 area = pos.add(hor.scale(scaleHor * 2)).add(vert.scale(scaleVert * 2 + 1));
            BlockPos spawnPos = new BlockPos(area);
            AABB axis = new AABB(spawnPos, spawnPos).inflate(1.0);
            List<BabylonWeapon> list = this.level.getEntitiesOfClass(BabylonWeapon.class, axis);
            if (list.isEmpty()) {
                this.shoot(thrower, thrower.getXRot(), thrower.getYRot(), 0, 0.5F, 0);
                this.setPos(area.x, area.y, area.z);
                this.level.addFreshEntity(this);
            }
        } else {
            this.setProjectileAreaPosition(range);
        }
    }
}
