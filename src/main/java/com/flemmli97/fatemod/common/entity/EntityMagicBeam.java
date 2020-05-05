package com.flemmli97.fatemod.common.entity;

import com.flemmli97.fatemod.common.handler.ConfigHandler;
import com.flemmli97.fatemod.common.handler.CustomDamageSource;
import com.flemmli97.tenshilib.common.entity.EntityBeam;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityMagicBeam extends EntityBeam{

    protected static final DataParameter<Integer> shootTime = EntityDataManager.createKey(EntityMagicBeam.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> preShootTick = EntityDataManager.createKey(EntityMagicBeam.class, DataSerializers.VARINT);

	private EntityLivingBase target;
	private int strengthMod;
    public boolean iddle=true;
	public EntityMagicBeam(World worldIn) {
		super(worldIn);
    }

	public EntityMagicBeam(World world, EntityLivingBase shootingEntity)
    {
		super(world, shootingEntity);
	}  
	
	public EntityMagicBeam(World world, EntityLivingBase shootingEntity, EntityLivingBase target, int strength)
    {
		this(world, shootingEntity);
		this.target=target;
		this.strengthMod=strength;
	}
	
	@Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(shootTime, this.rand.nextInt(20)+25);
        this.dataManager.register(preShootTick, 0);
    }
	
	/*@Override
    public int livingTickMax()
    {
        return this.dataManager.get(shootTime)+20;
    }*/
	
	@Override
	public void onUpdate() {
	    EntityLivingBase thrower = this.getShooter();
        if(this.getPreShootTick()<=this.dataManager.get(shootTime))
        {
            //this.livingTicks++;
            this.updatePreShootTick();
            if(this.getPreShootTick()==15 && this.target!=null) {
                this.setRotationTo(this.target.posX, this.target.posY, this.target.posZ, 1.2f);
            }
        }
        if(this.getPreShootTick()>this.dataManager.get(shootTime))
        {
            this.iddle=false;
            if(!this.world.isRemote)
            {
                if(thrower == null || thrower.isDead) 
                {
                    this.setDead();
                    return;
                }
            }
            super.onUpdate();
        }
	}
	
	private int getPreShootTick()
    {
        return this.dataManager.get(preShootTick);
    }
	
	private void updatePreShootTick()
    {
        this.dataManager.set(preShootTick, this.getPreShootTick()+1);
    }
    
    /*@Override
    public int livingTicks()
    {
        return Math.max(this.getPreShootTick(), this.livingTicks);
    }*/

	@Override
	protected void onImpact(RayTraceResult result) {
		result.entityHit.attackEntityFrom(CustomDamageSource.magicBeam(this, this.getShooter()), ConfigHandler.magicBeam*(1+0.25f*this.strengthMod));
	}
	
	@Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("PreShoot", this.getPreShootTick());
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.dataManager.set(preShootTick, compound.getInteger("PreShoot"));
    }
}
