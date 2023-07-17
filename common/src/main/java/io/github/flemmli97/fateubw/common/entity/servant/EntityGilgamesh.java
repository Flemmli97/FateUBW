package io.github.flemmli97.fateubw.common.entity.servant;


import io.github.flemmli97.fateubw.common.entity.SwitchableWeapon;
import io.github.flemmli97.fateubw.common.entity.misc.BabylonWeapon;
import io.github.flemmli97.fateubw.common.entity.misc.EnumaElish;
import io.github.flemmli97.fateubw.common.entity.servant.ai.GilgameshAttackGoal;
import io.github.flemmli97.fateubw.common.lib.LibEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.fateubw.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntityGilgamesh extends BaseServant {

    public final GilgameshAttackGoal attackAI = new GilgameshAttackGoal(this, 12);

    private static final AnimatedAction rangedAttack = new AnimatedAction(40, 10, "babylon1");
    private static final AnimatedAction rangedAttack2 = new AnimatedAction(40, 10, "babylon2");
    private static final AnimatedAction npAttack = new AnimatedAction(20, 10, "np");
    private static final AnimatedAction[] anims = {AnimatedAction.vanillaAttack, rangedAttack, npAttack, rangedAttack2};

    private final AnimationHandler<EntityGilgamesh> animationHandler = new AnimationHandler<>(this, anims);

    public final SwitchableWeapon<EntityGilgamesh> switchableWeapon = new SwitchableWeapon<>(this, new ItemStack(ModItems.enumaelish.get()), ItemStack.EMPTY);

    public EntityGilgamesh(EntityType<? extends EntityGilgamesh> entityType, Level world) {
        super(entityType, world, LibEntities.gilgamesh + ".hogou");
        this.revealServant();
        if (world != null && !world.isClientSide)
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    public boolean showServant() {
        return true;
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        if (type == AttackType.RANGED)
            return anim.getID().equals(rangedAttack.getID()) || anim.getID().equals(rangedAttack2.getID());
        else if (type == AttackType.NP)
            return anim.getID().equals(npAttack.getID());
        return anim.getID().equals(AnimatedAction.vanillaAttack.getID());
    }

    @Override
    public AnimationHandler<EntityGilgamesh> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public int attackCooldown(AnimatedAction anim) {
        return this.canUse(anim, AttackType.RANGED) ? 40 : 0;
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
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.enumaelish.get()));
        }
    }

    public void attackWithNP(double[] pos) {
        EnumaElish ea = new EnumaElish(this.level, this);
        if (pos != null)
            ea.setRotationTo(pos[0], pos[1], pos[2], 0);
        this.level.addFreshEntity(ea);
        this.revealServant();
        Platform.INSTANCE.getItemStackData(this.getMainHandItem()).ifPresent(data -> data.setInUse(this, false, true));
        this.switchableWeapon.switchItems(true);
    }

    public void attackWithRangedAttack(LivingEntity target) {
        int weaponAmount = this.getRandom().nextInt(15) + 4;
        if (this.getAnimationHandler().getAnimation() == null || this.getAnimationHandler().isCurrent(rangedAttack))
            this.spawnBehind(target, weaponAmount);
        else if (this.getAnimationHandler().isCurrent(rangedAttack2))
            this.spawnAroundTarget(target, weaponAmount);
    }

    private void spawnBehind(LivingEntity target, int amount) {
        for (int x = 0; x < amount; x++) {
            BabylonWeapon weapon = new BabylonWeapon(this.level, this, target);
            if (!this.level.isClientSide) {
                weapon.setEntityProperties();
            }
        }
    }

    private void spawnAroundTarget(LivingEntity target, int amount) {
        for (int x = 0; x < amount; x++) {
            BabylonWeapon weapon = new BabylonWeapon(this.level, this, target);
            if (!this.level.isClientSide) {
                weapon.setEntityProperties();
            }
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