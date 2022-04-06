package io.github.flemmli97.fateubw.common.lib;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class ItemTiers {

    public static final TierImpl excalibur = new TierImpl(1000, 0, 8, 0, 10, Ingredient.EMPTY);
    public static final TierImpl invis_excalibur = new TierImpl(1000, 0, 4, 0, 10, Ingredient.EMPTY);
    public static final TierImpl ea = new TierImpl(1100, 0, 7.5f, 0, 8, Ingredient.EMPTY);
    public static final TierImpl arondight = new TierImpl(1300, 0, 8, 0, 7, Ingredient.EMPTY);
    public static final TierImpl dagger = new TierImpl(950, 0, 4, 0, 11, Ingredient.EMPTY);
    public static final TierImpl kupriots = new TierImpl(700, 0, 3.5f, 0, 11, Ingredient.EMPTY);
    public static final TierImpl assassinDagger = new TierImpl(550, 0, 5f, 0, 11, Ingredient.EMPTY);
    public static final TierImpl katana = new TierImpl(1000, 0, 8.5f, 0, 15, Ingredient.EMPTY);
    public static final TierImpl ruleBreaker = new TierImpl(300, 0, 1f, 0, 7, Ingredient.EMPTY);
    public static final TierImpl heraclesAxe = new TierImpl(666, 0, 13f, 0, 6, Ingredient.EMPTY);

    public static final TierImpl gae_bolg = new TierImpl(900, 0, 6.5f, 0, 14, Ingredient.EMPTY);
    public static final TierImpl gae_buidhe = new TierImpl(900, 0, 5f, 0, 14, Ingredient.EMPTY);
    public static final TierImpl gae_dearg = new TierImpl(900, 0, 5.5f, 0, 14, Ingredient.EMPTY);

    public static final TierImpl kanshou = new TierImpl(650, 0, 5, 0, 16, Ingredient.EMPTY);
    public static final TierImpl bakuya = new TierImpl(650, 0, 5f, 0, 16, Ingredient.EMPTY);

    public static class TierImpl implements Tier {

        private final int use, harvestLevel, ench;
        private final float eff, dmg;
        private final Ingredient repair;

        public TierImpl(int use, float eff, float dmg, int harvestLevel, int ench, Ingredient repair) {
            this.use = use;
            this.eff = eff;
            this.dmg = dmg;
            this.harvestLevel = harvestLevel;
            this.ench = ench;
            this.repair = repair;
        }

        @Override
        public int getUses() {
            return this.use;
        }

        @Override
        public float getSpeed() {
            return this.eff;
        }

        @Override
        public float getAttackDamageBonus() {
            return this.dmg;
        }

        @Override
        public int getLevel() {
            return this.harvestLevel;
        }

        @Override
        public int getEnchantmentValue() {
            return this.ench;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return this.repair;
        }
    }
}
