package io.github.flemmli97.fateubw.common.entity;

import io.github.flemmli97.fateubw.common.network.S2CMultipartDataPkt;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
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

    private static final EntityDataAccessor<Optional<UUID>> PARENT_UUID = SynchedEntityData.defineId(MultiPartEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Float> SIZE_X = SynchedEntityData.defineId(MultiPartEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> SIZE_Y = SynchedEntityData.defineId(MultiPartEntity.class, EntityDataSerializers.FLOAT);

    private Entity parent;
    private EntityDimensions dimensions = EntityDimensions.fixed(1, 1);
    private boolean addedToLevel, isHead;

    private boolean smoothMovement;
    private Position offset = Position.DEFAULT;

    // Vanilla is not lerping correctly
    public float viewYRot, viewYRotO, viewXRot, viewXRotO;

    public MultiPartEntity(EntityType<MultiPartEntity> multipartType, Level level) {
        super(multipartType, level);
        this.setNoGravity(true);
    }

    public MultiPartEntity(Level level, float width, float height, Position offset) {
        this(FateEntities.MULTIPART.get(), level);
        this.setSize(width, height);
        this.offset = offset;
    }

    public void setParent(Entity parent) {
        this.entityData.set(PARENT_UUID, Optional.of(parent.getUUID()));
        this.parent = parent;
    }

    public MultiPartEntity setHeadPart() {
        this.isHead = true;
        return this;
    }

    public MultiPartEntity smoothMovement() {
        this.smoothMovement = true;
        return this;
    }

    public MultiPartEntity gravity() {
        this.setNoGravity(false);
        return this;
    }

    public void setOffset(Position offset) {
        this.offset = offset;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(PARENT_UUID, Optional.empty());
        this.entityData.define(SIZE_X, 0f);
        this.entityData.define(SIZE_Y, 0f);
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
        if (this.getParent() == null || !this.getParent().isAlive()) {
            if (!this.level.isClientSide) {
                this.remove(RemovalReason.KILLED);
            }
            return;
        }
        this.viewXRotO = this.viewXRot;
        this.viewYRotO = this.viewYRot;
        super.tick();
        float parentRotY = Mth.wrapDegrees(this.getParent().getYHeadRot());
        Vec3 anchorOffset = MathUtils.rotate(new Vec3(0, 1, 0), this.offset.anchorOffset, -parentRotY * Mth.DEG_TO_RAD);
        Vec3 anchor = this.getParent().position().add(anchorOffset);
        Vec3 dir = anchor.subtract(this.position());
        Vec3 delta = this.getDeltaMovement().scale(0.3);
        if (this.smoothMovement) {
            double dirLen = dir.lengthSqr();
            double offLen = this.offset.positionOffset.lengthSqr();
            if (dirLen > offLen * 5) {
                Vec3 offset = MathUtils.rotate(new Vec3(0, 1, 0), this.offset.positionOffset, -parentRotY * Mth.DEG_TO_RAD);
                this.updatePositionTo(anchor.x() + offset.x, anchor.y(), anchor.z() + offset.z, true);
            } else {
                if (dirLen > offLen || Math.abs(this.getParent().getY() - this.getY()) > 1.5) {
                    this.setOldPosAndRot();
                    delta = dir.scale(0.2).add(0, -0.08, 0);
                }
            }
        } else {
            Vec3 offset = MathUtils.rotate(new Vec3(0, 1, 0), this.offset.positionOffset, -parentRotY * Mth.DEG_TO_RAD);
            this.updatePositionTo(anchor.x() + offset.x, anchor.y(), anchor.z() + offset.z, true);
        }
        this.hasImpulse = true;
        double d = dir.horizontalDistance();
        float yRot = -(float) (Mth.atan2(dir.x, dir.z) * Mth.RAD_TO_DEG);
        yRot = Mth.wrapDegrees(yRot - this.getYRot());
        this.setRot(this.getYRot() + Mth.clamp(yRot, -8, 8), -(float) (Mth.atan2(dir.y, d) * Mth.RAD_TO_DEG));
        this.setViewRotation(this.getYRot(), this.getXRot());
        this.setDeltaMovement(delta);
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    public void forceUpdatePosition() {
        Vec3 offset = MathUtils.rotate(new Vec3(0, 1, 0), this.offset.positionOffset, -this.getParent().getYRot() * Mth.DEG_TO_RAD);
        Vec3 anchorOffset = MathUtils.rotate(new Vec3(0, 1, 0), this.offset.anchorOffset, -this.getParent().getYRot() * Mth.DEG_TO_RAD);
        Vec3 anchor = this.getParent().position().add(anchorOffset);
        this.updatePositionTo(anchor.x() + offset.x, anchor.y(), anchor.z() + offset.z, true);
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
    public boolean isInvulnerableTo(DamageSource source) {
        if (this.getParent() != null && this.getParent().isInvulnerableTo(source))
            return true;
        return source == DamageSource.FALL || source == DamageSource.DROWN || (!this.isHead && source == DamageSource.IN_WALL) || super.isInvulnerableTo(source);
    }

    public Entity getParent() {
        if (this.parent != null && this.parent.isAlive())
            return this.parent;
        this.entityData.get(PARENT_UUID).ifPresent(uuid -> this.parent = EntityUtil.findFromUUID(Entity.class, this.level, uuid));
        return this.parent;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (SIZE_Y.equals(key)) {
            this.setSize(this.entityData.get(SIZE_X), this.entityData.get(SIZE_Y));
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
            this.entityData.set(SIZE_X, x);
            this.entityData.set(SIZE_Y, y);
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

    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        LoaderNetwork.INSTANCE.sendToPlayer(new S2CMultipartDataPkt(this.getId(), this.offset, this.smoothMovement), serverPlayer);
    }

    public void setViewRotation(float rotY, float rotX) {
        this.viewYRot = rotY % 360;
        this.viewXRot = rotX % 360;
    }

    public record Position(Vec3 anchorOffset, Vec3 positionOffset) {
        public static final Position DEFAULT = new Position(Vec3.ZERO, Vec3.ZERO);
    }
}
