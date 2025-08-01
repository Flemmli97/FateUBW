package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.api.datapack.ServantExtraData;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

public class MagicBufCircle extends Entity implements OwnableEntity {

    protected static final EntityDataAccessor<Float> RANGE = SynchedEntityData.defineId(MagicBufCircle.class, EntityDataSerializers.FLOAT);

    private LivingEntity owner;
    private UUID ownerUUID;
    private int livingTick;
    private List<float[]> circlePoints;

    private final int maxLivingTicks;

    public MagicBufCircle(EntityType<?> entityTypeIn, Level level) {
        super(entityTypeIn, level);
        this.maxLivingTicks = DatapackHandler.SERVANT_PROPS.get(ModEntities.MEDEA.getID())
                .getConfig(ServantExtraData.MEDEA_CIRCLE_DURATION);
    }

    public MagicBufCircle(Level level, LivingEntity owner, float r) {
        this(ModEntities.MEDEA_CIRCLE.get(), level);
        this.setPos(owner.getX(), owner.getY(), owner.getZ());
        this.owner = owner;
        this.ownerUUID = owner.getUUID();
        this.entityData.set(RANGE, r);
    }

    @Override
    public void tick() {
        super.tick();
        this.livingTick++;
        if (this.level.isClientSide && this.livingTick % 5 == 0) {
            if (this.circlePoints == null)
                this.circlePoints = MathUtils.pointsOfCircle(this.entityData.get(RANGE), 7);
            for (float[] f : this.circlePoints)
                for (int i = 0; i < 3; i++)
                    this.level.addParticle(ParticleTypes.WITCH, this.getX() + f[0], this.getY() + 0.2, this.getZ() + f[1], 0, 0.12, 0);
        }
        if (!this.level.isClientSide) {
            if (this.tickCount % 5 == 0 && this.getOwner() != null) {
                float r = this.entityData.get(RANGE);
                for (LivingEntity entity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(r),
                        entity -> Utils.alliedTo(this.getOwner(), entity))) {
                    entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1, 2, true, false));
                    if (!entity.hasEffect(MobEffects.REGENERATION))
                        entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 50, 1, true, false));
                    entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1, 2, true, false));
                }
            }
            if (this.livingTick > this.maxLivingTicks || this.getOwner() == null || this.getOwner().isDeadOrDying())
                this.discard();
        }
    }

    private boolean canApplyTo(LivingEntity entity) {
        if (this.getOwner() == null)
            return false;
        return Utils.alliedTo(this.getOwner(), entity);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(RANGE, 1f);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.ownerUUID = compound.getUUID("Owner");
        this.livingTick = compound.getInt("Ticks");
        this.entityData.set(RANGE, compound.getFloat("Range"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putUUID("Owner", this.ownerUUID);
        compound.putInt("Ticks", this.livingTick);
        compound.putFloat("Range", this.entityData.get(RANGE));
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    @Override
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null)
            this.owner = EntityUtil.findFromUUID(LivingEntity.class, this.level, this.ownerUUID);
        return this.owner;
    }
}