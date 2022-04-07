package io.github.flemmli97.fateubw.common.entity.servant;


import io.github.flemmli97.fateubw.common.entity.servant.ai.DiarmuidAttackGoal;
import io.github.flemmli97.fateubw.common.lib.LibEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntityDiarmuid extends BaseServant {

    public final DiarmuidAttackGoal attackAI = new DiarmuidAttackGoal(this);

    private static final AnimatedAction npAttack = new AnimatedAction(20, 0, "np");
    private static final AnimatedAction[] anims = {AnimatedAction.vanillaAttack, npAttack};

    private final AnimationHandler<EntityDiarmuid> animationHandler = new AnimationHandler<>(this, anims);

    public EntityDiarmuid(EntityType<? extends EntityDiarmuid> entityType, Level world) {
        super(entityType, world, LibEntities.diarmuid + ".hogou");
        if (world != null && !world.isClientSide)
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.gaedearg.get()));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ModItems.gaebuidhe.get()));
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        if (type == AttackType.NP)
            return anim.getID().equals(npAttack.getID());
        return anim.getID().equals(AnimatedAction.vanillaAttack.getID());
    }

    @Override
    public AnimationHandler<EntityDiarmuid> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
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
    public void tick() {
        super.tick();
        if (this.getHealth() < 0.25 * this.getMaxHealth() && this.getHealth() > 0) {
            if (!this.critHealth) {
                this.critHealth = true;
            }
        }
        this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1, 1, false, false));
    }

    @Override
    public float damageModifier(Entity target) {
        if (target instanceof LivingEntity) {
            for (MobEffectInstance effect : ((LivingEntity) target).getActiveEffects()) {
                if (!effect.getEffect().isBeneficial())
                    return 2;
            }
        }
        return super.damageModifier(target);
    }

    public void attackWithNP(LivingEntity target) {
        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 7200, 2));
        target.addEffect(new MobEffectInstance(MobEffects.POISON, 800, 1));
        this.doHurtTarget(target);
        this.revealServant();
    }
}
