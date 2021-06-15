package io.github.flemmli97.fate.client.model;

import com.flemmli97.tenshilib.common.utils.MathUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelEA extends Model {

    private final ModelRenderer base;
    private final ModelRenderer handle;
    private final ModelRenderer handleEnd;
    private final ModelRenderer blade;
    private final ModelRenderer blade1;
    private final ModelRenderer blade2;
    private final ModelRenderer blade3;
    private final ModelRenderer backbone;
    private final ModelRenderer guard;
    private final ModelRenderer guardTop;
    private final ModelRenderer guardBottom;

    public ModelEA() {
        super(RenderType::getEntitySolid);
        textureWidth = 24;
        textureHeight = 24;

        base = new ModelRenderer(this);
        base.setRotationPoint(-1.0F, 16.0F, 1.0F);


        handle = new ModelRenderer(this);
        handle.setRotationPoint(0.5F, 5.4F, -1.2F);
        base.addChild(handle);
        handle.setTextureOffset(0, 3).addBox(-0.5F, -3.4F, 0.2F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        handleEnd = new ModelRenderer(this);
        handleEnd.setRotationPoint(0.0F, 0.0F, 0.0F);
        handle.addChild(handleEnd);
        setRotationAngle(handleEnd, -0.3927F, 0.0F, 0.0F);
        handleEnd.setTextureOffset(0, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        blade = new ModelRenderer(this);
        blade.setRotationPoint(0.0F, 0.0F, 0.0F);
        base.addChild(blade);


        blade1 = new ModelRenderer(this);
        blade1.setRotationPoint(0.5F, -7.5F, -0.5F);
        blade.addChild(blade1);
        setRotationAngle(blade1, 0.0F, -0.7854F, 0.0F);
        blade1.setTextureOffset(8, 3).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);

        blade2 = new ModelRenderer(this);
        blade2.setRotationPoint(0.5F, -4.5F, -0.5F);
        blade.addChild(blade2);
        blade2.setTextureOffset(0, 8).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, true);

        blade3 = new ModelRenderer(this);
        blade3.setRotationPoint(0.5F, -1.5F, -0.5F);
        blade.addChild(blade3);
        setRotationAngle(blade3, 0.0F, -0.7854F, 0.0F);
        blade3.setTextureOffset(8, 8).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);

        backbone = new ModelRenderer(this);
        backbone.setRotationPoint(0.5F, -6.5F, -0.5F);
        blade.addChild(backbone);
        backbone.setTextureOffset(16, 0).addBox(-0.5F, -3.5F, -0.5F, 1.0F, 7.0F, 1.0F, 0.0F, true);

        guard = new ModelRenderer(this);
        guard.setRotationPoint(0.0F, 0.0F, 0.0F);
        base.addChild(guard);
        guard.setTextureOffset(0, 13).addBox(-0.5F, 0.0F, -1.5F, 2.0F, 2.0F, 3.0F, 0.0F, true);

        guardTop = new ModelRenderer(this);
        guardTop.setRotationPoint(0.0F, 1.2F, -1.8F);
        guard.addChild(guardTop);
        setRotationAngle(guardTop, 0.3927F, 0.0F, 0.0F);
        guardTop.setTextureOffset(0, 18).addBox(-1.0F, -1.0F, 0.0F, 3.0F, 1.0F, 4.0F, 0.0F, true);

        guardBottom = new ModelRenderer(this);
        guardBottom.setRotationPoint(0.0F, 2.0F, -2.5F);
        guard.addChild(guardBottom);
        setRotationAngle(guardBottom, -0.3927F, 0.0F, 0.0F);
        guardBottom.setTextureOffset(10, 13).addBox(-1.0F, -1.0F, 0.0F, 3.0F, 1.0F, 4.0F, 0.0F, true);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        base.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void setBase(boolean flag) {
        this.backbone.showModel = flag;
        this.blade1.showModel = !flag;
        this.blade2.showModel = !flag;
        this.blade3.showModel = !flag;
        this.handle.showModel = flag;
        this.guard.showModel = flag;
    }

    public void spinBlade(int tick, float partialTicks) {
        float angle = (tick + partialTicks) * 10 % 360;
        this.blade1.rotateAngleY = MathUtils.degToRad(angle);
        this.blade2.rotateAngleY = -MathUtils.degToRad(angle);
        this.blade3.rotateAngleY = MathUtils.degToRad(angle + 90);
    }
}
