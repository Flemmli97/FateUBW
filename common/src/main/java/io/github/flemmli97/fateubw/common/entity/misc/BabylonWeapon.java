package io.github.flemmli97.fateubw.common.entity.misc;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModParticles;
import io.github.flemmli97.fateubw.common.utils.CachedWeaponList;
import io.github.flemmli97.fateubw.common.utils.CustomDamageSource;
import io.github.flemmli97.tenshilib.common.entity.EntityProjectile;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.tenshilib.common.utils.ItemUtils;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;

public class BabylonWeapon extends EntityProjectile {

    protected static final EntityDataAccessor<ItemStack> WEAPON_TYPE = SynchedEntityData.defineId(BabylonWeapon.class, EntityDataSerializers.ITEM_STACK);
    protected static final EntityDataAccessor<Integer> SHOOT_TIME = SynchedEntityData.defineId(BabylonWeapon.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> PRE_SHOOT_TICK = SynchedEntityData.defineId(BabylonWeapon.class, EntityDataSerializers.INT);

    public boolean idle = true;
    private LivingEntity target;
    private double dmg;

    public final int renderRand = this.random.nextInt(10);

    private final BlockState particleState = Blocks.GOLD_BLOCK.defaultBlockState();

    public BabylonWeapon(EntityType<? extends BabylonWeapon> type, Level world) {
        super(type, world);
    }

    public BabylonWeapon(Level world, LivingEntity shootingEntity) {
        super(ModEntities.BABYLON.get(), world, shootingEntity);
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
        this.entityData.define(WEAPON_TYPE, ItemStack.EMPTY);
        this.entityData.define(SHOOT_TIME, this.random.nextInt(20) + 25);
        this.entityData.define(PRE_SHOOT_TICK, 0);
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
                    this.shootAtPosition(hit.getLocation().x, hit.getLocation().y, hit.getLocation().z, 1.5F, 1);
                } else if (this.target != null) {
                    this.shootAtEntity(this.target, 1.5F, 1, 0);
                }
            }
        } else if (this.getPreShootTick() > this.entityData.get(SHOOT_TIME)) {
            this.idle = false;
            if (!this.level.isClientSide) {
                if (thrower == null || !thrower.isAlive()) {
                    this.discard();
                    return;
                }
            }
            if (this.level.isClientSide)
                this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, this.particleState), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            super.tick();
        }
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
        this.discard();
        return true;
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        this.discard();
    }

    public ItemStack getWeapon() {
        return this.entityData.get(WEAPON_TYPE);
    }

    public void setWeapon(ItemStack stack) {
        if (!stack.isEmpty()) {
            this.entityData.set(WEAPON_TYPE, stack);
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
            while (distance > rangeSq || offsets.contains(offset)) {
                offset = Pair.of(thrower.getRandom().nextInt(range) - (range - 1) / 2, thrower.getRandom().nextInt((range + 1) / 2));
                distance = (offset.getFirst() * offset.getFirst() + offset.getSecond() * offset.getSecond());
            }
            offsets.add(offset);
        }
        for (Pair<Integer, Integer> offset : offsets) {
            BabylonWeapon weapon = new BabylonWeapon(thrower.level, thrower, target);
            weapon.shoot(thrower, thrower.getXRot(), thrower.getYRot(), 0, 0.5F, 0);
            Vec3 area = pos.add(hor.scale(offset.getFirst() * 2)).add(vert.scale(offset.getSecond() * 2 + 1));
            weapon.setPos(area.x, area.y, area.z);
            weapon.setWeapon(CachedWeaponList.getRandomWeapon(weapon.random));
            weapon.level.addFreshEntity(weapon);
        }
    }
}
