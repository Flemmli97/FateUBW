package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.entity.servant.EntityMedea;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

public class MagicBufCircle extends Entity implements OwnableEntity {

    private EntityMedea owner;
    private UUID ownerUUID;
    private int livingTick;
    protected static final EntityDataAccessor<Float> range = SynchedEntityData.defineId(MagicBufCircle.class, EntityDataSerializers.FLOAT);
    private List<float[]> circlePoints;

    public MagicBufCircle(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public MagicBufCircle(Level world, EntityMedea owner, float r) {
        this(ModEntities.medeaCircle.get(), world);
        this.setPos(owner.getX(), owner.getY(), owner.getZ());
        this.owner = owner;
        this.ownerUUID = owner.getUUID();
        this.entityData.set(range, r);
    }

    @Override
    public void tick() {
        super.tick();
        this.livingTick++;
        if (this.level.isClientSide && this.livingTick % 5 == 0) {
            if (this.circlePoints == null)
                this.circlePoints = MathUtils.pointsOfCircle(this.entityData.get(range), 7);
            for (float[] f : this.circlePoints)
                for (int i = 0; i < 3; i++)
                    this.level.addParticle(ParticleTypes.WITCH, this.getX() + f[0], this.getY() + 0.2, this.getZ() + f[1], 0, 0.12, 0);
        }
        if (!this.level.isClientSide) {
            float r = this.entityData.get(range);
            if (this.getOwner() != null && this.getOwner().position().distanceToSqr(this.position()) < r * r)
                this.getOwner().buff();
            if (this.livingTick > Config.Common.medeaCircleSpan || this.getOwner() == null || this.getOwner().isDeadOrDying())
                this.kill();
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(range, 1f);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.ownerUUID = compound.getUUID("Owner");
        this.livingTick = compound.getInt("Ticks");
        this.entityData.set(range, compound.getFloat("Range"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putUUID("Owner", this.ownerUUID);
        compound.putInt("Ticks", this.livingTick);
        compound.putFloat("Range", this.entityData.get(range));
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
    public EntityMedea getOwner() {
        if (this.owner == null && this.ownerUUID != null)
            this.owner = EntityUtil.findFromUUID(EntityMedea.class, this.level, this.ownerUUID);
        return this.owner;
    }
}