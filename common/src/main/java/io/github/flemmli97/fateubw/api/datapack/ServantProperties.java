package io.github.flemmli97.fateubw.api.datapack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.common.lib.BuiltinServantClasses;
import net.minecraft.resources.ResourceLocation;

public class ServantProperties {

    public static final Codec<ServantProperties> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.FLOAT.fieldOf("projectile_block_chance").forGetter(d -> d.block),
                    Codec.INT.fieldOf("nobel_phantasm_cost").forGetter(d -> d.manaCost),

                    Codec.DOUBLE.fieldOf("projectile_prot").forGetter(d -> d.proj),
                    Codec.DOUBLE.fieldOf("magic_prot").forGetter(d -> d.magicProt),
                    Codec.DOUBLE.fieldOf("move_speed").forGetter(d -> d.move),

                    Codec.DOUBLE.fieldOf("magic").forGetter(d -> d.magic),
                    Codec.DOUBLE.fieldOf("armor").forGetter(d -> d.armor),

                    ResourceLocation.CODEC.fieldOf("class").forGetter(d -> d.servantClass),
                    Codec.DOUBLE.fieldOf("health").forGetter(d -> d.health),
                    Codec.DOUBLE.fieldOf("strength").forGetter(d -> d.strength)
            ).apply(instance, (block, manaCost, proj, magicProt, move, magic, armor, clss, health, strength) ->
                    new ServantProperties(health, strength, armor, block, proj, magic, magicProt, move, manaCost, clss)));

    public static final ServantProperties DEFAULT = new ServantProperties(20, 1, 0, 0, 0, 1, 0.2, 0.2, 0, BuiltinServantClasses.NONE);

    private final double health, strength, armor, proj, magic, magicProt, move;
    private final float block;
    private final int manaCost;
    private final ResourceLocation servantClass;

    public ServantProperties(double health, double strength, double armor, float block, double projProt, double magic, double magicProt, double moveSpeed, int hogouMana, ResourceLocation servantClass) {
        this.health = health;
        this.strength = strength;
        this.armor = armor;
        this.proj = projProt;
        this.magic = magic;
        this.magicProt = magicProt;
        this.move = moveSpeed;
        this.block = block;
        this.manaCost = hogouMana;
        this.servantClass = servantClass;
    }

    public double health() {
        return this.health;
    }

    public double strength() {
        return this.strength;
    }

    public double magic() {
        return this.magic;
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
        return this.magicProt;
    }

    public double moveSpeed() {
        return this.move;
    }

    public int hogouMana() {
        return this.manaCost;
    }

    public ResourceLocation getServantClass() {
        return this.servantClass;
    }
}