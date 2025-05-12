package io.github.flemmli97.fateubw.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import io.github.flemmli97.fateubw.client.model.BaseServantModel;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityTrailHolderProvider;
import io.github.flemmli97.fateubw.common.particles.trail.provider.entity.EntityTrailProvider;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.client.model.IItemArmModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class ItemTrailLayer<T extends LivingEntity & IAnimated & EntityTrailHolderProvider, M extends BaseServantModel<T> & IItemArmModel> extends RenderLayer<T, M> {

    private final RenderLayerParent<T, M> renderer;

    public ItemTrailLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
        this.renderer = renderer;
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource buffer, int light, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        AnimatedAction anim = entity.getAnimationHandler().getAnimation();
        if (anim != null && anim.isPast(EntityTrailProvider.TRAIL_START) && !anim.isPast(EntityTrailProvider.TRAIL_END)) {
            stack = this.renderer instanceof TrailPoseGetter getter ? getter.getPlainStack() : stack;
            Vec3[] data = this.calculatePosition(stack, entity, true);
            entity.getTrailHolder().recordData(anim.getID(), true, data[0], data[1], partialTicks);
            data = this.calculatePosition(stack, entity, false);
            entity.getTrailHolder().recordData(anim.getID(), false, data[0], data[1], partialTicks);
        }
    }

    protected Vec3[] calculatePosition(PoseStack stack, T entity, boolean left) {
        stack.pushPose();
        this.getParentModel().transform(left ? HumanoidArm.LEFT : HumanoidArm.RIGHT, stack);
        stack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
        stack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        this.getParentModel().postTransform(left, stack);
        stack.mulPose(Vector3f.XP.rotationDegrees(90.0F));

        Vector4f[] edge = entity.weaponTrailEdge(left);
        Vector4f start = edge[0];
        Vector4f end = edge[1];
        Matrix4f last = stack.last().pose();
        start.transform(last);
        end.transform(last);
        Vec3 normal = new Vec3(end.x() - start.x(), end.y() - start.y(), end.z() - start.z());
        stack.popPose();
        return new Vec3[]{new Vec3(start.x() + normal.x(), start.y() + normal.y(), start.z() + normal.z()), normal};
    }
}
