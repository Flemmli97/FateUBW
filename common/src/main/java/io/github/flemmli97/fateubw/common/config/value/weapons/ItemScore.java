package io.github.flemmli97.fateubw.common.config.value.weapons;

import java.util.Collection;

public record ItemScore(double durability, double damage, double enchantmentValue) {

    public static ScoreRange[] composite(Collection<ItemScore> itemScores) {
        ScoreRange min = null;
        ScoreRange max = null;
        for (ItemScore itemScore : itemScores) {
            if (min == null)
                min = new ScoreRange(itemScore, true);
            else
                min.update(itemScore);
            if (max == null)
                max = new ScoreRange(itemScore, false);
            else
                max.update(itemScore);
        }
        return new ScoreRange[]{min, max};
    }

    public static class ScoreRange {

        private final boolean min;
        private double durability, damage, enchantmentValue;

        public ScoreRange(ItemScore score, boolean min) {
            this.min = min;
            this.durability = score.durability();
            this.damage = score.damage();
            this.enchantmentValue = score.enchantmentValue();
        }

        private void update(ItemScore score) {
            if (this.min) {
                this.durability = Math.min(this.damage, score.durability());
                this.damage = Math.min(this.damage, score.damage());
                this.enchantmentValue = Math.min(this.enchantmentValue, score.enchantmentValue());
                return;
            }
            this.durability = Math.max(this.durability, score.durability());
            this.damage = Math.max(this.damage, score.damage());
            this.enchantmentValue = Math.max(this.enchantmentValue, score.enchantmentValue());
        }

        public double durability() {
            return this.durability;
        }

        public double damage() {
            return this.damage;
        }

        public double enchantmentValue() {
            return this.enchantmentValue;
        }

        @Override
        public String toString() {
            return "ScoreRange{" +
                    "min=" + this.min +
                    ", durability=" + this.durability +
                    ", damage=" + this.damage +
                    ", enchantmentValue=" + this.enchantmentValue +
                    '}';
        }
    }
}
