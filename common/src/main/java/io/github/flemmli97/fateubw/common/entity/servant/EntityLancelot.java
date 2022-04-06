package io.github.flemmli97.fateubw.common.entity.servant;


import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.entity.servant.ai.LancelotAttackGoal;
import io.github.flemmli97.fateubw.common.lib.LibEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.fateubw.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.utils.ItemUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityLancelot extends BaseServant {

    public final LancelotAttackGoal attackAI = new LancelotAttackGoal(this);

    public static final AnimatedAction[] anims = AnimatedAction.vanillaAttackOnly;

    private final AnimationHandler<EntityLancelot> animationHandler = new AnimationHandler<>(this, anims);

    public EntityLancelot(EntityType<? extends EntityLancelot> entityType, Level world) {
        super(entityType, world, LibEntities.lancelot + ".hogou");
        if (world != null && !world.isClientSide)
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        return true;
    }

    @Override
    public AnimationHandler<EntityLancelot> getAnimationHandler() {
        return this.animationHandler;
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
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDeadOrDying() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.arondight.get()));
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (this.isPassenger())
            return this.getVehicle().hurt(damageSource, damage);

        if (damageSource == DamageSource.OUT_OF_WORLD) {
            return this.preAttackEntityFrom(damageSource, damage);
        } else {
            if (!(damageSource.getEntity() instanceof BaseServant))
                damage *= 0.5;

            if (damageSource.isProjectile() && !damageSource.isBypassArmor() && this.projectileBlockChance(damageSource, damage)) {
                if (!this.level.isClientSide && this.getRandom().nextFloat() < Config.Common.lancelotReflectChance && !(damageSource.getDirectEntity() instanceof LivingEntity)) {
                    this.reflectProjectile(damageSource.getDirectEntity());
                    this.level.playSound(null, this.blockPosition(), SoundEvents.ANVIL_PLACE, SoundSource.NEUTRAL, 1, 1);
                } else
                    this.level.playSound(null, this.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.NEUTRAL, 1, 1);
                damageSource.getDirectEntity().remove(RemovalReason.KILLED);
                return true;
            }
            return this.preAttackEntityFrom(damageSource, Math.min(50, damage));
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level.isClientSide)
            for (int x = 0; x < 2; x++) {
                this.level.addParticle(
                        ParticleTypes.LARGE_SMOKE,
                        this.getX() + (this.random.nextDouble() - 0.5D) * this.getBbWidth(),
                        this.getY() + this.random.nextDouble() * this.getBbHeight(),
                        this.getZ() + (this.random.nextDouble() - 0.5D) * this.getBbWidth(),
                        this.random.nextGaussian() * 0.02D,
                        this.random.nextGaussian() * 0.02D,
                        this.random.nextGaussian() * 0.02D);
            }
        if (!this.level.isClientSide && this.canPickUpLoot() && this.isAlive() && !this.dead && Platform.INSTANCE.mobGriefing(this)) {
            for (ItemEntity itementity : this.level.getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(1.0D, 0.0D, 1.0D))) {
                if (this.canPickWeapon() && itementity.isAlive() && !itementity.getItem().isEmpty() && !itementity.hasPickUpDelay() && this.wantsToPickUp(itementity.getItem()) && this.checkItemToWield(itementity.getItem())
                        && ItemUtils.isItemBetter(this.getMainHandItem(), itementity.getItem())) {
                    this.pickUpItem(itementity);
                    this.revealServant();
                }
            }
        }
    }

    private void reflectProjectile(Entity oldProjectile) {
        CompoundTag old = new CompoundTag();
        oldProjectile.saveWithoutId(old);
        old.remove("UUID");
        //Vanilla-fix incompability
        old.remove("VFAABB");
        if (old.contains("Owner"))
            old.putUUID("Owner", this.getUUID());
        Entity e = oldProjectile.getType().create(this.level);
        if (e instanceof Projectile) {
            e.load(old);
            if (this.getTarget() != null) {
                LivingEntity target = this.getTarget();
                Vec3 dir = new Vec3(target.getX() - e.getX(), (target.getY() + target.getEyeHeight()) - e.getY(), target.getZ() - e.getZ());
                this.shootProj(e, dir.x, dir.y, dir.z, 1, 1);
            } else {
                this.shootProj(e, -e.getDeltaMovement().x, -e.getDeltaMovement().y, -e.getDeltaMovement().z, 1, 1);
            }
            this.level.addFreshEntity(e);
        }
    }

    private void shootProj(Entity e, double dirX, double dirY, double dirZ, float vel, float acc) {
        Vec3 vector3d = new Vec3(dirX, dirY, dirZ).normalize().add(this.random.nextGaussian() * 0.0075F * acc, this.random.nextGaussian() * 0.0075F * acc, this.random.nextGaussian() * 0.0075F * acc).scale(vel);
        e.setDeltaMovement(vector3d);
        float f = Mth.sqrt((float) (vector3d.x * vector3d.x + vector3d.z * vector3d.z));
        e.setYRot((float) (Mth.atan2(vector3d.x, vector3d.z) * 180F / Math.PI));
        e.setXRot((float) (Mth.atan2(vector3d.y, f) * 180F / Math.PI));
        e.yRotO = e.getYRot();
        e.xRotO = e.getXRot();
    }

    public boolean canPickWeapon() {
        return this.getMainHandItem().getItem() != ModItems.arondight.get();
    }

    public boolean checkItemToWield(ItemStack stack) {
        return stack.getItem() instanceof TieredItem;
    }
}