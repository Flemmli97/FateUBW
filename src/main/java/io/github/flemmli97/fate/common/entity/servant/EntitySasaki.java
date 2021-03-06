package io.github.flemmli97.fate.common.entity.servant;


import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.fate.common.entity.servant.ai.SasakiAttackGoal;
import io.github.flemmli97.fate.common.registry.ModItems;
import io.github.flemmli97.fate.common.utils.EnumServantUpdate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntitySasaki extends EntityServant {

    public final SasakiAttackGoal attackAI = new SasakiAttackGoal(this);

    private static final AnimatedAction npAttack = new AnimatedAction(40, 0, "np");
    private static final AnimatedAction[] anims = {AnimatedAction.vanillaAttack, npAttack};

    private final AnimationHandler<EntitySasaki> animationHandler = new AnimationHandler<>(this, anims);

    public EntitySasaki(EntityType<? extends EntityServant> entityType, World world) {
        super(entityType, world, "sasaki.hogou");
        if (world != null && !world.isRemote)
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.katana.get()));
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        if (type == AttackType.NP)
            return anim.getID().equals("np");
        return anim.getID().equals("vanilla");
    }

    @Override
    public AnimationHandler<EntitySasaki> getAnimationHandler() {
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
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        super.damageEntity(damageSrc, damageAmount);
        if (!this.canUseNP && !this.getShouldBeDead() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
        if (this.getAnimationHandler().getAnimation().map(anim -> this.canUse(anim, AttackType.NP)).orElse(false))
            return false;
        return super.attackEntityFrom(damageSource, damage);
    }

    public void attackWithNP(LivingEntity living) {
        this.revealServant();
    }

    public boolean canAttackNP() {
        if (this.getAnimationHandler().getAnimation().map(anim -> !this.canUse(anim, AttackType.NP)).orElse(true))
            return false;
        int i = this.getAnimationHandler().getAnimation().map(AnimatedAction::getTick).orElse(0);
        return i == 30 || i == 20 || i == 10;
    }
}
