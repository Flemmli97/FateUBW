package io.github.flemmli97.fate.common.entity.servant;


import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.fate.common.entity.EntityGaeBolg;
import io.github.flemmli97.fate.common.entity.servant.ai.CuchulainnAttackGoal;
import io.github.flemmli97.fate.common.registry.ModItems;
import io.github.flemmli97.fate.common.utils.EnumServantUpdate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCuchulainn extends EntityServant {

    public final CuchulainnAttackGoal attackAI = new CuchulainnAttackGoal(this);

    private static final AnimatedAction npAttack = new AnimatedAction(15, 9, "gae_bolg");
    private static final AnimatedAction[] anims = {AnimatedAction.vanillaAttack, npAttack};
    private int gaeBolgThrowTick;

    private final AnimationHandler<EntityCuchulainn> animationHandler = new AnimationHandler<>(this, anims);

    public EntityCuchulainn(EntityType<? extends EntityServant> entityType, World world) {
        super(entityType, world, "cuchulainn.hogou");
        if (world != null && !world.isRemote)
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.gaebolg.get()));
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
    public boolean onLivingFall(float distance, float damageMultiplier) {
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
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        super.damageEntity(damageSrc, damageAmount);
        if (!this.canUseNP && !this.getShouldBeDead() && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
        }
    }

    @Override
    public void livingTick() {
        if (!this.world.isRemote) {
            this.gaeBolgThrowTick = Math.max(0, --this.gaeBolgThrowTick);
            if (this.gaeBolgThrowTick == 1 && this.getHeldItemMainhand().getItem() != ModItems.gaebolg.get())
                this.setHeldItem(Hand.MAIN_HAND, new ItemStack(ModItems.gaebolg.get()));
            if (this.getHealth() < 0.25 * this.getMaxHealth() && this.getHealth() > 0) {
                if (!this.critHealth) {
                    this.world.getServer().getPlayerList().func_232641_a_(new TranslationTextComponent("chat.servant.cuchulainn").mergeStyle(TextFormatting.GOLD), ChatType.SYSTEM, Util.DUMMY_UUID);
                    this.critHealth = true;
                }
                this.addPotionEffect(new EffectInstance(Effects.SPEED, 1, 2, false, false));
            }
        }
        super.livingTick();
    }

    public void attackWithNP(LivingEntity target) {
        if (target != null) {
            EntityGaeBolg gaeBolg = new EntityGaeBolg(this.world, this);
            gaeBolg.shootAtPosition(target.getPosX(), target.getPosY() + target.getEyeHeight(), target.getPosZ(), 1.5F, 0);
            this.world.addEntity(gaeBolg);
            this.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
            this.gaeBolgThrowTick = 100;
            this.revealServant();
        }
    }

    public void retrieveGaeBolg() {
        this.setHeldItem(Hand.MAIN_HAND, new ItemStack(ModItems.gaebolg.get()));
        this.gaeBolgThrowTick = 0;
    }

    @Override
    public void writeAdditional(CompoundNBT tag) {
        super.writeAdditional(tag);
        tag.putInt("GaeBolgTick", this.gaeBolgThrowTick);
    }

    @Override
    public void readAdditional(CompoundNBT tag) {
        super.readAdditional(tag);
        this.gaeBolgThrowTick = tag.getInt("GaeBolgTick");
    }
}
