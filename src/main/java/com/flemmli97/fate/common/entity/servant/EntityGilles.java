package com.flemmli97.fate.common.entity.servant;

import com.flemmli97.fate.common.config.Config;
import com.flemmli97.fate.common.entity.EntityLesserMonster;
import com.flemmli97.fate.common.entity.servant.ai.GilledAttackGoal;
import com.flemmli97.fate.common.registry.ModItems;
import com.flemmli97.fate.common.utils.EnumServantUpdate;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityGilles extends EntityServant {

    public final GilledAttackGoal attackAI = new GilledAttackGoal(this);

    private static final AnimatedAction rangedAttack = new AnimatedAction(38, 25, "ranged");
    private static final AnimatedAction npAttack = new AnimatedAction(20, 0, "np");
    private static final AnimatedAction[] anims = new AnimatedAction[]{rangedAttack, npAttack};

    public EntityGilles(EntityType<? extends EntityGilles> entityType, World world) {
        super(entityType, world, "Prelati's Spellbook");
        if (world != null && !world.isRemote)
            this.goalSelector.addGoal(1, this.attackAI);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.grimoire));
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        if (type == AttackType.NP)
            return anim.getID().equals("np");
        return anim.getID().equals("ranged");
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public int attackCooldown(AnimatedAction anim) {
        return 60;
    }

    @Override
    public void updateAI(EnumServantUpdate behaviour) {
        super.updateAI(behaviour);
        if (this.commandBehaviour == EnumServantUpdate.STAY)
            this.goalSelector.removeGoal(this.attackAI);
        else
            this.goalSelector.addGoal(1, this.attackAI);
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        super.damageEntity(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDead() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
        }
    }

    public void attackWithNP() {
        if (!this.world.isRemote) {
            //EntityMonster minion = new EntityMonster(this.world, this);
            //this.world.spawnEntity(minion);
            //minion.setAttackTarget(this.getAttackTarget());
        }
    }

    public void attackWithRangedAttack(LivingEntity target) {
        if (!this.world.isRemote) {
            if (this.world.getEntitiesWithinAABB(EntityLesserMonster.class, this.getBoundingBox().grow(16)).size() < Config.Common.gillesMinionAmount) {
                EntityLesserMonster minion = new EntityLesserMonster(this.world, this);
                this.world.addEntity(minion);
                minion.setAttackTarget(this.getAttackTarget());
                this.revealServant();
            }
        }
    }
}
