package io.github.flemmli97.fateubw.forge.common.config;

import io.github.flemmli97.fateubw.common.config.ServantProperties;
import net.minecraftforge.common.ForgeConfigSpec;

public class ServantConfSpec {

    public final ForgeConfigSpec.ConfigValue<Double> health, strength, armor, proj, magic, move;
    public final ForgeConfigSpec.DoubleValue block;
    public final ForgeConfigSpec.IntValue mana;

    public ServantConfSpec(ForgeConfigSpec.Builder builder, ServantProperties def) {
        this.health = builder.comment("Health of the servant").define("Health", def.health());
        this.strength = builder.comment("Attack damage of the servant").define("Strength", def.strength());
        this.armor = builder.comment("Generic armor of the servant").define("Armor", def.armor());
        this.block = builder.comment("Chance of blocking incoming projectiles").defineInRange("Block", def.projectileBlockChance(), 0, 1);
        this.proj = builder.comment("Projectile armor of the servant").define("Projectile Prot", def.projectileProt());
        this.magic = builder.comment("Magic protection").define("Magic Prot", def.magicRes());
        this.move = builder.comment("Move Speed").define("MoveSpeed", def.moveSpeed());
        this.mana = builder.comment("Nobel Phantasm mana usage").defineInRange("Nobel Phantasm Mana", def.hogouMana(), 0, 100);
    }

    public static ServantProperties read(ServantConfSpec spec) {
        return new ServantProperties(spec.health.get(), spec.strength.get(), spec.armor.get(), spec.block.get().floatValue()
                , spec.proj.get(), spec.magic.get(), spec.move.get(), spec.mana.get());
    }
}
