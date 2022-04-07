package io.github.flemmli97.fateubw.common.entity.minions;

import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.entity.IServantMinion;
import io.github.flemmli97.fateubw.common.entity.ai.AnimatedMeleeGoal;
import io.github.flemmli97.fateubw.common.entity.ai.TargetOwnerEnemyGoal;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.entity.servant.EntityHassan;
import io.github.flemmli97.fateubw.common.entity.servant.ai.FollowMasterGoal;
import io.github.flemmli97.fateubw.common.registry.ModAttributes;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.UUID;

public class HassanClone extends PathfinderMob implements IAnimated, OwnableEntity, IServantMinion {

    private UUID ownerUUID;
    private EntityHassan owner;

    private final AnimationHandler<HassanClone> animationHandler = new AnimationHandler<>(this, AnimatedAction.vanillaAttackOnly);

    public HassanClone(EntityType<? extends HassanClone> type, Level world) {
        super(type, world);
        if (world != null && !world.isClientSide) {
            this.updateAttributes();
        }
    }

    public HassanClone(Level world, EntityHassan entityHassan) {
        this(ModEntities.hassanCopy.get(), world);
        this.setOriginal(entityHassan);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.assassinDagger.get()));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData data, CompoundTag nbt) {
        super.finalizeSpawn(world, difficulty, reason, data, nbt);
        this.populateDefaultEquipmentSlots(difficulty);
        return data;
    }

    public void setOriginal(EntityHassan entityHassan) {
        this.ownerUUID = entityHassan.getUUID();
        this.owner = entityHassan;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new AnimatedMeleeGoal<>(this, m -> AnimatedAction.vanillaAttack));
        this.goalSelector.addGoal(1, new FollowMasterGoal<>(this, 16.0D, 9.0F, 3.0F));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new FloatGoal(this));
        this.goalSelector.addGoal(7, new OpenDoorGoal(this, true));
        this.targetSelector.addGoal(0, new TargetOwnerEnemyGoal<>(this));
    }

    private void updateAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Config.Common.hassanCopyProps.health());
        this.setHealth(this.getMaxHealth());
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Config.Common.hassanCopyProps.strength());
        this.getAttribute(Attributes.ARMOR).setBaseValue(Config.Common.hassanCopyProps.armor());
        this.getAttribute(ModAttributes.MAGIC_RESISTANCE.get()).setBaseValue(Config.Common.hassanCopyProps.magicRes());
        this.getAttribute(ModAttributes.PROJECTILE_BLOCKCHANCE.get()).setBaseValue(Config.Common.hassanCopyProps.projectileBlockChance());
        this.getAttribute(ModAttributes.PROJECTILE_RESISTANCE.get()).setBaseValue(Config.Common.hassanCopyProps.projectileProt());
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(Config.Common.hassanCopyProps.moveSpeed());//default 0.3
    }

    @Override
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    @Override
    public EntityHassan getOwner() {
        if (this.owner == null && this.getOwnerUUID() != null)
            this.owner = EntityUtil.findFromUUID(EntityHassan.class, this.level, this.getOwnerUUID());
        return this.owner;
    }

    @Override
    public AnimationHandler<HassanClone> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void tick() {
        if (this.tickCount > 200 && this.isAlive() && (this.getOwner() == null || !this.getOwner().isAlive()))
            this.hurt(DamageSource.OUT_OF_WORLD, Integer.MAX_VALUE);
        super.tick();
        this.getAnimationHandler().tick();
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (entity instanceof Mob) {
            LivingEntity target = ((Mob) entity).getTarget();
            if (target == this.getOwner())
                ((Mob) entity).setTarget(this);
        }
        return super.doHurtTarget(entity);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (damageSource == DamageSource.OUT_OF_WORLD) {
            return super.hurt(damageSource, damage);
        } else {
            if (!(damageSource.getEntity() instanceof BaseServant || damageSource.getEntity() instanceof IServantMinion))
                damage *= 0.5;

            if (damageSource.isProjectile() && !damageSource.isBypassArmor() && this.projectileBlockChance(damageSource, damage)) {
                this.level.playSound(null, this.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.NEUTRAL, 1, 1);
                if (damageSource.getDirectEntity() != null)
                    damageSource.getDirectEntity().remove(RemovalReason.KILLED);
                return false;
            }
            return super.hurt(damageSource, Math.min(50, damage));
        }
    }

    public boolean projectileBlockChance(DamageSource damageSource, float damage) {
        return this.random.nextFloat() < (float) this.getAttributeValue(ModAttributes.PROJECTILE_BLOCKCHANCE.get());
    }

    @Override
    protected void tickDeath() {
		/*for(int i = 0; i < ((int)((7/(float)this.maxDeathTick())*this.deathTicks-1)); i++)
			Particles.spawnParticle(ModRender.particleFade, this.world, this.posX + (this.rand.nextDouble() - 0.5D) * (this.width+3),
    		this.posY + this.rand.nextDouble() * (this.height+1.5),
    		this.posZ + (this.rand.nextDouble() - 0.5D) * (this.width+3),
    		this.rand.nextGaussian() * 0.02D,
    		this.rand.nextGaussian() * 0.02D,
    		this.rand.nextGaussian() * 0.02D);*/
        if (!this.level.isClientSide) {
            ++this.deathTime;
            if (this.deathTime == 1) {
                //if(this.getLastDamageSource()!=DamageSource.OUT_OF_WORLD)
                this.level.getServer().getPlayerList().broadcastMessage(new TranslatableComponent("chat.servant.death").withStyle(ChatFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID);
                this.playSound(SoundEvents.WITHER_SPAWN, 1.0F, 1.0F);
            }
            if (this.deathTime == this.maxDeathTick()) {
                this.remove(RemovalReason.KILLED);
            }
        }
    }

    public int maxDeathTick() {
        return 200;
    }
}