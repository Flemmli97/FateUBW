package com.flemmli97.fate.common.entity;

import com.flemmli97.fate.common.config.Config;
import com.flemmli97.fate.common.entity.ai.StarfishAttackGoal;
import com.flemmli97.fate.common.entity.servant.EntityServant;
import com.flemmli97.fate.common.registry.ModEntities;
import com.flemmli97.tenshilib.api.entity.IAnimated;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.flemmli97.tenshilib.common.entity.EntityUtil;
import com.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityLesserMonster extends CreatureEntity implements IServantMinion, IAnimated {

    private UUID ownerUUID;
    private LivingEntity owner;
    private int livingTicks;
    private AnimatedAction currentAnim;
    public static final AnimatedAction walk = new AnimatedAction(31, 0, "walk");
    public static final AnimatedAction attack = new AnimatedAction(20, 15, "attack");
    private static final AnimatedAction[] anims = new AnimatedAction[]{walk, attack};
    public NearestAttackableTargetGoal<LivingEntity> target = new NearestAttackableTargetGoal<LivingEntity>(this, LivingEntity.class, 10, true, true,
            (living) -> EntityLesserMonster.this.canAttackTarget(living));

    public EntityLesserMonster(EntityType<? extends EntityLesserMonster> type, World world) {
        super(type, world);
        if (world != null && !world.isRemote) {
            this.goals();
            this.setAttributes();
        }
    }

    public EntityLesserMonster(World world, LivingEntity owner) {
        this(ModEntities.lesserMonster.get(), world);
        BlockPos pos = RayTraceUtils.randomPosAround(world, this, owner.getBlockPos(), 9, true, this.getRNG());
        this.setLocationAndAngles(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, MathHelper.wrapDegrees(this.world.rand.nextFloat() * 360.0F), 0.0F);
        this.owner = owner;
        this.ownerUUID = owner.getUniqueID();
    }

    protected void setAttributes() {
        this.getAttribute(Attributes.GENERIC_ATTACK_DAMAGE).setBaseValue(Config.Common.smallMonsterDamage);
        this.getAttribute(Attributes.GENERIC_MAX_HEALTH).setBaseValue(18);
        this.getAttribute(Attributes.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1);
        this.getAttribute(Attributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    protected void goals() {
        this.goalSelector.addGoal(2, new StarfishAttackGoal(this));
        this.goalSelector.addGoal(3, new SwimGoal(this));
        this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.targetSelector.addGoal(0, this.target);
    }

    @Override
    public void livingTick() {
        super.livingTick();
        if (!this.world.isRemote) {
            this.livingTicks++;
            if (this.livingTicks > Config.Common.gillesMinionDuration)
                this.remove();
            if (this.currentAnim == null && (this.getMotion().x != 0 || this.getMotion().z != 0))
                this.setAnimation(walk);
            if (this.getOwner() != null && this.getOwner().getRevengeTarget() != null && this.getAttackTarget() == null)
                this.setAttackTarget(this.getOwner().getRevengeTarget());
        }
        this.tickAnimation();
    }

    @Override
    public void tickAnimation() {
        if (this.getAnimation() != null && this.getAnimation().tick()) {
            if (this.getAnimation().getID().equals("walk") && (this.getMotion().x != 0 || this.getMotion().z != 0))
                this.getAnimation().reset();
            else
                this.setAnimation(null);
        }
    }

    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null) {
            this.owner = EntityUtil.findFromUUID(LivingEntity.class, this.world, this.ownerUUID);
        }
        return this.owner;
    }

    protected boolean canAttackTarget(LivingEntity e) {
        if (this.getClass().equals(e.getClass()) || e.getUniqueID().equals(this.ownerUUID))
            return false;
        if ((this.getOwner() instanceof EntityServant && ((EntityServant) this.getOwner()).getOwner() != null && ((EntityServant) this.getOwner()).getOwner().getUniqueID().equals(e.getUniqueID())))
            return false;
        return true;
    }

    @Override
    public void readAdditional(CompoundNBT tag) {
        super.readAdditional(tag);
        if (tag.contains("Owner"))
            this.ownerUUID = tag.getUniqueId("Owner");
    }

    @Override
    public void writeAdditional(CompoundNBT tag) {
        super.writeAdditional(tag);
        if (this.ownerUUID != null)
            tag.putUniqueId("Owner", this.ownerUUID);
    }

    @Override
    public AnimatedAction getAnimation() {
        return this.currentAnim;
    }

    @Override
    public void setAnimation(AnimatedAction anim) {
        this.currentAnim = anim == null ? null : anim.create();
        IAnimated.sentToClient(this);
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }
}