package com.flemmli97.fatemod.common.entity;

import com.flemmli97.fatemod.common.handler.CustomDamageSource;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
//IEntityAdditionalSpawnData, IThrowableEntity

public class EntityArcherArrow extends EntityArrow implements IEntityAdditionalSpawnData{
	
	private int knockbackStrength;

	public EntityArcherArrow(World world) 
	{
		super(world);
	}
	
	public EntityArcherArrow(World world, EntityLivingBase shootingEntity)
    {
        super(world, shootingEntity);
    }

	@Override
	protected ItemStack getArrowStack() 
	{
		return ItemStack.EMPTY;
	}

	@Override
	protected void onHit(RayTraceResult result) 
	{
		Entity hit = result.entityHit;
		if(hit!=null)
		{
            float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
            int i = MathHelper.ceil((double)f * this.getDamage())+10;
            if (this.getIsCritical())
            {
                i += this.rand.nextInt(i / 2 + 2);
            }
           //bow max 25 dmg, 16 without crit, emiya 21-22
            if (hit.attackEntityFrom(CustomDamageSource.archerNormal(this, (EntityLivingBase) this.shootingEntity), i))
            {
            	if (hit instanceof EntityLivingBase)
                {
                    EntityLivingBase entitylivingbase = (EntityLivingBase)hit;
                    if (this.knockbackStrength > 0)
                    {
                        float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

                        if (f1 > 0.0F)
                        {
                            entitylivingbase.addVelocity(this.motionX * (double)this.knockbackStrength * 0.6000000238418579D / (double)f1, 0.1D, this.motionZ * (double)this.knockbackStrength * 0.6000000238418579D / (double)f1);
                        }
                    }
                }
                this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                this.setDead();
            } 
            else
            {
                this.motionX *= -0.10000000149011612D;
                this.motionY *= -0.10000000149011612D;
                this.motionZ *= -0.10000000149011612D;
                this.rotationYaw += 180.0F;
                this.prevRotationYaw += 180.0F;
            }
		}
		else
		{
            super.onHit(result);
		}
	}
	
	@Override
	public void setKnockbackStrength(int knockbackStrengthIn)
    {
        this.knockbackStrength = knockbackStrengthIn;
    }
	
	

	@Override
	public void writeSpawnData(ByteBuf buf) {
		if(this.shootingEntity!=null)
		{
			buf.writeInt(this.shootingEntity.getEntityId());
		}
	}

	@Override
	public void readSpawnData(ByteBuf buf) {
		int x = buf.readInt();
		if(x!=0)
		{
			Entity shooter = world.getEntityByID(x);
			if (shooter instanceof EntityLivingBase) 
			{
				shootingEntity = (EntityLivingBase) shooter;
			}
		}
	}
		
}
