package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.common.entity.utils.EntityTrailHandler;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.provider.ParticlePositionProvider;
import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.utils.math.MathUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ReturningItemProjectile extends BaseProjectile {

    protected static final EntityDataAccessor<ItemStack> WEAPON_TYPE = SynchedEntityData.defineId(ReturningItemProjectile.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Integer> TYPE_DATA = SynchedEntityData.defineId(ReturningItemProjectile.class, EntityDataSerializers.INT);

    private final EntityTrailHandler trailHandler = new EntityTrailHandler(this, 12);

    private ItemType itemType = ItemType.KANSHOU;

    private boolean returning;

    public ReturningItemProjectile(EntityType<? extends ReturningItemProjectile> type, Level level) {
        super(type, level);
    }

    public ReturningItemProjectile(Level level, LivingEntity shootingEntity) {
        super(FateEntities.RETURNING_ITEM.get(), level, shootingEntity);
    }

    @Override
    public int livingTickMax() {
        return 50;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(WEAPON_TYPE, ItemStack.EMPTY);
        builder.define(TYPE_DATA, 0);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == TYPE_DATA) {
            int id = this.entityData.get(TYPE_DATA);
            if (id >= 0 && id < ItemType.values().length)
                this.setItemType(ItemType.values()[id]);
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

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
        this.entityData.set(TYPE_DATA, this.itemType.ordinal());
        this.trailHandler.setInfo(this.itemType.info);
        switch (this.itemType) {
            case KANSHOU -> this.trailHandler.setNormal(MathUtils.NORMAL_Z
                    .xRot(-5 * Mth.DEG_TO_RAD)
                    .yRot((this.getYRot() + 90) * Mth.DEG_TO_RAD));
            case BAKUYA -> this.trailHandler.setNormal(MathUtils.NORMAL_Z
                    .xRot(5 * Mth.DEG_TO_RAD)
                    .yRot((this.getYRot() + 90) * Mth.DEG_TO_RAD));
        }
    }

    @Override
    public void tick() {
        if (this.firstTick) {
            this.setItemType(this.getItemType());
        }
        super.tick();
        if (!this.level().isClientSide) {
            if (this.tickCount == 20) {
                this.checkedEntities.clear();
                this.attackedEntities.clear();
                this.push(0, 0.3, 0);
                this.returning = true;
            }
            if (this.returning) {
                Entity owner = this.getOwner();
                if (owner != null) {
                    Vec3 side = this.calculateViewVector(0, owner.getViewYRot(1) - 90).scale(owner.getBbWidth() * 0.4);
                    if (this.getItemType() == ItemType.BAKUYA) {
                        side = side.scale(-1);
                    }
                    Vec3 dir = owner.getEyePosition().add(side).subtract(this.getX(), this.getY(), this.getZ());
                    dir = this.getDeltaMovement().add(dir.scale(0.08));
                    if (dir.lengthSqr() > 1.1 * 1.1) {
                        dir = dir.normalize().scale(1.1);
                    }
                    this.setDeltaMovement(dir);
                    this.hasImpulse = true;
                } else {
                    this.discard();
                }
            }
        }
    }

    public ItemType getItemType() {
        return this.itemType;
    }

    @Override
    public int maxPierceAmount() {
        return 5;
    }

    @Override
    protected boolean canHit(Entity entity) {
        if (this.returning && entity == this.getOwner()) {
            return true;
        }
        return super.canHit(entity);
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        if (result.getEntity() == this.getOwner()) {
            this.discard();
            return true;
        }
        DamageSource source = FateDamageTypes.indirect(FateDamageTypes.THROWN_ITEM, this, this.getOwner());
        float damage = Utils.itemBasedProjectileDamage(this.getOwner(), this, source, this.getWeapon(), result.getEntity(), 0.5f);
        boolean res = Utils.runWithInvulTimer(this.getOwner(), result.getEntity(),
                e -> e.hurt(source, damage * this.damageMultiplier), 0);
        if (res && this.level() instanceof ServerLevel serverLevel) {
            EnchantmentHelper.doPostAttackEffects(serverLevel, result.getEntity(), source);
        }
        return res;
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
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
        compound.putInt("ItemType", this.itemType.ordinal());
        compound.putBoolean("Returning", this.returning);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setItemType(ItemType.values()[compound.getInt("ItemType")]);
        this.setWeapon(ItemStack.CODEC.parse(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), compound.get("Weapon")).getOrThrow());
        this.returning = compound.getBoolean("Returning");
    }

    public enum ItemType {

        KANSHOU(TrailInfo.builder(new ParticlePositionProvider.ParticlePositionData(0))
                .setWidth(0.5f)
                .setWidth2(0.05f)
                .setInterpolation(1)
                .setColor2(1, 1, 1, 0)
                .setInterpolation(1)
                .build()),
        BAKUYA(TrailInfo.builder(new ParticlePositionProvider.ParticlePositionData(0))
                .setWidth(0.5f)
                .setWidth2(0.05f)
                .setInterpolation(1)
                .setColor(0, 0, 0, 0.5f)
                .setColor2(0, 0, 0, 0)
                .setInterpolation(1)
                .build());

        public final TrailInfo info;

        ItemType(TrailInfo info) {
            this.info = info;
        }
    }
}
