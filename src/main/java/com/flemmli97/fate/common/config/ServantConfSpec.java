package com.flemmli97.fate.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServantConfSpec {

    public final ForgeConfigSpec.ConfigValue<Double> health, strength, armor, proj, magic, move;
    public final ForgeConfigSpec.DoubleValue block;
    public final ForgeConfigSpec.IntValue mana, drop, looting;

    public ServantConfSpec(ForgeConfigSpec.Builder builder, String configCategory, double health, double strength, double armor, float projectileBlock,
                           double projectileArmor, double magicRes, double moveSpeed, int npMana, int dropChance, int lootingAdd) {
        builder.push(configCategory);
        this.health = builder.comment("Health of the servant").define("Health", health);
        this.strength = builder.comment("Attack damage of the servant").define("Strength", strength);
        this.armor = builder.comment("Generic armor of the servant").define("Armor", armor);
        this.block = builder.comment("Chance of blocking incoming projectiles").defineInRange("Block", projectileBlock, 0, 1);
        this.proj = builder.comment("Projectile armor of the servant").define("Projectile Prot", projectileArmor);
        this.magic = builder.comment("Magic protection").define("Magic Prot", magicRes);
        this.move = builder.comment("Move Speed").define("MoveSpeed", moveSpeed);
        this.mana = builder.comment("Nobel Phantasm mana usage").defineInRange("Nobel Phantasm Mana", npMana, 0, 100);
        this.drop = builder.comment("Item drop chance upon death. Looting adds 10*level% chance to it").defineInRange("Drop Chance", dropChance, 0, 100);
        this.looting = builder.comment("Chance per level of looting adding on to the drop chance").defineInRange("Looting", lootingAdd, 0, 100);
        builder.pop();
    }

}
