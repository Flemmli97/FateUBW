package io.github.flemmli97.fateubw.common.effects;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.tenshilib.common.effect.ExtendedMobEffect;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.block.Blocks;

public class PetrificationEffect extends MobEffect implements ExtendedMobEffect {

    public static int MAX_PROGRESS = 2;

    public static final ResourceLocation MODIFIER = Fate.modRes("petrification_effect");

    public PetrificationEffect() {
        super(MobEffectCategory.HARMFUL, 0x606060);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, MODIFIER, -0.35, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.JUMP_STRENGTH, MODIFIER, -0.35, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, MODIFIER, -0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public void onEffectRemoved(LivingEntity entity, MobEffectInstance instance) {
        if (entity.level() instanceof ServerLevel level) {
            for (int i = 0; i < 30; i++) {
                level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.STONE.defaultBlockState()),
                        entity.getRandomX(1), entity.getRandomY(), entity.getRandomZ(1),
                        0,
                        0,
                        0,
                        0, 1);
            }
            entity.playSound(SoundEvents.STONE_BREAK, 2, 1);
            if (instance.getAmplifier() >= PetrificationEffect.MAX_PROGRESS) {
                entity.invulnerableTime = 0;
                if (entity.hurt(FateDamageTypes.create(FateDamageTypes.PETRIFICATION, entity.registryAccess()), entity.getMaxHealth() * 0.15f)) {
                    entity.invulnerableTime = 0;
                }
            }
        }
    }
}
