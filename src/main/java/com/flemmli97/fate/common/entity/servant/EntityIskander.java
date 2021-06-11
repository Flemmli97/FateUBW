package com.flemmli97.fate.common.entity.servant;

import com.flemmli97.fate.common.entity.EntityGordiusWheel;
import com.flemmli97.fate.common.entity.servant.ai.IskanderAttackGoal;
import com.flemmli97.fate.common.registry.ModEntities;
import com.flemmli97.fate.common.registry.ModItems;
import com.flemmli97.fate.common.utils.EnumServantUpdate;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityIskander extends EntityServant {

    public final IskanderAttackGoal attackAI = new IskanderAttackGoal(this);

    private static final AnimatedAction npAttack = new AnimatedAction(20, 0, "np");
    private static final AnimatedAction[] anims = new AnimatedAction[]{AnimatedAction.vanillaAttack, npAttack};

    public EntityIskander(EntityType<? extends EntityIskander> entityType, World world) {
        super(entityType, world, "iskander.hogou");
        this.canUseNP = true;
        if (world != null && !world.isRemote)
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.kupriots.get()));
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        if (type == AttackType.NP)
            return anim.getID().equals(npAttack.getID());
        return anim.getID().equals("vanilla");
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
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
        if (damageSource == DamageSource.OUT_OF_WORLD) {
            return super.attackEntityFrom(damageSource, damage);
        } else if (this.isPassenger()) {
            this.getRidingEntity().attackEntityFrom(damageSource, damage);
        }
        return super.attackEntityFrom(damageSource, damage);
    }

    public boolean attackWithNP() {

        if (this.isPassenger() || this.world.isRemote)
            return false;
        EntityGordiusWheel wheel = ModEntities.gordiusWheel.get().create(this.world);
        wheel.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
        this.world.addEntity(wheel);
        this.startRiding(wheel);
        //spawn lightning
        LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(this.world);
        lightningboltentity.moveForced(this.getPosX(), this.getPosY(), this.getPosZ());
        lightningboltentity.setEffectOnly(true);
        this.world.addEntity(lightningboltentity);
        this.revealServant();
        return true;
    }

    @Override
    public boolean attacksFromMount() {
        return false;
    }
}
