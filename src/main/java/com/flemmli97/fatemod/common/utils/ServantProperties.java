package com.flemmli97.fatemod.common.utils;

import com.flemmli97.tenshilib.api.config.IConfigSerializable;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

public class ServantProperties implements IConfigSerializable<ServantProperties>{
	
	private double health, strength, armor, proj, magic, move;
	private int mana, drop, looting;
	private float block;

	public ServantProperties(double health, double strength, double armor, float projectileBlock, double projectileArmor,
			double magicRes, double moveSpeed, int NPMana, int dropChance, int lootingAdd)
	{
		this.health=health;
		this.strength=strength;
		this.armor=armor;
		this.block=projectileBlock;
		this.proj=projectileArmor;
		this.magic=magicRes;
		this.move=moveSpeed;
		this.mana=NPMana;
		this.drop=dropChance;
		this.looting=lootingAdd;
	}

	public double health()
	{
		return this.health;
	}
	public double strength()
	{
		return this.strength;
	}
	public double armor()
	{
		return this.armor;
	}
	public float projectileBlockChance()
	{
		return this.block;
	}
	public double projectileProt()
	{
		return this.proj;
	}
	public double magicRes()
	{
		return this.magic;
	}
	public double moveSpeed()
	{
		return this.move;
	}
	public int hogouMana()
	{
		return this.mana;
	}
	public int dropChance()
	{
		return this.drop;
	}
	public int looting()
	{
		return this.looting;
	}

	@Override
	public ServantProperties config(Configuration config, ServantProperties old, String configCategory) {
		ConfigCategory cat = config.getCategory(configCategory);
		cat.setLanguageKey("entity."+configCategory.substring(configCategory.indexOf(":")+1)+".name");
		this.health=config.get(configCategory, "Health", this.health, "Health of the servant").getDouble(); 
		this.strength=config.get(configCategory, "Strength", this.strength, "Attack damage of the servant").getDouble();
		this.armor=config.get(configCategory, "Armor", this.armor, "Generic armor of the servant").getDouble();
		this.block=(float) config.get(configCategory, "Block", this.block, "Chance of blocking incoming projectiles").getDouble();
		this.proj=config.get(configCategory, "ProjectProt", this.proj, "Projectile armor of the servant").getDouble();
		this.magic=config.get(configCategory, "MagicProt", this.magic, "Magic protection of the servant").getDouble();
		this.move=config.get(configCategory, "MoveSpeed", this.move, "Move speed of the servant").getDouble();
		this.mana=config.getInt("NobelPhantasmMana", configCategory, this.mana, 0, 100, "Nobel Phantasm mana usage");
		this.drop=config.getInt("Drop chance", configCategory, this.drop, 0, 100, "Item drop chance upon death. Looting adds 10*level% chance to it");
		this.looting=config.getInt("Looting add", configCategory, this.looting, 0, 100, "Chance per level of looting adding on to the drop chance");
		return this;
	}
}
