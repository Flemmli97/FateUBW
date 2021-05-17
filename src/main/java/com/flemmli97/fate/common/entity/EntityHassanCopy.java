package com.flemmli97.fate.common.entity;

import com.flemmli97.fate.common.config.Config;
import com.flemmli97.fate.common.entity.ai.AnimatedMeleeGoal;
import com.flemmli97.fate.common.entity.ai.TargetOwnerEnemyGoal;
import com.flemmli97.fate.common.entity.servant.EntityHassan;
import com.flemmli97.fate.common.entity.servant.EntityServant;
import com.flemmli97.fate.common.entity.servant.ai.FollowMasterGoal;
import com.flemmli97.fate.common.registry.FateAttributes;
import com.flemmli97.fate.common.registry.ModEntities;
import com.flemmli97.fate.common.registry.ModItems;
import com.flemmli97.tenshilib.api.entity.IAnimated;
import com.flemmli97.tenshilib.api.entity.IOwnable;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.flemmli97.tenshilib.common.entity.EntityUtil;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityHassanCopy extends CreatureEntity implements IAnimated, IOwnable<EntityHassan>, IServantMinion {

    private UUID ownerUUID;
    private EntityHassan owner;

    public FollowMasterGoal<EntityHassanCopy> follow = new FollowMasterGoal<>(this, 16.0D, 9.0F, 3.0F);
    public WaterAvoidingRandomWalkingGoal wander = new WaterAvoidingRandomWalkingGoal(this, 1.0D);

    private AnimatedAction anim;

    public EntityHassanCopy(EntityType<? extends EntityHassanCopy> type, World world) {
        super(type, world);
        if (world != null && !world.isRemote) {
            this.updateAttributes();
        }
    }

    public EntityHassanCopy(World world, EntityHassan entityHassan) {
        this(ModEntities.hassanCopy.get(), world);
        this.setOriginal(entityHassan);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.assassinDagger.get()));
    }

    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, ILivingEntityData data, CompoundNBT nbt) {
        super.onInitialSpawn(world, difficulty, reason, data, nbt);
        this.setEquipmentBasedOnDifficulty(difficulty);
        return data;
    }

    public void setOriginal(EntityHassan entityHassan) {
        this.ownerUUID = entityHassan.getUniqueID();
        this.owner = entityHassan;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new AnimatedMeleeGoal<>(this, m -> AnimatedAction.vanillaAttack));
        this.goalSelector.addGoal(1, new FollowMasterGoal<>(this, 16.0D, 9.0F, 3.0F));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(0, new TargetOwnerEnemyGoal<>(this));
    }

    private void updateAttributes() {
        this.getAttribute(Attributes.GENERIC_MAX_HEALTH).setBaseValue(Config.Common.hassanCopyProps.health());
        this.setHealth(this.getMaxHealth());
        this.getAttribute(Attributes.GENERIC_ATTACK_DAMAGE).setBaseValue(Config.Common.hassanCopyProps.strength());
        this.getAttribute(Attributes.GENERIC_ARMOR).setBaseValue(Config.Common.hassanCopyProps.armor());
        this.getAttribute(FateAttributes.MAGIC_RESISTANCE).setBaseValue(Config.Common.hassanCopyProps.magicRes());
        this.getAttribute(FateAttributes.PROJECTILE_BLOCKCHANCE).setBaseValue(Config.Common.hassanCopyProps.projectileBlockChance());
        this.getAttribute(FateAttributes.PROJECTILE_RESISTANCE).setBaseValue(Config.Common.hassanCopyProps.projectileProt());
        this.getAttribute(Attributes.GENERIC_MOVEMENT_SPEED).setBaseValue(Config.Common.hassanCopyProps.moveSpeed());//default 0.3
    }

    @Override
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    @Override
    public EntityHassan getOwner() {
        if (this.owner == null && this.getOwnerUUID() != null)
            this.owner = EntityUtil.findFromUUID(EntityHassan.class, this.world, this.getOwnerUUID());
        return this.owner;
    }

    @Override
    public AnimatedAction getAnimation() {
        return this.anim;
    }

    @Override
    public void setAnimation(AnimatedAction animatedAction) {
        this.anim = animatedAction == null ? null : animatedAction.create();
        IAnimated.sentToClient(this);
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return AnimatedAction.vanillaAttackOnly;
    }

    @Override
    public void livingTick() {
        if (this.isAlive() && (this.getOwner() == null || !this.getOwner().isAlive()))
            this.attackEntityFrom(DamageSource.OUT_OF_WORLD, Integer.MAX_VALUE);
        super.livingTick();
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
        if (damageSource == DamageSource.OUT_OF_WORLD) {
            return super.attackEntityFrom(damageSource, damage);
        } else {
            if (!(damageSource.getTrueSource() instanceof EntityServant || damageSource.getTrueSource() instanceof IServantMinion))
                damage *= 0.5;

            if (damageSource.isProjectile() && !damageSource.isUnblockable() && this.projectileBlockChance(damageSource, damage)) {
                this.world.playSound(null, this.getBlockPos(), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.NEUTRAL, 1, 1);
                if (damageSource.getImmediateSource() != null)
                    damageSource.getImmediateSource().remove();
                return false;
            }
            return super.attackEntityFrom(damageSource, Math.min(50, damage));
        }
    }

    public boolean projectileBlockChance(DamageSource damageSource, float damage) {
        return this.rand.nextFloat() < (float) this.getAttributeValue(FateAttributes.PROJECTILE_BLOCKCHANCE);
    }

    @Override
    protected void onDeathUpdate() {
		/*for(int i = 0; i < ((int)((7/(float)this.maxDeathTick())*this.deathTicks-1)); i++)
			Particles.spawnParticle(ModRender.particleFade, this.world, this.posX + (this.rand.nextDouble() - 0.5D) * (this.width+3),
    		this.posY + this.rand.nextDouble() * (this.height+1.5),
    		this.posZ + (this.rand.nextDouble() - 0.5D) * (this.width+3),
    		this.rand.nextGaussian() * 0.02D,
    		this.rand.nextGaussian() * 0.02D,
    		this.rand.nextGaussian() * 0.02D);*/
        if (!this.world.isRemote) {
            ++this.deathTime;
            if (this.deathTime == 1) {
                //if(this.getLastDamageSource()!=DamageSource.OUT_OF_WORLD)
                this.world.getServer().getPlayerList().broadcastChatMessage(new TranslationTextComponent("chat.servant.death").formatted(TextFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
                this.playSound(SoundEvents.ENTITY_WITHER_SPAWN, 1.0F, 1.0F);
            }
            if (this.deathTime == this.maxDeathTick()) {
                this.remove();
            }
        }
    }

    public int maxDeathTick() {
        return 200;
    }
}