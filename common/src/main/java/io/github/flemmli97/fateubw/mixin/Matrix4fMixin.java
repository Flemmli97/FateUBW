package io.github.flemmli97.fateubw.mixin;

import com.mojang.math.Matrix4f;
import io.github.flemmli97.fateubw.mixinhelper.Matrix4fTransformer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Matrix4f.class)
public abstract class Matrix4fMixin implements Matrix4fTransformer {

    @Shadow
    protected float m00;
    @Shadow
    protected float m01;
    @Shadow
    protected float m02;
    @Shadow
    protected float m03;
    @Shadow
    protected float m10;
    @Shadow
    protected float m11;
    @Shadow
    protected float m12;
    @Shadow
    protected float m13;
    @Shadow
    protected float m20;
    @Shadow
    protected float m21;
    @Shadow
    protected float m22;
    @Shadow
    protected float m23;

    @Override
    public Vec3 transform(Vec3 vec3) {
        double f = vec3.x;
        double g = vec3.y;
        double h = vec3.z;
        double i = 1;
        return new Vec3(this.m00 * f + this.m01 * g + this.m02 * h + this.m03 * i,
                this.m10 * f + this.m11 * g + this.m12 * h + this.m13 * i,
                this.m20 * f + this.m21 * g + this.m22 * h + this.m23 * i);
    }
}
