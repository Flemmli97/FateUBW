package io.github.flemmli97.fateubw.common.utils;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class TeleportUtils {

    public static void teleportTo(LivingEntity entity, double x, double y, double z, @Nullable SoundEvent soundEvent, @Nullable ParticleOptions particle) {
        Vec3 prev = entity.position();
        entity.teleportTo(x, y, z);
        if (soundEvent != null) {
            entity.level().playSound(null, entity.xo, entity.yo, entity.zo, soundEvent, entity.getSoundSource(), 1.0F, 1.0F);
            entity.playSound(soundEvent, 1.0F, 1.0F);
        }
        if (particle != null) {
            for (int i = 0; i < 10; i++) {
                if (entity.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(particle,
                            prev.x() + entity.getBbWidth() * TeleportUtils.randomUniform(entity.getRandom()), prev.y() + entity.getBbHeight() * TeleportUtils.randomUniform(entity.getRandom()), prev.z() + entity.getBbWidth() * TeleportUtils.randomUniform(entity.getRandom())
                            , 0,
                            TeleportUtils.randomUniform(entity.getRandom()) * 0.1, TeleportUtils.randomUniform(entity.getRandom()) * 0.1, TeleportUtils.randomUniform(entity.getRandom()) * 0.1,
                            1);
                } else {
                    entity.level().addParticle(particle,
                            prev.x() + entity.getBbWidth() * TeleportUtils.randomUniform(entity.getRandom()), prev.y() + entity.getBbHeight() * TeleportUtils.randomUniform(entity.getRandom()), prev.z() + entity.getBbWidth() * TeleportUtils.randomUniform(entity.getRandom()),
                            TeleportUtils.randomUniform(entity.getRandom()) * 0.1, TeleportUtils.randomUniform(entity.getRandom()) * 0.1, TeleportUtils.randomUniform(entity.getRandom()) * 0.1);
                }
            }
            for (int i = 0; i < 10; i++) {
                if (entity.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(particle, entity.getRandomX(0.5), entity.getRandomY(), entity.getRandomZ(0.5),
                            0,
                            TeleportUtils.randomUniform(entity.getRandom()) * 0.1, TeleportUtils.randomUniform(entity.getRandom()) * 0.1, TeleportUtils.randomUniform(entity.getRandom()) * 0.1,
                            1);
                } else {
                    entity.level().addParticle(particle, entity.getRandomX(0.5), entity.getRandomY(), entity.getRandomZ(0.5),
                            TeleportUtils.randomUniform(entity.getRandom()) * 0.1, TeleportUtils.randomUniform(entity.getRandom()) * 0.1, TeleportUtils.randomUniform(entity.getRandom()) * 0.1);
                }
            }
        }
    }

    public static boolean safeTeleportTo(LivingEntity entity, ServerLevel level, Predicate<LivingEntity> teleporter) {
        Vec3 currentPos = entity.position();
        ServerLevel currentLevel = (ServerLevel) entity.level();
        if (!entity.level().dimension().equals(level.dimension())) {
            Entity success = entity.changeDimension(new DimensionTransition(level,
                    new Vec3(currentPos.x(), currentPos.y(), currentPos.z()),
                    Vec3.ZERO, entity.getYRot(), entity.getXRot(), DimensionTransition.DO_NOTHING));
            if (success != null) {
                entity = (LivingEntity) success;
            }
        }
        boolean success = teleporter.test(entity);
        if (!success) {
            entity.changeDimension(new DimensionTransition(currentLevel,
                    new Vec3(currentPos.x(), currentPos.y(), currentPos.z()),
                    Vec3.ZERO, entity.getYRot(), entity.getXRot(), DimensionTransition.DO_NOTHING));
            return true;
        }
        return false;
    }

    private static double randomUniform(RandomSource random) {
        return random.nextDouble() - 0.5;
    }
}
