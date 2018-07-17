package com.flemmli97.fatemod.common.utils;

public class ServantProperties {
	
	private double health, strength, armor, proj, magic, move;
	private int mana, drop, looting;

	public ServantProperties(double health, double strength, double armor, double projectileArmor,
			double magicRes, double moveSpeed, int NPMana, int dropChance, int lootingAdd)
	{
		this.health=health;
		this.strength=strength;
		this.armor=armor;
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
}
