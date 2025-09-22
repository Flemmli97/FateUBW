package io.github.flemmli97.fateubw.common.effects;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.registry.FateAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class RuleBreakerCurse extends MobEffect {

    public static final ResourceLocation MODIFIER = Fate.modRes("rule_breaker_effect");

    public RuleBreakerCurse() {
        super(MobEffectCategory.HARMFUL, 0x4f1f7f);
        this.addAttributeModifier(FateAttributes.MANA_REGEN.asHolder(), MODIFIER, -1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(FateAttributes.MANA_LEECH.asHolder(), MODIFIER, -1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(FateAttributes.MAGIC_RESISTANCE.asHolder(), MODIFIER, -1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
