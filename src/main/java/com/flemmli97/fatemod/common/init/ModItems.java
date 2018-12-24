package com.flemmli97.fatemod.common.init;

import com.flemmli97.fatemod.common.entity.servant.EntityServant.EnumServantType;
import com.flemmli97.fatemod.common.items.ItemChalk;
import com.flemmli97.fatemod.common.items.ItemDebug;
import com.flemmli97.fatemod.common.items.ItemGateofBabylon;
import com.flemmli97.fatemod.common.items.ItemGemCluster;
import com.flemmli97.fatemod.common.items.ItemGemShard;
import com.flemmli97.fatemod.common.items.ItemGemShard.ShardType;
import com.flemmli97.fatemod.common.items.ItemHolyGrail;
import com.flemmli97.fatemod.common.items.ItemIcon;
import com.flemmli97.fatemod.common.items.ItemManaBottle;
import com.flemmli97.fatemod.common.items.ItemServantCharm;
import com.flemmli97.fatemod.common.items.ItemSpawn;
import com.flemmli97.fatemod.common.items.weapons.ItemArondight;
import com.flemmli97.fatemod.common.items.weapons.ItemAssassinDagger;
import com.flemmli97.fatemod.common.items.weapons.ItemBakuya;
import com.flemmli97.fatemod.common.items.weapons.ItemDagger;
import com.flemmli97.fatemod.common.items.weapons.ItemEmiyaBow;
import com.flemmli97.fatemod.common.items.weapons.ItemEnumaElish;
import com.flemmli97.fatemod.common.items.weapons.ItemExcalibur;
import com.flemmli97.fatemod.common.items.weapons.ItemGaeBolg;
import com.flemmli97.fatemod.common.items.weapons.ItemGaeBuidhe;
import com.flemmli97.fatemod.common.items.weapons.ItemGaeDearg;
import com.flemmli97.fatemod.common.items.weapons.ItemGrimoire;
import com.flemmli97.fatemod.common.items.weapons.ItemHeraclesAxe;
import com.flemmli97.fatemod.common.items.weapons.ItemInvisExcalibur;
import com.flemmli97.fatemod.common.items.weapons.ItemKanshou;
import com.flemmli97.fatemod.common.items.weapons.ItemKatana;
import com.flemmli97.fatemod.common.items.weapons.ItemKupriots;
import com.flemmli97.fatemod.common.items.weapons.ItemRuleBreaker;
import com.flemmli97.fatemod.common.items.weapons.ItemStaff;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = LibReference.MODID)
public class ModItems {
	    
	public static final Item excalibur = new ItemExcalibur();
	public static final Item arondight = new ItemArondight();
	public static final Item enumaelish = new ItemEnumaElish();
	public static final Item bakuya = new ItemBakuya();
	public static final Item kanshou = new ItemKanshou();
	public static final Item archbow = new ItemEmiyaBow();
	public static final Item gaebolg = new ItemGaeBolg();
	public static final Item gaebuidhe = new ItemGaeBuidhe();
	public static final Item gaedearg = new ItemGaeDearg();
	public static final Item medusaDagger = new ItemDagger();
	public static final Item heraclesAxe = new ItemHeraclesAxe();
	public static final Item invisexcalibur = new ItemInvisExcalibur();
	public static final Item ruleBreaker = new ItemRuleBreaker();
	public static final Item katana = new ItemKatana();
	public static final Item staff = new ItemStaff();
	public static final Item grimoire = new ItemGrimoire();
	public static final Item dagger = new ItemAssassinDagger();
	public static final Item kupriots = new ItemKupriots();
	
	public static final Item[] WEAPONS = new Item[] {excalibur, arondight, enumaelish, bakuya, kanshou, archbow, gaebolg, gaebuidhe, gaedearg,
			medusaDagger, heraclesAxe, invisexcalibur, ruleBreaker, katana, staff, grimoire, dagger, kupriots};
	
	public static final Item crystalEarth = new ItemGemShard(ShardType.EARTH);
	public static final Item crystalFire = new ItemGemShard(ShardType.FIRE);
	public static final Item crystalVoid = new ItemGemShard(ShardType.VOID);
	public static final Item crystalWater = new ItemGemShard(ShardType.WATER);
	public static final Item crystalWind = new ItemGemShard(ShardType.WIND);

	public static final Item[] crystals = new Item[] {crystalEarth, crystalFire, crystalVoid, crystalWater, crystalWind};
	
	public static final Item crystalCluster = new ItemGemCluster();
	
	public static final Item charmNone = new ItemServantCharm(EnumServantType.NOTASSIGNED);
	public static final Item charmSaber = new ItemServantCharm(EnumServantType.SABER);
	public static final Item charmLancer = new ItemServantCharm(EnumServantType.LANCER);
	public static final Item charmArcher = new ItemServantCharm(EnumServantType.ARCHER);
	public static final Item charmBerserker = new ItemServantCharm(EnumServantType.BERSERKER);
	public static final Item charmRider = new ItemServantCharm(EnumServantType.RIDER);
	public static final Item charmCaster = new ItemServantCharm(EnumServantType.CASTER);
	public static final Item charmAssassin = new ItemServantCharm(EnumServantType.ASSASSIN);

	public static final Item[] charms = new Item[] {charmSaber, charmLancer, charmArcher, charmBerserker, charmRider, charmCaster, charmAssassin};
	public static final Item grail = new ItemHolyGrail();
	public static final Item manaBottle = new ItemManaBottle();
	public static final Item spawnEgg = new ItemSpawn();
	public static final Item babylon = new ItemGateofBabylon();
	public static final Item chalk = new ItemChalk();

	public static final Item[] OTHERS = new Item[] {crystalFire, crystalEarth, crystalWater, crystalWind, crystalVoid, crystalCluster, grail, manaBottle, spawnEgg, babylon, chalk};
	
	public static final Item icon0 = new ItemIcon(0);
	public static final Item icon1 = new ItemIcon(1);
	public static final Item icon2 = new ItemIcon(2);
	public static final Item icon3 = new ItemIcon(3);

	public static final Item debug = new ItemDebug();
	
	public static final Item[] ICONS = new Item[] {icon0, icon1, icon2, icon3};
	public static final Item[] CREATIVE = new Item[] {icon0, icon1, icon2, icon3, debug};

	@SubscribeEvent
	public static final void registerItems(RegistryEvent.Register<Item> event) {
		for(Item item : WEAPONS)
			event.getRegistry().register(item);
		event.getRegistry().register(charmNone);
		for(Item item : charms)
			event.getRegistry().register(item);
		for(Item item : OTHERS)
			event.getRegistry().register(item);
		for(Item item : CREATIVE)
			event.getRegistry().register(item);
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static final void initModel(ModelRegistryEvent event)
	{
		for(Item item : WEAPONS)
			registerDefaultModel(item);
		registerDefaultModel(charmNone);
		for(Item item : charms)
			registerDefaultModel(item);
		for(Item item : OTHERS)
			registerDefaultModel(item);
		for(Item item : CREATIVE)
			registerDefaultModel(item);
	}
	
	@SideOnly(Side.CLIENT)
	private static void registerDefaultModel(Item item)
	{
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}
