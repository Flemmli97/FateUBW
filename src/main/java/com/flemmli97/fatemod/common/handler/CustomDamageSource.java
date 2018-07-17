package com.flemmli97.fatemod.common.handler;

import com.flemmli97.fatemod.common.entity.EntityGordiusWheel;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;

public class CustomDamageSource extends DamageSource{

	public CustomDamageSource(String source) {
		super(source);
	}

	public static DamageSource excalibur(Entity source, EntityLivingBase sourceEntity)
    {
        return (new EntityDamageSourceIndirect("excalibur", source, sourceEntity)).setDamageBypassesArmor().setMagicDamage();
    }
	
	public static DamageSource babylon(Entity source, EntityLivingBase sourceEntity)
    {
        return (new EntityDamageSourceIndirect("babylon", source, sourceEntity));
    }
	
	public static DamageSource gaeBolg(Entity source, EntityLivingBase sourceEntity)
    {
        return (new EntityDamageSourceIndirect("gaeBolg", source, sourceEntity)).setDamageBypassesArmor().setMagicDamage();
    }
	
	public static DamageSource caladBolg(Entity source, EntityLivingBase sourceEntity)
    {
        return (new EntityDamageSourceIndirect("caladBolg", source, sourceEntity)).setDamageBypassesArmor().setMagicDamage();
    }

	public static DamageSource archerNormal(Entity source, EntityLivingBase sourceEntity)
    {
        return (new EntityDamageSourceIndirect("arrow", source, sourceEntity)).setProjectile();
    }
	
	public static DamageSource magicBeam(Entity source, EntityLivingBase sourceEntity)
    {
        return (new EntityDamageSourceIndirect("beam", source, sourceEntity));
    }
	
	public static DamageSource hiKen(EntityLivingBase sourceEntity)
    {
        return (new EntityDamageSource("tsubame",sourceEntity)).setDamageBypassesArmor();
    }
	
	public static DamageSource gordiusTrample(EntityGordiusWheel source, EntityLivingBase sourceEntity)
    {
        return (new EntityDamageSourceIndirect("gordius", source, sourceEntity));
    }
}
