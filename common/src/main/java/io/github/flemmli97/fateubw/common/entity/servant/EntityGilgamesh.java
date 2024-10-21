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
import net.minecraft.world.phys.Vec3;

public class EntityGilgamesh extends BaseServant {

    private static final AnimatedAction RANGED_ATTACK = new AnimatedAction(40, 10, "babylon1");
    private static final AnimatedAction RANGED_ATTACK_2 = new AnimatedAction(40, 10, "babylon2");
    private static final AnimatedAction NP_ATTACK = new AnimatedAction(20, 10, "np");
    private static final AnimatedAction[] ANIMS = {AnimatedAction.vanillaAttack, RANGED_ATTACK, NP_ATTACK, RANGED_ATTACK_2};

    public final GilgameshAttackGoal attackAI = new GilgameshAttackGoal(this, 12);

    private final AnimationHandler<EntityGilgamesh> animationHandler = new AnimationHandler<>(this, ANIMS);

    public final SwitchableWeapon<EntityGilgamesh> switchableWeapon = new SwitchableWeapon<>(this, new ItemStack(ModItems.ENUMAELISH.get()), ItemStack.EMPTY);

    public EntityGilgamesh(EntityType<? extends EntityGilgamesh> entityType, Level world) {
        super(entityType, world, LibEntities.GILGAMESH + ".hogou");
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
            return anim.getID().equals(RANGED_ATTACK.getID()) || anim.getID().equals(RANGED_ATTACK_2.getID());
        else if (type == AttackType.NP)
            return anim.getID().equals(NP_ATTACK.getID());
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
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.ENUMAELISH.get()));
        }
    }

    public void attackWithNP(Vec3 pos) {
        EnumaElish ea = new EnumaElish(this.level, this);
        if (pos != null)
            ea.setRotationTo(pos.x(), pos.y(), pos.z(), 0);
        this.level.addFreshEntity(ea);
        this.revealServant();
        Platform.INSTANCE.getItemStackData(this.getMainHandItem()).ifPresent(data -> data.setInUse(this, false, true));
        this.switchableWeapon.switchItems(true);
    }

    public void attackWithRangedAttack(LivingEntity target) {
        int weaponAmount = this.getRandom().nextInt(15) + 4;
        if (this.getAnimationHandler().getAnimation() == null || this.getAnimationHandler().isCurrent(RANGED_ATTACK))
            this.spawnBehind(target, weaponAmount);
        else if (this.getAnimationHandler().isCurrent(RANGED_ATTACK_2))
            this.spawnAroundTarget(target, weaponAmount);
    }

    private void spawnBehind(LivingEntity target, int amount) {
        BabylonWeapon.spawnWeapons(this, target, amount, 7);
    }

    private void spawnAroundTarget(LivingEntity target, int amount) {
        BabylonWeapon.spawnWeaponsAround(this, target, amount, 7);
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