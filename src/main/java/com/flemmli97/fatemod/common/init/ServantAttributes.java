package com.flemmli97.fatemod.common.init;

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
import com.flemmli97.fatemod.common.utils.ServantProperties;
import com.google.common.collect.Maps;

import net.minecraft.entity.Entity;

public class ServantAttributes {
	
	public static final Map<Class<? extends Entity>, ServantProperties> attributes = Maps.newHashMap();
	
	static
	{
		attributes.put(EntityArthur.class, new ServantProperties(300, 10, 17, 12, 10, 0.3, 100, 10, 5));
		attributes.put(EntityCuchulainn.class, new ServantProperties(275, 7.5, 10, 14, 6, 0.35, 75, 10, 5));
		attributes.put(EntityDiarmuid.class, new ServantProperties(310, 8.5, 12, 13, 7, 0.35, 80, 10, 5));
		attributes.put(EntityEmiya.class, new ServantProperties(250, 7.5, 8, 15.5, 7, 0.33, 66, 10, 5));
		attributes.put(EntityGilgamesh.class, new ServantProperties(250, 10, 9, 12.5, 5, 0.3, 100, 10, 5));
		attributes.put(EntityGilles.class, new ServantProperties(350, 5.5, 7, 5, 14, 0.3, 80, 10, 5));
		attributes.put(EntityHassan.class, new ServantProperties(200, 6, 8.5, 17, 4, 0.34, 1, 10, 5));
		attributes.put(EntityHassanCopy.class, new ServantProperties(50, 4, 3.5, 12, 2, 0.34, 0, 10, 5));

		attributes.put(EntityHeracles.class, new ServantProperties(100, 12.5, 10, 17, 9.5, 0.2, 0, 10, 5));
		attributes.put(EntityLancelot.class, new ServantProperties(450, 9, 14, 19, 4, 0.2, 0, 10, 5));
		attributes.put(EntityMedea.class, new ServantProperties(350, 9.5, 5, 4, 17.5, 0.2, 100, 10, 5));
		attributes.put(EntityIskander.class, new ServantProperties(400, 5.5, 10, 9, 9.5, 0.3, 100, 10, 5));
		attributes.put(EntityMedusa.class, new ServantProperties(250, 4.5, 11, 7, 10, 0.3, 80, 10, 5));
		attributes.put(EntitySasaki.class, new ServantProperties(350, 9.5, 9, 8, 8.5, 0.3, 50, 10, 5));

	}
}
