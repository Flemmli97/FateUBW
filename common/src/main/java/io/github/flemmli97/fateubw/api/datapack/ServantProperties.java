package io.github.flemmli97.fateubw.api.datapack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.common.lib.BuiltinServantClasses;
import net.minecraft.resources.ResourceLocation;

public class ServantProperties {

    public static final Codec<ServantProperties> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.DOUBLE.fieldOf("health").forGetter(d -> d.health),
                    Codec.DOUBLE.fieldOf("magicProt").forGetter(d -> d.magic),
                    Codec.DOUBLE.fieldOf("moveSpeed").forGetter(d -> d.move),
                    Codec.FLOAT.fieldOf("projectileBlockChance").forGetter(d -> d.block),

                    Codec.DOUBLE.fieldOf("projectileProt").forGetter(d -> d.proj),
                    Codec.INT.fieldOf("nobelPhantasmCost").forGetter(d -> d.mana),

                    ResourceLocation.CODEC.fieldOf("class").forGetter(d -> d.servantClass),
                    Codec.DOUBLE.fieldOf("strength").forGetter(d -> d.strength),
                    Codec.DOUBLE.fieldOf("armor").forGetter(d -> d.armor)
            ).apply(instance, (health, magic, move, block,  proj, mana, clss, strength, armor) ->
                    new ServantProperties(health, strength, armor, block, proj, magic, move, mana, clss)));

    public static final ServantProperties DEFAULT = new ServantProperties(20, 1, 0, 0, 0, 0.2, 0.2, 0, BuiltinServantClasses.NONE);

    private final double health, strength, armor, proj, magic, move;
    private final float block;
    private final int mana;
    private final ResourceLocation servantClass;

    public ServantProperties(double health, double strength, double armor, float block, double projProt, double magic, double moveSpeed, int hogouMana, ResourceLocation servantClass) {
        this.health = health;
        this.strength = strength;
        this.armor = armor;
        this.proj = projProt;
        this.magic = magic;
        this.move = moveSpeed;
        this.block = block;
        this.mana = hogouMana;
        this.servantClass = servantClass;
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

    public ResourceLocation getServantClass() {
        return this.servantClass;
    }
}