package com.flemmli97.fatemod.common.entity;

import java.util.List;

import com.flemmli97.fatemod.common.handler.CustomDamageSource;
import com.flemmli97.fatemod.network.CustomDataPacket;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityExcalibur extends EntitySpecialProjectile{

    protected static final DataParameter<Double> hitX = EntityDataManager.<Double>createKey(EntityExcalibur.class, CustomDataPacket.Double);
    protected static final DataParameter<Double> hitY = EntityDataManager.<Double>createKey(EntityExcalibur.class, CustomDataPacket.Double);
    protected static final DataParameter<Double> hitZ = EntityDataManager.<Double>createKey(EntityExcalibur.class, CustomDataPacket.Double);
    protected static final DataParameter<Float> rotPitch = EntityDataManager.<Float>createKey(EntityExcalibur.class, DataSerializers.FLOAT);
    protected static final DataParameter<Float> rotYaw = EntityDataManager.<Float>createKey(EntityExcalibur.class, DataSerializers.FLOAT);

    private RayTraceResult hitResult;
    public EntityExcalibur(World worldIn)
    {
        super(worldIn);
        this.ignoreFrustumCheck=true;
        this.range=1.2F;
        this.livingTickMax=50;
    }

    public EntityExcalibur(World worldIn, EntityLivingBase shooter)
    {
        super(worldIn, shooter, true, true);
        this.dataManager.set(rotPitch, shooter.rotationPitch);
        this.dataManager.set(rotYaw, shooter.rotationYaw);
        this.ignoreFrustumCheck=true;
        this.range=1.2F;
        this.livingTickMax=50;
        this.resetPositionToBB();
        hitResult = this.entityRayTrace(48);
        this.dataManager.set(hitX, hitResult.hitVec.xCoord);
        this.dataManager.set(hitY, hitResult.hitVec.yCoord);
        this.dataManager.set(hitZ, hitResult.hitVec.zCoord);
    }

	@Override
	protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(hitX, 0.0);
        this.dataManager.register(hitY, 0.0);
        this.dataManager.register(hitZ, 0.0);
        this.dataManager.register(rotPitch, 0.0F);
        this.dataManager.register(rotYaw, 0.0F);
    }
	
	/**0 = yaw, 1 = pitch*/
	public float[] getRotation()
	{
		return new float[] {this.dataManager.get(rotYaw),this.dataManager.get(rotPitch)};
	}
    
    public double[] getHitPos()
    {
    		return new double[]{this.dataManager.get(hitX), this.dataManager.get(hitY), this.dataManager.get(hitZ)};
    }
    
	@Override
	public void onUpdate() {
		super.onUpdate();
		if(hitResult!=null)
		{
			//min max
			Vec3d scaler = this.hitResult.hitVec.normalize().scale(this.range);
			List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, 
					new AxisAlignedBB(Math.min(this.posX,this.hitResult.hitVec.xCoord+scaler.xCoord),Math.min(this.posY,this.hitResult.hitVec.yCoord+scaler.yCoord),Math.min(this.posZ,this.hitResult.hitVec.zCoord+scaler.zCoord), 
							Math.max(this.posX,this.hitResult.hitVec.xCoord+scaler.xCoord),Math.max(this.posY,this.hitResult.hitVec.yCoord+scaler.yCoord),Math.max(this.posZ,this.hitResult.hitVec.zCoord+scaler.zCoord)));
			for (int i = 0; i < list.size(); ++i)
	        {
				Entity entity1 = (Entity)list.get(i);
	            if (entity1.canBeCollidedWith())
	            {
	            		if(!list.isEmpty() && entity1 instanceof EntityLivingBase && !this.ignore.contains(entity1))
	        			{
        					EntityLivingBase living = (EntityLivingBase) entity1;

	            			double range = this.range + 0.30000001192092896D;
	            			AxisAlignedBB axisalignedbb = living.getEntityBoundingBox().expandXyz(range);

	        				RayTraceResult raytraceresult1 = axisalignedbb.calculateIntercept(this.getPositionVector(), new Vec3d(this.dataManager.get(hitX), this.dataManager.get(hitY), this.dataManager.get(hitZ)));
	        				if (raytraceresult1 != null)
	        				{
	        					RayTraceResult raytraceresult = new RayTraceResult(living);
	        					this.onImpact(raytraceresult);
	                    		this.ignore.add(living);
	        				}
	        			} 
	            }
	        }
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.worldObj.isRemote)
        {
    			if (result.entityHit != null && result.entityHit != this.getThrower())
            {
    			result.entityHit.attackEntityFrom(CustomDamageSource.excalibur(this, this.getThrower()), 19.0F);
            }
        }
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setDouble("endX", this.dataManager.get(hitX));
		compound.setDouble("endY", this.dataManager.get(hitY));
		compound.setDouble("endZ", this.dataManager.get(hitZ));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.dataManager.set(hitX, compound.getDouble("endX"));
		this.dataManager.set(hitY, compound.getDouble("endY"));
		this.dataManager.set(hitZ, compound.getDouble("endZ"));
	}

	@Override
	protected float getGravityVelocity() {
		return 0;
	}
}
