package io.github.flemmli97.fateubw.common.entity;

import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.UUID;

public class MultiPartEntity extends Entity {

    private static final EntityDataAccessor<Optional<UUID>> parentUUID = SynchedEntityData.defineId(MultiPartEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Float> sizeX = SynchedEntityData.defineId(MultiPartEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> sizeY = SynchedEntityData.defineId(MultiPartEntity.class, EntityDataSerializers.FLOAT);

    private Entity parent;
    private EntityDimensions dimensions = EntityDimensions.fixed(1, 1);
    private boolean addedToLevel, isHead;

    public MultiPartEntity(EntityType<MultiPartEntity> multipartType, Level level) {
        super(multipartType, level);
    }

    public MultiPartEntity(Level level, float width, float height) {
        super(ModEntities.multipart.get(), level);
        this.setSize(width, height);
    }

    public void setParent(Entity parent) {
        this.entityData.set(parentUUID, Optional.of(parent.getUUID()));
        this.parent = parent;
    }

    public MultiPartEntity setHeadPart() {
        this.isHead = true;
        return this;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(parentUUID, Optional.empty());
        this.entityData.define(sizeX, 0f);
        this.entityData.define(sizeY, 0f);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.dimensions;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide)
            if (this.getParent() == null || !this.getParent().isAlive())
                this.remove(RemovalReason.KILLED);
        super.tick();
    }

    @Override
    public void baseTick() {
        super.baseTick();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return this.getParent() != null && this.getParent().hurt(source, amount);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        return this.getParent() != null ? this.getParent().interact(player, hand) : InteractionResult.PASS;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (this.getParent() != null && this.getParent().isInvulnerableTo(source))
            return true;
        return source == DamageSource.FALL || source == DamageSource.DROWN || (!this.isHead && source == DamageSource.IN_WALL) || super.isInvulnerableTo(source);
    }

    @SuppressWarnings("unchecked")
    public Entity getParent() {
        if (this.parent != null && this.parent.isAlive())
            return this.parent;
        this.entityData.get(parentUUID).ifPresent(uuid -> this.parent = EntityUtil.findFromUUID(Entity.class, this.level, uuid));
        return this.parent;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (sizeY.equals(key)) {
            this.setSize(this.entityData.get(sizeX), this.entityData.get(sizeY));
        }
    }

    public MultiPartEntity setSizeX(float x) {
        this.setSize(x, this.dimensions.height);
        return this;
    }

    public MultiPartEntity setSizeY(float y) {
        this.setSize(this.dimensions.width, y);
        return this;
    }

    public MultiPartEntity setSize(float x, float y) {
        if (!this.level.isClientSide) {
            this.entityData.set(sizeX, x);
            this.entityData.set(sizeY, y);
        }
        this.dimensions = EntityDimensions.fixed(x, y);
        this.refreshDimensions();
        return this;
    }

    @Override
    public boolean isPickable() {
        return this.getParent() != null;
    }

    @Override
    public void setLevelCallback(EntityInLevelCallback entityInLevelCallback) {
        super.setLevelCallback(entityInLevelCallback);
        this.addedToLevel = true;
    }

    public boolean isAddedToLevel() {
        return this.addedToLevel;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public void updatePositionTo(double x, double y, double z, boolean simple) {
        Vec3 old = this.position();
        this.setOldPosAndRot();
        if (simple)
            this.setPos(x, y, z);
        else {
            this.setOnGround(true);
            double vy = y - old.y;
            if (vy >= 0 && vy < 1.5) {
                if (vy <= 1)
                    vy = -0.08;
                else
                    vy = 0;
            }
            this.move(MoverType.SELF, new Vec3(x - old.x, vy, z - old.z));
        }
    }
}
