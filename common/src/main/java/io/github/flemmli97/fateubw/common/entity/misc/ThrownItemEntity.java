package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.tenshilib.common.utils.ItemUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
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

    public ThrownItemEntity(EntityType<? extends ThrownItemEntity> type, Level level) {
        super(type, level);
    }

    public ThrownItemEntity(Level level, LivingEntity shootingEntity) {
        super(FateEntities.THROWN_ITEM.get(), level, shootingEntity);
    }

    @Override
    public int livingTickMax() {
        return this.inGround ? Integer.MAX_VALUE : 250;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(WEAPON_TYPE, ItemStack.EMPTY);
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
        float damage = (float) ItemUtils.damage(this.level(), null, result.getEntity(), source, this.getWeapon());
        boolean res = result.getEntity().hurt(source, damage);
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
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setWeapon(ItemStack.CODEC.parse(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), compound.get("Weapon")).getOrThrow());
    }
}
