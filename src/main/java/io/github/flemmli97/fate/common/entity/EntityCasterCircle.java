package io.github.flemmli97.fate.common.entity;

import com.flemmli97.tenshilib.api.entity.IOwnable;
import com.flemmli97.tenshilib.common.entity.EntityUtil;
import com.flemmli97.tenshilib.common.utils.MathUtils;
import io.github.flemmli97.fate.common.config.Config;
import io.github.flemmli97.fate.common.entity.servant.EntityMedea;
import io.github.flemmli97.fate.common.registry.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;
import java.util.UUID;

public class EntityCasterCircle extends Entity implements IOwnable<EntityMedea> {

    private EntityMedea owner;
    private UUID ownerUUID;
    private int livingTick;
    protected static final DataParameter<Float> range = EntityDataManager.createKey(EntityCasterCircle.class, DataSerializers.FLOAT);
    private List<float[]> circlePoints;

    public EntityCasterCircle(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public EntityCasterCircle(World world, EntityMedea owner, float r) {
        this(ModEntities.medeaCircle.get(), world);
        this.setPosition(owner.getPosX(), owner.getPosY(), owner.getPosZ());
        this.owner = owner;
        this.ownerUUID = owner.getUniqueID();
        this.dataManager.set(range, r);
    }

    @Override
    public void tick() {
        super.tick();
        this.livingTick++;
        if (this.world.isRemote && this.livingTick % 5 == 0) {
            if (this.circlePoints == null)
                this.circlePoints = MathUtils.pointsOfCircle(this.dataManager.get(range), 7);
            for (float[] f : this.circlePoints)
                for (int i = 0; i < 3; i++)
                    this.world.addParticle(ParticleTypes.WITCH, this.getPosX() + f[0], this.getPosY() + 0.2, this.getPosZ() + f[1], 0, 0.12, 0);
        }
        if (!this.world.isRemote) {
            float r = this.dataManager.get(range);
            if (this.getOwner() != null && this.getOwner().getPositionVec().squareDistanceTo(this.getPositionVec()) < r * r)
                this.getOwner().buff();
            if (this.livingTick > Config.Common.medeaCircleSpan || this.getOwner() == null || this.getOwner().getShouldBeDead())
                this.remove();
        }
    }

    @Override
    protected void registerData() {
        this.dataManager.register(range, 1f);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        this.ownerUUID = compound.getUniqueId("Owner");
        this.livingTick = compound.getInt("Ticks");
        this.dataManager.set(range, compound.getFloat("Range"));
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putUniqueId("Owner", this.ownerUUID);
        compound.putInt("Ticks", this.livingTick);
        compound.putFloat("Range", this.dataManager.get(range));
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    @Override
    public EntityMedea getOwner() {
        if (this.owner == null && this.ownerUUID != null)
            this.owner = EntityUtil.findFromUUID(EntityMedea.class, this.world, this.ownerUUID);
        return this.owner;
    }
}