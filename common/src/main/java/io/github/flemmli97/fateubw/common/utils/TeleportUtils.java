package io.github.flemmli97.fateubw.common.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class TeleportUtils {

    public static boolean safeDimensionTeleport(Mob entity, ServerLevel newLevel, BlockPos target) {
        BlockPos safe = null;
        for (int i = 0; i < 10; ++i) {
            int x = entity.getRandom().nextInt(-3, 3);
            int y = entity.getRandom().nextInt(-2, 3);
            int z = entity.getRandom().nextInt(-3, 3);
            BlockPos pos = isSafePos(entity, newLevel, target.offset(x, y, z), s -> true);
            if (pos != null) {
                safe = pos;
                break;
            }
        }
        if (safe == null)
            return false;
        float yaw = entity.getYRot();
        float pitch = entity.getXRot();
        entity.unRide();
        Entity old = entity;
        entity = (Mob) old.getType().create(newLevel);
        if (entity != null) {
            entity.restoreFrom(old);
            entity.moveTo(safe.getX(), safe.getY(), safe.getZ(), yaw, pitch);
            old.setRemoved(Entity.RemovalReason.CHANGED_DIMENSION);
            newLevel.addDuringTeleport(entity);
        } else
            return false;
        newLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.MASTER, 1, 1);
        for (int i = 0; i < 32; ++i) {
            newLevel.sendParticles(ParticleTypes.PORTAL, entity.getX(), entity.getY() + newLevel.random.nextDouble() * 2.0, entity.getZ(), 0, newLevel.random.nextGaussian(), 0.0, newLevel.random.nextGaussian(), 1);
        }
        return true;
    }

    public static BlockPos isSafePos(Mob entity, Level level, BlockPos pos, Predicate<BlockState> validPos) {
        PathType blockPathTypes = entity.getNavigation().getNodeEvaluator().getPathType(new PathfindingContext(entity.level(), entity), pos.getX(), pos.getY(), pos.getZ());
        if (blockPathTypes == PathType.OPEN) {
            if (!entity.isNoGravity())
                return null;
        } else if (blockPathTypes != PathType.WALKABLE) {
            return null;
        }
        BlockState blockState = level.getBlockState(pos.below());
        if (!validPos.test(blockState)) {
            return null;
        }
        for (VoxelShape voxelShape : level.getBlockCollisions(entity, entity.getBoundingBox()
                .move(pos.getX() + 0.5 - entity.getX(), pos.getY() - entity.getY(), pos.getZ() + 0.5 - entity.getZ()))) {
            if (!voxelShape.isEmpty())
                return null;
        }
        return pos;
    }

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

    private static double randomUniform(RandomSource random) {
        return random.nextDouble() - 0.5;
    }
}
