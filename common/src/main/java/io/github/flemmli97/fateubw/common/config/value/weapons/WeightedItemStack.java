package io.github.flemmli97.fateubw.common.config.value.weapons;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.TestOnly;

public class WeightedItemStack {

    private final ItemStack stack;
    private final double weight;
    private final double quality;

    public WeightedItemStack(ItemStack stack, double itemWeight, double quality) {
        this.stack = stack;
        this.weight = itemWeight;
        this.quality = quality;
    }

    public ItemStack getItem() {
        return this.stack.copy();
    }

    public int getWeight(double modifier) {
        return (int) Math.ceil(Math.max(this.weight + modifier * this.quality, 0));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof WeightedItemStack other) {
            return ItemStack.matches(this.stack, other.stack);
        }
        return false;
    }

    @TestOnly
    ProbabiltyEntry withWeight(float probability) {
        return new ProbabiltyEntry(this.stack, probability, this.quality);
    }

    @Override
    public String toString() {
        return String.format("Stack: %s; Weight: %s, Quality: %s", this.stack, this.weight, this.quality);
    }

    record ProbabiltyEntry(ItemStack stack, double probability, double quality) {

        public static final Codec<ProbabiltyEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                ItemStack.CODEC.fieldOf("value").forGetter(ProbabiltyEntry::stack),
                Codec.DOUBLE.fieldOf("probability").forGetter(ProbabiltyEntry::probability),
                Codec.DOUBLE.fieldOf("quality").forGetter(ProbabiltyEntry::quality)
        ).apply(inst, ProbabiltyEntry::new));

    }
}
