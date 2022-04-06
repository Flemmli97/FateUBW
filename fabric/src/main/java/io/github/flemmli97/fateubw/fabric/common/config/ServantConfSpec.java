package io.github.flemmli97.fateubw.fabric.common.config;

import io.github.flemmli97.fateubw.common.config.ServantProperties;
import io.github.flemmli97.tenshilib.common.config.CommentedJsonConfig;

public class ServantConfSpec {

    public final CommentedJsonConfig.DoubleVal health, strength, armor, proj, magic, move;
    public final CommentedJsonConfig.DoubleVal block;
    public final CommentedJsonConfig.IntVal mana;

    public ServantConfSpec(CommentedJsonConfig.Builder builder, ServantProperties def) {
        this.health = builder.comment("Health of the servant").defineInRange("Health", def.health(), 0, Double.MAX_VALUE);
        this.strength = builder.comment("Attack damage of the servant").defineInRange("Strength", def.strength(), 0, Double.MAX_VALUE);
        this.armor = builder.comment("Generic armor of the servant").defineInRange("Armor", def.armor(), 0, Double.MAX_VALUE);
        this.block = builder.comment("Chance of blocking incoming projectiles").defineInRange("Block", def.projectileBlockChance(), 0, 1);
        this.proj = builder.comment("Projectile armor of the servant").defineInRange("Projectile Prot", def.projectileProt(), 0, Double.MAX_VALUE);
        this.magic = builder.comment("Magic protection").defineInRange("Magic Prot", def.magicRes(), 0, Double.MAX_VALUE);
        this.move = builder.comment("Move Speed").defineInRange("MoveSpeed", def.moveSpeed(), 0, Double.MAX_VALUE);
        this.mana = builder.comment("Nobel Phantasm mana usage").defineInRange("Nobel Phantasm Mana", def.hogouMana(), 0, 100);
    }

    public static ServantProperties read(ServantConfSpec spec) {
        return new ServantProperties(spec.health.get(), spec.strength.get(), spec.armor.get(), spec.block.get().floatValue()
                , spec.proj.get(), spec.magic.get(), spec.move.get(), spec.mana.get());
    }
}
