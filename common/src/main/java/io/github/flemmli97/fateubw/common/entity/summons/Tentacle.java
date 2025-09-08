package io.github.flemmli97.fateubw.common.entity.summons;

import io.github.flemmli97.fateubw.api.datapack.ServantExtraData;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.network.S2CAttackDebug;
import io.github.flemmli97.fateubw.common.network.S2CScreenShake;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.tenshilib.common.entity.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;
import java.util.function.Predicate;

public class Tentacle extends Entity implements AnimatedEntity, TraceableEntity {

    public static final float SCALE = 1.75f;

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String SLAM = BUILDER.add("slam", AnimationsBuilder.definition(2.4)
            .marker("attack", 1.56).marker("can_despawn", 1.84));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<Tentacle> animationHandler = new AnimationHandler<>(this, ANIMS);

    private UUID ownerUuid;
    private LivingEntity owner;

    public Tentacle(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.noCulling = true;
    }

    public Tentacle setOwner(LivingEntity owner) {
        this.owner = owner;
        if (this.owner != null) {
            this.ownerUuid = this.owner.getUUID();
        } else {
            this.ownerUuid = null;
        }
        return this;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.hasUUID("Owner")) {
            this.ownerUuid = compound.getUUID("Owner");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        if (this.ownerUuid != null) {
            compound.putUUID("Shooter", this.ownerUuid);
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d = this.getBoundingBoxForCulling().getSize();
        if (Double.isNaN(d)) {
            d = 1.0F;
        }
        d *= 64.0F * getViewScale();
        return distance < d * d;
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return super.getBoundingBoxForCulling().inflate(2 * SCALE, 4 * SCALE, 2 * SCALE);
    }

    @Override
    public void baseTick() {
        if (!this.level().isClientSide) {
            if (!this.getAnimationHandler().hasAnimation()) {
                this.remove(RemovalReason.KILLED);
                return;
            }
        }
        super.baseTick();
        this.getAnimationHandler().tick();
        if (!this.level().isClientSide) {
            AnimationState anim = this.getAnimationHandler().getAnimation();
            if (anim != null && anim.is(SLAM)) {
                if (anim.isAt("attack")) {
                    double width = 1.7 * SCALE;
                    double len = 4.5 * SCALE;
                    double height = 2.2 * SCALE;
                    AABB aabb = new AABB(-width * 0.5, -0.03, 0, width * 0.5, height + 0.003, len);
                    OrientedBoundingBox obb = new OrientedBoundingBox(aabb, -this.getYRot(), 0, this.position());
                    S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, this);
                    float damage = DatapackHandler.SERVANT_PROPS.get(FateEntities.GILLES.get())
                            .getConfig(ServantExtraData.GILLES_TENTACLE_DAMAGE);
                    Predicate<LivingEntity> pred = this.getOwner() instanceof BaseServant servant ? servant.targetPred : e -> true;
                    this.level().getEntitiesOfClass(LivingEntity.class, obb.getEncompassingBox(),
                                    entity -> pred.test(entity) && obb.intersects(entity.getBoundingBox()))
                            .forEach(e -> e.hurt(this.damageSources().mobProjectile(this, this.getOwner()), damage));
                    S2CScreenShake.sendAround(this, 16, 8, 4);
                    this.playSound(SoundEvents.GENERIC_EXPLODE.value(), 1.0f, 0.7f);
                }
            }
        }
    }

    @Override
    public LivingEntity getOwner() {
        if (this.owner != null && !this.owner.isRemoved()) {
            return this.owner;
        } else if (this.ownerUuid != null) {
            this.owner = EntityUtils.findFromUUID(LivingEntity.class, this.level(), this.ownerUuid);
        }
        return this.owner;
    }

    @Override
    public AnimationHandler<?> getAnimationHandler() {
        return this.animationHandler;
    }

    public void setup(Vec3 target) {
        this.getAnimationHandler().setAnimation(Tentacle.SLAM);
        Vec3 dir = target.subtract(this.position());
        if (dir.lengthSqr() < 0.0001) {
            dir = new Vec3(1, 0, 0);
        } else {
            dir = new Vec3(dir.x(), 0, dir.z()).normalize();
        }
        dir = dir.scale(3).yRot(this.getRandom().nextFloat() * Mth.TWO_PI);
        this.setPos(this.position().add(dir));
        this.setDeltaMovement(dir);
        double f = Math.sqrt(dir.horizontalDistance());
        this.setYRot((float) (Mth.atan2(dir.x, dir.z) * (180 / Math.PI)) - 180);
        this.setXRot((float) (Mth.atan2(dir.y, f) * (180 / Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    public float getDespawnProgress(float partialTicks) {
        AnimationState anim = this.getAnimationHandler().getAnimation();
        if (anim == null)
            return 1;
        if (!anim.isPast("can_despawn"))
            return -1;
        return (float) anim.progress(anim.getMarker("can_despawn", 0) * 20, anim.getLength(), partialTicks, 0);
    }
}
