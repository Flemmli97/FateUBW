package io.github.flemmli97.fateubw.mixinhelper;

import com.mojang.math.Matrix4f;
import net.minecraft.world.phys.Vec3;

public interface Matrix4fTransformer {

    static Vec3 transformVec(Vec3 vec3, Matrix4f matrix4f) {
        return ((Matrix4fTransformer) (Object) matrix4f).fateubw$transform(vec3);
    }

    Vec3 fateubw$transform(Vec3 vec3);
}
