package io.github.flemmli97.fateubw.common.entity.servant;

import io.github.flemmli97.fateubw.common.entity.misc.GaeBolg;
import io.github.flemmli97.fateubw.common.entity.servant.ai.CuchulainnAttackGoal;
import io.github.flemmli97.fateubw.common.lib.LibEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntityCuchulainn extends BaseServant {

    public final CuchulainnAttackGoal attackAI = new CuchulainnAttackGoal(this);

    private static final AnimatedAction npAttack = new AnimatedAction(15, 9, "gae_bolg");
    private static final AnimatedAction[] anims = {AnimatedAction.vanillaAttack, npAttack};
    private int gaeBolgThrowTick;

    private final AnimationHandler<EntityCuchulainn> animationHandler = new AnimationHandler<>(this, anims);

    public EntityCuchulainn(EntityType<? extends BaseServant> entityType, Level world) {
        super(entityType, world, LibEntities.cuchulainn + ".hogou");
        if (world != null && !world.isClientSide)
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.gaebolg.get()));
    }

    @Override
    public int attackCooldown(AnimatedAction anim) {
        return 0;
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        if (type == AttackType.NP)
            return anim.getID().equals(npAttack.getID());
        return anim.getID().equals(AnimatedAction.vanillaAttack.getID());
    }

    @Override
    public AnimationHandler<EntityCuchulainn> getAnimationHandler() {
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
    public void aiStep() {
        if (!this.level.isClientSide) {
            this.gaeBolgThrowTick = Math.max(0, --this.gaeBolgThrowTick);
            if (this.gaeBolgThrowTick == 1 && this.getMainHandItem().getItem() != ModItems.gaebolg.get())
                this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.gaebolg.get()));
            if (this.getHealth() < 0.25 * this.getMaxHealth() && this.getHealth() > 0) {
                if (!this.critHealth) {
                    this.level.getServer().getPlayerList().broadcastMessage(new TranslatableComponent("chat.servant.cuchulainn").withStyle(ChatFormatting.GOLD), ChatType.SYSTEM, Util.NIL_UUID);
                    this.critHealth = true;
                }
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1, 2, false, false));
            }
        }
        super.aiStep();
    }

    public void attackWithNP(LivingEntity target) {
        if (target != null) {
            GaeBolg gaeBolg = new GaeBolg(this.level, this);
            gaeBolg.shootAtPosition(target.getX(), target.getY() + target.getEyeHeight(), target.getZ(), 1.5F, 0);
            this.level.addFreshEntity(gaeBolg);
            this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            this.gaeBolgThrowTick = 100;
            this.revealServant();
        }
    }

    public void retrieveGaeBolg() {
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.gaebolg.get()));
        this.gaeBolgThrowTick = 0;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("GaeBolgTick", this.gaeBolgThrowTick);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.gaeBolgThrowTick = tag.getInt("GaeBolgTick");
    }
}
