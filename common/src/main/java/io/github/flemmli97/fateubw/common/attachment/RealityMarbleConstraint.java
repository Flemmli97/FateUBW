package io.github.flemmli97.fateubw.common.attachment;

import io.github.flemmli97.fateubw.common.network.S2CRealityMarbleConstraint;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Objects;
import java.util.Optional;

public class RealityMarbleConstraint {

    private final Entity entity;

    private BlockPos center;
    private double radius;

    private VoxelShape shape;
    private AABB bounds;

    public RealityMarbleConstraint(Entity entity) {
        this.entity = entity;
    }

    public void clearConstraints() {
        this.setConstraint(null, 0);
    }

    public void setConstraint(BlockPos center, double radius) {
        if (Objects.equals(center, this.center) && radius == this.radius) {
            return;
        }
        this.center = center;
        this.radius = radius;
        if (this.center != null) {
            VoxelShape inner = Shapes.box(
                    this.center.getX() - this.radius,
                    Double.NEGATIVE_INFINITY,
                    this.center.getZ() - this.radius,
                    this.center.getX() + this.radius,
                    Double.POSITIVE_INFINITY,
                    this.center.getZ() + this.radius
            );
            this.shape = Shapes.join(
                    Shapes.INFINITY,
                    inner,
                    BooleanOp.ONLY_FIRST
            );
            this.bounds = inner.bounds();
        } else {
            this.shape = null;
            this.bounds = null;
        }
        if (!this.entity.level().isClientSide()) {
            LoaderNetwork.INSTANCE.sendToTracking(new S2CRealityMarbleConstraint(this.entity), this.entity);
        }
    }

    private boolean isInsideCloseToBorder(Entity entity, AABB bounds) {
        if (this.bounds == null) {
            return true;
        }
        double offset = Math.max(Mth.absMax(bounds.getXsize(), bounds.getZsize()), 1.0);
        return this.getDistanceToBorder(entity.getX(), entity.getZ()) < offset * 2.0 && this.isWithinBounds(entity.getX(), entity.getZ(), offset);
    }

    private double getDistanceToBorder(double x, double z) {
        double dZMax = z - this.bounds.minZ;
        double dZMin = this.bounds.maxZ - z;
        double dxMin = x - this.bounds.minX;
        double dxMax = this.bounds.maxX - x;
        double dist = Math.min(dxMin, dxMax);
        dist = Math.min(dist, dZMax);
        return Math.min(dist, dZMin);
    }

    private boolean isWithinBounds(double x, double z, double offset) {
        return x >= this.bounds.minX - offset
                && x < this.bounds.maxX + offset
                && z >= this.bounds.minZ - offset
                && z < this.bounds.maxZ + offset;
    }

    public VoxelShape getShape(AABB boundingBox) {
        if (!this.isInsideCloseToBorder(this.entity, boundingBox)) {
            return null;
        }
        return this.shape;
    }

    public AABB bounds() {
        return this.bounds;
    }

    public boolean inside() {
        if (this.bounds == null) {
            return true;
        }
        AABB aabb = this.entity.getBoundingBox();
        double x = this.entity.getX();
        double z = this.entity.getZ();
        double dx = Math.max(0, aabb.getXsize() * 0.5 - 0.2);
        double dz = Math.max(0, aabb.getZsize() * 0.5 - 0.2);
        return x >= this.bounds.minX + dx
                && x <= this.bounds.maxX - dx
                && z >= this.bounds.minZ + dz
                && z <= this.bounds.maxZ - dz;
    }

    public Data pack() {
        return new Data(Optional.ofNullable(this.center), this.radius);
    }

    public record Data(Optional<BlockPos> center, double radius) {

        public static final StreamCodec<ByteBuf, Data> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.optional(BlockPos.STREAM_CODEC), Data::center,
                ByteBufCodecs.DOUBLE, Data::radius, Data::new);
    }
}
