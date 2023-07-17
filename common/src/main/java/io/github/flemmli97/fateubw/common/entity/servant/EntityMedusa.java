package io.github.flemmli97.fateubw.common.entity.servant;


import io.github.flemmli97.fateubw.common.entity.DaggerHitNotifiable;
import io.github.flemmli97.fateubw.common.entity.minions.Pegasus;
import io.github.flemmli97.fateubw.common.entity.misc.ChainDagger;
import io.github.flemmli97.fateubw.common.entity.servant.ai.MedusaAttackGoal;
import io.github.flemmli97.fateubw.common.lib.LibEntities;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class EntityMedusa extends BaseServant implements DaggerHitNotifiable {

    public final MedusaAttackGoal attackAI = new MedusaAttackGoal(this);

    public static final AnimatedAction daggerAttack = new AnimatedAction(20, 7, "dagger");
    public static final AnimatedAction daggerRetract = new AnimatedAction(20, 7, "dagger_retract");
    private static final AnimatedAction npAttack = new AnimatedAction(20, 5, "np");
    private static final AnimatedAction[] anims = {AnimatedAction.vanillaAttack, daggerAttack, daggerRetract, npAttack};

    private final AnimationHandler<EntityMedusa> animationHandler = new AnimationHandler<>(this, anims);

    protected static final EntityDataAccessor<Boolean> thrownDagger = SynchedEntityData.defineId(EntityMedusa.class, EntityDataSerializers.BOOLEAN);

    private ChainDagger dagger;
    private int throwCooldown;

    public EntityMedusa(EntityType<? extends BaseServant> entityType, Level world) {
        super(entityType, world, LibEntities.medusa + ".hogou");
        if (world != null && !world.isClientSide)
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(thrownDagger, false);
    }

    public boolean daggerThrown() {
        return this.getEntityData().get(thrownDagger);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.medusaDagger.get()));
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        if (type == AttackType.NP)
            return anim.getID().equals(npAttack.getID());
        if (type == AttackType.RANGED)
            return this.canThrow() && anim.getID().equals(daggerAttack.getID());
        return anim.getID().equals(AnimatedAction.vanillaAttack.getID());
    }

    public boolean canThrow() {
        return this.dagger == null && this.throwCooldown <= 0;
    }

    @Override
    public AnimationHandler<EntityMedusa> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide && this.dagger != null) {
            if (!this.dagger.isAlive()) {
                this.dagger = null;
                this.getEntityData().set(thrownDagger, false);
            }
            this.getAnimationHandler().runIfNotNull(anim -> {
                if (anim.getID().equals(daggerRetract.getID()) && anim.canAttack() && this.dagger != null) {
                    this.dagger.retractHook();
                    this.dagger = null;
                    this.getEntityData().set(thrownDagger, false);
                }
            });
        } else
            this.throwCooldown--;
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
        }
    }

    @Override
    public int attackCooldown(AnimatedAction anim) {
        if (daggerAttack.is(anim))
            return this.random.nextInt(15) + 5;
        return super.attackCooldown(anim);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (this.getVehicle() != null && this.getVehicle() instanceof LivingEntity) {
            LivingEntity mount = (LivingEntity) this.getVehicle();
            return mount.hurt(damageSource, damage);
        }
        return super.hurt(damageSource, damage);
    }

    public void throwDaggerAt(LivingEntity target) {
        if (!this.level.isClientSide) {
            ChainDagger dagger = new ChainDagger(this.level, this, true);
            dagger.shootAtEntity(target, 3, 0, 0);
            this.level.addFreshEntity(dagger);
            this.dagger = dagger;
            this.throwCooldown = this.random.nextInt(32) + 45;
            this.getEntityData().set(thrownDagger, true);
        }
    }

    public void attackWithNP() {
        if (!this.level.isClientSide) {
            Pegasus peg = ModEntities.pegasus.get().create(this.level);
            peg.setPos(this.getX(), this.getY(), this.getZ());
            for (int x = 0; x < 5; x++) {
                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(this.level);
                lightning.moveTo(this.getX(), this.getY(), this.getZ());
                lightning.setVisualOnly(true);
            }
            List<Entity> list = this.level.getEntities(this, this.getBoundingBox().expandTowards(5, 3.0D, 5));
            for (int x = 0; x < list.size(); x++) {
                if (list.get(x) instanceof LivingEntity) {
                    LivingEntity ent = (LivingEntity) list.get(x);
                    ent.knockback(2, Mth.sin(this.getYRot() * 0.017453292F), (-Mth.cos(this.getYRot() * 0.017453292F)));
                }
            }
            this.level.addFreshEntity(peg);
            this.startRiding(peg);
            this.revealServant();
        }
    }

    @Override
    public void onDaggerHit(ChainDagger dagger) {
        this.getAnimationHandler().setAnimation(daggerRetract);
    }
}
