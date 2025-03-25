package io.github.flemmli97.fateubw.common.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class GaeBuidheCurse extends MobEffect {

    public GaeBuidheCurse() {
        super(MobEffectCategory.HARMFUL, 0x480d0d);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "0848c047-a139-4ecc-93b6-691828c7c2ec", -0.2, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "33d79e42-0845-47c0-ba49-95f590d11799", -2, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, AttributeModifier modifier) {
        return modifier.getAmount() * (amplifier * 0.5 + 1);
    }
}
