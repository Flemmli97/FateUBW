package io.github.flemmli97.fateubw.api.datapack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class ServantProperties {

    public static final Codec<ServantProperties> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.DOUBLE.fieldOf("magicProt").forGetter(d -> d.magic),
                    Codec.DOUBLE.fieldOf("moveSpeed").forGetter(d -> d.move),
                    Codec.FLOAT.fieldOf("projectileBlockChance").forGetter(d -> d.block),
                    Codec.INT.fieldOf("nobelPhantasmCost").forGetter(d -> d.mana),

                    Codec.DOUBLE.fieldOf("health").forGetter(d -> d.health),
                    Codec.DOUBLE.fieldOf("strength").forGetter(d -> d.strength),
                    Codec.DOUBLE.fieldOf("armor").forGetter(d -> d.armor),
                    Codec.DOUBLE.fieldOf("projectileProt").forGetter(d -> d.proj)
            ).apply(instance, (magic, move, block, mana, health, strength, armor, proj) ->
                    new ServantProperties(health, strength, armor, block, proj, magic, move, mana)));

    public static final ServantProperties DEFAULT = new ServantProperties(20, 1, 0, 0, 0, 0.2, 0.2, 0);

    private final double health, strength, armor, proj, magic, move;
    private final float block;
    private final int mana;

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