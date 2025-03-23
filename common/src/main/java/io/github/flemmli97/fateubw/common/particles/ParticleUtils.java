package io.github.flemmli97.fateubw.common.particles;

import io.github.flemmli97.fateubw.common.utils.MathsHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class ParticleUtils {

    public static void createSlashTrailParticle(LivingEntity entity, float radius, float startAngle, float endAngle, float roll) {
        float r = (235 + entity.getRandom().nextInt(10)) / 255F;
        float g = (235 + entity.getRandom().nextInt(10)) / 255F;
        float b = 245 / 255F;
        float targetYRot = MathsHelper.YRotFrom(entity.getLookAngle());
        Vec3 pos = entity.getEyePosition();
        radius = 2;
        roll = 15;
//        entity.level.addParticle(new TrailParticleData(ModParticles.TRAIL.get(), TrailInfo.builder(new Vec3(0, 0, radius), new Vec3(0, 0, -radius))
//                        .setControlPoint(new Vec3(radius, 0, 0))
//                        .setColor(r, g, b, 0.6f)
//                        .setColor2(r, g, b, 0.6f)
//                        .rotateBy(-targetYRot - 90, 0, roll)
//                        .duration(5)
//                        .setScale(1)
//                        .setScale2(1)
//                        .build()),
//                pos.x(), pos.y(), pos.z(), 0, 0, 0);
    }
}
