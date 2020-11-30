package com.flemmli97.fate.common.entity.servant;

import com.flemmli97.fate.common.entity.servant.ai.HeraclesAttackGoal;
import com.flemmli97.fate.common.registry.ModItems;
import com.flemmli97.fate.common.utils.EnumServantUpdate;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityHeracles extends EntityServant {

    public final HeraclesAttackGoal attackAI = new HeraclesAttackGoal(this);

    private static final AnimatedAction swing_1 = new AnimatedAction(20, 18, "swing_1");
    private static final AnimatedAction[] anims = new AnimatedAction[]{swing_1};
    protected static final DataParameter<Integer> deathCount = EntityDataManager.createKey(EntityHeracles.class, DataSerializers.VARINT);
    private boolean voidDeath;

    public EntityHeracles(EntityType<? extends EntityServant> entityType, World world) {
        super(entityType, world, "heracles.hogou");
        if (world != null && !world.isRemote)
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.heraclesAxe.get()));
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        return true;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(deathCount, 0);
    }

    @Override
    public void updateAI(EnumServantUpdate behaviour) {
        super.updateAI(behaviour);
        if (this.commandBehaviour == EnumServantUpdate.STAY)
            this.goalSelector.removeGoal(this.attackAI);
        else
            this.goalSelector.addGoal(0, this.attackAI);
    }

    public void setDeathNumber(int death) {
        this.dataManager.set(deathCount, death);
    }

    public int getDeaths() {
        return this.dataManager.get(deathCount);
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
        if (damageSource != DamageSource.OUT_OF_WORLD && damage < 4)
            return false;
        return super.attackEntityFrom(damageSource, damage);
    }

    @Override
    public void livingTick() {
        if (this.getDeaths() > 4 && this.getDeaths() <= 8) {
            this.addPotionEffect(new EffectInstance(Effects.STRENGTH, 1, 1, false, false));

        } else if (this.getDeaths() > 8) {
            this.addPotionEffect(new EffectInstance(Effects.STRENGTH, 1, 2, false, false));
        }
        super.livingTick();
    }

    @Override
    protected void onDeathUpdate() {
        if (this.getLastDamageSource() == DamageSource.OUT_OF_WORLD || this.voidDeath) {
            this.voidDeath = true;
            super.onDeathUpdate();
        } else {
            if (this.getDeaths() < 12) {
                this.deathTime++;
                if (this.deathTime == 40 && !this.world.isRemote) {
                    this.setDeathNumber(this.getDeaths() + 1);
                    double heal = 1 - this.getDeaths() * 0.08;
                    this.setHealth((float) (heal * this.getMaxHealth()));
                    this.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 200, 3, false, false));
                    this.deathTime = 0;
                    this.revealServant();
                }
            } else {
                super.onDeathUpdate();
            }
        }
    }

    @Override
    public void writeAdditional(CompoundNBT tag) {
        super.writeAdditional(tag);
        tag.putInt("Deaths", this.getDeaths());
        tag.putBoolean("DeathType", this.voidDeath);
    }

    @Override
    public void readAdditional(CompoundNBT tag) {
        super.readAdditional(tag);
        this.setDeathNumber(tag.getInt("Deaths"));
        this.voidDeath = tag.getBoolean("DeathType");
    }

    @Override
    public int attackCooldown(AnimatedAction anim) {
        return 7;
    }
}
