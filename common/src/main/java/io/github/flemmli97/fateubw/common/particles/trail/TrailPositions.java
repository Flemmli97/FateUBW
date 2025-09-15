package io.github.flemmli97.fateubw.common.particles.trail;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class TrailPositions {

    private final TrailPosition[] positions;
    private final int length;

    /**
     * The index of the first element
     */
    private int head;
    /**
     * The index of the last added element
     */
    private int tail;
    private int size;

    private AABB bounds;
    private double lastScale = 1;

    public TrailPositions(int length) {
        this(length, null);
    }

    public TrailPositions(int length, TrailPosition init) {
        this.length = length;
        this.positions = new TrailPosition[length];
        if (init != null) {
            this.add(init);
        }
    }

    public void add(Vec3 pos) {
        this.add(pos, null);
    }

    public void add(Vec3 pos, Vec3 normal) {
        this.add(pos != null ? new TrailPosition(pos, normal) : null);
    }

    public void add(@Nullable TrailPosition pos) {
        this.tail = this.size == 0 ? 0 : this.nextTail();
        this.positions[this.tail] = pos;
        if (this.size == this.length) {
            this.head = this.nextTail();
        }
        this.size = Math.min(this.size + 1, this.length);
        this.calculateBounds(this.lastScale);
    }

    public void replaceLast(@Nullable TrailPosition pos) {
        this.positions[this.tail] = pos;
        this.calculateBounds(this.lastScale);
    }

    public void removeHead() {
        this.positions[this.head] = null;
        this.head = (this.head + 1) % this.positions.length;
        this.size = Math.max(this.size - 1, 0);
        this.calculateBounds(this.lastScale);
    }

    @Nullable
    public TrailPosition getFirst() {
        return this.positions[this.head];
    }

    @Nullable
    public TrailPosition getLast() {
        return this.positions[this.tail];
    }

    public int getLength() {
        return this.length;
    }

    public int size() {
        return this.size;
    }

    public AABB getBounds(double scale) {
        if (this.bounds == null || this.lastScale != scale) {
            this.calculateBounds(scale);
        }
        return this.bounds;
    }

    private void calculateBounds(double scale) {
        double minX = 0, minY = 0, minZ = 0, maxX = 0, maxY = 0, maxZ = 0;
        double nX = 0, nY = 1, nZ = 0;
        boolean firstBB = false, firstNormal = false;
        for (TrailPosition pos : this.positions) {
            if (pos == null)
                continue;
            if (!firstBB) {
                minX = pos.pos().x();
                minY = pos.pos().y();
                minZ = pos.pos().z();
                maxX = pos.pos().x();
                maxY = pos.pos().y();
                maxZ = pos.pos().z();
                firstBB = true;
            } else {
                minX = Math.min(minX, pos.pos().x());
                minY = Math.min(minY, pos.pos().y());
                minZ = Math.min(minZ, pos.pos().z());
                maxX = Math.max(maxX, pos.pos().x());
                maxY = Math.max(maxY, pos.pos().y());
                maxZ = Math.max(maxZ, pos.pos().z());
            }
            if (pos.normal() != null) {
                if (!firstNormal) {
                    firstNormal = true;
                    nX = pos.normal().x();
                    nY = pos.normal().y();
                    nZ = pos.normal().z();
                } else {
                    nX = Math.max(nX, pos.normal().x());
                    nY = Math.max(nY, pos.normal().y());
                    nZ = Math.max(nZ, pos.normal().z());
                }
            }
        }
        this.bounds = new AABB(minX - nX * scale, minY - nY * scale, minZ - nZ * scale,
                maxX + nX * scale, maxY + nY * scale, maxZ + nZ * scale);
        this.lastScale = scale;
    }

    @Nullable
    public TrailPosition getAt(int i) {
        if (i >= this.size() || i < 0)
            return null;
        return this.positions[(this.head + i) % this.positions.length];
    }

    private int nextTail() {
        return (this.tail + 1) % this.positions.length;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.positions);
    }

    public record TrailPosition(Vec3 pos, @Nullable Vec3 normal) {

    }
}
