package com.flemmli97.fate.common.entity.servant;

import com.flemmli97.fate.common.entity.EntityExcalibur;
import com.flemmli97.fate.common.entity.servant.ai.ArthurAttackGoal;
import com.flemmli97.fate.common.registry.ModItems;
import com.flemmli97.fate.common.utils.EnumServantType;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityArthur extends EntityServant {

    public final ArthurAttackGoal attackAI = new ArthurAttackGoal(this);

    public static final AnimatedAction swing_1 = new AnimatedAction(18, 15, "swing_1");

    public static final AnimatedAction npAttack = new AnimatedAction(21, 17, "excalibur");
    public static final AnimatedAction[] anims = new AnimatedAction[]{swing_1, npAttack};

    public EntityArthur(EntityType<? extends EntityArthur> entityType, World world) {
        super(entityType, world, "arthur.hogou");
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.invisexcalibur));
    }

    @Override
    public int attackCooldown(AnimatedAction anim) {
        return 0;
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        if (type == AttackType.NP)
            return anim.getID().equals(npAttack.getID());
        return anim.getID().equals(swing_1.getID());
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public void updateAI(int behaviour) {
        super.updateAI(behaviour);
        if (this.commandBehaviour == 3)
            this.goalSelector.addGoal(1, this.attackAI);
        else if (this.commandBehaviour == 4)
            this.goalSelector.removeGoal(this.attackAI);
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        super.damageEntity(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDead() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
            this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.excalibur));
        }
    }

    @Override
    public void livingTick() {
        if (this.getHealth() < 0.25 * this.getMaxHealth() && this.getHealth() > 0) {
            if (this.critHealth == false) {
                if (!this.world.isRemote)
                    this.world.getServer().getPlayerList().broadcastChatMessage(new TranslationTextComponent("chat.servant.avalon").formatted(TextFormatting.GOLD), ChatType.SYSTEM, Util.NIL_UUID);
                this.critHealth = true;
            }
            if (!this.isPotionActive(Effects.REGENERATION))
                this.addPotionEffect(new EffectInstance(Effects.REGENERATION, 50, 1, false, false));
        }
        super.livingTick();
    }

    public void attackWithNP(double[] pos) {
        EntityExcalibur excalibur = new EntityExcalibur(this.world, this);
        if (pos != null)
            excalibur.setRotationTo(pos[0], pos[1], pos[2], 0);
        this.world.addEntity(excalibur);
        this.revealServant();
    }
}
