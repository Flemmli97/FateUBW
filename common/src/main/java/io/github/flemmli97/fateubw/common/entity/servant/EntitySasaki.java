package io.github.flemmli97.fateubw.common.entity.servant;


import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntitySasaki extends BaseServant {

    private static final AnimatedAction NP_ATTACK = AnimatedAction.builder(40, "np").build();
    private static final AnimatedAction[] ANIMS = {NP_ATTACK};


    private final AnimationHandler<EntitySasaki> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntitySasaki(EntityType<? extends BaseServant> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.MONOHOSHI_ZAO.get()));
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        if (type == AttackType.NP)
            return anim.getID().equals(NP_ATTACK.getID());
        return false;
    }

    @Override
    public AnimationHandler<EntitySasaki> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void updateAI(EnumServantUpdate behaviour) {
        super.updateAI(behaviour);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDeadOrDying() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        AnimatedAction anim = this.getAnimationHandler().getAnimation();
        if (anim != null && this.canUse(anim, AttackType.NP))
            return false;
        return super.hurt(damageSource, damage);
    }

    public void attackWithNP(LivingEntity living) {
        this.revealServant();
    }

    public boolean canAttackNP() {
        AnimatedAction anim = this.getAnimationHandler().getAnimation();
        if (anim == null || !this.canUse(anim, AttackType.NP))
            return false;
        int i = 0;
        return i == 30 || i == 20 || i == 10;
    }
}
