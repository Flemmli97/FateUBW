package io.github.flemmli97.fateubw.common.entity.misc;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ItemInGroundEntity extends Entity {

    protected static final EntityDataAccessor<ItemStack> WEAPON_TYPE = SynchedEntityData.defineId(ItemInGroundEntity.class, EntityDataSerializers.ITEM_STACK);

    public ItemInGroundEntity(EntityType<? extends ItemInGroundEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(WEAPON_TYPE, ItemStack.EMPTY);
    }

    public ItemStack getItem() {
        return this.entityData.get(WEAPON_TYPE);
    }

    public void setItem(ItemStack stack) {
        if (!stack.isEmpty()) {
            this.entityData.set(WEAPON_TYPE, stack);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.moveEntity();
    }

    public void moveEntity() {
        Vec3 motion = this.getDeltaMovement();
        double newX = this.getX() + motion.x;
        double newY = this.getY() + motion.y;
        double newZ = this.getZ() + motion.z;
        boolean water = this.isInWater();
        if (water) {
            for (int i = 0; i < 4; ++i) {
                this.level().addParticle(ParticleTypes.BUBBLE, this.getX() * 0.25D, this.getY() * 0.25D, this.getZ() * 0.25D, motion.x, motion.y, motion.z);
            }
        }
        float friction = water ? 0.8f : 0.99f;
        this.setDeltaMovement(motion.scale(friction));
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().subtract(0, 0.03F, 0));
        }
        this.setPos(newX, newY, newZ);
        Vec3 pos = this.position();
        Vec3 to = pos.add(this.getDeltaMovement());
        BlockHitResult raytraceresult = this.level().clip(new ClipContext(pos, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (raytraceresult.getType() == HitResult.Type.BLOCK) {
            this.setDeltaMovement(0, 0, 0);
        }
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.put("Weapon", ItemStack.CODEC.encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.getItem()).getOrThrow());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.setItem(ItemStack.CODEC.parse(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), compound.get("Weapon")).getOrThrow());
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (this.isAlive() && !player.level().isClientSide()) {
            if (this.getItem().isDamageableItem()) {
                this.getItem().set(DataComponents.MAX_DAMAGE, 1);
            }
            ItemEntity itementity = player.drop(this.getItem(), false);
            if (itementity != null) {
                itementity.setNoPickUpDelay();
                itementity.setTarget(player.getUUID());
            }
            this.discard();
        }
        return InteractionResult.sidedSuccess(true);
    }
}
