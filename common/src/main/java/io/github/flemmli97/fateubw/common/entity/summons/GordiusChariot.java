package io.github.flemmli97.fateubw.common.entity.summons;

import io.github.flemmli97.fateubw.common.entity.MultiPartEntity;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.utils.MathsHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class GordiusChariot extends MultiPartEntity implements OwnableEntity {

    private static final double CHARIOT_JOINT_DIST = 1.6;

    public GordiusChariot(EntityType<GordiusChariot> multipartType, Level level) {
        super(multipartType, level);
    }

    public GordiusChariot(LivingEntity parent, float width, float height) {
        super(FateEntities.GORDIUS_CHARIOT.get(), parent, width, height);
    }

    @Override
    public boolean parentTick() {
        if (this.getOwner() != null && !this.getOwner().level().isClientSide && !this.isEntityAddedToLevel()) {
            Vec3 wheelJoint = this.getOwner().getWheelJoint();
            Vec3 dir = wheelJoint.subtract(this.getOwner().position()).normalize().scale(CHARIOT_JOINT_DIST);
            this.setPos(wheelJoint.x() + dir.x(), wheelJoint.y() + dir.y(), wheelJoint.z() + dir.z());
            this.level().addFreshEntity(this);
            return true;
        }
        return false;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level().isClientSide()) {
            Vec3 lookDir = this.directionToLookAt();
            if (lookDir != null) {
                float[] yxRot = MathsHelper.YXRotFrom(lookDir);
                this.setYRot(MathsHelper.rotlerp(this.getYRot(), yxRot[0], 15));
                this.setXRot(MathsHelper.rotlerp(this.getXRot(), yxRot[1], 15));
                this.setYBodyRot(this.getYRot());
                this.setYHeadRot(this.getYRot());
            }
        }
    }

    private Vec3 directionToLookAt() {
        LivingEntity owner = this.getOwner();
        if (owner != null)
            return this.getOwner().getWheelJoint().subtract(this.position());
        return null;
    }

    @Override
    protected void updatePosition() {
        Vec3 wheelJoint = this.getOwner().getWheelJoint();
        Vec3 offsetDir = wheelJoint.subtract(this.position());
        double maxDist = CHARIOT_JOINT_DIST * this.getOwner().getScale();
        Vec3 target = wheelJoint.subtract(offsetDir.normalize().scale(maxDist));
        if (this.position().distanceToSqr(target) > 6) {
            this.teleportTo(target.x(), target.y(), target.z());
        } else {
            this.moveTo(target.x(), target.y(), target.z(), false);
        }
    }

    @Override
    public float maxUpStep() {
        return this.getOwner().maxUpStep();
    }

    @Override
    public void setYRot(float yRot) {
        super.setYRot(Mth.wrapDegrees(yRot));
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float partialTick) {
        float scale = this.getOwner() != null ? this.getOwner().getScale() : 1;
        return new Vec3(0, (17. / 16) * scale + entity.getVehicleAttachmentPoint(this.getOwner()).y(), -0.15)
                .yRot(-this.getYRot() * Mth.DEG_TO_RAD);
    }

    @Override
    public GordiusWheel getOwner() {
        LivingEntity sup = super.getOwner();
        if (sup instanceof GordiusWheel wheel)
            return wheel;
        return null;
    }
}
