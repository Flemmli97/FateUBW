package io.github.flemmli97.fateubw.common.entity.misc;

import com.mojang.serialization.DynamicOps;
import io.github.flemmli97.fateubw.common.entity.utils.EntityTrailHandler;
import io.github.flemmli97.fateubw.common.lib.FateTags;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.provider.ParticlePositionProvider;
import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateSounds;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.fateubw.mixin.ProjectileAccessor;
import io.github.flemmli97.fateubw.mixinhelper.ProjectileExtension;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import io.github.flemmli97.tenshilib.loader.TenshiLibCrossPlat;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class ProjectileBlockingItemEntity extends BaseProjectile {

    protected static final EntityDataAccessor<ItemStack> WEAPON_TYPE = SynchedEntityData.defineId(ProjectileBlockingItemEntity.class, EntityDataSerializers.ITEM_STACK);
    protected static final EntityDataAccessor<Boolean> TARGETING = SynchedEntityData.defineId(ProjectileBlockingItemEntity.class, EntityDataSerializers.BOOLEAN);
    private static final TrailInfo INFO = TrailInfo.builder(new ParticlePositionProvider.ParticlePositionData(0))
            .setColor(20 / 255f, 41 / 255f, 168 / 255f, 0.7f)
            .setColor2(42 / 255f, 63 / 255f, 185 / 255f, 0.3f)
            .setWidth(0.1f)
            .setWidth2(0.001f)
            .setInterpolation(1)
            .build();
    protected static final int LIVE_TIME = 300;

    private Vec3 offset = Vec3.ZERO;
    private Projectile targeting;

    private int protectionTime = LIVE_TIME - 60;

    private final EntityTrailHandler trailHandler = new EntityTrailHandler(this, 6).setInfo(INFO);

    public ProjectileBlockingItemEntity(EntityType<? extends ProjectileBlockingItemEntity> type, Level level) {
        super(type, level);
    }

    public ProjectileBlockingItemEntity(Level level, LivingEntity shooter, Vec3 offset) {
        super(FateEntities.PROJECTILE_BLOCKING_ENTITY.get(), level, shooter);
        this.offset = offset;
        this.protectionTime -= this.getRandom().nextInt(4) * 4;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(WEAPON_TYPE, ItemStack.EMPTY);
        builder.define(TARGETING, false);
    }

    public ItemStack getItem() {
        return this.entityData.get(WEAPON_TYPE);
    }

    public void setItem(ItemStack stack) {
        if (!stack.isEmpty()) {
            this.entityData.set(WEAPON_TYPE, stack);
        }
    }

    public boolean isTargeting() {
        return this.entityData.get(TARGETING);
    }

    @Override
    public int livingTickMax() {
        return LIVE_TIME;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            --this.protectionTime;
            this.handleMovement();
            this.entityData.set(TARGETING, this.targeting != null);
            if (this.protectionTime == 0 && this.targeting == null) {
                Entity owner = this.getOwner();
                if (owner instanceof Mob mob && mob.getTarget() != null) {
                    this.shootAtEntity(mob.getTarget(), 1, 0);
                } else if (owner != null) {
                    this.shoot(owner, owner.getXRot(), owner.getYRot(), -5, 1, 0);
                }
            }
        }
        if (!this.entityData.get(TARGETING)) {
            Entity owner = this.getOwner();
            if (owner != null) {
                this.setRot(owner.getViewYRot(1), 0);
            } else {
                this.discard();
            }
        } else {
            Vec3 motion = this.getDeltaMovement();
            double f = Math.sqrt(horizontalMag(motion));
            this.setYRot((float) (Mth.atan2(motion.x, motion.z) * (180 / Math.PI)));
            this.setXRot((float) (Mth.atan2(motion.y, f) * (180 / Math.PI)));
        }
    }

    protected void handleMovement() {
        if (!(this.getOwner() instanceof LivingEntity owner)) {
            return;
        }
        if (this.targeting == null || !this.targeting.isAlive()) {
            List<Projectile> projectiles = owner.level().getEntities(EntityTypeTest.forClass(Projectile.class),
                    owner.getBoundingBox().inflate(16), this::isProjectileTargetingOwner);
            projectiles.sort(Comparator.comparingDouble(this::distanceToSqr));
            Projectile target = projectiles.isEmpty() ? null : projectiles.getFirst();
            if (target != null) {
                this.targeting = target;
                ((ProjectileExtension) this.targeting).fateubw$setTargetedBy(this);
            }
        }
        this.updatePosition();
        if (this.targeting != null) {
            Vec3 pos = this.targeting.position();
            Vec3 to = pos.add(this.targeting.getDeltaMovement());
            BlockHitResult hit = this.level().clip(new ClipContext(pos, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            Vec3 dir = hit.getLocation().subtract(this.position());
            if (dir.lengthSqr() > 9) {
                dir = dir.scale(0.75);
                this.setDeltaMovement(dir);
            } else {
                this.setPos(hit.getLocation());
            }
        }
    }

    protected boolean isProjectileTargetingOwner(Projectile projectile) {
        if (projectile.onGround() || projectile.horizontalCollision || projectile.verticalCollision || projectile.getType().is(FateTags.EntityTypes.NON_INTERCEPTABLE_PROJECTILE))
            return false;
        if (((ProjectileExtension) projectile).fateubw$targetedBy() != null && ((ProjectileExtension) projectile).fateubw$targetedBy() != this) {
            return false;
        }
        Entity owner = this.getOwner();
        if (owner == null) {
            return false;
        }
        if (projectile.getOwner() == owner) {
            return false;
        }
        if (Utils.alliedTo(projectile.getOwner(), owner)) {
            return false;
        }
        double dist = Math.max(3, projectile.getDeltaMovement().length());
        double size = 8;
        OrientedBoundingBox obb = new OrientedBoundingBox(new AABB(-size * 0.5, -size * 0.5, dist * 0.5, size * 0.5, size * 0.5, dist),
                -projectile.getYRot(), projectile.getXRot(), projectile.position());
        return obb.intersects(owner.getBoundingBox());
    }

    public void updatePosition() {
        Entity owner = this.getOwner();
        if (owner == null || this.protectionTime <= 0)
            return;
        if (this.targeting != null && this.targeting.isAlive()
                && !this.targeting.onGround() && !this.targeting.horizontalCollision && !this.targeting.verticalCollision) {
            return;
        }
        this.targeting = null;
        Vec3 ownerPos = owner.getEyePosition();
        Vec3 offset = this.offset.xRot(-owner.getViewXRot(1) * Mth.DEG_TO_RAD)
                .yRot(-owner.getViewYRot(1) * Mth.DEG_TO_RAD);
        if (this.tickCount == 0) {
            this.setPos(ownerPos.x + offset.x(), ownerPos.y + offset.y(), ownerPos.z + offset.z());
        } else {
            this.setDeltaMovement(ownerPos.x + offset.x() - this.getX(), ownerPos.y + offset.y() - this.getY(), ownerPos.z + offset.z() - this.getZ());
        }
        this.setRot(owner.getViewYRot(1), 0);
        this.hasImpulse = true;
    }

    @Override
    protected void doCollision() {
        Vec3 pos = this.position();
        Vec3 to = pos.add(this.getDeltaMovement());
        EntityHitResult res;
        while ((res = this.getEntityHit(pos, to)) != null && this.isAlive()) {
            this.checkedEntities.add(res.getEntity().getUUID());
            if (!TenshiLibCrossPlat.INSTANCE.projectileImpactEvent(this, res) && !this.attackedEntities.contains(res.getEntity().getUUID()) && this.entityRayTraceHit(res)) {
                this.attackedEntities.add(res.getEntity().getUUID());
                if (this.maxPierceAmount() != -1 && this.attackedEntities.size() > this.maxPierceAmount())
                    this.onReachMaxPierce();
            }
        }
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    protected boolean canHit(Entity target) {
        if (target instanceof Projectile && !this.checkedEntities.contains(target.getUUID())) {
            return true;
        }
        return super.canHit(target);
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        if (result.getEntity() instanceof Projectile p && p.getOwner() != this.getOwner()) {
            result.getEntity().discard();
            ((ProjectileAccessor) p).fateubw$onHit(new EntityHitResult(this, result.getLocation()));
            p.discard();
            this.discard();
            this.playSound(FateSounds.BLOCK.get(), 1, 1.5f);
            for (int i = 0; i < 20; i++) {
                double d0 = this.random.nextGaussian() * 0.02;
                double d1 = this.random.nextGaussian() * 0.02;
                double d2 = this.random.nextGaussian() * 0.02;
                ((ServerLevel) this.level()).sendParticles(ParticleTypes.POOF,
                        this.getRandomX(1.0), this.getRandomY(), this.getRandomZ(1.0), 0, d0, d1, d2, 0);
            }
            return true;
        }
        if (this.protectionTime > 0) {
            return false;
        }
        DamageSource source = FateDamageTypes.indirect(FateDamageTypes.WEAPON_PROJECTILE, this, this.getOwner());
        float damage = Utils.itemBasedProjectileDamage(this.getOwner(), this, source, this.getItem(), result.getEntity(), 0.5f);
        boolean res = Utils.runWithInvulTimer(this.getOwner(), result.getEntity(),
                e -> e.hurt(source, damage * this.damageMultiplier), 2);
        if (res) {
            if (this.level() instanceof ServerLevel serverLevel) {
                EnchantmentHelper.doPostAttackEffects(serverLevel, result.getEntity(), source);
            }
        }
        this.discard();
        return true;
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        DynamicOps<Tag> ops = this.registryAccess().createSerializationContext(NbtOps.INSTANCE);
        if (!this.getItem().isEmpty()) {
            compound.put("Weapon", ItemStack.CODEC.encodeStart(ops, this.getItem()).getOrThrow());
        }
        compound.put("Offset", Vec3.CODEC.encodeStart(ops, this.offset).getOrThrow());
        compound.putInt("ProtectionTime", this.protectionTime);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        DynamicOps<Tag> ops = this.registryAccess().createSerializationContext(NbtOps.INSTANCE);
        if (compound.contains("Weapon")) {
            this.setItem(ItemStack.CODEC.parse(ops, compound.get("Weapon")).getOrThrow());
        }
        this.offset = Vec3.CODEC.parse(ops, compound.get("Offset")).getOrThrow();
        this.protectionTime = compound.getInt("ProtectionTime");
    }
}
