package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.common.entity.TargetableOpponent;
import io.github.flemmli97.tenshilib.common.entity.EntityBeam;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.function.Predicate;

public abstract class BaseBeam extends EntityBeam {

    private Predicate<LivingEntity> pred = e -> !e.getUUID().equals(this.getOwnerUUID());

    public BaseBeam(EntityType<? extends BaseBeam> type, Level world) {
        super(type, world);
    }

    public BaseBeam(EntityType<? extends BaseBeam> type, Level world, double x, double y, double z) {
        super(type, world, x, y, z);
    }

    public BaseBeam(EntityType<? extends BaseBeam> type, Level world, LivingEntity shooter) {
        super(type, world, shooter);
        this.getOwner();
    }

    @Override
    protected boolean check(Entity e, Predicate<AABB> intersects) {
        return (!(e instanceof LivingEntity) || this.pred == null || this.pred.test((LivingEntity) e)) && super.check(e, intersects);
    }

    @Override
    public Entity getOwner() {
        Entity owner = super.getOwner();
        if (owner instanceof TargetableOpponent targetableOpponent)
            this.pred = targetableOpponent.validTargetPredicate();
        return owner;
    }
}
