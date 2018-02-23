package com.flemmli97.fatemod.common.init;

import com.flemmli97.fatemod.common.item.ItemChalk;
import com.flemmli97.fatemod.common.item.ItemDebug;
import com.flemmli97.fatemod.common.item.ItemGateofBabylon;
import com.flemmli97.fatemod.common.item.ItemGemCluster;
import com.flemmli97.fatemod.common.item.ItemGemShard;
import com.flemmli97.fatemod.common.item.ItemHolyGrail;
import com.flemmli97.fatemod.common.item.ItemIcon;
import com.flemmli97.fatemod.common.item.ItemManaBottle;
import com.flemmli97.fatemod.common.item.ItemServantCharm;
import com.flemmli97.fatemod.common.item.ItemSpawn;
import com.flemmli97.fatemod.common.item.weapon.ItemArondight;
import com.flemmli97.fatemod.common.item.weapon.ItemAssassinDagger;
import com.flemmli97.fatemod.common.item.weapon.ItemBakuya;
import com.flemmli97.fatemod.common.item.weapon.ItemDagger;
import com.flemmli97.fatemod.common.item.weapon.ItemEmiyaBow;
import com.flemmli97.fatemod.common.item.weapon.ItemEnumaElish;
import com.flemmli97.fatemod.common.item.weapon.ItemExcalibur;
import com.flemmli97.fatemod.common.item.weapon.ItemGaeBolg;
import com.flemmli97.fatemod.common.item.weapon.ItemGaeBuidhe;
import com.flemmli97.fatemod.common.item.weapon.ItemGaeDearg;
import com.flemmli97.fatemod.common.item.weapon.ItemGrimoire;
import com.flemmli97.fatemod.common.item.weapon.ItemHeraclesAxe;
import com.flemmli97.fatemod.common.item.weapon.ItemInvisExcalibur;
import com.flemmli97.fatemod.common.item.weapon.ItemKanshou;
import com.flemmli97.fatemod.common.item.weapon.ItemKatana;
import com.flemmli97.fatemod.common.item.weapon.ItemRuleBreaker;
import com.flemmli97.fatemod.common.item.weapon.ItemStaff;

import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {
	    
    public static Item excalibur;
    public static Item arondight;
    public static Item bakuya;
    public static Item kanshou;
    public static Item archbow;
    public static Item enumaelish;
    public static Item gaebolg;
    public static Item gaebuidhe;
    public static Item gaedearg;
    public static Item heraclesAxe;
    public static Item invisexcabibur;
    public static Item ruleBreaker;
    public static Item katana;
    public static Item medusaDagger;
    public static Item staff;
    public static Item grimoire;
    public static Item dagger;
        
    public static Item manaBottle;
    public static Item babylon;
    public static Item crystalCluster;
    public static Item crystal;
    public static Item grail;
    public static Item charm;
    public static Item chalk;
    public static Item spawnEgg;

    public static Item icon;
    
    public static Item debug;
    
	public static final void init() {
		
		chalk = new ItemChalk();

		excalibur = new ItemExcalibur();
		arondight = new ItemArondight();
		enumaelish = new ItemEnumaElish();
		bakuya = new ItemBakuya();
		kanshou = new ItemKanshou();
		archbow = new ItemEmiyaBow();
		gaebolg = new ItemGaeBolg();
		gaebuidhe = new ItemGaeBuidhe();
		gaedearg = new ItemGaeDearg();
		medusaDagger = new ItemDagger();
		heraclesAxe = new ItemHeraclesAxe();
		invisexcabibur = new ItemInvisExcalibur();
		ruleBreaker = new ItemRuleBreaker();
		katana = new ItemKatana();
		staff = new ItemStaff();
		grimoire = new ItemGrimoire();
		dagger = new ItemAssassinDagger();
		
		crystal = new ItemGemShard();
		crystalCluster = new ItemGemCluster();
		charm = new ItemServantCharm();
		grail = new ItemHolyGrail();
		manaBottle = new ItemManaBottle();
		spawnEgg = new ItemSpawn();
		babylon = new ItemGateofBabylon();

		icon = new ItemIcon();
		
		debug = new ItemDebug();
		
	}
	
	@SideOnly(Side.CLIENT)
	public static final void initModels()
	{
		((ItemExcalibur) excalibur).initModel();
		((ItemArondight) arondight).initModel();
		((ItemBakuya) bakuya).initModel();
		((ItemKanshou) kanshou).initModel();
		((ItemEmiyaBow) archbow).initModel();
		((ItemEnumaElish) enumaelish).initModel();
		((ItemGaeBolg) gaebolg).initModel();
		((ItemGaeBuidhe) gaebuidhe).initModel();
		((ItemGaeDearg) gaedearg).initModel();
		((ItemHeraclesAxe) heraclesAxe).initModel();
		((ItemInvisExcalibur) invisexcabibur).initModel();
		((ItemRuleBreaker) ruleBreaker).initModel();
		((ItemKatana) katana).initModel();
		((ItemDagger) medusaDagger).initModel();
		((ItemStaff) staff).initModel();
		((ItemGrimoire) grimoire).initModel();
		((ItemAssassinDagger) dagger).initModel();
		
		((ItemSpawn) spawnEgg).initModel();
		((ItemManaBottle) manaBottle).initModel();
		((ItemGateofBabylon)babylon).initModel();
		((ItemGemCluster)crystalCluster).initModel();
		((ItemGemShard)crystal).initModel();
		((ItemHolyGrail)grail).initModel();
		((ItemServantCharm)charm).initModel();
		((ItemChalk) chalk).initModel();
		
		((ItemIcon) icon).initModel();
	}
	
}
