package com.flemmli97.fate.common.config;

public class ServantProperties {

    public static final ServantProperties def = new ServantProperties(20, 1, 0, 0, 0, 0.2, 0.7, 0);

    private double health, strength, armor, proj, magic, move;
    private float block;
    private int mana;

    public ServantProperties(double health, double strength, double armor, float block, double projProt, double magic, double moveSpeed, int hogouMana) {
        this.health = health;
        this.strength = strength;
        this.armor = armor;
        this.proj = projProt;
        this.magic = magic;
        this.move = moveSpeed;
        this.block = block;
        this.mana = hogouMana;
    }

    public static ServantProperties read(ServantConfSpec spec) {
        return new ServantProperties(spec.health.get(), spec.strength.get(), spec.armor.get(), spec.block.get().floatValue()
                , spec.proj.get(), spec.magic.get(), spec.move.get(), spec.mana.get());
    }

    public double health() {
        return this.health;
    }

    public double strength() {
        return this.strength;
    }

    public double armor() {
        return this.armor;
    }

    public float projectileBlockChance() {
        return this.block;
    }

    public double projectileProt() {
        return this.proj;
    }

    public double magicRes() {
        return this.magic;
    }

    public double moveSpeed() {
        return this.move;
    }

    public int hogouMana() {
        return this.mana;
    }
}
