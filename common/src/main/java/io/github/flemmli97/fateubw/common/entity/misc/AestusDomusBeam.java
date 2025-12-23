package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.network.S2CScreenShake;
import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import io.github.flemmli97.fateubw.common.registry.FateSounds;
import io.github.flemmli97.fateubw.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.entity.EntityUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.UUID;

public class AestusDomusBeam extends Entity implements OwnableEntity {

    public static final int START_DELAY = 4;

    private LivingEntity owner;
    private UUID ownerUUID;
    private int livingTick;

    private LivingEntity target;

    public AestusDomusBeam(EntityType<?> type, Level level) {
        super(type, level);
    }

    public AestusDomusBeam(Level level, LivingEntity owner, LivingEntity target) {
        this(FateEntities.AESTUS_DOMUS_BEAM.get(), level);
        this.setPos(target.getX(), target.getY(), target.getZ());
        this.target = target;
        this.owner = owner;
        this.ownerUUID = owner.getUUID();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public void tick() {
        super.tick();
        this.livingTick++;
        if (!this.level().isClientSide) {
            if (this.livingTick > 30) {
                this.discard();
                return;
            }
            if (this.target != null && this.livingTick > START_DELAY && this.livingTick < 21) {
                if (this.livingTick % 4 == 1) {
                    AABB hitBB = this.getBoundingBox().inflate(1.9, 8, 1.9);
                    if (hitBB.intersects(this.target.getBoundingBox())) {
                        if (Utils.runWithInvulTimer(this.getOwner(), this.target, t ->
                                t.hurt(FateDamageTypes.indirect(FateDamageTypes.AESTUS_DOMUS, this, this.getOwner()), CommonConfig.aestusDomusDamage), 0)) {
                            S2CScreenShake.sendAround(this, 32, 8, 2);
                            this.playSound(FateSounds.AESTUS_DOMUS_IMPACT.get(), 2, 1);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.hasUUID("Owner")) {
            this.ownerUUID = compound.getUUID("Owner");
        }
        this.livingTick = compound.getInt("LivingTicks");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        if (this.ownerUUID != null) {
            compound.putUUID("Owner", this.ownerUUID);
        }
        compound.putInt("LivingTicks", this.livingTick);
    }

    @Override
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    @Override
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null)
            this.owner = EntityUtils.findFromUUID(LivingEntity.class, this.level(), this.ownerUUID);
        return this.owner;
    }
}