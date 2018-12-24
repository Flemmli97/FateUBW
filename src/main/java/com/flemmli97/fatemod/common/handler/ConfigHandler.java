package com.flemmli97.fatemod.common.handler;

import java.util.Map;

import com.flemmli97.fatemod.common.entity.servant.EntityArthur;
import com.flemmli97.fatemod.common.entity.servant.EntityCuchulainn;
import com.flemmli97.fatemod.common.entity.servant.EntityDiarmuid;
import com.flemmli97.fatemod.common.entity.servant.EntityEmiya;
import com.flemmli97.fatemod.common.entity.servant.EntityGilgamesh;
import com.flemmli97.fatemod.common.entity.servant.EntityGilles;
import com.flemmli97.fatemod.common.entity.servant.EntityHassan;
import com.flemmli97.fatemod.common.entity.servant.EntityHeracles;
import com.flemmli97.fatemod.common.entity.servant.EntityIskander;
import com.flemmli97.fatemod.common.entity.servant.EntityLancelot;
import com.flemmli97.fatemod.common.entity.servant.EntityMedea;
import com.flemmli97.fatemod.common.entity.servant.EntityMedusa;
import com.flemmli97.fatemod.common.entity.servant.EntitySasaki;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.fatemod.common.utils.ServantProperties;
import com.flemmli97.tenshilib.api.config.ConfigAnnotations;
import com.flemmli97.tenshilib.api.config.MapSerializable;
import com.flemmli97.tenshilib.common.config.ConfigUtils.Init;
import com.flemmli97.tenshilib.common.javahelper.StringParser;
import com.google.common.collect.Maps;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraftforge.common.config.Config;

@Config(modid = LibReference.MODID,name="fate/main", category="")
public class ConfigHandler {
	
	@Config.LangKey(value="config.fatemod.general")
	public static General general = new General();
	@Config.LangKey(value="config.fatemod.servants")
	public static ServantAttributes servants = new ServantAttributes();
	@Config.LangKey(value="config.fatemod.minions")
	public static MinionAttributes minions = new MinionAttributes();

	public static class General
	{
		@Config.Name(value="Min Player")
		@Config.Comment(value="Minimum of player count required to start a grail war")
		public int minPlayer;
		@Config.Name(value="Max Player")
		@Config.Comment(value="Maximum of player allowed in a grail war")
		public int maxPlayer=7;
		@Config.Name(value="Join Time")
		@Config.Comment(value="Time buffer to join a grail war after start")
		public int joinTime=12000;
		@Config.Name(value="Reward Delay")
		@Config.Comment(value="Delay after an ended grail war for getting the grail")
		public int rewardDelay=2000;
		@Config.Name(value="Medallion Rate")
		@Config.Comment(value="Customize the spawn rate of the servant medallion ores, higher value means higher spawn rate. Default is 1")
		public int charmSpawnRate=1;
		@Config.Name(value="Magic Gem Rate")
		@Config.Comment(value="Customize the spawn rate of the magic gem ores, higher value means higher spawn rate. Default is 10")
		public int gemSpawnRate=10;
			
		@Config.Name(value="Allow Duplicate Servants")
		@Config.Comment(value="Allow the summoning of duplicate servants during a grail war")
		public boolean allowDuplicateServant;
		@Config.Name(value="Allow Duplicate Classes")
		@Config.Comment(value="Allow the summoning of duplicate servant classes during a grail war")
		public boolean allowDuplicateClass;
	}
	
	public static class ServantAttributes
	{
		@Config.Ignore
		public static final Map<Class<? extends Entity>, ServantProperties> attributes = Maps.newHashMap();

		@Config.Ignore
		@ConfigAnnotations.ConfigValue(getInitTime=Init.INIT)
		@Config.Name(value="")
		@Config.LangKey(value="config.fatemod.servants")
		public MapSerializable<Class<? extends Entity>,ServantProperties> servants = new MapSerializable<Class<? extends Entity>,ServantProperties>(attributes, new StringParser<Class<? extends Entity>>() {

			@Override
			public String getString(Class<? extends Entity> t) {
				return EntityList.getKey(t).toString();
			}});
		
		static
		{
			attributes.put(EntityArthur.class, new ServantProperties(300, 10, 17, 0.7F, 12, 10, 0.3, 100, 10, 5));
			attributes.put(EntityCuchulainn.class, new ServantProperties(275, 7.5, 10, 0, 14, 6, 0.35, 75, 10, 5));
			attributes.put(EntityDiarmuid.class, new ServantProperties(310, 8.5, 12, 0, 13, 7, 0.35, 80, 10, 5));
			attributes.put(EntityEmiya.class, new ServantProperties(250, 7.5, 8, 0, 15.5, 7, 0.33, 66, 10, 5));
			attributes.put(EntityGilgamesh.class, new ServantProperties(250, 10, 9, 0, 12.5, 5, 0.3, 100, 10, 5));
			attributes.put(EntityGilles.class, new ServantProperties(350, 5.5, 7, 0, 5, 14, 0.3, 80, 10, 5));
			attributes.put(EntityHassan.class, new ServantProperties(200, 6, 8.5, 0, 17, 4, 0.34, 1, 10, 5));
			attributes.put(EntityHeracles.class, new ServantProperties(75, 12.5, 10, 0, 17, 9.5, 0.2, 0, 10, 5));
			attributes.put(EntityLancelot.class, new ServantProperties(450, 9, 14, 0, 19, 4, 0.2, 0, 10, 5));
			attributes.put(EntityMedea.class, new ServantProperties(350, 9.5, 5, 0, 4, 17.5, 0.2, 100, 10, 5));
			attributes.put(EntityIskander.class, new ServantProperties(400, 5.5, 10, 0, 9, 9.5, 0.3, 100, 10, 5));
			attributes.put(EntityMedusa.class, new ServantProperties(250, 4.5, 11, 0, 7, 10, 0.3, 80, 10, 5));
			attributes.put(EntitySasaki.class, new ServantProperties(350, 9.5, 9, 0, 8, 8.5, 0.3, 50, 10, 5));
		}
	}
	
	public static class MinionAttributes
	{
		@Config.LangKey(value="config.tentacle")
		public int gillesMinionDuration;
		@Config.Ignore
		@ConfigAnnotations.ConfigValue(getInitTime=Init.INIT)
		@Config.LangKey(value="config.fatemod.hassancopy")
		public ServantProperties hassansCopy = new ServantProperties(50, 4, 3.5, 0, 12, 2, 0.34, 0, 10, 5);
	}
}
