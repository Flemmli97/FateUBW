package io.github.flemmli97.fate.client.render.misc;

import com.flemmli97.tenshilib.client.render.RenderProjectileItem;
import com.flemmli97.tenshilib.client.render.RenderUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.common.entity.EntityBabylonWeapon;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderBabylon extends RenderProjectileItem<EntityBabylonWeapon> {

    public final ResourceLocation babylonIddle = new ResourceLocation(Fate.MODID, "textures/entity/babylon.png");
    private final RenderUtils.TextureBuilder textureBuilder = new RenderUtils.TextureBuilder();

    public RenderBabylon(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public void render(EntityBabylonWeapon projectile, float rotation, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int packedLight) {
        if (projectile.iddle) {
            stack.push();
            RenderUtils.applyYawPitch(stack, MathHelper.lerp(partialTicks, projectile.prevRotationYaw, projectile.rotationYaw),
                    MathHelper.lerp(partialTicks, projectile.prevRotationPitch, projectile.rotationPitch));
            float ripple = MathHelper.sin((projectile.ticksExisted + projectile.renderRand) / 2f) * 0.025f + 1;
            float size = (float) (1.45 * ripple);
            this.textureBuilder.setLight(packedLight);
            RenderUtils.renderTexture(stack, buffer.getBuffer(RenderType.getEntityCutoutNoCull(this.babylonIddle)), size, size, this.textureBuilder);
            stack.pop();
        }
        super.render(projectile, rotation, partialTicks, stack, buffer, packedLight);
    }

    @Override
    public ItemStack getRenderItemStack(EntityBabylonWeapon entity) {
        return entity.getWeapon();
    }

    @Override
    public Type getRenderType(EntityBabylonWeapon entity) {
        return Type.WEAPON;
    }
}