package com.flemmli97.fate.common.lib;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;

public class ItemTiers {

    public static final TierImpl excalibur = new TierImpl(1000, 0, 8, 0, 10, null);
    public static final TierImpl invis_excalibur = new TierImpl(1000, 0, 4, 0, 10, null);
    public static final TierImpl ea = new TierImpl(1100, 0, 7.5f, 0, 8, null);
    public static final TierImpl arondight = new TierImpl(1300, 0, 8, 0, 7, null);

    public static final TierImpl gae_bolg = new TierImpl(900, 0, 6.5f, 0, 14, null);
    public static final TierImpl gae_buidhe = new TierImpl(900, 0, 5f, 0, 14, null);
    public static final TierImpl gae_dearg = new TierImpl(900, 0, 5.5f, 0, 14, null);

    public static final TierImpl kanshou = new TierImpl(650, 0, 5, 0, 16, null);
    public static final TierImpl bakuya = new TierImpl(650, 0, 5f, 0, 16, null);

    public static class TierImpl implements IItemTier {

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
        public int getMaxUses() {
            return this.use;
        }

        @Override
        public float getEfficiency() {
            return this.eff;
        }

        @Override
        public float getAttackDamage() {
            return this.dmg;
        }

        @Override
        public int getHarvestLevel() {
            return this.harvestLevel;
        }

        @Override
        public int getEnchantability() {
            return this.ench;
        }

        @Override
        public Ingredient getRepairMaterial() {
            return this.repair;
        }
    }
}
