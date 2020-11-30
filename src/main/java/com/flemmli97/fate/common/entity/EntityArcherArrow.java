package com.flemmli97.fate.common.entity;

import com.flemmli97.fate.common.registry.ModEntities;
import com.flemmli97.fate.common.utils.CustomDamageSource;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityArcherArrow extends AbstractArrowEntity implements IEntityAdditionalSpawnData {

    private int knockbackStrength;

    public EntityArcherArrow(EntityType<? extends EntityArcherArrow> type, World world) {
        super(type, world);
    }

    public EntityArcherArrow(World world, LivingEntity shootingEntity) {
        super(ModEntities.archerArrow.get(), shootingEntity, world);
    }

    @Override
    protected ItemStack getArrowStack() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult p_213868_1_) {
        Entity entity = p_213868_1_.getEntity();
        float f = (float) this.getMotion().length();
        int i = MathHelper.ceil(MathHelper.clamp((double) f * this.getDamage() + 10, 0.0D, 2.147483647E9D));

        if (this.getIsCritical()) {
            long j = this.rand.nextInt(i / 2 + 2);
            i = (int) Math.min(j + (long) i, 2147483647L);
        }

        Entity owner = this.getOwner();
        if (owner instanceof LivingEntity) {
            ((LivingEntity) owner).setLastAttackedEntity(entity);
        }

        boolean flag = entity.getType() == EntityType.ENDERMAN;
        int k = entity.getFireTimer();
        if (this.isBurning() && !flag) {
            entity.setFire(5);
        }

        if (entity.attackEntityFrom(CustomDamageSource.archerNormal(this, this.getOwner()), (float) i)) {
            if (flag) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity) entity;
                if (!this.world.isRemote && this.getPierceLevel() <= 0) {
                    livingentity.setArrowCountInEntity(livingentity.getArrowCountInEntity() + 1);
                }

                if (this.knockbackStrength > 0) {
                    Vector3d vector3d = this.getMotion().mul(1.0D, 0.0D, 1.0D).normalize().scale((double) this.knockbackStrength * 0.6D);
                    if (vector3d.lengthSquared() > 0.0D) {
                        livingentity.addVelocity(vector3d.x, 0.1D, vector3d.z);
                    }
                }

                if (!this.world.isRemote && owner instanceof LivingEntity) {
                    EnchantmentHelper.applyThornEnchantments(livingentity, owner);
                    EnchantmentHelper.applyArthropodEnchantments((LivingEntity) owner, livingentity);
                }

                this.arrowHit(livingentity);
                if (livingentity != owner && livingentity instanceof PlayerEntity && owner instanceof ServerPlayerEntity && !this.isSilent()) {
                    ((ServerPlayerEntity) owner).connection.sendPacket(new SChangeGameStatePacket(SChangeGameStatePacket.PROJECTILE_HIT_PLAYER, 0.0F));
                }
            }

            this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
            this.remove();
        } else {
            entity.setFireTicks(k);
            this.setMotion(this.getMotion().scale(-0.1D));
            this.rotationYaw += 180.0F;
            this.prevRotationYaw += 180.0F;
            if (!this.world.isRemote && this.getMotion().lengthSquared() < 1.0E-7D)
                this.remove();
        }
    }

    @Override
    public void setKnockbackStrength(int knockbackStrengthIn) {
        this.knockbackStrength = knockbackStrengthIn;
    }

    @Override
    public void writeSpawnData(PacketBuffer buf) {
        buf.writeBoolean(this.getOwner() != null);
        if (this.getOwner() != null) {
            buf.writeInt(this.getOwner().getEntityId());
        }
    }

    @Override
    public void readSpawnData(PacketBuffer buf) {
        if (buf.readBoolean()) {
            Entity shooter = this.world.getEntityByID(buf.readInt());
            this.setShooter(shooter);
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}