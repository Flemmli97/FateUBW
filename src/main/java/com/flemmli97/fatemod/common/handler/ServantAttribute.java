package com.flemmli97.fatemod.common.handler;

public class ServantAttribute {
	
	private double health, strength, armor, proj, magic, move;
	private int mana, drop;
	/**
	 * @param health
	 * @param strength
	 * @param armor
	 * @param projectileArmor
	 * @param magicRes
	 * @param moveSpeed
	 * @param NPMana
	 * @param dropChance
	 */
	public ServantAttribute(double health, double strength, double armor, double projectileArmor, double magicRes, double moveSpeed, int NPMana, int dropChance)
	{
		this.health=health;
		this.strength=strength;
		this.armor=armor;
		this.proj=projectileArmor;
		this.magic=magicRes;
		this.move=moveSpeed;
		this.mana=NPMana;
		this.drop=dropChance;
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
}
