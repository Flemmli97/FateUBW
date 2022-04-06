package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.common.entity.SwitchableWeapon;
import io.github.flemmli97.fateubw.common.entity.misc.Excalibur;
import io.github.flemmli97.fateubw.common.entity.servant.ai.ArthurAttackGoal;
import io.github.flemmli97.fateubw.common.lib.LibEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntityArthur extends BaseServant {

    public final ArthurAttackGoal attackAI = new ArthurAttackGoal(this);

    public static final AnimatedAction swing_1 = new AnimatedAction(18, 15, "swing_1");

    public static final AnimatedAction npAttack = new AnimatedAction(15, 8, "excalibur");
    public static final AnimatedAction[] anims = {swing_1, npAttack};

    private final AnimationHandler<EntityArthur> animationHandler = new AnimationHandler<>(this, anims);

    public final SwitchableWeapon<EntityArthur> switchableWeapon = new SwitchableWeapon<>(this, new ItemStack(ModItems.excalibur.get()), ItemStack.EMPTY);

    public EntityArthur(EntityType<? extends EntityArthur> entityType, Level world) {
        super(entityType, world, LibEntities.arthur + ".hogou");
        if (world != null && !world.isClientSide)
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.invisexcalibur.get()));
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        if (type == AttackType.NP)
            return anim.getID().equals(npAttack.getID());
        return anim.getID().equals(swing_1.getID());
    }

    @Override
    public AnimationHandler<EntityArthur> getAnimationHandler() {
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
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.excalibur.get()));
        }
    }

    @Override
    public void aiStep() {
        if (this.getHealth() < 0.25 * this.getMaxHealth() && this.getHealth() > 0) {
            if (!this.critHealth) {
                if (!this.level.isClientSide)
                    this.level.getServer().getPlayerList().broadcastMessage(new TranslatableComponent("chat.servant.avalon").withStyle(ChatFormatting.GOLD), ChatType.SYSTEM, Util.NIL_UUID);
                this.critHealth = true;
            }
            if (!this.hasEffect(MobEffects.REGENERATION))
                this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 50, 1, false, false));
        }
        super.aiStep();
    }

    public void attackWithNP(double[] pos) {
        Excalibur excalibur = new Excalibur(this.level, this);
        if (pos != null)
            excalibur.setRotationTo(pos[0], pos[1], pos[2], 0);
        this.level.addFreshEntity(excalibur);
        this.revealServant();
        this.releaseUsingItem();
        this.switchableWeapon.switchItems(true);
    }
}
