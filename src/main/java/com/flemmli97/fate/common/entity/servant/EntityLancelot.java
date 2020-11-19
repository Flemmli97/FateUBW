package com.flemmli97.fate.common.entity.servant;

import com.flemmli97.fate.common.config.Config;
import com.flemmli97.fate.common.entity.servant.ai.LancelotAttackGoal;
import com.flemmli97.fate.common.registry.ModItems;
import com.flemmli97.fate.common.utils.EnumServantUpdate;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.flemmli97.tenshilib.common.utils.ItemUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class EntityLancelot extends EntityServant {

    public final LancelotAttackGoal attackAI = new LancelotAttackGoal(this);

    public static final AnimatedAction[] anims = AnimatedAction.vanillaAttackOnly;

    public EntityLancelot(EntityType<? extends EntityLancelot> entityType, World world) {
        super(entityType, world, "lancelot.hogou");
        if (world != null && !world.isRemote)
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        return true;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public void updateAI(EnumServantUpdate behaviour) {
        super.updateAI(behaviour);
        if (this.commandBehaviour == EnumServantUpdate.STAY)
            this.goalSelector.removeGoal(this.attackAI);
        else
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        super.damageEntity(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDead() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
            this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.arondight.get()));
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
        if (this.isPassenger())
            return this.getRidingEntity().attackEntityFrom(damageSource, damage);

        if (damageSource == DamageSource.OUT_OF_WORLD) {
            return this.preAttackEntityFrom(damageSource, damage);
        } else {
            if (!(damageSource.getTrueSource() instanceof EntityServant))
                damage *= 0.5;

            if (damageSource.isProjectile() && !damageSource.isUnblockable() && this.projectileBlockChance(damageSource, damage)) {
                if (!this.world.isRemote && this.getRNG().nextFloat() < Config.Common.lancelotReflectChance && !(damageSource.getImmediateSource() instanceof LivingEntity)) {
                    this.reflectProjectile(damageSource.getImmediateSource());
                    this.world.playSound(null, this.getBlockPos(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.NEUTRAL, 1, 1);
                } else
                    this.world.playSound(null, this.getBlockPos(), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.NEUTRAL, 1, 1);
                damageSource.getImmediateSource().remove();
                return true;
            }
            return this.preAttackEntityFrom(damageSource, (float) Math.min(50, damage));
        }
    }

    @Override
    public void livingTick() {
        super.livingTick();
        if (this.world.isRemote)
            for (int x = 0; x < 2; x++) {
                this.world.addParticle(
                        ParticleTypes.LARGE_SMOKE,
                        this.getX() + (this.rand.nextDouble() - 0.5D) * this.getWidth(),
                        this.getY() + this.rand.nextDouble() * this.getHeight(),
                        this.getZ() + (this.rand.nextDouble() - 0.5D) * this.getWidth(),
                        this.rand.nextGaussian() * 0.02D,
                        this.rand.nextGaussian() * 0.02D,
                        this.rand.nextGaussian() * 0.02D);
            }
        if (!this.world.isRemote && this.canPickUpLoot() && this.isAlive() && !this.dead && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
            for (ItemEntity itementity : this.world.getEntitiesWithinAABB(ItemEntity.class, this.getBoundingBox().grow(1.0D, 0.0D, 1.0D))) {
                if (this.canPickWeapon() && itementity.isAlive() && !itementity.getItem().isEmpty() && !itementity.cannotPickup() && this.canGather(itementity.getItem()) && this.checkItemToWield(itementity.getItem())
                        && ItemUtils.isItemBetter(this.getHeldItemMainhand(), itementity.getItem())) {
                    this.updateEquipmentIfNeeded(itementity);
                    this.revealServant();
                }
            }
        }
    }

    private void reflectProjectile(Entity oldProjectile) {
        CompoundNBT old = new CompoundNBT();
        oldProjectile.writeWithoutTypeId(old);
        old.remove("UUID");
        //Vanilla-fix incompability
        old.remove("VFAABB");
        if (old.contains("Owner"))
            old.putUniqueId("Owner", this.getUniqueID());
        Entity e = oldProjectile.getType().create(this.world);
        if (e instanceof ProjectileEntity) {
            e.read(old);
            if (this.getAttackTarget() != null) {
                LivingEntity target = this.getAttackTarget();
                Vector3d dir = new Vector3d(target.getX() - e.getX(), (target.getY() + target.getEyeHeight()) - e.getY(), target.getZ() - e.getZ());
                this.shootProj(e, dir.x, dir.y, dir.z, 1, 1);
            } else {
                this.shootProj(e, -e.getMotion().x, -e.getMotion().y, -e.getMotion().z, 1, 1);
            }
            this.world.addEntity(e);
        }
    }

    private void shootProj(Entity e, double dirX, double dirY, double dirZ, float vel, float acc) {
        Vector3d vector3d = new Vector3d(dirX, dirY, dirZ).normalize().add(this.rand.nextGaussian() * 0.0075F * acc, this.rand.nextGaussian() * 0.0075F * acc, this.rand.nextGaussian() * 0.0075F * acc).scale(vel);
        e.setMotion(vector3d);
        float f = MathHelper.sqrt(horizontalMag(vector3d));
        e.rotationYaw = (float) (MathHelper.atan2(vector3d.x, vector3d.z) * 180F / Math.PI);
        e.rotationPitch = (float) (MathHelper.atan2(vector3d.y, f) * 180F / Math.PI);
        e.prevRotationYaw = e.rotationYaw;
        e.prevRotationPitch = e.rotationPitch;
    }

    public boolean canPickWeapon() {
        return this.getHeldItemMainhand().getItem() != ModItems.arondight.get();
    }

    public boolean checkItemToWield(ItemStack stack) {
        return stack.getItem() instanceof TieredItem;
    }
}