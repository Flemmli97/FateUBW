package com.flemmli97.fate.common.config;

public class ServantProperties{

	public static final ServantProperties def = new ServantProperties();

	private double health = 20, strength = 1, armor, proj, magic, move = 0.1;
	private float block;
	private int mana, drop, looting;

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

	public void loadSpecs(ServantConfSpec spec){
		this.health = spec.health.get();
		this.strength = spec.strength.get();
		this.armor = spec.armor.get();
		this.block = spec.block.get().floatValue();
		this.proj = spec.proj.get();
		this.magic = spec.magic.get();
		this.move = spec.move.get();
		this.mana = spec.mana.get();
		this.drop = spec.drop.get();
		this.looting = spec.looting.get();
	}
}
