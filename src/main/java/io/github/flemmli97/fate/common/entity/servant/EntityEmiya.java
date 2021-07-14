package io.github.flemmli97.fate.common.entity.servant;


import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.fate.common.entity.EntityArcherArrow;
import io.github.flemmli97.fate.common.entity.EntityCaladBolg;
import io.github.flemmli97.fate.common.entity.SwitchableWeapon;
import io.github.flemmli97.fate.common.entity.servant.ai.EmiyaAttackGoal;
import io.github.flemmli97.fate.common.registry.ModItems;
import io.github.flemmli97.fate.common.utils.EnumServantUpdate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityEmiya extends EntityServant {

    public final EmiyaAttackGoal attackAI = new EmiyaAttackGoal(this, 10);

    private static final AnimatedAction rangedAttack = new AnimatedAction(30, 10, "ranged");
    private static final AnimatedAction npAttack = new AnimatedAction(20, 0, "np");
    private static final AnimatedAction[] anims = {AnimatedAction.vanillaAttack, rangedAttack, npAttack};

    private final AnimationHandler<EntityEmiya> animationHandler = new AnimationHandler<>(this, anims);

    public final SwitchableWeapon<EntityEmiya> switchableWeapon = new SwitchableWeapon<>(this, new ItemStack(ModItems.archbow.get()), ItemStack.EMPTY);

    public EntityEmiya(EntityType<? extends EntityEmiya> entityType, World world) {
        super(entityType, world, "Calad Bolg II");
        if (world != null && !world.isRemote)
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.kanshou.get()));
        this.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(ModItems.bakuya.get()));
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        if (type == AttackType.RANGED)
            return anim.getID().equals("ranged");
        else if (type == AttackType.NP)
            return anim.getID().equals("np");
        return anim.getID().equals("vanilla");
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
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        super.damageEntity(damageSrc, damageAmount);
        if (!this.canUseNP && !this.getShouldBeDead() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
            this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.archbow.get()));
            this.setItemStackToSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
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
        EntityArcherArrow arrow = new EntityArcherArrow(this.world, this);
        if (!this.world.isRemote) {
            double d0 = target.getPosX() - this.getPosX();
            double d1 = target.getBoundingBox().minY + (double) (target.getHeight() / 3.0F) - arrow.getPosY();
            double d2 = target.getPosZ() - this.getPosZ();
            double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
            arrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, 2);
            arrow.setDamage(arrow.getDamage() + 5.0);
            arrow.setKnockbackStrength(0);
            this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
            this.world.addEntity(arrow);
        }
    }

    public void attackWithNP(LivingEntity target) {
        if (target != null) {
            EntityCaladBolg bolg = new EntityCaladBolg(this.world, this);
            bolg.shootAtPosition(target.getPosX(), target.getPosY() + target.getEyeHeight(), target.getPosZ(), 2F, 0);
            this.world.addEntity(bolg);
            this.revealServant();
            this.switchableWeapon.switchItems(true);
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