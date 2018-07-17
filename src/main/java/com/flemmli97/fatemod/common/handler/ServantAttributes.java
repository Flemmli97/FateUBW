package com.flemmli97.fatemod.common.handler;

import java.util.Map;

import com.flemmli97.fatemod.common.entity.servant.EntityArthur;
import com.flemmli97.fatemod.common.entity.servant.EntityCuchulainn;
import com.flemmli97.fatemod.common.entity.servant.EntityDiarmuid;
import com.flemmli97.fatemod.common.entity.servant.EntityEmiya;
import com.flemmli97.fatemod.common.entity.servant.EntityGilgamesh;
import com.flemmli97.fatemod.common.entity.servant.EntityGilles;
import com.flemmli97.fatemod.common.entity.servant.EntityHassan;
import com.flemmli97.fatemod.common.entity.servant.EntityHassanCopy;
import com.flemmli97.fatemod.common.entity.servant.EntityHeracles;
import com.flemmli97.fatemod.common.entity.servant.EntityIskander;
import com.flemmli97.fatemod.common.entity.servant.EntityLancelot;
import com.flemmli97.fatemod.common.entity.servant.EntityMedea;
import com.flemmli97.fatemod.common.entity.servant.EntityMedusa;
import com.flemmli97.fatemod.common.entity.servant.EntitySasaki;
import com.google.common.collect.Maps;

import net.minecraft.entity.Entity;

public class ServantAttributes {
	
	public static final Map<Class<? extends Entity>, ServantAttribute> attributes = Maps.newHashMap();
	
	static
	{
		attributes.put(EntityArthur.class, new ServantAttribute(300, 10, 17, 12, 10, 0.3, 100, 10));
		attributes.put(EntityCuchulainn.class, new ServantAttribute(275, 7.5, 10, 14, 6, 0.35, 75, 10));
		attributes.put(EntityDiarmuid.class, new ServantAttribute(310, 8.5, 12, 13, 7, 0.35, 80, 10));
		attributes.put(EntityEmiya.class, new ServantAttribute(250, 7.5, 8, 15.5, 7, 0.33, 66, 10));
		attributes.put(EntityGilgamesh.class, new ServantAttribute(250, 10, 9, 12.5, 5, 0.3, 100, 10));
		attributes.put(EntityGilles.class, new ServantAttribute(350, 5.5, 7, 5, 14, 0.3, 80, 10));
		attributes.put(EntityHassan.class, new ServantAttribute(200, 6, 8.5, 17, 4, 0.34, 1, 10));
		attributes.put(EntityHassanCopy.class, new ServantAttribute(50, 4, 3.5, 12, 2, 0.34, 0, 10));

		attributes.put(EntityHeracles.class, new ServantAttribute(100, 12.5, 10, 17, 9.5, 0.2, 0, 10));
		attributes.put(EntityLancelot.class, new ServantAttribute(450, 9, 14, 19, 4, 0.2, 0, 10));
		attributes.put(EntityMedea.class, new ServantAttribute(350, 9.5, 5, 4, 17.5, 0.2, 100, 10));
		attributes.put(EntityIskander.class, new ServantAttribute(400, 5.5, 10, 9, 9.5, 0.3, 100, 10));
		attributes.put(EntityMedusa.class, new ServantAttribute(250, 4.5, 11, 7, 10, 0.3, 80, 10));
		attributes.put(EntitySasaki.class, new ServantAttribute(350, 9.5, 9, 8, 8.5, 0.3, 50, 10));

	}
}
