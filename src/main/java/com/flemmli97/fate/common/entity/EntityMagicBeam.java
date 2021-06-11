package com.flemmli97.fate.common.entity;

import com.flemmli97.fate.common.config.Config;
import com.flemmli97.fate.common.registry.ModEntities;
import com.flemmli97.fate.common.utils.CustomDamageSource;
import com.flemmli97.tenshilib.common.entity.EntityBeam;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;

public class EntityMagicBeam extends EntityBeam {

    protected static final DataParameter<Integer> shootTime = EntityDataManager.createKey(EntityMagicBeam.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> preShootTick = EntityDataManager.createKey(EntityMagicBeam.class, DataSerializers.VARINT);

    private LivingEntity target;
    private int strengthMod;
    public boolean iddle = true;

    public EntityMagicBeam(EntityType<? extends EntityMagicBeam> type, World world) {
        super(type, world);
    }

    public EntityMagicBeam(World world, LivingEntity shooter) {
        super(ModEntities.magicBeam.get(), world, shooter);
    }

    public EntityMagicBeam(World world, LivingEntity shootingEntity, LivingEntity target, int strength) {
        this(world, shootingEntity);
        this.target = target;
        this.strengthMod = strength;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(shootTime, this.rand.nextInt(20) + 25);
        this.dataManager.register(preShootTick, 0);
    }

    @Override
    public void tick() {
        LivingEntity thrower = this.getOwner();
        if (this.getPreShootTick() <= this.dataManager.get(shootTime)) {
            //this.livingTicks++;
            this.updatePreShootTick();
            if (this.getPreShootTick() == 15 && this.target != null) {
                this.setRotationTo(this.target.getPosX(), this.target.getPosY(), this.target.getPosZ(), 1.2f);
            }
        }
        if (this.getPreShootTick() > this.dataManager.get(shootTime)) {
            this.iddle = false;
            if (!this.world.isRemote) {
                if (thrower == null || !thrower.isAlive()) {
                    this.remove();
                    return;
                }
            }
            super.tick();
        }
    }

    @Override
    public void onImpact(EntityRayTraceResult result) {
        result.getEntity().attackEntityFrom(CustomDamageSource.magicBeam(this, this.getOwner()), Config.Common.magicBeam);
    }

    private int getPreShootTick() {
        return this.dataManager.get(preShootTick);
    }

    private void updatePreShootTick() {
        this.dataManager.set(preShootTick, this.getPreShootTick() + 1);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("PreShoot", this.getPreShootTick());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.dataManager.set(preShootTick, compound.getInt("PreShoot"));
    }
}