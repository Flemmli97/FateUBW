package com.flemmli97.fate.common.entity;

import com.flemmli97.fate.common.config.Config;
import com.flemmli97.fate.common.registry.ModEntities;
import com.flemmli97.fate.common.utils.CachedWeaponList;
import com.flemmli97.fate.common.utils.CustomDamageSource;
import com.flemmli97.tenshilib.common.entity.EntityProjectile;
import com.flemmli97.tenshilib.common.utils.ItemUtils;
import com.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class EntityBabylonWeapon extends EntityProjectile {

    protected static final DataParameter<ItemStack> weaponType = EntityDataManager.createKey(EntityBabylonWeapon.class, DataSerializers.ITEMSTACK);
    protected static final DataParameter<Integer> shootTime = EntityDataManager.createKey(EntityBabylonWeapon.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> preShootTick = EntityDataManager.createKey(EntityBabylonWeapon.class, DataSerializers.VARINT);

    public boolean iddle = true;
    private LivingEntity target;
    private double dmg;

    public final int renderRand = this.rand.nextInt(10);

    private final BlockState particleState = Blocks.GOLD_BLOCK.getDefaultState();

    public EntityBabylonWeapon(EntityType<? extends EntityBabylonWeapon> type, World world) {
        super(type, world);
    }

    public EntityBabylonWeapon(World world, LivingEntity shootingEntity) {
        super(ModEntities.babylon.get(), world, shootingEntity);
    }

    public EntityBabylonWeapon(World world, LivingEntity shootingEntity, LivingEntity target) {
        this(world, shootingEntity);
        this.target = target;
    }

    @Override
    public int livingTickMax() {
        return 200;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(weaponType, ItemStack.EMPTY);
        this.dataManager.register(shootTime, this.rand.nextInt(20) + 25);
        this.dataManager.register(preShootTick, 0);
    }

    @Override
    public void tick() {
        LivingEntity thrower = this.getShooter();
        if (this.getPreShootTick() <= this.dataManager.get(shootTime)) {
            this.livingTicks++;
            this.updatePreShootTick();
        }
        if (this.getPreShootTick() == this.dataManager.get(shootTime)) {
            if (!this.world.isRemote) {
                if (thrower instanceof PlayerEntity) {
                    RayTraceResult hit = RayTraceUtils.entityRayTrace(thrower, 64, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, false, null);
                    this.shootAtPosition(hit.getHitVec().x, hit.getHitVec().y, hit.getHitVec().z, 0.5F, 0.5F);
                } else if (this.target != null) {
                    this.shootAtPosition(this.target.getX(), this.target.getY() + this.target.getHeight() / 2, this.target.getZ(), 0.5F, 4);
                }
            }
        } else if (this.getPreShootTick() > this.dataManager.get(shootTime)) {
            this.iddle = false;
            if (!this.world.isRemote) {
                if (thrower == null || thrower.isDead()) {
                    this.remove();
                    return;
                }
            }
            if (this.world.isRemote)
                this.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, this.particleState), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            super.tick();
        }
    }

    private int getPreShootTick() {
        return this.dataManager.get(preShootTick);
    }

    private void updatePreShootTick() {
        this.dataManager.set(preShootTick, this.getPreShootTick() + 1);
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
    protected float motionReduction() {
        return 1;
    }

    @Override
    protected boolean onEntityHit(EntityRayTraceResult result) {
        result.getEntity().attackEntityFrom(CustomDamageSource.babylon(this, this.getShooter()), (float) this.dmg * 1.5F);
        this.remove();
        return true;
    }

    @Override
    protected void onBlockHit(BlockRayTraceResult result) {
        this.remove();
    }

    public void setEntityProperties() {
        this.setWeapon(CachedWeaponList.getRandomWeapon(this.rand));
        this.setProjectileAreaPosition(7);
    }

    public ItemStack getWeapon() {
        return this.dataManager.get(weaponType);
    }

    public void setWeapon(ItemStack stack) {
        if (!stack.isEmpty()) {
            this.dataManager.set(weaponType, stack);
            this.dmg = ItemUtils.damage(stack) * Config.Common.babylonScale;
        }
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.put("Weapon", this.getWeapon().serializeNBT());
        compound.putInt("PreShoot", this.getPreShootTick());
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setWeapon(ItemStack.read(compound.getCompound("Weapon")));
        this.dataManager.set(preShootTick, compound.getInt("PreShoot"));
    }

    /**
     * for symmetry range should be odd number, minimum is 3
     */
    public void setProjectileAreaPosition(int range) {
        Random rand = new Random();
        LivingEntity thrower = this.getShooter();
        Vector3d pos = thrower.getPositionVec();
        Vector3d look = thrower.getLookVec();
        Vector3d vert = new Vector3d(0, 1, 0);
        if (-20 < thrower.rotationPitch && thrower.rotationPitch > 20)
            vert.rotatePitch(thrower.rotationPitch);
        if (-20 > thrower.rotationPitch)
            vert.rotatePitch(-20);
        if (20 < thrower.rotationPitch)
            vert.rotatePitch(20);
        Vector3d hor = look.crossProduct(vert);
        vert.normalize();
        hor.normalize();
        int scaleHor = rand.nextInt(range) - (range - 1) / 2;
        int scaleVert = rand.nextInt((range + 1) / 2);
        double distance = (scaleHor * scaleHor + scaleVert * scaleVert);
        float rangeSq = (range - 1) / 2 * (range - 1) / 2;
        boolean playerSpace = (scaleVert == 0 && scaleHor == 0);
        if (distance <= rangeSq && !playerSpace) {
            Vector3d area = pos.add(hor.scale(scaleHor * 2)).add(vert.scale(scaleVert * 2 + 1));
            BlockPos spawnPos = new BlockPos(area);
            AxisAlignedBB axis = new AxisAlignedBB(spawnPos, spawnPos).grow(1.0);
            List<Entity> list = this.world.getEntitiesWithinAABB(EntityBabylonWeapon.class, axis);
            if (list.isEmpty()) {
                this.shoot(thrower, thrower.rotationPitch, thrower.rotationYaw, 0, 0.5F, 0);
                this.setPosition(area.x, area.y, area.z);
                this.world.addEntity(this);
            }
        } else {
            this.setProjectileAreaPosition(range);
        }
    }
}
