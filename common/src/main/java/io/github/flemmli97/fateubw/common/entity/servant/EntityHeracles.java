package io.github.flemmli97.fateubw.common.entity.servant;


import io.github.flemmli97.fateubw.common.entity.servant.ai.HeraclesAttackGoal;
import io.github.flemmli97.fateubw.common.lib.LibEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntityHeracles extends BaseServant {

    public final HeraclesAttackGoal attackAI = new HeraclesAttackGoal(this);

    private static final AnimatedAction swing_1 = new AnimatedAction(20, 18, "swing_1");
    private static final AnimatedAction[] anims = {swing_1};
    protected static final EntityDataAccessor<Integer> deathCount = SynchedEntityData.defineId(EntityHeracles.class, EntityDataSerializers.INT);
    private boolean voidDeath;

    private final AnimationHandler<EntityHeracles> animationHandler = new AnimationHandler<>(this, anims);

    public EntityHeracles(EntityType<? extends BaseServant> entityType, Level world) {
        super(entityType, world, LibEntities.heracles + ".hogou");
        if (world != null && !world.isClientSide)
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.heraclesAxe.get()));
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        return true;
    }

    @Override
    public AnimationHandler<EntityHeracles> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(deathCount, 0);
    }

    @Override
    public void updateAI(EnumServantUpdate behaviour) {
        super.updateAI(behaviour);
        if (this.commandBehaviour == EnumServantUpdate.STAY)
            this.goalSelector.removeGoal(this.attackAI);
        else
            this.goalSelector.addGoal(0, this.attackAI);
    }

    public void setDeathNumber(int death) {
        this.entityData.set(deathCount, death);
    }

    public int getDeaths() {
        return this.entityData.get(deathCount);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (damageSource != DamageSource.OUT_OF_WORLD && damage < 4)
            return false;
        return super.hurt(damageSource, damage);
    }

    @Override
    public void aiStep() {
        if (this.getDeaths() > 4 && this.getDeaths() <= 8) {
            this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1, 1, false, false));

        } else if (this.getDeaths() > 8) {
            this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1, 2, false, false));
        }
        super.aiStep();
    }

    @Override
    protected void tickDeath() {
        if (this.getLastDamageSource() == DamageSource.OUT_OF_WORLD || this.voidDeath) {
            this.voidDeath = true;
            super.tickDeath();
        } else {
            if (this.getDeaths() < 12) {
                this.deathTime++;
                if (this.deathTime == 40 && !this.level.isClientSide) {
                    this.setDeathNumber(this.getDeaths() + 1);
                    double heal = 1 - this.getDeaths() * 0.04 + 0.5;
                    this.setHealth((float) (heal * this.getMaxHealth()));
                    this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 3, false, false));
                    this.deathTime = 0;
                    this.revealServant();
                }
            } else {
                super.tickDeath();
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Deaths", this.getDeaths());
        tag.putBoolean("DeathType", this.voidDeath);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setDeathNumber(tag.getInt("Deaths"));
        this.voidDeath = tag.getBoolean("DeathType");
    }

    @Override
    public int attackCooldown(AnimatedAction anim) {
        return 7;
    }
}
