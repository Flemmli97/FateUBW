package io.github.flemmli97.fateubw.common.entity.servant;


import io.github.flemmli97.fateubw.common.entity.SwitchableWeapon;
import io.github.flemmli97.fateubw.common.entity.misc.ArcherArrow;
import io.github.flemmli97.fateubw.common.entity.misc.CaladBolg;
import io.github.flemmli97.fateubw.common.entity.servant.ai.EmiyaAttackGoal;
import io.github.flemmli97.fateubw.common.lib.LibEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntityEmiya extends BaseServant {

    public final EmiyaAttackGoal attackAI = new EmiyaAttackGoal(this, 10);

    private static final AnimatedAction rangedAttack = new AnimatedAction(30, 10, "ranged");
    private static final AnimatedAction npAttack = new AnimatedAction(20, 0, "np");
    private static final AnimatedAction[] anims = {AnimatedAction.vanillaAttack, rangedAttack, npAttack};

    private final AnimationHandler<EntityEmiya> animationHandler = new AnimationHandler<>(this, anims);

    public final SwitchableWeapon<EntityEmiya> switchableWeapon = new SwitchableWeapon<>(this, new ItemStack(ModItems.archbow.get()), ItemStack.EMPTY);

    public EntityEmiya(EntityType<? extends EntityEmiya> entityType, Level world) {
        super(entityType, world, LibEntities.emiya + ".hogou");
        if (world != null && !world.isClientSide)
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.kanshou.get()));
        //this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ModItems.bakuya.get()));
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        if (type == AttackType.RANGED)
            return anim.getID().equals(rangedAttack.getID());
        else if (type == AttackType.NP)
            return anim.getID().equals(npAttack.getID());
        return anim.getID().equals(AnimatedAction.vanillaAttack.getID());
    }

    @Override
    public AnimationHandler<EntityEmiya> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public int attackCooldown(AnimatedAction anim) {
        return this.canUse(anim, AttackType.RANGED) ? 40 : 0;
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.canUseNP && !this.isDeadOrDying() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.archbow.get()));
            this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        }
    }

    @Override
    public void updateAI(EnumServantUpdate behaviour) {
        super.updateAI(behaviour);
        if (this.commandBehaviour == EnumServantUpdate.STAY)
            this.goalSelector.removeGoal(this.attackAI);
        else
            this.goalSelector.addGoal(0, this.attackAI);
    }

    public void attackWithRangedAttack(LivingEntity target) {
        ArcherArrow arrow = new ArcherArrow(this.level, this);
        if (!this.level.isClientSide) {
            double d0 = target.getX() - this.getX();
            double d1 = target.getBoundingBox().minY + (double) (target.getBbHeight() / 3.0F) - arrow.getY();
            double d2 = target.getZ() - this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            arrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, 2);
            arrow.setBaseDamage(arrow.getBaseDamage() + 5.0);
            arrow.setKnockback(0);
            this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level.addFreshEntity(arrow);
        }
    }

    public void attackWithNP(LivingEntity target) {
        if (target != null) {
            CaladBolg bolg = new CaladBolg(this.level, this);
            bolg.shootAtPosition(target.getX(), target.getY() + target.getEyeHeight(), target.getZ(), 2F, 0);
            this.level.addFreshEntity(bolg);
            this.revealServant();
            this.switchableWeapon.switchItems(true);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        this.switchableWeapon.save(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.switchableWeapon.read(tag);
    }
}