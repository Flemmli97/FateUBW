package io.github.flemmli97.fateubw.common.lib;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public class ItemTiers {

    public static final TierImpl EXCALIBUR = new TierImpl(1000, 0, 12, 10);
    public static final TierImpl INVIS_EXCALIBUR = new TierImpl(1000, 0, 8, 10);
    public static final TierImpl ENUMA_ELISH = new TierImpl(1100, 0, 14, 8);
    public static final TierImpl ARONDIGHT = new TierImpl(1300, 0, 12, 7);
    public static final TierImpl DAGGER = new TierImpl(950, 0, 7, 11);
    public static final TierImpl KUPRIOTS = new TierImpl(700, 0, 6.5f, 11);
    public static final TierImpl ASSASSIN_DAGGER = new TierImpl(550, 0, 6, 11);
    public static final TierImpl KATANA = new TierImpl(1000, 0, 11, 15);
    public static final TierImpl RULE_BREAKER = new TierImpl(400, 0, 4, 7);
    public static final TierImpl HERACLES_AXE = new TierImpl(666, 0, 17, 6);

    public static final TierImpl GAE_BOLG = new TierImpl(900, 0, 11, 14);
    public static final TierImpl GAE_BUIDHE = new TierImpl(900, 0, 9, 14);
    public static final TierImpl GAE_DEARG = new TierImpl(900, 0, 9.5f, 14);

    public static final TierImpl KANSHOU_BAKUYA = new TierImpl(650, 0, 7, 16);

    public static class TierImpl implements Tier {

        private final int uses, enchantability;
        private final float speed, damageBonus;
        private final TagKey<Block> incorrectBlocks;
        private final Ingredient repair;

        public TierImpl(int uses, float speed, float damageBonus, int enchantability) {
            this(uses, speed, damageBonus, enchantability, FateTags.Blocks.GENERIC_INCORRECT, Ingredient.EMPTY);
        }

        public TierImpl(int uses, float speed, float damageBonus, int enchantability, TagKey<Block> incorrectBlocks, Ingredient repair) {
            this.uses = uses;
            this.speed = speed;
            this.damageBonus = damageBonus;
            this.enchantability = enchantability;
            this.incorrectBlocks = incorrectBlocks;
            this.repair = repair;
        }

        @Override
        public int getUses() {
            return this.uses;
        }

        @Override
        public float getSpeed() {
            return this.speed;
        }

        @Override
        public float getAttackDamageBonus() {
            return this.damageBonus;
        }

        @Override
        public TagKey<Block> getIncorrectBlocksForDrops() {
            return this.incorrectBlocks;
        }

        @Override
        public int getEnchantmentValue() {
            return this.enchantability;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return this.repair;
        }
    }
}
