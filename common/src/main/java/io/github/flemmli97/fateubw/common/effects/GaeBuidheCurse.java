package io.github.flemmli97.fateubw.common.effects;

import io.github.flemmli97.fateubw.Fate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class GaeBuidheCurse extends MobEffect {

    public static final ResourceLocation MODIFIER = Fate.modRes("gae_buidhe_curse");

    public GaeBuidheCurse() {
        super(MobEffectCategory.HARMFUL, 0x480d0d);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, MODIFIER, -0.175, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, MODIFIER, -1.5, AttributeModifier.Operation.ADD_VALUE);
    }
}
