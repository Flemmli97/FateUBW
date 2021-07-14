package io.github.flemmli97.fate.common.entity.servant;


import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.fate.common.entity.EntityExcalibur;
import io.github.flemmli97.fate.common.entity.SwitchableWeapon;
import io.github.flemmli97.fate.common.entity.servant.ai.ArthurAttackGoal;
import io.github.flemmli97.fate.common.registry.ModItems;
import io.github.flemmli97.fate.common.utils.EnumServantUpdate;
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

public class EntityArthur extends EntityServant<EntityArthur> {

    public final ArthurAttackGoal attackAI = new ArthurAttackGoal(this);

    public static final AnimatedAction swing_1 = new AnimatedAction(18, 15, "swing_1");

    public static final AnimatedAction npAttack = new AnimatedAction(15, 8, "excalibur");
    public static final AnimatedAction[] anims = {swing_1, npAttack};

    public final SwitchableWeapon<EntityArthur> switchableWeapon = new SwitchableWeapon<>(this, new ItemStack(ModItems.excalibur.get()), ItemStack.EMPTY);

    public EntityArthur(EntityType<? extends EntityArthur> entityType, World world) {
        super(entityType, world, "arthur.hogou");
        if (world != null && !world.isRemote)
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.invisexcalibur.get()));
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        if (type == AttackType.NP)
            return anim.getID().equals(npAttack.getID());
        return anim.getID().equals(swing_1.getID());
    }

    @Override
    public AnimationHandler<EntityArthur> createAnimationHandler() {
        return new AnimationHandler<>(this, anims);
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
            this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.excalibur.get()));
        }
    }

    @Override
    public void livingTick() {
        if (this.getHealth() < 0.25 * this.getMaxHealth() && this.getHealth() > 0) {
            if (!this.critHealth) {
                if (!this.world.isRemote)
                    this.world.getServer().getPlayerList().func_232641_a_(new TranslationTextComponent("chat.servant.avalon").mergeStyle(TextFormatting.GOLD), ChatType.SYSTEM, Util.DUMMY_UUID);
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
        this.stopActiveHand();
        this.switchableWeapon.switchItems(true);
    }
}
