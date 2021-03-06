package io.github.flemmli97.fate.common.entity.servant;


import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.fate.common.capability.CapabilityInsts;
import io.github.flemmli97.fate.common.entity.EntityBabylonWeapon;
import io.github.flemmli97.fate.common.entity.EntityEnumaElish;
import io.github.flemmli97.fate.common.entity.SwitchableWeapon;
import io.github.flemmli97.fate.common.entity.servant.ai.GilgameshAttackGoal;
import io.github.flemmli97.fate.common.registry.ModItems;
import io.github.flemmli97.fate.common.utils.EnumServantUpdate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityGilgamesh extends EntityServant {

    public final GilgameshAttackGoal attackAI = new GilgameshAttackGoal(this, 12);

    private static final AnimatedAction rangedAttack = new AnimatedAction(40, 10, "babylon1");
    private static final AnimatedAction rangedAttack2 = new AnimatedAction(40, 10, "babylon2");
    private static final AnimatedAction npAttack = new AnimatedAction(20, 10, "np");
    private static final AnimatedAction[] anims = {AnimatedAction.vanillaAttack, rangedAttack, npAttack, rangedAttack2};

    private final AnimationHandler<EntityGilgamesh> animationHandler = new AnimationHandler<>(this, anims);

    public final SwitchableWeapon<EntityGilgamesh> switchableWeapon = new SwitchableWeapon<>(this, new ItemStack(ModItems.enumaelish.get()), ItemStack.EMPTY);

    public EntityGilgamesh(EntityType<? extends EntityGilgamesh> entityType, World world) {
        super(entityType, world, "Enuma Elish");
        this.revealServant();
        if (world != null && !world.isRemote)
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
        return anim.getID().equals("vanilla");
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
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        super.damageEntity(damageSrc, damageAmount);
        if (!this.canUseNP && !this.getShouldBeDead() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
            this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.enumaelish.get()));
        }
    }

    public void attackWithNP(double[] pos) {
        EntityEnumaElish ea = new EntityEnumaElish(this.world, this);
        if (pos != null)
            ea.setRotationTo(pos[0], pos[1], pos[2], 0);
        this.world.addEntity(ea);
        this.revealServant();
        this.getHeldItemMainhand().getCapability(CapabilityInsts.ItemStackCap).ifPresent(cap -> cap.setInUse(this, false, true));
        this.switchableWeapon.switchItems(true);
    }

    public void attackWithRangedAttack(LivingEntity target) {
        int weaponAmount = this.getRNG().nextInt(15) + 4;
        if (!this.getAnimationHandler().getAnimation().isPresent() || this.getAnimationHandler().isCurrentAnim(rangedAttack.getID()))
            this.spawnBehind(target, weaponAmount);
        else if (this.getAnimationHandler().isCurrentAnim(rangedAttack2.getID()))
            this.spawnAroundTarget(target, weaponAmount);
    }

    private void spawnBehind(LivingEntity target, int amount) {
        for (int x = 0; x < amount; x++) {
            EntityBabylonWeapon weapon = new EntityBabylonWeapon(this.world, this, target);
            if (!this.world.isRemote) {
                weapon.setEntityProperties();
            }
        }
    }

    private void spawnAroundTarget(LivingEntity target, int amount) {
        for (int x = 0; x < amount; x++) {
            EntityBabylonWeapon weapon = new EntityBabylonWeapon(this.world, this, target);
            if (!this.world.isRemote) {
                weapon.setEntityProperties();
            }
        }
    }

    @Override
    public void writeAdditional(CompoundNBT tag) {
        super.writeAdditional(tag);
        this.switchableWeapon.save(tag);
    }

    @Override
    public void readAdditional(CompoundNBT tag) {
        super.readAdditional(tag);
        this.switchableWeapon.read(tag);
    }
}