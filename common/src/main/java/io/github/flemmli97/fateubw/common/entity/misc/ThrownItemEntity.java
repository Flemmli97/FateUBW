package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.common.entity.utils.EntityTrailHandler;
import io.github.flemmli97.fateubw.common.particles.trail.TrailInfo;
import io.github.flemmli97.fateubw.common.particles.trail.TrailPositions;
import io.github.flemmli97.fateubw.common.particles.trail.provider.ParticlePositionProvider;
import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.utils.ItemUtils;
import io.github.flemmli97.tenshilib.common.utils.math.MathUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class ThrownItemEntity extends BaseProjectile {

    protected static final EntityDataAccessor<ItemStack> WEAPON_TYPE = SynchedEntityData.defineId(ThrownItemEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Integer> TYPE_DATA = SynchedEntityData.defineId(ThrownItemEntity.class, EntityDataSerializers.INT);

    private final EntityTrailHandler trailHandler = new EntityTrailHandler(this, 12);

    private ItemType itemType = ItemType.NONE;

    public ThrownItemEntity(EntityType<? extends ThrownItemEntity> type, Level level) {
        super(type, level);
    }

    public ThrownItemEntity(Level level, LivingEntity shootingEntity) {
        super(FateEntities.THROWN_ITEM.get(), level, shootingEntity);
    }

    @Override
    public int livingTickMax() {
        return 20;
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

    public ItemType getItemType() {
        return this.itemType;
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
        DamageSource source = FateDamageTypes.indirect(FateDamageTypes.THROWN_ITEM, this, this.getOwner());
        float damage = Utils.randomizeDamage(this.getRandom(), (float) ItemUtils.damage(this.level(), null, result.getEntity(), source, this.getWeapon()));
        boolean res = Utils.runWithInvulTimer(this.getOwner(), result.getEntity(),
                e -> e.hurt(source, damage * this.damageMultiplier), 2);
        if (res && this.level() instanceof ServerLevel serverLevel) {
            EnchantmentHelper.doPostAttackEffects(serverLevel, result.getEntity(), source);
        }
        this.discard();
        return res;
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
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("Weapon", ItemStack.CODEC.encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.getWeapon()).getOrThrow());
        compound.putInt("ItemType", this.itemType.ordinal());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setWeapon(ItemStack.CODEC.parse(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), compound.get("Weapon")).getOrThrow());
        this.setItemType(ItemType.values()[compound.getInt("ItemType")]);
    }

    public TrailPositions trailPositions() {
        return this.trailHandler.getPositions();
    }

    public enum ItemType {

        NONE(null),
        KANSHOU(TrailInfo.builder(new ParticlePositionProvider.ParticlePositionData(0))
                .setWidth(0.5f)
                .setWidth2(0.05f)
                .setInterpolation(1)
                .setColor2(1, 1, 1, 0)
                .build()),
        BAKUYA(TrailInfo.builder(new ParticlePositionProvider.ParticlePositionData(0))
                .setWidth(0.5f)
                .setWidth2(0.05f)
                .setInterpolation(1)
                .setColor(0, 0, 0, 0.5f)
                .setColor2(0, 0, 0, 0)
                .build());

        public final TrailInfo info;

        ItemType(TrailInfo info) {
            this.info = info;
        }
    }
}
