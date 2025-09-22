package io.github.flemmli97.fateubw.common.effects;

import io.github.flemmli97.fateubw.Fate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class GravityEffect extends MobEffect {

    public static final ResourceLocation MODIFIER = Fate.modRes("gravity_effect");

    public GravityEffect() {
        super(MobEffectCategory.HARMFUL, 0x4f1f7f);
        this.addAttributeModifier(Attributes.GRAVITY, MODIFIER, -0.04, AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        entity.setDeltaMovement(entity.getDeltaMovement().add(0, -0.1 * (amplifier + 1), 0));
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
