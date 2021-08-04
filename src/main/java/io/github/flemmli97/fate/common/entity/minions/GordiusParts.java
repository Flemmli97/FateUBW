package io.github.flemmli97.fate.common.entity.minions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraftforge.entity.PartEntity;

public class GordiusParts extends PartEntity<EntityGordius> {

    private final EntitySize size;

    public GordiusParts(EntityGordius parent, float width, float height) {
        super(parent);
        this.size = EntitySize.flexible(width, height);
        this.recalculateSize();
    }

    @Override
    protected void registerData() {
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return this.isInvulnerableTo(source) ? false : this.getParent().attackEntityFrom(source, amount);
    }

    @Override
    public boolean isEntityEqual(Entity entityIn) {
        return this == entityIn || this.getParent() == entityIn;
    }

    @Override
    public EntitySize getSize(Pose poseIn) {
        return this.size;
    }
}
