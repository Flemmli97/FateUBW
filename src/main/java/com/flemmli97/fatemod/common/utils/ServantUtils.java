package com.flemmli97.fatemod.common.utils;

import java.util.List;
import java.util.UUID;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.handler.TruceMapHandler;

import net.minecraft.entity.player.EntityPlayer;

public class ServantUtils {

	public static boolean testNearbyEnemy(EntityServant servant)
	{
		List<?> var1 = servant.world.getEntitiesWithinAABB(EntityServant.class, servant.getEntityBoundingBox().expand((double)32, 3.0D, (double)32));
		List<?> var2 = servant.world.getEntitiesWithinAABB(EntityServant.class, servant.getEntityBoundingBox().expand((double)15, 3.0D, (double)15));
		
		if (var1.size() > 1)
		{
			if(var2.size() < 2)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
		
	}

	public static float getDamageAfterMagicAbsorb(EntityServant servant, float damage)
    {
        return (float) (damage * servant.getEntityAttribute(EntityServant.MAGIC_RESISTANCE).getAttributeValue());
    }
	
	public static float projectileReduce(EntityServant servant, float damage)
	{
		float reduceAmount = (float) (servant.getEntityAttribute(EntityServant.PROJECTILE_RESISTANCE).getAttributeValue()*0.04);
		return damage*reduceAmount;
	}
	
	public static boolean inSameTeam(EntityPlayer player, EntityPlayer other)
	{
		return TruceMapHandler.get(player.world).playerTruces(player).contains(other.getUniqueID());
	}
	
	public static boolean inSameTeam(EntityPlayer player, EntityServant servant)
	{
		UUID other = servant.ownerUUID();
		return other==null?false:TruceMapHandler.get(player.world).playerTruces(player).contains(other);
	}
	
	public static boolean inSameTeam(EntityServant servant, EntityServant other)
	{
		UUID first = servant.ownerUUID();
		UUID second = other.ownerUUID();
		return (first==null||other==null)?false:TruceMapHandler.get(servant.world).playerTruces(first).contains(second);
	}
}
