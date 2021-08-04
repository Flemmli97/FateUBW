package io.github.flemmli97.fate.common.entity.minions;

import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.api.entity.AnimationHandler;
import com.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.fate.common.entity.ChargingHandler;
import io.github.flemmli97.fate.common.entity.IServantMinion;
import io.github.flemmli97.fate.common.entity.ai.GordiusAttackGoal;
import net.minecraft.block.BlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.entity.PartEntity;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class EntityGordius extends CreatureEntity implements IServantMinion, IAnimated {

    public final GordiusAttackGoal attackAI = new GordiusAttackGoal(this);

    private static final AnimatedAction charging = new AnimatedAction(20, 5, "charge");
    private static final AnimatedAction[] anims = {charging};

    private final AnimationHandler<EntityGordius> animationHandler = new AnimationHandler<>(this, anims);

    private final GordiusParts bulls = new GordiusParts(this, 2, 1.5f);
    private final GordiusParts wheels = new GordiusParts(this, 2.2f, 1.6f);
    private final GordiusParts[] parts = {this.bulls, this.wheels};

    private static final Function<AnimatedAction, Boolean> chargingAnim = anim -> anim != null && anim.getID().equals(charging.getID());
    private static final DataParameter<Float> lockedYaw = EntityDataManager.createKey(EntityGordius.class, DataSerializers.FLOAT);

    public final ChargingHandler<EntityGordius> chargingHandler = new ChargingHandler<>(this, lockedYaw, chargingAnim);

    public EntityGordius(EntityType<? extends EntityGordius> type, World world) {
        super(type, world);
        this.stepHeight = 1;
        if (world != null && !world.isRemote)
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(lockedYaw, 0f);
    }

    @Override
    public AnimationHandler<EntityGordius> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    public AnimatedAction getChargingAnim() {
        return charging;
    }

    public boolean isCharging() {
        return this.getAnimationHandler().getAnimation().map(a -> a.getID().equals(charging.getID()) && a.getTick() >= a.getAttackTime()).orElse(false);
    }

    @Override
    public void livingTick() {
        super.livingTick();
        this.getAnimationHandler().tick();
        this.chargingHandler.tick();
        Vector3d[] prePos = {this.bulls.getPositionVec(), this.wheels.getPositionVec()};
        Vector3d offSet = this.getVectorForRotation(0, this.rotationYaw);
        Vector3d bullOffset = offSet.scale(1.7);
        Vector3d wheelOffset = offSet.scale(1.2);
        this.bulls.setPosition(this.getPosX() + bullOffset.x, this.getPosY(), this.getPosZ() + bullOffset.z);
        this.wheels.setPosition(this.getPosX() - wheelOffset.x, this.getPosY(), this.getPosZ() - wheelOffset.z);
        for (int i = 0; i < prePos.length; i++) {
            PartEntity e = this.getParts()[i];
            e.prevPosX = prePos[i].x;
            e.prevPosY = prePos[i].y;
            e.prevPosZ = prePos[i].z;
            e.lastTickPosX = prePos[i].x;
            e.lastTickPosY = prePos[i].y;
            e.lastTickPosZ = prePos[i].z;
        }
    }

    @Override
    public void updatePassenger(Entity passenger) {
        if (this.isPassenger(passenger)) {
            Vector3d offSet = this.getVectorForRotation(0, this.rotationYaw).scale(1.35);
            passenger.setPosition(this.getPosX() - offSet.x, this.getPosY() + this.getMountedYOffset(), this.getPosZ() - offSet.z);
        }
    }

    @Override
    public double getMountedYOffset() {
        return this.getHeight() * 0.825;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
        if (damageSource != DamageSource.OUT_OF_WORLD && this.isCharging())
            damage *= 0.5f;
        return super.attackEntityFrom(damageSource, damage);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_COW_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENTITY_COW_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_COW_DEATH;
    }

    @Override
    protected float getSoundPitch() {
        return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {
    }

    @Override
    protected void collideWithNearbyEntities() {
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.bulls.getBoundingBox(), EntityPredicates.pushableBy(this));
        list.addAll(this.world.getEntitiesInAABBexcluding(this, this.wheels.getBoundingBox(), EntityPredicates.pushableBy(this)));
        if (!list.isEmpty()) {
            int i = this.world.getGameRules().getInt(GameRules.MAX_ENTITY_CRAMMING);
            if (i > 0 && list.size() > i - 1 && this.rand.nextInt(4) == 0) {
                int j = 0;

                for (int k = 0; k < list.size(); ++k) {
                    if (!list.get(k).isPassenger()) {
                        ++j;
                    }
                }

                if (j > i - 1) {
                    this.attackEntityFrom(DamageSource.CRAMMING, 6.0F);
                }
            }

            for (int l = 0; l < list.size(); ++l) {
                Entity entity = list.get(l);
                this.collideWithEntity(entity);
            }
        }
    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        if (this.noClip)
            return false;
        float f1 = this.bulls.getSize(this.getPose()).width * 0.8F;
        AxisAlignedBB axisalignedbb = AxisAlignedBB.withSizeAtOrigin(f1, 0.1F, f1).offset(this.bulls.getPosX(), this.bulls.getPosYEye(), this.bulls.getPosZ());
        if (this.world.func_241457_a_(this, axisalignedbb, (state, pos) -> state.isSuffocating(this.world, pos)).findAny().isPresent())
            return true;
        f1 = this.wheels.getSize(this.getPose()).width * 0.8F;
        axisalignedbb = AxisAlignedBB.withSizeAtOrigin(f1, 0.1F, f1).offset(this.wheels.getPosX(), this.wheels.getPosYEye(), this.wheels.getPosZ());
        return this.world.func_241457_a_(this, axisalignedbb, (state, pos) -> state.isSuffocating(this.world, pos)).findAny().isPresent();
    }

    @Override
    protected void doBlockCollisions() {
        AxisAlignedBB mainAABB = this.getBoundingBox();
        BlockPos corner1 = new BlockPos(mainAABB.minX + 0.001D, mainAABB.minY + 0.001D, mainAABB.minZ + 0.001D);
        BlockPos corner2 = new BlockPos(mainAABB.maxX - 0.001D, mainAABB.maxY - 0.001D, mainAABB.maxZ - 0.001D);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        if (this.world.isAreaLoaded(corner1, corner2)) {
            this.doBlockCollisionIn(mutable, this.bulls.getBoundingBox());
            this.doBlockCollisionIn(mutable, this.wheels.getBoundingBox());
        }
    }

    private void doBlockCollisionIn(BlockPos.Mutable mutable, AxisAlignedBB aabb) {
        BlockPos corner1 = new BlockPos(aabb.minX + 0.001D, aabb.minY + 0.001D, aabb.minZ + 0.001D);
        BlockPos corner2 = new BlockPos(aabb.maxX - 0.001D, aabb.maxY - 0.001D, aabb.maxZ - 0.001D);
        for (int i = corner1.getX(); i <= corner2.getX(); ++i) {
            for (int j = corner1.getY(); j <= corner2.getY(); ++j) {
                for (int k = corner1.getZ(); k <= corner2.getZ(); ++k) {
                    mutable.setPos(i, j, k);
                    BlockState blockstate = this.world.getBlockState(mutable);
                    try {
                        blockstate.onEntityCollision(this.world, mutable, this);
                        this.onInsideBlock(blockstate);
                    } catch (Throwable throwable) {
                        CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Colliding entity with block");
                        CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being collided with");
                        CrashReportCategory.addBlockInfo(crashreportcategory, mutable, blockstate);
                        throw new ReportedException(crashreport);
                    }
                }
            }
        }
    }

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    @Nullable
    @Override
    public PartEntity<?>[] getParts() {
        return this.parts;
    }
}
