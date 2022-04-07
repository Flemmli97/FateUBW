package io.github.flemmli97.fateubw.common.entity.servant;


import io.github.flemmli97.fateubw.common.entity.minions.Gordius;
import io.github.flemmli97.fateubw.common.entity.servant.ai.IskanderAttackGoal;
import io.github.flemmli97.fateubw.common.lib.LibEntities;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntityIskander extends BaseServant {

    public final IskanderAttackGoal attackAI = new IskanderAttackGoal(this);

    private static final AnimatedAction npAttack = new AnimatedAction(20, 5, "np");
    private static final AnimatedAction[] anims = {AnimatedAction.vanillaAttack, npAttack};

    private final AnimationHandler<EntityIskander> animationHandler = new AnimationHandler<>(this, anims);

    public EntityIskander(EntityType<? extends EntityIskander> entityType, Level world) {
        super(entityType, world, LibEntities.alexander + ".hogou");
        this.canUseNP = true;
        if (world != null && !world.isClientSide)
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.kupriots.get()));
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        if (type == AttackType.NP)
            return anim.getID().equals(npAttack.getID());
        return anim.getID().equals(AnimatedAction.vanillaAttack.getID());
    }

    @Override
    public AnimationHandler<EntityIskander> getAnimationHandler() {
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
    public boolean hurt(DamageSource damageSource, float damage) {
        if (damageSource == DamageSource.OUT_OF_WORLD) {
            return super.hurt(damageSource, damage);
        } else if (this.isPassenger()) {
            this.getVehicle().hurt(damageSource, damage);
        }
        return super.hurt(damageSource, damage);
    }

    public boolean attackWithNP() {
        if (this.isPassenger() || this.level.isClientSide)
            return false;
        Gordius wheel = ModEntities.gordiusWheel.get().create(this.level);
        wheel.setPos(this.getX(), this.getY(), this.getZ());
        this.level.addFreshEntity(wheel);
        this.boardingCooldown = 0;
        this.startRiding(wheel);
        //spawn lightning
        for (int i = 0; i < 5; i++) {
            LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(this.level);
            lightningboltentity.moveTo(this.getX() + this.random.nextGaussian() * 2, this.getY(), this.getZ() + this.random.nextGaussian() * 2);
            lightningboltentity.setVisualOnly(true);
            this.level.addFreshEntity(lightningboltentity);
        }
        this.revealServant();
        return true;
    }

    @Override
    public boolean attacksFromMount() {
        return false;
    }
}
